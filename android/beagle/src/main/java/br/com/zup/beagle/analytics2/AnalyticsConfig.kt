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

/**
 * This interface is used to create the analytics recording according to rendering process and actions execute
 */
interface AnalyticsConfig {
    /**
     * This attribute enable the analytics to create a report when a screen is loaded.
     * To not create analytics when a screen is loaded, set this attribute as false
    */
    var enableScreenAnalytics : Boolean?

    /**
     * This attribute is a map of actions allowed to create analytics actions record.
     * In this map, each key is an action name following the _beagleAction_ value on the JSON provided to create our
     * screen. The value for each key is a list of attributes that you would like to be reported.
    */
    var actions : Map<String, List<String>>?
}
