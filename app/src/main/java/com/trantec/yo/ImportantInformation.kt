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


class ImportantInformation : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.important_information)
        val decline = findViewById<Button>(R.id.decline)
        val accept = findViewById<Button>(R.id.accept)


        decline.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        accept.setOnClickListener {
            val intent = Intent(this, Biometric::class.java)
            startActivity(intent)
        }
    }

}

