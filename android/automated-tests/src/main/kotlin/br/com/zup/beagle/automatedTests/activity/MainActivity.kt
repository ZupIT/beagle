/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
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

package br.com.zup.beagle.automatedTests.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.automatedTests.R

class MainActivity : AppCompatActivity() {

    companion object {
        const val BFF_URL_KEY = "bffUrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = bffUrl()
        if(url != null) {
            val intent = BeagleActivity.newIntent(this, ScreenRequest(url))
            startActivity(intent)
            finish()
        }
    }

    fun bffUrl() = intent.extras?.getString(BFF_URL_KEY) ?: "http://10.0.2.2:8080/button"
}