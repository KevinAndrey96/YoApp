package com.trantec.yo.services

import android.os.Handler
import android.os.Looper
import com.orhanobut.logger.Logger
import com.trantec.yo.YoPrestoApp
import com.trantec.yo.constants.AppConstants
import com.trantec.yo.constants.WebConstant
import com.trantec.yo.dto.TokenResponse
import com.trantec.yo.enumeration.SessionKeys
import com.trantec.yo.utils.JSONUtils
import hundredthirtythree.sessionmanager.SessionManager
import okhttp3.*
import org.codehaus.jackson.map.ObjectMapper
import org.json.JSONObject
import java.io.IOException

class TokenRefresh {


    companion object {

        private var yoprestoAliado: YoPrestoApp? = null
        private var client = OkHttpClient()
        var mHandler =  Handler(Looper.getMainLooper())
        private val mapper = ObjectMapper()

        fun refreshToken(): Boolean {

            var tokenRefreshed = false

            try{

                var tokenString = ""
                /*val requestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("username", AppConstants.TOKEN_USERNAME)
                        .addFormDataPart("password", AppConstants.TOKEN_PASSWORD)
                        .addFormDataPart("grant_type", AppConstants.TOKEN_GRANT_TYPE)
                        .build()*/


                val formBody = FormBody.Builder()
                        .add("username", AppConstants.TOKEN_USERNAME)
                        .add("password", AppConstants.TOKEN_PASSWORD)
                        .add("grant_type", AppConstants.TOKEN_GRANT_TYPE)
                        .build()

                val builder = Request.Builder()
                builder.url(WebConstant.TOKEN_URL)
                Logger.d(WebConstant.TOKEN_URL)

                val request = builder
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Authorization", AppConstants.TOKEN_HEADER_AUTHORIZATION_TOKEN)
                        .post(formBody)
                        .build()



                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                        e.printStackTrace()


                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {


                        val responseTokenString = response.body()!!.string()
                        val tokenJSONObject: JSONObject
                        val tokenResponse: TokenResponse

                        if (responseTokenString != null) {

                            Logger.d("Response from get token")
                            Logger.d(responseTokenString)

                            if (JSONUtils.isJSONValid(responseTokenString)) {

                                tokenJSONObject = JSONObject(responseTokenString)

                                if(tokenJSONObject != null){

                                    tokenResponse =  mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)

                                    if(tokenResponse != null){

                                        Logger.d("Token response on object")
                                        Logger.d(tokenResponse.access_token)

                                        if(tokenResponse.access_token != null){

                                            tokenString = tokenResponse.access_token!!
                                            SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenString)

                                            tokenRefreshed = true


                                        }else{
                                            SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                        }

                                    }else{
                                        Logger.d("json object is null")
                                    }

                                }else{
                                    Logger.d("Is not json object")
                                }

                            }

                        }


                    }
                })
            }catch (ex: Exception){
                ex.printStackTrace()
            }finally {
                Logger.d("Token refreshed ? $tokenRefreshed")
                return tokenRefreshed
            }


        }
    }

}