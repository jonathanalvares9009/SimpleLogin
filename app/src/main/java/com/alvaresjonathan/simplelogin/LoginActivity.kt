package com.alvaresjonathan.simplelogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    lateinit var facebookLoginButton: Button
    lateinit var googleLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        facebookLoginButton = findViewById(R.id.facebook_login_button)
        googleLoginButton = findViewById(R.id.google_login_button)

        facebookLoginButton.setOnClickListener(
            View.OnClickListener {
                startActivity(Intent(this, SignOutActivity::class.java))
            }
        )

        googleLoginButton.setOnClickListener(
            View.OnClickListener {
                startActivity(Intent(this, SignOutActivity::class.java))
            }
        )
    }
}