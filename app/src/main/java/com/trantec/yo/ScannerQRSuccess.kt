package com.trantec.yo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
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
import com.trantec.yo.ui.main.ImportantInformation
import kotlinx.android.synthetic.main.activity_enrollment.*
import kotlinx.android.synthetic.main.capture_document_front.*


import java.util.*


class ScannerQRSuccess : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_qr_success)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)




    }

}
