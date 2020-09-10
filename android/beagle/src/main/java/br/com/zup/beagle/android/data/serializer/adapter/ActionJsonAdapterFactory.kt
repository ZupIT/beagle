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

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.AddChildrenAction
import br.com.zup.beagle.android.action.Alert
import br.com.zup.beagle.android.action.Condition
import br.com.zup.beagle.android.action.Confirm
import br.com.zup.beagle.android.action.FormLocalAction
import br.com.zup.beagle.android.action.FormRemoteAction
import br.com.zup.beagle.android.action.FormValidation
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.SendRequest
import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.action.SubmitForm
import br.com.zup.beagle.android.action.UndefinedAction
import br.com.zup.beagle.android.data.serializer.PolymorphicJsonAdapterFactory
import java.util.Locale

private const val BEAGLE_NAMESPACE = "beagle"

@Deprecated(message = "This class will be no longer needed. @see new AndroidActionJsonAdapterFactory")
internal object ActionJsonAdapterFactory {

    fun make(factory: PolymorphicJsonAdapterFactory<Action>): PolymorphicJsonAdapterFactory<Action> {
        return factory
            .withDefaultValue(UndefinedAction())
            .withSubtype(UndefinedAction::class.java, createNamespaceFor<UndefinedAction>())
            .withSubtype(FormRemoteAction::class.java, createNamespaceFor<FormRemoteAction>())
            .withSubtype(FormLocalAction::class.java, createNamespaceFor<FormLocalAction>())
            .withSubtype(FormValidation::class.java, createNamespaceFor<FormValidation>())
            .withSubtype(Alert::class.java, createNamespaceFor<Alert>())
            .withSubtype(Confirm::class.java, createNamespaceFor<Confirm>())
            .withSubtype(Navigate.OpenExternalURL::class.java, createNamespaceFor<Navigate.OpenExternalURL>())
            .withSubtype(Navigate.OpenNativeRoute::class.java, createNamespaceFor<Navigate.OpenNativeRoute>())
            .withSubtype(Navigate.PushStack::class.java, createNamespaceFor<Navigate.PushStack>())
            .withSubtype(Navigate.PopStack::class.java, createNamespaceFor<Navigate.PopStack>())
            .withSubtype(Navigate.PushView::class.java, createNamespaceFor<Navigate.PushView>())
            .withSubtype(Navigate.PopView::class.java, createNamespaceFor<Navigate.PopView>())
            .withSubtype(Navigate.PopToView::class.java, createNamespaceFor<Navigate.PopToView>())
            .withSubtype(Navigate.ResetApplication::class.java, createNamespaceFor<Navigate.ResetApplication>())
            .withSubtype(Navigate.ResetStack::class.java, createNamespaceFor<Navigate.ResetStack>())
            .withSubtype(SendRequest::class.java, createNamespaceFor<SendRequest>())
            .withSubtype(SetContext::class.java, createNamespaceFor<SetContext>())
            .withSubtype(SubmitForm::class.java, createNamespaceFor<SubmitForm>())
            .withSubtype(AddChildrenAction::class.java, createNamespaceFor<AddChildrenAction>())
            .withSubtype(Condition::class.java, createNamespaceFor<Condition>())

    }

    private inline fun <reified T : Action> createNamespaceFor(): String {
        return "$BEAGLE_NAMESPACE:${T::class.java.simpleName.toLowerCase(Locale.getDefault())}"
    }
}
