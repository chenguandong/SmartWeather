package com.smart.journal.tools;

import android.content.Context;

import com.blankj.utilcode.util.NetworkUtils;

/**
 * @author guandongchen
 * @date 2018/1/16
 */
public class NetWorkTools {
    /**
     * 判断是否有网络连接
     */
    public boolean isConnected(Context context) {

        return NetworkUtils.isConnected();
    }
}
