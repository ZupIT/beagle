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

package br.com.zup.beagle.android.components.utils

import android.view.View
import android.widget.TextView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import br.com.zup.beagle.core.AccessibilityComponent
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("Given Component with accessibility")
class AccessibilitySetupTest {

    private val view: TextView = mockk(relaxUnitFun = true, relaxed = true)

    private val component: AccessibilityComponent = mockk(relaxed = true)

    private val accessibilityDelegate = slot<AccessibilityDelegateCompat>()

    @InjectMockKs
    private lateinit var accessibilitySetup: AccessibilitySetup

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(ViewCompat::class)


        every {
            ViewCompat.setAccessibilityDelegate(view, capture(accessibilityDelegate))
        } just Runs
    }

    @DisplayName("When apply accessibility")
    @Nested
    inner class ApplyAccessibility {

        @Test
        @DisplayName("Then the component should be accessible")
        fun testComponentIsAccessible() {
            val result = View.IMPORTANT_FOR_ACCESSIBILITY_YES
            every { component.accessibility?.accessible } returns true
            every { component.accessibility?.accessibilityLabel } returns null

            accessibilitySetup.applyAccessibility(view, component)

            verify { view.importantForAccessibility = result }
        }

        @Test
        @DisplayName("Then the component should not be accessible")
        fun testComponentIsNotAccessible() {
            val result = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            every { component.accessibility?.accessible } returns false
            every { component.accessibility?.accessibilityLabel } returns null

            accessibilitySetup.applyAccessibility(view, component)

            verify { view.importantForAccessibility = result }
        }

        @Test
        @DisplayName("Then the component should set accessibility label")
        fun testLabelAccessibility() {
            val result = "Test with AccessibilityLabel"
            every { component.accessibility?.accessibilityLabel } returns "Test with AccessibilityLabel"
            every { component.accessibility?.accessible } returns true

            accessibilitySetup.applyAccessibility(view, component)

            verify { view.contentDescription = result }
        }

        @Test
        @DisplayName("Then the component should set heading")
        fun testHeading() {
            val accessibilityNodeInfoCompatMock: AccessibilityNodeInfoCompat = mockk(relaxed = true, relaxUnitFun = true)

            every { component.accessibility?.isHeader } returns true

            accessibilitySetup.applyAccessibility(view, component)
            accessibilityDelegate.captured.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompatMock)

            verify { accessibilityNodeInfoCompatMock.isHeading = true }
        }
    }

}