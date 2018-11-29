package com.trantec.yo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.trantec.yo.ui.main.TakeDNICaptureActivity
import kotlinx.android.synthetic.main.activity_utilizacion.*


class Utilization : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utilizacion)

        imageButtonTakeDNICapture1.setOnClickListener {
            val intent = Intent(this, TakeDNICaptureActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
