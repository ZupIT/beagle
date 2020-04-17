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

package br.com.zup.beagle.form

import br.com.zup.beagle.action.Action
import br.com.zup.beagle.data.serializer.BeagleSerializer
import br.com.zup.beagle.exception.BeagleException
import br.com.zup.beagle.networking.HttpClient
import br.com.zup.beagle.networking.HttpClientFactory
import br.com.zup.beagle.networking.HttpMethod
import br.com.zup.beagle.networking.RequestData
import br.com.zup.beagle.networking.urlbuilder.UrlBuilder
import br.com.zup.beagle.networking.urlbuilder.UrlBuilderFactory
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.widget.form.FormMethodType
import br.com.zup.beagle.widget.form.FormRemoteAction
import java.net.URI

internal sealed class FormResult {
    class Success(val action: Action) : FormResult()
    class Error(val throwable: Throwable) : FormResult()
}

internal class FormSubmitter(
    private val httpClient: HttpClient = HttpClientFactory().make(),
    private val deserialization: BeagleSerializer = BeagleSerializer(),
    private val urlBuilder: UrlBuilder = UrlBuilderFactory().make()
) {

    fun submitForm(
        form: FormRemoteAction,
        formsValue: Map<String, String>,
        result: (formResult: FormResult) -> Unit
    ) {
        val requestData = createRequestData(form, formsValue)

        httpClient.execute(requestData, { response ->
            try {
                val action = deserialization.deserializeAction(String(response.data))
                result(FormResult.Success(action))
            } catch (ex: BeagleException) {
                result(FormResult.Error(ex))
            }
        }, {
            result(FormResult.Error(it))
        })
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

    private fun createUrl(form: FormRemoteAction, formsValue: Map<String, String>): String {
        return if (form.method == FormMethodType.GET || form.method == FormMethodType.DELETE) {
            var query = if (formsValue.isNotEmpty()) {
                "?"
            } else {
                ""
            }

            for ((index, value) in formsValue.iterator().withIndex()) {
                query += "${value.key}=${value.value}"
                if (index < formsValue.size - 1) {
                    query += "&"
                }
            }

            "${form.path}$query"
        } else {
            form.path
        }
    }
}