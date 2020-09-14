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
    const val DEPRECATED_PAGE_VIEW =
        "This constructor will be removed in a future version, use the constructor with Bind"
    const val DEPRECATED_TAB_VIEW = "This component will be removed in a future version, use TabBar instead."
    const val DEPRECATED_STATE_LOADING =
        "State loading will be removed in a future version, use Started and Finished."
    const val DEPRECATED_LOADING_VIEW =
     "This method will be removed in a future version, use the method with listener attribute of type ServerDrivenState"
    const val DEPRECATED_ON_STATE_CHANGED =
        "OnStateChanged will be removed in a future version, use OnServerStateChanged instead."
    const val DEPRECATED_BEAGLE_VIEW_STATE_CHANGED_LISTENER =
        "stateChangedListener will be removed in a future version, use serverStateChangedListener instead."
}

internal object NewIntentDeprecatedConstants {
    const val BEAGLE_ACTIVITY_COMPONENT = "Use @RegisterController with no arguments to register " +
        "your default BeagleActivity"

    const val DEPRECATED_NEW_INTENT = "To create a intent of your sub-class of BeagleActivity use " +
        "Context.newServerDrivenIntent instead."

    const val NEW_INTENT_NEW_IMPORT = "br.com.zup.beagle.android.utils.newServerDrivenIntent"
}

internal object HandleEventDeprecatedConstants {
    const val HANDLE_EVENT_DEPRECATED_MESSAGE: String =
        "Use handleEvent without eventName and eventValue or with ContextData for create a implicit context"
    const val HANDLE_EVENT_POINTER: String = "handleEvent(rootView, origin, action)"
    const val HANDLE_EVENT_ACTIONS_POINTER: String = "handleEvent(rootView, origin, actions)"
}