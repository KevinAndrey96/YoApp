package com.trantec.yo.ui.main



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.trantec.yo.*
import com.trantec.yo.ui.LoginActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val utilization = findViewById<Button>(R.id.BtnUtilizacion)
        val enrolamiento = findViewById<Button>(R.id.BtnEnrollment)
        val tiendas = findViewById<Button>(R.id.BtnMyStores)
        val reportes = findViewById<Button>(R.id.BtnReports)

        utilization.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        tiendas.setOnClickListener {
            val intent = Intent(this, Stores::class.java)
            startActivity(intent)
        }

        enrolamiento.setOnClickListener {
            val intent = Intent(this, CaptureDocumentReverse::class.java)
            //val intent = Intent(this, ImportantInformation::class.java)
            startActivity(intent)
        }

        reportes.setOnClickListener {
            val intent = Intent(this, Reports::class.java)
            startActivity(intent)
        }

        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val name = prefs.getString("nombre", "")

        if (name != null){
            val lastname = prefs.getString("apellido", "")
            val cuenta = prefs.getString("cuenta", "")
            val txtname = findViewById<TextView>(R.id.txtName)
            val txtaccount = findViewById<TextView>(R.id.txtAccount)
            txtname.text = name +" "+ lastname
            txtaccount.text = cuenta
        }

    }




}

