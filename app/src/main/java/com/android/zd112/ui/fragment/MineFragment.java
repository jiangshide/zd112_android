package com.android.zd112.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.zd112.R;
import com.android.zd112.data.FileData;
import com.android.zd112.ui.MainActivity;
import com.android.zd112.ui.activity.MsgActivity;
import com.android.zd112.ui.activity.QrCardActivity;
import com.android.zd112.ui.activity.SettingActivity;
import com.android.zd112.ui.view.CircleImageView;
import com.android.zd112.ui.view.sticky.StickyViewHelper;
import com.android.zd112.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by etongdai on 2017/11/17.
 */

public class MineFragment extends BaseFragment {

    private ImageView mineLoginQr;
    private CircleImageView headIcon;
    private Button mineCameraBtn, mineAlbumBtn;

    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    //调用照相机返回图片文件
    private File tempFile;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.tab_fragment_mine);
        topView(R.mipmap.msg, "个人中心", R.mipmap.setting);
        mineLoginQr = viewId(R.id.mineLoginQr);
        headIcon = viewId(R.id.headIcon);
    }

    @Override
    protected void setListener() {
        headIcon.setOnClickListener(this);
        mineLoginQr.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
//        Glide.with(this).load("http://10.20.7.21:8089/static/app/qrImgUrl/236a4c32aaccbb1b9_qr.png").transition(new GenericTransitionOptions<Drawable>()).into(headIconImg);
    }

    public void upload(byte[] data) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), data);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", "image.jpg", requestBody);
        mNetApi.upload(part).enqueue(new Callback<FileData>() {
            @Override
            public void onResponse(Call<FileData> call, Response<FileData> response) {
                LogUtils.e("---------------version:", response.body().res);
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(tempUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                headIcon.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Call<FileData> call, Throwable t) {

            }
        });
    }

    public void upload(String... fileUrls) {
        Map<String, RequestBody> files = new HashMap<>();
        for (int i = 0; i < fileUrls.length; i++) {
            files.put("file" + i + "\"; filename=\"icon.png", RequestBody.create(MediaType.parse("multipart/form-data"), new File(fileUrls[i])));
        }
        mNetApi.upload(files).enqueue(new Callback<FileData>() {
            @Override
            public void onResponse(Call<FileData> call, Response<FileData> response) {
                LogUtils.e("----------file-res:", response.body().res);
            }

            @Override
            public void onFailure(Call<FileData> call, Throwable t) {
                LogUtils.e("----------t:", t);
            }
        });
    }

    private void sticky() {
        StickyViewHelper stickyViewHelper = new StickyViewHelper(getActivity(), mRedDotTxt, R.layout.includeview);
//        stickyViewHelper.setViewOutRangeMoveRun(new Runnable() {
//            @Override
//            public void run() {
//                show("ViewOutRangeMove");
//            }
//        });
//        stickyViewHelper.setViewInRangeMoveRun(new Runnable() {
//            @Override
//            public void run() {
//                show("ViewInRangeMove");
//            }
//        });
//        stickyViewHelper.setViewInRangeUpRun(new Runnable() {
//            @Override
//            public void run() {
//                show("ViewInRangeUp");
//            }
//        });
//        stickyViewHelper.setViewOutRangeUpRun(new Runnable() {
//            @Override
//            public void run() {
//                show("ViewOutRangeUp");
//            }
//        });
//        stickyViewHelper.setViewOut2InRangeUpRun(new Runnable() {
//            @Override
//            public void run() {
//                show("ViewOut2InRangeUp");
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topLeftBtn:
                startActivity(new Intent(getActivity(), MsgActivity.class));
                break;
            case R.id.topRightBtn:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.headIcon:
                upload("/storage/emulated/0/1521618604819.jpg", "/storage/emulated/0/1521618453130.jpg", "/storage/emulated/0/1520992388095.jpg", "/storage/emulated/0/1520991544217.jpg");
                showDialog(getContext(), R.style.BottomDialog, R.layout.dialog_photo).setGravity(Gravity.BOTTOM).setAnim(R.style.BottomDialog_Animation).setOutside(true).show();
                break;
            case R.id.mineLoginQr:
                startActivity(new Intent(getActivity(), QrCardActivity.class));
                break;
            case R.id.mineCameraBtn:
                getPicFromCamera();
                break;
            case R.id.mineAlbumBtn:
                getPicFromAlbm();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        LogUtils.e("--------------requestCode:", requestCode, " | resultCode:", resultCode, " | intent:", intent);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == MainActivity.RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.android.zd112", tempFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(tempFile));
                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == MainActivity.RESULT_OK) {
                    Uri uri = intent.getData();
                    cropPhoto(uri);
                }
                break;
            case CROP_REQUEST_CODE:     //调用剪裁后返回
                try {
                    upload(getBytes(getActivity().getContentResolver().openInputStream(tempUri)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.android.zd112", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        LogUtils.e("-------------tempFile:", tempFile);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    /**
     * 裁剪图片
     */
    Uri tempUri;

    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
//        intent.putExtra("return-data", true);
        tempUri = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    @Override
    public void onView(View view) {
        super.onView(view);
        mineCameraBtn = view.findViewById(R.id.mineCameraBtn);
        mineAlbumBtn = view.findViewById(R.id.mineAlbumBtn);
        mineCameraBtn.setOnClickListener(this);
        mineAlbumBtn.setOnClickListener(this);
    }
}
