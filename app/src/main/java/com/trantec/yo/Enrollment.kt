package com.trantec.yo

import org.json.JSONObject
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bytte.biometricbytte.bfactor.face.BytteCaptureFace
import com.bytte.biometricbytte.bfactor.huella.BytteFingerPrint
import com.bytte.biometricbytte.bfactor.license.BytteLicense
import com.bytte.biometricbytte.bfactor.license.BytteLicenseA
import com.bytte.docbytte.BiometricCamera.ValuesBiometric
import com.bytte.docbytte.ui.CBackDocument
import com.bytte.docbytte.ui.CFrontDocument
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
import com.trantec.yo.alerts.EnrollmentNo
import com.trantec.yo.alerts.EnrollmentOk
import com.trantec.yo.constants.AppConstants
import com.trantec.yo.constants.HttpObjectsConstants
import com.trantec.yo.constants.OperationConstants
import com.trantec.yo.constants.WebConstant
import com.trantec.yo.enumeration.SessionKeys
import com.trantec.yo.services.bytte.authentication.*
import com.yopresto.app.yoprestoapp.dto.*
import hundredthirtythree.sessionmanager.SessionManager
import libs.mjn.prettydialog.PrettyDialog
import okhttp3.*
import okhttp3.Response
import java.io.File
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
    //Reconocimiento Facial
    var estatusoperacion : String? = null
    var mensajeretorno : String? = null
    var imagenpath : String? = null
    var imagentemplate : String? = null
    //Huellas
    var minutia: String? = null
    var fingerprint: String? = null
    var pathbitmap: String? = null
    var pathwsq: String? = null
    //Imagenes huellas
    var imghuellaone: ByteArray? = null
    var imghuellatwo: ByteArray? = null
    var imghuellathree: ByteArray? = null
    var imghuellafour: ByteArray? = null

    //Images
    var _imageFront: File? = null
    var _imageBack: File? = null

    var DocumentoQR: String? = null
    var YoPrestoQR: String? = null
    var QR1: String? = null
    var QR2: String? = null
    var QR3: String? = null
    var idinformacionpersona: String? = null
    var identidad: String? = null
    var idusuario: String? = null

    private var progressDialog: MaterialDialog? = null
    var mHandler = Handler(Looper.getMainLooper())
    var context: Context? = null
    private var client = OkHttpClient()
    private val mapper = ObjectMapper()
    var aux: Int? = null
    val  license = BytteLicenseA()

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
        BtnSend.isEnabled = false
        cedulaback.isEnabled = false
        cedulafront.isEnabled = false
        reconocimientofacial.isEnabled = false
        escaner_huella.isEnabled = false

        try{
            val lis = license.activarlicensia(URLPETICION,applicationContext,this@Enrollment)

            Logger.d("LIC $lis")
        }catch (ex: Exception){
            ex.printStackTrace()
        }


        val intent1 = Intent(this@Enrollment, BytteLicense::class.java)
        intent1.putExtra("URLPETICION", URLPETICION)
        startActivityForResult(intent1, MY_REQUEST_CODE_LISENCE)

        val btnScan = findViewById<Button>(R.id.scannQr)
        btnScan!!.setOnClickListener { performAction() }

        qrScanIntegrator = IntentIntegrator(this)
        qrScanIntegrator?.setPrompt("Realice la lectura del código generado en la plataforma administrativa.")

        btnSend.setOnClickListener {
            requestVerificationToBytte()
        }

        btnCedula.setOnClickListener {
            //captura de frente doc
            val inten = Intent(this@Enrollment, CFrontDocument::class.java)
            inten.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
            inten.putExtra(BaseScanActivity.EXTRAS_LICENSE_KEY, LICENSEMICROBLINK)
            inten.putExtra("EXTRAS_TIMEOUT", "20")
            startActivityForResult(inten, MY_REQUEST_CODE_FRONT)
        }

        button7.setOnClickListener {
            //captura de back doc
            val inten = Intent(this@Enrollment, CBackDocument::class.java)
            inten.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
            inten.putExtra(BaseScanActivity.EXTRAS_LICENSE_KEY, LICENSEMICROBLINK)
            inten.putExtra("EXTRAS_TIMEOUT", "20")
            startActivityForResult(inten, MY_REQUEST_CODE_BACK)
        }

        button4.setOnClickListener {
            //rostro
            val builder = AlertDialog.Builder(this@Enrollment)
            builder.setTitle("Escaner Facial")
            builder.setIcon(R.drawable.reconocimiento_facial)
            builder.setMessage("¿Que cámara deseas usar para realizar el escaner facial?")

            builder.setPositiveButton("Cámara Frontal"){ _, _ ->
                openCameraFront()
            }
            builder.setNegativeButton("Cámara Posterior"){ _, _ ->
                openCameraBack()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        button2.setOnClickListener {
            //Huellas
            val inten = Intent(this@Enrollment, BytteFingerPrint::class.java)
            inten.putExtra("TipoHuella", "2")
            startActivityForResult(inten, MY_REQUEST_CODE_BIOMETRIC)
        }
    }

    private fun openCameraFront(){
        val intent = Intent(this@Enrollment, BytteCaptureFace::class.java)
        intent.putExtra(ValuesBiometric.EXTRAS_FACECAPTURE, "4")//factor de captura  0  facil 1  medio 2 dificil 3 muy dificil 0,1,2,3 deteccion por movimientos, 4,5,6 deteccion de rostro
        intent.putExtra("TIPO_CAMERA", "0")//0 camara frontal 1 camara posterior
        intent.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
        startActivityForResult(intent, MY_REQUEST_CODE_FACECAPTURE)
    }

    private fun openCameraBack(){
        val intent = Intent(this@Enrollment, BytteCaptureFace::class.java)
        intent.putExtra(ValuesBiometric.EXTRAS_FACECAPTURE, "4")//factor de captura  0  facil 1  medio 2 dificil 3 muy dificil 0,1,2,3 deteccion por movimientos, 4,5,6 deteccion de rostro
        intent.putExtra("TIPO_CAMERA", "1")//0 camara frontal 1 camara posterior
        intent.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
        startActivityForResult(intent, MY_REQUEST_CODE_FACECAPTURE)
    }

    private fun performAction() {
        qrScanIntegrator?.initiateScan()
    }

    private fun redirect() {
        val intent = Intent(this, EnrollmentOk::class.java)
        startActivity(intent)
        finish()
    }

    private fun redirectno() {
        val intent = Intent(this, EnrollmentNo::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val results_biometric: String
        //val bitmap: Bitmap
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

                val lis = license.activarlicensia(URLPETICION,applicationContext,this@Enrollment)

                Logger.d("LIC $lis")

                results_biometric = data?.extras?.getString("InfoFrontDoc")!!

                if (JSONUtils.isJSONValid(results_biometric)) {
                    val escaner_front: EscanerFront
                    val res_ = results_biometric.toLowerCase()

                    Logger.d("RES --> $res_")

                    escaner_front = mapper.readValue<EscanerFront>(res_, EscanerFront::class.java)

                    _statusoperacion = escaner_front.statusoperacion

                    Logger.d("Imagen path front $escaner_front.pathimagen!!")

                    _imageFront = File(escaner_front.pathimagen!!)

                    enviarPaquete2()

                    val cedulafront = findViewById<Button>(R.id.btnCedula)
                    cedulafront.isEnabled = false

                    cedulafront.setBackgroundResource(R.drawable.rounded_button2)

                    aux = aux!! + 1
                    validarDatos()
                }

            } else if (requestCode == MY_REQUEST_CODE_FRONT && resultCode == BaseScanActivity.RESULT_CANCELED) {

                val lis = license.activarlicensia(URLPETICION,applicationContext,this@Enrollment)

                Logger.d("LIC $lis")

                results_biometric = data?.extras?.getString("InfoFrontDoc")!!

                mHandler.post {
                    run {
                        stopProgess()
                        PrettyDialog(this@Enrollment)
                                .setTitle("Información")
                                .setMessage("Intentelo de nuevo")
                                .show()
                    }
                }

                error()

            //RECONOCIMIENTO FACIAL
            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_OK) {

                val lis = license.activarlicensia(URLPETICION,applicationContext,this@Enrollment)

                Logger.d("LIC $lis")

                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                val properties = gson.fromJson(results_biometric, Properties::class.java)

                Log.d("ReconocimientoFacial", results_biometric)

                if (JSONUtils.isJSONValid(results_biometric)) {
                    val reconocimiento_facial: ReconocimientoFacialDatos
                    val res_ = results_biometric.toLowerCase()

                    reconocimiento_facial = mapper.readValue<ReconocimientoFacialDatos>(res_, ReconocimientoFacialDatos::class.java)
                    Logger.d(reconocimiento_facial.mensajeoriginal)

                    estatusoperacion = reconocimiento_facial.statusoperacion
                    mensajeretorno = reconocimiento_facial.mensajeoriginal
                    imagenpath  = reconocimiento_facial.imagepath
                    imagentemplate = reconocimiento_facial.imagetemplate

                    enviarPaquete4()

                    val reconocimientofacial = findViewById<Button>(R.id.button4)
                    reconocimientofacial.isEnabled = false

                    reconocimientofacial.setBackgroundResource(R.drawable.rounded_button2)

                    aux = aux!! + 1
                    validarDatos()
                }

            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_CANCELED) {

                val lis = license.activarlicensia(URLPETICION,applicationContext,this@Enrollment)

                Logger.d("LIC $lis")

                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                mHandler.post {
                    run {
                        stopProgess()
                        PrettyDialog(this@Enrollment)
                                .setTitle("Información")
                                .setMessage("Intentelo de nuevo")
                                .show()
                    }
                }
                error()

            //HUELLAS
            } else if (requestCode == MY_REQUEST_CODE_BIOMETRIC && resultCode == BaseScanActivity.RESULT_OK) {//

                val lis = license.activarlicensia(URLPETICION,applicationContext,this@Enrollment)

                Logger.d("LIC $lis")

                results_biometric = data?.extras?.getString("InfoBiometric")!!

                val res_ = results_biometric.toLowerCase()

                val jsonObj = JSONObject(res_.substring(res_.indexOf("{"), res_.lastIndexOf("}") + 1))
                val fingers = jsonObj.getJSONArray("fingerprintsobjects")

                Logger.d("1"+fingers.getJSONObject(0).getString("pathbitmap"))
                Logger.d("2"+fingers.getJSONObject(1).getString("pathbitmap"))
                Logger.d("3"+fingers.getJSONObject(2).getString("pathbitmap"))
                Logger.d("4"+fingers.getJSONObject(3).getString("pathbitmap"))


                var img1 = File(fingers.getJSONObject(0).getString("pathbitmap"))
                var img2 = File(fingers.getJSONObject(1).getString("pathbitmap"))
                var img3 = File(fingers.getJSONObject(2).getString("pathbitmap"))
                var img4 = File(fingers.getJSONObject(3).getString("pathbitmap"))

                imghuellaone = org.apache.commons.io.FileUtils.readFileToByteArray(img1)
                imghuellatwo = org.apache.commons.io.FileUtils.readFileToByteArray(img2)
                imghuellathree = org.apache.commons.io.FileUtils.readFileToByteArray(img3)
                imghuellafour = org.apache.commons.io.FileUtils.readFileToByteArray(img4)
                
                for (i in 0 until fingers!!.length()) {
                    //val scanner = Fingers()
                    minutia = fingers.getJSONObject(i).getString("minutia")
                    fingerprint = fingers.getJSONObject(i).getString("fingerprint")
                    pathbitmap = fingers.getJSONObject(i).getString("pathbitmap")
                    pathwsq = fingers.getJSONObject(i).getString("pathwsq")

                    enviarPaquete3()
                }

                val escaner_huella = findViewById<Button>(R.id.button2)
                escaner_huella.isEnabled = false

                escaner_huella.setBackgroundResource(R.drawable.rounded_button2)

                aux = aux!! + 1
                validarDatos()

                mHandler.post {
                    run {
                        stopProgess()
                        PrettyDialog(this@Enrollment)
                                .setTitle("Información")
                                .setMessage("Escaneo exitoso")
                                .show()
                    }
                }

                //CEDULA REVERSO
            } else if (requestCode == MY_REQUEST_CODE_BACK && resultCode == BaseScanActivity.RESULT_OK) {//back del documento colombiano

                val lis = license.activarlicensia(URLPETICION,applicationContext,this@Enrollment)

                Logger.d("LIC $lis")

                results_biometric = data?.extras?.getString("InfoBackDoc")!!

                if (JSONUtils.isJSONValid(results_biometric)) {

                    val escaner_back: EscanerBack
                    val res_ = results_biometric.toLowerCase()

                    Logger.d("Res reverso $res_")

                    //val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)

                    escaner_back = mapper.readValue<EscanerBack>(res_, EscanerBack::class.java)

                    if (escaner_back.numerocedula == DocumentoQR){

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

                        Logger.d("Fecha Nac:" + escaner_back.fechanacimiento)
                        Logger.d("Path imagen " + escaner_back.pathimagen)

                        _imageBack = File(escaner_back.pathimagen)

                        enviarPaquete1()

                        val cedulaback = findViewById<Button>(R.id.button7)
                        cedulaback.isEnabled = false

                        cedulaback.setBackgroundResource(R.drawable.rounded_button2)

                        aux = aux!! + 1
                        validarDatos()

                    } else {
                        mHandler.post {
                            run {
                                stopProgess()
                                PrettyDialog(this@Enrollment)
                                        .setTitle("Información")
                                        .setMessage("Este documento no es valido para el QR escaneado")
                                        .show()
                            }
                        }
                        error()
                    }
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

                    if (tam > 30) {

                        YoPrestoQR = string.substring(tam - 8, tam)

                        if (YoPrestoQR == "yopresto") {
                            val parts = string.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            //val t_doc = parts[0].length
                            DocumentoQR = parts[0]//.substring(7, t_doc)

                            if (DocumentoQR!!.length == 9)
                                DocumentoQR = "0$DocumentoQR"

                            if (DocumentoQR!!.length == 8)
                                DocumentoQR = "00$DocumentoQR"

                            if (DocumentoQR!!.length == 8)
                                DocumentoQR = "000$DocumentoQR"

                            QR1 = parts[1]
                            QR2 = parts[2]
                            idusuario = parts[1].substring(0, 5)
                            idinformacionpersona = parts[1].substring(6, 11)
                            identidad = QR2
                            val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                            val editor = prefs.edit()
                            editor.putString("enrollment_idinformacionpersona", idinformacionpersona)
                            editor.putString("enrollment_identidad", identidad)
                            editor.putString("enrollment_doc_val", DocumentoQR)
                            editor.putString("enrollment_idusuario", idusuario)
                            editor.apply()

                            /*val intent = Intent(this, ScannerQRSuccess::class.java)
                            startActivity(intent)*/
                            Toast.makeText(this, "Se ha escaneado el codigo exitosamente.", Toast.LENGTH_LONG).show()

                            val cedulafront = findViewById<Button>(R.id.btnCedula)
                            val reconocimientofacial = findViewById<Button>(R.id.button4)
                            val escaner_huella = findViewById<Button>(R.id.button2)
                            val cedulaback = findViewById<Button>(R.id.button7)
                            cedulaback.isEnabled = true
                            cedulafront.isEnabled = true
                            reconocimientofacial.isEnabled = true
                            escaner_huella.isEnabled = true

                            val btnScan = findViewById<Button>(R.id.scannQr)
                            btnScan.setBackgroundResource(R.drawable.rounded_button2)
                            btnScan.isEnabled = false

                            aux = aux!! + 1
                            validarDatos()

                        } else {
                            Toast.makeText(this, "Hubo un error, por favor inténtelo de nuevo", Toast.LENGTH_LONG).show()
                            error()
                        }
                    } else {
                        Toast.makeText(this, "Hubo un error, por favor inténtelo de nuevo", Toast.LENGTH_LONG).show()
                        error()
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Hubo un error, por favor inténtelo de nuevo", Toast.LENGTH_LONG).show()
                    error()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun enviarPaquete1() {

        try {
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
            mHandler.post {
                run {
                    startProgress()
                }
            }

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    e.printStackTrace()
                    Logger.d("Error generando token")
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
                            if (tokenJSONObject != null) {

                                tokenResponse = mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)

                                if (tokenResponse != null) {
                                    Logger.d("Token response on object")
                                    Logger.d(tokenResponse.access_token)

                                    if (tokenResponse.access_token != null) {
                                        SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenResponse.access_token)

                                        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                                        val ip = prefs.getString("ip", "")
                                        val idusuario = prefs.getString("enrollment_idusuario", "")
                                        val idinformacionpersona = prefs.getString("enrollment_idinformacionpersona", "")
                                        val identidad = prefs.getString("enrollment_identidad", "")

                                        val infoDocument = EnrollmentDatosCedula()
                                        val paq1 = EnrollmentDatosCedulaRequest()
                                        infoDocument.idusuario = idusuario
                                        infoDocument.ip = ip
                                        infoDocument.identidad = identidad
                                        infoDocument.idcliente = idinformacionpersona
                                        infoDocument.accion = "1"
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
                                        infoDocument.pathimagen = ""
                                        infoDocument.platafomra = _platafomra

                                        paq1.datos = infoDocument

                                        val bodyMap = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(paq1))

                                        val builderMap = Request.Builder()
                                        builderMap.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                        Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                        val requestReport = builderMap
                                                .header("Content-Type", "application/json; charset=UTF-8")
                                                .header("Accept", "application/json")
                                                .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                .header("operation", OperationConstants.PACKAGE)
                                                .post(bodyMap)
                                                .build()


                                        client.newCall(requestReport).enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {

                                                e.printStackTrace()

                                                mHandler.post {
                                                    run {
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

                                                val responseMapString = response.body()!!.string()
                                                val enrollmentCedulaResponse: EnrollmentDatosCedulaResponse

                                                Logger.d("Respu:$responseMapString")

                                                if (JSONUtils.isJSONValid(responseMapString)) {

                                                    enrollmentCedulaResponse = mapper.readValue<EnrollmentDatosCedulaResponse>(responseMapString, EnrollmentDatosCedulaResponse::class.java)

                                                    if (enrollmentCedulaResponse?.response != null) {
                                                        if (enrollmentCedulaResponse.status!! && enrollmentCedulaResponse != null && enrollmentCedulaResponse.status!!) {
                                                            mHandler.post {
                                                                run {
                                                                    stopProgess()
                                                                    PrettyDialog(this@Enrollment)
                                                                            .setTitle("Información")
                                                                            .setMessage("Escaneo exitoso")
                                                                            .show()
                                                                }
                                                            }

                                                        } else {
                                                            mHandler.post {
                                                                run {
                                                                    stopProgess()
                                                                    PrettyDialog(this@Enrollment)
                                                                            .setTitle("Información")
                                                                            .setMessage(enrollmentCedulaResponse.message)
                                                                            .show()
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        mHandler.post {
                                                            run {
                                                                stopProgess()
                                                                PrettyDialog(this@Enrollment)
                                                                        .setTitle("Información")
                                                                        .setMessage("Ha ocurrido un error, por favor intente de nuevo--.")
                                                                        .show()
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        })

                                    } else {
                                        SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                        mHandler.post {
                                            run {
                                                stopProgess()
                                            }
                                        }
                                    }

                                } else {
                                    Logger.d("json object is null")
                                    mHandler.post {
                                        run {
                                            stopProgess()
                                        }
                                    }
                                }

                            } else {
                                Logger.d("Is not json object")
                                mHandler.post {
                                    run {
                                        stopProgess()
                                    }
                                }
                            }
                        }
                    }
                }
            })

        } catch (ex: Exception) {
            ex.printStackTrace()
            mHandler.post {
                run {
                    stopProgess()
                    PrettyDialog(this@Enrollment)
                            .setTitle("Información")
                            .setMessage("Error ingresando")
                            .show()
                }
            }
        }
    }

    private fun enviarPaquete2() {

        try {
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
            mHandler.post {
                run {
                    startProgress()
                }
            }

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    e.printStackTrace()
                    Logger.d("Error generando token")
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
                            if (tokenJSONObject != null) {

                                tokenResponse = mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)

                                if (tokenResponse != null) {
                                    Logger.d("Token response on object")
                                    Logger.d(tokenResponse.access_token)

                                    if (tokenResponse.access_token != null) {
                                        SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenResponse.access_token)

                                        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                                        val ip = prefs.getString("ip", "")
                                        val idusuario = prefs.getString("enrollment_idusuario", "")
                                        val idinformacionpersona = prefs.getString("enrollment_idinformacionpersona", "")
                                        val identidad = prefs.getString("enrollment_identidad", "")

                                        val infoDocumentFront = EnrollmentDocumentoFrontal()
                                        val paq2 = EnrollmentDocumentoRequest()
                                        infoDocumentFront.idusuario = idusuario
                                        infoDocumentFront.ip = ip
                                        infoDocumentFront.identidad = identidad
                                        infoDocumentFront.idcliente = idinformacionpersona
                                        infoDocumentFront.accion = "2"
                                        infoDocumentFront.estatusoperacion = _statusoperacion
                                        infoDocumentFront.pathimagen = _pathimagen

                                        paq2.datos = infoDocumentFront

                                        val bodyMap = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(paq2))

                                        val builderMap = Request.Builder()
                                        builderMap.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                        Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                        val requestReport = builderMap
                                                .header("Content-Type", "application/json; charset=UTF-8")
                                                .header("Accept", "application/json")
                                                .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                .header("operation", OperationConstants.PACKAGE)
                                                .post(bodyMap)
                                                .build()


                                        client.newCall(requestReport).enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {

                                                e.printStackTrace()

                                                mHandler.post {
                                                    run {
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

                                                val responseMapString = response.body()!!.string()
                                                val enrollmentCedulaResponse: EnrollmentDatosCedulaResponse

                                                Logger.d("Respu:$responseMapString")

                                                if (JSONUtils.isJSONValid(responseMapString)) {

                                                    enrollmentCedulaResponse = mapper.readValue<EnrollmentDatosCedulaResponse>(responseMapString, EnrollmentDatosCedulaResponse::class.java)

                                                    if (enrollmentCedulaResponse?.response != null) {
                                                        if (enrollmentCedulaResponse.status!! && enrollmentCedulaResponse != null && enrollmentCedulaResponse.status!!) {
                                                            mHandler.post {
                                                                run {
                                                                    stopProgess()
                                                                    PrettyDialog(this@Enrollment)
                                                                            .setTitle("Información")
                                                                            .setMessage("Escaneo exitoso")
                                                                            .show()
                                                                }
                                                            }

                                                        } else {
                                                            mHandler.post {
                                                                run {
                                                                    stopProgess()
                                                                    PrettyDialog(this@Enrollment)
                                                                            .setTitle("Información")
                                                                            .setMessage(enrollmentCedulaResponse.message)
                                                                            .show()
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        mHandler.post {
                                                            run {
                                                                stopProgess()
                                                                PrettyDialog(this@Enrollment)
                                                                        .setTitle("Información")
                                                                        .setMessage("Ha ocurrido un error, por favor intente de nuevo--.")
                                                                        .show()
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        })

                                    } else {
                                        SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                        mHandler.post {
                                            run {
                                                stopProgess()
                                            }
                                        }
                                    }

                                } else {
                                    Logger.d("json object is null")
                                    mHandler.post {
                                        run {
                                            stopProgess()
                                        }
                                    }
                                }

                            } else {
                                Logger.d("Is not json object")
                                mHandler.post {
                                    run {
                                        stopProgess()
                                    }
                                }
                            }
                        }
                    }
                }
            })

        } catch (ex: Exception) {
            ex.printStackTrace()
            mHandler.post {
                run {
                    stopProgess()
                    PrettyDialog(this@Enrollment)
                            .setTitle("Información")
                            .setMessage("Error ingresando")
                            .show()
                }
            }
        }
    }

    private fun enviarPaquete3() {

        try {
            val formBody = FormBody.Builder()
                    .add("username", AppConstants.TOKEN_USERNAME)
                    .add("password", AppConstants.TOKEN_PASSWORD)
                    .add("grant_type", AppConstants.TOKEN_GRANT_TYPE)
                    .build()

            val builderToken = Request.Builder()
            builderToken.url(WebConstant.TOKEN_URL)

            val request = builderToken
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", AppConstants.TOKEN_HEADER_AUTHORIZATION_TOKEN)
                    .post(formBody)
                    .build()
            mHandler.post {
                run {
                    startProgress()
                }
            }

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    e.printStackTrace()
                    Logger.d("Error generando token")
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
                            if (tokenJSONObject != null) {

                                tokenResponse = mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)

                                if (tokenResponse != null) {
                                    Logger.d("Token response on object")
                                    Logger.d(tokenResponse.access_token)

                                    if (tokenResponse.access_token != null) {
                                        SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenResponse.access_token)

                                        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                                        val ip = prefs.getString("ip", "")
                                        val idusuario = prefs.getString("enrollment_idusuario", "")
                                        val idinformacionpersona = prefs.getString("enrollment_idinformacionpersona", "")
                                        val identidad = prefs.getString("enrollment_identidad", "")

                                        val scannHuellas = EnrollmentHuellas()
                                        val paq3 = EnrollmentHuellasRequest()
                                        scannHuellas.idusuario = idusuario
                                        scannHuellas.ip = ip
                                        scannHuellas.identidad = identidad
                                        scannHuellas.idcliente = idinformacionpersona
                                        scannHuellas.accion = "3"
                                        scannHuellas.minutia = minutia
                                        scannHuellas.fingerprint = fingerprint
                                        scannHuellas.pathbitmap = pathbitmap
                                        scannHuellas.pathwsq = pathwsq

                                        paq3.datos = scannHuellas

                                        val bodyMap = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(paq3))

                                        val builderMap = Request.Builder()
                                        builderMap.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                        Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                        val requestReport = builderMap
                                                .header("Content-Type", "application/json; charset=UTF-8")
                                                .header("Accept", "application/json")
                                                .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                .header("operation", OperationConstants.PACKAGE)
                                                .post(bodyMap)
                                                .build()


                                        client.newCall(requestReport).enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {

                                                e.printStackTrace()

                                                mHandler.post {
                                                    run {
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

                                                val responseMapString = response.body()!!.string()
                                                val enrollmentCedulaResponse: EnrollmentDatosCedulaResponse

                                                Logger.d("Respu:$responseMapString")

                                                if (JSONUtils.isJSONValid(responseMapString)) {

                                                    enrollmentCedulaResponse = mapper.readValue<EnrollmentDatosCedulaResponse>(responseMapString, EnrollmentDatosCedulaResponse::class.java)

                                                    if (enrollmentCedulaResponse?.response != null) {
                                                        if (enrollmentCedulaResponse.status!! && enrollmentCedulaResponse != null && enrollmentCedulaResponse.status!!) {
//bien
                                                        } else {
                                                            mHandler.post {
                                                                run {
                                                                    stopProgess()
                                                                    PrettyDialog(this@Enrollment)
                                                                            .setTitle("Información")
                                                                            .setMessage(enrollmentCedulaResponse.message)
                                                                            .show()
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        mHandler.post {
                                                            run {
                                                                stopProgess()
                                                                PrettyDialog(this@Enrollment)
                                                                        .setTitle("Información")
                                                                        .setMessage("Ha ocurrido un error, por favor intente de nuevo--.")
                                                                        .show()
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        })

                                    } else {
                                        SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                        mHandler.post {
                                            run {
                                                stopProgess()
                                            }
                                        }
                                    }

                                } else {
                                    Logger.d("json object is null")
                                    mHandler.post {
                                        run {
                                            stopProgess()
                                        }
                                    }
                                }

                            } else {
                                Logger.d("Is not json object")
                                mHandler.post {
                                    run {
                                        stopProgess()
                                    }
                                }
                            }
                        }
                    }
                }
            })

        } catch (ex: Exception) {
            ex.printStackTrace()
            mHandler.post {
                run {
                    stopProgess()
                    PrettyDialog(this@Enrollment)
                            .setTitle("Información")
                            .setMessage("Error ingresando")
                            .show()
                }
            }
        }
    }

    private fun enviarPaquete4() {

        try {
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
            mHandler.post {
                run {
                    startProgress()
                }
            }

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    e.printStackTrace()
                    Logger.d("Error generando token")
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
                            if (tokenJSONObject != null) {

                                tokenResponse = mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)

                                if (tokenResponse != null) {
                                    Logger.d("Token response on object")
                                    Logger.d(tokenResponse.access_token)

                                    if (tokenResponse.access_token != null) {
                                        SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenResponse.access_token)

                                        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                                        val ip = prefs.getString("ip", "")
                                        val idusuario = prefs.getString("enrollment_idusuario", "")
                                        val idinformacionpersona = prefs.getString("enrollment_idinformacionpersona", "")
                                        val identidad = prefs.getString("enrollment_identidad", "")

                                        val reconocimientoFacial = ReconocimientoFacial()
                                        val paq4 = ReconocimientoFacialRequest()
                                        reconocimientoFacial.idusuario = idusuario
                                        reconocimientoFacial.ip = ip
                                        reconocimientoFacial.identidad = identidad
                                        reconocimientoFacial.idcliente = idinformacionpersona
                                        reconocimientoFacial.accion = "4"
                                        reconocimientoFacial.estatusoperacion = estatusoperacion
                                        reconocimientoFacial.mensajeretorno = mensajeretorno
                                        reconocimientoFacial.imagenpath = imagenpath
                                        reconocimientoFacial.imagentemplate = imagentemplate

                                        paq4.datos = reconocimientoFacial

                                        val bodyMap = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(paq4))

                                        val builderMap = Request.Builder()
                                        builderMap.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                        Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                        val requestReport = builderMap
                                                .header("Content-Type", "application/json; charset=UTF-8")
                                                .header("Accept", "application/json")
                                                .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                .header("operation", OperationConstants.PACKAGE)
                                                .post(bodyMap)
                                                .build()


                                        client.newCall(requestReport).enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {

                                                e.printStackTrace()

                                                mHandler.post {
                                                    run {
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

                                                val responseMapString = response.body()!!.string()
                                                val enrollmentCedulaResponse: EnrollmentDatosCedulaResponse

                                                Logger.d("Respu:$responseMapString")

                                                if (JSONUtils.isJSONValid(responseMapString)) {

                                                    enrollmentCedulaResponse = mapper.readValue<EnrollmentDatosCedulaResponse>(responseMapString, EnrollmentDatosCedulaResponse::class.java)

                                                    if (enrollmentCedulaResponse?.response != null) {
                                                        if (enrollmentCedulaResponse.status!! && enrollmentCedulaResponse != null && enrollmentCedulaResponse.status!!) {
                                                            mHandler.post {
                                                                run {
                                                                    stopProgess()
                                                                    PrettyDialog(this@Enrollment)
                                                                            .setTitle("Información")
                                                                            .setMessage("Escaneo exitoso")
                                                                            .show()
                                                                }
                                                            }

                                                        } else {
                                                            mHandler.post {
                                                                run {
                                                                    stopProgess()
                                                                    PrettyDialog(this@Enrollment)
                                                                            .setTitle("Información")
                                                                            .setMessage(enrollmentCedulaResponse.message)
                                                                            .show()
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        mHandler.post {
                                                            run {
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

                                    } else {
                                        SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                        mHandler.post {
                                            run {
                                                stopProgess()
                                            }
                                        }
                                    }

                                } else {
                                    Logger.d("json object is null")
                                    mHandler.post {
                                        run {
                                            stopProgess()
                                        }
                                    }
                                }

                            } else {
                                Logger.d("Is not json object")
                                mHandler.post {
                                    run {
                                        stopProgess()
                                    }
                                }
                            }
                        }
                    }
                }
            })

        } catch (ex: Exception) {
            ex.printStackTrace()
            mHandler.post {
                run {
                    stopProgess()
                    PrettyDialog(this@Enrollment)
                            .setTitle("Información")
                            .setMessage("Error ingresando")
                            .show()
                }
            }
        }
    }

    private fun startProgress() {
        try {
            val builder = MaterialDialog.Builder(context!!).title(R.string.progress_dialog)
                    .content(R.string.please_wait)
                    .progress(true, 0)

            progressDialog = builder.build()
            progressDialog!!.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun stopProgess() {
        try {
            if (progressDialog != null) {
                progressDialog!!.dismiss()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun validarDatos() {
        if (aux == 5) {
            val BtnSend = findViewById<Button>(R.id.btnSend)
            BtnSend.isEnabled = true
            BtnSend.setBackgroundResource(R.drawable.use_button)
            BtnSend.setTextColor(Color.WHITE)
        }
    }

    private fun enviarDatos(score: String) {

        while (_numerocedula!!.substring(0, 1) == "0") {
            _numerocedula = _numerocedula!!.substring(1)
        }

        if (DocumentoQR == _numerocedula) {
            try {
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
                mHandler.post {
                    run {
                        startProgress()
                    }
                }

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                        e.printStackTrace()

                        mHandler.post {
                            run {
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

                                if (tokenJSONObject != null) {

                                    tokenResponse = mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)


                                    if (tokenResponse != null) {

                                        Logger.d("Token response on object")
                                        Logger.d(tokenResponse.access_token)

                                        if (tokenResponse.access_token != null) {

                                            SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenResponse.access_token)

                                            val builderIp = Request.Builder()
                                            builderIp.url(WebConstant.IPIFY_ENDPOINT)

                                            val requestIp = builderIp
                                                    .header("Accept", "application/json")
                                                    .build()

                                            client.newCall(requestIp).enqueue(object : Callback {
                                                override fun onFailure(call: Call, e: IOException) {

                                                    e.printStackTrace()

                                                    mHandler.post {
                                                        run {
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

                                                            if (ipJSONObject != null) {

                                                                ipResponse = mapper.readValue<IPResponse>(ipJSONObject.toString(), IPResponse::class.java)

                                                                if (ipResponse != null) {

                                                                    Logger.d("Token response on object")
                                                                    Logger.d(ipResponse.ip)

                                                                    if (ipResponse.ip != null) {

                                                                        val enrollment = EnrollmentRequest()
                                                                        val _datos = EnrollmentDatos()

                                                                        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)

                                                                        _datos.idusuario = prefs.getString("enrollment_idusuario", "")
                                                                        _datos.ip = prefs.getString("ip", "")
                                                                        _datos.accion = "1"
                                                                        _datos.tipodocumento = "CC"
                                                                        _datos.numerodocumento = _numerocedula
                                                                        _datos.primerapellido = _primerapellido
                                                                        _datos.segundoapellido = _segundoapellido
                                                                        _datos.primernombre = _primernombre
                                                                        _datos.segundonombre = _segundonombre
                                                                        _datos.fechanacimiento = _fechanacimiento
                                                                        _datos.sexo = _sexo
                                                                        _datos.rh = _rh
                                                                        _datos.codigo = ""
                                                                        _datos.idinformacionpersona = prefs.getString("enrollment_idinformacionpersona", "")
                                                                        _datos.identidad = prefs.getString("enrollment_identidad", "")
                                                                        _datos.respuesta = "true"
                                                                        _datos.nombreimagencc = _pathimagen
                                                                        _datos.nombreimagenfrontal = "frontal.jpg"
                                                                        _datos.nombreimagentracera = "tracera.jpg"
                                                                        _datos.ruta = "/media/imagenes/80794978/"
                                                                        _datos.escaneo = score
                                                                        _datos.templatecc = "920478697654678907657489d7s6g98ya89fh4t9c7qr37gb9t5nyotc984ny7wm3gx5o8m59nfxh"
                                                                        _datos.templatefrontal = ""
                                                                        _datos.templatetracera = ""

                                                                        enrollment.datos = _datos

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

                                                                                mHandler.post {
                                                                                    run {
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

                                                                                Logger.d("Resp:$response")

                                                                                val responseMapString = response.body()!!.string()
                                                                                        .replace("\"response\": {\"dataresponse\":{\"@nil\":\"true\"},\"response\":\"LA ENTIDAD YA SE ENCUENTRA ENROLADA EXITOSAMENTE\",\"idresponse\":78},", "")
                                                                                val enrollmentResponse: EnrollmentResponse

                                                                                Logger.d("Respu:$responseMapString")

                                                                                if (JSONUtils.isJSONValid(responseMapString)) {

                                                                                    enrollmentResponse = mapper.readValue<EnrollmentResponse>(responseMapString, EnrollmentResponse::class.java)

                                                                                    if (enrollmentResponse?.response != null) {
                                                                                        if (enrollmentResponse.response!!.dataresponse != null) {
                                                                                            val enrollDataresponse: EnrollmentDataResponse = mapper.readValue<EnrollmentDataResponse>(enrollmentResponse.response!!.dataresponse, EnrollmentDataResponse::class.java)

                                                                                            if (enrollmentResponse.status!! && enrollDataresponse != null) {
                                                                                                mHandler.post {
                                                                                                    run {
                                                                                                        stopProgess()
                                                                                                        /*PrettyDialog(this@Enrollment)
                                                                                                            .setTitle("Información")
                                                                                                            .setMessage("Se ha hecho el enrolamiento exitosamente")
                                                                                                            .show()*/
                                                                                                        redirect()
                                                                                                    }
                                                                                                }
                                                                                            } else {
                                                                                                mHandler.post {
                                                                                                    run {
                                                                                                        stopProgess()
                                                                                                        /*PrettyDialog(this@Enrollment)
                                                                                                            .setTitle("Información")
                                                                                                            .setMessage(enrollmentResponse.message)
                                                                                                            .show()*/
                                                                                                        redirectno()
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            mHandler.post {
                                                                                                run {
                                                                                                    stopProgess()
                                                                                                    PrettyDialog(this@Enrollment)
                                                                                                            .setTitle("Información")
                                                                                                            .setMessage(enrollmentResponse.message)
                                                                                                            .show()
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        mHandler.post {
                                                                                            run {
                                                                                                stopProgess()
                                                                                                /*PrettyDialog(this@Enrollment)
                                                                                                    .setTitle("Información")
                                                                                                    .setMessage("Ha ocurrido un error, por favor intente de nuevo y asegurese de que los datos sean correctos y la persona no haya sido enrolada antes.")
                                                                                                    .show()*/
                                                                                                redirectno()
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        })
                                                                    } else {
                                                                        SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                                                        mHandler.post {
                                                                            run {
                                                                                stopProgess()
                                                                                PrettyDialog(this@Enrollment)
                                                                                        .setTitle("Información")
                                                                                        .setMessage("Intente de nuevo.")
                                                                                        .show()
                                                                            }
                                                                        }
                                                                    }

                                                                } else {
                                                                    Logger.d("json object is null")
                                                                    mHandler.post {
                                                                        run {
                                                                            stopProgess()
                                                                            PrettyDialog(this@Enrollment)
                                                                                    .setTitle("Información")
                                                                                    .setMessage("Intente de nuevo.")
                                                                                    .show()
                                                                        }
                                                                    }
                                                                }

                                                            } else {
                                                                Logger.d("Is not json object")
                                                                mHandler.post {
                                                                    run {
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
                                        } else {
                                            SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                            mHandler.post {
                                                run {
                                                    stopProgess()
                                                }
                                            }
                                        }

                                    } else {
                                        Logger.d("json object is null")
                                        mHandler.post {
                                            run {
                                                stopProgess()
                                            }
                                        }
                                    }

                                } else {
                                    Logger.d("Is not json object")
                                    mHandler.post {
                                        run {
                                            stopProgess()
                                        }
                                    }
                                }
                            }
                        }
                    }
                })

            } catch (ex: Exception) {
                ex.printStackTrace()
                mHandler.post {
                    run {
                        stopProgess()
                        PrettyDialog(this@Enrollment)
                                .setTitle("Información")
                                .setMessage("Error ingresando")
                                .show()
                        redirectno()
                    }
                }
            }
        }else
        {
            redirectno()
        }
    }


    private fun requestVerificationToBytte(){

        try{
            val autenticacionCapturaHuella = ArrayOfProcesoAutenticacionCapturaHuella()

            val autenticacionCapturaHuellaOne = ProcesoAutenticacionCapturaHuella()
            autenticacionCapturaHuellaOne.IdFingerprint = 2
            autenticacionCapturaHuellaOne.Imagen = imghuellaone
            //imghuellaone

            autenticacionCapturaHuella.add(autenticacionCapturaHuellaOne)

            val autenticacionCapturaHuellaTwo = ProcesoAutenticacionCapturaHuella()
            autenticacionCapturaHuellaTwo.IdFingerprint = 3
            autenticacionCapturaHuellaTwo.Imagen = imghuellatwo
            //imghuellatwo

            autenticacionCapturaHuella.add(autenticacionCapturaHuellaTwo)

            val autenticacionCapturaHuellaThree = ProcesoAutenticacionCapturaHuella()
            autenticacionCapturaHuellaThree.IdFingerprint = 4
            autenticacionCapturaHuellaThree.Imagen = imghuellathree
            //imghuellathree

            autenticacionCapturaHuella.add(autenticacionCapturaHuellaThree)

            val autenticacionCapturaHuellaFour = ProcesoAutenticacionCapturaHuella()
            autenticacionCapturaHuellaFour.IdFingerprint = 5
            autenticacionCapturaHuellaFour.Imagen = imghuellafour
            //imghuellafour

            autenticacionCapturaHuella.add(autenticacionCapturaHuellaFour)


            val validacionEnrolamientoDocumentoRequest = ValidacionEnrolamientoDocumentoRequest()

            validacionEnrolamientoDocumentoRequest.BarCodeBase64 = _barcodebase
            validacionEnrolamientoDocumentoRequest.IdentificadorProceso =  UUID.randomUUID().toString()
            validacionEnrolamientoDocumentoRequest.NumeroDocumento = _numerocedula
            validacionEnrolamientoDocumentoRequest.ImageKey = ""



            if(_imageFront!!.exists()){
                validacionEnrolamientoDocumentoRequest.ImagenFrente = org.apache.commons.io.FileUtils.readFileToByteArray(_imageFront) //TODO byte[] imagen frontal
            }else{
                Logger.d("File no existe frontal")
                /*PrettyDialog(this@Enrollment)
                        .setTitle("Información")
                        .setMessage("No hay imagen frontal de la cedula")
                        .show()

                return*/
            }
            if(_imageBack!!.exists()){
                validacionEnrolamientoDocumentoRequest.ImagenReverso = org.apache.commons.io.FileUtils.readFileToByteArray(_imageBack) //TODO byte[] imagen tracera
            }else{
                Logger.d("File no existe tracera")
                /*PrettyDialog(this@Enrollment)
                        .setTitle("Información")
                        .setMessage("No hay imagen trasera de la cedula")
                        .show()
                return*/
            }




            validacionEnrolamientoDocumentoRequest.SoloMinucia = false
            validacionEnrolamientoDocumentoRequest.Completo = false

            val scoreRequest = ScoreRequest()
            scoreRequest.Score_ConANI_ANIBarCode = 20
            scoreRequest.Score_ConANI_ANIOCR = 10
            scoreRequest.Score_ConANI_Fingerprint = 60
            scoreRequest.Score_ConANI_OCRBarCode = 10
            scoreRequest.Score_SinANI_Fingerprint = 70
            scoreRequest.Score_SinANI_OCRBarCode = 30
            scoreRequest.Score_AutenticacionDactilar = 3000

            validacionEnrolamientoDocumentoRequest.HuellasAProcesar = autenticacionCapturaHuella.size
            validacionEnrolamientoDocumentoRequest.HuellasProceso = autenticacionCapturaHuella

            val service = CASB_x002E_ProcesoAutenticacionService()

            val response = service.ValidacionEnrolamientoDocumento(validacionEnrolamientoDocumentoRequest)


            Logger.d("Score ${response.ScoreDactilarValor}")

            if(response != null){
                if(response.OperacionExitosa){
                    enviarDatos(response.ScoreDactilarValor.toString())
                }else{
                    PrettyDialog(this@Enrollment)
                            .setTitle("Información")
                            .setMessage(response.Mensaje)
                            .show()
                    Logger.d(response.Mensaje)
                }
            }else{
                PrettyDialog(this@Enrollment)
                        .setTitle("Información")
                        .setMessage("No se pudo verificar los datos.")
                        .show()
                Logger.d("No hay respuesta de bytte")
            }


        }catch(ex: Exception){
            ex.printStackTrace()
            PrettyDialog(this@Enrollment)
                    .setTitle("Información")
                    .setMessage(ex.message)
                    .show()
        }




    }


    private fun getBytesFromFile(){

    }

    private fun error(){


        val `as` = File(this@Enrollment.filesDir, "Documents")
        if (`as`.isDirectory) {
            val children = `as`.list()
            for (i in children!!.indices) {
                File(`as`, children!![i]).delete()
            }
        }
        //Util.Eliminarimgaplicacion(this@Enrollment)
//        _numerotarjeta  = null
//        _numerocedula = null
//        _primerapellido = null
//        _segundoapellido = null
//        _primernombre = null
//        _segundonombre = null
//        _nombrecompletos = null
//        _sexo = null
//        _fechanacimiento = null
//        _rh = null
//        _tipodedo = null
//        _versioncedula = null
//        _barcodebase = null
//        _platafomra = null
//        _pathimagen = null
//        _statusoperacion = null
//        estatusoperacion = null
//        mensajeretorno = null
//        imagenpath = null
//        imagentemplate = null
//        minutia = null
//        fingerprint = null
//        pathbitmap = null
//        pathwsq  = null
//        DocumentoQR = null
//        YoPrestoQR  = null
//        QR1  = null
//        QR2 = null
//        QR3 = null
//        idinformacionpersona = null
//        identidad = null
//        idusuario = null
//
//        val cedulafront = findViewById<Button>(R.id.btnCedula)
//        val reconocimientofacial = findViewById<Button>(R.id.button4)
//        val escaner_huella = findViewById<Button>(R.id.button2)
//        val cedulaback = findViewById<Button>(R.id.button7)
//        val BtnSend = findViewById<Button>(R.id.btnSend)
//        val btnScan = findViewById<Button>(R.id.scannQr)
//        btnScan.setBackgroundResource(R.drawable.rounded_button1)
//        btnScan.setEnabled(true)
//        BtnSend.setEnabled(false)
//        cedulaback.setEnabled(false)
//        cedulafront.setEnabled(false)
//        reconocimientofacial.setEnabled(false)
//        escaner_huella.setEnabled(false)
    }
}

