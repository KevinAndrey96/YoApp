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
import com.example.docbytte.ui.CBackDocument
import com.google.gson.Gson
import com.microblink.activity.BaseScanActivity
import kotlinx.android.synthetic.main.capture_document_reverse.*


import java.util.*


class CaptureDocumentReverse : AppCompatActivity() {

    val LICENSEMICROBLINK = "V76CKZT3-NONJVDPE-NT4KLLY5-5OOBC5AI-JZKZL23B-FLBJP77K-EH4NX33O-LF3REX3F"//esta licencia se debe solicitar a bytte para el funcionamiento en los documentos
    val MY_REQUEST_CODE_LISENCE = 1329
    val MY_REQUEST_CODE_BACK = 1331
    val REQUEST_PERMISSION = 1433
    val URLPETICION = "https://portal.bytte.com.co/casb/SmartBio/SB/API/SmartBio/"//esta url se debe solicitar a bytte para licenciar el projecto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.capture_document_reverse)


        captureReverse.setOnClickListener {
            //captura de back doc
            var intent = Intent(this@CaptureDocumentReverse, CBackDocument::class.java)
            intent.putExtra("EXTRAS_LICENSEE", "")//si la imagen estara protegida si esta en vacio no esta protejida
            intent.putExtra(BaseScanActivity.EXTRAS_LICENSE_KEY, LICENSEMICROBLINK)
            startActivityForResult(intent, MY_REQUEST_CODE_BACK)
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
        //val bitmap: Bitmap
        //val gson = Gson()
        try {
            if (requestCode == MY_REQUEST_CODE_LISENCE && resultCode == Activity.RESULT_OK) {
                results_biometric = data!!.extras!!.getString("License")
                Log.d("TAG", results_biometric)
            } else if (requestCode == MY_REQUEST_CODE_LISENCE && resultCode == Activity.RESULT_CANCELED) {
                results_biometric = data!!.extras!!.getString("License")
                Log.d("TAG", results_biometric)
            }else if (requestCode == MY_REQUEST_CODE_BACK && resultCode == BaseScanActivity.RESULT_OK) {//back del documento colombiano
                results_biometric = data?.extras?.getString("InfoBackDoc")!!
                val intent = Intent(this, CaptureDocumentFront::class.java)
                startActivity(intent)
            }
            Log.d("TAG", results_biometric)

        } catch (e: Exception) {

        }


    }
}
