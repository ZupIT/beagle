package br.com.zup.beagle.appiumapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.appiumapp.R
import br.com.zup.beagle.appiumapp.config.newanalytics.RecordService
import br.com.zup.beagle.appiumapp.config.newanalytics.ReportListener
import br.com.zup.beagle.newanalytics.AnalyticsRecord
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_analytics.*
import org.json.JSONObject

class AnalyticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)
        RecordService.setListener(object : ReportListener {
            override fun onReport(report: AnalyticsRecord) {
                runOnUiThread {
                    val json = JSONObject()
                    json.put("report", report)
                    val gson: Gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJsonString: String = gson.toJson(report)
                    analytics_text.text = prettyJsonString
                }
            }
        })
    }
}
