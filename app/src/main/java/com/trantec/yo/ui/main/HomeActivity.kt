package com.trantec.yo.ui.main



import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.trantec.yo.*
import com.trantec.yo.ui.LoginActivity
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.trantec.yo.R
import kotlinx.android.synthetic.main.app_bar_main.*
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.trantec.yo.enumeration.SessionKeys
import com.trantec.yo.ui.fragment.TakeDNIPictureFragment
import hundredthirtythree.sessionmanager.SessionManager
import libs.mjn.prettydialog.PrettyDialog


class HomeActivity : AppCompatActivity() {

    private var context: HomeActivity? = null
    var cuenta = ""
    var nombre = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val name = prefs.getString("nombre", "")

        if (name != "" || name == null){
            val lastname = prefs.getString("apellido", "")
            nombre = name +" "+ lastname
            cuenta = prefs.getString("cuenta", "")
            val txtname = findViewById<TextView>(R.id.txtName)
            val txtaccount = findViewById<TextView>(R.id.txtAccount)
            txtname.text = nombre
            txtaccount.text = cuenta
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val utilization = findViewById<Button>(R.id.BtnUtilizacion)
        val enrolamiento = findViewById<Button>(R.id.BtnEnrollment)
        val tiendas = findViewById<Button>(R.id.BtnMyStores)
        val reportes = findViewById<Button>(R.id.BtnReports)

        utilization.setOnClickListener {
            val intent = Intent(this, TakeDNICaptureActivity::class.java)
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

        DrawerBuilder().withActivity(this).build()

        val headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorAccent)
                .addProfiles(
                        ProfileDrawerItem().withName(nombre).withEmail(cuenta).withIcon((R.drawable.logoyopresto))
                )
                .withOnAccountHeaderListener(object : AccountHeader.OnAccountHeaderListener {
                    override fun onProfileChanged(view: View, profile: IProfile<*>, currentProfile: Boolean): Boolean {
                        return false
                    }
                })
                .build()

        var item1 = PrimaryDrawerItem().withIdentifier(1).withName("Inicio")
        var item2 = PrimaryDrawerItem().withIdentifier(2).withName("Enrolamiento")
        var item3 = PrimaryDrawerItem().withIdentifier(3).withName("Utilizacion")
        var item4 = PrimaryDrawerItem().withIdentifier(4).withName("Mis tiendas")
        var item5 = PrimaryDrawerItem().withIdentifier(5).withName("Reportes")
        var item6 = PrimaryDrawerItem().withIdentifier(6).withName("Cerrar sesion")

        DrawerBuilder()
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
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    when (drawerItem.identifier) {
                        item1.identifier -> {
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        item2.identifier -> {
                            val intent = Intent(this, QR::class.java)
                            startActivity(intent)
                        }
                        item3.identifier -> {
                            val intent = Intent(this, TakeDNIPictureFragment::class.java)
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
                            prefs.edit().remove("nombre").commit()
                            prefs.edit().remove("apellido").commit()
                            prefs.edit().remove("cuenta").commit()
                            prefs.edit().remove("saldo").commit()
                            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                            finish()
                        }

                    }
                    false
                }
                .build()

    }

}

