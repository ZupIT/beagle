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

package br.com.zup.beagle.android.data.serializer.actions

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.data.serializer.DefaultSerializerTest
import br.com.zup.beagle.android.data.serializer.makeActionAddChildrenJson
import br.com.zup.beagle.android.data.serializer.makeActionAddChildrenObject
import br.com.zup.beagle.android.data.serializer.makeActionAlertJson
import br.com.zup.beagle.android.data.serializer.makeActionAlertObject
import br.com.zup.beagle.android.data.serializer.makeActionConditionJson
import br.com.zup.beagle.android.data.serializer.makeActionConditionObject
import br.com.zup.beagle.android.data.serializer.makeActionConfirmJson
import br.com.zup.beagle.android.data.serializer.makeActionConfirmObject
import br.com.zup.beagle.android.data.serializer.makeActionCustomActionJson
import br.com.zup.beagle.android.data.serializer.makeActionCustomActionObject
import br.com.zup.beagle.android.data.serializer.makeActionFormLocalActionJson
import br.com.zup.beagle.android.data.serializer.makeActionFormLocalActionObject
import br.com.zup.beagle.android.data.serializer.makeActionFormRemoteActionJson
import br.com.zup.beagle.android.data.serializer.makeActionFormRemoteActionObject
import br.com.zup.beagle.android.data.serializer.makeActionOpenExternalURLJson
import br.com.zup.beagle.android.data.serializer.makeActionOpenExternalURLObject
import br.com.zup.beagle.android.data.serializer.makeActionOpenExternalURLWithExpressionJson
import br.com.zup.beagle.android.data.serializer.makeActionOpenExternalURLWithExpressionObject
import br.com.zup.beagle.android.data.serializer.makeActionPopToViewJson
import br.com.zup.beagle.android.data.serializer.makeActionPopToViewObject
import br.com.zup.beagle.android.data.serializer.makeActionPopToViewWithExpressionJson
import br.com.zup.beagle.android.data.serializer.makeActionPopToViewWithExpressionObject
import br.com.zup.beagle.android.data.serializer.makeActionPushStackJson
import br.com.zup.beagle.android.data.serializer.makeActionPushStackObject
import br.com.zup.beagle.android.data.serializer.makeActionPushStackWithExpressionJson
import br.com.zup.beagle.android.data.serializer.makeActionPushStackWithExpressionObject
import br.com.zup.beagle.android.data.serializer.makeActionPushStackWithHardcodedUrlJson
import br.com.zup.beagle.android.data.serializer.makeActionPushStackWithHardcodedUrlObject
import br.com.zup.beagle.android.data.serializer.makeActionPushViewJson
import br.com.zup.beagle.android.data.serializer.makeActionPushViewObject
import br.com.zup.beagle.android.data.serializer.makeActionPushViewWithExpressionJson
import br.com.zup.beagle.android.data.serializer.makeActionPushViewWithExpressionObject
import br.com.zup.beagle.android.data.serializer.makeActionPushViewWithHardcodedUrlJson
import br.com.zup.beagle.android.data.serializer.makeActionPushViewWithHardcodedUrlObject
import br.com.zup.beagle.android.data.serializer.makeActionResetApplicationJson
import br.com.zup.beagle.android.data.serializer.makeActionResetApplicationObject
import br.com.zup.beagle.android.data.serializer.makeActionResetApplicationWithExpressionJson
import br.com.zup.beagle.android.data.serializer.makeActionResetApplicationWithExpressionObject
import br.com.zup.beagle.android.data.serializer.makeActionResetApplicationWithHardcodedUrlJson
import br.com.zup.beagle.android.data.serializer.makeActionResetApplicationWithHardcodedUrlObject
import br.com.zup.beagle.android.data.serializer.makeActionResetStackJson
import br.com.zup.beagle.android.data.serializer.makeActionResetStackObject
import br.com.zup.beagle.android.data.serializer.makeActionSetContextJson
import br.com.zup.beagle.android.data.serializer.makeActionSetContextObject
import br.com.zup.beagle.android.data.serializer.makeResetStackWithExpressionJson
import br.com.zup.beagle.android.data.serializer.makeResetStackWithExpressionObject
import br.com.zup.beagle.android.data.serializer.makeResetStackWithHardcodedUrlJson
import br.com.zup.beagle.android.data.serializer.makeResetStackWithHardcodedUrlObject
import br.com.zup.beagle.android.mockdata.CustomAndroidAction
import io.mockk.every
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.provider.Arguments

@Suppress("UNCHECKED_CAST")
private val ACTIONS = listOf(
    CustomAndroidAction::class.java as Class<Action>
)

@DisplayName("Given an Action")
class DefaultActionSerializerTest : DefaultSerializerTest<Action>(Action::class.java) {

    @BeforeAll
    override fun setUp() {
        super.setUp()
        every { beagleSdk.registeredActions() } returns ACTIONS
        moshi = BeagleMoshi.createMoshi()
    }

    override fun testArguments() = listOf<Arguments>(
        Arguments.of(makeActionAddChildrenJson(), makeActionAddChildrenObject()),
        Arguments.of(makeActionAlertJson(), makeActionAlertObject()),
        Arguments.of(makeActionConditionJson(), makeActionConditionObject()),
        Arguments.of(makeActionConfirmJson(), makeActionConfirmObject()),
        Arguments.of(makeActionCustomActionJson(), makeActionCustomActionObject()),
        Arguments.of(makeActionFormLocalActionJson(), makeActionFormLocalActionObject()),
        Arguments.of(makeActionFormRemoteActionJson(), makeActionFormRemoteActionObject()),
        Arguments.of(makeActionOpenExternalURLJson(), makeActionOpenExternalURLObject()),
        Arguments.of(makeActionOpenExternalURLWithExpressionJson(), makeActionOpenExternalURLWithExpressionObject()),
        Arguments.of(makeActionPopToViewJson(), makeActionPopToViewObject()),
        Arguments.of(makeActionPopToViewWithExpressionJson(), makeActionPopToViewWithExpressionObject()),
        Arguments.of(makeActionPushStackJson(), makeActionPushStackObject()),
        Arguments.of(makeActionPushStackWithExpressionJson(), makeActionPushStackWithExpressionObject()),
        Arguments.of(makeActionPushStackWithHardcodedUrlJson(), makeActionPushStackWithHardcodedUrlObject()),
        Arguments.of(makeActionPushViewJson(), makeActionPushViewObject()),
        Arguments.of(makeActionPushViewWithExpressionJson(), makeActionPushViewWithExpressionObject()),
        Arguments.of(makeActionPushViewWithHardcodedUrlJson(), makeActionPushViewWithHardcodedUrlObject()),
        Arguments.of(makeActionResetApplicationJson(), makeActionResetApplicationObject()),
        Arguments.of(makeActionResetApplicationWithExpressionJson(), makeActionResetApplicationWithExpressionObject()),
        Arguments.of(
            makeActionResetApplicationWithHardcodedUrlJson(),
            makeActionResetApplicationWithHardcodedUrlObject()
        ),
        Arguments.of(makeActionResetStackJson(), makeActionResetStackObject()),
        Arguments.of(makeResetStackWithExpressionJson(), makeResetStackWithExpressionObject()),
        Arguments.of(makeResetStackWithHardcodedUrlJson(), makeResetStackWithHardcodedUrlObject()),
        Arguments.of(makeActionSetContextJson(), makeActionSetContextObject()),
    )
}