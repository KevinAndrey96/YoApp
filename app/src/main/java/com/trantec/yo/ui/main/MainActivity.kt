package com.trantec.yo.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.orhanobut.logger.Logger
import com.trantec.yo.R
import com.trantec.yo.enumeration.SessionKeys
import hundredthirtythree.sessionmanager.SessionManager
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.trantec.yo.Stores
import com.trantec.yo.dto.LoginDataresponse
import com.trantec.yo.ui.LoginActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import org.codehaus.jackson.map.ObjectMapper
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import android.widget.Toast
import com.trantec.yo.R.id.toolbar






class MainActivity : AppCompatActivity() {

    var user: LoginDataresponse? = null
    val mapper = ObjectMapper()
    var result: Drawer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val name = prefs.getString("nombre", "")

        if (name != "" || name == null){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val tiendas = findViewById<Button>(R.id.BtnMystores)

        tiendas.setOnClickListener {
            val intent = Intent(this, Stores::class.java)
            startActivity(intent)
        }

        val toggle = findViewById<Button>(R.id.btnToggleMain)

        toggle.setOnClickListener {
            result!!.openDrawer()
        }

        DrawerBuilder().withActivity(this).build()

        val headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorAccent)
                .addProfiles(
                        ProfileDrawerItem().withName("Yo Presto").withEmail("").withIcon((R.drawable.logoyopresto))
                )

                .build()

        val iniciar_session = PrimaryDrawerItem().withIdentifier(1).withName("Iniciar sesion")
        val mis_tiendas = PrimaryDrawerItem().withIdentifier(2).withName("Mis tiendas")

        result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                //.withAccountHeader(headerResult)
                .addDrawerItems(
                        iniciar_session,
                        mis_tiendas
                )
                .withOnDrawerItemClickListener { _, _, drawerItem ->
                    when (drawerItem.identifier) {
                        iniciar_session.identifier -> {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        mis_tiendas.identifier -> {
                            val intent = Intent(this, Stores::class.java)
                            startActivity(intent)
                        }
                    }
                    false
                }

                .build()

    }




}
