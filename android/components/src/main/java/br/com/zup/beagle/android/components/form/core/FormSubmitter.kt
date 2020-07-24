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

package br.com.zup.beagle.android.components.form.core

import android.net.Uri
import br.com.zup.beagle.android.action.FormMethodType
import br.com.zup.beagle.android.action.FormRemoteAction
import br.com.zup.beagle.android.data.BeagleApi
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.factory.networking.urlbuilder.UrlBuilderFactory
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.android.setup.BeagleEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.URI
import kotlin.coroutines.CoroutineContext

@Deprecated(Constants.FORM_DEPRECATED_MESSAGE)
internal class FormSubmitter(
    private val beagleApi: BeagleApi = BeagleApi(),
    private val deserialization: BeagleSerializer = BeagleSerializer(),
    private val urlBuilder: UrlBuilder = UrlBuilderFactory().make(),
    private val job:Job = Job(),
    override val coroutineContext: CoroutineContext = job + Main
) : CoroutineScope {

    fun submitForm(
        form: FormRemoteAction,
        formsValue: Map<String, String>,
        result: (formResult: FormResult) -> Unit
    ) {
        launch {
            try {
                val requestData = createRequestData(form, formsValue)
                val response = beagleApi.fetchData(requestData)
                val action = deserialization.deserializeAction(String(response.data))
                result(FormResult.Success(action))
            } catch (e: BeagleException) {
                result(FormResult.Error(e))
            }
        }
    }

    private fun createRequestData(form: FormRemoteAction, formsValue: Map<String, String>): RequestData {
        val action = createUrl(form, formsValue)
        val newUrl = urlBuilder.format(BeagleEnvironment.beagleSdk.config.baseUrl, action)

        return RequestData(
            uri = URI(newUrl),
            method = when (form.method) {
                FormMethodType.POST -> HttpMethod.POST
                FormMethodType.GET -> HttpMethod.GET
                FormMethodType.PUT -> HttpMethod.PUT
                FormMethodType.DELETE -> HttpMethod.DELETE
            },
            body = createBody(form, formsValue)
        )
    }

    private fun createBody(form: FormRemoteAction, formsValue: Map<String, String>): String? {
        return if (form.method == FormMethodType.POST || form.method == FormMethodType.PUT) {
            formsValue.map {
                """ "${it.key}":"${it.value}" """.trim()
            }.toString().replace("[", "{").replace("]", "}")
        } else {
            null
        }
    }

    private fun createUrl(form: FormRemoteAction, formsValue: Map<String, String>)
        = if (form.method == FormMethodType.GET || form.method == FormMethodType.DELETE)
            formsValue.filterValues {
                isFormsValueValid(it)
            }.toList().fold(Uri.parse(form.path).buildUpon()) { path, param ->
                path.appendQueryParameter(param.first, param.second)
            }.build().toString()
        else form.path

    private fun isFormsValueValid(value: String?) = !value.isNullOrEmpty()
}
