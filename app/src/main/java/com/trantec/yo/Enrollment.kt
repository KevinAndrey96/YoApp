package com.trantec.yo

import android.annotation.TargetApi
import org.json.JSONObject
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.support.annotation.RequiresApi
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
import com.bytte.docbytte.helper.Util
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


import com.orhanobut.logger.Logger
import com.trantec.yo.alerts.EnrollmentNo
import com.trantec.yo.alerts.EnrollmentOk
import com.trantec.yo.constants.AppConstants
import com.trantec.yo.constants.HttpObjectsConstants
import com.trantec.yo.constants.OperationConstants
import com.trantec.yo.constants.WebConstant
import com.trantec.yo.data.Serverprocess
import com.trantec.yo.data.objetos
import com.trantec.yo.data.objetos1
import com.trantec.yo.enumeration.SessionKeys
import com.trantec.yo.services.bytte.authentication.*
import com.trantec.yo.utils.Utils
import com.yopresto.app.yoprestoapp.dto.*
import hundredthirtythree.sessionmanager.SessionManager
import libs.mjn.prettydialog.PrettyDialog
import okhttp3.*
import okhttp3.Response
import java.io.*
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


class Enrollment : AppCompatActivity() {

    internal var btnScan: Button? = null
    internal var qrScanIntegrator: IntentIntegrator? = null
    val LICENSEMICROBLINK = "Y7NBT5HO-UVKHQUT2-NOCCBXY4-2NZGXGMC-RWSEA4FB-4XNE2ALX-MUT5RH7S-6PO3H7EL"
    val MY_REQUEST_CODE_LISENCE = 1329
    val MY_REQUEST_CODE_FRONT = 1330
    val MY_REQUEST_CODE_BACK = 1331
    val MY_REQUEST_CODE_FRONTPAST = 1332
    val MY_REQUEST_CODE_FACECAPTURE = 1333
    val MY_REQUEST_CODE_BIOMETRIC = 1334
    val MY_REQUEST_CODE_QR = 49374
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
    var _pathimagen: String? = null
    var pathFront: String? = null
    var pathBack: String? = null
    var pathFrontRostro: String? = null
    var templatetracera: String? = null
    var templatefrontal: String? = null
    var templatecc: String? = null

    //Reconocimiento Facial
    var estatusoperacion: String? = null
    var mensajeretorno: String? = null
    var imagenpath: String? = null
    var imagentemplate: String? = null
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

    //Images Cedula
    var imgfront: ByteArray? = null
    var imgrostro: ByteArray? = null
    var imgback: ByteArray? = null


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
    var aux: Int? = 0
    val license = BytteLicenseA()

    //Creacion instancias UI
    var cedulafront: Button? = null
    var BtnSend: Button? = null
    var cedulaback: Button? = null
    var reconocimientofacial: Button? = null
    var escaner_huella: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrollment)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //Inicio de UI y deshabiltar botones para escaneo y enrolamiento
        //val respuesta = ProcesoValidacionDocumentoResponse()
        this.cedulafront = findViewById<Button>(R.id.btnCedula)
        this.BtnSend = findViewById<Button>(R.id.btnSend)
        this.cedulaback = findViewById<Button>(R.id.button7)
        this.reconocimientofacial = findViewById<Button>(R.id.button4)
        this.escaner_huella = findViewById<Button>(R.id.button2)
        val btnScan = findViewById<Button>(R.id.scannQr)
        this.BtnSend!!.isEnabled = false
        this.cedulaback!!.isEnabled = false
        this.cedulafront!!.isEnabled = false
        this.reconocimientofacial!!.isEnabled = false
        this.escaner_huella!!.isEnabled = false
        btnScan!!.setOnClickListener { performAction() }
        qrScanIntegrator = IntentIntegrator(this)
        qrScanIntegrator?.setPrompt("Realice la lectura del código generado en la plataforma administrativa.")

        //Activar Licencia si ya tiene la reutiliza
        try {
            //val lis = license.activarlicensia(URLPETICION, applicationContext, this@Enrollment)
            val lis = license.activarlicensia(URLPETICION, applicationContext, this@Enrollment,"yopresto")
            Logger.d("LIC $lis")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        //Boton para envio de datos a Bytte
        btnSend.setOnClickListener {
            try {
                try {
                    sendBytte().execute()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()

            }
        }
        //captura de frente doc
        btnCedula.setOnClickListener {
            val inten = Intent(this@Enrollment, CFrontDocument::class.java)
            inten.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
            inten.putExtra(BaseScanActivity.EXTRAS_LICENSE_KEY, LICENSEMICROBLINK)
            inten.putExtra("EXTRAS_TIMEOUT", "20")
            startActivityForResult(inten, MY_REQUEST_CODE_FRONT)
        }

        //captura de back doc
        button7.setOnClickListener {
            val inten = Intent(this@Enrollment, CBackDocument::class.java)
            inten.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
            inten.putExtra(BaseScanActivity.EXTRAS_LICENSE_KEY, LICENSEMICROBLINK)
            inten.putExtra("EXTRAS_TIMEOUT", "20")
            startActivityForResult(inten, MY_REQUEST_CODE_BACK)
        }

        //Boton para captura facial
        button4.setOnClickListener {
            //Seleccion de camara para el proceso
            val builder = AlertDialog.Builder(this@Enrollment)
            builder.setTitle("Escaner Facial")
            builder.setIcon(R.drawable.reconocimiento_facial)
            builder.setMessage("¿Que cámara deseas usar para realizar el escaner facial?")
            builder.setPositiveButton("Cámara Frontal") { _, _ ->
                openCameraFront()
            }
            builder.setNegativeButton("Cámara Posterior") { _, _ ->
                openCameraBack()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        //Inicia  Captura Huellas
        button2.setOnClickListener {
            val inten = Intent(this@Enrollment, BytteFingerPrint::class.java)
            inten.putExtra("TipoHuella", "2")
            startActivityForResult(inten, MY_REQUEST_CODE_BIOMETRIC)
        }
    }


    /**
     * Valida y captura informacion de cada uno de los procesos
     */
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val results_biometric: String
        val gson = Gson()
        try {
            //Verificacion del codigo de resultado
            if (resultCode == BaseScanActivity.RESULT_OK) {
                //Pool de comportamientos segun respuesta exitosa
                when (requestCode) {
                    //***************************************************RETORNO CAPTURA QR****************************************************************
                    MY_REQUEST_CODE_QR -> {
                        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
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
//                                    Toast.makeText(this, "Se ha escaneado el codigo exitosamente.", Toast.LENGTH_LONG).show()

                                    cedulaback!!.isEnabled = true
                                    cedulafront!!.isEnabled = true
                                    reconocimientofacial!!.isEnabled = true
                                    escaner_huella!!.isEnabled = true

                                    val btnScan = findViewById<Button>(R.id.scannQr)
                                    btnScan.setBackgroundResource(R.drawable.rounded_button2)
                                    btnScan.isEnabled = false

                                    aux = aux!! + 1
                                    validarDatos("true")

                                } else {
                                    Toast.makeText(this, "90 - El QR no es compatible con nuestra aplicacion", Toast.LENGTH_LONG).show()
                                    error()
                                }
                            } else {
                                //Toast.makeText(this, "91 - El QR es incorrecto", Toast.LENGTH_LONG).show()
                                PrettyDialog(this@Enrollment)
                                        .setTitle("Información")
                                        .setMessage("91 - El QR es incorrecto")
                                        .setIconTint(R.color.pdlg_color_red)

                                        .show()
                                error()
                            }


                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(this, "99 - Hubo un error, por favor inténtelo de nuevo", Toast.LENGTH_LONG).show()
                            error()
                        }

                    }
                    //***************************************************RETORNO CAPTURA CEDULA FRONTAL****************************************************************
                    MY_REQUEST_CODE_FRONT -> {
                        //Obtenemos respuesta del SDK
                        results_biometric = data?.extras?.getString("InfoFrontDoc")!!
                        //Mapeo resultado a Json
                        var properties: Properties = gson.fromJson(results_biometric, Properties::class.java)
                        //Convertir cedula desde la url a  BitMap
                        var src = BitmapFactory.decodeFile(properties.getProperty("PathImagen"))
                        pathFront = properties.getProperty("PathImagen")
                        imgfront = Utils().convertBitmapToBytes(src)
                        //Convertir rostro desde la url a  BitMap
                        var srcRostro = BitmapFactory.decodeFile(properties.getProperty("PathImagenRostro"))
                        pathFrontRostro = properties.getProperty("PathImagenRostro")
                        imgrostro = Utils().convertBitmapToBytes(srcRostro)
                        templatecc = Base64.getEncoder().encodeToString(imgrostro)

                        /*TODO Se deja pendiente para envio al integrador enviarPaquete2()*/

                        //Inhabilitamos el boton y le cambiamos de color
                        this.cedulafront!!.isEnabled = false
                        this.cedulafront!!.setBackgroundResource(R.drawable.rounded_button2)

                        //Incremento de auxiliar para verificar si ya esta lista la informacion para su envio
                        validarDatos(properties.getProperty("StatusOperacion"))
                    }
                    //***************************************************RETORNO CAPTURA CEDULA REVERSO****************************************************************
                    MY_REQUEST_CODE_BACK -> {
                        //Obtenemos respuesta del SDK
                        results_biometric = data?.extras?.getString("InfoBackDoc")!!
                        //Mapeo resultado a Json
                        var properties: Properties = gson.fromJson(results_biometric, Properties::class.java)
                        //Convertir desde la url a  BitMap
                        var src = BitmapFactory.decodeFile(properties.getProperty("PathImagen"))
                        pathBack = properties.getProperty("PathImagen")
                        //Asociar a variable global el arreglo de bytes
                        imgback = Utils().convertBitmapToBytes(src)
                        templatetracera = Base64.getEncoder().encodeToString(imgback)
                        //Verificacion datos cedula QR
                        if (properties.getProperty("NumeroCedula") == DocumentoQR) {
                            /*var datanoseque=objetos1()
                            datanoseque.apellidos=""*/

                            _numerotarjeta = properties.getProperty("NumeroTarjeta")
                            _numerocedula = properties.getProperty("NumeroCedula")
                            _primerapellido = properties.getProperty("PrimerApellido")
                            _segundoapellido = properties.getProperty("SegundoApellido")
                            _primernombre = properties.getProperty("PrimerNombre")
                            _segundonombre = properties.getProperty("SegundoNombre")
                            _nombrecompletos = properties.getProperty("NombresCompletos")
                            _sexo = properties.getProperty("Sexo")
                            _fechanacimiento = properties.getProperty("FechaNacimiento")
                            _rh = properties.getProperty("RH")
                            _tipodedo = properties.getProperty("TipoDedo")
                            _versioncedula = properties.getProperty("VersionCedula")
                            _barcodebase = properties.getProperty("BarcodeBase64")
                            _platafomra = properties.getProperty("plataforma")

                            //TODO se deja pendiente el envio a el integrador enviarPaquete1()

                            //Inhabilitamos el boton y le cambiamos de color
                            this.cedulaback!!.isEnabled = false
                            this.cedulaback!!.setBackgroundResource(R.drawable.rounded_button2)

                            //Incremento de auxiliar para verificar si ya esta lista la informacion para su envio
                            validarDatos(properties.getProperty("StatusOperacion"))

                        } else {
                            //Se Muestra mensaje porque el documento leido en el web no corresponde a el de la cedula fisica
                            mHandler.post {
                                run {
                                    PrettyDialog(this@Enrollment)
                                            .setTitle("Información")
                                            .setMessage("91 - Este documento no es valido para el QR escaneado")
                                            .setIconTint(R.color.pdlg_color_red)
                                            .show()
                                }
                            }
                            error()
                        }
                    }
                    //***************************************************RETORNO CAPTURA FACIAL****************************************************************
                    MY_REQUEST_CODE_FACECAPTURE -> {
                        //Obtenemos respuesta del SDK
                        results_biometric = data?.extras?.getString("InfoimgRostro")!!
                        //Mapeo resultado a Json
                        val properties = gson.fromJson(results_biometric, Properties::class.java)

                        mensajeretorno = properties.getProperty("MensajeOriginal")
                        imagenpath = properties.getProperty("ImagePath")
                        imagentemplate = properties.getProperty("ImageTemplate")

                        //TODO se deja pensiente para envio al integrador enviarPaquete4()

                        //Inhabilitamos el boton y le cambiamos de color
                        this.reconocimientofacial!!.isEnabled = false
                        this.reconocimientofacial!!.setBackgroundResource(R.drawable.rounded_button2)

                        //Incremento de auxiliar para verificar si ya esta lista la informacion para su envio
                        validarDatos(properties.getProperty("StatusOperacion"))


                    }
                    //***************************************************RETORNO CAPTURA HUELLAS****************************************************************
                    MY_REQUEST_CODE_BIOMETRIC -> {
                        //Obtenemos respuesta del SDK
                        results_biometric = data?.extras?.getString("InfoBiometric")!!
                        val jsonObj = JSONObject(results_biometric.substring(results_biometric.indexOf("{"), results_biometric.lastIndexOf("}") + 1))
                        val fingers = jsonObj.getJSONArray("FingerprintsObjects")

                        //Convertir imagen huella 1
                        var properties: Properties = gson.fromJson(fingers.getJSONObject(0).toString(), Properties::class.java)
                        var src = BitmapFactory.decodeFile(properties.getProperty("PathBitmap"))
                        imghuellaone = Utils().convertBitmapToBytes(src)

                        //Convertir imagen huella 2
                        properties = gson.fromJson(fingers.getJSONObject(1).toString(), Properties::class.java)
                        src = BitmapFactory.decodeFile(properties.getProperty("PathBitmap"))
                        imghuellatwo = Utils().convertBitmapToBytes(src)

                        //Convertir imagen huella 3
                        properties = gson.fromJson(fingers.getJSONObject(2).toString(), Properties::class.java)
                        src = BitmapFactory.decodeFile(properties.getProperty("PathBitmap"))
                        imghuellathree = Utils().convertBitmapToBytes(src)

                        //Convertir imagen huella 4
                        properties = gson.fromJson(fingers.getJSONObject(3).toString(), Properties::class.java)
                        src = BitmapFactory.decodeFile(properties.getProperty("PathBitmap"))
                        imghuellafour = Utils().convertBitmapToBytes(src)


                        /* TODO for (i in 0 until fingers!!.length()) {   Se debe usar para envio de las huellas al integrador
                            minutia = fingers.getJSONObject(i).getString("Minutia")
                            fingerprint = fingers.getJSONObject(i).getString("Fingerprint")
                            pathbitmap = fingers.getJSONObject(i).getString("PathBitmap")
                            pathwsq = fingers.getJSONObject(i).getString("PathWSQ")

                            //enviarPaquete3()
                        }*/

                        //Inhabilitamos el boton y le cambiamos de color
                        this.escaner_huella!!.isEnabled = false
                        this.escaner_huella!!.setBackgroundResource(R.drawable.rounded_button2)

                        //Incremento de auxiliar para verificar si ya esta lista la informacion para su envio
                        validarDatos(jsonObj.getString("StatusOperacion"))
                    }

                }


            } else if (resultCode == BaseScanActivity.RESULT_CANCELED) {
                mHandler.post {
                    run {
                        PrettyDialog(this@Enrollment)
                                .setTitle("Información")
                                .setMessage("90 - Proceso no termino, intentelo de nuevo")
                                .setIconTint(R.color.pdlg_color_red)
                                .show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mHandler.post {
                run {
                    PrettyDialog(this@Enrollment)
                            .setTitle("Información")
                            .setMessage("99 - Proceso no termino, intentelo de nuevo")
                            .setIconTint(R.color.pdlg_color_red)
                            .show()
                }
            }
        }
    }

    /**
     * Abre Camara Frontal
     */
    private fun openCameraFront() {
        val intent = Intent(this@Enrollment, BytteCaptureFace::class.java)
        intent.putExtra(ValuesBiometric.EXTRAS_FACECAPTURE, "4")//factor de captura  0  facil 1  medio 2 dificil 3 muy dificil 0,1,2,3 deteccion por movimientos, 4,5,6 deteccion de rostro
        intent.putExtra("TIPO_CAMERA", "0")//0 camara frontal 1 camara posterior
        intent.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
        startActivityForResult(intent, MY_REQUEST_CODE_FACECAPTURE)
    }

    /**
     * Abre Camara Posterior Para Captura Facial
     */
    private fun openCameraBack() {
        val intent = Intent(this@Enrollment, BytteCaptureFace::class.java)
        intent.putExtra(ValuesBiometric.EXTRAS_FACECAPTURE, "4")//factor de captura  0  facil 1  medio 2 dificil 3 muy dificil 0,1,2,3 deteccion por movimientos, 4,5,6 deteccion de rostro
        intent.putExtra("TIPO_CAMERA", "1")//0 camara frontal 1 camara posterior
        intent.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
        startActivityForResult(intent, MY_REQUEST_CODE_FACECAPTURE)
    }

    /**
     * Inicia Camara para Lectura QR
     */
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


//    private fun startProgress() {
//        try {
//            val builder = MaterialDialog.Builder(context!!).title(R.string.progress_dialog)
//                    .content(R.string.please_wait)
//                    .progress(true, 0)
//
//            progressDialog = builder.build()
//            progressDialog!!.show()
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }


    private fun validarDatos(estatus: String) {
        //Aumenta ejecucion de procesos
        aux = aux!! + 1

        //Valida estado de cada proceso para saber q mostrar al usuario
        if (estatus.equals("true")) {
            mHandler.post {
                run {
                    PrettyDialog(this@Enrollment)
                            .setTitle("Información")
                            .setMessage("Proceso terminado con exito")
                            .setIconTint(R.color.pdlg_color_red)
                            .show()
                }
            }
        } else {
            mHandler.post {
                run {
                    PrettyDialog(this@Enrollment)
                            .setTitle("Información")
                            .setMessage("Ejecutar de nuevo el proceso")
                            .setIconTint(R.color.pdlg_color_red)
                            .show()
                }
            }
        }

        //Validacion para saber que el proceso finaliza y habilita envio
        if (aux == 6) {
            this.BtnSend!!.isEnabled = true
            this.BtnSend!!.setBackgroundResource(R.drawable.use_button)
            this.BtnSend!!.setTextColor(Color.WHITE)
        }
    }

    private fun enviarDatos(score: String) {

        val gson = Gson()
        try {
            //Confirmacion de datos y proceso web
            if (DocumentoQR == _numerocedula) {
                //Eliminando los ceros a la derecha para envio yopresto
                while (_numerocedula!!.substring(0, 1) == "0") {
                    _numerocedula = _numerocedula!!.substring(1)
                }

                try {
                    //Alistamiento de informacion para token
                    val formBody = FormBody.Builder()
                            .add("username", AppConstants.TOKEN_USERNAME)
                            .add("password", AppConstants.TOKEN_PASSWORD)
                            .add("grant_type", AppConstants.TOKEN_GRANT_TYPE)
                            .build()

                    val builderToken = Request.Builder()
                    builderToken.url(WebConstant.TOKEN_URL)
                    Logger.d(WebConstant.TOKEN_URL)

                    //Preparacion de cabeceras
                    val request = builderToken
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .header("Authorization", AppConstants.TOKEN_HEADER_AUTHORIZATION_TOKEN)
                            .post(formBody)
                            .build()

                    //llamado token yopresto
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {

                            e.printStackTrace()

                            mHandler.post {
                                run {
                                    PrettyDialog(this@Enrollment)
                                            .setTitle("Información")
                                            //.setMessage("Error. " + e.message)
                                            .setMessage("Verifique su conexión a internet e intentelo de nuevo")
                                            .setIconTint(R.color.pdlg_color_red)
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
//                                Logger.d(responseTokenString)

                                if (JSONUtils.isJSONValid(responseTokenString)) {

                                    tokenJSONObject = JSONObject(responseTokenString)

                                    if (tokenJSONObject != null) {

                                        tokenResponse = mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)


                                        if (tokenResponse != null) {

                                            Logger.d("Token response on object")
//                                            Logger.d(tokenResponse.access_token)

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
                                                                PrettyDialog(this@Enrollment)
                                                                        .setTitle("Información")
                                                                        .setMessage("Intente de nuevo" + e.message)
                                                                        .setIconTint(R.color.pdlg_color_red)
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
//                                                            Logger.d(responseIpString)

                                                            if (JSONUtils.isJSONValid(responseIpString)) {

                                                                ipJSONObject = JSONObject(responseIpString)

                                                                if (ipJSONObject != null) {

                                                                    ipResponse = mapper.readValue<IPResponse>(ipJSONObject.toString(), IPResponse::class.java)

                                                                    if (ipResponse != null) {

                                                                        Logger.d("Token response on object")
//                                                                        Logger.d(ipResponse.ip)

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
                                                                            _datos.nombreimagencc = pathFrontRostro
                                                                            _datos.nombreimagenfrontal = pathFront
                                                                            _datos.nombreimagentracera = pathBack
                                                                            _datos.ruta = "Base Datos"
                                                                            _datos.escaneo = score
                                                                            _datos.templatecc = templatecc
                                                                            _datos.templatefrontal = templatefrontal
                                                                            _datos.templatetracera = templatetracera

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
                                                                                            PrettyDialog(this@Enrollment)
                                                                                                    .setTitle("Información")
                                                                                                    .setMessage("Error. " + e.message)
                                                                                                    .setIconTint(R.color.pdlg_color_red)
                                                                                                    .show()
                                                                                        }
                                                                                    }

                                                                                }

                                                                                @Throws(IOException::class)
                                                                                override fun onResponse(call: Call, response: Response) {

                                                                                    Logger.d("Resp:$response")

                                                                                    val responseMapString = response.body()!!.string()
                                                                                    val autenticResponse: AutenticResponse
                                                                                    val enrollmentResponse: EnrollmentResponse

                                                                                    Logger.d("Respu:$responseMapString")
                                                                                    //Mapeo resultado a Json
//                                                                                    var enrolamientoresponse: EnrollmentResponse? = gson.fromJson(responseMapString, EnrollmentResponse::class.java)

                                                                                    if (JSONUtils.isJSONValid(responseMapString)) {

                                                                                        autenticResponse = mapper.readValue<AutenticResponse>(responseMapString, AutenticResponse::class.java)

                                                                                        if (autenticResponse?.status!!) {
                                                                                            enrollmentResponse = mapper.readValue<EnrollmentResponse>(responseMapString, EnrollmentResponse::class.java)
                                                                                            if (enrollmentResponse.response!!.dataresponse != null) {
                                                                                                val enrollDataresponse: EnrollmentDataResponse = mapper.readValue<EnrollmentDataResponse>(enrollmentResponse.response!!.dataresponse, EnrollmentDataResponse::class.java)

                                                                                                if (enrollmentResponse.status!! && enrollDataresponse != null) {
                                                                                                    mHandler.post {
                                                                                                        run {
                                                                                                            redirect()
                                                                                                        }
                                                                                                    }
                                                                                                } else {
                                                                                                    mHandler.post {
                                                                                                        run {
                                                                                                            redirectno()
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            } else {
                                                                                                mHandler.post {
                                                                                                    run {
                                                                                                        PrettyDialog(this@Enrollment)
                                                                                                                .setTitle("Información")
                                                                                                                .setMessage(enrollmentResponse.message)
                                                                                                                .setIconTint(R.color.pdlg_color_red)
                                                                                                                .show()
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            mHandler.post {
                                                                                                run {
                                                                                                    Toast.makeText(getApplicationContext(),autenticResponse.response!!.response , Toast.LENGTH_LONG).show()
                                                                                                    redirectno()
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        mHandler.post {
                                                                                            run {
                                                                                                Toast.makeText(getApplicationContext(), "91- Error de proceso", Toast.LENGTH_LONG).show()
                                                                                                redirectno()
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            })
                                                                        } else {
                                                                            SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                                                            mHandler.post {
                                                                                run {
                                                                                    PrettyDialog(this@Enrollment)
                                                                                            .setTitle("Información")
                                                                                            .setMessage("Intente de nuevo.")
                                                                                            .setIconTint(R.color.pdlg_color_red)
                                                                                            .show()
                                                                                }
                                                                            }
                                                                        }

                                                                    } else {
                                                                        Logger.d("json object is null")
                                                                        mHandler.post {
                                                                            run {
                                                                                PrettyDialog(this@Enrollment)
                                                                                        .setTitle("Información")
                                                                                        .setMessage("Intente de nuevo.")
                                                                                        .setIconTint(R.color.pdlg_color_red)
                                                                                        .show()
                                                                            }
                                                                        }
                                                                    }

                                                                } else {
                                                                    Logger.d("Is not json object")
                                                                    mHandler.post {
                                                                        run {
                                                                            PrettyDialog(this@Enrollment)
                                                                                    .setTitle("Información")
                                                                                    .setMessage("Intente de nuevo.")
                                                                                    .setIconTint(R.color.pdlg_color_red)
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
                                            }

                                        } else {
                                            Logger.d("json object is null")

                                        }

                                    } else {
                                        Logger.d("Is not json object")

                                    }
                                }
                            }
                        }
                    })

                } catch (ex: Exception) {
                    ex.printStackTrace()
                    mHandler.post {
                        run {
                            Toast.makeText(this, "92 - Error Ingresando.", Toast.LENGTH_LONG).show()
                            redirectno()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "93 - La informacion no corresponde al proceso.", Toast.LENGTH_LONG).show()
                redirectno()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            redirectno()
        }
    }

    private fun error() {
        val `as` = File(this@Enrollment.filesDir, "Documents")
        if (`as`.isDirectory) {
            val children = `as`.list()
            for (i in children!!.indices) {
                File(`as`, children!![i]).delete()
            }
        }
    }

    //Tarea Background para envio de datos a Bytte
    private inner class sendBytte : AsyncTask<Void, Any, ProcesoValidacionDocumentoResponse>() {
        val autenticacionCapturaHuella = ArrayOfProcesoAutenticacionCapturaHuella()
        val validacionEnrolamientoDocumentoRequest = ValidacionEnrolamientoDocumentoRequest()
        val scoreRequest = ScoreRequest()
        val service = CASB_x002E_ProcesoAutenticacionService()
        var excepcion: Boolean = false


        override fun onPreExecute() {
            mHandler.post {
                run {
                    PrettyDialog(this@Enrollment)
                            .setTitle("Información")
                            .setMessage("Enviando Información")
                            .setIconTint(R.color.pdlg_color_red)
                            .show()
                }
            }

            super.onPreExecute()
            //Arreglo de Huellas
            //1
            val autenticacionCapturaHuellaOne = ProcesoAutenticacionCapturaHuella()
            autenticacionCapturaHuellaOne.IdFingerprint = 2
            autenticacionCapturaHuellaOne.Imagen = imghuellaone
            autenticacionCapturaHuella.add(autenticacionCapturaHuellaOne)
            //2
            val autenticacionCapturaHuellaTwo = ProcesoAutenticacionCapturaHuella()
            autenticacionCapturaHuellaTwo.IdFingerprint = 3
            autenticacionCapturaHuellaTwo.Imagen = imghuellatwo
            autenticacionCapturaHuella.add(autenticacionCapturaHuellaTwo)
            //3
            val autenticacionCapturaHuellaThree = ProcesoAutenticacionCapturaHuella()
            autenticacionCapturaHuellaThree.IdFingerprint = 4
            autenticacionCapturaHuellaThree.Imagen = imghuellathree
            autenticacionCapturaHuella.add(autenticacionCapturaHuellaThree)
            //4
            val autenticacionCapturaHuellaFour = ProcesoAutenticacionCapturaHuella()
            autenticacionCapturaHuellaFour.IdFingerprint = 5
            autenticacionCapturaHuellaFour.Imagen = imghuellafour
            autenticacionCapturaHuella.add(autenticacionCapturaHuellaFour)

            //Datos  cedula
            validacionEnrolamientoDocumentoRequest.NumeroDocumento = _numerocedula
            validacionEnrolamientoDocumentoRequest.BarCodeBase64 = _barcodebase
            validacionEnrolamientoDocumentoRequest.ImagenFrente = imgfront
            validacionEnrolamientoDocumentoRequest.ImagenReverso = imgback

            //Configuracion
            validacionEnrolamientoDocumentoRequest.IdentificadorProceso = UUID.randomUUID().toString() //Identificador del Proceso automatico
            validacionEnrolamientoDocumentoRequest.ImageKey = "" //Se envia vacio porque la imagen no va comprimida
            validacionEnrolamientoDocumentoRequest.SoloMinucia = false
            validacionEnrolamientoDocumentoRequest.Completo = false
            scoreRequest.Score_ConANI_ANIBarCode = 20
            scoreRequest.Score_ConANI_ANIOCR = 10
            scoreRequest.Score_ConANI_Fingerprint = 60
            scoreRequest.Score_ConANI_OCRBarCode = 10
            scoreRequest.Score_SinANI_Fingerprint = 70
            scoreRequest.Score_SinANI_OCRBarCode = 30
            scoreRequest.Score_AutenticacionDactilar = 3000
            validacionEnrolamientoDocumentoRequest.ScoreProceso = scoreRequest
            validacionEnrolamientoDocumentoRequest.HuellasAProcesar = autenticacionCapturaHuella.size
            validacionEnrolamientoDocumentoRequest.HuellasProceso = autenticacionCapturaHuella
        }


        override fun doInBackground(vararg params: Void?): ProcesoValidacionDocumentoResponse? {
            var response :ProcesoValidacionDocumentoResponse
            try{
                response = service.ValidacionEnrolamientoDocumento(validacionEnrolamientoDocumentoRequest)
                enviarDatos(response!!.ScoreDactilarValor.toString())
            }catch (e: Exception ){
                excepcion=true
                runOnUiThread(Runnable() {
                    run() {
                        Toast.makeText(getApplicationContext(), "No se logro el envio de datos, por favor verifique su internet.", Toast.LENGTH_LONG).show()
                    }
                })
            }finally {
                response= ProcesoValidacionDocumentoResponse()
            }
            // If you need update UI, simply do this:
            return response

        }

        override fun onPostExecute(result: ProcesoValidacionDocumentoResponse?) {
//            enviarDatos(result!!.ScoreDactilarValor.toString())
            if(excepcion){
                Toast.makeText(getApplicationContext(), "No se logro el envio de datos, por favor verifique su internet.", Toast.LENGTH_LONG).show()
            }else{
                mHandler.post {
                    run {
                        PrettyDialog(this@Enrollment)
                                .setTitle("Información")
                                .setMessage("Enviando Información")
                                .setIconTint(R.color.pdlg_color_red)
                                .hide()
                    }
                }
            }
            super.onPostExecute(result)
        }
    }


}