package com.trantec.yo

import android.Manifest
import android.app.Activity
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_enrollment.*
import org.json.JSONException


import java.util.*


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrollment)

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

        /*button6.setOnClickListener {
            //captura de Pasaporte
            var intent = Intent(this@Enrollment, FrontDocPassport::class.java)
            intent.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
            intent.putExtra(BaseScanActivity.EXTRAS_LICENSE_KEY, LICENSEMICROBLINK)
            startActivityForResult(intent, MY_REQUEST_CODE_FRONTPAST)
        }*/

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

        /*button9.setOnClickListener {
            Util.Eliminarimgaplicacion(this@Enrollment)
        }*/

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

    private fun performAction() {
        qrScanIntegrator?.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var results_biometric = ""
        val bitmap: Bitmap
        val gson = Gson()
        try {
            if (requestCode == MY_REQUEST_CODE_LISENCE && resultCode == Activity.RESULT_OK) {
                results_biometric = data!!.extras!!.getString("License")
                Log.d("TAG", results_biometric)
            } else if (requestCode == MY_REQUEST_CODE_LISENCE && resultCode == Activity.RESULT_CANCELED) {
                results_biometric = data!!.extras!!.getString("License")
                Log.d("TAG", results_biometric)
            } else if (requestCode == MY_REQUEST_CODE_FRONTPAST && resultCode == BaseScanActivity.RESULT_OK) {
                results_biometric = data?.extras?.getString("InfoFrontDoc")!!
            } else if (requestCode == MY_REQUEST_CODE_FRONT && resultCode == BaseScanActivity.RESULT_OK) {
                results_biometric = data?.extras?.getString("InfoFrontDoc")!!
                val cedulafront = findViewById<Button>(R.id.btnCedula)
                cedulafront.setEnabled(false)
            } else if (requestCode == MY_REQUEST_CODE_FRONT && resultCode == BaseScanActivity.RESULT_CANCELED) {
                results_biometric = data?.extras?.getString("InfoFrontDoc")!!
            } else if (requestCode == MY_REQUEST_CODE_NFCPAST && resultCode == BaseScanActivity.RESULT_OK) {
                results_biometric = data?.extras?.getString("NUMPASSPORT")!!
                bitmap = data?.getParcelableExtra("Infofacechip")
                bitmap.height
            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_OK) {
                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                val properties = gson.fromJson(results_biometric, Properties::class.java)
                val reconocimientofacial = findViewById<Button>(R.id.button4)
                reconocimientofacial.setEnabled(false)
            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_CANCELED) {
                results_biometric = data?.extras?.getString("InfoimgRostro")!!

            } else if (requestCode == MY_REQUEST_CODE_BIOMETRIC && resultCode == BaseScanActivity.RESULT_OK) {//
                results_biometric = data?.extras?.getString("InfoBiometric")!!
                val escaner_huella = findViewById<Button>(R.id.button2)
                escaner_huella.setEnabled(false)
            }else if (requestCode == MY_REQUEST_CODE_BACK && resultCode == BaseScanActivity.RESULT_OK) {//back del documento colombiano
                results_biometric = data?.extras?.getString("InfoBackDoc")!!
                val cedulaback = findViewById<Button>(R.id.button7)
                cedulaback.setEnabled(false)
            }

            Log.d("TAG", results_biometric)

        } catch (e: Exception) {

        }

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
}