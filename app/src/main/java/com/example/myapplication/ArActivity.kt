package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.ar.sceneform.ux.ArFragment

class ArActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        var message: String? = intent.getStringExtra("message_key")

        (supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment)
            .setOnTapPlaneGlbModel(message)
    }
}