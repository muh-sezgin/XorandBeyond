package com.example.xorbeyond

import android.os.Bundle
import android.util.Log
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
import com.example.xorbeyond.MainActivity.Companion.baseURL
import com.example.xorbeyond.MainActivity.Companion.profileUpdateList
import java.lang.Thread.sleep

//fragment for log page
//get json from url and put it into list
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
        val request = Request.Builder().url(baseURL + "id")
            .get().build()
        var jsonarray_info:JSONArray ?= null
        var received = false
        //get response
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
            Log.d("ttt", "waiting")
        }
        val lv = context.findViewById(R.id.visayas_mindanao_lview) as ListView
        var list = ArrayList<String>()

        //parse response and add it into list
        for (i in 0 until jsonarray_info!!.length()) {
            var eventType = ((jsonarray_info?.get(i) as JSONObject).get("Event Type").toString())
            var logOfEvent = ""
            if(eventType == "Intrusion") {
                logOfEvent += "Intrusion"
            }
            else if(eventType == "Anomalous Activity") {
                logOfEvent += "Anomalous Activity"
            }

            else if(eventType == "Malfunction") {
                logOfEvent += "Malfunction"
            }

            else {
                var currentSubject = ((jsonarray_info?.get(i) as JSONObject).get("Subject").toString())

                if(!profileUpdateList?.get(currentSubject).isNullOrBlank()) {
                    currentSubject = profileUpdateList?.get(currentSubject).toString()

                }

                logOfEvent += currentSubject
                logOfEvent += " entered"
            }

            logOfEvent += " from "

            logOfEvent += ((jsonarray_info?.get(i) as JSONObject).get("Event Location").toString())

            logOfEvent += " at "

            logOfEvent += ((jsonarray_info?.get(i) as JSONObject).get("Event Time").toString())

            list.add(logOfEvent)
        }
        //reverse list to have newest log first
        list.reverse()

        //listview creater
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
        lv.adapter = adapter
    }
}
