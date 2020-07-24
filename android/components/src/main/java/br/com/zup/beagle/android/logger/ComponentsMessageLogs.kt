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

package br.com.zup.beagle.android.logger

object ComponentsMessageLogs  {
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

    fun logNotFoundSimpleForm() {
        BeagleLoggerProxy.error("Not found simple form in the parents")
    }

    fun errorWhileTryingToSetInvalidImage(image: String, ex: Exception) {
        val errorMessage = "Could not find image $image"
        BeagleLoggerProxy.error(errorMessage, ex)
    }
}