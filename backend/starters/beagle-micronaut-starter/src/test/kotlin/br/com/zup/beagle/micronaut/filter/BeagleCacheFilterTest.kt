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

package br.com.zup.beagle.micronaut.filter

import br.com.zup.beagle.constants.BEAGLE_CACHE_ENABLED
import br.com.zup.beagle.micronaut.containsBeans
import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class BeagleCacheFilterTest {
    @Test
    fun test_BeagleCacheFilter_is_in_context_by_default() {
        assertTrue { ApplicationContext.run().containsBeans(BeagleCacheFilter::class) }
    }

    @Test
    fun test_BeagleCacheFilter_is_in_context_when_enabled_is_true() {
        assertTrue {
            ApplicationContext.run(mapOf(BEAGLE_CACHE_ENABLED to true)).containsBeans(BeagleCacheFilter::class)
        }
    }

    @Test
    fun test_BeagleCacheFilter_is_not_in_context_when_enabled_is_false() {
        assertFalse {
            ApplicationContext.run(mapOf(BEAGLE_CACHE_ENABLED to false)).containsBeans(BeagleCacheFilter::class)
        }
    }
}