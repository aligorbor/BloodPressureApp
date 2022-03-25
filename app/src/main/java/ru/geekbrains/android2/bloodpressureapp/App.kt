package ru.geekbrains.android2.bloodpressureapp

import android.annotation.SuppressLint
import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.parse.Parse

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )
        dbInstance = Firebase.firestore
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var dbInstance: FirebaseFirestore
    }

}