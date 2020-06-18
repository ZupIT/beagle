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

data class ResponseData(
    val statusCode: Int?,
    val data: ByteArray,
    val headers: Map<String, String> = mapOf(),
    val statusText: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResponseData

        if (statusCode != other.statusCode) return false
        if (!data.contentEquals(other.data)) return false
        if (headers != other.headers) return false
        if (statusText != other.statusText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = statusCode ?: 0
        result = 31 * result + data.contentHashCode()
        result = 31 * result + headers.hashCode()
        result = 31 * result + (statusText?.hashCode() ?: 0)
        return result
    }
}