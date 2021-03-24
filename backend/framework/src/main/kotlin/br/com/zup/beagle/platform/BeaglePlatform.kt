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

package br.com.zup.beagle.platform

import br.com.zup.beagle.core.ServerDrivenComponent

/**
 *  The Platform is a helper to apply platform in your component
 *
 * @param self the component will apply platform
 *
 */
@Suppress("FunctionNaming")
fun <T : ServerDrivenComponent> Platform(platform: BeaglePlatform,
                                         self: T): BeaglePlatformWrapper<T> {
    return self.setPlatform(platform)
}

/**
 *
 *  The setPlatform method is a helper used to set the platform that will render the component
 *
 */
fun <T : ServerDrivenComponent> T.setPlatform(platform: BeaglePlatform) = BeaglePlatformWrapper(this, platform)

enum class BeaglePlatform {
    ALL,
    MOBILE,
    ANDROID,
    IOS,
    WEB;

    fun isMobilePlatform() = this == MOBILE || this == ANDROID || this == IOS

    fun allowToSendComponentToPlatform(beaglePlatform: BeaglePlatform) =
        when (this) {
            ALL -> true
            MOBILE -> beaglePlatform.isMobilePlatform()
            ANDROID -> beaglePlatform == ANDROID
            IOS -> beaglePlatform == IOS
            WEB -> beaglePlatform == WEB
        }
}
