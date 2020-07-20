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

package br.com.zup.beagle.spring.configuration

import br.com.zup.beagle.cache.BeagleCacheHandler
import br.com.zup.beagle.constants.BEAGLE_CACHE_ENABLED
import br.com.zup.beagle.constants.BEAGLE_CACHE_EXCLUDES
import br.com.zup.beagle.constants.BEAGLE_CACHE_INCLUDES
import br.com.zup.beagle.constants.BEAGLE_CACHE_TTL
import br.com.zup.beagle.spring.filter.BeagleCacheFilter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.FilteredClassLoader
import org.springframework.boot.test.context.assertj.AssertableApplicationContext
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.boot.web.servlet.FilterRegistrationBean
import java.time.Duration
import kotlin.test.assertTrue

internal class BeagleCacheAutoConfigurationTest {
    companion object {
        private val BLANK_LIST = listOf("")
        private val SOME_LIST = listOf("test")
        private const val ENDPOINT = "/test%d/.*"
    }

    private val contextRunner by lazy {
        ApplicationContextRunner().withConfiguration(AutoConfigurations.of(BeagleCacheAutoConfiguration::class.java))
    }
    private val cacheFilterBeanName = "beagleCachingFilter"
    private val includesField = "includeEndpointList"
    private val excludesField = "excludeEndpointList"
    private val propertiesField = "properties"

    @Test
    fun `beagleCacheAutoConfiguration must not be present with enabled property false`() {
        this.contextRunner.withPropertyValues("$BEAGLE_CACHE_ENABLED=false").run {
            validateCacheFilter(it, true)
        }
    }

    @Test
    fun `beagleCacheAutoConfiguration must be present with enabled property true or absent`() {
        this.contextRunner.withPropertyValues("$BEAGLE_CACHE_ENABLED=true").run {
            validateCacheFilter(it)
        }
        this.contextRunner.run {
            validateCacheFilter(it)
        }
    }

    @Test
    fun `beagleCacheAutoConfiguration must not be present without required classes`() {
        val filterClassLoader = FilteredClassLoader(BeagleCacheFilter::class.java, BeagleCacheHandler::class.java)
        this.contextRunner.withClassLoader(filterClassLoader).run {
            validateCacheFilter(it, true)
        }
    }

    @Test
    fun `beagleCacheAutoConfiguration must be present with default value for properties`() {
        this.contextRunner.run {
            validateCacheFilter(it)
            assertThat(it).getBean(BeagleCacheAutoConfiguration::class.java)
                .hasFieldOrPropertyWithValue(this.propertiesField, BeagleSpringCacheProperties())
        }
    }

    @Test
    fun `beagleCacheAutoConfiguration must be present with test values for include property`() {
        this.contextRunner.withPropertyValues("$BEAGLE_CACHE_INCLUDES=${SOME_LIST.joinToString(",")}").run {
            validateCacheFilter(it)
            assertThat(it).getBean(BeagleCacheAutoConfiguration::class.java)
                .hasFieldOrPropertyWithValue(this.propertiesField, BeagleSpringCacheProperties(include = SOME_LIST))
        }
    }

    @Test
    fun `beagleCacheAutoConfiguration must be present with test values for exclude property`() {
        this.contextRunner.withPropertyValues("$BEAGLE_CACHE_EXCLUDES=${SOME_LIST.joinToString(",")}").run {
            validateCacheFilter(it)
            assertThat(it).getBean(BeagleCacheAutoConfiguration::class.java)
                .hasFieldOrPropertyWithValue(this.propertiesField, BeagleSpringCacheProperties(exclude = SOME_LIST))
        }
    }

    @Test
    fun `beagleCacheAutoConfiguration must be present with test values for include and exclude property`() {
        this.contextRunner.withPropertyValues(
            "$BEAGLE_CACHE_INCLUDES=${SOME_LIST.joinToString(",")}",
            "$BEAGLE_CACHE_EXCLUDES=${SOME_LIST.joinToString(",")}"
        ).run {
            validateCacheFilter(it)
            assertThat(it).getBean(BeagleCacheAutoConfiguration::class.java).hasFieldOrPropertyWithValue(
                this.propertiesField,
                BeagleSpringCacheProperties(include = SOME_LIST, exclude = SOME_LIST)
            )
        }
    }

    @Test
    fun `beagleCacheAutoConfiguration must be present with blank input values for include and exclude property`() {
        this.contextRunner.withPropertyValues(
            "$BEAGLE_CACHE_INCLUDES=${BLANK_LIST.joinToString(",")}",
            "$BEAGLE_CACHE_EXCLUDES=${BLANK_LIST.joinToString(",")}"
        ).run {
            validateCacheFilter(it)
            assertThat(it).getBean(BeagleCacheAutoConfiguration::class.java)
                .hasFieldOrPropertyWithValue(this.propertiesField, BeagleSpringCacheProperties())
        }
    }


    @Test
    fun `beagleCacheAutoConfiguration must fail to start with invalid exclude property`() {
        this.contextRunner.withPropertyValues("$BEAGLE_CACHE_EXCLUDES=?").run { assertThat(it).hasFailed() }
    }

    @Test
    fun `beagleCacheAutoConfiguration must fail to start with invalid include property`() {
        this.contextRunner.withPropertyValues("$BEAGLE_CACHE_INCLUDES=?").run { assertThat(it).hasFailed() }
    }

    @Test
    fun `beagleCacheAutoConfiguration configures endpoint TTL map`() {
        this.contextRunner.withPropertyValues(
            "$BEAGLE_CACHE_TTL.[${ENDPOINT.format(1)}]=15ns",
            "$BEAGLE_CACHE_TTL.[${ENDPOINT.format(2)}]=15ms",
            "$BEAGLE_CACHE_TTL.[${ENDPOINT.format(3)}]=15s"
        ).run {
            validateCacheFilter(it)
            assertThat(it).getBean(BeagleCacheAutoConfiguration::class.java)
                .hasFieldOrPropertyWithValue(
                    this.propertiesField,
                    BeagleSpringCacheProperties(
                        ttl = mapOf(
                            ENDPOINT.format(1) to Duration.ofNanos(15),
                            ENDPOINT.format(2) to Duration.ofMillis(15),
                            ENDPOINT.format(3) to Duration.ofSeconds(15)
                        )
                    )
                )
        }
    }

    @Test
    fun `beagleCacheAutoConfiguration configures endpoint TTL map overwriting keys for different profiles`() {
        this.contextRunner.withPropertyValues(
            "$BEAGLE_CACHE_TTL.[${ENDPOINT.format(1)}]=1",
            "$BEAGLE_CACHE_TTL.[${ENDPOINT.format(3)}]=15s"
        ).withPropertyValues(
            "$BEAGLE_CACHE_TTL.[${ENDPOINT.format(1)}]=15ns",
            "$BEAGLE_CACHE_TTL.[${ENDPOINT.format(2)}]=15ms"
        ).run {
            validateCacheFilter(it)
            assertThat(it).getBean(BeagleCacheAutoConfiguration::class.java)
                .hasFieldOrPropertyWithValue(
                    this.propertiesField,
                    BeagleSpringCacheProperties(
                        ttl = mapOf(
                            ENDPOINT.format(1) to Duration.ofNanos(15),
                            ENDPOINT.format(2) to Duration.ofMillis(15),
                            ENDPOINT.format(3) to Duration.ofSeconds(15)
                        )
                    )
                )
        }
    }

    @Test
    fun `beagleCacheAutoConfiguration configures endpoint TTL map when duration has no time unit`() {
        this.contextRunner.withPropertyValues("$BEAGLE_CACHE_TTL.[$ENDPOINT]=10").run {
            validateCacheFilter(it)
            assertThat(it).getBean(BeagleCacheAutoConfiguration::class.java)
                .hasFieldOrPropertyWithValue(
                    this.propertiesField,
                    BeagleSpringCacheProperties(ttl = mapOf(ENDPOINT to Duration.ofMillis(10)))
                )
        }
    }

    private fun validateCacheFilter(context: AssertableApplicationContext, toNotExists: Boolean = false) {
        if (toNotExists) {
            assertThat(context).doesNotHaveBean(BeagleCacheAutoConfiguration::class.java)
            assertThat(context).doesNotHaveBean(FilterRegistrationBean::class.java)
        } else {
            val cacheFilter = (context.getBean(this.cacheFilterBeanName) as FilterRegistrationBean<*>).filter
            assertThat(context).hasSingleBean(BeagleCacheAutoConfiguration::class.java)
            assertTrue(cacheFilter is BeagleCacheFilter)
        }
    }
}