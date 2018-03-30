package com.android.zd112.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.zd112.R;
import com.android.zd112.ui.view.qrcode.codec.QRCodeEncoder;
import com.android.zd112.ui.view.qrcode.core.QRCodeUtil;

/**
 * Created by etongdai on 2018/3/20.
 */

public class QrCardActivity extends BaseActivity{

    private ImageView qrCardImg;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.tab_fragment_mine_qrcard);
        qrCardImg = viewId(R.id.qrCardImg);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        createChineseQRCodeWithLogo();
    }

    private void createChineseQRCodeWithLogo() {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitmap = BitmapFactory.decodeResource(QrCardActivity.this.getResources(), R.mipmap.ic_launcher);
                return QRCodeEncoder.syncEncodeQRCode("蒋世德", QRCodeUtil.dp2px(QrCardActivity.this, 150), Color.parseColor("#ff0000"), logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    qrCardImg.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(QrCardActivity.this, "生成带logo的中文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
