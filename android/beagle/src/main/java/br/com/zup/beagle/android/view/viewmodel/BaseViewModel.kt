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

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

internal val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throw throwable
}

internal open class BaseViewModel : CoroutineScope, ViewModel() {
    private val job : Job = Job()
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
    private val coroutineExceptionHandler : CoroutineExceptionHandler = defaultExceptionHandler

    override val coroutineContext: CoroutineContext
        get() = job + dispatcher + coroutineExceptionHandler

}