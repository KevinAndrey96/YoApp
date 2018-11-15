package com.trantec.yo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
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
import com.google.zxing.integration.android.IntentIntegrator
import com.microblink.activity.BaseScanActivity
import com.trantec.yo.dto.*
import com.trantec.yo.utils.JSONUtils
import kotlinx.android.synthetic.main.activity_enrollment.*
import org.codehaus.jackson.map.ObjectMapper
import org.json.JSONException


import java.util.*
import com.orhanobut.logger.Logger
import com.trantec.yo.alerts.EnrollmentOk
import com.trantec.yo.constants.AppConstants
import com.trantec.yo.constants.HttpObjectsConstants
import com.trantec.yo.constants.OperationConstants
import com.trantec.yo.constants.WebConstant
import com.trantec.yo.enumeration.SessionKeys
import com.trantec.yo.ui.main.HomeActivity
import com.yopresto.app.yoprestoapp.dto.EnrollmentDataResponse
import com.yopresto.app.yoprestoapp.dto.EnrollmentRequest
import com.yopresto.app.yoprestoapp.dto.EnrollmentResponse
import com.yopresto.app.yoprestoapp.dto.MapDataresponse
import hundredthirtythree.sessionmanager.SessionManager
import libs.mjn.prettydialog.PrettyDialog
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class Enrollment : AppCompatActivity() {

    internal var btnScan: Button? = null
    internal var qrScanIntegrator: IntentIntegrator? = null
    val LICENSEMICROBLINK = "XBHOQRTB-HZ3XE3FF-K4FNTNCU-TTVWNPNM-JNYKDZO2-JUAXOZJH-3A74S463-I7KKLSOW"//esta licencia se debe solicitar a bytte para el funcionamiento en los documentos
    val MY_REQUEST_CODE_LISENCE = 1329
    val MY_REQUEST_CODE_FRONT = 1330
    val MY_REQUEST_CODE_BACK = 1331
    val MY_REQUEST_CODE_FRONTPAST = 1332
    val MY_REQUEST_CODE_FACECAPTURE = 1333
    val MY_REQUEST_CODE_BIOMETRIC = 1334
    val MY_REQUEST_CODE_NFCPAST = 1335
    val REQUEST_PERMISSION = 1433
    val URLPETICION = "https://portal.bytte.com.co/casb/SmartBio/SB/API/SmartBio/"//esta url se debe solicitar a bytte para licenciar el projecto

    //Datos cedula
    var _numerotarjeta: String? = null
    var _numerocedula: String? = null
    var _primerapellido: String? = null
    var _segundoapellido: String? = null
    var _primernombre: String? = null
    var _segundonombre: String? = null
    var _nombrecompletos: String? = null
    var _sexo: String? = null
    var _fechanacimiento: String? = null
    var _rh: String? = null
    var _tipodedo: String? = null
    var _versioncedula: String? = null
    var _barcodebase: String? = null
    var _platafomra: String? = null
    //Datos cedula frontal
    var _pathimagen: String? = null
    var _statusoperacion: String? = null

    var DocumentoQR: String? = null
    var YoPrestoQR: String? = null
    var QR1: String? = null
    var QR2: String? = null
    var QR3: String? = null
    var idinformacionpersona: String? = null
    var identidad: String? = null

    private var progressDialog: MaterialDialog? = null
    var mHandler =  Handler(Looper.getMainLooper())
    var context: Context? = null
    private var client = OkHttpClient()
    private val mapper = ObjectMapper()
    var aux: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrollment)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        aux = 0

        val cedulafront = findViewById<Button>(R.id.btnCedula)
        val reconocimientofacial = findViewById<Button>(R.id.button4)
        val escaner_huella = findViewById<Button>(R.id.button2)
        val cedulaback = findViewById<Button>(R.id.button7)
        val BtnSend = findViewById<Button>(R.id.btnSend)
        BtnSend.setEnabled(false)
        cedulaback.setEnabled(false)
        cedulafront.setEnabled(false)
        reconocimientofacial.setEnabled(false)
        escaner_huella.setEnabled(false)

        val intent = Intent(this@Enrollment, BytteLicense::class.java)
        intent.putExtra("URLPETICION", URLPETICION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivityForResult(intent, MY_REQUEST_CODE_LISENCE)

        val btnScan = findViewById<Button>(R.id.scannQr)
        btnScan!!.setOnClickListener { performAction() }

        qrScanIntegrator = IntentIntegrator(this)
        qrScanIntegrator?.setPrompt("Realice la lectura del código generado en la plataforma administrativa.")

        btnSend.setOnClickListener {
            enviarDatos()
            /*val intent = Intent(this, EnrollmentOk::class.java)
            startActivity(intent)
            finish()*/
        }

        btnCedula.setOnClickListener {
            //captura de frente doc
            var intent = Intent(this@Enrollment, CFrontDocument::class.java)
            intent.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
            intent.putExtra(BaseScanActivity.EXTRAS_LICENSE_KEY, LICENSEMICROBLINK)
            startActivityForResult(intent, MY_REQUEST_CODE_FRONT)
        }

        button7.setOnClickListener {
            //captura de back doc
            var intent = Intent(this@Enrollment, CBackDocument::class.java)
            intent.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
            intent.putExtra(BaseScanActivity.EXTRAS_LICENSE_KEY, LICENSEMICROBLINK)
            startActivityForResult(intent, MY_REQUEST_CODE_BACK)
        }

        button4.setOnClickListener {
            //rostro
            val intent = Intent(this@Enrollment, BytteCaptureFace::class.java)
            intent.putExtra(ValuesBiometric.EXTRAS_FACECAPTURE, "2")//factor de captura  0  facil 1  medio 2 dificil 3 muy dificil 0,1,2,3 deteccion por movimientos, 4,5,6 deteccion de rostro
            intent.putExtra("TIPO_CAMERA", "0")//0 capara frontal 1 camara posterior
            intent.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
            startActivityForResult(intent, MY_REQUEST_CODE_FACECAPTURE)
        }

        button2.setOnClickListener {
            //Huellas
            val intent = Intent(this@Enrollment, BytteFingerPrint::class.java)
            intent.putExtra("TipoHuella", "2")
            startActivityForResult(intent, MY_REQUEST_CODE_BIOMETRIC)
        }

        /*button8.setOnClickListener {
            val intent = Intent(this@Enrollment, BytteLicense::class.java)
            intent.putExtra("URLPETICION", URLPETICION)
            startActivityForResult(intent, MY_REQUEST_CODE_LISENCE)
        }*/



    }

    private fun performAction() {
        qrScanIntegrator?.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var results_biometric = ""
        val bitmap: Bitmap
        val gson = Gson()
        try {
            //LICENCIA
            if (requestCode == MY_REQUEST_CODE_LISENCE && resultCode == Activity.RESULT_OK) {
                results_biometric = data!!.extras!!.getString("License")
                Log.d("TAG", results_biometric)
            } else if (requestCode == MY_REQUEST_CODE_LISENCE && resultCode == Activity.RESULT_CANCELED) {
                results_biometric = data!!.extras!!.getString("License")
                Log.d("TAG", results_biometric)

            //CEDULA FRONTAL
            } else if (requestCode == MY_REQUEST_CODE_FRONT && resultCode == BaseScanActivity.RESULT_OK) {
                results_biometric = data?.extras?.getString("InfoFrontDoc")!!

                if (JSONUtils.isJSONValid(results_biometric)) {
                    val escaner_front: EscanerFront
                    val res_ = results_biometric.toLowerCase()

                    escaner_front =  mapper.readValue<EscanerFront>(res_, EscanerFront::class.java)

                    _pathimagen = escaner_front.pathimagen
                    _statusoperacion = escaner_front.statusoperacion

                    val cedulafront = findViewById<Button>(R.id.btnCedula)
                    cedulafront.setEnabled(false)

                    cedulafront.setBackgroundResource(R.drawable.rounded_button2)

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Atención")
                    builder.setMessage("Escaneo exitoso")
                    builder.setPositiveButton("OK", null)
                    builder.create()
                    builder.show()

                    aux = aux!! + 1
                    validarDatos()
                }

            } else if (requestCode == MY_REQUEST_CODE_FRONT && resultCode == BaseScanActivity.RESULT_CANCELED) {
                results_biometric = data?.extras?.getString("InfoFrontDoc")!!

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Atención")
                builder.setMessage("Inténtelo de nuevo")
                builder.setPositiveButton("OK", null)
                builder.create()
                builder.show()

                //RECONOCIMIENTO FACIAL
            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_OK) {
                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                val properties = gson.fromJson(results_biometric, Properties::class.java)

                Log.d("ReconocimientoFacial", results_biometric)

                val reconocimientofacial = findViewById<Button>(R.id.button4)
                reconocimientofacial.setEnabled(false)

                reconocimientofacial.setBackgroundResource(R.drawable.rounded_button2)

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Atención")
                builder.setMessage("Escaneo exitoso")
                builder.setPositiveButton("OK", null)
                builder.create()
                builder.show()

                aux = aux!! + 1
                validarDatos()

            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_CANCELED) {
                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Atención")
                builder.setMessage("Inténtelo de nuevo")
                builder.setPositiveButton("OK", null)
                builder.create()
                builder.show()

                //HUELLAS
            } else if (requestCode == MY_REQUEST_CODE_BIOMETRIC && resultCode == BaseScanActivity.RESULT_OK) {//
                results_biometric = data?.extras?.getString("InfoBiometric")!!
                Log.d("Huellas", results_biometric)

                val escaner_huella = findViewById<Button>(R.id.button2)
                escaner_huella.setEnabled(false)

                escaner_huella.setBackgroundResource(R.drawable.rounded_button2)

                aux = aux!! + 1
                validarDatos()

                if (JSONUtils.isJSONValid(results_biometric)) {
                    val scanner_huella: ScannerHuellas
                    val res_ = results_biometric.toLowerCase()

                    scanner_huella =  mapper.readValue<ScannerHuellas>(res_, ScannerHuellas::class.java)
                    Log.d("Huellasqqqq", ""+scanner_huella)
                    val mapp = jacksonObjectMapper()
                    val huellas: List<Fingers> = mapp.readValue((scanner_huella.fingerprintsobjects).toString())
                }

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Atención")
                builder.setMessage("Escaneo exitoso")
                builder.setPositiveButton("OK", null)
                builder.create()
                builder.show()

                //CEDULA REVERSO
            }else if (requestCode == MY_REQUEST_CODE_BACK && resultCode == BaseScanActivity.RESULT_OK) {//back del documento colombiano
                results_biometric = data?.extras?.getString("InfoBackDoc")!!

                if (JSONUtils.isJSONValid(results_biometric)) {
                    val escaner_back: EscanerBack
                    val res_ = results_biometric.toLowerCase()

                    escaner_back =  mapper.readValue<EscanerBack>(res_, EscanerBack::class.java)

                    _numerotarjeta = escaner_back.numerotarjeta
                    _numerocedula = escaner_back.numerocedula
                    _primerapellido = escaner_back.primerapellido
                    _segundoapellido = escaner_back.segundoapellido
                    _primernombre = escaner_back.primernombre
                    _segundonombre = escaner_back.segundonombre
                    _nombrecompletos = escaner_back.nombrescompletos
                    _sexo = escaner_back.sexo
                    _fechanacimiento = escaner_back.fechanacimiento
                    _rh = escaner_back.rh
                    _tipodedo = escaner_back.tipodedo
                    _versioncedula = escaner_back.versioncedula
                    _barcodebase = escaner_back.barcodebase64
                    //var _pathimagen: String? = null
                    _platafomra = escaner_back.plataforma

                    val cedulaback = findViewById<Button>(R.id.button7)
                    cedulaback.setEnabled(false)

                    cedulaback.setBackgroundResource(R.drawable.rounded_button2)

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Atención")
                    builder.setMessage("Escaneo exitoso")
                    builder.setPositiveButton("OK", null)
                    builder.create()
                    builder.show()

                    aux = aux!! + 1
                    validarDatos()
                }
            }

        } catch (e: Exception) {

        }

        //CODIGO QR
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {

            if (result.contents == null) {
                Toast.makeText(this, getString(R.string.result_not_found), Toast.LENGTH_LONG).show()
            } else {
                try {
                    val string = result.contents
                    val tam = string.length

                    if (tam > 30){

                        YoPrestoQR = string.substring(tam-8, tam)

                        if(YoPrestoQR == "yopresto"){
                            val parts = string.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            DocumentoQR = parts[0]
                            QR1 = parts[1]
                            QR2 = parts[2]
                            idinformacionpersona = parts[1].substring(6,11)
                            identidad = QR2

                            val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                            val editor = prefs.edit()
                            editor.putString("enrollment_idinformacionpersona", idinformacionpersona)
                            editor.putString("enrollment_identidad", identidad)
                            editor.putString("enrollment_doc_val", DocumentoQR)
                            editor.commit()

                            /*val intent = Intent(this, ScannerQRSuccess::class.java)
                            startActivity(intent)*/
                            Toast.makeText(this, "Se ha escaneado el codigo exitosamente.", Toast.LENGTH_LONG).show()

                            val cedulafront = findViewById<Button>(R.id.btnCedula)
                            val reconocimientofacial = findViewById<Button>(R.id.button4)
                            val escaner_huella = findViewById<Button>(R.id.button2)
                            val cedulaback = findViewById<Button>(R.id.button7)
                            cedulaback.setEnabled(true)
                            cedulafront.setEnabled(true)
                            reconocimientofacial.setEnabled(true)
                            escaner_huella.setEnabled(true)

                            val btnScan = findViewById<Button>(R.id.scannQr)
                            btnScan.setBackgroundResource(R.drawable.rounded_button2)
                            btnScan.setEnabled(false)

                            aux = aux!! + 1
                            validarDatos()

                        } else {
                            Toast.makeText(this, "Hubo un error, por favor inténtelo de nuevo", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Hubo un error, por favor inténtelo de nuevo", Toast.LENGTH_LONG).show()
                    }



                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Hubo un error, por favor inténtelo de nuevo", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /*private fun enviarDatos(){
        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val ip = prefs.getString("ip", "")
        val idusuario = prefs.getString("idusuario", "")

        //Informacion cedula
        val infoDocument = EnrollmentDatosCedula()
        infoDocument.idusuario = idusuario.toInt()
        infoDocument.ip = ip
        infoDocument.identidad
        infoDocument.idcleinte
        infoDocument.accion = 1
        infoDocument.numerotarjeta = _numerotarjeta
        infoDocument.numerocedula = _numerocedula
        infoDocument.primerapellido = _primerapellido
        infoDocument.segundoapellido = _segundoapellido
        infoDocument.primernombre = _primernombre
        infoDocument.segundonombre = _segundonombre
        infoDocument.nombrecompletos = _nombrecompletos
        infoDocument.sexo = _sexo
        infoDocument.fechanacimiento = _fechanacimiento
        infoDocument.rh = _rh
        infoDocument.tipodedo = _tipodedo
        infoDocument.versioncedula = _versioncedula
        infoDocument.barcodebase = _barcodebase
        infoDocument.pathimagen
        infoDocument.platafomra = _platafomra

        //Cedula frontal
        val infoDocumentFront = EnrollmentDocumentoFrontal()
        infoDocumentFront.idusuario = idusuario.toInt()
        infoDocumentFront.ip = ip
        infoDocumentFront.identidad
        infoDocumentFront.idcleinte
        infoDocumentFront.accion = 2
        infoDocumentFront.estatusoperacion = _statusoperacion
        infoDocumentFront.pathimagen = _pathimagen

        //Huellas
        val infoFingerPrints = EnrollmentHuellas()

        //Cara
        val infoFace = EnrollmentReconocimientoFacial()
    }*/

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

    private fun validarDatos(){
        if(aux == 5){
            val BtnSend = findViewById<Button>(R.id.btnSend)
            BtnSend.setEnabled(true)
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
                            PrettyDialog(this@Enrollment)
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
                                                        PrettyDialog(this@Enrollment)
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
                                                                    _datos.escaneo = ""
                                                                    _datos.templatecc = ""
                                                                    _datos.templatefrontal = ""
                                                                    _datos.templatetracera = ""

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
                                                                                    PrettyDialog(this@Enrollment)
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
                                                                                    .replace("\"response\": {\"dataresponse\":{\"@nil\":\"true\"},\"response\":\"LA ENTIDAD YA SE ENCUENTRA ENROLADA EXITOSAMENTE\",\"idresponse\":78},","")
                                                                            val enrollmentResponse: EnrollmentResponse

                                                                            Logger.d("Respu:"+responseMapString)

                                                                            if (JSONUtils.isJSONValid(responseMapString)) {

                                                                                enrollmentResponse =  mapper.readValue<EnrollmentResponse>(responseMapString, EnrollmentResponse::class.java)

                                                                                if(enrollmentResponse?.response != null){
                                                                                    if (enrollmentResponse.response!!.dataresponse != null){
                                                                                        val enrollDataresponse: EnrollmentDataResponse = mapper.readValue<EnrollmentDataResponse>(enrollmentResponse.response!!.dataresponse, EnrollmentDataResponse::class.java)

                                                                                        if(enrollmentResponse.status!! && enrollDataresponse != null){
                                                                                            mHandler.post{
                                                                                                run{
                                                                                                    stopProgess()
                                                                                                    PrettyDialog(this@Enrollment)
                                                                                                            .setTitle("Información")
                                                                                                            .setMessage("Se ha hecho el enrolamiento exitosamente")
                                                                                                            .show()
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            mHandler.post{
                                                                                                run{
                                                                                                    stopProgess()
                                                                                                    PrettyDialog(this@Enrollment)
                                                                                                            .setTitle("Información")
                                                                                                            .setMessage(enrollmentResponse.message)
                                                                                                            .show()
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        mHandler.post{
                                                                                            run{
                                                                                                stopProgess()
                                                                                                PrettyDialog(this@Enrollment)
                                                                                                        .setTitle("Información")
                                                                                                        .setMessage(enrollmentResponse.message)
                                                                                                        .show()
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    mHandler.post{
                                                                                        run{
                                                                                            stopProgess()
                                                                                            PrettyDialog(this@Enrollment)
                                                                                                    .setTitle("Información")
                                                                                                    .setMessage("Ha ocurrido un error, por favor intente de nuevo.")
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
                                                                            PrettyDialog(this@Enrollment)
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
                                                                        PrettyDialog(this@Enrollment)
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
                                                                    PrettyDialog(this@Enrollment)
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
                    PrettyDialog(this@Enrollment)
                            .setTitle("Información")
                            .setMessage("Error ingresando")
                            .show()
                }
            }
        }
    }
}