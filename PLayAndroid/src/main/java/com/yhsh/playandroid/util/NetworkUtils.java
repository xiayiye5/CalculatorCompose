package com.yhsh.playandroid.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class NetworkUtils {

    // 基础网络状态检测（API 21以下）
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    // 增强型网络检测（包含实际连通性验证）
    public static boolean isInternetAvailable(Context context) {
        if (!isNetworkConnected(context)) return false;

        // 可添加实际网络请求验证（如ping百度）
        try {
            Process process = Runtime.getRuntime().exec("ping -c 1 www.baidu.com");
            int exitValue = process.waitFor();
            return exitValue == 0;
        } catch (Exception e) {
            return false;
        }
    }
}

