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

import android.os.Parcelable
import br.com.zup.beagle.android.annotation.ContextDataValue
import br.com.zup.beagle.core.BeagleJson
import kotlinx.android.parcel.Parcelize
import java.net.URI

/**
 * RequestData is used to do requests.
 *
 * @param uri Server URL.
 * @param method HTTP method.
 * @param headers Header items for the request.
 * @param body Content that will be delivered with the request.
 * @param httpAdditionalData pass additional data to the request
 *
 */
@Parcelize
data class RequestData(
    @Deprecated(
        message = "It was deprecated in version 1.7.0 and will be removed in a future version. " +
            "Use field url.", replaceWith = ReplaceWith("url = ")
    )
    val uri: URI,
    @Deprecated(
        message = "It was deprecated in version 1.7.0 and will be removed in a future version. " +
            "Use field httpAdditionalData.", replaceWith = ReplaceWith("httpAdditionalData = ")
    )
    val method: HttpMethod = HttpMethod.GET,
    @Deprecated(
        message = "It was deprecated in version 1.7.0 and will be removed in a future version. " +
            "Use field httpAdditionalData.", replaceWith = ReplaceWith("httpAdditionalData = ")
    )
    val headers: Map<String, String> = mapOf(),
    @Deprecated(
        message = "It was deprecated in version 1.7.0 and will be removed in a future version. " +
            "Use field httpAdditionalData.", replaceWith = ReplaceWith("httpAdditionalData = ")
    )
    val body: String? = null,
    val url: String? = "",
    val httpAdditionalData: HttpAdditionalData = HttpAdditionalData(),
) : Parcelable