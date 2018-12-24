package com.smart.journal.app

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.smart.journal.BuildConfig
import com.smart.journal.tools.location.LocationTools
import com.tencent.bugly.Bugly
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author guandongchen
 * @date 2018/1/5
 */

class MyApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        instance = this
        initLogger()
        LocationTools.getInstance()
        com.blankj.utilcode.util.Utils.init(this)
        Bugly.init(applicationContext, "c789e27850", false)
        initRealm()
        initBaiDuMao()

    }

    fun  initBaiDuMao(){
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initRealm() {


        Realm.init(this)
        /* byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);*/

        val config = RealmConfiguration.Builder()
                .schemaVersion(7)
                //.encryptionKey(key)//数据库加密
                .deleteRealmIfMigrationNeeded()
                .build()
        // Use the config
        Realm.setDefaultConfiguration(config)

    }

    private fun initLogger() {

        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("com.smart.weather")
                .build()
        val androidLogAdapter = object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        }
        Logger.addLogAdapter(androidLogAdapter)

    }


    companion object {

        var instance: MyApp? = null
            private set
        const val UPDATE_APP_ID = BuildConfig.APPLICATION_ID + ".fileProvider"

    }
}