package com.seook.api_example

import android.app.Application

//싱글턴으로 context 가져옴.
//manifest에 name으로 반영해주어야함.
class App : Application() {

    companion object{
        lateinit var instance : App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}