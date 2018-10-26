package com.trantec.yo.ui.main

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.orhanobut.logger.Logger
import com.trantec.yo.R
import com.trantec.yo.constants.AppConstants

class TakeDNICaptureActivity : AppCompatActivity() {

    private val initScanRequestCode = 0x0000ffff
    private var integrator = IntentIntegrator(this@TakeDNICaptureActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_dnicapture)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        integrator.setDesiredBarcodeFormats(IntentIntegrator.PDF_417)
        integrator.setPrompt("Inicie la captura del documento")
        integrator.setOrientationLocked(false)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intentResult = Intent()
        val bundle = Bundle()
        bundle.putString("MESSAGE", "cancelled")
        intentResult.putExtras(bundle)
        setResult(Activity.RESULT_CANCELED, intentResult)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != initScanRequestCode && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        when (requestCode) {
            initScanRequestCode -> {
                Toast.makeText(this@TakeDNICaptureActivity, "REQUEST_CODE = $requestCode", Toast.LENGTH_LONG).show()
            }
            else -> {
            }
        }

        val result = IntentIntegrator.parseActivityResult(resultCode, data)

        if (result.contents == null) {
            Logger.d("MainActivity Cancelled scan")
            //Toast.makeText(this@TakeDNICaptureActivity, "Cancelled", Toast.LENGTH_LONG).show()
            val intentResult = Intent()
            val bundle = Bundle()
            bundle.putString(AppConstants.DNI_CAPTURE_OBJECT_MESSAGE, "Captura cancelada")
            intentResult.putExtras(bundle)
            setResult(Activity.RESULT_CANCELED, intentResult)
            finish()

        } else {
            Logger.d("MainActivity", "Scanned")
            Toast.makeText(this@TakeDNICaptureActivity, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            val codigo = result.contents

            val cedula = decodePdf417(codigo)

            Logger.d(cedula)

            val intentResult = Intent()
            val bundle = Bundle()
            bundle.putLong(AppConstants.DNI_OBJECT_NAME, cedula!!)
            intentResult.putExtras(bundle)

            setResult(Activity.RESULT_OK, intentResult)
            finish()
        }
    }


    private fun decodePdf417(codigo: String): Long? {
        var ced: Long = 0
        try {
            if (codigo.contains("PubDSK_")) {

                val split = codigo.split("PubDSK_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (codigo.contains("0F")) {  // mujer
                    val split2 = split[1].split("0F".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    var doc = split2[0]
                    doc = doc.replace("[^0-9?!\\.]".toRegex(), "")
                    var docNum = ""
                    docNum = doc.substring(doc.length - 10)
                    ced = java.lang.Long.parseLong(docNum)
                } else { // hombre
                    val split2 = split[1].split("0M".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    var doc = split2[0]
                    doc = doc.replace("[^0-9?!\\.]".toRegex(), "")
                    var docNum = ""
                    docNum = doc.substring(doc.length - 10)
                    ced = java.lang.Long.parseLong(docNum)
                }


            } else {
                // hombre
                val split = codigo.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var doc = split[15]
                var docNum = ""
                doc = doc.replace("[^0-9?!\\.]".toRegex(), "")
                docNum = doc.substring(8, doc.length)
                ced = java.lang.Long.parseLong(docNum)
                println(ced)
            }
            return ced

        } catch (e: Exception) {
            // This will catch any exception, because they are all descended from Exception
            println("Error " + e.message)
        }

        return ced

    }


}
