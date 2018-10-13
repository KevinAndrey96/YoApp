package com.yopresto.app.yoprestoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class Enrollment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrollment)
        Toast.makeText(applicationContext, "Hola Enrolamiento", Toast.LENGTH_LONG)
    }
}
