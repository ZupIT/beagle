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
import br.com.zup.beagle.android.action.AddChildren
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
import br.com.zup.beagle.android.data.serializer.generateNameSpaceToDefaultAction

@Deprecated(message = "It was deprecated in version 1.0.0 and will be removed in a future version. " +
    "Use AndroidActionJsonAdapterFactory instead.",
    replaceWith = ReplaceWith("AndroidActionJsonAdapterFactory"))
internal object ActionJsonAdapterFactory {

    fun make(factory: PolymorphicJsonAdapterFactory<Action>): PolymorphicJsonAdapterFactory<Action> {
        return factory
            .withDefaultValue(UndefinedAction())
            .withSubtype(UndefinedAction::class.java, createNamespaceFor<UndefinedAction>("undefinedAction"))
            .withSubtype(FormRemoteAction::class.java, createNamespaceFor<FormRemoteAction>("formRemoteAction"))
            .withSubtype(FormLocalAction::class.java, createNamespaceFor<FormLocalAction>("formLocalAction"))
            .withSubtype(FormValidation::class.java, createNamespaceFor<FormValidation>("formValidation"))
            .withSubtype(Alert::class.java, createNamespaceFor<Alert>("alert"))
            .withSubtype(Confirm::class.java, createNamespaceFor<Confirm>("confirm"))
            .withSubtype(Navigate.OpenExternalURL::class.java,
                createNamespaceFor<Navigate.OpenExternalURL>("openExternalURL"))
            .withSubtype(Navigate.OpenNativeRoute::class.java,
                createNamespaceFor<Navigate.OpenNativeRoute>("openNativeRoute"))
            .withSubtype(Navigate.PushStack::class.java, createNamespaceFor<Navigate.PushStack>("pushStack"))
            .withSubtype(Navigate.PopStack::class.java, createNamespaceFor<Navigate.PopStack>("popStack"))
            .withSubtype(Navigate.PushView::class.java, createNamespaceFor<Navigate.PushView>("pushView"))
            .withSubtype(Navigate.PopView::class.java, createNamespaceFor<Navigate.PopView>("popView"))
            .withSubtype(Navigate.PopToView::class.java, createNamespaceFor<Navigate.PopToView>("popToView"))
            .withSubtype(Navigate.ResetApplication::class.java,
                createNamespaceFor<Navigate.ResetApplication>("resetApplication"))
            .withSubtype(Navigate.ResetStack::class.java, createNamespaceFor<Navigate.ResetStack>("resetStack"))
            .withSubtype(SendRequest::class.java, createNamespaceFor<SendRequest>("sendRequest"))
            .withSubtype(SetContext::class.java, createNamespaceFor<SetContext>("setContext"))
            .withSubtype(SubmitForm::class.java, createNamespaceFor<SubmitForm>("submitForm"))
            .withSubtype(AddChildren::class.java, createNamespaceFor<AddChildren>("addChildren"))
            .withSubtype(Condition::class.java, createNamespaceFor<Condition>("condition"))
    }

    private inline fun <reified T : Action> createNamespaceFor(name: String): String {
        return generateNameSpaceToDefaultAction(T::class.java, name)
    }
}
