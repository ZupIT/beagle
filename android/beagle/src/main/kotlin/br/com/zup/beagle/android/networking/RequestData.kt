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

import android.os.Parcel
import android.os.Parcelable
import java.net.URI

/**
 * RequestData is used to do requests.
 *
 * @param uri Server URL.
 * @param method HTTP method.
 * @param headers Header items for the request.
 * @param body Content that will be delivered with the request.
 * @param url Server URL in string format.
 * @param httpAdditionalData pass additional data to the request.
 *
 */
@Suppress("DataClassPrivateConstructor")
data class RequestData private constructor(
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
    var url: String = "",
    val httpAdditionalData: HttpAdditionalData = HttpAdditionalData(),
) : Parcelable {

    constructor(
        url: String,
        httpAdditionalData: HttpAdditionalData = HttpAdditionalData(),
    ) : this(
        uri = URI(url),
        method = httpAdditionalData.method,
        headers = httpAdditionalData.headers,
        body = httpAdditionalData.body,
        url = url,
        httpAdditionalData = httpAdditionalData
    )

    @Deprecated(
        message = "It was deprecated in version 1.8.0 and will be removed in a future version. " +
            "Use the constructor with url and httpAdditionalData params instead.",
        replaceWith = ReplaceWith("RequestData(url = url, httpAdditionalData = httpAdditionalData)"),
    )
    constructor(
        uri: URI,
        method: HttpMethod = HttpMethod.GET,
        headers: Map<String, String> = mapOf(),
        body: String? = null,
    ) : this(
        uri = uri,
        method = method,
        headers = headers,
        body = body,
        url = uri.toString(),
        httpAdditionalData = HttpAdditionalData(method, headers, body)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(url)
        dest?.writeParcelable(httpAdditionalData, flags)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RequestData> = object : Parcelable.Creator<RequestData> {
            override fun createFromParcel(source: Parcel) =
                RequestData(
                    url = source.readString() ?: "",
                    httpAdditionalData = source.readParcelable(HttpAdditionalData::class.java.classLoader)
                        ?: HttpAdditionalData(),
                )

            override fun newArray(size: Int) = arrayOfNulls<RequestData?>(size)
        }
    }
}
