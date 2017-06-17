package com.auth0.samples.androidkotlinauth

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.android.volley.toolbox.Volley
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.samples.androidkotlinauth.databinding.ActivityMainBinding
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.UserProfile


class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null
    var auth0: Auth0 = Auth0("dCbk1ioiI470l5RjsQJjaI4M5OtEdtmd", "bkrebs.auth0.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding?.loggedIn = false

        val loginButton = findViewById(R.id.login_button)
        loginButton.setOnClickListener { login() }
        val queue = Volley.newRequestQueue(this)

        val addItemButton = findViewById(R.id.add_item)
        val itemEditText = findViewById(R.id.item) as EditText
        val act = this;

        addItemButton.setOnClickListener { v ->
            val authentication = AuthenticationAPIClient(auth0)
            try {
                authentication.userInfo(CredentialsManager.getCredentials(this).accessToken!!)
                        .start(object : BaseCallback<UserProfile, AuthenticationException> {
                            override fun onSuccess(payload: UserProfile) {
                                Toast.makeText(act.baseContext, payload.email, Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(error: AuthenticationException) {
                                error.toString()
                            }
                        })
            } catch (e: Exception) {
                e.toString()
            }
        }

        val listView = findViewById(R.id.listview) as ListView
        getItems(this, queue, listView)
    }

    override fun onNewIntent(intent: Intent) {
        if (WebAuthProvider.resume(intent)) {
            binding?.loggedIn = true
            return
        }
        super.onNewIntent(intent)
    }

    private fun login() {
        WebAuthProvider.init(auth0)
                .withScheme("demo")
                .withScope("openid")
                .withConnectionScope("manage:todo")
                .start(this, AuthenticationHandler(this.applicationContext))
    }
}
