package com.alvaresjonathan.simplelogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class SignOutActivity: AppCompatActivity() {

    lateinit var signOutButton: Button

    private val RC_SIGN_IN = 1
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signout)

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signOutButton = findViewById(R.id.signout_button)
        signOutButton.setOnClickListener(
            View.OnClickListener {
                mGoogleSignInClient?.signOut()?.addOnCompleteListener(this
                ) { Toast.makeText(this@SignOutActivity, "Signed Out", Toast.LENGTH_LONG).show() }
                startActivity(Intent(this, LoginActivity::class.java))
            }
        )
    }
}