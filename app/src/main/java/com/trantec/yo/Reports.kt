package com.trantec.yo

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.afollestad.materialdialogs.MaterialDialog
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.orhanobut.logger.Logger
import com.trantec.yo.constants.AppConstants
import com.trantec.yo.constants.HttpObjectsConstants
import com.trantec.yo.constants.OperationConstants
import com.trantec.yo.constants.WebConstant
import com.trantec.yo.dto.IPResponse
import com.trantec.yo.dto.TokenResponse
import com.trantec.yo.enumeration.SessionKeys
import com.trantec.yo.utils.JSONUtils
import com.yopresto.app.yoprestoapp.dto.*
import hundredthirtythree.sessionmanager.SessionManager
import libs.mjn.prettydialog.PrettyDialog
import okhttp3.*
import org.codehaus.jackson.map.ObjectMapper
import org.json.JSONObject
import java.io.IOException
import CustomAdapter
import android.support.v7.app.AlertDialog

class Reports : AppCompatActivity() {

    private var progressDialog: MaterialDialog? = null
    var mHandler =  Handler(Looper.getMainLooper())
    var context: Context? = null
    private var client = OkHttpClient()
    private val mapper = ObjectMapper()
    var listActive = ArrayList<ListReportActive>()
    var adapter: CustomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ReportActive()

        //ReportFinish()
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

    private fun ReportActive() {
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
                            PrettyDialog(this@Reports)
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
                                                        PrettyDialog(this@Reports)
                                                                .setTitle("Información")
                                                                .setMessage("Error al cargar reportes " + e.message)
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

                                                                    val reports = ReportRequest()
                                                                    val _datos = ReportDatos()


                                                                    _datos.schema = AppConstants.REPORT_SCHEMA
                                                                    _datos.tabla = AppConstants.REPORT_TABLA
                                                                    _datos.campo = AppConstants.REPORT_CAMPO
                                                                    _datos.condiciones = AppConstants.REPORT_STATE_ACTIVE

                                                                    //Logger.d("Clave " + datos.clave)

                                                                    reports.datos = _datos

                                                                    val bodyMap = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(reports))

                                                                    val builderMap = Request.Builder()
                                                                    builderMap.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                                                    Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                                                    val requestReport = builderMap
                                                                            .header("Content-Type", "application/json; charset=UTF-8")
                                                                            .header("Accept", "application/json")
                                                                            .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                                            .header("operation", OperationConstants.MAP_OPERATION)
                                                                            .post(bodyMap)
                                                                            .build()


                                                                    client.newCall(requestReport).enqueue(object : Callback {
                                                                        override fun onFailure(call: Call, e: IOException) {

                                                                            e.printStackTrace()

                                                                            mHandler.post{
                                                                                run{
                                                                                    stopProgess()
                                                                                    PrettyDialog(this@Reports)
                                                                                            .setTitle("Información")
                                                                                            .setMessage("Error. " + e.message)
                                                                                            .show()
                                                                                }
                                                                            }

                                                                        }

                                                                        @Throws(IOException::class)
                                                                        override fun onResponse(call: Call, response: Response) {

                                                                            val responseMapString = response.body()!!.string()
                                                                            val reportResponse: ReportResponse

                                                                            Logger.d(responseMapString)

                                                                            if (JSONUtils.isJSONValid(responseMapString)) {
                                                                                reportResponse =  mapper.readValue<ReportResponse>(responseMapString, ReportResponse::class.java)
                                                                                Logger.d(reportResponse)
                                                                                if(reportResponse.status == true && reportResponse.response!!.dataresponse != null){
                                                                                    val mapp = jacksonObjectMapper()
                                                                                    val result_report = reportResponse.response!!.dataresponse!!.toLowerCase()
                                                                                    val data_reports: List<ReportDataresponse> = mapp.readValue((result_report))

                                                                                    for (item in data_reports) {
                                                                                        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                                                                                        val cedula = prefs.getString("cuenta", "")
                                                                                        val identi = prefs.getString("identidad", "")

                                                                                        //if(cedula == item.cedula.toString()){
                                                                                        if(identi == item.identidad.toString()){
                                                                                            adaptar(item.movimiento.toString(),item.fecha.toString(),item.valor.toString(), item.nombre.toString())
                                                                                        }
                                                                                    }
                                                                                }else
                                                                                {
                                                                                    Logger.d(reportResponse.status)
                                                                                    Logger.d(reportResponse.response!!.dataresponse)
                                                                                }
                                                                            }
                                                                        }
                                                                    })
                                                                }else{
                                                                    SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                                                    mHandler.post{
                                                                        run{
                                                                            stopProgess()
                                                                            PrettyDialog(this@Reports)
                                                                                    .setTitle("Información")
                                                                                    .setMessage("Error al cargar reportes.")
                                                                                    .show()
                                                                        }
                                                                    }
                                                                }

                                                            }else{
                                                                Logger.d("json object is null")
                                                                mHandler.post{
                                                                    run{
                                                                        stopProgess()
                                                                        PrettyDialog(this@Reports)
                                                                                .setTitle("Información")
                                                                                .setMessage("Error al cargar reportes.")
                                                                                .show()
                                                                    }
                                                                }
                                                            }

                                                        }else{
                                                            Logger.d("Is not json object")
                                                            mHandler.post{
                                                                run{
                                                                    stopProgess()
                                                                    PrettyDialog(this@Reports)
                                                                            .setTitle("Información")
                                                                            .setMessage("No se pudo cargar el mapa.")
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
                    PrettyDialog(this@Reports)
                            .setTitle("Información")
                            .setMessage("Error ingresando")
                            .show()
                }
            }
        }
    }


    fun adaptar(nombre:String, fecha:String, valor:String, name:String) {
        runOnUiThread {
            val add = ListReportActive()
            add.nombre = nombre.toUpperCase()
            add.fecha = fecha
            add.valor = valor
            add.nombre_persona = name.toUpperCase()

            listActive.add(add)

            val list = findViewById<ListView>(R.id.list)
            adapter = CustomAdapter(this)
            list.adapter = adapter
        }
    }
    /*private fun ReportFinish() {
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
                            PrettyDialog(this@Reports)
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
                                                        PrettyDialog(this@Reports)
                                                                .setTitle("Información")
                                                                .setMessage("Error al cargar reportes " + e.message)
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

                                                                    val reports = ReportRequest()
                                                                    val _datos = ReportDatos()


                                                                    _datos.schema = AppConstants.REPORT_SCHEMA
                                                                    _datos.tabla = AppConstants.REPORT_TABLA
                                                                    _datos.campo = AppConstants.REPORT_CAMPO
                                                                    _datos.condiciones = AppConstants.REPORT_STATE_FINISH

                                                                    //Logger.d("Clave " + datos.clave)

                                                                    reports.datos = _datos


                                                                    val bodyMap = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(reports))

                                                                    val builderMap = Request.Builder()
                                                                    builderMap.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                                                    Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                                                    val requestReport = builderMap
                                                                            .header("Content-Type", "application/json; charset=UTF-8")
                                                                            .header("Accept", "application/json")
                                                                            .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                                            .header("operation", OperationConstants.MAP_OPERATION)
                                                                            .post(bodyMap)
                                                                            .build()


                                                                    client.newCall(requestReport).enqueue(object : Callback {
                                                                        override fun onFailure(call: Call, e: IOException) {

                                                                            e.printStackTrace()

                                                                            mHandler.post{
                                                                                run{
                                                                                    stopProgess()
                                                                                    PrettyDialog(this@Reports)
                                                                                            .setTitle("Información")
                                                                                            .setMessage("Error. " + e.message)
                                                                                            .show()
                                                                                }
                                                                            }

                                                                        }

                                                                        @Throws(IOException::class)
                                                                        override fun onResponse(call: Call, response: Response) {

                                                                            val responseMapString = response.body()!!.string()
                                                                            val reportResponse: ReportResponse

                                                                            Logger.d("Response Map")
                                                                            Logger.d(responseMapString)

                                                                            if (JSONUtils.isJSONValid(responseMapString)) {
                                                                                reportResponse =  mapper.readValue<ReportResponse>(responseMapString, ReportResponse::class.java)

                                                                                if(reportResponse?.response != null && reportResponse.response!!.dataresponse != null){
                                                                                    val mapp = jacksonObjectMapper()
                                                                                    val data_reports: List<ReportDataresponse> = mapp.readValue((reportResponse.response!!.dataresponse).toString())
                                                                                    //Lista de datos
                                                                                }
                                                                            }
                                                                        }
                                                                    })
                                                                }else{
                                                                    SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                                                    mHandler.post{
                                                                        run{
                                                                            stopProgess()
                                                                            PrettyDialog(this@Reports)
                                                                                    .setTitle("Información")
                                                                                    .setMessage("Error al cargar reportes.")
                                                                                    .show()
                                                                        }
                                                                    }
                                                                }

                                                            }else{
                                                                Logger.d("json object is null")
                                                                mHandler.post{
                                                                    run{
                                                                        stopProgess()
                                                                        PrettyDialog(this@Reports)
                                                                                .setTitle("Información")
                                                                                .setMessage("Error al cargar reportes.")
                                                                                .show()
                                                                    }
                                                                }
                                                            }

                                                        }else{
                                                            Logger.d("Is not json object")
                                                            mHandler.post{
                                                                run{
                                                                    stopProgess()
                                                                    PrettyDialog(this@Reports)
                                                                            .setTitle("Información")
                                                                            .setMessage("No se pudo cargar el mapa.")
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
////// FIN CONSULTA
            mHandler.post{
                run{
                    stopProgess()
                    PrettyDialog(this@Reports)
                            .setTitle("Información")
                            .setMessage("Error ingresando")
                            .show()
                }
            }
        }
    }*/
}
