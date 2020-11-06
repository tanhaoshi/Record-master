package com.coderpage.mine.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * @author abner-l. 2017-06-01
 */

public class AndroidUtils {

    /**
     * 获取唯一 DEVICE ID
     */
    public static String generateDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = "" + tm.getDeviceId();
            String simSerialNumber = "" + tm.getSimSerialNumber();
            String androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) deviceId.hashCode() << 32) | simSerialNumber.hashCode());
            return deviceUuid.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "DEFAULT";
        }
    }

    /**
     * generate uuid
     *
     * @return uuid
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * 打开应用设置页面
     */
    public static void openAppSettingPage(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 保留小数点两位
     * @param d
     * @return
     */
    public static String formatDouble2(double d) {
        DecimalFormat form = new DecimalFormat();
        form.setMaximumFractionDigits(2);
        form.setGroupingSize(0);
        form.setRoundingMode(RoundingMode.FLOOR);
        return form.format(d);
    }


    public static String getDayOfWeek(String dateTime) {
        Calendar cal = Calendar.getInstance();
        if (dateTime.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date;
            try {
                date = sdf.parse(dateTime);
            } catch (Exception e) {
                date = null;
                e.printStackTrace();
            }
            if (date != null) {
                cal.setTime(new Date(date.getTime()));
            }
        }


        String date = "";

        int week = cal.get(Calendar.DAY_OF_WEEK);
        //为什么要减一,因为我传入的时间格式是yyyy-MM-dd 相对于它来说就是前一天,所以减一。（24点还没到新一天的概念）
        week = week - 1;

        switch (week){
            case 1:
                date = "星期一";
                break;
            case 2:
                date = "星期二";
                break;
            case 3:
                date = "星期三";
                break;
            case 4:
                date = "星期四";
                break;
            case 5:
                date = "星期五";
                break;
            case 6:
                date = "星期六";
            case 7:
                date = "星期天";
                break;
            default:
                date = "为止日期";
                break;
        }

        return date;
    }
}
