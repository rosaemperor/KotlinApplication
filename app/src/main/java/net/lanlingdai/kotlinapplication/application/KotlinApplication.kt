package net.lanlingdai.kotlinapplication.application

import android.app.Application
import android.util.Log

class KotlinApplication : Application(){
    companion object {
        lateinit var  instance : KotlinApplication
    }

    val tag : String = "KotlinApplication"
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}