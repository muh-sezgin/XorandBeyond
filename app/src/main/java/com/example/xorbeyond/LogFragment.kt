package com.example.xorbeyond

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class LogFragment : Fragment() {
    val client = OkHttpClient()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = context as MainActivity
        var request = Request.Builder().url("http://192.168.137.176:5002/id")
            .get().build()
        var jsonarray_info:JSONArray ?= null
        var received = false
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                //println(response.body?.string())
                var str_response = response.body!!.string()

                val json_contact: JSONObject = JSONObject(str_response)
                jsonarray_info = json_contact.getJSONArray("Log")
                received = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println(e.message.toString())
            }
        })
        while(!received) {

        }
        val lv = context.findViewById(R.id.visayas_mindanao_lview) as ListView
        var list = ArrayList<String>()

        for (i in 0 until jsonarray_info!!.length()) {

            list.add(jsonarray_info?.get(i).toString())
        }

        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
        lv.adapter = adapter
    }
}
