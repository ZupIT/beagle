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

package br.com.zup.beagle.android.compiler.mocks

const val INVALID_ACTION =
    """
        import br.com.zup.beagle.annotation.RegisterAction

        @RegisterAction
        class InvalidAction { }
    """

const val INVALID_ACTION_WITH_INHERITANCE =
    """
        import br.com.zup.beagle.annotation.RegisterAction
        import br.com.zup.beagle.android.operation.Operation

        @RegisterAction
        class InvalidAction: Operation { }
    """

const val VALID_ACTION =
    """ 
        import br.com.zup.beagle.annotation.RegisterAction
        import br.com.zup.beagle.android.action.Action

        @RegisterAction
        class ActionTest: Action { }
    """

const val VALID_ACTION_WITH_INHERITANCE_ASYNC_ACTION =
    """
        abstract class AsyncAction: Action { }
        
        @RegisterAction
        class AsyncActionTest: AsyncAction() { }
    """

const val VALID_LIST_ACTION = VALID_ACTION + VALID_ACTION_WITH_INHERITANCE_ASYNC_ACTION

const val INTERNAL_LIST_ACTION_GENERATED_EXPECTED: String =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
        package br.com.test.beagle
        import br.com.zup.beagle.android.action.Action
        import java.lang.Class
        import kotlin.Suppress
        import kotlin.collections.List
        
        public final object RegisteredActions { 
            public fun registeredActions() : List<Class<Action>> {
                val registeredActions = listOf<Class<Action>>(
                    br.com.test.beagle.AsyncActionTest::class.java as Class<Action>,
                    br.com.test.beagle.ActionTest::class.java as Class<Action>,
                )
                return registeredActions
            }
        }

    """

const val INTERNAL_SINGLE_ACTION_GENERATED_EXPECTED: String =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
        package br.com.test.beagle
        import br.com.zup.beagle.android.action.Action
        import java.lang.Class
        import kotlin.Suppress
        import kotlin.collections.List
        
        public final object RegisteredActions {
            
            public fun registeredActions() : List<Class<Action>> {
                val registeredActions = listOf<Class<Action>>(
                    br.com.test.beagle.ActionTest::class.java as Class<Action>,
                )
                return registeredActions
            }
        }

    """