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
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import br.com.zup.beagle.annotation.BeagleComponent
import br.com.zup.beagle.sample.R
import br.com.zup.beagle.view.BeagleActivity
import br.com.zup.beagle.view.FragmentTransitionAnimation
import br.com.zup.beagle.view.ServerDrivenState
import com.google.android.material.snackbar.Snackbar

@BeagleComponent
class SampleServerDrivenActivity : BeagleActivity() {

    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val mToolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }

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
            progressBar.visibility = View.GONE
            Snackbar.make(findViewById(android.R.id.content), "Error", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun getFragmentTransitionAnimation() = FragmentTransitionAnimation(
        br.com.zup.beagle.R.anim.slide_from_right,
        br.com.zup.beagle.R.anim.none_animation,
        br.com.zup.beagle.R.anim.none_animation,
        br.com.zup.beagle.R.anim.slide_to_right
    )
}