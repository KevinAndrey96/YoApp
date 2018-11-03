package com.trantec.yo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import com.example.biometricbytte.morpho.face.BytteCaptureFace
import com.example.biometricbytte.morpho.huella.BytteFingerPrint
import com.example.biometricbytte.morpho.license.BytteLicense
import com.example.docbytte.BiometricCamera.ValuesBiometric
import com.example.docbytte.helper.Util
import com.example.docbytte.ui.CBackDocument
import com.example.docbytte.ui.CFrontDocument
import com.example.docbytte.ui.FrontDocPassport
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import com.microblink.activity.BaseScanActivity
import com.orhanobut.logger.Logger
import com.trantec.yo.constants.AppConstants
import com.trantec.yo.constants.HttpObjectsConstants
import com.trantec.yo.constants.OperationConstants
import com.trantec.yo.constants.WebConstant
import com.trantec.yo.dto.EnrollmentDatos
import com.trantec.yo.dto.IPResponse
import com.trantec.yo.dto.TokenResponse
import com.trantec.yo.enumeration.SessionKeys
import com.trantec.yo.ui.main.HomeActivity
import com.trantec.yo.utils.JSONUtils
import com.yopresto.app.yoprestoapp.dto.*
import hundredthirtythree.sessionmanager.SessionManager
import kotlinx.android.synthetic.main.activity_enrollment.*
import kotlinx.android.synthetic.main.scanner_facial.*
import libs.mjn.prettydialog.PrettyDialog
import okhttp3.*
import org.codehaus.jackson.map.ObjectMapper
import org.json.JSONObject
import java.io.IOException


import java.util.*


class ScannerFacial : AppCompatActivity() {

    val LICENSEMICROBLINK = "V76CKZT3-NONJVDPE-NT4KLLY5-5OOBC5AI-JZKZL23B-FLBJP77K-EH4NX33O-LF3REX3F"//esta licencia se debe solicitar a bytte para el funcionamiento en los documentos
    val MY_REQUEST_CODE_LISENCE = 1329
    val MY_REQUEST_CODE_FACECAPTURE = 1333
    val REQUEST_PERMISSION = 1433
    val URLPETICION = "https://portal.bytte.com.co/casb/SmartBio/SB/API/SmartBio/"//esta url se debe solicitar a bytte para licenciar el projecto

    private var progressDialog: MaterialDialog? = null
    var mHandler =  Handler(Looper.getMainLooper())
    var context: Context? = null
    private var client = OkHttpClient()
    private val mapper = ObjectMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_facial)


        btnScanner.setOnClickListener {

            //rostro
            val intent = Intent(this@ScannerFacial, BytteCaptureFace::class.java)
            intent.putExtra(ValuesBiometric.EXTRAS_FACECAPTURE, "2")//factor de captura  0  facil 1  medio 2 dificil 3 muy dificil 0,1,2,3 deteccion por movimientos, 4,5,6 deteccion de rostro
            intent.putExtra("TIPO_CAMERA", "0")//0 capara frontal 1 camara posterior
            intent.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
            startActivityForResult(intent, MY_REQUEST_CODE_FACECAPTURE)
        }

        //Permisos en runtime
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var results_biometric = ""
        val bitmap: Bitmap
        val gson = Gson()
        try {
            if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_OK) {
                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                val properties = gson.fromJson(results_biometric, Properties::class.java)

                enviarDatos()

            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_CANCELED) {
                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                /*val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)*/
                enviarDatos()
            }

            Log.d("TAG", results_biometric)

        } catch (e: Exception) {

        }
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

    private fun enviarDatos() {
        Logger.d("Test")
        try{
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
                            PrettyDialog(this@ScannerFacial)
                                    .setTitle("Información")
                                    //.setMessage("Error. " + e.message)
                                    .setMessage("Verifique su conexión a internet e intentelo de nuevo")
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
                                                        PrettyDialog(this@ScannerFacial)
                                                                .setTitle("Información")
                                                                .setMessage("Intente de nuevo" + e.message)
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

                                                                    val enrollment = EnrollmentRequest()
                                                                    val _datos = EnrollmentDatos()

                                                                    val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                                                                    val name = prefs.getString("nombre", "")

                                                                    _datos.idusuario = prefs.getString("idusuario", "")
                                                                    _datos.ip = prefs.getString("ip", "")
                                                                    _datos.accion = "1"
                                                                    _datos.tipodocumento = "CC"
                                                                    _datos.numerodocumento = prefs.getString("enrollment_numerodocumento", "")
                                                                    _datos.primerapellido = prefs.getString("enrollment_primerapellido", "")
                                                                    _datos.segundoapellido = prefs.getString("enrollment_segundoapellido", "")
                                                                    _datos.primernombre = prefs.getString("enrollment_primernombre", "")
                                                                    _datos.segundonombre = prefs.getString("enrollment_segundonombre", "")
                                                                    _datos.fechanacimiento = prefs.getString("enrollment_fechanacimiento", "")
                                                                    _datos.sexo = prefs.getString("enrollment_sexo", "")
                                                                    _datos.rh = prefs.getString("enrollment_rh", "")
                                                                    _datos.codigo = ""
                                                                    _datos.idinformacionpersona = prefs.getString("enrollment_idinformacionpersona", "")
                                                                    _datos.identidad = prefs.getString("enrollment_identidad", "")
                                                                    _datos.respuesta = "true"
                                                                    _datos.nombreimagencc = prefs.getString("enrollment_imgfrontal", "")
                                                                    _datos.nombreimagenfrontal = ""
                                                                    _datos.nombreimagentracera = ""
                                                                    _datos.ruta = ""

                                                                    enrollment.datos = _datos
                                                                    Logger.d("Datos:"+enrollment.datos)

                                                                    val bodyMap = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(enrollment))

                                                                    val builderMap = Request.Builder()
                                                                    builderMap.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                                                    Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                                                    val requestReport = builderMap
                                                                            .header("Content-Type", "application/json; charset=UTF-8")
                                                                            .header("Accept", "application/json")
                                                                            .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                                            .header("operation", OperationConstants.ENROLLMENT)
                                                                            .post(bodyMap)
                                                                            .build()


                                                                    client.newCall(requestReport).enqueue(object : Callback {
                                                                        override fun onFailure(call: Call, e: IOException) {

                                                                            e.printStackTrace()

                                                                            mHandler.post{
                                                                                run{
                                                                                    stopProgess()
                                                                                    PrettyDialog(this@ScannerFacial)
                                                                                            .setTitle("Información")
                                                                                            .setMessage("Error. " + e.message)
                                                                                            .show()
                                                                                }
                                                                            }

                                                                        }

                                                                        @Throws(IOException::class)
                                                                        override fun onResponse(call: Call, response: Response) {

                                                                            Logger.d("Resp:"+response)

                                                                            val responseMapString = response.body()!!.string()
                                                                            val enrollmentResponse: EnrollmentResponse

                                                                            Logger.d(responseMapString)
                                                                            mHandler.post{
                                                                                run{
                                                                                    stopProgess()
                                                                                    PrettyDialog(this@ScannerFacial)
                                                                                            .setTitle("Información")
                                                                                            .setMessage("No existe entidad para la persona.")
                                                                                            .show()
                                                                                }
                                                                            }



                                                                            if (JSONUtils.isJSONValid(responseMapString)) {
                                                                                enrollmentResponse =  mapper.readValue<EnrollmentResponse>(responseMapString, EnrollmentResponse::class.java)
                                                                                Logger.d(enrollmentResponse)
                                                                                if(enrollmentResponse.status == true ){
                                                                                    mHandler.post{
                                                                                        run{
                                                                                            stopProgess()
                                                                                            PrettyDialog(this@ScannerFacial)
                                                                                                    .setTitle("Información")
                                                                                                    .setMessage("Enrolamiento exitoso.")
                                                                                                    .show()
                                                                                        }
                                                                                    }

                                                                                } else {
                                                                                    mHandler.post{
                                                                                        run{
                                                                                            stopProgess()
                                                                                            PrettyDialog(this@ScannerFacial)
                                                                                                    .setTitle("Información")
                                                                                                    .setMessage("Error" + enrollmentResponse.message)
                                                                                                    .show()
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
                                                                            PrettyDialog(this@ScannerFacial)
                                                                                    .setTitle("Información")
                                                                                    .setMessage("Intente de nuevo.")
                                                                                    .show()
                                                                        }
                                                                    }
                                                                }

                                                            }else{
                                                                Logger.d("json object is null")
                                                                mHandler.post{
                                                                    run{
                                                                        stopProgess()
                                                                        PrettyDialog(this@ScannerFacial)
                                                                                .setTitle("Información")
                                                                                .setMessage("Intente de nuevo.")
                                                                                .show()
                                                                    }
                                                                }
                                                            }

                                                        }else{
                                                            Logger.d("Is not json object")
                                                            mHandler.post{
                                                                run{
                                                                    stopProgess()
                                                                    PrettyDialog(this@ScannerFacial)
                                                                            .setTitle("Información")
                                                                            .setMessage("Intente de nuevo.")
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

        }catch (ex: Exception){
            ex.printStackTrace()
            mHandler.post{
                run{
                    stopProgess()
                    PrettyDialog(this@ScannerFacial)
                            .setTitle("Información")
                            .setMessage("Error ingresando")
                            .show()
                }
            }
        }
    }
}
