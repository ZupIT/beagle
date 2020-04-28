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

package br.com.zup.beagle.expression

import br.com.zup.beagle.core.DataBindingComponent
import br.com.zup.beagle.data.BeagleServiceWrapper
import br.com.zup.beagle.data.FetchDataListener
import br.com.zup.beagle.view.ScreenRequest

class ModelValueHelper(private val dataBindingComponent: DataBindingComponent,
                       private val beagleService: BeagleServiceWrapper = BeagleServiceWrapper(),
                       private val jsonParser: JsonParser = JsonParser()) {

    fun fetchModelValue(onSuccess: (Value) -> Unit,
                        onError: (Throwable) -> Unit) {
        try {
            if (dataBindingComponent.modelPath.isNullOrEmpty().not() &&
                dataBindingComponent.modelJson.isNullOrEmpty()) {
                beagleService.fetchData(
                    ScreenRequest(url = dataBindingComponent.modelPath ?: ""),
                    object : FetchDataListener {
                        override fun onSuccess(json: String) {
                            onSuccess(jsonParser.parseJsonToValue(json))
                        }

                        override fun onError(error: Throwable) {
                            onError(error)
                        }
                    })
            } else if (dataBindingComponent.modelJson.isNullOrEmpty().not()) {
                val data = jsonParser.parseJsonToValue(dataBindingComponent.modelJson ?: "")
                onSuccess(data)
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

}