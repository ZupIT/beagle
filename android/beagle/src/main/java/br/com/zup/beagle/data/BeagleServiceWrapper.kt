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
import br.com.zup.beagle.view.ScreenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface FetchListener {

    fun onSuccess(component: ServerDrivenComponent)
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
            try {
                listener.onSuccess(beagleService.fetchComponent(screenRequest))
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
}
