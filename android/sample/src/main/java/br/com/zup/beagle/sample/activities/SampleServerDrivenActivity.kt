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

package br.com.zup.beagle.sample.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import br.com.zup.beagle.android.annotation.BeagleComponent
import br.com.zup.beagle.android.annotation.RegisterController
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.FragmentTransitionAnimation
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.sample.R

@RegisterController
class SampleServerDrivenActivity : BeagleActivity() {

    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val mToolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val mButton: Button by lazy { findViewById<Button>(R.id.btn_retry) }
    private val mFrame: FrameLayout by lazy { findViewById<FrameLayout>(R.id.server_driven_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_server_driven)
    }

    override fun getServerDrivenContainerId(): Int = R.id.server_driven_container

    override fun getToolbar(): Toolbar = mToolbar

    override fun onServerDrivenContainerStateChanged(state: ServerDrivenState) {
        if (state is ServerDrivenState.Loading) {
            progressBar.visibility = if (state.loading) View.VISIBLE else View.GONE
        } else if (state is ServerDrivenState.Error) {
            mFrame.visibility = View.GONE
            progressBar.visibility = View.GONE
            buttonRetry(state)
        }
    }

    override fun getFragmentTransitionAnimation() = FragmentTransitionAnimation(
        br.com.zup.beagle.R.anim.slide_from_right,
        br.com.zup.beagle.R.anim.none_animation,
        br.com.zup.beagle.R.anim.none_animation,
        br.com.zup.beagle.R.anim.slide_to_right
    )

    private fun buttonRetry(state: ServerDrivenState.Error){
        mButton.visibility = View.VISIBLE
        mButton.setOnClickListener {
                state.retry()
            mButton.visibility = View.GONE
            mFrame.visibility = View.VISIBLE
        }
    }
}