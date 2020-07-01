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

package br.com.zup.beagle.android.view.viewmodel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

private val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throw throwable
}

class BeagleCoroutineScope(
    val job : Job,
    val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    val coroutineExceptionHandler : CoroutineExceptionHandler = defaultExceptionHandler
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = job + dispatcher + coroutineExceptionHandler

}