package com.trantec.yo.alerts

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import com.trantec.yo.R
import com.trantec.yo.ui.main.HomeActivity

class SuccessfulTransaction : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alert_successful_transaction)

        val btnclose2 = findViewById<ImageButton>(R.id.btnexitoclose)

        btnclose2.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}