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
 * Configures the error messages returned by a service external to the application.
 * For example, when checking the registration status of a CPF in the recipe,
 * we can return the API error message to the application using FormValidation.
 *
 * @param errors list of errors.
 *
 */
data class FormValidation(
    val errors: List<FieldError>
) : Action

/**
 * class to define error.
 *
 * @param inputName component name to which this error refers.
 * @param message The error message displayed.
 *
 */
data class FieldError(
    val inputName: String,
    val message: String
)