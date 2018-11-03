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
import com.example.biometricbytte.morpho.face.BytteCaptureFace
import com.example.biometricbytte.morpho.huella.BytteFingerPrint
import com.example.biometricbytte.morpho.license.BytteLicense
import com.example.docbytte.BiometricCamera.ValuesBiometric
import com.example.docbytte.helper.Util
import com.example.docbytte.ui.CBackDocument
import com.example.docbytte.ui.CFrontDocument
import com.example.docbytte.ui.FrontDocPassport
import com.google.gson.Gson
import com.microblink.activity.BaseScanActivity
import com.trantec.yo.ui.main.HomeActivity
import kotlinx.android.synthetic.main.activity_enrollment.*
import kotlinx.android.synthetic.main.scanner_facial.*


import java.util.*


class ScannerFacial : AppCompatActivity() {

    val LICENSEMICROBLINK = "V76CKZT3-NONJVDPE-NT4KLLY5-5OOBC5AI-JZKZL23B-FLBJP77K-EH4NX33O-LF3REX3F"//esta licencia se debe solicitar a bytte para el funcionamiento en los documentos
    val MY_REQUEST_CODE_LISENCE = 1329
    val MY_REQUEST_CODE_FACECAPTURE = 1333
    val REQUEST_PERMISSION = 1433
    val URLPETICION = "https://portal.bytte.com.co/casb/SmartBio/SB/API/SmartBio/"//esta url se debe solicitar a bytte para licenciar el projecto

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
            if (requestCode == MY_REQUEST_CODE_LISENCE && resultCode == Activity.RESULT_OK) {
                results_biometric = data!!.extras!!.getString("License")
                Log.d("TAG", results_biometric)

            } else if (requestCode == MY_REQUEST_CODE_LISENCE && resultCode == Activity.RESULT_CANCELED) {
                results_biometric = data!!.extras!!.getString("License")
                Log.d("TAG", results_biometric)
            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_OK) {
                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                val properties = gson.fromJson(results_biometric, Properties::class.java)
            } else if (requestCode == MY_REQUEST_CODE_FACECAPTURE && resultCode == BaseScanActivity.RESULT_CANCELED) {
                results_biometric = data?.extras?.getString("InfoimgRostro")!!
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }

            Log.d("TAG", results_biometric)

        } catch (e: Exception) {

        }


    }
}
