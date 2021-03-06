package com.trantec.yo.ui.main



import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.trantec.yo.*
import com.trantec.yo.ui.LoginActivity
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.trantec.yo.R
import kotlinx.android.synthetic.main.app_bar_main.*


class HomeActivity : AppCompatActivity() {

    private var context: HomeActivity? = null
    var cuenta = ""
    var nombre = ""

    val REQUEST_PERMISSION = 1433

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val name = prefs.getString("nombre", "")

        if (name != ""){
            val lastname = prefs.getString("apellido", "")
            nombre = "$name $lastname"
            cuenta = prefs.getString("cuenta", "")
            val txtname = findViewById<TextView>(R.id.txtName)
            val txtaccount = findViewById<TextView>(R.id.txtAccount)
            txtname.text = nombre
            txtaccount.text = cuenta
            txtaccount.text = prefs.getString("cuenta", "")
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val utilization = findViewById<Button>(R.id.BtnUtilizacion)
        val enrolamiento = findViewById<Button>(R.id.BtnEnrollment)
        val tiendas = findViewById<Button>(R.id.BtnMyStores)
        val reportes = findViewById<Button>(R.id.BtnReports)
        val toggle = findViewById<Button>(R.id.BtnToggle)

        val item1 = PrimaryDrawerItem().withIdentifier(1).withName("Inicio")
        val item2 = PrimaryDrawerItem().withIdentifier(2).withName("Enrolamiento")
        val item3 = PrimaryDrawerItem().withIdentifier(3).withName("Utilización")
        val item4 = PrimaryDrawerItem().withIdentifier(4).withName("Mis tiendas")
        val item5 = PrimaryDrawerItem().withIdentifier(5).withName("Reportes")
        val item6 = PrimaryDrawerItem().withIdentifier(6).withName("Cerrar sesion")

        DrawerBuilder().withActivity(this).build()

        val headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorAccent)
                .addProfiles(
                        ProfileDrawerItem().withName(nombre).withEmail(cuenta).withIcon((R.drawable.logoyopresto))
                )
                .build()

        val result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        item6
                )
                .withOnDrawerItemClickListener { _, _, drawerItem ->
                    when (drawerItem.identifier) {
                        item1.identifier -> {
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        item2.identifier -> {
                            //val intent = Intent(this, Enrollment::class.java)
                            val intent = Intent(this, Enrollment::class.java)
                            startActivity(intent)
                        }
                        item3.identifier -> {
                            val intent = Intent(this, Utilization::class.java)
                            startActivity(intent)
                        }
                        item4.identifier -> {
                            val intent = Intent(this, Stores::class.java)
                            startActivity(intent)
                        }
                        item5.identifier -> {
                            val intent = Intent(this, Reports::class.java)
                            startActivity(intent)
                        }
                        item6.identifier -> {
                            prefs.edit().remove("nombre").apply()
                            prefs.edit().remove("apellido").apply()
                            prefs.edit().remove("cuenta").apply()
                            prefs.edit().remove("saldo").apply()
                            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                            finish()
                        }

                    }
                    false
                }
                .build()



        utilization.setOnClickListener {
            val intent = Intent(this, Utilization::class.java)
            startActivity(intent)
        }

        tiendas.setOnClickListener {
            val intent = Intent(this, Stores::class.java)
            startActivity(intent)

        }
        toggle.setOnClickListener {
            result.openDrawer()
        }

        enrolamiento.setOnClickListener {
            //val intent = Intent(this, Enrollment::class.java)
            //intent.putExtra("Cedula","1032485")
            //val intent = Intent(this, ImportantInformation::class.java)
            val intent = Intent(this, Enrollment::class.java)
            startActivity(intent)
        }

        reportes.setOnClickListener {
            val intent = Intent(this, Reports::class.java)
            startActivity(intent)
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

}

