package com.trantec.yo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.biometricbytte.morpho.face.BytteCaptureFace
import com.example.biometricbytte.morpho.huella.BytteFingerPrint
import com.example.biometricbytte.morpho.license.BytteLicense
import com.example.docbytte.BiometricCamera.ValuesBiometric
import com.example.docbytte.helper.Util
import com.example.docbytte.ui.CBackDocument
import com.example.docbytte.ui.CFrontDocument
import com.example.docbytte.ui.FrontDocPassport
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


class Enrollment : AppCompatActivity() {

    internal var btnScan: Button? = null
    internal var qrScanIntegrator: IntentIntegrator? = null
    val LICENSEMICROBLINK = "V76CKZT3-NONJVDPE-NT4KLLY5-5OOBC5AI-JZKZL23B-FLBJP77K-EH4NX33O-LF3REX3F"//esta licencia se debe solicitar a bytte para el funcionamiento en los documentos
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

    private val mapper = ObjectMapper()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrollment)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        val intent = Intent(this@Enrollment, BytteLicense::class.java)
        intent.putExtra("URLPETICION", URLPETICION)
        startActivityForResult(intent, MY_REQUEST_CODE_LISENCE)

        val btnScan = findViewById<Button>(R.id.scannQr)
        btnScan!!.setOnClickListener { performAction() }

        qrScanIntegrator = IntentIntegrator(this)

        val BtnSend = findViewById<Button>(R.id.btnSend)
        BtnSend.setEnabled(false)

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

                    Toast.makeText(this, "Escaneo exitoso", Toast.LENGTH_SHORT)
                }

            } else if (requestCode == MY_REQUEST_CODE_FRONT && resultCode == BaseScanActivity.RESULT_CANCELED) {
                results_biometric = data?.extras?.getString("InfoFrontDoc")!!
                Toast.makeText(this, "Intentelo de nuevo", Toast.LENGTH_LONG)

            //RECONOCIMIENTO FACIAL
            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_OK) {
                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                val properties = gson.fromJson(results_biometric, Properties::class.java)

                Log.d("ReconocimientoFacial", results_biometric)

                val reconocimientofacial = findViewById<Button>(R.id.button4)
                reconocimientofacial.setEnabled(false)

                reconocimientofacial.setBackgroundResource(R.drawable.rounded_button2)
                Toast.makeText(this, "Escaneo exitoso", Toast.LENGTH_SHORT)

            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_CANCELED) {
                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                Toast.makeText(this, "Intentelo de nuevo", Toast.LENGTH_LONG)

            //HUELLAS
            } else if (requestCode == MY_REQUEST_CODE_BIOMETRIC && resultCode == BaseScanActivity.RESULT_OK) {//
                results_biometric = data?.extras?.getString("InfoBiometric")!!

                val escaner_huella = findViewById<Button>(R.id.button2)
                escaner_huella.setEnabled(false)

                escaner_huella.setBackgroundResource(R.drawable.rounded_button2)
                Toast.makeText(this, "Escaneo exitoso", Toast.LENGTH_SHORT)

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
                    Toast.makeText(this, "Escaneo exitoso", Toast.LENGTH_SHORT)
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
                    Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                    val scannQr = findViewById<Button>(R.id.scannQr)
                    scannQr.setEnabled(false)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Hubo un error, por favor intentelo de nuevo", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun enviarDatos(){
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
    }
}