package com.trantec.yo


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException

class ScannerQR : AppCompatActivity() {

    internal var btnScan: Button? = null
    internal var qrScanIntegrator: IntentIntegrator? = null

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
                    Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                    val intent = Intent(this, ScannerQRSuccess::class.java)
                    startActivity(intent)
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
