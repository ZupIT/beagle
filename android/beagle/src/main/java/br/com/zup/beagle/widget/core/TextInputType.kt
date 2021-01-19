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

package br.com.zup.beagle.widget.core

import br.com.zup.beagle.core.BeagleJson

/**
 * Enum define text support.
 */
@BeagleJson
enum class TextInputType {
    /**
     * Date text support.
     * This attribute on iOS will have the same effect as NUMBER.
     */
    @BeagleJson(name = "DATE")
    DATE,

    /**
     * Email text support.
     */
    @BeagleJson(name = "EMAIL")
    EMAIL,

    /**
     * Password text support.
     */
    @BeagleJson(name = "PASSWORD")
    PASSWORD,

    /**
     * Numeric text support.
     */
    @BeagleJson(name = "NUMBER")
    NUMBER,

    /**
     * Normal text support.
     */
    @BeagleJson(name = "TEXT")
    TEXT
}