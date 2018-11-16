package com.trantec.yo.alerts


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import com.trantec.yo.R
import com.trantec.yo.ui.main.HomeActivity

class NoCredit : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alert_no_credit)

        val btnclose1 = findViewById<ImageButton>(R.id.btnclosecredit)

        btnclose1.setOnClickListener {
            //val intent = Intent(this, HomeActivity::class.java)
            //startActivity(intent)
            finish()
        }
    }
}