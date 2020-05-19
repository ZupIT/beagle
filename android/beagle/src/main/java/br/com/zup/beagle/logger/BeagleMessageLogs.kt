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

@file:Suppress("TooManyFunctions")

package br.com.zup.beagle.logger

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.networking.RequestData
import br.com.zup.beagle.networking.ResponseData

internal object BeagleMessageLogs {

    fun logHttpRequestData(requestData: RequestData) {
        BeagleLogger.info("""
            *** HTTP REQUEST ***
            Uri=${requestData.uri}
            Method=${requestData.method}
            Headers=${requestData.headers}
            Body=${requestData.body}
        """.trimIndent())
    }

    fun logHttpResponseData(responseData: ResponseData) {
        BeagleLogger.info("""
            *** HTTP RESPONSE ***
            StatusCode=${responseData.statusCode}
            Body=${String(responseData.data)}
            Headers=${responseData.headers}
        """.trimIndent())
    }

    fun logUnknownHttpError(throwable: Throwable) {
        BeagleLogger.error("Exception thrown while trying to call http client.", throwable)
    }

    fun logDeserializationError(json: String, ex: Exception) {
        val message = "Exception thrown while trying to deserialize the following json: $json"
        BeagleLogger.error(message, ex)
    }

    fun logViewFactoryNotFound(component: ServerDrivenComponent) {
        val message = """
            Did you miss to create a WidgetViewFactory for Widget ${component::class.java.simpleName}
        """.trimIndent()
        BeagleLogger.warning(message)
    }

    fun logActionBarAlreadyPresentOnView(ex: Exception) {
        BeagleLogger.error("SupportActionBar is already present", ex)
    }

    fun logFormValidatorNotFound(validator: String) {
        BeagleLogger.warning("Validation with name '$validator' were not found!")
    }

    fun logInvalidFormInputState(inputName: String) {
        BeagleLogger.warning("FormInput with name $inputName is not valid" +
                " and does not implement a ValidationErrorListener")
    }

    fun logFormInputsNotFound(formActionName: String) {
        BeagleLogger.warning("Are you missing to declare your FormInput for " +
                "form action '$formActionName'?")
    }

    fun logFormSubmitNotFound(formActionName: String) {
        BeagleLogger.warning("Are you missing to declare your FormSubmit component for " +
                "form action '$formActionName'?")
    }

    fun logDataNotInsertedOnDatabase(key: String, value: String) {
        BeagleLogger.warning("Error when trying to insert key=$key with value=$value on Beagle default database.")
    }
}