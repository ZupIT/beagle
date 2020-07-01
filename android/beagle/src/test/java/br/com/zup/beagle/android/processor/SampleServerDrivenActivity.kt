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

package br.com.zup.beagle.android.processor

import android.support.v7.widget.Toolbar
import br.com.zup.beagle.android.annotation.BeagleComponent
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState

@BeagleComponent
class SampleServerDrivenActivity : BeagleActivity() {

    override fun getServerDrivenContainerId(): Int = 1

    override fun getToolbar(): Toolbar = Toolbar(this)

    override fun onServerDrivenContainerStateChanged(state: ServerDrivenState) {
    }
}