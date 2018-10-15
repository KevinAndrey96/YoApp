package com.yopresto.app.yoprestoapp.ui.main

import android.app.Application
import android.support.multidex.MultiDex
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.yopresto.app.yoprestoapp.constants.WebConstant
import com.yopresto.app.yoprestoapp.enumeration.SessionKeys
import hundredthirtythree.sessionmanager.SessionManager

class HomeActivity : Application(){
    val endpoint = WebConstant.DEV_ENDPOINT_URL

    override fun onCreate() {
        super.onCreate()

        MultiDex.install(this)

        Logger.addLogAdapter(AndroidLogAdapter())

        SessionManager.Builder()
                .setContext(applicationContext)
                .setPrefsName(SessionKeys.PREFS_NAME.key)
                .build()


        SessionManager.putString(SessionKeys.TEST.key, "I'm SessionManager")
        Logger.d("onCreate: "+SessionManager.getString(SessionKeys.TEST.key, "1"))



    }

    companion object {

        fun getEndpoint(yoPrestoAliado: HomeActivity): String {
            return yoPrestoAliado.endpoint
        }
    }
}
