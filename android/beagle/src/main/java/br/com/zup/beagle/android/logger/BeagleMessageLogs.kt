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

package br.com.zup.beagle.android.logger

import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import br.com.zup.beagle.core.ServerDrivenComponent

internal object BeagleMessageLogs {

    fun logHttpRequestData(requestData: RequestData) {
        BeagleLoggerProxy.info("""
            *** HTTP REQUEST ***
            Uri=${requestData.uri}
            Method=${requestData.method}
            Headers=${requestData.headers}
            Body=${requestData.body}
        """.trimIndent())
    }

    fun logHttpResponseData(responseData: ResponseData) {
        BeagleLoggerProxy.info("""
            *** HTTP RESPONSE ***
            StatusCode=${responseData.statusCode}
            Body=${String(responseData.data)}
            Headers=${responseData.headers}
        """.trimIndent())
    }

    fun logUnknownHttpError(throwable: Throwable) {
        BeagleLoggerProxy.error("Exception thrown while trying to call http client.", throwable)
    }

    fun logDeserializationError(json: String, ex: Exception) {
        val message = "Exception thrown while trying to deserialize the following json: $json"
        BeagleLoggerProxy.error(message, ex)
    }

    fun logViewFactoryNotFound(component: ServerDrivenComponent) {
        val message = """
            Did you miss to create a WidgetViewFactory for Widget ${component::class.java.simpleName}
        """.trimIndent()
        BeagleLoggerProxy.warning(message)
    }

    fun logActionBarAlreadyPresentOnView(ex: Exception) {
        BeagleLoggerProxy.error("SupportActionBar is already present", ex)
    }

    fun logFormValidatorNotFound(validator: String) {
        BeagleLoggerProxy.warning("Validation with name '$validator' were not found!")
    }

    fun logFormInputsNotFound(formActionName: String) {
        BeagleLoggerProxy.warning("Are you missing to declare your FormInput for " +
            "form action '$formActionName'?")
    }

    fun logFormSubmitNotFound(formActionName: String) {
        BeagleLoggerProxy.warning("Are you missing to declare your FormSubmit component for " +
            "form action '$formActionName'?")
    }

    fun logDataNotInsertedOnDatabase(key: String, value: String) {
        BeagleLoggerProxy.warning("Error when trying to insert key=$key with value=$value on Beagle default database.")
    }

    fun errorWhileTryingToAccessContext(ex: Throwable) {
        val errorMessage = "Error while evaluating expression bindings."
        BeagleLoggerProxy.error(errorMessage, ex)
    }

    fun errorWhileTryingToChangeContext(ex: Throwable) {
        val errorMessage = "Error while trying to change context."
        BeagleLoggerProxy.error(errorMessage, ex)
    }

    fun errorWhileTryingToNotifyContextChanges(ex: Throwable) {
        val errorMessage = "Error while trying to notify context changes."
        BeagleLoggerProxy.error(errorMessage, ex)
    }

    fun errorWhileTryingToEvaluateBinding(ex: Throwable) {
        val errorMessage = "Error while trying to evaluate binding."
        BeagleLoggerProxy.error(errorMessage, ex)
    }

    fun multipleExpressionsInValueThatIsNotString() {
        val errorMessage = "You are trying to use multiple expressions in a type that is not string!"
        BeagleLoggerProxy.warning(errorMessage)
    }

    fun errorWhenMalformedColorIsProvided(color: String, ex: Exception) {
        val errorMessage = "Could not parses color $color"
        BeagleLoggerProxy.error(errorMessage, ex)
    }

    fun errorWhenExpressionEvaluateNullValue(value: String) {
        val errorMessage = "Could not found value for $value"
        BeagleLoggerProxy.error(errorMessage)
    }

    fun logNotFoundSimpleForm() {
        BeagleLoggerProxy.error("Not found simple form in the parents")
    }

    fun errorWhileTryingToSetInvalidImage(image: String, ex: Exception) {
        val errorMessage = "Could not find image $image"
        BeagleLoggerProxy.error(errorMessage, ex)
    }

    fun globalKeywordIsReservedForGlobalContext() {
        val errorMessage = "Context name global is a reserved keyword for Global Context only"
        BeagleLoggerProxy.warning(errorMessage)
    }

    fun errorWhileTryingParseExpressionFunction(expression: String, ex: Exception) {
        val errorMessage = "Error while trying to parse expression: $expression"
        BeagleLoggerProxy.error(errorMessage, ex)
    }

    fun errorWhileTryingExecuteExpressionFunction(ex: Exception) {
        val errorMessage = "Error while trying to execute expression function."
        BeagleLoggerProxy.error(errorMessage, ex)
    }

    fun functionWithNameDoesNotExist(functionName: String) {
        val errorMessage = "Function with named $functionName does not exist."
        BeagleLoggerProxy.warning(errorMessage)
    }

    fun somethingHappenGenerateId(ex: Exception) {
        BeagleLoggerProxy.error("Something Happen when generate id", ex)
    }

    fun errorWhileTryingToAddViewWithAddChildrenAction(id : String){
        val errorMessage = "The view with id:$id cannot receive children"
        BeagleLoggerProxy.error(errorMessage)
    }

    fun toolbarNotSupportExpressionInIcon(expression: String){
        val errorMessage = "Expression: $expression not supported in toolbar"
        BeagleLoggerProxy.error(errorMessage)
    }
}
