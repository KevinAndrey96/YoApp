package com.trantec.yo

import android.app.Application
import android.support.multidex.MultiDex
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.trantec.yo.constants.WebConstant
import com.trantec.yo.enumeration.SessionKeys
import hundredthirtythree.sessionmanager.SessionManager

class YoPrestoApp : Application(){
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

        fun getEndpoint(yoPrestoAliado: YoPrestoApp): String {
            return yoPrestoAliado.endpoint
        }
    }
}
