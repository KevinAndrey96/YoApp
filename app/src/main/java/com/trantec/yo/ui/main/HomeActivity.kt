package com.trantec.yo.ui.main



import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.trantec.yo.R
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.app_bar_main.*


class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val utilization = findViewById<Button>(R.id.BtnUtilizacion)

        utilization.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1
                )


                .build()
    }
//NAVDRAW ENDS
    }

