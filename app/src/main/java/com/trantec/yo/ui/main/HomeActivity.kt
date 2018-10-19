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
        user = mapper.readValue(SessionManager.getString(SessionKeys.USER_SESSION.key, null), LoginDataresponse::class.java)
        val utilization = findViewById<Button>(R.id.BtnUtilizacion)
        val enrolamiento = findViewById<Button>(R.id.BtnEnrollment)
        val tiendas = findViewById<Button>(R.id.BtnMyStores)
        val reportes = findViewById<Button>(R.id.BtnReport)


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
            startActivity(intent)
        }

        reportes.setOnClickListener {
            val intent = Intent(this, Reports::class.java)
            startActivity(intent)
        }

        if(user != null){
            val txtName = findViewById<TextView>(R.id.textName)
            val txtAmount = findViewById<TextView>(R.id.textAmount)
            txtName.text = user!!.primernombre + " " +  user!!.primerapellido
            txtAmount.text = user!!.saldo.toString()
        }

        //NAVDRAW START
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

        var item1 = PrimaryDrawerItem().withIdentifier(1).withName("Enrolamiento")

        DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1
                )


                .build()
    }

}

