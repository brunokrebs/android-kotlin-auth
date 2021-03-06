package com.auth0.samples.androidkotlinauth

import android.app.Activity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray

val ENDPOINT = "http://10.0.2.2:8080/"

fun getItems(activity: Activity, queue: RequestQueue, listView: ListView) {
    val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, ENDPOINT, null,
            Response.Listener<JSONArray> { response ->
                val list = ArrayList<String>()

                (0 until response.length()).mapTo(list) { response[it].toString() }

                val adapter = ArrayAdapter(activity,
                        android.R.layout.simple_list_item_1, list)
                listView.adapter = adapter
            },
            Response.ErrorListener { error ->
                Toast.makeText(activity.applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            }
    )
    //add getItems to queue
    queue.add(jsonArrayRequest)
}

fun addItem(activity: Activity, queue: RequestQueue, item: String, accessToken: String) {
    val postRequest = object : StringRequest(Request.Method.POST, ENDPOINT,
            Response.Listener<String> {
                response -> Log.d("Response", response)
            },
            Response.ErrorListener { error ->
                Log.d("Error.Response", error.localizedMessage)
            }
    ) {
        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            return item.toByteArray()
        }

        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val headers = super.getHeaders()
            headers.put("Authorization", "Beaerer " + accessToken)
            return headers
        }
    }

    queue.add(postRequest)
}
