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

package br.com.zup.beagle.setup

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.MobileCapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import java.net.URL

object SuiteSetup {

    const val ERROR_SCREENSHOTS_FOLDER = "./build/screenshots"
    const val SCREENSHOTS_DATABASE_FOLDER = "./src/test/resources/screenshots_database"
    private var platform: String? = null
    private var driver: AppiumDriver<*>? = null

    //private var service: AppiumDriverLocalService? = null
    private var bffBaseUrl: String? = null


    fun getDriver(): AppiumDriver<*> {

        if (driver == null)
            throw Exception("Test suite not initialized correctly!")

        return driver!!
    }

    fun isAndroid(): Boolean {
        return platform.equals("android", ignoreCase = true)
    }

    fun isIos(): Boolean {
        return platform.equals("ios", ignoreCase = true)
    }

    fun getBffBaseUrl(): String {
        return bffBaseUrl!!
    }

    fun initSuit() {

        //startAppiumServer()

        if (platform == null) {
            platform = System.getProperty("platform") // command-line argument
        }

        println("#### Initializing suite setup with platform $platform...")

        if (bffBaseUrl == null) {
            if (isAndroid())
                bffBaseUrl = "http://10.0.2.2:8080"
            else
                bffBaseUrl = "http://localhost:8080"
        }

        val capabilities = DesiredCapabilities()

        if (isAndroid()) {

            /**
             * Reset strategies
             * http://appium.io/docs/en/writing-running-appium/other/reset-strategies/index.html
             */
            capabilities.setCapability("noReset", true)

            /**
             * Device & platform
             */
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11")
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_3a_API_30_x86")

            /**
             * When the .apk being tested is build as test-only (ex when it is created by running on Android Studio)
             * https://github.com/appium/appium/issues/10758
             * https://stackoverflow.com/questions/25274296/adb-install-fails-with-install-failed-test-only
             */
            capabilities.setCapability("allowTestPackages", true)

            /**
             * Android driver strategy: UiAutomator2 or Espresso
             */

            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2")

            // Espresso driver config. It is mandatory to set the app capability when using Espresso driver
            //capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Espresso")
            //capabilities.setCapability("forceEspressoRebuild", true)
            //capabilities.setCapability(MobileCapabilityType.APP, "C:\\workspaces\\beagle\\android\\automated-tests\\build\\outputs\\apk\\debug\\automated-tests-debug.apk");

            /**
             *
             * MobileCapabilityType.APP capability is not required for Android if you specify appPackage and appActivity
             * In this case, the Android image already contains the app
             */
            capabilities.setCapability("appPackage", "br.com.zup.beagle.appiumApp")
            capabilities.setCapability("appActivity", ".activity.MainActivity")

            driver = AndroidDriver<MobileElement>(/*service?.url*/URL(APPIUM_URL), capabilities)
            //driver?.launchApp()
        } else {
            capabilities.setCapability("noReset", true)
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS")
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "13.4")
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 11")
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest")
            capabilities.setCapability(
                MobileCapabilityType.APP,
                "/Users/luisgustavooliveirasilva/Library/Developer/Xcode/DerivedData/Beagle-gnqdhkpaxlbwgnbcpaltnxvwyeum/Build/Products/Debug-iphonesimulator/AutomatedTests.app"
            )
            capabilities.setCapability("waitForQuiescence", false)

            driver = IOSDriver<MobileElement>(URL(APPIUM_URL), capabilities)
        }
    }

    // Too slow on iOS
    fun resetApp() {
        try {
            driver?.resetApp();
        } catch (e: Exception) {
            println("ERROR resetting app: ${e.message}")
        }
    }

    fun restartApp() {
        try {
            driver?.closeApp()
        } catch (e: Exception) {
            println("ERROR closing app: ${e.message}")
        } finally {
            driver?.launchApp()
        }
    }

    fun closeDriver() {
        try {
            driver?.closeApp();
        } catch (e: Exception) {
            println("ERROR closing app: ${e.message}")
        } finally {
            driver?.quit()
        }
    }

    /*
    @Throws(IOException::class)
    private fun startAppiumServer() {
        val serviceBuilder = AppiumServiceBuilder()
        //serviceBuilder.usingDriverExecutable(File("/path/to/node/executable"));
        //serviceBuilder.withAppiumJS(new File("/path/to/appium"));
        //service = AppiumDriverLocalService.buildService(serviceBuilder);
        service = AppiumDriverLocalService.buildDefaultService()
        service?.start()
    }
    */


}