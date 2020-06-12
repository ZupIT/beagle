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

package br.com.zup.beagle.action

/**
 *  Define remote action, when you want to do some request when submit the form.
 *
 * @param path defines the URL path to the back-end service which will receive this form inputs.
 * @param method defines the type of operation submitted by this form. It will map these values to Http methods.
 *
 */
data class FormRemoteAction(
    val path: String,
    val method: FormMethodType
) : Action {
    override fun toString() = "FormRemoteAction: $path / ${method.name}"
}

/**
 *  Defines the type of operation submitted by this form. It will map these values to Http methods.
 *
 * @property GET
 * @property POST
 * @property PUT
 * @property DELETE
 *
 */
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