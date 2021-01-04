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

package br.com.zup.beagle.analytics2

typealias analyticsConfig = (analyticConfig: AnalyticsConfig) -> Unit
typealias startSession = ()-> Unit

/**
 * This interface is used to start the configuration of analytics, without this the analytics will not be reported.
*/
interface AnalyticsProvider {

    /**
     * This function is called to get the AnalyticsConfig.
     * @attribute config is a function that need to be invoked passing yours AnalyticConfig on parameter
    */
    fun getConfig(config: analyticsConfig)

    /**
     * This function is called when Beagle is initialized.
     * @attribute startSession is a function that need to be invoked when session is started to get the config.
    */
    fun startSession(startSession: startSession)

    /**
     * This function is called to record the reports.
     * @attribute record is an AnalyticsRecord generated when a screen is loaded or a action is executed
     * according the config passing on getConfig function.
    */
    fun createRecord(record: AnalyticsRecord)

    /**
     *@return an Int to control the maximum iitems that will be on the queue of reports while the config
     * was not provided.
    */
    fun getMaximumItemsInQueue() = 100
}
