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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import br.com.zup.beagle.android.annotation.RegisterController
import br.com.zup.beagle.android.utils.newServerDrivenIntent
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.automatedTests.R

@RegisterController("otherController")
class AppBeagleActivity : BeagleActivity() {

    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val mToolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.custom_toolbar) }
    private val mButton: Button by lazy { findViewById<Button>(R.id.btn_retry) }
    private val mFrame: FrameLayout by lazy { findViewById<FrameLayout>(R.id.server_driven_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_beagle)
    }

    override fun getServerDrivenContainerId(): Int = R.id.server_driven_container

    override fun getToolbar(): Toolbar = mToolbar

    override fun onServerDrivenContainerStateChanged(state: ServerDrivenState) {
        when (state) {
            is ServerDrivenState.Started -> {
                progressBar.visibility = View.VISIBLE
            }
            is ServerDrivenState.Error -> {
                mFrame.visibility = View.GONE
                buttonRetry(state)
            }
            is ServerDrivenState.Finished -> progressBar.visibility = View.GONE
        }
    }

    private fun buttonRetry(state: ServerDrivenState.Error) {
        mButton.visibility = View.VISIBLE
        mButton.setOnClickListener {
            state.retry()
            mButton.visibility = View.GONE
            mFrame.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (mFrame.visibility == View.GONE && supportFragmentManager.backStackEntryCount == 1) {
            mFrame.visibility = View.VISIBLE
            mButton.visibility = View.GONE
        } else
            super.onBackPressed()
    }

    companion object {
        fun newAppIntent(context: Context, screenRequest: ScreenRequest): Intent {
            return context.newServerDrivenIntent<AppBeagleActivity>(screenRequest)
        }
    }
}