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

package br.com.zup.beagle.action

import br.com.zup.beagle.core.CoreDeclarativeDsl
import br.com.zup.beagle.widget.layout.Screen

/**
 * This defines navigation type;
 *
 * @property OPEN_DEEP_LINK
 * @property ADD_VIEW
 * @property SWAP_VIEW
 * @property FINISH_VIEW
 * @property POP_VIEW
 * @property POP_TO_VIEW
 * @property PRESENT_VIEW
 */
enum class NavigationType {
    /**
     * This action opens the path to execute the action declared in the deeplink that was defined for the application.
     */
    OPEN_DEEP_LINK,

    /**
     * This type means the action to be performed is the opening
     * of a new screen using the link passed in the path attribute.
     * This screen will also be stacked at the top of the hierarchy of views in the application flow.
     */
    ADD_VIEW,

    /**
     * This attribute, when selected, opens a screen with the path informed
     * from a new flow and clears the stack of previously loaded screens.
     */
    SWAP_VIEW,

    /**
     * This action closes the current view.
     */
    FINISH_VIEW,

    /**
     * Attribute that adds the action of returning to the stack of added screens whether a dialog or not.
     */
    POP_VIEW,

    /**
     * It is responsible for returning the stack of screens in the application flow to a specific screen.
     */
    POP_TO_VIEW,

    /**
     * Present a new screen with the link declared in the path attribute.
     * This attribute basically has the same functionality as ADD_VIEW but starting a new flow instead.
     */
    PRESENT_VIEW
}

/**
 * class handles transition actions between screens in the application. Its structure is the following:.
 *
 * @param type The type of action that triggered the navigation.
 * @param shouldPrefetch tells Beagle if the navigation request should be previously loaded or not.
 * @param path Attribute that contains the navigation endpoint.
 * @param data pass information between screens.
 * @param screen This attribute has two distinct functions according to the path:
 *  <p><ul>
 *      li>If the Navigate object is built without a path, the screen
 *      will be rendered instead of making the service call.
 *      <li>If the path response fails, the screen acts as a fallback..
 *  </ul><p>
 *
 */
data class Navigate(
    val type: NavigationType,
    val shouldPrefetch: Boolean = false,
    val path: String? = null,
    val data: Map<String, String>? = null,
    val screen: Screen? = null
) : Action


fun navigate(block: NavigateBuilder.() -> Unit): Navigate = NavigateBuilder().apply(block).build()

@CoreDeclarativeDsl
class NavigateBuilder {

    var type: NavigationType = NavigationType.ADD_VIEW
    var shouldPrefetch: Boolean = false
    var path: String? = null
    var data: Map<String, String>? = null
    var screen: Screen? = null

    fun build(): Navigate = Navigate(type = type, shouldPrefetch = shouldPrefetch, path = path,
        data = data, screen = screen)

}