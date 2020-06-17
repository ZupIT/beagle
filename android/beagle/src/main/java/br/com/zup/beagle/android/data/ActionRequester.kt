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

package br.com.zup.beagle.android.data

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.exception.BeagleApiException
import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData

internal class ActionRequester(
    private val beagleApi: BeagleApi = BeagleApi(),
    private val serializer: BeagleSerializer = BeagleSerializer()
) {

    @Throws(BeagleException::class)
    suspend fun fetchAction(url: String): Action {
        val jsonResponse = String(beagleApi.fetchData(url.toRequestData()).data)
        return deserializeAction(jsonResponse)
    }

    @Throws(BeagleApiException::class)
    suspend fun fetchData(requestData: RequestData): ResponseData {
        return beagleApi.fetchData(requestData)
    }

    private fun deserializeAction(response: String): Action {
        return serializer.deserializeAction(response)
    }
}