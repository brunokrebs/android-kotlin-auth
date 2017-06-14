package com.auth0.samples.androidkotlinauth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import android.databinding.DataBindingUtil
import com.auth0.samples.androidkotlinauth.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding : ActivityMainBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_main)

        binding.loggedIn = false
        binding.inputLabel = resources.getString(R.string.label_please_login)
        binding.buttonLabel = resources.getString(R.string.label_please_login)

        request()
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
}
