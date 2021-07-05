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

package br.com.zup.beagle.serialization.components.widgets

import br.com.zup.beagle.serialization.components.DefaultSerializerTest
import br.com.zup.beagle.serialization.components.makeButtonJson
import br.com.zup.beagle.serialization.components.makeContainerJson
import br.com.zup.beagle.serialization.components.makeFormJson
import br.com.zup.beagle.serialization.components.makeFormSubmitJson
import br.com.zup.beagle.serialization.components.makeImageWithLocalPathJson
import br.com.zup.beagle.serialization.components.makeImageWithRemotePathJson
import br.com.zup.beagle.serialization.components.makeJsonGridView
import br.com.zup.beagle.serialization.components.makeLazyComponentJson
import br.com.zup.beagle.serialization.components.makeListViewJson
import br.com.zup.beagle.serialization.components.makeObjectButton
import br.com.zup.beagle.serialization.components.makeObjectContainer
import br.com.zup.beagle.serialization.components.makeObjectForm
import br.com.zup.beagle.serialization.components.makeObjectFormSubmit
import br.com.zup.beagle.serialization.components.makeObjectGridView
import br.com.zup.beagle.serialization.components.makeObjectImageWithLocalPath
import br.com.zup.beagle.serialization.components.makeObjectImageWithRemotePath
import br.com.zup.beagle.serialization.components.makeObjectLazyComponent
import br.com.zup.beagle.serialization.components.makeObjectListView
import br.com.zup.beagle.serialization.components.makeObjectScreenComponent
import br.com.zup.beagle.serialization.components.makeObjectScrollView
import br.com.zup.beagle.serialization.components.makeObjectSimpleForm
import br.com.zup.beagle.serialization.components.makeObjectTabBar
import br.com.zup.beagle.serialization.components.makeObjectTabView
import br.com.zup.beagle.serialization.components.makeObjectText
import br.com.zup.beagle.serialization.components.makeObjectTextInput
import br.com.zup.beagle.serialization.components.makeObjectTextInputWithExpression
import br.com.zup.beagle.serialization.components.makeObjectTouchable
import br.com.zup.beagle.serialization.components.makeObjectWebView
import br.com.zup.beagle.serialization.components.makeObjectWebViewWithExpression
import br.com.zup.beagle.serialization.components.makePullToRefreshJson
import br.com.zup.beagle.serialization.components.makePullToRefreshObject
import br.com.zup.beagle.serialization.components.makePullToRefreshWithoutExpressionJson
import br.com.zup.beagle.serialization.components.makePullToRefreshWithoutExpressionObject
import br.com.zup.beagle.serialization.components.makeScreenComponentJson
import br.com.zup.beagle.serialization.components.makeScrollViewJson
import br.com.zup.beagle.serialization.components.makeSimpleFormJson
import br.com.zup.beagle.serialization.components.makeTabBarJson
import br.com.zup.beagle.serialization.components.makeTabViewJson
import br.com.zup.beagle.serialization.components.makeTextInputJson
import br.com.zup.beagle.serialization.components.makeTextInputWithExpressionJson
import br.com.zup.beagle.serialization.components.makeTextJson
import br.com.zup.beagle.serialization.components.makeTouchableJson
import br.com.zup.beagle.serialization.components.makeWebViewJson
import br.com.zup.beagle.serialization.components.makeWebViewWithExpressionJson
import br.com.zup.beagle.widget.Widget
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.provider.Arguments

@DisplayName("Given a Widget")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DefaultWidgetSerializerTest : DefaultSerializerTest<Widget>() {
    override fun testArguments() = listOf(
        Arguments.of(makeButtonJson(), makeObjectButton()),
        Arguments.of(makeContainerJson(), makeObjectContainer()),
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
}