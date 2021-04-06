package com.alvaresjonathan.simplelogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alvaresjonathan.simplelogin.FacebookLogin.callbackManager
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import org.json.JSONException
import java.util.*


class LoginActivity : AppCompatActivity() {

    lateinit var facebookLoginButton: LoginButton
    lateinit var googleLoginButton: SignInButton

    private val RC_SIGN_IN = 1
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        callbackManager = CallbackManager.Factory.create()

        val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        facebookLoginButton = findViewById(R.id.facebook_login_button)
        googleLoginButton = findViewById(R.id.google_login_button)

        facebookLoginButton.setOnClickListener(
                View.OnClickListener {
                    var success = 0
                    LoginManager.getInstance().logInWithReadPermissions(this, (Arrays.asList("public_profile", "email", "last_name")))
                    LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                        override fun onSuccess(loginResult: LoginResult?) {
                            Log.d("Demo", "Login Successful")
                            try {
                                val accessToken = AccessToken.getCurrentAccessToken()
                                val request = GraphRequest.newMeRequest(
                                        accessToken
                                ) { `object`, response ->
                                    Log.d("TAG", "Graph Object :$`object`")
                                    try {
                                        val splitStr = `object`.getString("name").split("\\s+").toTypedArray()
                                        var FBFirstname = splitStr[0]
//                            var FBLastName = `object`.getString("last_name")
                                        var FBEmail = `object`.getString("email")
                                        var FBUUID = `object`.getString("id")
                                        Log.e("TAG", "firstnamev: " + splitStr[0])
//                            Log.e("TAG", "last name: " + FBLastName)
                                        Log.e("TAG", "Email id : " + `object`.getString("email"))
                                        Log.e("TAG", "ID :" + `object`.getString("id"))
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                                val parameters = Bundle()
                                parameters.putString("fields", "id,name,link,birthday,gender,email")
                                request.parameters = parameters
                                request.executeAsync()

                                success = 1
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onCancel() {
                            // App code
                            Log.v("MainActivity", "cancel")
                        }

                        override fun onError(exception: FacebookException) {
                            // App code
                            Log.v("MainActivity", exception.cause.toString())
                        }
                    })

                    if (success == 1) {
                        startActivity(Intent(this, SignOutActivity::class.java))
                    }
                }
        )

        googleLoginButton.setOnClickListener(
                View.OnClickListener {
                    signIn()
                }
        )
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(account!=null) {
            account.email?.let { Log.i("account", it) }
            account.givenName?.let { Log.i("first_name", it) }
            account.familyName?.let { Log.i("last_name", it) }
        }
    }

    private fun signIn() {
        val intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                    GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account : GoogleSignInAccount? = task.getResult(ApiException::class.java)
                startActivity(Intent(this, SignOutActivity::class.java))

            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.e("TAG", "signInResult:failed code=" + e.statusCode)
            }
        }
        if(callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }
}