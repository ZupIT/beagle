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
package br.com.zup.beagle.widget.action

import br.com.zup.beagle.widget.layout.Screen

/**
 * This defines navigation type,
 * it can be a navigation to a remote route in which Beagle will deserialize the content
 * or to a local screen already built.
 */
sealed class Route {
    /**
     * Class that takes care of navigation to remote content.
     * @param url attribute that contains the navigation endpoint.
     * @param shouldPrefetch tells Beagle if the navigation request should be previously loaded or not.
     * @param fallback screen that is rendered in case the request fails.
     */
    data class Remote(val url: String, val shouldPrefetch: Boolean = false, val fallback: Screen? = null) : Route()


    /**
     * Class indicating navigation to a local screen.
     * @param screen screen to be rendered.
     */
    data class Local(val screen: Screen) : Route()

}

/**
 * Class handles transition actions between screens in the application. Its structure is the following:.
 */
sealed class Navigate : Action {

    /**
     * Opens one of the browsers available on the device with the passed url.
     * @param url defined route to be shown.
     */
    data class OpenExternalURL(val url: String) : Navigate()

    /**
     * This action opens the route to execute the action declared in the deeplink that was defined for the application.
     * @param route deeplink identifier
     * @param shouldResetApplication attribute that allows customization of the behavior of
     * restarting the application view stack.
     * @param data pass information between screens.
     */
    class OpenNativeRoute(val route: String,
                          val shouldResetApplication: Boolean = false,
                          val data: Map<String, String>? = null) : Navigate()

    /**
     * Present a new screen with the link declared in the route attribute.
     * This attribute basically has the same functionality as PushView but starting a new flow instead.
     */
    data class PushStack(val route: Route, val controllerId: String? = null) : Navigate()

    /**
     * This action closes the current view stack.
     */
    class PopStack : Navigate()

    /**
     * This type means the action to be performed is the opening
     * of a new screen using the route passed.
     * This screen will also be stacked at the top of the hierarchy of views in the application flow.
     */
    data class PushView(val route: Route) : Navigate()

    /**
     * Action that closes the current view.
     */
    class PopView : Navigate()

    /**
     * It is responsible for returning the stack of screens in the application flow to a specific screen.
     */
    data class PopToView(val route: String) : Navigate()

    /**
     * This attribute, when selected, opens a screen with the route informed
     * from a new flow and clears clears the view stack for the entire application.
     */
    data class ResetApplication(val route: Route, val controllerId: String? = null) : Navigate()

    /**
     * This attribute, when selected, opens a screen with the route informed
     * from a new flow and clears the stack of previously loaded screens.
     */
    data class ResetStack(val route: Route, val controllerId: String? = null) : Navigate()

}