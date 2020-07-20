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

package br.com.zup.beagle.micronaut.configuration

import br.com.zup.beagle.cache.BeagleCacheHandler
import br.com.zup.beagle.constants.BEAGLE_CACHE_EXCLUDES
import br.com.zup.beagle.constants.BEAGLE_CACHE_INCLUDES
import br.com.zup.beagle.micronaut.containsBeans
import br.com.zup.beagle.micronaut.getProperty
import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BeagleCacheConfigurationTest {
    companion object {
        private val BLANK_LIST = listOf("")
        private val SOME_LIST = listOf("test1", "test2")
    }

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context`() {
        ApplicationContext.run().also { this.validateContext(it, BLANK_LIST, BLANK_LIST) }
    }

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context with includes`() =
        this.testWithProperties(SOME_LIST, BLANK_LIST)

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context with excludes`() =
        this.testWithProperties(BLANK_LIST, SOME_LIST)

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context with includes and excludes`() =
        this.testWithProperties(SOME_LIST, SOME_LIST)

    private fun testWithProperties(includes: List<String>, excludes: List<String>) {
        val properties = mapOf(
            BEAGLE_CACHE_INCLUDES to includes.joinToString(","),
            BEAGLE_CACHE_EXCLUDES to excludes.joinToString(",")
        )

        ApplicationContext.build(properties).start().also { this.validateContext(it, includes, excludes) }
    }

    private fun validateContext(context: ApplicationContext, includes: List<String>, excludes: List<String>) {
        assertTrue { context.containsBeans(BeagleCacheConfiguration::class, BeagleCacheHandler::class) }
        context.getBean(BeagleCacheConfiguration::class.java).also {
            assertEquals(includes, it.getProperty("includeEndpoints"))
            assertEquals(excludes, it.getProperty("excludeEndpoints"))
        }
    }
}