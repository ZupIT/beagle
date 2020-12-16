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

package br.com.zup.beagle.android.utils

import android.content.Context
import android.content.Intent
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ScreenRequest

/**
 * Create a intent to start BeagleActivity's sub-classes.
 * @property screenJson that represents a Screen to be shown
 */
inline fun <reified T : BeagleActivity> Context.newServerDrivenIntent(screenJson: String): Intent {
    return Intent(this, T::class.java).putExtras(BeagleActivity.bundleOf(screenJson))
}

/**
 * Create a intent to start BeagleActivity's sub-classes.
 * @property screen to be shown
 */
inline fun <reified T : BeagleActivity> Context.newServerDrivenIntent(screen: Screen): Intent {
    return Intent(this, T::class.java).putExtras(BeagleActivity.bundleOf(screen))
}

/**
 * Create a intent to start BeagleActivity's sub-classes.
 * @property screenRequest to configure how the first screen request will be done
 */
inline fun <reified T : BeagleActivity> Context.newServerDrivenIntent(screenRequest: ScreenRequest): Intent {
    return Intent(this, T::class.java).putExtras(BeagleActivity.bundleOf(screenRequest))
}