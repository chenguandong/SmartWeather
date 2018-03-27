package com.smart.weather.app

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.smart.weather.BuildConfig
import com.smart.weather.tools.location.LocationTools
import com.squareup.leakcanary.LeakCanary
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
        initLeakCanary()
        com.blankj.utilcode.util.Utils.init(this)
        Fresco.initialize(this)
        Bugly.init(applicationContext, "c789e27850", false)
        initRealm()
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
                .schemaVersion(6)
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

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

    companion object {

        var instance: MyApp? = null
            private set
        const val UPDATE_APP_ID = BuildConfig.APPLICATION_ID + ".fileProvider"

    }
}