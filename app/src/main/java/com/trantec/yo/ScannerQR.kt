package com.trantec.yo


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException

class ScannerQR : AppCompatActivity() {

    internal var btnScan: Button? = null
    internal var qrScanIntegrator: IntentIntegrator? = null

    var DocumentoQR: String? = null
    var YoPrestoQR: String? = null
    var QR1: String? = null
    var QR2: String? = null
    var QR3: String? = null
    var idinformacionpersona: String? = null
    var identidad: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_qr)

        btnScan = findViewById(R.id.btnScanner)
        btnScan!!.setOnClickListener { performAction() }

        qrScanIntegrator = IntentIntegrator(this)
    }

    private fun performAction() {
        qrScanIntegrator?.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {

            if (result.contents == null) {
                Toast.makeText(this, getString(R.string.result_not_found), Toast.LENGTH_LONG).show()
            } else {
                try {
                    val string = result.contents
                    val parts = string.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    DocumentoQR = parts[0]
                    QR1 = parts[1]
                    QR2 = parts[2]
                    YoPrestoQR = parts[3]
                    idinformacionpersona = parts[1].substring(6,11)
                    identidad = QR2

                    if(YoPrestoQR == "yopresto"){
                        Toast.makeText(this, idinformacionpersona, Toast.LENGTH_LONG).show()
                        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.putString("enrollment_idinformacionpersona", idinformacionpersona)
                        editor.putString("enrollment_identidad", identidad)
                        editor.putString("enrollment_doc_val", DocumentoQR)
                        editor.commit()

                        val intent = Intent(this, ScannerQRSuccess::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Hubo un error, por favor inténtelo de nuevo", Toast.LENGTH_LONG).show()
                    }
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
