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

package br.com.zup.beagle.android.action

import android.view.View
import br.com.zup.beagle.android.annotation.ContextDataValue
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.expressionOrValueOf
import br.com.zup.beagle.android.context.normalizeContextValue
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.view.custom.BeagleNavigator
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.BeagleJson
import br.com.zup.beagle.newanalytics.ActionAnalyticsConfig

/**
 * Class handles transition actions between screens in the application. Its structure is the following:.
 */

@BeagleJson(name = "navigate")
sealed class Navigate : AnalyticsAction {

    /**
     * Opens one of the browsers available on the device with the passed url.
     * @param url defined route to be shown.
     */
    @BeagleJson(name = "openExternalURL")
    data class OpenExternalURL(
        val url: Bind<String>,
        override var analytics: ActionAnalyticsConfig? = null,
    ) : Navigate() {

        constructor(
            url: String,
            analytics: ActionAnalyticsConfig? = null,
        ) : this(
            url = expressionOrValueOf(url),
            analytics = analytics
        )

        override fun execute(rootView: RootView, origin: View) {
            evaluateExpression(rootView, origin, url)?.let { url ->
                BeagleNavigator.openExternalURL(rootView.getContext(), url)
            }
        }
    }

    /**
     * This action opens the route to execute the action declared in the deeplink that was defined for the application.
     * @param route deeplink identifier
     * @param shouldResetApplication attribute that allows customization of the behavior of
     * restarting the application view stack.
     * @param data pass information between screens.
     */
    @BeagleJson(name = "openNativeRoute")
    class OpenNativeRoute(
        val route: Bind<String>,
        val shouldResetApplication: Boolean = false,
        val data: Map<String, String>? = null,
        override var analytics: ActionAnalyticsConfig? = null,
    ) : Navigate() {

        constructor(
            route: String,
            shouldResetApplication: Boolean = false,
            data: Map<String, String>? = null,
            analytics: ActionAnalyticsConfig? = null,
        ) : this(
            route = expressionOrValueOf(route),
            shouldResetApplication = shouldResetApplication,
            data = data,
            analytics = analytics
        )

        override fun execute(rootView: RootView, origin: View) {
            evaluateExpression(rootView, origin, route)?.let { route ->
                BeagleNavigator.openNativeRoute(rootView, route, data, shouldResetApplication)
            }
        }
    }

    /**
     * This action closes the current view stack.
     */
    @BeagleJson(name = "popStack")
    class PopStack(override var analytics: ActionAnalyticsConfig? = null) : Navigate() {
        override fun execute(rootView: RootView, origin: View) {
            BeagleNavigator.popStack(rootView.getContext())
        }
    }

    /**
     * Action that closes the current view.
     */
    @BeagleJson(name = "popView")
    class PopView(override var analytics: ActionAnalyticsConfig? = null) : Navigate() {
        override fun execute(rootView: RootView, origin: View) {
            BeagleNavigator.popView(rootView.getContext())
        }
    }

    /**
     * It is responsible for returning the stack of screens in the application flow to a specific screen.
     *
     * @param route route of a screen that it's on the pile.
     */
    @BeagleJson(name = "popToView")
    data class PopToView(
        val route: Bind<String>,
        override var analytics: ActionAnalyticsConfig? = null,
    ) : Navigate() {

        constructor(
            route: String,
            analytics: ActionAnalyticsConfig? = null,
        ) : this(
            route = expressionOrValueOf(route),
            analytics = analytics
        )

        override fun execute(rootView: RootView, origin: View) {
            evaluateExpression(rootView, origin, route)?.let { route ->
                BeagleNavigator.popToView(rootView.getContext(), route)
            }
        }
    }

    /**
     * This type means the action to be performed is the opening
     * of a new screen using the route passed.
     * This screen will also be stacked at the top of the hierarchy of views in the application flow.
     *
     * @param route this defines navigation type, it can be a navigation to a remote route in which Beagle will
     * deserialize the content or to a local screen already built.
     */
    @BeagleJson(name = "pushView")
    data class PushView(val route: Route, override var analytics: ActionAnalyticsConfig? = null) : Navigate() {
        override fun execute(rootView: RootView, origin: View) {
            BeagleNavigator.pushView(rootView.getContext(), route.getSafe(rootView, origin))
        }
    }

    /**
     * Present a new screen with the link declared in the route attribute.
     * This attribute basically has the same functionality as PushView but starting a new flow instead.
     *
     * @param route this defines navigation type, it can be a navigation to a remote route in which Beagle will
     * deserialize the content or to a local screen already built.
     * @param controllerId in this field passes the id created in the custom activity for beagle to create the flow,
     * if not the beagle passes default activity.
     */
    @BeagleJson(name = "pushStack")
    data class PushStack(
        val route: Route,
        val controllerId: String? = null,
        override var analytics: ActionAnalyticsConfig? = null,
    ) : Navigate() {
        override fun execute(rootView: RootView, origin: View) {
            BeagleNavigator.pushStack(rootView.getContext(), route.getSafe(rootView, origin), controllerId)
        }
    }

    /**
     * This attribute, when selected, opens a screen with the route informed
     * from a new flow and clears clears the view stack for the entire application.
     *
     * @param route this defines navigation type, it can be a navigation to a remote route in which Beagle will
     * deserialize the content or to a local screen already built.
     * @param controllerId in this field passes the id created in the custom activity for beagle to create the flow,
     * if not the beagle passes default activity.
     */
    @BeagleJson(name = "resetApplication")
    data class ResetApplication(
        val route: Route,
        val controllerId: String? = null,
        override var analytics: ActionAnalyticsConfig? = null,
    ) : Navigate() {
        override fun execute(rootView: RootView, origin: View) {
            BeagleNavigator.resetApplication(rootView.getContext(), route.getSafe(rootView, origin), controllerId)
        }
    }

    /**
     * This attribute, when selected, opens a screen with the route informed
     * from a new flow and clears the stack of previously loaded screens.
     *
     * @param route this defines navigation type, it can be a navigation to a remote route in which Beagle will
     * deserialize the content or to a local screen already built.
     * @param controllerId in this field passes the id created in the custom activity for beagle to create the flow,
     * if not the beagle passes default activity.
     */
    @BeagleJson(name = "resetStack")
    data class ResetStack(
        val route: Route,
        val controllerId: String? = null,
        override var analytics: ActionAnalyticsConfig? = null,
    ) : Navigate() {
        override fun execute(rootView: RootView, origin: View) {
            BeagleNavigator.resetStack(rootView.getContext(), route.getSafe(rootView, origin), controllerId)
        }
    }

    internal fun Route.getSafe(rootView: RootView, origin: View): Route {
        if (this is Route.Remote) {
            val newValue = evaluateExpression(rootView, origin, url)
            val body = httpAdditionalData?.body?.normalizeContextValue()
                ?.let { evaluateExpression(rootView, origin, it) }

            return this.copy(
                url = Bind.Value(newValue ?: ""),
                httpAdditionalData = httpAdditionalData?.copy(body = body),
            )
        }
        return this
    }
}

/**
 * This defines navigation type,
 * it can be a navigation to a remote route in which Beagle will deserialize the content
 * or to a local screen already built.
 */
@BeagleJson
sealed class Route {
    /**
     * Class that takes care of navigation to remote content.
     * @param url attribute that contains the navigation endpoint.
     * @param shouldPrefetch tells Beagle if the navigation request should be previously loaded or not.
     * @param fallback screen that is rendered in case the request fails.
     * @param httpAdditionalData additional parameters to request
     */
    @BeagleJson
    data class Remote constructor(
        val url: Bind<String>,
        val shouldPrefetch: Boolean? = null,
        val fallback: Screen? = null,
        val httpAdditionalData: HttpAdditionalData? = null,
    ) : Route() {

        constructor(
            url: String,
            shouldPrefetch: Boolean? = null,
            fallback: Screen? = null,
            httpAdditionalData: HttpAdditionalData? = null,
        ) : this(
            expressionOrValueOf(url),
            shouldPrefetch,
            fallback,
            httpAdditionalData
        )
    }

    /**
     * Class indicating navigation to a local screen.
     * @param screen screen to be rendered.
     */
    @BeagleJson
    data class Local(
        val screen: Screen,
    ) : Route()
}

/**
 * HttpAdditionalData is used to do requests.
 * @param method HTTP method.
 * @param headers Header items for the request.
 * @param body Content that will be delivered with the request.
 */
@BeagleJson
data class HttpAdditionalData(
    val method: HttpMethod? = HttpMethod.GET,
    val headers: Map<String, String>? = hashMapOf(),
    @ContextDataValue
    val body: Any? = null,
)
