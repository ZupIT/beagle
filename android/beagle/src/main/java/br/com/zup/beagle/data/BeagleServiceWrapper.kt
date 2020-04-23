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

package br.com.zup.beagle.data

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.data.serializer.BeagleSerializer
import br.com.zup.beagle.utils.CoroutineDispatchers
import br.com.zup.beagle.view.ScreenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface FetchListener {

    fun onSuccess(component: ServerDrivenComponent)
    fun onError(error: Throwable)
}

interface FetchDataListener {
    fun onSuccess(json: String)
    fun onError(error: Throwable)
}

class BeagleServiceWrapper {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private var beagleService = BeagleService()
    private var beagleSerialize = BeagleSerializer()

    internal fun init(service: BeagleService, serialize: BeagleSerializer) {
        beagleService = service
        beagleSerialize = serialize
    }

    fun fetchComponent(screenRequest: ScreenRequest, listener: FetchListener) {
        scope.launch {
            fetchComponentSuspend(listener, screenRequest)
        }
    }

    private suspend fun fetchComponentSuspend(
        listener: FetchListener,
        screenRequest: ScreenRequest
    ) {
        withContext(CoroutineDispatchers.Default) {
            try {
                val component = beagleService.fetchComponent(screenRequest)
                withContext(CoroutineDispatchers.Main) {
                    listener.onSuccess(component)
                }
            } catch (e: Throwable) {
                listener.onError(e)
            }
        }
    }

    fun serializeComponent(component: ServerDrivenComponent): String {
        return beagleSerialize.serializeComponent(component)
    }

    fun deserializeComponent(response: String): ServerDrivenComponent {
        return beagleSerialize.deserializeComponent(response)
    }

    fun fetchData(screenRequest: ScreenRequest, listener: FetchDataListener) {
        scope.launch {
            fetchDataSuspend(listener, screenRequest)
        }
    }

    private suspend fun fetchDataSuspend(
        listener: FetchDataListener,
        screenRequest: ScreenRequest
    ) {
        withContext(CoroutineDispatchers.Default) {
            try {
                val data = beagleService.fetchData(screenRequest)
                withContext(CoroutineDispatchers.Main) {
                    listener.onSuccess(data)
                }
            } catch (e: Throwable) {
                listener.onError(e)
            }
        }
    }
}
