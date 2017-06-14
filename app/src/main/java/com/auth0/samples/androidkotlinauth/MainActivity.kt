package com.auth0.samples.androidkotlinauth

import android.app.Dialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.samples.androidkotlinauth.databinding.ActivityMainBinding
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding : ActivityMainBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_main)

        binding.loggedIn = false

        val button = findViewById(R.id.login_button)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                login()
            }
        })

        request()
    }

    override fun onNewIntent(intent: Intent) {
        if (WebAuthProvider.resume(intent)) {
            return
        }
        super.onNewIntent(intent)
    }

    fun request() {
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:8080/"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener<JSONArray> { response -> run {
                        val listview = findViewById(R.id.listview) as ListView

                        val list = ArrayList<String>()

                        for (i in 0 until response.length()) {
                            list.add(response[i].toString())
                        }

                        val adapter = ArrayAdapter(this,
                                android.R.layout.simple_list_item_1, list)
                        listview.setAdapter(adapter)
                    }
                },
                Response.ErrorListener { error -> run {
                        Log.w(error.toString(), "err")
                    }
                })
        //add request to queue
        queue.add(jsonArrayRequest)
    }

    private fun login() {
        val auth0 = Auth0("dCbk1ioiI470l5RjsQJjaI4M5OtEdtmd", "bkrebs.auth0.com")
        auth0.isOIDCConformant = true
        WebAuthProvider.init(auth0)
                .withScheme("demo")
                .start(this@MainActivity, object : AuthCallback {
                    override fun onFailure(dialog: Dialog) {
                        Log.w("", dialog.toString())
                        // Show error Dialog to user
                    }

                    override fun onFailure(exception: AuthenticationException) {
                        // Show error to user
                        Log.w("", exception.toString())
                    }

                    override fun onSuccess(credentials: Credentials) {
                        // Store credentials
                        // Navigate to your main activity
                        Log.w("", credentials.toString())
                    }
                })
    }
}
