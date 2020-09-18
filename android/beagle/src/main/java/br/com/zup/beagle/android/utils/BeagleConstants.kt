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

package br.com.zup.beagle.android.utils

internal object BeagleRegex {
    val EXPRESSION_REGEX = "(\\\\*)@\\{(([^'\\}]|('([^'\\\\]|\\\\.)*'))*)\\}".toRegex()
    val FULL_MATCH_EXPRESSION_SEPARATOR_REGEX = "(?<=\\})".toRegex()
    val QUANTITY_OF_SLASHES_REGEX = "(\\\\*)@".toRegex()
}

internal object DeprecationMessages {
    const val DEPRECATED_STATE_LOADING =
        "It was deprecated in version 1.2.0 and will be removed in a future version. Use Started and Finished instead."
    const val DEPRECATED_LOADING_VIEW =
        "This method was deprecated in version 1.2.0 and will be removed in a future version. Use the method with" +
            " listener attribute of type ServerDrivenState instead."
    const val DEPRECATED_ON_STATE_CHANGED =
        "It was deprecated in version 1.2.0 and will be removed in a future version. Use OnServerStateChanged instead."
    const val DEPRECATED_BEAGLE_VIEW_STATE_CHANGED_LISTENER =
        "It was deprecated in version 1.2.0 and will be removed in a future version. Use serverStateChangedListener" +
            " instead."
}

internal object NewIntentDeprecatedConstants {
    const val BEAGLE_ACTIVITY_COMPONENT = "It was deprecated in version 1.2.0 and will be removed in a future " +
        "version. Use @RegisterController with no arguments to register your default BeagleActivity."

    const val DEPRECATED_NEW_INTENT = "It was deprecated in version 1.2.0 and will be removed in a future " +
        "version. To create a intent of your sub-class of BeagleActivity use Context.newServerDrivenIntent instead."

    const val NEW_INTENT_NEW_IMPORT = "br.com.zup.beagle.android.utils.newServerDrivenIntent"
}

internal object HandleEventDeprecatedConstants {
    const val HANDLE_EVENT_DEPRECATED_MESSAGE: String =
        "It was deprecated in version 1.1.0 and will be removed in a future version. Use handleEvent without " +
            "eventName and eventValue or with ContextData for create a implicit context."
    const val HANDLE_EVENT_POINTER: String = "handleEvent(rootView, origin, action)"
    const val HANDLE_EVENT_ACTIONS_POINTER: String = "handleEvent(rootView, origin, actions)"
}

internal object CacheDeprecatedConstants {
    const val MEMORY_MAXIMUM_CAPACITY =
        "It was deprecated in version 1.2.2 and will be removed in a future version. Use size instead."

    const val MEMORY_MAXIMUM_CAPACITY_REPLACE = "size"

    const val CONSTRUCTOR =
        "It was deprecated in version 1.2.2 and will be removed in a future version."

    const val CONSTRUCTOR_REPLACE =
        "Cache(enabled, maxAge, size=your_cache_size)"
}

internal object PageViewDeprecatedConstants {
    const val CONSTRUCTOR_WITH_PAGE_INDICATOR =
        "This constructor was deprecated in version 1.1.0 and will be removed in a future version."

    const val CONSTRUCTOR_WITH_PAGE_INDICATOR_REPLACE =
        "PageView(children, context, onPageChange=null, currentPage=null)"

    const val PAGE_INDICATOR_COMPONENT =
        "This interface was deprecated in version 1.1.0 and will be removed in a future version."

    const val PAGE_INDICATOR_PROPERTY =
        "This property was deprecated in version 1.1.0 and will be removed in a future version."
}

internal object TabViewDeprecatedConstants {
    const val TAB_ITEM = "It was deprecated in version 1.1.0 and will be removed in a future version. " +
        "Use TabBarItem instead."

    const val TAB_ITEM_REPLACE = "TabBarItem(title, icon)"

    const val TAB_VIEW = "It was deprecated in version 1.1.0 and will be removed in a future version. " +
        "Use TabBar instead."

    const val TAB_VIEW_REPLACE = "TabBar(items=children, styleId=styleId, currentTab=null, onTabSelection=null)"
}

internal object ActionJsonAdapterFactoryDeprecatedConstants {
    const val MESSAGE = "It was deprecated in version 1.0.0 and will be removed in a future version. " +
        "Use AndroidActionJsonAdapterFactory instead."

    const val REPLACE = "AndroidActionJsonAdapterFactory"
}

internal object EventsRelatedToNavigationAction {
    const val URL: String = "url"
    const val SHOULD_PREFETCH = "shouldPrefetch"
    const val FALLBACK: String = "fallback"
    const val SCREEN: String = "screen"
}