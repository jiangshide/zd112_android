package com.android.zd112

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var intent = Intent(this,MainActivity::class.java)
        var handle = Handler()
        handle.postDelayed(Runnable {
                kotlin.run {
                    startActivity(intent)
                }
        },3*1000)
    }
}
