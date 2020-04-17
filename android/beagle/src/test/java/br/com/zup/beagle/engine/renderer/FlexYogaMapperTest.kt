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

package br.com.zup.beagle.engine.renderer

import br.com.zup.beagle.engine.mapper.makeYogaAlign
import br.com.zup.beagle.engine.mapper.makeYogaDirection
import br.com.zup.beagle.engine.mapper.makeYogaDisplay
import br.com.zup.beagle.engine.mapper.makeYogaFlexDirection
import br.com.zup.beagle.engine.mapper.makeYogaJustify
import br.com.zup.beagle.engine.mapper.makeYogaPositionType
import br.com.zup.beagle.engine.mapper.makeYogaWrap
import br.com.zup.beagle.widget.core.Alignment
import br.com.zup.beagle.widget.core.FlexWrap
import br.com.zup.beagle.widget.core.Direction
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.FlexDisplay
import br.com.zup.beagle.widget.core.FlexPositionType
import br.com.zup.beagle.widget.core.JustifyContent
import com.facebook.yoga.YogaAlign
import com.facebook.yoga.YogaDirection
import com.facebook.yoga.YogaDisplay
import com.facebook.yoga.YogaFlexDirection
import com.facebook.yoga.YogaJustify
import com.facebook.yoga.YogaPositionType
import com.facebook.yoga.YogaWrap
import org.junit.Test

import org.junit.Assert.*

class FlexYogaMapperTest {

    @Test
    fun makeYogaWrap_should_return_YogaWrap_NO_WRAP_when_FlexWrap_NO_WRAP() {
        // Given
        val flexWrap = FlexWrap.NO_WRAP

        // When
        val actual = makeYogaWrap(flexWrap)

        // Then
        assertEquals(YogaWrap.NO_WRAP, actual)
    }

    @Test
    fun makeYogaWrap_should_return_YogaWrap_WRAP_when_FlexWrap_WRAP() {
        // Given
        val flexWrap = FlexWrap.WRAP

        // When
        val actual = makeYogaWrap(flexWrap)

        // Then
        assertEquals(YogaWrap.WRAP, actual)
    }

    @Test
    fun makeYogaWrap_should_return_YogaWrap_WRAP_REVERSE_when_FlexWrap_WRAP_REVERSE() {
        // Given
        val flexWrap = FlexWrap.WRAP_REVERSE

        // When
        val actual = makeYogaWrap(flexWrap)

        // Then
        assertEquals(YogaWrap.WRAP_REVERSE, actual)
    }

    @Test
    fun makeYogaWrap_should_return_null_when_FlexWrap_is_null() {
        // Given
        val flexWrap: FlexWrap? = null

        // When
        val actual = makeYogaWrap(flexWrap)

        // Then
        assertNull(actual)
    }

    @Test
    fun makeYogaAlign_should_return_YogaAlign_CENTER_when_Alignment_CENTER() {
        // Given
        val alignment = Alignment.CENTER

        // When
        val actual = makeYogaAlign(alignment)

        // Then
        assertEquals(YogaAlign.CENTER, actual)
    }

    @Test
    fun makeYogaAlign_should_return_YogaAlign_FLEX_START_when_Alignment_FLEX_START() {
        // Given
        val alignment = Alignment.FLEX_START

        // When
        val actual = makeYogaAlign(alignment)

        // Then
        assertEquals(YogaAlign.FLEX_START, actual)
    }

    @Test
    fun makeYogaAlign_should_return_YogaAlign_FLEX_END_when_Alignment_FLEX_END() {
        // Given
        val alignment = Alignment.FLEX_END

        // When
        val actual = makeYogaAlign(alignment)

        // Then
        assertEquals(YogaAlign.FLEX_END, actual)
    }

    @Test
    fun makeYogaAlign_should_return_YogaAlign_SPACE_BETWEEN_when_Alignment_SPACE_BETWEEN() {
        // Given
        val alignment = Alignment.SPACE_BETWEEN

        // When
        val actual = makeYogaAlign(alignment)

        // Then
        assertEquals(YogaAlign.SPACE_BETWEEN, actual)
    }

    @Test
    fun makeYogaAlign_should_return_YogaAlign_SPACE_AROUND_when_Alignment_SPACE_AROUND() {
        // Given
        val alignment = Alignment.SPACE_AROUND

        // When
        val actual = makeYogaAlign(alignment)

        // Then
        assertEquals(YogaAlign.SPACE_AROUND, actual)
    }

    @Test
    fun makeYogaAlign_should_return_YogaAlign_BASELINE_when_Alignment_BASELINE() {
        // Given
        val alignment = Alignment.BASELINE

        // When
        val actual = makeYogaAlign(alignment)

        // Then
        assertEquals(YogaAlign.BASELINE, actual)
    }

    @Test
    fun makeYogaAlign_should_return_YogaAlign_AUTO_when_Alignment_AUTO() {
        // Given
        val alignment = Alignment.AUTO

        // When
        val actual = makeYogaAlign(alignment)

        // Then
        assertEquals(YogaAlign.AUTO, actual)
    }

    @Test
    fun makeYogaAlign_should_return_YogaAlign_STRETCH_when_Alignment_STRETCH() {
        // Given
        val alignment = Alignment.STRETCH

        // When
        val actual = makeYogaAlign(alignment)

        // Then
        assertEquals(YogaAlign.STRETCH, actual)
    }

    @Test
    fun makeYogaAlign_should_return_null_when_Alignment_is_null() {
        // Given
        val alignment: Alignment? = null

        // When
        val actual = makeYogaAlign(alignment)

        // Then
        assertNull(actual)
    }

    @Test
    fun makeYogaJustify_should_return_YogaJustify_FLEX_START_when_JustifyContent_FLEX_START() {
        // Given
        val alignment = JustifyContent.FLEX_START

        // When
        val actual = makeYogaJustify(alignment)

        // Then
        assertEquals(YogaJustify.FLEX_START, actual)
    }

    @Test
    fun makeYogaJustify_should_return_YogaJustify_CENTER_when_JustifyContent_CENTER() {
        // Given
        val alignment = JustifyContent.CENTER

        // When
        val actual = makeYogaJustify(alignment)

        // Then
        assertEquals(YogaJustify.CENTER, actual)
    }

    @Test
    fun makeYogaJustify_should_return_YogaJustify_FLEX_END_when_JustifyContent_FLEX_END() {
        // Given
        val alignment = JustifyContent.FLEX_END

        // When
        val actual = makeYogaJustify(alignment)

        // Then
        assertEquals(YogaJustify.FLEX_END, actual)
    }

    @Test
    fun makeYogaJustify_should_return_YogaJustify_SPACE_BETWEEN_when_JustifyContent_SPACE_BETWEEN() {
        // Given
        val alignment = JustifyContent.SPACE_BETWEEN

        // When
        val actual = makeYogaJustify(alignment)

        // Then
        assertEquals(YogaJustify.SPACE_BETWEEN, actual)
    }

    @Test
    fun makeYogaJustify_should_return_YogaJustify_SPACE_AROUND_when_JustifyContent_SPACE_AROUND() {
        // Given
        val alignment = JustifyContent.SPACE_AROUND

        // When
        val actual = makeYogaJustify(alignment)

        // Then
        assertEquals(YogaJustify.SPACE_AROUND, actual)
    }

    @Test
    fun makeYogaJustify_should_return_YogaJustify_SPACE_EVENLY_when_JustifyContent_SPACE_EVENLY() {
        // Given
        val alignment = JustifyContent.SPACE_EVENLY

        // When
        val actual = makeYogaJustify(alignment)

        // Then
        assertEquals(YogaJustify.SPACE_EVENLY, actual)
    }

    @Test
    fun makeYogaJustify_should_return_null_when_JustifyContent_is_null() {
        // Given
        val justifyContent: JustifyContent? = null

        // When
        val actual = makeYogaJustify(justifyContent)

        // Then
        assertNull(actual)
    }

    @Test
    fun makeYogaDirection_should_return_YogaDirection_INHERIT_when_ItemDirection_INHERIT() {
        // Given
        val direction = Direction.INHERIT

        // When
        val actual =
            makeYogaDirection(direction)

        // Then
        assertEquals(YogaDirection.INHERIT, actual)
    }

    @Test
    fun makeYogaDirection_should_return_YogaDirection_LTR_when_ItemDirection_LTR() {
        // Given
        val direction = Direction.LTR

        // When
        val actual =
            makeYogaDirection(direction)

        // Then
        assertEquals(YogaDirection.LTR, actual)
    }

    @Test
    fun makeYogaDirection_should_return_YogaDirection_RTL_when_ItemDirection_RTL() {
        // Given
        val direction = Direction.RTL

        // When
        val actual = makeYogaDirection(direction)

        // Then
        assertEquals(YogaDirection.RTL, actual)
    }

    @Test
    fun makeYogaDirection_should_return_null_when_Direction_is_null() {
        // Given
        val direction: Direction? = null

        // When
        val actual = makeYogaDirection(direction)

        // Then
        assertNull(actual)
    }

    @Test
    fun makeYogaFlexDirection_should_return_YogaFlexDirection_COLUMN_when_FlexDirection_is_COLUMN() {
        // Given
        val direction = FlexDirection.COLUMN

        // When
        val actual =
            makeYogaFlexDirection(direction)

        // Then
        assertEquals(YogaFlexDirection.COLUMN, actual)
    }

    @Test
    fun makeYogaFlexDirection_should_return_YogaFlexDirection_ROW_when_FlexDirection_is_ROW() {
        // Given
        val direction = FlexDirection.ROW

        // When
        val actual =
            makeYogaFlexDirection(direction)

        // Then
        assertEquals(YogaFlexDirection.ROW, actual)
    }

    @Test
    fun makeYogaFlexDirection_should_return_YogaFlexDirection_ROW_REVERSE_when_FlexDirection_is_ROW_REVERSE() {
        // Given
        val direction = FlexDirection.ROW_REVERSE

        // When
        val actual =
            makeYogaFlexDirection(direction)

        // Then
        assertEquals(YogaFlexDirection.ROW_REVERSE, actual)
    }

    @Test
    fun makeYogaFlexDirection_should_return_YogaFlexDirection_COLUMN_REVERSE_when_FlexDirection_is_COLUMN_REVERSE() {
        // Given
        val direction = FlexDirection.COLUMN_REVERSE

        // When
        val actual =
            makeYogaFlexDirection(direction)

        // Then
        assertEquals(YogaFlexDirection.COLUMN_REVERSE, actual)
    }

    @Test
    fun makeYogaFlexDirection_should_return_null_when_FlexDirection_is_null() {
        // Given
        val direction: FlexDirection? = null

        // When
        val actual = makeYogaFlexDirection(direction)

        // Then
        assertNull(actual)
    }

    @Test
    fun makeYogaDisplay_should_return_FlexDisplay_as_FLEX_when_FlexDisplay_is_FLEX() {
        // Given
        val display = FlexDisplay.FLEX

        // When
        val actual = makeYogaDisplay(display)

        // Then
        assertEquals(YogaDisplay.FLEX, actual)
    }

    @Test
    fun makeYogaDisplay_should_return_FlexDisplay_as_NONE_when_FlexDisplay_is_NONE() {
        // Given
        val display = FlexDisplay.NONE

        // When
        val actual = makeYogaDisplay(display)

        // Then
        assertEquals(YogaDisplay.NONE, actual)
    }

    @Test
    fun makeYogaDisplay_should_return_null_when_FlexDisplay_is_null() {
        // Given
        val flexDisplay: FlexDisplay? = null

        // When
        val actual = makeYogaDisplay(flexDisplay)

        // Then
        assertNull(actual)
    }

    @Test
    fun makeYogaPositionType_should_return_RELATIVE_when_FlexPositionType_is_RELATIVE() {
        // Given
        val flexPositionType = FlexPositionType.RELATIVE

        // When
        val actual = makeYogaPositionType(flexPositionType)

        // Then
        assertEquals(YogaPositionType.RELATIVE, actual)
    }

    @Test
    fun makeYogaPositionType_should_return_ABSOLUTE_when_FlexPositionType_is_ABSOLUTE() {
        // Given
        val flexPositionType = FlexPositionType.ABSOLUTE

        // When
        val actual = makeYogaPositionType(flexPositionType)

        // Then
        assertEquals(YogaPositionType.ABSOLUTE, actual)
    }
}