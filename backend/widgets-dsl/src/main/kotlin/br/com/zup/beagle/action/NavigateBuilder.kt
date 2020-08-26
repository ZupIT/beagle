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

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.BeagleMapBuilder
import br.com.zup.beagle.layout.ScreenComponentBuilder
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.layout.Screen
import kotlin.properties.Delegates

fun routeRemote(block: RouteRemoteBuilder.() -> Unit) = RouteRemoteBuilder().apply(block).build()
fun routeLocal(block: RouteLocalBuilder.() -> Unit) = RouteLocalBuilder().apply(block).build()

interface RouteBuilderHelper {
    var route: Route

    fun route(route: Route) = this.apply { this.route = route }

    fun route(block: () -> Route) {
        route(block.invoke())
    }

    fun routeLocal(block: RouteLocalBuilder.() -> Unit) {
        route(RouteLocalBuilder().apply(block).build())
    }

    fun routeRemote(block: RouteRemoteBuilder.() -> Unit) {
        route(RouteRemoteBuilder().apply(block).build())
    }
}

class RouteRemoteBuilder : BeagleBuilder<Route.Remote> {

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

    fun fallback(block: ScreenComponentBuilder.() -> Unit) {
        fallback(ScreenComponentBuilder().apply(block).build())
    }

    override fun build() = Route.Remote(
        url = url,
        shouldPrefetch = shouldPrefetch,
        fallback = fallback
    )
}


class RouteLocalBuilder : BeagleBuilder<Route.Local> {
    var screen: Screen by Delegates.notNull()

    fun screen(screen: Screen) = this.apply { this.screen = screen }

    fun screen(block: ScreenComponentBuilder.() -> Unit) {
        screen(ScreenComponentBuilder().apply(block).build())
    }

    override fun build() = Route.Local(screen)

}

fun navigateOpenExternalUrl(block: NavigateOpenExternalURLBuilder.() -> Unit) =
    NavigateOpenExternalURLBuilder().apply(block).build()

fun navigateOpenNativeRoute(block: NavigateOpenNativeRouteBuilder.() -> Unit) =
    NavigateOpenNativeRouteBuilder().apply(block).build()

fun navigatePushStack(block: NavigatePushStackBuilder.() -> Unit) = NavigatePushStackBuilder().apply(block).build()

fun navigatePopStack(block: NavigatePopStackBuilder.() -> Unit) = NavigatePopStackBuilder().apply(block).build()

fun navigatePushView(block: NavigatePushViewBuilder.() -> Unit) = NavigatePushViewBuilder().apply(block).build()

fun navigatePopView(block: NavigatePopViewBuilder.() -> Unit) = NavigatePopViewBuilder().apply(block).build()

fun navigatePopToView(block: NavigatePopToViewBuilder.() -> Unit) = NavigatePopToViewBuilder().apply(block).build()

fun navigateResetApplication(block: NavigateResetApplicationBuilder.() -> Unit) =
    NavigateResetApplicationBuilder().apply(block).build()

fun navigateResetStack(block: NavigateResetStackBuilder.() -> Unit) =
    NavigateResetStackBuilder().apply(block).build()

interface NavigateBuilderHelper {
    var navigate: Navigate

    fun navigate(navigate: Navigate) = this.apply { this.navigate = navigate }

    fun navigate(block: () -> Navigate) {
        navigate(block.invoke())
    }

    fun navigateOpenExternalUrl(block: NavigateOpenExternalURLBuilder.() -> Unit) {
        navigate(NavigateOpenExternalURLBuilder().apply(block).build())
    }

    fun navigateOpenNativeRoute(block: NavigateOpenNativeRouteBuilder.() -> Unit) {
        navigate(NavigateOpenNativeRouteBuilder().apply(block).build())
    }

    fun navigatePushStack(block: NavigatePushStackBuilder.() -> Unit) {
        navigate(NavigatePushStackBuilder().apply(block).build())
    }

    fun navigatePopStack(block: NavigatePopStackBuilder.() -> Unit) {
        navigate(NavigatePopStackBuilder().apply(block).build())
    }

    fun navigatePushView(block: NavigatePushViewBuilder.() -> Unit) {
        navigate(NavigatePushViewBuilder().apply(block).build())
    }

    fun navigatePopView(block: NavigatePopViewBuilder.() -> Unit) {
        navigate(NavigatePopViewBuilder().apply(block).build())
    }

    fun navigatePopToView(block: NavigatePopToViewBuilder.() -> Unit) {
        navigate(NavigatePopToViewBuilder().apply(block).build())
    }

    fun navigateResetApplication(block: NavigateResetApplicationBuilder.() -> Unit) {
        navigate(NavigateResetApplicationBuilder().apply(block).build())
    }

    fun navigateResetStack(block: NavigateResetStackBuilder.() -> Unit) {
        navigate(NavigateResetStackBuilder().apply(block).build())
    }
}

class NavigateOpenExternalURLBuilder : BeagleBuilder<Navigate.OpenExternalURL> {
    var url: String by Delegates.notNull()

    fun url(url: String) = this.apply { this.url = url }

    fun url(block: () -> String) {
        url(block.invoke())
    }

    override fun build() = Navigate.OpenExternalURL(url)

}

class NavigateOpenNativeRouteBuilder : BeagleBuilder<Navigate.OpenNativeRoute> {
    var route: String by Delegates.notNull()
    var shouldResetApplication: Boolean = false
    var data: MutableMap<String, String>? = null

    fun route(route: String) = this.apply { this.route = route }
    fun shouldResetApplication(shouldResetApplication: Boolean) =
        this.apply { this.shouldResetApplication = shouldResetApplication }

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

    override fun build() = Navigate.OpenNativeRoute(
        route = route,
        shouldResetApplication = shouldResetApplication,
        data = data
    )

}

class NavigatePushStackBuilder : BeagleBuilder<Navigate.PushStack>, RouteBuilderHelper {
    override var route: Route by Delegates.notNull()
    var controllerId: String? = null

    fun controllerId(controllerId: String?) = this.apply { this.controllerId = controllerId }

    override fun build() = Navigate.PushStack(route, controllerId)

}

class NavigatePopStackBuilder : BeagleBuilder<Navigate.PopStack> {
    override fun build() = Navigate.PopStack()
}


class NavigatePushViewBuilder : BeagleBuilder<Navigate.PushView>, RouteBuilderHelper {
    override var route: Route by Delegates.notNull()

    override fun build() = Navigate.PushView(route)

}

class NavigatePopViewBuilder : BeagleBuilder<Navigate.PopView> {
    override fun build() = Navigate.PopView()
}


class NavigatePopToViewBuilder : BeagleBuilder<Navigate.PopToView> {
    var route: String by Delegates.notNull()

    fun route(route: String) = this.apply { this.route = route }

    fun route(block: () -> String) {
        route(block.invoke())
    }

    override fun build() = Navigate.PopToView(route)
}


class NavigateResetApplicationBuilder : BeagleBuilder<Navigate.ResetApplication>, RouteBuilderHelper {
    override var route: Route by Delegates.notNull()
    var controllerId: String? = null

    fun controllerId(controllerId: String?) = this.apply { this.controllerId = controllerId }

    override fun build() = Navigate.ResetApplication(route, controllerId)

}

class NavigateResetStackBuilder : BeagleBuilder<Navigate.ResetStack>, RouteBuilderHelper {
    override var route: Route by Delegates.notNull()
    var controllerId: String? = null

    fun controllerId(controllerId: String?) = this.apply { this.controllerId = controllerId }

    override fun build() = Navigate.ResetStack(route, controllerId)

}