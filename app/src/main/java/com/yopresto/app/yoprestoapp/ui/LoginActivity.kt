package com.yopresto.app.yoprestoapp.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.widget.TextView

import android.os.*
import android.support.annotation.RequiresApi
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.orhanobut.logger.Logger
import com.yopresto.app.yoprestoapp.R
import com.yopresto.app.yoprestoapp.YoPrestoApp
import com.yopresto.app.yoprestoapp.constants.AppConstants
import com.yopresto.app.yoprestoapp.constants.HttpObjectsConstants
import com.yopresto.app.yoprestoapp.constants.OperationConstants
import com.yopresto.app.yoprestoapp.constants.WebConstant
import com.yopresto.app.yoprestoapp.dto.*
import com.yopresto.app.yoprestoapp.enumeration.SessionKeys
import com.yopresto.app.yoprestoapp.ui.main.MainActivity
import com.yopresto.app.yoprestoapp.utils.JSONUtils
import com.yopresto.app.yoprestoapp.utils.PhoneUtil
import dmax.dialog.SpotsDialog
import hundredthirtythree.sessionmanager.SessionManager
import kotlinx.android.synthetic.main.activity_login.*
import libs.mjn.prettydialog.PrettyDialog
import net.hockeyapp.android.CrashManager

import okhttp3.*
import org.codehaus.jackson.map.ObjectMapper
import org.json.JSONObject
import java.io.IOException

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    private var yoprestoAliado: YoPrestoApp? = null
    private var client = OkHttpClient()
    var mHandler =  Handler(Looper.getMainLooper())
    private val mapper = ObjectMapper()
    var context: Context? = null
    private var progressDialog: MaterialDialog? = null


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(SessionManager.getBoolean(SessionKeys.IS_LOGGED_IN.key, false)){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        editTextPassword.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                return@OnEditorActionListener true
            }
            false
        })

        yoprestoAliado = applicationContext as YoPrestoApp?

        buttonSignIn.setOnClickListener {


            try{
                when {
                    editTextUser.text.toString().isNullOrEmpty() -> editTextUser.error = getString(R.string.error_field_required)
                    editTextPassword.text.toString().isNullOrEmpty() -> editTextPassword.error = getString(R.string.error_field_required)
                    else -> SafetyNet.getClient(this@LoginActivity).verifyWithRecaptcha(getString(R.string.site_key))
                            .addOnSuccessListener {response ->

                                val userResponseToken = response.tokenResult

                                if (!userResponseToken.isEmpty()) {
                                    // Validate the user response token using the
                                    // reCAPTCHA siteverify API.


                                    val formBody = FormBody.Builder()
                                            .add("username", AppConstants.TOKEN_USERNAME)
                                            .add("password", AppConstants.TOKEN_PASSWORD)
                                            .add("grant_type", AppConstants.TOKEN_GRANT_TYPE)
                                            .build()

                                    val builderToken = Request.Builder()
                                    builderToken.url(WebConstant.TOKEN_URL)
                                    Logger.d(WebConstant.TOKEN_URL)

                                    val request = builderToken
                                            .header("Content-Type", "application/x-www-form-urlencoded")
                                            .header("Authorization", AppConstants.TOKEN_HEADER_AUTHORIZATION_TOKEN)
                                            .post(formBody)
                                            .build()
                                    mHandler.post{
                                        run{
                                            startProgress()
                                        }
                                    }


                                    client.newCall(request).enqueue(object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {

                                            e.printStackTrace()

                                            mHandler.post{
                                                run{
                                                    stopProgess()
                                                    PrettyDialog(this@LoginActivity)
                                                            .setTitle("Información")
                                                            .setMessage("Error. " + e.message)
                                                            .show()
                                                }
                                            }
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

                                                                SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenResponse.access_token)


                                                                val builderIp = Request.Builder()
                                                                builderIp.url(WebConstant.IPIFY_ENDPOINT)

                                                                val requestIp = builderIp
                                                                        .header("Accept", "application/json")
                                                                        .build()

                                                                client.newCall(requestIp).enqueue(object : Callback {
                                                                    override fun onFailure(call: Call, e: IOException) {

                                                                        e.printStackTrace()

                                                                        mHandler.post{
                                                                            run{
                                                                                stopProgess()
                                                                                PrettyDialog(this@LoginActivity)
                                                                                        .setTitle("Información")
                                                                                        .setMessage("Error ingresando " + e.message)
                                                                                        .show()
                                                                            }
                                                                        }

                                                                    }

                                                                    @Throws(IOException::class)
                                                                    override fun onResponse(call: Call, response: Response) {

                                                                        val responseIpString = response.body()!!.string()
                                                                        val ipJSONObject: JSONObject
                                                                        val ipResponse: IPResponse

                                                                        if (responseIpString != null) {

                                                                            Logger.d("Response from get ip")
                                                                            Logger.d(responseIpString)

                                                                            if (JSONUtils.isJSONValid(responseIpString)) {

                                                                                ipJSONObject = JSONObject(responseIpString)

                                                                                if(ipJSONObject != null){

                                                                                    ipResponse =  mapper.readValue<IPResponse>(ipJSONObject.toString(), IPResponse::class.java)

                                                                                    if(ipResponse != null){

                                                                                        Logger.d("Token response on object")
                                                                                        Logger.d(ipResponse.ip)

                                                                                        if(ipResponse.ip != null){

                                                                                            val login = LoginRequest()
                                                                                            val datos = LoginDatos()


                                                                                            datos.cuenta = editTextUser.text.toString()
                                                                                            datos.clave = PhoneUtil.getSHA512(editTextPassword.text.toString())
                                                                                            datos.ip = ipResponse.ip

                                                                                            Logger.d("Clave " + datos.clave)

                                                                                            login.datos = datos


                                                                                            val bodyLogin = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(login))

                                                                                            val builderLogin = Request.Builder()
                                                                                            builderLogin.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                                                                            Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                                                                            val requestLogin = builderLogin
                                                                                                    .header("Content-Type", "application/json; charset=UTF-8")
                                                                                                    .header("Accept", "application/json")
                                                                                                    .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                                                                    .header("operation", OperationConstants.LOGIN_OPERATION)
                                                                                                    .post(bodyLogin)
                                                                                                    .build()


                                                                                            client.newCall(requestLogin).enqueue(object : Callback {
                                                                                                override fun onFailure(call: Call, e: IOException) {

                                                                                                    e.printStackTrace()

                                                                                                    mHandler.post{
                                                                                                        run{
                                                                                                            stopProgess()
                                                                                                            PrettyDialog(this@LoginActivity)
                                                                                                                    .setTitle("Información")
                                                                                                                    .setMessage("Error. " + e.message)
                                                                                                                    .show()
                                                                                                        }
                                                                                                    }

                                                                                                }

                                                                                                @Throws(IOException::class)
                                                                                                override fun onResponse(call: Call, response: Response) {

                                                                                                    val responseLoginString = response.body()!!.string()
                                                                                                            .replace("\"response\": {\"dataresponse\":{\"@nil\":\"true\"},\"response\":\"CLAVE INVALIDA\",\"idresponse\":3},","")
                                                                                                            .replace("\"response\": {\"dataresponse\":{\"@nil\":\"true\"},\"response\":\"CUENTA INVALIDA\",\"idresponse\":2},", "")
                                                                                                    val loginResponse: LoginResponse

                                                                                                    Logger.d("Response login")
                                                                                                    Logger.d(responseLoginString)


                                                                                                    if (JSONUtils.isJSONValid(responseLoginString)) {

                                                                                                        loginResponse =  mapper.readValue<LoginResponse>(responseLoginString, LoginResponse::class.java)

                                                                                                        if(loginResponse?.response != null && loginResponse.response!!.dataresponse != null){


                                                                                                            val loginDataresponse: LoginDataresponse = mapper.readValue<LoginDataresponse>(loginResponse.response!!.dataresponse, LoginDataresponse::class.java)

                                                                                                            if(loginResponse.status!!){

                                                                                                                if(loginResponse.response!!.dataresponse != null){


                                                                                                                    if(loginDataresponse != null){
                                                                                                                        if(loginDataresponse.idusuario != null){

                                                                                                                            SessionManager.putString(SessionKeys.USER_SESSION.key, mapper.writeValueAsString(loginDataresponse))
                                                                                                                            SessionManager.putBoolean(SessionKeys.IS_LOGGED_IN.key, true)

                                                                                                                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                                                                                                            finish()
                                                                                                                        }else{

                                                                                                                            mHandler.post{
                                                                                                                                run{
                                                                                                                                    stopProgess()
                                                                                                                                    PrettyDialog(this@LoginActivity)
                                                                                                                                            .setTitle("Información")
                                                                                                                                            .setMessage("Datos incorrectos, verifique e intente nuevamente.")
                                                                                                                                            .show()
                                                                                                                                }
                                                                                                                            }


                                                                                                                        }
                                                                                                                    }else{
                                                                                                                        mHandler.post{
                                                                                                                            stopProgess()
                                                                                                                            run{
                                                                                                                                PrettyDialog(this@LoginActivity)
                                                                                                                                        .setTitle("Información")
                                                                                                                                        .setMessage("Datos incorrectos, verifique e intente nuevamente.")
                                                                                                                                        .show()
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }

                                                                                                                }else{
                                                                                                                    mHandler.post{
                                                                                                                        stopProgess()
                                                                                                                        run{
                                                                                                                            PrettyDialog(this@LoginActivity)
                                                                                                                                    .setTitle("Información")
                                                                                                                                    .setMessage("Datos incorrectos, verifique e intente nuevamente.")
                                                                                                                                    .show()
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }else{
                                                                                                                mHandler.post{
                                                                                                                    run{
                                                                                                                        stopProgess()
                                                                                                                        PrettyDialog(this@LoginActivity)
                                                                                                                                .setTitle("Error")
                                                                                                                                .setMessage("Datos incorrectos, verifique e intente nuevamente.")
                                                                                                                                .show()
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }else{
                                                                                                            mHandler.post{
                                                                                                                run{
                                                                                                                    stopProgess()
                                                                                                                    PrettyDialog(this@LoginActivity)
                                                                                                                            .setTitle("Información")
                                                                                                                            .setMessage("Datos incorrectos, verifique e intente nuevamente.")
                                                                                                                            .show()
                                                                                                                }
                                                                                                            }
                                                                                                        }

                                                                                                    }else{
                                                                                                        mHandler.post{
                                                                                                            run{
                                                                                                                stopProgess()
                                                                                                                PrettyDialog(this@LoginActivity)
                                                                                                                        .setTitle("Información")
                                                                                                                        .setMessage("Datos incorrectos, verifique e intente nuevamente.")
                                                                                                                        .show()
                                                                                                            }
                                                                                                        }
                                                                                                    }


                                                                                                }
                                                                                            })


                                                                                        }else{
                                                                                            SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                                                                            mHandler.post{
                                                                                                run{
                                                                                                    stopProgess()
                                                                                                    PrettyDialog(this@LoginActivity)
                                                                                                            .setTitle("Información")
                                                                                                            .setMessage("Datos incorrectos, verifique e intente nuevamente.")
                                                                                                            .show()
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                    }else{
                                                                                        Logger.d("json object is null")
                                                                                        mHandler.post{
                                                                                            run{
                                                                                                stopProgess()
                                                                                                PrettyDialog(this@LoginActivity)
                                                                                                        .setTitle("Información")
                                                                                                        .setMessage("Datos incorrectos, verifique e intente nuevamente.")
                                                                                                        .show()
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                }else{
                                                                                    Logger.d("Is not json object")
                                                                                    mHandler.post{
                                                                                        run{
                                                                                            stopProgess()
                                                                                            PrettyDialog(this@LoginActivity)
                                                                                                    .setTitle("Información")
                                                                                                    .setMessage("Datos incorrectos, verifique e intente nuevamente.")
                                                                                                    .show()
                                                                                        }
                                                                                    }
                                                                                }

                                                                            }

                                                                        }


                                                                    }
                                                                })
                                                            }else{
                                                                SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                                                mHandler.post{
                                                                    run{
                                                                        stopProgess()
                                                                    }
                                                                }
                                                            }

                                                        }else{
                                                            Logger.d("json object is null")
                                                            mHandler.post{
                                                                run{
                                                                    stopProgess()
                                                                }
                                                            }
                                                        }

                                                    }else{
                                                        Logger.d("Is not json object")
                                                        mHandler.post{
                                                            run{
                                                                stopProgess()
                                                            }
                                                        }
                                                    }

                                                }

                                            }


                                        }
                                    })


                                }
                            }
                            .addOnFailureListener { e ->

                                if (e is ApiException) {
                                    // An error occurred when communicating with the
                                    // reCAPTCHA service. Refer to the status code to
                                    // handle the error appropriately.
                                    val statusCode = e.statusCode
                                    Logger.d("Error: " + CommonStatusCodes.getStatusCodeString(statusCode))
                                } else {
                                    // A different, unknown type of error occurred.
                                    Logger.d("Error: " + e.message)
                                }
                                mHandler.post{
                                    run{
                                        stopProgess()
                                        PrettyDialog(this@LoginActivity)
                                                .setTitle("Información")
                                                .setMessage("Ha fallado el reconocimiento captcha.")
                                                .show()
                                    }
                                }

                            }
                            .addOnCanceledListener {
                                //TODO
                                //if user cancel request
                                Logger.d("Cancel")
                                mHandler.post{
                                    run{
                                        stopProgess()
                                        PrettyDialog(this@LoginActivity)
                                                .setTitle("Información")
                                                .setMessage("Se ha cancelado el reconocimiento captcha.")
                                                .show()
                                    }
                                }
                            }
                }



            }catch (ex: Exception){
                ex.printStackTrace()

                mHandler.post{
                    run{
                        stopProgess()
                        PrettyDialog(this@LoginActivity)
                                .setTitle("Información")
                                .setMessage("Error ingresando")
                                .show()
                    }
                }
            }finally {

            }

        }



    }


    override fun onResume() {
        super.onResume()
        checkForCrashes()
        context = this

    }

    private fun checkForCrashes() {
        CrashManager.register(this)
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {

    }


    private fun startProgress(){

        try{
            val builder = MaterialDialog.Builder(context!!).title(R.string.progress_dialog)
                    .content(R.string.please_wait)
                    .progress(true, 0)

            progressDialog = builder.build()
            progressDialog!!.show()
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }

    private fun stopProgess(){
        try{
            if(progressDialog != null){
                progressDialog!!.dismiss()
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }

}
