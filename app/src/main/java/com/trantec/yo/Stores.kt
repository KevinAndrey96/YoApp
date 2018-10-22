package com.trantec.yo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.trantec.yo.R
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.afollestad.materialdialogs.MaterialDialog
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.*
import com.trantec.yo.utils.JSONUtils
import com.orhanobut.logger.Logger
import libs.mjn.prettydialog.PrettyDialog
import com.trantec.yo.constants.AppConstants
import com.trantec.yo.constants.HttpObjectsConstants
import com.trantec.yo.constants.OperationConstants
import com.trantec.yo.constants.WebConstant
import com.trantec.yo.dto.*
import hundredthirtythree.sessionmanager.SessionManager
import com.trantec.yo.enumeration.SessionKeys
import com.yopresto.app.yoprestoapp.dto.MapDatos
import com.yopresto.app.yoprestoapp.dto.MapRequest
import com.yopresto.app.yoprestoapp.dto.MapResponse

import okhttp3.*
import org.codehaus.jackson.map.ObjectMapper
import org.json.JSONObject
import java.io.IOException
import com.orhanobut.logger.Logger.json
import com.yopresto.app.yoprestoapp.dto.MapDataresponse
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker




class Stores : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var progressDialog: MaterialDialog? = null
    var mHandler =  Handler(Looper.getMainLooper())
    var context: Context? = null
    private var client = OkHttpClient()
    private val mapper = ObjectMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stores)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val yopresto = LatLng(4.676927, -74.129331)
        val zoomLevel = 16.0f;
        mMap.addMarker(MarkerOptions().position(yopresto).title("Yo Presto"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yopresto, zoomLevel))

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
                            PrettyDialog(this@Stores)
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
                                                        PrettyDialog(this@Stores)
                                                                .setTitle("Información")
                                                                .setMessage("Error al cargar mapa " + e.message)
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

                                                                    val Mapa = MapRequest()
                                                                    val _datos = MapDatos()


                                                                    _datos.schema = AppConstants.MAP_SCHEMA
                                                                    _datos.tabla = AppConstants.MAP_TABLA
                                                                    _datos.campo = AppConstants.MAP_CAMPO

                                                                    //Logger.d("Clave " + datos.clave)

                                                                    Mapa.datos = _datos


                                                                    val bodyMap = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(Mapa))

                                                                    val builderMap = Request.Builder()
                                                                    builderMap.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                                                    Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                                                    val requestMap = builderMap
                                                                            .header("Content-Type", "application/json; charset=UTF-8")
                                                                            .header("Accept", "application/json")
                                                                            .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                                            .header("operation", OperationConstants.MAP_OPERATION)
                                                                            .post(bodyMap)
                                                                            .build()


                                                                    client.newCall(requestMap).enqueue(object : Callback {
                                                                        override fun onFailure(call: Call, e: IOException) {

                                                                            e.printStackTrace()

                                                                            mHandler.post{
                                                                                run{
                                                                                    stopProgess()
                                                                                    PrettyDialog(this@Stores)
                                                                                            .setTitle("Información")
                                                                                            .setMessage("Error. " + e.message)
                                                                                            .show()
                                                                                }
                                                                            }

                                                                        }

                                                                        @Throws(IOException::class)
                                                                        override fun onResponse(call: Call, response: Response) {

                                                                            val responseMapString = response.body()!!.string()
                                                                            val mapResponse: MapResponse

                                                                            Logger.d("Response Map")
                                                                            Logger.d(responseMapString)

                                                                            if (JSONUtils.isJSONValid(responseMapString)) {
                                                                                mapResponse =  mapper.readValue<MapResponse>(responseMapString, MapResponse::class.java)

                                                                                if(mapResponse?.response != null && mapResponse.response!!.dataresponse != null){
                                                                                    val mapp = jacksonObjectMapper()
                                                                                    val data_maps: List<MapDataresponse> = mapp.readValue((mapResponse.response!!.dataresponse).toString())
//

                                                                                    for (item in data_maps) {
                                                                                        mHandler.post{
                                                                                            run{
                                                                                                stopProgess()
                                                                                                PrettyDialog(this@Stores)
                                                                                                        .setTitle("Información")
                                                                                                        .setMessage("Nfdgfdg" + item.nombreestablecimiento)
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
                                                                            PrettyDialog(this@Stores)
                                                                                    .setTitle("Información")
                                                                                    .setMessage("No se pudo cargar el mapa.")
                                                                                    .show()
                                                                        }
                                                                    }
                                                                }

                                                            }else{
                                                                Logger.d("json object is null")
                                                                mHandler.post{
                                                                    run{
                                                                        stopProgess()
                                                                        PrettyDialog(this@Stores)
                                                                                .setTitle("Información")
                                                                                .setMessage("No se pudo cargar el mapa.")
                                                                                .show()
                                                                    }
                                                                }
                                                            }

                                                        }else{
                                                            Logger.d("Is not json object")
                                                            mHandler.post{
                                                                run{
                                                                    stopProgess()
                                                                    PrettyDialog(this@Stores)
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
                    PrettyDialog(this@Stores)
                            .setTitle("Información")
                            .setMessage("Error ingresando")
                            .show()
                }
            }
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

    /*private fun createMarker(latitude: Double, longitude: Double, title: String): Marker {

        return mMap.addMarker(MarkerOptions()
                .position(LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title))
    }*/
}
