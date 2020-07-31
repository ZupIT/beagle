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

package br.com.zup.beagle.android.builder.action

import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.builder.layout.ScreenWidgetBuilder
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.BeagleMapBuilder
import kotlin.properties.Delegates

fun route(block: RouteBuilder.() -> Unit) = RouteBuilder().apply(block).build()

class RouteBuilder {
    var route: Route by Delegates.notNull()

    fun remote(block: RouteRemoteBuilder.() -> Unit) {
        this.route = RouteRemoteBuilder().apply(block).build()
    }

    fun local(block: RouteLocalBuilder.() -> Unit) {
        this.route = RouteLocalBuilder().apply(block).build()
    }

    fun build() = route
}


class RouteRemoteBuilder: BeagleBuilder<Route.Remote> {

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

    fun fallback(block: ScreenWidgetBuilder.() -> Unit) {
        fallback(ScreenWidgetBuilder().apply(block).build())
    }

    override fun build() = Route.Remote(
        url = url,
        shouldPrefetch = shouldPrefetch,
        fallback = fallback
    )
}


class RouteLocalBuilder: BeagleBuilder<Route.Local> {
    var screen: Screen by Delegates.notNull()

    fun screen(screen: Screen) = this.apply { this.screen = screen }

    fun screen(block: ScreenWidgetBuilder.() -> Unit) {
        screen(ScreenWidgetBuilder().apply(block).build())
    }

    override fun build() = Route.Local(screen)

}

fun navigate(block: NavigateBuilder.() -> Unit) = NavigateBuilder().apply(block).build()

class NavigateBuilder {
    var navigate: Navigate by Delegates.notNull()

    fun navigate(navigate: Navigate) = this.apply { this.navigate = navigate }

    fun openExternalUrl(block: NavigateOpenExternalURLBuilder.() -> Unit) {
        navigate(NavigateOpenExternalURLBuilder().apply(block).build())
    }

    fun openNativeRoute(block: NavigateOpenNativeRouteBuilder.() -> Unit) {
        navigate(NavigateOpenNativeRouteBuilder().apply(block).build())
    }

    fun pushStack(block: NavigatePushStackBuilder.() -> Unit) {
        navigate(NavigatePushStackBuilder().apply(block).build())
    }

    fun popStack(block: NavigatePopStackBuilder.() -> Unit) {
        navigate(NavigatePopStackBuilder().apply(block).build())
    }

    fun pushView(block: NavigatePushViewBuilder.() -> Unit) {
        navigate(NavigatePushViewBuilder().apply(block).build())
    }

    fun popView(block: NavigatePopViewBuilder.() -> Unit) {
        navigate(NavigatePopViewBuilder().apply(block).build())
    }

    fun popToView(block: NavigatePopToViewBuilder.() -> Unit) {
        navigate(NavigatePopToViewBuilder().apply(block).build())
    }

    fun resetApplication(block: NavigateResetApplicationBuilder.() -> Unit) {
        navigate(NavigateResetApplicationBuilder().apply(block).build())
    }

    fun resetStack(block: NavigateResetStackBuilder.() -> Unit) {
        navigate(NavigateResetStackBuilder().apply(block).build())
    }

    fun build() = navigate

}

class NavigateOpenExternalURLBuilder : BeagleBuilder<Navigate.OpenExternalURL> {
    var url: String by Delegates.notNull()

    fun url(url: String) = this.apply { this.url = url }

    fun url(block: () -> String) {
        url(block.invoke())
    }

    override fun build() = Navigate.OpenExternalURL(url)

}

class NavigateOpenNativeRouteBuilder: BeagleBuilder<Navigate.OpenNativeRoute> {
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

    override fun build() = Navigate.OpenNativeRoute(
        route = route,
        shouldResetApplication = shouldResetApplication,
        data = data
    )

}

class NavigatePushStackBuilder: BeagleBuilder<Navigate.PushStack> {
    var route: Route by Delegates.notNull()

    fun route(route: Route) = this.apply { this.route = route }

    fun route(block: RouteBuilder.() -> Unit) {
        route(RouteBuilder().apply(block).build())
    }

    override fun build() = Navigate.PushStack(route)

}

class NavigatePopStackBuilder: BeagleBuilder<Navigate.PopStack> {
    override fun build() = Navigate.PopStack()
}


class NavigatePushViewBuilder: BeagleBuilder<Navigate.PushView> {
    var route: Route by Delegates.notNull()

    fun route(route: Route) = this.apply { this.route = route }

    fun route(block: RouteBuilder.() -> Unit) {
        route(RouteBuilder().apply(block).build())
    }

    override fun build() = Navigate.PushView(route)

}

class NavigatePopViewBuilder: BeagleBuilder<Navigate.PopView> {
    override fun build() = Navigate.PopView()
}


class NavigatePopToViewBuilder: BeagleBuilder<Navigate.PopToView> {
    var route: String by Delegates.notNull()

    fun route(route: String) = this.apply { this.route = route }

    fun route(block: () -> String) {
        route(block.invoke())
    }

    override fun build() = Navigate.PopToView(route)
}


class NavigateResetApplicationBuilder: BeagleBuilder<Navigate.ResetApplication> {
    var route: Route by Delegates.notNull()

    fun route(route: Route) = this.apply { this.route = route }

    fun route(block: RouteBuilder.() -> Unit) {
        route(RouteBuilder().apply(block).build())
    }

    override fun build() = Navigate.ResetApplication(route)

}

class NavigateResetStackBuilder : BeagleBuilder<Navigate.ResetStack> {
    var route: Route by Delegates.notNull()

    fun route(route: Route) = this.apply { this.route = route }

    fun route(block: RouteBuilder.() -> Unit) {
        route(RouteBuilder().apply(block).build())
    }

    override fun build() = Navigate.ResetStack(route)

}