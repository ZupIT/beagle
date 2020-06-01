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

package br.com.zup.beagle.data.serializer.adapter

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.data.serializer.PolymorphicJsonAdapterFactory
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormInputHidden
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.form.InputWidget
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Horizontal
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.layout.ScreenComponent
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.layout.Spacer
import br.com.zup.beagle.widget.layout.Stack
import br.com.zup.beagle.widget.layout.Vertical
import br.com.zup.beagle.widget.lazy.LazyComponent
import br.com.zup.beagle.widget.navigation.Touchable
import br.com.zup.beagle.widget.pager.PageIndicator
import br.com.zup.beagle.widget.pager.PageIndicatorComponent
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.NetworkImage
import br.com.zup.beagle.widget.ui.TabView
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.UndefinedWidget
import java.util.*

private const val BEAGLE_WIDGET_TYPE = "_beagleComponent_"
private const val BEAGLE_NAMESPACE = "beagle"

internal object ComponentJsonAdapterFactory {

    fun make(): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        var factory = PolymorphicJsonAdapterFactory.of(
            ServerDrivenComponent::class.java, BEAGLE_WIDGET_TYPE
        )

        factory = registerBaseSubTypes(factory)
        factory = registerLayoutClass(factory)
        factory = registerUIClass(factory)
        factory = registerCustomWidget(factory)
        factory = registerUndefinedWidget(factory)

        return factory
    }

    private fun registerBaseSubTypes(
        factory: PolymorphicJsonAdapterFactory<ServerDrivenComponent>
    ): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        return factory.withBaseSubType(PageIndicatorComponent::class.java)
            .withBaseSubType(InputWidget::class.java)
            .withBaseSubType(Widget::class.java)
    }

    private fun registerLayoutClass(
        factory: PolymorphicJsonAdapterFactory<ServerDrivenComponent>
    ): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        return factory.withSubtype(ScreenComponent::class.java, createNamespaceFor<ScreenComponent>())
            .withSubtype(Container::class.java, createNamespaceFor<Container>())
            .withSubtype(Vertical::class.java, createNamespaceFor<Vertical>())
            .withSubtype(Horizontal::class.java, createNamespaceFor<Horizontal>())
            .withSubtype(Stack::class.java, createNamespaceFor<Stack>())
            .withSubtype(Spacer::class.java, createNamespaceFor<Spacer>())
            .withSubtype(ScrollView::class.java, createNamespaceFor<ScrollView>())
            .withSubtype(LazyComponent::class.java, createNamespaceFor<LazyComponent>())
            .withSubtype(PageView::class.java, createNamespaceFor<PageView>())
            .withSubtype(Form::class.java, createNamespaceFor<Form>())
    }

    private fun registerUIClass(
        factory: PolymorphicJsonAdapterFactory<ServerDrivenComponent>
    ): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        return factory.withSubtype(Text::class.java, createNamespaceFor<Text>())
            .withSubtype(Image::class.java, createNamespaceFor<Image>())
            .withSubtype(NetworkImage::class.java, createNamespaceFor<NetworkImage>())
            .withSubtype(Button::class.java, createNamespaceFor<Button>())
            .withSubtype(ListView::class.java, createNamespaceFor<ListView>())
            .withSubtype(Touchable::class.java, createNamespaceFor<Touchable>())
            .withSubtype(TabView::class.java, createNamespaceFor<TabView>())
            .withSubtype(PageIndicator::class.java, createNamespaceFor<PageIndicator>())
            .withSubtype(FormInput::class.java, createNamespaceFor<FormInput>())
            .withSubtype(FormInputHidden::class.java, createNamespaceFor<FormInputHidden>())
            .withSubtype(FormSubmit::class.java, createNamespaceFor<FormSubmit>())
            .withSubtype(UndefinedWidget::class.java, createNamespaceFor<UndefinedWidget>())
    }

    private fun registerCustomWidget(
        factory: PolymorphicJsonAdapterFactory<ServerDrivenComponent>
    ): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        val appName = "custom"
        val widgets = BeagleEnvironment.beagleSdk.registeredWidgets()

        var newFactory = factory

        widgets.forEach {
            newFactory = newFactory.withSubtype(it, createNamespace(appName, it))
        }

        return newFactory
    }

    private fun registerUndefinedWidget(
        factory: PolymorphicJsonAdapterFactory<ServerDrivenComponent>
    ): PolymorphicJsonAdapterFactory<ServerDrivenComponent> {
        return factory.withDefaultValue(UndefinedWidget())
    }

    private inline fun <reified T : ServerDrivenComponent> createNamespaceFor(): String {
        return createNamespace(BEAGLE_NAMESPACE, T::class.java)
    }

    private fun createNamespace(appNamespace: String, clazz: Class<*>): String {
        return "$appNamespace:${clazz.simpleName.toLowerCase(Locale.getDefault())}"
    }
}