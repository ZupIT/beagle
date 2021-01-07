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

import br.com.zup.beagle.setup.DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
import br.com.zup.beagle.setup.DEFAULT_SCREEN_WAIT_TIME_IN_MILL
import br.com.zup.beagle.utils.AppiumUtil
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.pagefactory.AppiumFieldDecorator
import org.openqa.selenium.By
import org.openqa.selenium.support.PageFactory
import java.time.Duration


abstract class AbstractScreen(mobileDriver: AppiumDriver<*>?) {

    protected var driver: AppiumDriver<*>? = mobileDriver

    init {
        PageFactory.initElements(
            AppiumFieldDecorator(
                driver,
                Duration.ofMillis(DEFAULT_ELEMENT_WAIT_TIME_IN_MILL)
            ), this
        )
        waitForScreenToLoad<AbstractScreen>()
    }

    /**
     * Implement here the conditions to guarantee a screen is loaded, eg. wait for the visibility of key screen elements
     * @param <T>
     * @return
    </T> */
    abstract fun <T : AbstractScreen> waitForScreenToLoad(): T

    /**
     * @param elementArray
     */
    protected open fun waitForScreenToLoad(elementArray: Array<MobileElement>) {
        AppiumUtil.waitForElementsToBeClickable(
            driver!!,
            elementArray,
            DEFAULT_SCREEN_WAIT_TIME_IN_MILL
        )
    }

}