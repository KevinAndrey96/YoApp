package com.yopresto.app.yoprestoapp.ui.main



import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toolbar
import com.yopresto.app.yoprestoapp.R
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.app_bar_main.*


class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
//NAVDRAW ENDS
    }
