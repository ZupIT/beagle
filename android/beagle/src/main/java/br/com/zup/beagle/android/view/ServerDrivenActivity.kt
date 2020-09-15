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

package br.com.zup.beagle.android.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import br.com.zup.beagle.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.beagle_activity_server_driven.*
import kotlinx.android.synthetic.main.beagle_include_error_server_driven.*

class ServerDrivenActivity : BeagleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.beagle_activity_server_driven)
    }

    override fun getServerDrivenContainerId(): Int = R.id.server_driven_container

    override fun getToolbar(): Toolbar = toolbar

    override fun onServerDrivenContainerStateChanged(state: ServerDrivenState) {
        progress_bar.visibility = if (state is ServerDrivenState.Started) View.VISIBLE else View.GONE
        handleError(state)
    }

    private fun handleError(state: ServerDrivenState) {
        if (state is ServerDrivenState.Error) {
            if (hasServerDrivenScreen()) {
                Snackbar.make(server_driven_container, R.string.beagle_label_generic_error, Snackbar.LENGTH_LONG)
                    .setAction(R.string.beagle_label_try_again) { state.retry() }
                    .show()
                return
            }
            buttonRetry.setOnClickListener {
                state.retry()
            }
            include_layout_error.visibility = View.VISIBLE
        } else {
            include_layout_error.visibility = View.GONE
        }
    }

    override fun getFragmentTransitionAnimation() = FragmentTransitionAnimation(
        R.anim.slide_from_right,
        R.anim.none_animation,
        R.anim.none_animation,
        R.anim.slide_to_right
    )
}