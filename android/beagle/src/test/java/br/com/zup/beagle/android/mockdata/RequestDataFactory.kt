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

package br.com.zup.beagle.android.mockdata

import br.com.zup.beagle.android.data.serializer.makeButtonJson
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import br.com.zup.beagle.android.testutil.RandomData
import java.net.URI

fun makeResponseData() = ResponseData(
    statusCode = RandomData.int(),
    data = RandomData.string().toByteArray(),
    headers = mapOf("request-type" to "application/json")
)

fun makeRequestData() = RequestData(
    uri = URI(RandomData.string()),
    method =  HttpMethod.GET,
    headers = mapOf("Authorization" to RandomData.string()),
    body = makeButtonJson()
)