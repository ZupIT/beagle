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

package br.com.zup.beagle.sample.spring.service

import br.com.zup.beagle.sample.builder.NavigateTypeBuilder
import br.com.zup.beagle.sample.compose.quality.ComposeNavigateTypeQuality
import br.com.zup.beagle.sample.compose.sample.ComposeSampleNavigateType
import org.springframework.stereotype.Service

@Service
class SampleNavigationTypeService {

    fun getScreenNavigateType(qaFlag: Boolean) = NavigateTypeBuilder(qaFlag)

    fun getScreenSte2(qaFlag: Boolean) =
        if (qaFlag) ComposeNavigateTypeQuality.step2() else ComposeSampleNavigateType.step2()

    fun getScreenPresentView(qaFlag: Boolean) =
        if (qaFlag) ComposeNavigateTypeQuality.presentView() else ComposeSampleNavigateType.presentView()

    fun getScreenStep3(qaFlag: Boolean) =
        if (qaFlag) ComposeNavigateTypeQuality.step3() else ComposeSampleNavigateType.step2()
}
