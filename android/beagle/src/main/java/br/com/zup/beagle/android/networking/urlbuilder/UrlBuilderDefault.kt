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

package br.com.zup.beagle.android.networking.urlbuilder

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal class UrlBuilderDefault : UrlBuilder {

    override fun format(endpoint: String?, path: String): String? {
        val newPath = encodeUrlBeforeCalls(path)
        return when {
            newPath.isEmpty() -> null
            endpoint.isNullOrEmpty() -> newPath
            endpoint?.takeLast(1) == "/" && newPath.take(1) == "/" -> endpoint + newPath.takeLast(newPath.length - 1)
            isRelativePath(newPath) -> endpoint + newPath
            else -> newPath
        }
    }

    private fun encodeUrlBeforeCalls(baseUrl: String): String {
        return URLEncoder.encode(baseUrl, StandardCharsets.UTF_8.name())
            .replace("+", "%20")
            .replace("%2F", "/")
            .replace("%25", "%")
            .replace("%2B", "+")
            .replace("%23", "#")
            .replace("*", "%2A")
            .replace("%7E", "~")
            .replace("%3A", ":")
            .replace("%3F", "?")
            .replace("%3D", "=")
            .replace("%26", "&")
    }

    private fun isRelativePath(path: String): Boolean {
        return path.startsWith("/")
    }
}