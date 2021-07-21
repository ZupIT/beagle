/*
 * Copyright 2021 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
                    val json: String = GsonBuilder().setPrettyPrinting().create().toJson(report)
                    analytics_text.text = json
                }
            }
        })
    }
}
