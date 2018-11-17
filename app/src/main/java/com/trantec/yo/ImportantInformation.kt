package com.trantec.yo.ui.main



import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.biometricbytte.morpho.license.BytteLicense
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
    val LICENSEMICROBLINK = "V76CKZT3-NONJVDPE-NT4KLLY5-5OOBC5AI-JZKZL23B-FLBJP77K-EH4NX33O-LF3REX3F"//esta licencia se debe solicitar a bytte para el funcionamiento en los documentos
    val MY_REQUEST_CODE_LISENCE = 1329
    val MY_REQUEST_CODE_FRONT = 1330
    val MY_REQUEST_CODE_BACK = 1331
    val MY_REQUEST_CODE_FRONTPAST = 1332
    val MY_REQUEST_CODE_FACECAPTURE = 1333
    val MY_REQUEST_CODE_BIOMETRIC = 1334
    val MY_REQUEST_CODE_NFCPAST = 1335
    val REQUEST_PERMISSION = 1433
    val URLPETICION = "https://portal.bytte.com.co/casb/SmartBio/SB/API/SmartBio/"//esta url se debe solicitar a bytte para licenciar el projecto
    //val URLPETICION = "https://portal.bytte.com.co/casb/YoPrestoPR/CASB.ProcesoAutenticacion/Service/ServicioAutenticacion.svc"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.important_information)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        val decline = findViewById<Button>(R.id.decline)
        val accept = findViewById<Button>(R.id.accept)


        decline.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        accept.setOnClickListener {
            val intent1 = Intent(this@ImportantInformation, BytteLicense::class.java)
            intent1.putExtra("URLPETICION", URLPETICION)
            startActivityForResult(intent1, MY_REQUEST_CODE_LISENCE)

        }
    }

}

