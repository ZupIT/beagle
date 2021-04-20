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
import io.appium.java_client.android.AndroidElement
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.MobileCapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import java.net.URL


object SuiteSetup {

    const val ERROR_SCREENSHOTS_FOLDER = "./build/screenshots"
    const val SCREENSHOTS_DATABASE_FOLDER = "./src/test/resources/screenshots_database"
    private var platform: String? = null
    private var deviceName: String? = null
    private var driver: AppiumDriver<*>? = null
    private var bffBaseUrl: String? = null

    fun getBaseUrl() = bffBaseUrl

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

    fun getDeviceName(): String {
        return deviceName!!
    }

    fun initSuit() {

        if (driver != null)
            throw Exception("Test suite already running")

        platform = System.getProperty("platform") // mandatory param
        if (platform.isNullOrBlank())
            throw Exception("Missing param: platform")

        if (!"android".equals(platform, true) && !"ios".equals(platform, true))
            throw Exception("Invalid platform param: $platform. Platform must be android or ios")


        println("#### Initializing test suite setup with platform $platform...")

        bffBaseUrl = System.getProperty("bff_base_url")
        if (bffBaseUrl.isNullOrBlank()) {
            if (isAndroid())
                bffBaseUrl = "http://10.0.2.2:8080"
            else
                bffBaseUrl = "http://localhost:8080"
        }

        /**
         * List of capabilities: http://appium.io/docs/en/writing-running-appium/caps/
         */
        var platformVersion = System.getProperty("platform_version")
        deviceName = System.getProperty("device_name")
        var appFile = System.getProperty("app_file")
        var browserstackUser = System.getProperty("browserstack_user")
        var browserstackKey = System.getProperty("browserstack_key")
        val capabilities = DesiredCapabilities()
        if (isAndroid()) {

            val appPackage = "br.com.zup.beagle.appiumapp"
            val appActivity = ".activity.MainActivity"

            // when device is running at BrowserStack
            if (!browserstackUser.isNullOrBlank() && !browserstackKey.isNullOrBlank()) {

                capabilities.setCapability("ignoreHiddenApiPolicyError", true)
                capabilities.setCapability("noReset", true)
                capabilities.setCapability("fullReset", false)
                capabilities.setCapability("allowTestPackages", true)

                capabilities.setCapability("browserstack.user", browserstackUser)
                capabilities.setCapability("browserstack.key", browserstackKey)
                capabilities.setCapability("app", appFile)
                capabilities.setCapability("device", deviceName);
                capabilities.setCapability("os_version", platformVersion);
                capabilities.setCapability("project", "Beagle Appium tests")
                capabilities.setCapability("build", "Kotlin Android")
                capabilities.setCapability("name", "Beagle Appium tests on Android")
                capabilities.setCapability("browserstack.networkLogs", true)

                println("#### Using BrowserStack ... ")
                driver = AndroidDriver<AndroidElement>(URL("http://hub.browserstack.com/wd/hub"), capabilities)


            } else // device is running locally
            {

                if (platformVersion.isNullOrBlank())
                    platformVersion = "11"

                if (deviceName.isNullOrBlank())
                    deviceName = "Pixel_4_API_30"

                capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2")
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion)
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName)
                capabilities.setCapability("appPackage", appPackage)
                capabilities.setCapability("appActivity", appActivity)
                if (!appFile.isNullOrBlank())  // The "app" capability is not required if the emulator already has the app installed
                    capabilities.setCapability(MobileCapabilityType.APP, appFile)

                capabilities.setCapability("ignoreHiddenApiPolicyError", true)
                // capabilities.setCapability("disableWindowAnimation", true)
                // capabilities.setCapability("uiautomator2ServerInstallTimeout", 90000);
                capabilities.setCapability("noReset", true)
                capabilities.setCapability("fullReset", false)
                capabilities.setCapability("allowTestPackages", true)

                driver = AndroidDriver<MobileElement>(URL(APPIUM_URL), capabilities)
            }

            // checks if the app has started correctly
            if (!appPackage.equals((driver as AndroidDriver<MobileElement>).currentPackage) ||
                !appActivity.equals((driver as AndroidDriver<MobileElement>).currentActivity())
            ) {
                throw Exception("Error loading the app and activity!")
            }

        } else /* iOS */ {

            if (platformVersion.isNullOrBlank())
                platformVersion = "13.5"

            if (deviceName.isNullOrBlank())
                deviceName = "iPhone 11"

            /**
             * The required .app file is usually at
             * ~/Library/Developer/Xcode/DerivedData/APP-RANDOM-CODE/Build/Products/Debug-iphonesimulator/AppiumApp.app
             */
            if (appFile.isNullOrBlank())
                appFile = "COMPLETE-PATH-TO/AppiumApp.app"

            capabilities.setCapability("noReset", true)
            capabilities.setCapability("waitForQuiescence", "false");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS")
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest")
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion)
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName)
            capabilities.setCapability(MobileCapabilityType.APP, appFile)

            // helps with caching and socket hangup problems
            //capabilities.setCapability("clearSystemFiles", "true")
            //capabilities.setCapability("shouldUseSingletonTestManager", "false")

            println("#### starting iOS driver ... ")
            driver = IOSDriver<MobileElement>(URL(APPIUM_URL), capabilities)
            println("#### iOS driver started! ")
        }
    }


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

}