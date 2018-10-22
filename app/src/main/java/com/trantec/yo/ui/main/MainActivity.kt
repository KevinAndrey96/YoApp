package com.trantec.yo.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.orhanobut.logger.Logger
import com.trantec.yo.R
import com.trantec.yo.enumeration.SessionKeys
import com.trantec.yo.ui.LoginActivity
import com.trantec.yo.ui.fragment.TakeDNIPictureFragment
import hundredthirtythree.sessionmanager.SessionManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import libs.mjn.prettydialog.PrettyDialog
//import net.hockeyapp.android.CrashManager
//import net.hockeyapp.android.UpdateManager
import android.widget.TextView
import com.trantec.yo.Enrollment
import com.trantec.yo.Stores
import com.trantec.yo.dto.LoginDataresponse
import org.codehaus.jackson.map.ObjectMapper
import android.R.id.edit
import com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences
import android.content.SharedPreferences




class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var context: MainActivity? = null
    var user: LoginDataresponse? = null
    val mapper = ObjectMapper()


    override fun onCreate(savedInstanceState: Bundle?) {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)//Primera Vista

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        Logger.d(SessionManager.getString(SessionKeys.USER_SESSION.key, null))

        context = this

        val fragment = TakeDNIPictureFragment()
        //val fragment = HomeActivity()
        val tx = supportFragmentManager.beginTransaction()
        tx.replace(R.id.main_fragment, fragment)
        tx.addToBackStack("WelcomeFrg").commit()

        user = mapper.readValue(SessionManager.getString(SessionKeys.USER_SESSION.key, null), LoginDataresponse::class.java)


    }
    override fun onResume() {
        super.onResume()
        //checkForCrashes()
        //checkForUpdates()


            if(user != null){

                val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                navigationView.setNavigationItemSelectedListener(this)
                val txtProfileName = navigationView.getHeaderView(0).findViewById(R.id.textViewName) as TextView
                val txtProfileDetail = navigationView.getHeaderView(0).findViewById(R.id.textViewDetail) as TextView
                txtProfileName.text = user!!.primernombre + " " +  user!!.primerapellido
                txtProfileDetail.text = user!!.cuenta
            }

    }
    /*
    private fun checkForCrashes() {
        CrashManager.register(this)
    }
    private fun checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this)
    }
    private fun unregisterManagers() {
        UpdateManager.unregister()
    }
    */
    public override fun onDestroy() {
        super.onDestroy()
        //unregisterManagers()
    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_use_transaction -> {
                val fragment = TakeDNIPictureFragment()
                val tx = supportFragmentManager.beginTransaction()
                tx.replace(R.id.main_fragment, fragment)
                tx.addToBackStack("TakeDNIPicFrg").commit()
            }
            R.id.nav_stores -> {
                val intent = Intent(this, Stores::class.java)
                startActivity(intent)
            }
            R.id.nav_enrollment -> {
                val intent = Intent(this, Enrollment::class.java)
                startActivity(intent)
            }

            R.id.nav_home -> {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_close_session -> {


                try{
                    val dialog = PrettyDialog(context)
                            .setTitle(getString(R.string.information))
                            .setMessage(getString(R.string.close_session_ask))
                            .setAnimationEnabled(true)



                    dialog.addButton(
                                    getString(android.R.string.ok), // button text
                                    R.color.pdlg_color_white, // button text color
                                    R.color.pdlg_color_green // button background color
                            ) // button OnClick listener
                            {
                                SessionManager.removeKey(SessionKeys.USER_SESSION.key)
                                SessionManager.putBoolean(SessionKeys.IS_LOGGED_IN.key, false)
                                val settings = context!!.getSharedPreferences("login_data", Context.MODE_PRIVATE)
                                settings.edit().remove("nombre").commit()
                                settings.edit().remove("apellido").commit()
                                settings.edit().remove("cuenta").commit()
                                settings.edit().remove("saldo").commit()
                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))

                                finish()
                            }

                    dialog.addButton(
                            getString(android.R.string.cancel),
                            R.color.pdlg_color_white,
                            R.color.pdlg_color_red
                    )
                    {
                        dialog.dismiss()
                    }

                    dialog.show()
                }catch (ex: Exception){
                    ex.printStackTrace()
                }


            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    fun changeFragment(fragment: Fragment, bundle: Bundle){

        val tx = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        tx.replace(R.id.main_fragment, fragment)
        tx.addToBackStack("TakeDNIPicFrg").commit()
    }
    companion object {
        fun getContext(mainActivity: MainActivity): MainActivity? {
            return mainActivity.context
        }
    }


}