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

package br.com.zup.beagle.analytics

/**
 * Beagle analytics is used to track click events.
 */

@Deprecated("It was deprecated in version 1.10.0 and will be removed in a future version." +
    " Use the new analytics.")
interface Analytics {
    /**
     * sends the click event had in the view
     */
    @Deprecated("It was deprecated in version 1.10.0 and will be removed in a future version." +
        " Use the new analytics.")
    fun trackEventOnClick(event: ClickEvent)

    /**
     * sends the event when view appear
     */
    @Deprecated("It was deprecated in version 1.10.0 and will be removed in a future version." +
        " Use the new analytics.")
    fun trackEventOnScreenAppeared(event: ScreenEvent)

    /**
     * sends the event when view disappear
     */
    @Deprecated("It was deprecated in version 1.10.0 and will be removed in a future version." +
        " Use the new analytics.")
    fun trackEventOnScreenDisappeared(event: ScreenEvent)
}
