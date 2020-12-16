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

package br.com.zup.beagle.android.data.serializer

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.logger.BeagleMessageLogs

private const val EXCEPTION_MESSAGE = "Unexpected error when trying to serialize json="

internal class BeagleSerializer(
    private val beagleMoshiFactory: BeagleMoshi = BeagleMoshi
) {

    @Throws(BeagleException::class)
    fun serializeComponent(component: ServerDrivenComponent): String {
        try {
            return beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).toJson(component) ?:
            throw NullPointerException()
        } catch (ex: Exception) {
            val message = """
            Did you miss to serialize for Component ${component::class.java.simpleName}
        """.trimIndent()
           throw BeagleException("$EXCEPTION_MESSAGE$message")
        }
    }

    @Throws(BeagleException::class)
    fun deserializeComponent(json: String): ServerDrivenComponent {
        try {
            return beagleMoshiFactory.moshi.adapter(ServerDrivenComponent::class.java).fromJson(json) ?:
                throw NullPointerException()
        } catch (ex: Exception) {
            BeagleMessageLogs.logDeserializationError(json, ex)
            throw makeBeagleDeserializationException(json, ex.message)
        }
    }

    @Throws(BeagleException::class)
    fun deserializeAction(json: String): Action {
        try {
            return beagleMoshiFactory.moshi.adapter(Action::class.java).fromJson(json) ?:
                throw NullPointerException()
        } catch (ex: Exception) {
            BeagleMessageLogs.logDeserializationError(json, ex)
            throw makeBeagleDeserializationException(json, ex.message)
        }
    }

    private fun makeBeagleDeserializationException(json: String, exceptionMessage: String? = null): BeagleException {
        val message = if (exceptionMessage != null) {
            "\nWith exception message=$exceptionMessage"
        } else {
            ""
        }
        return BeagleException("$EXCEPTION_MESSAGE$json$message")
    }
}
