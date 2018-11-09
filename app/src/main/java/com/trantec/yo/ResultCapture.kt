package com.trantec.yo

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.orhanobut.logger.Logger
import com.trantec.yo.alerts.NoCredit
import com.trantec.yo.alerts.SuccessfulTransaction
import com.trantec.yo.constants.AppConstants
import com.trantec.yo.constants.HttpObjectsConstants
import com.trantec.yo.constants.OperationConstants
import com.trantec.yo.constants.WebConstant
import com.trantec.yo.dto.*
import com.trantec.yo.enumeration.SessionKeys
import com.trantec.yo.ui.fragment.TakeDNIPictureFragment
import com.trantec.yo.ui.main.HomeActivity
import com.trantec.yo.utils.JSONUtils
import dmax.dialog.SpotsDialog
import hundredthirtythree.sessionmanager.SessionManager
import kotlinx.android.synthetic.main.activity_result_capture.*
import libs.mjn.prettydialog.PrettyDialog
import okhttp3.*
import org.codehaus.jackson.map.ObjectMapper
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.minDate
import android.R.attr.maxDate
import android.content.Context
import android.widget.Toast
import java.lang.Long.MAX_VALUE


class ResultCapture : AppCompatActivity() {

    private var yoprestoAliado: YoPrestoApp? = null
    private var client = OkHttpClient()
    var mHandler =  Handler(Looper.getMainLooper())
    private val mapper = ObjectMapper()
    var documento: String? = null
    var sessionString: String? = null
    var loginDataresponse: LoginDataresponse? = null
    var celular: String? = null
    var otp: String? = null
    var searchResponse: SearchResponse? = null
    var searchDataresponse: SearchDataresponse? = null
    var myCalendar = Calendar.getInstance()
    var dialog: SpotsDialog? = null
    var identidad: String? = null
    var idusuario: String? = null
    var idperiodo: Int? = null

    private var progressDialog: MaterialDialog? ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_capture)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val textViewDNINumber = findViewById<TextView>(R.id.textViewDNINumber)
        sessionString = SessionManager.getString(SessionKeys.USER_SESSION.key, null)
        val prefs = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        idusuario = prefs.getString("idusuario", "")
        identidad = prefs.getString("identidad", "")
        documento=intent.getStringExtra(AppConstants.DNI_OBJECT_NAME).toString()
        var idperiodos=intent.getStringExtra(AppConstants.IDP_OBJECT_NAME).toString()
        getAvailableAmountForClient()
        //textViewDNINumber.text = "Hey -> "+idperiodos
        //textViewDNINumber.text = documento

        val date: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        val pickerDialog = DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))
        pickerDialog.datePicker.minDate = myCalendar.timeInMillis
        val now = System.currentTimeMillis() - 1000


        if(idperiodos == "1")
        {
            pickerDialog.datePicker.maxDate = now + 1000 * 60 * 60 * 24 * 30//30 días
            //pickerDialog.datePicker.maxDate()
            textViewDNINumber.text = "uno "+idperiodos
            //pickerDialog.datePicker.maxDate = now + 1000 * 60 * 60 * 24 * 30//30 días
        }
        if(idperiodos == "2")
        {
            textViewDNINumber.text = "dos "+idperiodos
            pickerDialog.datePicker.maxDate = now + 1000 * 60 * 60 * 24 * 15//15 días
        }
        if(idperiodos == "3")
        {
            textViewDNINumber.text = "tres "+idperiodos
            pickerDialog.datePicker.maxDate = now + 1000 * 60 * 60 * 24 * 7//7 días
        }



        textViewGenerateOTP.movementMethod = LinkMovementMethod.getInstance()
        textViewGenerateOTP.setOnClickListener {
            generateOTP()
        }
        buttonAcceptUse.setOnClickListener {

            if(editTextAmount.text.toString().isEmpty()){
                editTextAmount.error = getString(R.string.error_field_required)
            }else{
                val amount = editTextAmount.text.toString().toLong()
                val balance = textViewAvailableBalance.text.toString().replace(".0","").replace(".00","").toLong()

                if(amount  >  balance){
                    editTextAmount.error = getString(R.string.amount_to_use_must_be_minor_that_balance)
                }else{

                    if(amount < 100000){
                        editTextAmount.error = getString(R.string.amount_to_use_must_be_almost)
                    }else{
                        if(editTextQuota.text.toString().isEmpty()){
                            editTextQuota.error = getString(R.string.error_field_required)
                        }else{
                            val quota = editTextQuota.text.toString().toLong()

                            if(quota > amount){
                                editTextQuota.error = getString(R.string.quota_must_be_minor_that_amount)
                            }else{

                                if(quota < 20000){
                                    editTextQuota.error = getString(R.string.quota_must_be_almost)
                                }else{
                                    if(editTextOTP.text.toString().isEmpty()){
                                        editTextOTP.error = getString(R.string.error_field_required)
                                    }else{
                                        if(otp != null && otp!! != editTextOTP.text.toString()){
                                            editTextOTP.error = getString(R.string.wrong_otp)
                                        }else{
                                            makeUseAmount()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }



        }
        editTextQuotaDate.setOnFocusChangeListener { v, hasFocus ->

            if(hasFocus) {

                pickerDialog.show()
                //DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }



    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }*/
    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editTextQuotaDate.setText(sdf.format(myCalendar.time))
    }
    private fun getAvailableAmountForClient(){

        try{
            val formBody = FormBody.Builder()
                    .add("username", AppConstants.TOKEN_USERNAME)
                    .add("password", AppConstants.TOKEN_PASSWORD)
                    .add("grant_type", AppConstants.TOKEN_GRANT_TYPE)
                    .build()

            val builderToken = Request.Builder()
            builderToken.url(WebConstant.TOKEN_URL)
            Logger.d(WebConstant.TOKEN_URL)

            val request = builderToken
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", AppConstants.TOKEN_HEADER_AUTHORIZATION_TOKEN)
                    .post(formBody)
                    .build()

            mHandler.post{
                runOnUiThread{
                    startProgress()
                }

            }



            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    e.printStackTrace()
                    mHandler.post{
                        runOnUiThread{
                            stopProgess()
                            PrettyDialog(applicationContext)
                                    .setTitle("Información")
                                    .setMessage("Error consultando el cupo " + e.message)
                                    .show()
                        }
                    }

                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {


                    val responseTokenString = response.body()!!.string()
                    val tokenJSONObject: JSONObject
                    val tokenResponse: TokenResponse

                    if (responseTokenString != null) {

                        Logger.d("Response from get token")
                        Logger.d(responseTokenString)

                        if (JSONUtils.isJSONValid(responseTokenString)) {

                            tokenJSONObject = JSONObject(responseTokenString)

                            if(tokenJSONObject != null){

                                tokenResponse =  mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)

                                if(tokenResponse != null){

                                    Logger.d("Token response on object")
                                    Logger.d(tokenResponse.access_token)

                                    if(tokenResponse.access_token != null){

                                        SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenResponse.access_token)


                                        val builderIp = Request.Builder()
                                        builderIp.url(WebConstant.IPIFY_ENDPOINT)

                                        val requestIp = builderIp
                                                .header("Accept", "application/json")
                                                .build()


                                        client.newCall(requestIp).enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {

                                                e.printStackTrace()
                                                mHandler.post{
                                                    runOnUiThread{
                                                        stopProgess()
                                                        PrettyDialog(applicationContext)
                                                                .setTitle("Información")
                                                                .setMessage("Error consultando el cupo " + e.message)
                                                                .show()
                                                    }
                                                }

                                            }

                                            @Throws(IOException::class)
                                            override fun onResponse(call: Call, response: Response) {


                                                val responseIpString = response.body()!!.string()
                                                val ipJSONObject: JSONObject
                                                val ipResponse: IPResponse

                                                if (responseIpString != null) {

                                                    Logger.d("Response from get ip")
                                                    Logger.d(responseIpString)

                                                    if (JSONUtils.isJSONValid(responseIpString)) {

                                                        ipJSONObject = JSONObject(responseIpString)

                                                        if(ipJSONObject != null){

                                                            ipResponse =  mapper.readValue<IPResponse>(ipJSONObject.toString(), IPResponse::class.java)

                                                            if(ipResponse != null){

                                                                Logger.d("Token response on object")
                                                                Logger.d(ipResponse.ip)

                                                                if(ipResponse.ip != null){

                                                                    val searchRequest = SearchAvailableBalanceRequest()
                                                                    val searchDatos = SearchAvailableDatos()

                                                                    searchDatos.accion = "1"
                                                                    searchDatos.ip = ipResponse.ip
                                                                    searchDatos.idusuario = idusuario
                                                                    searchDatos.documento = documento


                                                                    searchRequest.datos = searchDatos

                                                                    Logger.d(mapper.writeValueAsString(searchRequest))

                                                                    val bodySearch = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(searchRequest))

                                                                    val builderSearch = Request.Builder()
                                                                    builderSearch.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                                                    Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                                                    val requestSearch = builderSearch
                                                                            .header("Content-Type", "application/json; charset=UTF-8")
                                                                            .header("Accept", "application/json")
                                                                            .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                                            .header("operation", OperationConstants.SEARCH_BALANCE_OPERATION)
                                                                            .post(bodySearch)
                                                                            .build()


                                                                    client.newCall(requestSearch).enqueue(object : Callback {
                                                                        override fun onFailure(call: Call, e: IOException) {

                                                                            e.printStackTrace()

                                                                            mHandler.post {
                                                                                run {
                                                                                    stopProgess()
                                                                                    PrettyDialog(applicationContext)
                                                                                            .setTitle("Información")
                                                                                            .setMessage("Error consultando la cedula " + e.message)
                                                                                            .show()
                                                                                }
                                                                            }

                                                                        }

                                                                        @Throws(IOException::class)
                                                                        override fun onResponse(call: Call, response: Response) {


                                                                            val searchString = response.body()!!.string()
                                                                                    .replace("@nil","status").replace(",\"response\": ,",",")
                                                                                    .replace("\"response\": {\"dataresponse\":{\"status\":\"true\"},\"response\":\"EL DOCUMENTO ES REQUERIDO\",\"idresponse\":55},", "")
                                                                            val searchJSONObject: JSONObject


                                                                            Logger.d(searchString)


                                                                            if(searchString != null){

                                                                                if (JSONUtils.isJSONValid(searchString)) {

                                                                                    searchJSONObject = JSONObject(searchString)

                                                                                    if(searchJSONObject != null){

                                                                                        searchResponse = mapper.readValue<SearchResponse>(searchString, SearchResponse::class.java)



                                                                                        if(searchResponse != null && searchResponse!!.response  != null && searchResponse!!.response!!.dataresponse != null){

                                                                                            searchDataresponse = mapper.readValue<SearchDataresponse>(searchResponse!!.response!!.dataresponse, SearchDataresponse::class.java)

                                                                                            if(searchDataresponse != null){

                                                                                                mHandler.post{
                                                                                                    run {

                                                                                                        stopProgess()
                                                                                                        if(textViewAvailableBalance != null) {

                                                                                                            if(searchResponse!!.status!!) {

                                                                                                                if(searchDataresponse!!.saldo!! > 0) {
                                                                                                                    textViewAvailableBalance.text = searchDataresponse!!.saldo.toString()
                                                                                                                    editTextQuotaDate.isEnabled = searchDataresponse!!.verfecha == 1
                                                                                                                    celular = searchDataresponse!!.celular
                                                                                                                    idperiodo = searchDataresponse!!.idperiodo

                                                                                                                    //pickerDialog.datePicker.maxDate = maxDate.toLong()

                                                                                                                    /*if(idperiodo==1) {
                                                                                                                        pickerDialog!!?.datePicker.maxDate = now!! + 1000 * 60 * 60 * 24 * 7//7 días
                                                                                                                    }*/

                                                                                                                    generateOTP()

                                                                                                                }else{
                                                                                                                    try{


                                                                                                                        val intent = Intent(applicationContext, NoCredit::class.java)
                                                                                                                        startActivity(intent)
                                                                                                                        //finish()

                                                                                                                        val dialog = PrettyDialog(applicationContext)
                                                                                                                                .setTitle(getString(R.string.information))
                                                                                                                                .setMessage("El usuario no tiene cupo disponible para retiro.")
                                                                                                                                .setAnimationEnabled(true)





                                                                                                                        dialog.addButton(
                                                                                                                                getString(android.R.string.ok), // button text
                                                                                                                                R.color.pdlg_color_white, // button text color
                                                                                                                                R.color.pdlg_color_green // button background color
                                                                                                                        ) // button OnClick listener
                                                                                                                        {
                                                                                                                            val fragment = TakeDNIPictureFragment()
                                                                                                                            val tx = fragmentManager!!.beginTransaction()
                                                                                                                            //aquitx.replace(R.id.main_fragment, fragment)
                                                                                                                            tx.addToBackStack("TakeDNIPicFrg").commit()
                                                                                                                            dialog.dismiss()
                                                                                                                        }



                                                                                                                        dialog.show()
                                                                                                                    }catch (ex: Exception){
                                                                                                                        ex.printStackTrace()
                                                                                                                    }
                                                                                                                }
                                                                                                            }else{
                                                                                                                try{
                                                                                                                    startActivity(Intent(this@ResultCapture, NoCredit::class.java))
                                                                                                                    val dialog = PrettyDialog(applicationContext)
                                                                                                                            .setTitle(getString(R.string.information))
                                                                                                                            .setMessage("El usuario no tiene cupo disponible para retiro.")
                                                                                                                            .setAnimationEnabled(true)




                                                                                                                    dialog.addButton(
                                                                                                                            getString(android.R.string.ok), // button text
                                                                                                                            R.color.pdlg_color_white, // button text color
                                                                                                                            R.color.pdlg_color_green // button background color
                                                                                                                    ) // button OnClick listener
                                                                                                                    {
                                                                                                                        val fragment = TakeDNIPictureFragment()
                                                                                                                        val tx = fragmentManager!!.beginTransaction()
                                                                                                                        //aquitx.replace(R.id.main_fragment, fragment)
                                                                                                                        tx.addToBackStack("TakeDNIPicFrg").commit()
                                                                                                                        dialog.dismiss()
                                                                                                                    }


                                                                                                                    dialog.show()
                                                                                                                }catch (ex: Exception){
                                                                                                                    ex.printStackTrace()
                                                                                                                }
                                                                                                            }
                                                                                                        }

                                                                                                    }
                                                                                                }

                                                                                            }else{
                                                                                                mHandler.post{
                                                                                                    run {
                                                                                                        stopProgess()
                                                                                                        try{
                                                                                                            val dialog = PrettyDialog(applicationContext)
                                                                                                                    .setTitle(getString(R.string.information))
                                                                                                                    .setMessage("No hay información para el documento.")
                                                                                                                    .setAnimationEnabled(true)



                                                                                                            dialog.addButton(
                                                                                                                    getString(android.R.string.ok), // button text
                                                                                                                    R.color.pdlg_color_white, // button text color
                                                                                                                    R.color.pdlg_color_green // button background color
                                                                                                            ) // button OnClick listener
                                                                                                            {
                                                                                                                val fragment = TakeDNIPictureFragment()
                                                                                                                val tx = fragmentManager!!.beginTransaction()
                                                                                                                //aquitx.replace(R.id.main_fragment, fragment)
                                                                                                                tx.addToBackStack("TakeDNIPicFrg").commit()
                                                                                                                dialog.dismiss()
                                                                                                            }



                                                                                                            dialog.show()
                                                                                                        }catch (ex: Exception){
                                                                                                            ex.printStackTrace()
                                                                                                        }
                                                                                                    }
                                                                                                }

                                                                                            }

                                                                                        }else{
                                                                                            mHandler.post{
                                                                                                run {
                                                                                                    stopProgess()
                                                                                                    try{
                                                                                                        val dialog = PrettyDialog(applicationContext)
                                                                                                                .setTitle(getString(R.string.information))
                                                                                                                .setMessage("No hay información para el documento.")
                                                                                                                .setAnimationEnabled(true)



                                                                                                        dialog.addButton(
                                                                                                                getString(android.R.string.ok), // button text
                                                                                                                R.color.pdlg_color_white, // button text color
                                                                                                                R.color.pdlg_color_green // button background color
                                                                                                        ) // button OnClick listener
                                                                                                        {
                                                                                                            val fragment = TakeDNIPictureFragment()
                                                                                                            val tx = fragmentManager!!.beginTransaction()
                                                                                                            //aquitx.replace(R.id.main_fragment, fragment)
                                                                                                            tx.addToBackStack("TakeDNIPicFrg").commit()
                                                                                                            dialog.dismiss()
                                                                                                        }


                                                                                                        dialog.show()
                                                                                                    }catch (ex: Exception){
                                                                                                        ex.printStackTrace()
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                    }else{
                                                                                        mHandler.post{
                                                                                            run {
                                                                                                stopProgess()
                                                                                                try{
                                                                                                    val dialog = PrettyDialog(applicationContext)
                                                                                                            .setTitle(getString(R.string.information))
                                                                                                            .setMessage("No hay información para el documento.")
                                                                                                            .setAnimationEnabled(true)



                                                                                                    dialog.addButton(
                                                                                                            getString(android.R.string.ok), // button text
                                                                                                            R.color.pdlg_color_white, // button text color
                                                                                                            R.color.pdlg_color_green // button background color
                                                                                                    ) // button OnClick listener
                                                                                                    {
                                                                                                        val fragment = TakeDNIPictureFragment()
                                                                                                        val tx = fragmentManager!!.beginTransaction()
                                                                                                        //aquitx.replace(R.id.main_fragment, fragment)
                                                                                                        tx.addToBackStack("TakeDNIPicFrg").commit()
                                                                                                        dialog.dismiss()
                                                                                                    }


                                                                                                    dialog.show()
                                                                                                }catch (ex: Exception){
                                                                                                    ex.printStackTrace()
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                }else{
                                                                                    mHandler.post{
                                                                                        run {
                                                                                            stopProgess()
                                                                                            try{
                                                                                                val dialog = PrettyDialog(applicationContext)
                                                                                                        .setTitle(getString(R.string.information))
                                                                                                        .setMessage("El usuario no tiene cupo disponible para retiro.")
                                                                                                        .setAnimationEnabled(true)



                                                                                                dialog.addButton(
                                                                                                        getString(android.R.string.ok), // button text
                                                                                                        R.color.pdlg_color_white, // button text color
                                                                                                        R.color.pdlg_color_green // button background color
                                                                                                ) // button OnClick listener
                                                                                                {
                                                                                                    val fragment = TakeDNIPictureFragment()
                                                                                                    val tx = fragmentManager!!.beginTransaction()
                                                                                                    //aquitx.replace(R.id.main_fragment, fragment)
                                                                                                    tx.addToBackStack("TakeDNIPicFrg").commit()
                                                                                                    dialog.dismiss()
                                                                                                }


                                                                                                dialog.show()
                                                                                            }catch (ex: Exception){
                                                                                                ex.printStackTrace()
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }

                                                                            }else{

                                                                            }

                                                                        }
                                                                    })


                                                                }else{
                                                                    SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                                                }

                                                            }else{
                                                                Logger.d("json object is null")
                                                            }

                                                        }else{
                                                            Logger.d("Is not json object")
                                                        }

                                                    }

                                                }

                                            }
                                        })

                                    }else{
                                        SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                    }

                                }else{
                                    Logger.d("json object is null")
                                }

                            }else{
                                Logger.d("Is not json object")
                            }

                        }

                    }

                }
            })


        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }
    fun generateOTP(){
        try{
            val formBody = FormBody.Builder()
                    .add("username", AppConstants.TOKEN_USERNAME)
                    .add("password", AppConstants.TOKEN_PASSWORD)
                    .add("grant_type", AppConstants.TOKEN_GRANT_TYPE)
                    .build()

            val builderToken = Request.Builder()
            builderToken.url(WebConstant.TOKEN_URL)
            Logger.d(WebConstant.TOKEN_URL)

            val request = builderToken
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", AppConstants.TOKEN_HEADER_AUTHORIZATION_TOKEN)
                    .post(formBody)
                    .build()




            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    e.printStackTrace()
                    mHandler.post {
                        run {
                            PrettyDialog(applicationContext)
                                    .setTitle("Información")
                                    .setMessage("Error generando la OTP " + e.message)
                                    .show()
                        }
                    }

                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {


                    val responseTokenString = response.body()!!.string()
                    val tokenJSONObject: JSONObject
                    val tokenResponse: TokenResponse

                    if (responseTokenString != null) {

                        Logger.d("Response from get token")
                        Logger.d(responseTokenString)

                        if (JSONUtils.isJSONValid(responseTokenString)) {

                            tokenJSONObject = JSONObject(responseTokenString)

                            if(tokenJSONObject != null){

                                tokenResponse =  mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)

                                if(tokenResponse != null){

                                    Logger.d("Token response on object")
                                    Logger.d(tokenResponse.access_token)

                                    if(tokenResponse.access_token != null){

                                        SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenResponse.access_token)


                                        val builderIp = Request.Builder()
                                        builderIp.url(WebConstant.IPIFY_ENDPOINT)

                                        val requestIp = builderIp
                                                .header("Accept", "application/json")
                                                .build()


                                        client.newCall(requestIp).enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {

                                                e.printStackTrace()

                                                mHandler.post {
                                                    run {
                                                        PrettyDialog(applicationContext)
                                                                .setTitle("Información")
                                                                .setMessage("Error generando la OTP " + e.message)
                                                                .show()
                                                    }
                                                }


                                            }

                                            @Throws(IOException::class)
                                            override fun onResponse(call: Call, response: Response) {


                                                val responseIpString = response.body()!!.string()
                                                val ipJSONObject: JSONObject
                                                val ipResponse: IPResponse

                                                if (responseIpString != null) {

                                                    Logger.d("Response from get ip")
                                                    Logger.d(responseIpString)

                                                    if (JSONUtils.isJSONValid(responseIpString)) {

                                                        ipJSONObject = JSONObject(responseIpString)

                                                        if(ipJSONObject != null){

                                                            ipResponse =  mapper.readValue<IPResponse>(ipJSONObject.toString(), IPResponse::class.java)

                                                            if(ipResponse != null){

                                                                Logger.d("Token response on object")
                                                                Logger.d(ipResponse.ip)

                                                                if(ipResponse.ip != null){

                                                                    //val generateOTPRequest = GenerateOTPRequest()
                                                                    val generateOTPDatos = GenerateOTPDatos()

                                                                    generateOTPDatos.size = 6
                                                                    generateOTPDatos.alphabets = false
                                                                    generateOTPDatos.special = false
                                                                    generateOTPDatos.upper = false
                                                                    generateOTPDatos.digits = true
                                                                    generateOTPDatos.clave = "asdfsaf"
                                                                    generateOTPDatos.ip = ipResponse.ip
                                                                    generateOTPDatos.movil = celular
                                                                    //generateOTPDatos.movil = "3185563342"
                                                                    generateOTPDatos.mensaje = "Estimado Cliente: Para realizar el proceso de utilización de tu préstamo, por favor confirma al aliado la siguiente OTP:"


                                                                    //generateOTPRequest.datos = generateOTPDatos


                                                                    Logger.d(mapper.writeValueAsString(generateOTPDatos))

                                                                    val bodyGenerateOTP = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(generateOTPDatos))

                                                                    val builderGenerateOTP = Request.Builder()
                                                                    builderGenerateOTP.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.OTP_URL)
                                                                    Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.OTP_URL)

                                                                    val requestSearch = builderGenerateOTP
                                                                            .header("Content-Type", "application/json")
                                                                            .header("Accept", "application/json")
                                                                            .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                                            .post(bodyGenerateOTP)
                                                                            .build()


                                                                    client.newCall(requestSearch).enqueue(object : Callback {
                                                                        override fun onFailure(call: Call, e: IOException) {

                                                                            e.printStackTrace()

                                                                        }

                                                                        @Throws(IOException::class)
                                                                        override fun onResponse(call: Call, response: Response) {

                                                                            var generateOTPString = response.body()!!.string()

                                                                            Logger.d(generateOTPString)

                                                                            if(generateOTPString != null){

                                                                                generateOTPString = convertStandardJSONString(generateOTPString)

                                                                                if (JSONUtils.isJSONValid(generateOTPString)) {


                                                                                    val generateOTPResponse = mapper.readValue<GenerateOTPResponse>(generateOTPString, GenerateOTPResponse::class.java)


                                                                                    if(generateOTPResponse != null){

                                                                                        if(generateOTPResponse.status!!) {
                                                                                            otp = generateOTPResponse.otp
                                                                                            SessionManager.putString("OTP", otp)
                                                                                        }else{
                                                                                            mHandler.post {
                                                                                                run {
                                                                                                    PrettyDialog(applicationContext)
                                                                                                            .setTitle("Información")
                                                                                                            .setMessage("Error generando la OTP, por favor de click en generar nueva OTP.")
                                                                                                            .show()
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                    }else{

                                                                                    }

                                                                                }else{

                                                                                }


                                                                            }else{

                                                                            }

                                                                        }
                                                                    })


                                                                }else{
                                                                    SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                                                }

                                                            }else{
                                                                Logger.d("json object is null")
                                                            }

                                                        }else{
                                                            Logger.d("Is not json object")
                                                        }

                                                    }

                                                }

                                            }
                                        })

                                    }else{
                                        SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                    }

                                }else{
                                    Logger.d("json object is null")
                                }

                            }else{
                                Logger.d("Is not json object")
                            }

                        }

                    }

                }
            })


        }catch (ex: Exception){

        }
    }
    fun convertStandardJSONString(datajson: String): String {
        var datajson = datajson
        datajson = datajson.replace("@", "")
        return datajson
    }
    private fun makeUseAmount(){
        try{
            val formBody = FormBody.Builder()
                    .add("username", AppConstants.TOKEN_USERNAME)
                    .add("password", AppConstants.TOKEN_PASSWORD)
                    .add("grant_type", AppConstants.TOKEN_GRANT_TYPE)
                    .build()

            val builderToken = Request.Builder()
            builderToken.url(WebConstant.TOKEN_URL)
            Logger.d(WebConstant.TOKEN_URL)

            val request = builderToken
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", AppConstants.TOKEN_HEADER_AUTHORIZATION_TOKEN)
                    .post(formBody)
                    .build()


            mHandler.post{
                runOnUiThread{
                    startProgress()
                }
            }


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    e.printStackTrace()
                    mHandler.post {
                        run {
                            stopProgess()
                            PrettyDialog(applicationContext)
                                    .setTitle("Información")
                                    .setMessage("Error realizando la transacción" + e.message)
                                    .show()
                        }
                    }

                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {


                    val responseTokenString = response.body()!!.string()
                    val tokenJSONObject: JSONObject
                    val tokenResponse: TokenResponse

                    if (responseTokenString != null) {

                        Logger.d("Response from get token")
                        Logger.d(responseTokenString)

                        if (JSONUtils.isJSONValid(responseTokenString)) {

                            tokenJSONObject = JSONObject(responseTokenString)

                            if(tokenJSONObject != null){

                                tokenResponse =  mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)

                                if(tokenResponse != null){

                                    Logger.d("Token response on object")
                                    Logger.d(tokenResponse.access_token)

                                    if(tokenResponse.access_token != null){

                                        SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenResponse.access_token)


                                        val builderIp = Request.Builder()
                                        builderIp.url(WebConstant.IPIFY_ENDPOINT)

                                        val requestIp = builderIp
                                                .header("Accept", "application/json")
                                                .build()


                                        client.newCall(requestIp).enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {

                                                e.printStackTrace()
                                                mHandler.post {
                                                    run {
                                                        stopProgess()
                                                        PrettyDialog(applicationContext)
                                                                .setTitle("Información")
                                                                .setMessage("Error realizando la transacción" + e.message)
                                                                .show()
                                                    }
                                                }

                                            }

                                            @Throws(IOException::class)
                                            override fun onResponse(call: Call, response: Response) {


                                                val responseIpString = response.body()!!.string()
                                                val ipJSONObject: JSONObject
                                                val ipResponse: IPResponse

                                                if (responseIpString != null) {

                                                    Logger.d("Response from get ip")
                                                    Logger.d(responseIpString)

                                                    if (JSONUtils.isJSONValid(responseIpString)) {

                                                        ipJSONObject = JSONObject(responseIpString)

                                                        if(ipJSONObject != null){

                                                            ipResponse =  mapper.readValue<IPResponse>(ipJSONObject.toString(), IPResponse::class.java)

                                                            if(ipResponse != null){

                                                                Logger.d("Token response on object")
                                                                Logger.d(ipResponse.ip)

                                                                if(ipResponse.ip != null){

                                                                    val makeUseTransactionRequest = MakeUseTransactionRequest()
                                                                    val makeUseTransactionDatos = MakeUseTransactionDatos()



                                                                    /*{
                                                                    "idusuario":"10024",
                                                                    "ip":"127.0.0.1",
                                                                    "accion":"3",
                                                                    "entidadorigen":"10003",
                                                                    "entidaddestino":"10266",
                                                                    "idtipomovimientoproducto":"6",
                                                                    "valor":"100000",
                                                                    "observacion":"252",
                                                                    "documento":"80794978",
                                                                    "otp":"123456",
                                                                    "valorcuota":"20000",
                                                                    "idperiodo":"",
                                                                    "fechapago":""
                                                                    }*/


                                                                    // {"datos":
                                                                    // {"accion":"3",
                                                                    // "documento":"80794978",
                                                                    // "entidaddestino":"10266",
                                                                    // "entidadorigen":"10384",
                                                                    // "fechapago":"",
                                                                    // "idperiodo":"",
                                                                    // "idtipomovimientoproducto":"6",
                                                                    // "idusuario":"10259",
                                                                    // "ip":"161.10.31.113",
                                                                    // "observacion":"",
                                                                    // "otp":"575127",
                                                                    // "valor":"10000",
                                                                    // "valorcuota":"1000"}}

                                                                    makeUseTransactionDatos.accion = "3"
                                                                    makeUseTransactionDatos.ip = ipResponse.ip
                                                                    makeUseTransactionDatos.idusuario = idusuario
                                                                    makeUseTransactionDatos.idusuario = idusuario
                                                                    makeUseTransactionDatos.documento = documento
                                                                    makeUseTransactionDatos.entidadorigen = identidad
                                                                    //makeUseTransactionDatos.entidadorigen = "10003"
                                                                    makeUseTransactionDatos.entidaddestino = searchDataresponse!!.identidad.toString()
                                                                    //makeUseTransactionDatos.entidaddestino = "10266"
                                                                    makeUseTransactionDatos.fechapago = editTextQuotaDate.text.toString()
                                                                    makeUseTransactionDatos.idperiodo = idperiodo
                                                                    makeUseTransactionDatos.observacion = ""
                                                                    makeUseTransactionDatos.otp = SessionManager.getString("OTP", null)
                                                                    // makeUseTransactionDatos.otp = "123456"
                                                                    makeUseTransactionDatos.valor = editTextAmount.text.toString()
                                                                    makeUseTransactionDatos.valorcuota = editTextQuota.text.toString()
                                                                    makeUseTransactionDatos.idtipomovimientoproducto = "5"


                                                                    makeUseTransactionRequest.datos = makeUseTransactionDatos


                                                                    Logger.d(mapper.writeValueAsString(makeUseTransactionRequest))

                                                                    val bodySearch = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(makeUseTransactionRequest))

                                                                    val builderSearch = Request.Builder()
                                                                    builderSearch.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                                                    Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                                                    val requestSearch = builderSearch
                                                                            .header("Content-Type", "application/json; charset=UTF-8")
                                                                            .header("Accept", "application/json")
                                                                            .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                                            .header("operation", OperationConstants.SEARCH_BALANCE_OPERATION)
                                                                            .post(bodySearch)
                                                                            .build()


                                                                    client.newCall(requestSearch).enqueue(object : Callback {
                                                                        override fun onFailure(call: Call, e: IOException) {

                                                                            e.printStackTrace()
                                                                            mHandler.post{
                                                                                runOnUiThread{
                                                                                    stopProgess()
                                                                                    PrettyDialog(applicationContext)
                                                                                            .setTitle("Información")
                                                                                            .setMessage("Error procesando la transacción" + e.message)
                                                                                            .show()
                                                                                }
                                                                            }

                                                                        }

                                                                        @Throws(IOException::class)
                                                                        override fun onResponse(call: Call, response: Response) {


                                                                            var makeTransactionString = response.body()!!.string()
                                                                            Logger.d(makeTransactionString)
                                                                            makeTransactionString = makeTransactionString.replace("\"dataresponse\":{\"@nil\":\"true\"},", "")
                                                                            Logger.d(makeTransactionString)

                                                                            if (JSONUtils.isJSONValid(responseIpString)) {

                                                                                val makeUseTransactionResponse = mapper.readValue<MakeUseTransactionResponse>(makeTransactionString, MakeUseTransactionResponse::class.java)

                                                                                //val makeUseTransactionDataResponse = mapper.readValue<MakeUseTransactionDataResponse>(makeTransactionString, MakeUseTransactionDataResponse::class.java)

                                                                                if(makeUseTransactionResponse != null){

                                                                                    if(makeUseTransactionResponse.status!!){

                                                                                        mHandler.post {
                                                                                            run {

                                                                                                stopProgess()
                                                                                                try {

                                                                                                    sendSMSSuccessTransaction()

                                                                                                    sendPaymentPlanMail(makeUseTransactionResponse.response!!.dataresponse!!)

                                                                                                    val dialog = PrettyDialog(applicationContext)
                                                                                                            .setTitle(getString(R.string.information))
                                                                                                            .setMessage("Transacción Exitosa. Desembolso realizado exitosamente.")
                                                                                                            .setAnimationEnabled(true)


                                                                                                    dialog.addButton(
                                                                                                            getString(android.R.string.ok), // button text
                                                                                                            R.color.pdlg_color_white, // button text color
                                                                                                            R.color.pdlg_color_green // button background color
                                                                                                    ) // button OnClick listener
                                                                                                    {


                                                                                                        val fragment = TakeDNIPictureFragment()
                                                                                                        val tx = fragmentManager!!.beginTransaction()
                                                                                                        ///tx.replace(R.id.main_fragment, fragment)
                                                                                                        tx.addToBackStack("TakeDNIPicFrg").commit()


                                                                                                    }

                                                                                                    dialog.setOnCancelListener {
                                                                                                        val fragment = TakeDNIPictureFragment()
                                                                                                        val tx = fragmentManager!!.beginTransaction()
                                                                                                        //aquitx.replace(R.id.main_fragment, fragment)
                                                                                                        tx.addToBackStack("TakeDNIPicFrg").commit()
                                                                                                    }

                                                                                                    dialog.show()
                                                                                                } catch (ex: Exception) {
                                                                                                    ex.printStackTrace()
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }else{
                                                                                        mHandler.post {
                                                                                            run {
                                                                                                stopProgess()
                                                                                                PrettyDialog(applicationContext)
                                                                                                        .setTitle("Información")
                                                                                                        .setMessage("Error realizando la transacción, intente nuevamente.")
                                                                                                        .show()
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }else{
                                                                                    mHandler.post {
                                                                                        run {
                                                                                            stopProgess()
                                                                                            PrettyDialog(applicationContext)
                                                                                                    .setTitle("Información")
                                                                                                    .setMessage("Error realizando la transacción")
                                                                                                    .show()
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }else{
                                                                                mHandler.post {
                                                                                    stopProgess()
                                                                                    run {
                                                                                        PrettyDialog(applicationContext)
                                                                                                .setTitle("Información")
                                                                                                .setMessage("Error realizando la transacción")
                                                                                                .show()
                                                                                    }
                                                                                }
                                                                            }

                                                                        }
                                                                    })


                                                                }else{
                                                                    SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                                                    mHandler.post {
                                                                        stopProgess()
                                                                        run {
                                                                            PrettyDialog(applicationContext)
                                                                                    .setTitle("Información")
                                                                                    .setMessage("Error realizando la transacción")
                                                                                    .show()
                                                                        }
                                                                    }
                                                                }

                                                            }else{
                                                                Logger.d("json object is null")
                                                                mHandler.post {
                                                                    stopProgess()
                                                                    run {
                                                                        PrettyDialog(applicationContext)
                                                                                .setTitle("Información")
                                                                                .setMessage("Error realizando la transacción")
                                                                                .show()
                                                                    }
                                                                }
                                                            }

                                                        }else{
                                                            Logger.d("Is not json object")
                                                            mHandler.post {
                                                                stopProgess()
                                                                run {
                                                                    PrettyDialog(applicationContext)
                                                                            .setTitle("Información")
                                                                            .setMessage("Error realizando la transacción")
                                                                            .show()
                                                                }
                                                            }
                                                        }

                                                    }

                                                }

                                            }
                                        })

                                    }else{
                                        SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                        mHandler.post {
                                            stopProgess()
                                            run {
                                                PrettyDialog(applicationContext)
                                                        .setTitle("Información")
                                                        .setMessage("Error realizando la transacción")
                                                        .show()
                                            }
                                        }
                                    }

                                }else{
                                    Logger.d("json object is null")
                                    mHandler.post {
                                        stopProgess()
                                        run {
                                            PrettyDialog(applicationContext)
                                                    .setTitle("Información")
                                                    .setMessage("Error realizando la transacción")
                                                    .show()
                                        }
                                    }
                                }

                            }else{
                                Logger.d("Is not json object")
                                mHandler.post {
                                    stopProgess()
                                    run {
                                        PrettyDialog(applicationContext)
                                                .setTitle("Información")
                                                .setMessage("Error realizando la transacción")
                                                .show()
                                    }
                                }
                            }

                        }

                    }

                }
            })


        }catch (ex: Exception){
            ex.printStackTrace()
            mHandler.post {
                stopProgess()
                run {
                    PrettyDialog(applicationContext)
                            .setTitle("Información")
                            .setMessage("Error realizando la transacción")
                            .show()
                }
            }
        }
    }
    fun sendSMSSuccessTransaction(){

        try{
            val formBody = FormBody.Builder()
                    .add("username", AppConstants.TOKEN_USERNAME)
                    .add("password", AppConstants.TOKEN_PASSWORD)
                    .add("grant_type", AppConstants.TOKEN_GRANT_TYPE)
                    .build()

            val builderToken = Request.Builder()
            builderToken.url(WebConstant.TOKEN_URL)
            Logger.d(WebConstant.TOKEN_URL)

            val request = builderToken
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", AppConstants.TOKEN_HEADER_AUTHORIZATION_TOKEN)
                    .post(formBody)
                    .build()




            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    e.printStackTrace()
                    mHandler.post {
                        run {
                            PrettyDialog(applicationContext)
                                    .setTitle("Información")
                                    .setMessage("Error generando la OTP " + e.message)
                                    .show()
                        }
                    }

                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {


                    val responseTokenString = response.body()!!.string()
                    val tokenJSONObject: JSONObject
                    val tokenResponse: TokenResponse

                    if (responseTokenString != null) {

                        Logger.d("Response from get token")
                        Logger.d(responseTokenString)

                        if (JSONUtils.isJSONValid(responseTokenString)) {

                            tokenJSONObject = JSONObject(responseTokenString)

                            if(tokenJSONObject != null){

                                tokenResponse =  mapper.readValue<TokenResponse>(tokenJSONObject.toString(), TokenResponse::class.java)

                                if(tokenResponse != null){

                                    Logger.d("Token response on object")
                                    Logger.d(tokenResponse.access_token)

                                    if(tokenResponse.access_token != null){

                                        SessionManager.putString(SessionKeys.ESB_TOKEN.key, tokenResponse.access_token)


                                        val builderIp = Request.Builder()
                                        builderIp.url(WebConstant.IPIFY_ENDPOINT)

                                        val requestIp = builderIp
                                                .header("Accept", "application/json")
                                                .build()


                                        client.newCall(requestIp).enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {

                                                e.printStackTrace()

                                                mHandler.post {
                                                    run {
                                                        PrettyDialog(applicationContext)
                                                                .setTitle("Información")
                                                                .setMessage("Error generando la OTP " + e.message)
                                                                .show()
                                                    }
                                                }


                                            }

                                            @Throws(IOException::class)
                                            override fun onResponse(call: Call, response: Response) {


                                                val responseIpString = response.body()!!.string()
                                                val ipJSONObject: JSONObject
                                                val ipResponse: IPResponse

                                                if (responseIpString != null) {

                                                    Logger.d("Response from get ip")
                                                    Logger.d(responseIpString)

                                                    if (JSONUtils.isJSONValid(responseIpString)) {

                                                        ipJSONObject = JSONObject(responseIpString)

                                                        if(ipJSONObject != null){

                                                            ipResponse =  mapper.readValue<IPResponse>(ipJSONObject.toString(), IPResponse::class.java)

                                                            if(ipResponse != null){

                                                                Logger.d("Token response on object")
                                                                Logger.d(ipResponse.ip)

                                                                if(ipResponse.ip != null){

                                                                    val searchMessageRequest = SearchMessageRequest()
                                                                    val searchMessageDatos = SearchMessageDatos()
                                                                    searchMessageDatos.schema = "admin"
                                                                    searchMessageDatos.tabla = "mensajes"
                                                                    searchMessageDatos.campo = "*"
                                                                    searchMessageDatos.condicion = "idmensaje = 6"
                                                                    searchMessageRequest.datos =searchMessageDatos


                                                                    Logger.d(mapper.writeValueAsString(searchMessageRequest))

                                                                    val bodyGenerateOTP = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(searchMessageRequest))

                                                                    val builderGenerateOTP = Request.Builder()
                                                                    builderGenerateOTP.url(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)
                                                                    Logger.d(YoPrestoApp.getEndpoint(YoPrestoApp()) + WebConstant.DEV_PORT + WebConstant.WEB_URL)

                                                                    val requestSearch = builderGenerateOTP
                                                                            .header("Content-Type", "application/json")
                                                                            .header("Accept", "application/json")
                                                                            .header("Authorization", "Bearer " + SessionManager.getString(SessionKeys.ESB_TOKEN.key, null))
                                                                            .post(bodyGenerateOTP)
                                                                            .build()


                                                                    client.newCall(requestSearch).enqueue(object : Callback {
                                                                        override fun onFailure(call: Call, e: IOException) {

                                                                            e.printStackTrace()

                                                                        }

                                                                        @Throws(IOException::class)
                                                                        override fun onResponse(call: Call, response: Response) {

                                                                            var getMessageString = response.body().toString()
                                                                            Logger.d(getMessageString)

                                                                            //Toast.makeText(applicationContext, "La utilización se ha realizado con exito!", Toast.LENGTH_LONG)

                                                                            val intent = Intent(applicationContext, SuccessfulTransaction::class.java)
                                                                            startActivity(intent)
                                                                        }
                                                                    })


                                                                }else{
                                                                    SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                                                }

                                                            }else{
                                                                Logger.d("json object is null")
                                                            }

                                                        }else{
                                                            Logger.d("Is not json object")
                                                        }

                                                    }

                                                }

                                            }
                                        })

                                    }else{
                                        SessionManager.removeKey(SessionKeys.ESB_TOKEN.key)
                                    }

                                }else{
                                    Logger.d("json object is null")
                                }

                            }else{
                                Logger.d("Is not json object")
                            }

                        }

                    }

                }
            })


        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }
    fun sendPaymentPlanMail(idtransaccion: String){
        try{
            /*val formBody = FormBody.Builder()
                    .add("idtransaccion", idtransaccion)
                    .build()

            val builderToken = Request.Builder()
            builderToken.url(WebConstant.SEND_MAIL_ENDPOINT)
            Logger.d(WebConstant.SEND_MAIL_ENDPOINT)*/
            val sendMail = SendMail()
            sendMail.idtransaccion = idtransaccion

            Logger.d("To send idtransaction")
            Logger.d(sendMail.idtransaccion)

            val body = RequestBody.create(HttpObjectsConstants.jsonMediaType, mapper.writeValueAsString(sendMail))

            /*val request = builderToken
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .post(formBody)
                    .build()*/

            val builder = Request.Builder()
            builder.url(WebConstant.SEND_MAIL_ENDPOINT)
            Logger.d(WebConstant.SEND_MAIL_ENDPOINT)

            val request = builder
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("Accept", "application/json")
                    .post(body)
                    .build()




            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    e.printStackTrace()
                    mHandler.post {
                        run {
                            PrettyDialog(applicationContext)
                                    .setTitle("Información")
                                    .setMessage("Error generando enviando el mensaje " + e.message)
                                    .show()
                        }
                    }

                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {


                    val responseEmail = response.body()!!.string()
                    Logger.d("Send image response")
                    Logger.d(responseEmail)


                }
            })


        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }
    private fun startProgress(){

        val builder = MaterialDialog.Builder(this).title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)

        progressDialog = builder.build()
        progressDialog!!.show()
    }
    private fun stopProgess(){
        if(progressDialog != null){
            progressDialog!!.dismiss()
        }
    }

}