package com.alvaresjonathan.simplelogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SignOutActivity: AppCompatActivity() {

    lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signout)

        signOutButton = findViewById(R.id.signout_button)
        signOutButton.setOnClickListener(
            View.OnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        )
    }
}