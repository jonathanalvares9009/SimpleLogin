package com.alvaresjonathan.simplelogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaresjonathan.simplelogin.FacebookLogin.callbackManager
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        facebookLoginButton.setPermissions(Arrays.asList("public_profile", "email"))
        facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.e("TAG", "login")
                //                Toast.makeText(MainActivity.this,"Logged In successfully",Toast.LENGTH_LONG).show();
                //Use GraphApi to get the information into the app.
                val request = GraphRequest.newMeRequest( //pass two parameter
                        loginResult.accessToken
                )  //one is the current token
                { `object`, response ->
                    //2nd is grahJSONObject callback
                    Log.v("MainActivity", response.toString())

                    // Application code
                    try {
                        val obj = `object`.toString() //get complete JSON object refrence.
                        val name = `object`.getString("name") //get particular JSON Object
                        val email = `object`.getString("email")
                        val id = `object`.getString("id")
                        Log.e("completeObjInfo", obj)
                        Log.e("TAGObjectInfo", "id: $id")
                        Log.e("TAGObjectInfo", "name: $name")
                        Log.e("TAGObjectInfo", "email: $email")
                        saveUser(createRequest(email.toString(), name.toString()))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender,birthday") //set these parameter
                request.parameters = parameters
                request.executeAsync() //exuecute task in seprate thread
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

        googleLoginButton.setOnClickListener(
                View.OnClickListener {
                    signIn()
                }
        )
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        var name: String = "Jonathan"
        var email: String = "alvares.jonthan@yahoo.com"
        if(account!=null) {
            account.email?.let { email = it }
            account.givenName?.let { name = it }
            account.familyName?.let { name += " $it" }
        }
        saveUser(createRequest(email, name))
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
                onStart()
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

    fun createRequest(email: String, name: String): UserRequest? {
        val userRequest: UserRequest = UserRequest()
        userRequest.email = email
        userRequest.name = name
        return userRequest
    }

    fun saveUser(userRequest: UserRequest?) {
        Log.e("error", userRequest.toString())
        val userResponseCall = ApiClient.getUserService().saveUser(userRequest)
        userResponseCall.enqueue(object : Callback<UserResponse?> {
            override fun onResponse(
                call: Call<UserResponse?>,
                response: Response<UserResponse?>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Saved data", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Couldn't save data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Couldn't save data"+t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e("data error", t.localizedMessage)
            }
        })
    }
}