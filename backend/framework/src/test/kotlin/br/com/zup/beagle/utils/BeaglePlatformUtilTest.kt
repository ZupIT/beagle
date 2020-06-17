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

package br.com.zup.beagle.utils

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.platform.BeaglePlatform
import br.com.zup.beagle.platform.BeaglePlatformUtil
import br.com.zup.beagle.platform.forPlatform
import br.com.zup.beagle.serialization.jackson.BeagleSerializationUtil
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class BeaglePlatformUtilTest {

    companion object {
        const val mobileText = "Mobile only Text"
        const val freeForAllText = "Free for all Text"
        const val webText = "Web only Text"
    }

    @Test
    fun treatBeaglePlatformWhenPlatformIsAndroid() {
        val jsonNode = createJsonData()
        val result = BeaglePlatformUtil.treatBeaglePlatform(BeaglePlatform.ANDROID.name, jsonNode).toPrettyString()
        assertTrue { result.contains(mobileText) }
        assertTrue { result.contains(freeForAllText) }
        assertFalse { result.contains(webText) }
    }

    @Test
    fun treatBeaglePlatformWhenPlatformIsIOS() {
        val jsonNode = createJsonData()
        val result = BeaglePlatformUtil.treatBeaglePlatform(BeaglePlatform.IOS.name, jsonNode).toPrettyString()
        assertTrue { result.contains(mobileText) }
        assertTrue { result.contains(freeForAllText) }
        assertFalse { result.contains(webText) }
    }

    @Test
    fun treatBeaglePlatformWhenPlatformIsMobile() {
        val jsonNode = createJsonData()
        val result = BeaglePlatformUtil.treatBeaglePlatform(BeaglePlatform.MOBILE.name, jsonNode).toPrettyString()
        assertTrue { result.contains(mobileText) }
        assertTrue { result.contains(freeForAllText) }
        assertFalse { result.contains(webText) }
    }

    @Test
    fun treatBeaglePlatformWhenPlatformIsWeb() {
        val jsonNode = createJsonData()
        val result = BeaglePlatformUtil.treatBeaglePlatform(BeaglePlatform.WEB.name, jsonNode).toPrettyString()
        assertFalse { result.contains(mobileText) }
        assertTrue { result.contains(freeForAllText) }
        assertTrue { result.contains(webText) }
    }

    @Test
    fun treatBeaglePlatformWhenPlatformIsAllOrAbsent() {
        val jsonNode = createJsonData()
        val resultAll = BeaglePlatformUtil.treatBeaglePlatform(BeaglePlatform.ALL.name, jsonNode).toPrettyString()
        assertTrue { resultAll.contains(mobileText) }
        assertTrue { resultAll.contains(freeForAllText) }
        assertTrue { resultAll.contains(webText) }

        val resultAbsent = BeaglePlatformUtil.treatBeaglePlatform(null, jsonNode).toPrettyString()
        assertTrue { resultAbsent.contains(mobileText) }
        assertTrue { resultAbsent.contains(freeForAllText) }
        assertTrue { resultAbsent.contains(webText) }
    }

    private fun createJsonData(): JsonNode {
        val data = Container(
            listOf(
                CustomButton(
                    text = mobileText
                ).forPlatform(BeaglePlatform.MOBILE),
                Button(
                    text = freeForAllText,
                    onPress = listOf(
                        Navigate.PushView(
                            Route.Local(
                                Screen(
                                    child = CustomButton(
                                        text = webText
                                    ).forPlatform(BeaglePlatform.WEB)
                                )
                            )
                        )
                    )
                )
            )
        )
        val objectMapper = BeagleSerializationUtil.beagleObjectMapper()
        return objectMapper.readTree(objectMapper.writeValueAsString(data))
    }

    class CustomButton(val text: String) : ServerDrivenComponent
}