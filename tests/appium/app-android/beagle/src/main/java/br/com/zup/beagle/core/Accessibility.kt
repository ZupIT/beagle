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

package br.com.zup.beagle.core

/**
 * The accessibility will enable a textual information to explain the view content in case a screen reader is used.
 *
 * @param accessible
 *                      that will inform when the accessibilityLabel is available.
 *                      By default is kept as true and it indicates that the view is an accessibility element.
 * @param accessibilityLabel
 *                      that will hold the textual information to be read by VoiceOver programs.
 *                      By enabling this, the VoiceOver will read this if a user selects this view,
 *                      them he will now where he is on the app.
 */
data class Accessibility(
    val accessible: Boolean = true,
    val accessibilityLabel: String? = null
)