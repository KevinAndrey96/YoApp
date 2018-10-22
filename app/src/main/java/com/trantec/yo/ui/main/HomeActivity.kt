package com.trantec.yo.ui.main



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.trantec.yo.*
import com.trantec.yo.ui.LoginActivity
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.trantec.yo.R
import kotlinx.android.synthetic.main.app_bar_main.*


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

        DrawerBuilder().withActivity(this).build()

        val headerResult = AccountHeaderBuilder()
                .withActivity(this)
                //.withHeaderBackground(R.drawable.header)
                .addProfiles(
                        ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(resources.getDrawable(R.drawable.logoyopresto))
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
                        item5

                )


                .build()
    }
}

