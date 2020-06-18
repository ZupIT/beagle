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

package br.com.zup.beagle.android.networking

import java.lang.Exception
import java.net.HttpURLConnection

internal fun HttpURLConnection.getSafeResponseCode(): Int? {
    return getMessageFormatted { this.responseCode }
}

internal fun HttpURLConnection.getSafeResponseMessage(): String? {
    return getMessageFormatted { this.responseMessage }
}

internal fun HttpURLConnection.getSafeError(): ByteArray? {
    return getMessageFormatted { this.errorStream.readBytes() }
}

internal typealias GetData<T> = () -> T

internal fun <T> getMessageFormatted(getData: GetData<T>): T? {
    return try {
        getData.invoke()
    } catch (exception: Exception) {
        null
    }
}