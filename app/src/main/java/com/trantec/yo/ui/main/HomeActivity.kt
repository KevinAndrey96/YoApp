package com.trantec.yo.ui.main



import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.trantec.yo.*
import com.trantec.yo.ui.LoginActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import com.trantec.yo.dto.LoginDataresponse
import com.trantec.yo.enumeration.SessionKeys
import hundredthirtythree.sessionmanager.SessionManager
import org.codehaus.jackson.map.ObjectMapper


class HomeActivity : AppCompatActivity() {

    val mapper = ObjectMapper()
    var user: LoginDataresponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //user = mapper.readValue(SessionManager.getString(SessionKeys.USER_SESSION.key, null), LoginDataresponse::class.java)
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
    }

}

