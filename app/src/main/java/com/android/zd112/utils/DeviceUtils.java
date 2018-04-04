package com.android.zd112.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.android.zd112.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by etongdai on 2017/12/26.
 */
public class DeviceUtils {

    /**
     * 获取屏幕分辨率
     * width:point.x
     * height:point.y
     */
    public static Point getDisyplay(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    /**
     * 获取屏幕尺寸
     */
    public static void getWindow(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        int density = displayMetrics.densityDpi;
        double realW = (double) width / (double) density;
        double realH = (double) height / (double) density;
        double x = Math.pow(realW, 2);
        double y = Math.pow(realH, 2);
        double screenInches = Math.sqrt(x + y);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static HashMap<String, Object> Info(Context context) {
//        StringBuffer deviceInfo = new StringBuffer();
        HashMap<String, Object> deviceInfo = new HashMap<>();

        try {
            Class localClass = Class.forName("android.os.SystemProperties");
            Object localObject1 = localClass.newInstance();
            Object localObject2 = localClass.getMethod("get", new Class[]{String.class, String.class}).invoke(localObject1, new Object[]{"gsm.version.baseband", "no message"});
            Object localObject3 = localClass.getMethod("get", new Class[]{String.class, String.class}).invoke(localObject1, new Object[]{"ro.build.display.id", ""});
            deviceInfo.put("localObject2", localObject2);//基带版本
            deviceInfo.put("localObject3", localObject3);//内核版本
        } catch (Exception e) {
            LogUtils.e(e);
        }

        NetworkInfo netInfo = getNetInfo(context);
        if (netInfo != null) {
            int netType = netInfo.getType();
            String netTypeName = netInfo.getTypeName();
            deviceInfo.put("netType", netType);//联网方式
            deviceInfo.put("netTypeName", netTypeName);//网络名称
        }

        TelephonyManager phone = getPhone(context);
        String devicesId = phone.getDeviceId();//序列号IMEI:根据不同的手机设备返回IMEI，MEID或者ESN码，可以根据以下代码获得:非手机设备：最开始搭载Android系统都手机设备，而现在也出现了非手机设备：如平板电脑、电视、音乐播放器等。这些设备没有通话的硬件功能，系统中也就没有TELEPHONY_SERVICE，自然也就无法通过上面的方法获得DEVICE_ID;权限问题：获取DEVICE_ID需要READ_PHONE_STATE权限，在Android 6.0上使用运行时动态授予权限的机制，一旦用户不给予授权，将获取不到DEVICE_ID;厂商定制系统中的Bug：少数手机设备上，由于该实现有漏洞，会返回垃圾。
        String deviceSoftwareVersion = phone.getDeviceSoftwareVersion();//返回系统版本
        String subscriberId = phone.getSubscriberId();//IMSI
        String line1Number = phone.getLine1Number();//手机号码
        String simSerialNumber = phone.getSimSerialNumber();//手机卡序列号
        String simOperator = phone.getSimOperator();//运营商
        String simOperatorName = phone.getSimOperatorName();//运营商名字
        String simCountryIso = phone.getSimCountryIso();//手机卡所属国家
        int networkType = phone.getNetworkType();//网络类型
        String networkCountryIso = phone.getNetworkCountryIso();//国家ISO代码
        String networkOperator = phone.getNetworkOperator();//网络运营商类型:返回MCC+MNC代码=>(SIM卡运营商国家代码和运营商网络代码)(IMSI) 46001
        String networkOperatorName = phone.getNetworkOperatorName();//网络类型名:返回移动网络运营商的名字(SPN)=>中国电信
        int phoneType = phone.getPhoneType();//手机卡类型
        int simState = phone.getSimState();//手机卡状态

        deviceInfo.put("devicesId", devicesId);
        deviceInfo.put("deviceSoftwareVersion", deviceSoftwareVersion);
        deviceInfo.put("subscriberId", subscriberId);
        deviceInfo.put("line1Number", line1Number);
        deviceInfo.put("simSerialNumber", simSerialNumber);
        deviceInfo.put("simOperator", simOperator);
        deviceInfo.put("simOperatorName", simOperatorName);
        deviceInfo.put("simCountryIso", simCountryIso);
        deviceInfo.put("networkType", networkType);
        deviceInfo.put("networkCountryIso", networkCountryIso);
        deviceInfo.put("networkOperator", networkOperator);
        deviceInfo.put("networkOperatorName", networkOperatorName);
        deviceInfo.put("phoneType", phoneType);
        deviceInfo.put("simState", simState);

        WifiManager wifi = getWifi(context);
        String mac = wifi.getConnectionInfo().getMacAddress();//mac地址:02:00:00:00:00:00=>可以使用手机WiFi或蓝牙的MAC地址作为设备标识，但是并不推荐这么做，原因有以下两点：硬件限制：并不是所有的设备都有WiFi和蓝牙硬件，硬件不存在自然也就得不到这一信息。获取的限制：如果WiFi没有打开过，是无法获取其Mac地址的；而蓝牙是只有在打开的时候才能获取到其Mac地址。
        String ssid = wifi.getConnectionInfo().getSSID();//无线路由器名:ETD-tech
        String bssid = wifi.getConnectionInfo().getBSSID();//无线路由器地址:48:7a:da:32:31:f2
        int wifiIp = wifi.getConnectionInfo().getIpAddress();//内网ip(wifl可用):可以用代码转成192.168形式=>2114393098
//        String bluetoothAddress = BluetoothAdapter.getDefaultAdapter().getAddress();//蓝牙地址:蓝牙地址MAC地址=>02:00:00:00:00:00
//        String bluetoothName = BluetoothAdapter.getDefaultAdapter().getName();//蓝牙名称:QCOM-BTD

        deviceInfo.put("mac", mac);
        deviceInfo.put("ssid", ssid);
        deviceInfo.put("bssid", bssid);
        deviceInfo.put("wifiIp", intIP2StringIP(wifiIp));
//        deviceInfo.put("bluetoothAddress", bluetoothAddress);
//        deviceInfo.put("bluetoothName", bluetoothName);

        String cpuName = getCpuName();//cpu名称:Qualcomm Technologies, Inc MSM8953
        deviceInfo.put("cpuName:", cpuName);

        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);//andrlid_id:ANDROID_ID是设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置
        deviceInfo.put("androidId", androidId);

        String radioVersion = android.os.Build.getRadioVersion();
        String serial = Build.SERIAL;//硬件序列，在Android 2.2 以上可以通过 android.os.Build.SERIAL 获得序列号。在一些没有电话功能的设备会提供，某些手机上也可能提供（所以就是经常会返回Unknown）
        String brand = Build.BRAND;//品牌:获取设备品牌=>xiaomi
        String tags = Build.TAGS;//描述build的标签:设备标签。如release-keys 或测试的 test-keys=>release-keys
        String device = Build.DEVICE;//设备名:获取设备驱动名称=>vince
        String fingerprint = Build.FINGERPRINT;//指纹:设备的唯一标识。由设备的多个信息拼接合成=>xiaomi/vince/vince:7.1.2/N2G47H/V9.2.4.0.NEGCNEK:user/release-keys
        String bootloader = Build.BOOTLOADER;//主板引导程序:获取设备引导程序版本号=>unknown
        String board = Build.BOARD;//主板:获取设备基板名称=>msm8953
        String model = Build.MODEL;//型号:获取手机的型号=>Redmi 5 Plus
        String product = Build.PRODUCT;//产品名:整个产品的名称=>vince
        String type = Build.TYPE;//设备版本类型:主要为user 或eng.=>user
        String user = Build.USER;//设备用户名:基本上都为android-build=>builder
        String display = Build.DISPLAY;//DISPLAY:获取设备显示的版本包（在系统设置中显示为版本号）和ID一样=>ZQL1711-vince-build-20180209170332
        String hardware = Build.HARDWARE;//硬件:设备硬件名称,一般和基板名称一样（BOARD）=>qcom
        String host = Build.HOST;//设备主机地址:设备主机地址=>c3-miui-ota-bd95.bj
        String manufacturer = Build.MANUFACTURER;//制造商:获取设备制造商=>Xiaomi
        String buildId = Build.ID;//ID:设备版本号=>N2G47H
        long time = Build.TIME;//Build时间:Build时间=>1518167011000

        deviceInfo.put("radioVersion", radioVersion);
        deviceInfo.put("serial", serial);
        deviceInfo.put("brand", brand);
        deviceInfo.put("tags", tags);
        deviceInfo.put("device", device);
        deviceInfo.put("fingerprint", fingerprint);
        deviceInfo.put("bootloader", bootloader);
        deviceInfo.put("board", board);
        deviceInfo.put("model", model);
        deviceInfo.put("product", product);
        deviceInfo.put("type", type);
        deviceInfo.put("user", user);
        deviceInfo.put("display", display);
        deviceInfo.put("hardware", hardware);
        deviceInfo.put("host", host);
        deviceInfo.put("changshang", manufacturer);
        deviceInfo.put("buildId", buildId);
        deviceInfo.put("time", time);

        String release = Build.VERSION.RELEASE;//系统版本:获取系统版本字符串。如4.1.2 或2.2 或2.3等=>7.1.2
        String sdk = Build.VERSION.SDK;//系统版本值:系统的API级别 一般使用下面大的SDK_INT 来查看=>25
        int sdkInt = Build.VERSION.SDK_INT;//系统的API级别:数字表示=>25
        String codeName = Build.VERSION.CODENAME;//固件开发版本代号:设备当前的系统开发代号，一般使用REL代替=>REL
        String incremental = Build.VERSION.INCREMENTAL;//源码控制版本号:系统源代码控制值，一个数字或者git hash值=>V9.2.4.0.NEGCNEK

        deviceInfo.put("release", release);
        deviceInfo.put("sdk", sdk);
        deviceInfo.put("sdkInt", sdkInt);
        deviceInfo.put("codeName", codeName);
        deviceInfo.put("incremental", incremental);

        String cpuABI = Build.CPU_ABI;//TODO cpu指令集1:获取设备指令集名称（CPU的类型）=>arm64-v8a
        String cpuABI2 = Build.CPU_ABI2;//TODO cpu指令集2
        deviceInfo.put("cpuABI", cpuABI);
        deviceInfo.put("cpuABI2", cpuABI2);

        return deviceInfo;
    }

    public static NetworkInfo getNetInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    public static TelephonyManager getPhone(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static WifiManager getWifi(Context context) {
        return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static String getCpuName() {
        String name = "";
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((name = bufferedReader.readLine()) != null) {
                if (name.contains("Hardware")) {
                    return name.split(":")[1];
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return name;
    }

    /**
     * API >=9:通过“Build.SERIAL”这个属性来保证ID的独一无二。API 9 以上的Android设备目前市场占有率在99.5%
     * API < 9:我们可以通过读取设备的ROM版本号、厂商名、CPU型号和其他硬件信息来组合出一串15位的号码，这15位号码有可能重复，但是几率太小了，小到可以忽略，况且就算重复了，我们损失的用户最多也只不过是0.5%而已。
     *
     * @return
     */
    public static String getUniqueID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    private static String sID = null;
    private static String INSTALLATION = "INSTALLATION";

    /**
     * 在程序安装后第一次运行时生成一个ID，该方式和设备唯一标识不一样，不同的应用程序会产生不同的ID，同一个程序重新安装也会不同。所以这不是设备的唯一ID，但是可以保证每个用户的ID是不同的。 可以说是用来标识每一份应用程序的唯一ID（即Installtion ID），可以用来跟踪应用的安装数量等（其实就是UUID）
     *
     * @param context
     * @return
     */
    public static String getAppId(Context context) {
        if (sID == null) {
            File installation = new File(context.getCacheDir(), INSTALLATION);
            try {
                if (!installation.exists()) {
                    writeInstallationFile(installation);
                }
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) randomAccessFile.length()];
        randomAccessFile.readFully(bytes);
        randomAccessFile.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        fileOutputStream.write(id.getBytes());
        fileOutputStream.close();
    }

    public static void notification(Context context,Class _class,int notificationID,String title,String content){
        Intent intent = new Intent(context,_class);
        intent.putExtra("notificationID",notificationID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,context.getPackageName()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(content).addAction(R.mipmap.ic_launcher,"Notzuonotdied",pendingIntent);
        notificationManager.notify(notificationID,builder.build());
    }
}
