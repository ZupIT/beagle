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

import br.com.zup.beagle.widget.builder.BeagleBuilder
import br.com.zup.beagle.widget.builder.BeagleMapBuilder
import br.com.zup.beagle.widget.layout.Screen
import kotlin.properties.Delegates


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
    data class Remote(val url: String, val shouldPrefetch: Boolean = false, val fallback: Screen? = null) : Route() {
        class Builder : BeagleBuilder<Remote> {

            var url: String by Delegates.notNull()
            var shouldPrefetch: Boolean = false
            var fallback: Screen? = null

            fun url(url: String) = this.apply { this.url = url }
            fun shouldPrefetch(shouldPrefetch: Boolean) = this.apply { this.shouldPrefetch = shouldPrefetch }
            fun fallback(fallback: Screen?) = this.apply { this.fallback = fallback }

            fun url(block: () -> String) {
                url(block.invoke())
            }

            fun shouldPrefetch(block: () -> Boolean) {
                shouldPrefetch(block.invoke())
            }

            fun fallback(block: Screen.Builder.() -> Unit) {
                fallback(Screen.Builder().apply(block).build())
            }

            override fun build() = Remote(
                url = url,
                shouldPrefetch = shouldPrefetch,
                fallback = fallback
            )
        }
    }

    /**
     * Class indicating navigation to a local screen.
     * @param screen screen to be rendered.
     */
    data class Local(val screen: Screen) : Route() {
        class Builder : BeagleBuilder<Local> {
            var screen: Screen by Delegates.notNull()

            fun screen(screen: Screen) = this.apply { this.screen = screen }

            fun screen(block: Screen.Builder.() -> Unit) {
                screen(Screen.Builder().apply(block).build())
            }

            override fun build() = Local(screen)

        }
    }
}

fun routeRemote(block: Route.Remote.Builder.() -> Unit) = Route.Remote.Builder().apply(block).build()

fun routeLocal(block: Route.Local.Builder.() -> Unit) = Route.Local.Builder().apply(block).build()

/**
 * Class handles transition actions between screens in the application. Its structure is the following:.
 */
sealed class Navigate : Action {

    /**
     * Opens one of the browsers available on the device with the passed url.
     * @param url defined route to be shown.
     */
    data class OpenExternalURL(val url: String) : Navigate() {
        class Builder : BeagleBuilder<OpenExternalURL> {
            var url: String by Delegates.notNull()

            fun url(url: String) = this.apply { this.url = url }

            fun url(block: () -> String) {
                url(block.invoke())
            }

            override fun build() = OpenExternalURL(url)

        }
    }

    /**
     * This action opens the route to execute the action declared in the deeplink that was defined for the application.
     * @param route deeplink identifier
     * @param shouldResetApplication attribute that allows customization of the behavior of
     * restarting the application view stack.
     * @param data pass information between screens.
     */
    class OpenNativeRoute(val route: String,
                          val shouldResetApplication: Boolean = false,
                          val data: Map<String, String>? = null) : Navigate() {
        class Builder : BeagleBuilder<OpenNativeRoute> {
            var route: String by Delegates.notNull()
            var shouldResetApplication: Boolean = false
            var data: MutableMap<String, String>? = null

            fun route(route: String) = this.apply { this.route = route }
            fun shouldResetApplication(shouldResetApplication: Boolean)
                = this.apply { this.shouldResetApplication = shouldResetApplication }
            fun data(data: MutableMap<String, String>?) = this.apply { this.data = data }

            fun route(block: () -> String) {
                route(block.invoke())
            }

            fun shouldResetApplication(block: () -> Boolean) {
                shouldResetApplication(block.invoke())
            }

            fun data(block: BeagleMapBuilder<String, String>.() -> Unit) {
                data(BeagleMapBuilder<String, String>().apply(block).buildNullable()?.toMutableMap())
            }

            override fun build() = OpenNativeRoute(
                route = route,
                shouldResetApplication = shouldResetApplication,
                data = data
            )

        }
    }

    /**
     * Present a new screen with the link declared in the route attribute.
     * This attribute basically has the same functionality as PushView but starting a new flow instead.
     */
    data class PushStack(val route: Route) : Navigate() {
        class Builder : BeagleBuilder<PushStack> {
            var route: Route by Delegates.notNull()

            fun route(route: Route) = this.apply { this.route = route }

            fun route(block: () -> Route) {
                route(block.invoke())
            }

            override fun build() = PushStack(route)

        }
    }

    /**
     * This action closes the current view stack.
     */
    class PopStack : Navigate() {
        class Builder : BeagleBuilder<PopStack> {
            override fun build() = PopStack()
        }
    }

    /**
     * This type means the action to be performed is the opening
     * of a new screen using the route passed.
     * This screen will also be stacked at the top of the hierarchy of views in the application flow.
     */
    data class PushView(val route: Route) : Navigate() {
        class Builder : BeagleBuilder<PushView> {
            var route: Route by Delegates.notNull()

            fun route(route: Route) = this.apply { this.route = route }

            fun route(block: () -> Route) {
                route(block.invoke())
            }

            override fun build() = PushView(route)

        }
    }

    /**
     * Action that closes the current view.
     */
    class PopView : Navigate() {
        class Builder : BeagleBuilder<PopView> {
            override fun build() = PopView()
        }
    }

    /**
     * It is responsible for returning the stack of screens in the application flow to a specific screen.
     */
    data class PopToView(val route: String) : Navigate() {
        class Builder : BeagleBuilder<PopToView> {
            var route: String by Delegates.notNull()

            fun route(route: String) = this.apply { this.route = route }

            fun route(block: () -> String) {
                route(block.invoke())
            }

            override fun build() = PopToView(route)
        }
    }

    /**
     * This attribute, when selected, opens a screen with the route informed
     * from a new flow and clears clears the view stack for the entire application.
     */
    data class ResetApplication(val route: Route) : Navigate() {
        class Builder : BeagleBuilder<ResetApplication> {
            var route: Route by Delegates.notNull()

            fun route(route: Route) = this.apply { this.route = route }

            fun route(block: () -> Route) {
                route(block.invoke())
            }

            override fun build() = ResetApplication(route)

        }
    }

    /**
     * This attribute, when selected, opens a screen with the route informed
     * from a new flow and clears the stack of previously loaded screens.
     */
    data class ResetStack(val route: Route) : Navigate() {
        class Builder : BeagleBuilder<ResetStack> {
            var route: Route by Delegates.notNull()

            fun route(route: Route) = this.apply { this.route = route }

            fun route(block: () -> Route) {
                route(block.invoke())
            }

            override fun build() = ResetStack(route)

        }
    }
}

fun openExternalUrl(block: Navigate.OpenExternalURL.Builder.() -> Unit)
    = Navigate.OpenExternalURL.Builder().apply(block).build()

fun openNativeRoute(block: Navigate.OpenNativeRoute.Builder.() -> Unit)
    = Navigate.OpenNativeRoute.Builder().apply(block).build()

fun pushStack(block: Navigate.PushStack.Builder.() -> Unit)
    = Navigate.PushStack.Builder().apply(block).build()

fun popStack(block: Navigate.PopStack.Builder.() -> Unit)
    = Navigate.PopStack.Builder().apply(block).build()

fun pushView(block: Navigate.PushView.Builder.() -> Unit)
    = Navigate.PushView.Builder().apply(block).build()

fun popView(block: Navigate.PopView.Builder.() -> Unit)
    = Navigate.PopView.Builder().apply(block).build()

fun popToView(block: Navigate.PopToView.Builder.() -> Unit)
    = Navigate.PopToView.Builder().apply(block).build()

fun resetApplication(block: Navigate.ResetApplication.Builder.() -> Unit)
    = Navigate.ResetApplication.Builder().apply(block).build()

fun resetStack(block: Navigate.ResetStack.Builder.() -> Unit)
    = Navigate.ResetStack.Builder().apply(block).build()