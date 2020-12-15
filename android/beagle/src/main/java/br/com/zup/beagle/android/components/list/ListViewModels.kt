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

package br.com.zup.beagle.android.components.list

import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.view.viewmodel.AsyncActionViewModel
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ListViewIdViewModel
import br.com.zup.beagle.android.view.viewmodel.OnInitViewModel
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView

internal data class ListViewModels(
    val rootView: RootView,
    val asyncActionViewModel: AsyncActionViewModel = rootView.generateViewModelInstance(),
    val contextViewModel: ScreenContextViewModel = rootView.generateViewModelInstance(),
    val listViewIdViewModel: ListViewIdViewModel = rootView.generateViewModelInstance(),
    val generateIdViewModel: GenerateIdViewModel = rootView.generateViewModelInstance(),
    val onInitViewModel: OnInitViewModel = rootView.generateViewModelInstance()
)
