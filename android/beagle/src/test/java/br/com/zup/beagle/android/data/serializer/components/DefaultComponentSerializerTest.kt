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

package br.com.zup.beagle.android.data.serializer.components

import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.SafeArea
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.data.serializer.DefaultSerializerTest
import br.com.zup.beagle.android.data.serializer.makeButtonJson
import br.com.zup.beagle.android.data.serializer.makeContainerJson
import br.com.zup.beagle.android.data.serializer.makeCustomWidgetJson
import br.com.zup.beagle.android.data.serializer.makeFormJson
import br.com.zup.beagle.android.data.serializer.makeFormSubmitJson
import br.com.zup.beagle.android.data.serializer.makeImageWithLocalPathJson
import br.com.zup.beagle.android.data.serializer.makeImageWithRemotePathJson
import br.com.zup.beagle.android.data.serializer.makeJsonGridView
import br.com.zup.beagle.android.data.serializer.makeLazyComponentJson
import br.com.zup.beagle.android.data.serializer.makeListViewJson
import br.com.zup.beagle.android.data.serializer.makeObjectButton
import br.com.zup.beagle.android.data.serializer.makeObjectContainer
import br.com.zup.beagle.android.data.serializer.makeObjectContextWithPrimitiveValue
import br.com.zup.beagle.android.data.serializer.makeObjectCustomWidget
import br.com.zup.beagle.android.data.serializer.makeObjectForm
import br.com.zup.beagle.android.data.serializer.makeObjectFormSubmit
import br.com.zup.beagle.android.data.serializer.makeObjectGridView
import br.com.zup.beagle.android.data.serializer.makeObjectImageWithLocalPath
import br.com.zup.beagle.android.data.serializer.makeObjectImageWithRemotePath
import br.com.zup.beagle.android.data.serializer.makeObjectLazyComponent
import br.com.zup.beagle.android.data.serializer.makeObjectListView
import br.com.zup.beagle.android.data.serializer.makeObjectScrollView
import br.com.zup.beagle.android.data.serializer.makeObjectSimpleForm
import br.com.zup.beagle.android.data.serializer.makeObjectTabBar
import br.com.zup.beagle.android.data.serializer.makeObjectTabView
import br.com.zup.beagle.android.data.serializer.makeObjectText
import br.com.zup.beagle.android.data.serializer.makeObjectTextInput
import br.com.zup.beagle.android.data.serializer.makeObjectTextInputWithExpression
import br.com.zup.beagle.android.data.serializer.makeObjectTouchable
import br.com.zup.beagle.android.data.serializer.makeObjectWebView
import br.com.zup.beagle.android.data.serializer.makeObjectWebViewWithExpression
import br.com.zup.beagle.android.data.serializer.makePullToRefreshJson
import br.com.zup.beagle.android.data.serializer.makePullToRefreshObject
import br.com.zup.beagle.android.data.serializer.makePullToRefreshWithoutExpressionJson
import br.com.zup.beagle.android.data.serializer.makePullToRefreshWithoutExpressionObject
import br.com.zup.beagle.android.data.serializer.makeScreenComponentJson
import br.com.zup.beagle.android.data.serializer.makeScrollViewJson
import br.com.zup.beagle.android.data.serializer.makeSimpleFormJson
import br.com.zup.beagle.android.data.serializer.makeTabBarJson
import br.com.zup.beagle.android.data.serializer.makeTabViewJson
import br.com.zup.beagle.android.data.serializer.makeTextInputJson
import br.com.zup.beagle.android.data.serializer.makeTextInputWithExpressionJson
import br.com.zup.beagle.android.data.serializer.makeTextJson
import br.com.zup.beagle.android.data.serializer.makeTouchableJson
import br.com.zup.beagle.android.data.serializer.makeWebViewJson
import br.com.zup.beagle.android.data.serializer.makeWebViewWithExpressionJson
import br.com.zup.beagle.android.mockdata.CustomWidget
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.every
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.provider.Arguments

@Suppress("UNCHECKED_CAST")
private val WIDGETS = listOf(
    CustomWidget::class.java as Class<WidgetView>,
)

@DisplayName("Given a ServerDrivenComponent")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultComponentSerializerTest : DefaultSerializerTest<ServerDrivenComponent>(ServerDrivenComponent::class.java) {

    @BeforeAll
    override fun setUp() {
        super.setUp()
        every { beagleSdk.registeredWidgets() } returns WIDGETS
        moshi = BeagleMoshi.createMoshi()
    }

    override fun testArguments() = listOf(
        Arguments.of(makeButtonJson(), makeObjectButton()),
        Arguments.of(makeContainerJson(), makeObjectContainer()),
        Arguments.of(makeCustomWidgetJson(), makeObjectCustomWidget()),
        Arguments.of(makeFormJson(), makeObjectForm()),
        Arguments.of(makeFormSubmitJson(), makeObjectFormSubmit()),
        Arguments.of(makeJsonGridView(), makeObjectGridView()),
        Arguments.of(makeImageWithLocalPathJson(), makeObjectImageWithLocalPath()),
        Arguments.of(makeImageWithRemotePathJson(), makeObjectImageWithRemotePath()),
        Arguments.of(makeLazyComponentJson(), makeObjectLazyComponent()),
        Arguments.of(makeListViewJson(), makeObjectListView()),
        Arguments.of(makeScreenComponentJson(), makeObjectScreenComponent()),
        Arguments.of(makeScrollViewJson(), makeObjectScrollView()),
        Arguments.of(makeSimpleFormJson(), makeObjectSimpleForm()),
        Arguments.of(makeTabBarJson(), makeObjectTabBar()),
        Arguments.of(makeTabViewJson(), makeObjectTabView()),
        Arguments.of(makeTextInputJson(), makeObjectTextInput()),
        Arguments.of(makeTextInputWithExpressionJson(), makeObjectTextInputWithExpression()),
        Arguments.of(makeTextJson(), makeObjectText()),
        Arguments.of(makeTouchableJson(), makeObjectTouchable()),
        Arguments.of(makeWebViewJson(), makeObjectWebView()),
        Arguments.of(makeWebViewWithExpressionJson(), makeObjectWebViewWithExpression()),
        Arguments.of(makePullToRefreshJson(), makePullToRefreshObject()),
        Arguments.of(makePullToRefreshWithoutExpressionJson(), makePullToRefreshWithoutExpressionObject()),
    )

    private fun makeObjectScreenComponent() = ScreenComponent(
        identifier = "id",
        safeArea = SafeArea(top = true, leading = true, bottom = false, trailing = false),
        navigationBar = NavigationBar(
            title = "Screen Title",
            showBackButton = true,
        ),
        child = makeObjectContainer(),
        screenAnalyticsEvent = ScreenEvent("Test"),
        context = makeObjectContextWithPrimitiveValue()
    )
}