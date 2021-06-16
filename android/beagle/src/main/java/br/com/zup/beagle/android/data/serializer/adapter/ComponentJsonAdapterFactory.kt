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

package br.com.zup.beagle.android.data.serializer.adapter

import br.com.zup.beagle.android.components.Touchable
import br.com.zup.beagle.android.components.form.FormInput
import br.com.zup.beagle.android.components.form.FormSubmit
import br.com.zup.beagle.android.components.form.InputWidget
import br.com.zup.beagle.android.components.page.PageIndicator
import br.com.zup.beagle.android.components.page.PageIndicatorComponent
import br.com.zup.beagle.android.components.page.PageView
import br.com.zup.beagle.android.data.serializer.PolymorphicJsonAdapterFactory
import br.com.zup.beagle.android.data.serializer.generateNameSpaceToDefaultWidget
import br.com.zup.beagle.android.data.serializer.generateNameSpaceToWidget
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.InternalWidgetFactory
import br.com.zup.beagle.android.widget.UndefinedWidget
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.Widget

private const val BEAGLE_WIDGET_TYPE = "_beagleComponent_"
private const val CUSTOM_NAMESPACE = "custom"

internal object ComponentJsonAdapterFactory {

    fun make(): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        var factory = PolymorphicJsonAdapterFactory.of(
            ServerDrivenComponent::class.java, BEAGLE_WIDGET_TYPE
        )

        factory = registerBaseSubTypes(factory)
        factory = registerUIClass(factory)
        factory = registerWidgets(factory, true, InternalWidgetFactory.registeredWidgets())
        factory = registerWidgets(factory, false, BeagleEnvironment.beagleSdk.registeredWidgets())
        factory = registerUndefinedWidget(factory)

        return factory
    }

    private fun registerBaseSubTypes(
        factory: PolymorphicJsonAdapterFactory<ServerDrivenComponent>,
    ): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        return factory.withBaseSubType(PageIndicatorComponent::class.java)
            .withBaseSubType(InputWidget::class.java)
            .withBaseSubType(Widget::class.java)
    }

    private fun registerUIClass(
        factory: PolymorphicJsonAdapterFactory<ServerDrivenComponent>,
    ): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        return factory
            .withSubtype(Touchable::class.java, createNameSpaceToDefaultWidget<Touchable>("touchable"))
            .withSubtype(FormInput::class.java, createNameSpaceToDefaultWidget<FormInput>("formInput"))
            .withSubtype(FormSubmit::class.java, createNameSpaceToDefaultWidget<FormSubmit>("formSubmit"))
            .withSubtype(PageView::class.java, createNameSpaceToDefaultWidget<PageView>("pageView"))
            .withSubtype(PageIndicator::class.java, createNameSpaceToDefaultWidget<PageIndicator>("pageIndicator"))
    }

    private fun registerWidgets(
        factory: PolymorphicJsonAdapterFactory<ServerDrivenComponent>,
        isDefault: Boolean,
        widgets: List<Class<WidgetView>>,
    ): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        var newFactory = factory

        widgets.forEach {
            val nameSpace = if (isDefault) generateNameSpaceToDefaultWidget(it)
            else generateNameSpaceToWidget(CUSTOM_NAMESPACE, it)

            newFactory = newFactory.withSubtype(it, nameSpace)
        }

        return newFactory
    }

    private fun registerUndefinedWidget(
        factory: PolymorphicJsonAdapterFactory<ServerDrivenComponent>,
    ): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        return factory.withDefaultValue(UndefinedWidget())
    }

    private inline fun <reified T : ServerDrivenComponent> createNameSpaceToDefaultWidget(name: String = ""): String {
        return generateNameSpaceToDefaultWidget(T::class.java, name)
    }
}