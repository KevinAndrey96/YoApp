package com.trantec.yo

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.docbytte.ui.CBackDocument
import com.microblink.activity.BaseScanActivity
import kotlinx.android.synthetic.main.scanner_qr_success.*


import java.util.*


class ScannerQRSuccess : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_qr_success)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        qrSuccess.setOnClickListener {
            var intent = Intent(this, CaptureDocumentReverse::class.java)
            startActivity(intent)
        }
    }
}
