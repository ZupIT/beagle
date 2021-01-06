package br.com.zup.beagle.appiumapp.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.analytics2.AnalyticsRecord
import br.com.zup.beagle.android.utils.loadView
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.appiumapp.R
import br.com.zup.beagle.appiumapp.config.analytics2.RecordService
import br.com.zup.beagle.appiumapp.config.analytics2.ReportListener
import kotlinx.android.synthetic.main.activity_analytics.*

class AnalyticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)
        RecordService.setListener(object : ReportListener {
            override fun onReport(report: AnalyticsRecord) {
                val text = "platform:" + report.platform + "," +
                        report.attributes + "," +
                        "type:" + report.type
                analytics_text.text = text

            }
        })
    }

}