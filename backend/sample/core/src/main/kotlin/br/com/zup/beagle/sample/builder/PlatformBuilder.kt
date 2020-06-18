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

package br.com.zup.beagle.sample.builder

import br.com.zup.beagle.platform.BeaglePlatform
import br.com.zup.beagle.platform.forPlatform
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.Button

object PlatformBuilder : ScreenBuilder {
    override fun build() = Screen(
        child = Container(
            listOf(
                Button(text = "mobile only text").forPlatform(BeaglePlatform.MOBILE),
                Button(text = "web only text").forPlatform(BeaglePlatform.WEB),
                Button(text = "ios only text").forPlatform(BeaglePlatform.IOS),
                Button(text = "android only text").forPlatform(BeaglePlatform.ANDROID),
                Button(text = "text free for all")
            )
        )
    )
}
