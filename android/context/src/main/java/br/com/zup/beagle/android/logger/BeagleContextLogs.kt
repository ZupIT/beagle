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

object BeagleContextLogs  {

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


    fun errorWhenExpressionEvaluateNullValue(value: String) {
        val errorMessage = "Could not found value for $value"
        BeagleLoggerProxy.error(errorMessage)
    }
}