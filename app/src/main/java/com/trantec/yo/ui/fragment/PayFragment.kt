package com.trantec.yo.ui.fragment


import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.afollestad.materialdialogs.MaterialDialog
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.orhanobut.logger.Logger
import com.trantec.yo.R
import com.trantec.yo.YoPrestoApp
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

class PayFragment : Fragment() {

    private var progressDialog: MaterialDialog? = null
    var mHandler =  Handler(Looper.getMainLooper())
    var contexto: Context? = null
    private var client = OkHttpClient()
    private val mapper = ObjectMapper()
    var listActive = ArrayList<ListReportActive>()
    var adapter: CustomAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_to_pay, container, false)
       //ReportActive()
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

/*    private fun ReportActive() {
        Logger.d("Holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
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
                            PrettyDialog(contexto)
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
                                                        PrettyDialog(contexto)
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
                                                                                    PrettyDialog(contexto)
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
                                                                                Logger.d(reportResponse)
                                                                                if(reportResponse.status == true && reportResponse.response!!.dataresponse != null){
                                                                                    Logger.d(reportResponse.status)
                                                                                    val mapp = jacksonObjectMapper()
                                                                                    val result_report = reportResponse.response!!.dataresponse!!.toLowerCase()
                                                                                    val data_reports: List<ReportDataresponse> = mapp.readValue((result_report))
                                                                                    Logger.d(""+data_reports)

                                                                                    for (item in data_reports) {
                                                                                        adaptar(item.movimiento.toString(),item.fecha.toString(),item.valor.toString())
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
                                                                            PrettyDialog(contexto)
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
                                                                        PrettyDialog(contexto)
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
                                                                    PrettyDialog(contexto)
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
                    PrettyDialog(contexto)
                            .setTitle("Información")
                            .setMessage("Error ingresando")
                            .show()
                }
            }
        }
    }

    fun adaptar(nombre:String, fecha:String, valor:String) {
        getActivity()!!.runOnUiThread {
            val add = ListReportActive()
            add.nombre = nombre.toUpperCase()
            add.fecha = fecha
            add.valor = valor

            listActive.add(add)

            val list = getView()!!.findViewById<ListView>(R.id.list1)
            adapter = CustomAdapter(this)
            list.adapter = adapter
        }
    }*/
}
