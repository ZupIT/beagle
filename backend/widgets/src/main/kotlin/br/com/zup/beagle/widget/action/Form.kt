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

package br.com.zup.beagle.widget.action

import br.com.zup.beagle.analytics2.ActionAnalyticsConfig

/**
 *  Defines the type of operation submitted by this form. It will map these values to Http methods.
 *
 * @property GET
 * @property POST
 * @property PUT
 * @property DELETE
 *
 */
@Deprecated("use SimpleForm and SubmitForm instead")
enum class FormMethodType {
    /**
     * The GET method requests a representation of the specified resource.
     * Requests using GET should only retrieve data.
     *
     */
    GET,

    /**
     * The POST method is used to submit an entity to the specified resource,
     * often causing a change in state or side effects on the server.
     *
     */
    POST,

    /**
     * The PUT method replaces all current representations of the target resource with the request payload.
     *
     */
    PUT,

    /**
     * The DELETE method deletes the specified resource.
     *
     */
    DELETE
}

/**
 *  Define remote action, when you want to do some request when submit the form.
 *
 * @param path defines the URL path to the back-end service which will receive this form inputs.
 * @param method defines the type of operation submitted by this form. It will map these values to Http methods.
 *
 */
data class FormRemoteAction(
    val path: String,
    val method: FormMethodType,
    override var analytics: ActionAnalyticsConfig? = null
) : ActionAnalytics()

/**
 * Defines form local actions, that is, that do not make http requests,
 * such as an action that creates a customized Dialog.
 *
 * @param name define name of the action.
 * @param data sending data for the action.
 *
 * # Example: #
 * ```
 *
 *  val action = Action(name = "openPosterDetector", data = mapOf("key" to "value"))
 *
 * ```
 *
 */
@Deprecated("use SimpleForm and SubmitForm instead")
data class FormLocalAction(
    val name: String,
    val data: Map<String, String>,
    override var analytics: ActionAnalyticsConfig? = null
) : ActionAnalytics()

/**
 * Configures the error messages returned by a service external to the application.
 * For example, when checking the registration status of a CPF in the recipe,
 * we can return the API error message to the application using FormValidation.
 *
 * @param errors list of errors.
 *
 */
@Deprecated("use SimpleForm and SubmitForm instead")
data class FormValidation(
    val errors: List<FieldError>,
    override var analytics: ActionAnalyticsConfig? = null
) : ActionAnalytics()

/**
 * class to define error.
 *
 * @param inputName component name to which this error refers.
 * @param message The error message displayed.
 *
 */
@Deprecated("use SimpleForm and SubmitForm instead")
data class FieldError(
    val inputName: String,
    val message: String
)
