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

package br.com.zup.beagle.screens

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileDriver
import io.appium.java_client.MobileElement

import io.appium.java_client.pagefactory.AndroidFindBy
import io.appium.java_client.pagefactory.iOSXCUITFindBy

/**
 * Class that represents the main screen of both Android and iOS.
 * It is an implementation of the Page Object Model pattern.
 * Not all screens have its own class because they either have very few elements and interactions, or are used by
 * only one test.
 *
 */
@Suppress("UNCHECKED_CAST")
class MainScreen(mobileDriver: AppiumDriver<*>?) : AbstractScreen(mobileDriver) {

    @iOSXCUITFindBy(accessibility = "MainScreenLabel")
    @AndroidFindBy(id = "MainScreenLabel") // br.com.zup.beagle.automatedTests:id/MainScreenLabel
    public lateinit var labelElement: MobileElement

    @iOSXCUITFindBy(accessibility = "TextBffUrl")
    @AndroidFindBy(id = "TextBffUrl") // br.com.zup.beagle.automatedTests:id/TextBffUrl
    public lateinit var bffInputTextFieldElement: MobileElement

    @iOSXCUITFindBy(accessibility = "SendBffRequestButton")
    @AndroidFindBy(id = "SendBffRequestButton") // br.com.zup.beagle.automatedTests:id/SendBffRequestButton
    public lateinit var sendBffRequestButtonElement: MobileElement

    override fun <T : AbstractScreen> waitForScreenToLoad(): T {
        waitForScreenToLoad(arrayOf(labelElement, bffInputTextFieldElement, sendBffRequestButtonElement))
        return this as T
    }

    fun setBffUrl(url: String) {
        bffInputTextFieldElement.sendKeys(url)
    }

    fun clickOnGoButton() {
        sendBffRequestButtonElement.click()
    }

}