package com.android.zd112.ui.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.zd112.R;
import com.android.zd112.data.AppInfoData;
import com.android.zd112.ui.adapter.CommAdapter;
import com.android.zd112.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class InstallAppActivity extends BaseActivity {

    private ListView installedAppList;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.activity_install_app);
        topView("应用管理");
        installedAppList = viewId(R.id.installedAppList);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        requestTips(LOADING);
        new CurrAsyncTask().execute();
    }

    class CurrAsyncTask extends AsyncTask {

        @Override
        protected List<AppInfoData> doInBackground(Object[] objects) {
            return appInfo();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            requestTips(LOADING_FINISH);
            showView((List<AppInfoData>) o);
        }
    }

    private void showView(List<AppInfoData> infoDataList) {
        installedAppList.setAdapter(new CommAdapter<AppInfoData>(InstallAppActivity.this, infoDataList, R.layout.activity_install_app_item) {
            @Override
            protected void convertView(int position,View item, final AppInfoData appInfoData) {
                LogUtils.e("-----------adapter");
                ImageView installAppIcon = get(item, R.id.installAppIcon);
                TextView installAppName = get(item, R.id.installAppName);
                TextView installAppPkg = get(item, R.id.installAppPkg);
                get(item, R.id.installAppInstall).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uninstall(appInfoData.pkgName);
                    }
                });
                installAppIcon.setImageDrawable(appInfoData.icon);
                installAppName.setText(appInfoData.name);
                installAppPkg.setText(appInfoData.pkgName);
            }
        });
    }

    public List<AppInfoData> appInfo() {
        List<AppInfoData> appInfos = new ArrayList<>();
        PackageManager packageManager = this.getApplication().getPackageManager();
        List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo applicationInfo : applicationInfos) {
            if (applicationInfo == null) {
                continue;
            }
            AppInfoData appInfoData = new AppInfoData();
            appInfoData.icon = applicationInfo.loadIcon(packageManager);
            appInfoData.name = (String) packageManager.getApplicationLabel(applicationInfo);
            appInfoData.pkgName = applicationInfo.packageName;
            String channelName = getAppMetaData(this, applicationInfo.packageName, "UMENG_CHANNEL");
            if (TextUtils.isEmpty(channelName)) {
                continue;
            }
            appInfoData.channel = channelName;

            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo(appInfoData.pkgName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (packageInfo != null) {
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                appInfoData.localSign = packageInfo.applicationInfo.flags;
                appInfoData.version = packageInfo.versionName;
                appInfoData.code = packageInfo.versionCode;
            }
            appInfoData.targetSdkVersion = applicationInfo.targetSdkVersion;
            appInfoData.minSdkVersion = applicationInfo.minSdkVersion;
            appInfos.add(appInfoData);
            LogUtils.e("----------info:", appInfoData.toString());
        }
        LogUtils.e("------------size:", appInfos.size());
        return appInfos;
    }

    /**
     * 获取app当前的渠道号或application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context, String pkgName, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
    }

    private void uninstall(String pkgName) {
        LogUtils.e("----------pkgName:", pkgName);
        Intent intent = new Intent(this, this.getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PackageInstaller packageInstaller = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            packageInstaller = getPackageManager().getPackageInstaller();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            packageInstaller.uninstall(pkgName, pendingIntent.getIntentSender());
        }
        LogUtils.e("-----2-----pkgName:", pkgName);
    }

}
