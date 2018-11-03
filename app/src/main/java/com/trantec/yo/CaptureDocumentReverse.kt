package com.trantec.yo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.example.docbytte.ui.CBackDocument
import com.google.gson.Gson
import com.microblink.activity.BaseScanActivity
import com.trantec.yo.dto.EscanerBack
import com.trantec.yo.utils.JSONUtils
import kotlinx.android.synthetic.main.capture_document_reverse.*
import org.codehaus.jackson.map.ObjectMapper


import java.util.*


class CaptureDocumentReverse : AppCompatActivity() {

    val LICENSEMICROBLINK = "V76CKZT3-NONJVDPE-NT4KLLY5-5OOBC5AI-JZKZL23B-FLBJP77K-EH4NX33O-LF3REX3F"//esta licencia se debe solicitar a bytte para el funcionamiento en los documentos
    val MY_REQUEST_CODE_LISENCE = 1329
    val MY_REQUEST_CODE_BACK = 1331
    val REQUEST_PERMISSION = 1433
    val URLPETICION = "https://portal.bytte.com.co/casb/SmartBio/SB/API/SmartBio/"//esta url se debe solicitar a bytte para licenciar el projecto
    private val mapper = ObjectMapper()

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

                if (JSONUtils.isJSONValid(results_biometric)) {
                    val escaner_back: EscanerBack
                    val res_ = results_biometric.toLowerCase()

                    escaner_back =  mapper.readValue<EscanerBack>(res_, EscanerBack::class.java)
                    val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putString("enrollment_numerodocumento", escaner_back.numerocedula)
                    editor.putString("enrollment_primerapellido", escaner_back.primerapellido)
                    editor.putString("enrollment_segundoapellido", escaner_back.segundoapellido)
                    editor.putString("enrollment_primernombre", escaner_back.primernombre)
                    editor.putString("enrollment_segundonombre", escaner_back.segundonombre)
                    editor.putString("enrollment_fechanacimiento", escaner_back.fechanacimiento)
                    editor.putString("enrollment_sexo", escaner_back.sexo)
                    editor.putString("enrollment_rh", escaner_back.rh)

                    editor.commit()


                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Atención")
                    builder.setMessage("Escaneo exitoso")
                    builder.setPositiveButton("OK", null)
                    builder.create()
                    builder.show()

                    val intent = Intent(this, CaptureDocumentFront::class.java)
                    startActivity(intent)
                }
            }
            Log.d("TAG", results_biometric)

        } catch (e: Exception) {

        }


    }
}
