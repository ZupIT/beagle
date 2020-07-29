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
import br.com.zup.beagle.constants.BEAGLE_CACHE_TTL
import br.com.zup.beagle.micronaut.containsBeans
import br.com.zup.beagle.micronaut.getProperty
import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BeagleCacheConfigurationTest {
    companion object {
        private val BLANK_LIST = listOf("")
        private val SOME_LIST = listOf("test1", "test2")
        private const val ENDPOINT = "/test%d"  // TODO support Kotlin Regex syntax with escapes, similar to Spring
    }

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context`() {
        ApplicationContext.run().also { this.validateContext(it) }
    }

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context with includes`() =
        this.testWithInclusionAndExclusionProperties(SOME_LIST, BLANK_LIST)

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context with excludes`() =
        this.testWithInclusionAndExclusionProperties(BLANK_LIST, SOME_LIST)

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context with includes and excludes`() =
        this.testWithInclusionAndExclusionProperties(SOME_LIST, SOME_LIST)

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context with TTL map`() {
        ApplicationContext.build(
            mapOf(
                "$BEAGLE_CACHE_TTL.${ENDPOINT.format(1)}" to "15ns",
                "$BEAGLE_CACHE_TTL.${ENDPOINT.format(2)}" to "15ms",
                "$BEAGLE_CACHE_TTL.${ENDPOINT.format(3)}" to "15s"
            )
        ).start().also {
            this.validateContext(
                context = it,
                ttl = mapOf(
                    ENDPOINT.format(1) to Duration.ofNanos(15),
                    ENDPOINT.format(2) to Duration.ofMillis(15),
                    ENDPOINT.format(3) to Duration.ofSeconds(15)
                )
            )
        }
    }

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context with TTL map overwriting keys for different profiles`() {
        ApplicationContext.build(
            mapOf(
                "$BEAGLE_CACHE_TTL.${ENDPOINT.format(1)}" to "10ns",
                "$BEAGLE_CACHE_TTL.${ENDPOINT.format(3)}" to "15s"
            )
        ).properties(
            mapOf(
                "$BEAGLE_CACHE_TTL.${ENDPOINT.format(1)}" to "15ns",
                "$BEAGLE_CACHE_TTL.${ENDPOINT.format(2)}" to "15ms"
            )
        ).start().also {
            this.validateContext(
                context = it,
                ttl = mapOf(
                    ENDPOINT.format(1) to Duration.ofNanos(15),
                    ENDPOINT.format(2) to Duration.ofMillis(15),
                    ENDPOINT.format(3) to Duration.ofSeconds(15)
                )
            )
        }
    }

    @Test
    fun `Test beagleCacheConfiguration sets up beagleCacheHandler in context with TTL map when duration has no time unit`() {
        ApplicationContext.build(mapOf("$BEAGLE_CACHE_TTL.`${ENDPOINT.format(1)}`" to "15")).start().also {
            this.validateContext(context = it, ttl = emptyMap())    // TODO support reading duration without TimeUnit, define a Beagle standard
        }
    }

    private fun testWithInclusionAndExclusionProperties(includes: List<String>, excludes: List<String>) {
        val properties = mapOf(
            BEAGLE_CACHE_INCLUDES to includes.joinToString(","),
            BEAGLE_CACHE_EXCLUDES to excludes.joinToString(",")
        )

        ApplicationContext.build(properties).start().also { this.validateContext(it, includes, excludes) }
    }

    private fun validateContext(
        context: ApplicationContext,
        includes: List<String> = emptyList(),
        excludes: List<String> = emptyList(),
        ttl: Map<String, Duration> = emptyMap()
    ) {
        assertTrue { context.containsBeans(BeagleCacheConfiguration::class, BeagleCacheHandler::class) }
        context.getBean(BeagleCacheConfiguration::class.java).also {
            val properties = it.getProperty("properties") as BeagleMicronautCacheProperties
            assertEquals(includes, properties.include)
            assertEquals(excludes, properties.exclude)
            assertEquals(ttl, properties.ttl)
        }
    }
}