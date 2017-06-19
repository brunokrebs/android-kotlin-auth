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
import com.auth0.android.provider.WebAuthProvider
import com.auth0.samples.androidkotlinauth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null

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

        CredentialsManager.setContext(this)

        val listView = findViewById(R.id.listview) as ListView

        addItemButton.setOnClickListener {
            val item = itemEditText.text.toString()
            addItem(queue, item, CredentialsManager.getAccessToken(), {
                itemEditText.text.clear()
                Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show()
                getItems(this, queue, listView)
            })
        }

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
        WebAuthProvider.init(Auth0("-2g6R4254d_BtLR2NTfsks_Iu_4LVMsi", "krebshaus.auth0.com"))
                .withScheme("demo")
                .withConnectionScope("manage:todo")
                .withAudience("http://kotlin-test.com")
                .start(this, AuthenticationHandler(this.applicationContext))
    }
}
