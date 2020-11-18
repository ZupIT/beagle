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

const val INVALID_WIDGET =
    """
        import br.com.zup.beagle.annotation.RegisterWidget

        @RegisterWidget
        class InvalidWidget { }
    """

const val INVALID_WIDGET_WITH_INHERITANCE =
    """
        import br.com.zup.beagle.annotation.RegisterWidget
        import br.com.zup.beagle.android.operation.Operation

        @RegisterWidget
        class InvalidWidget: Operation { }
    """

const val VALID_WIDGET_WITH_INHERITANCE_WIDGET_VIEW =
    """ 
        import br.com.zup.beagle.annotation.RegisterWidget
        import br.com.zup.beagle.android.components.form.InputWidget
        import br.com.zup.beagle.android.widget.WidgetView
        import br.com.zup.beagle.android.components.page.PageIndicatorComponent

        @RegisterWidget
        class TextTest: WidgetView() { }
    """

const val VALID_WIDGET_WITH_INHERITANCE_INPUT_WIDGET =
    """
        @RegisterWidget
        class InputWidgetTest: InputWidget() { }
    """

const val VALID_WIDGET_WITH_INHERITANCE_PAGE_INDICATOR =
    """
        @RegisterWidget
        class PageIndicatorTest: PageIndicatorComponent { }
    """

const val VALID_LIST_WIDGETS =
    VALID_WIDGET_WITH_INHERITANCE_WIDGET_VIEW +
        VALID_WIDGET_WITH_INHERITANCE_INPUT_WIDGET +
        VALID_WIDGET_WITH_INHERITANCE_PAGE_INDICATOR

const val INTERNAL_LIST_WIDGET_GENERATED_EXPECTED: String =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
        package br.com.test.beagle
        import br.com.zup.beagle.android.widget.WidgetView
        import java.lang.Class
        import kotlin.Suppress
        import kotlin.collections.List
        
        public final object RegisteredWidgets { 
            public fun registeredWidgets() : List<Class<WidgetView>> {
                val registeredWidgets = listOf<Class<WidgetView>>(
                    br.com.test.beagle.PageIndicatorTest::class.java as Class<WidgetView>,
                    br.com.test.beagle.TextTest::class.java as Class<WidgetView>,
                    br.com.test.beagle.InputWidgetTest::class.java as Class<WidgetView>,
                    )
                return registeredWidgets
            }
        }

    """

const val INTERNAL_SINGLE_WIDGET_GENERATED_EXPECTED: String =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
        package br.com.test.beagle
        import br.com.zup.beagle.android.widget.WidgetView
        import java.lang.Class
        import kotlin.Suppress
        import kotlin.collections.List
        
        public final object RegisteredWidgets {
            
            public fun registeredWidgets() : List<Class<WidgetView>> {
                val registeredWidgets = listOf<Class<WidgetView>>(
                    br.com.test.beagle.TextTest::class.java as Class<WidgetView>,
                )
                return registeredWidgets
            }
        }

    """