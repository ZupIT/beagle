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

package br.com.zup.beagle.android.engine.mapper

import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.core.Display
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.FlexWrap
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import com.facebook.yoga.YogaAlign
import com.facebook.yoga.YogaDisplay
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaFlexDirection
import com.facebook.yoga.YogaJustify
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaWrap
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearStaticMockk
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

private const val HUNDRED_UNIT_VALUE = 100.0
private const val ONE_UNIT_VALUE = 1.0

class FlexMapperTest {

    @MockK
    private lateinit var yogaNode: YogaNode

    private lateinit var flexMapper: FlexMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        flexMapper = FlexMapper()

        mockkStatic(YogaNode::class)
        mockkStatic("br.com.zup.beagle.android.utils.NumberExtensionsKt")

        every { HUNDRED_UNIT_VALUE.dp() } returns HUNDRED_UNIT_VALUE
        every { ONE_UNIT_VALUE.dp() } returns ONE_UNIT_VALUE
        every { YogaNode.create() } returns yogaNode
        every { yogaNode.flexDirection = any() } just Runs
        every { yogaNode.wrap = any() } just Runs
        every { yogaNode.justifyContent = any() } just Runs
        every { yogaNode.alignItems = any() } just Runs
        every { yogaNode.alignSelf = any() } just Runs
        every { yogaNode.alignContent = any() } just Runs
        every { yogaNode.flex = any() } just Runs
        every { yogaNode.flexGrow = any() } just Runs
        every { yogaNode.flexShrink = any() } just Runs
        every { yogaNode.display = any() } just Runs
        every { yogaNode.aspectRatio = any() } just Runs
        every { yogaNode.positionType = any() } just Runs
        every { yogaNode.setWidth(any()) } just Runs
        every { yogaNode.setWidthPercent(any()) } just Runs
        every { yogaNode.setHeight(any()) } just Runs
        every { yogaNode.setHeightPercent(any()) } just Runs
        every { yogaNode.setMaxWidth(any()) } just Runs
        every { yogaNode.setMaxWidthPercent(any()) } just Runs
        every { yogaNode.setMaxHeight(any()) } just Runs
        every { yogaNode.setMaxHeightPercent(any()) } just Runs
        every { yogaNode.setMinWidth(any()) } just Runs
        every { yogaNode.setMinWidthPercent(any()) } just Runs
        every { yogaNode.setMinHeight(any()) } just Runs
        every { yogaNode.setMinHeightPercent(any()) } just Runs
        every { yogaNode.setFlexBasis(any()) } just Runs
        every { yogaNode.setFlexBasisPercent(any()) } just Runs
        every { yogaNode.setMargin(any(), any()) } just Runs
        every { yogaNode.setMarginPercent(any(), any()) } just Runs
        every { yogaNode.setPadding(any(), any()) } just Runs
        every { yogaNode.setPaddingPercent(any(), any()) } just Runs
        every { yogaNode.setPosition(any(), any()) } just Runs
        every { yogaNode.setPositionPercent(any(), any()) } just Runs
        every { yogaNode.setFlexBasisAuto() } just Runs
    }

    @After
    fun tearDown() {
        clearStaticMockk()
    }

    @Test
    fun makeYogaNode_should_set_flexDirection_as_COLUMN() {
        // Given
        val flex = Flex(
            flexDirection = FlexDirection.COLUMN
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.flexDirection = YogaFlexDirection.COLUMN }
    }

    @Test
    fun makeYogaNode_should_set_wrap_as_WRAP() {
        // Given
        val flex = Flex(
            flexWrap = FlexWrap.WRAP
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.wrap = YogaWrap.WRAP }
    }

    @Test
    fun makeYogaNode_should_set_justifyContent_as_SPACE_BETWEEN() {
        // Given
        val flex = Flex(
            justifyContent = JustifyContent.SPACE_BETWEEN
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.justifyContent = YogaJustify.SPACE_BETWEEN }
    }

    @Test
    fun makeYogaNode_should_set_alignItems_as_FLEX_START() {
        // Given
        val flex = Flex(
            alignItems = AlignItems.FLEX_START
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.alignItems = YogaAlign.FLEX_START }
    }

    @Test
    fun makeYogaNode_should_set_alignSelf_as_FLEX_START() {
        // Given
        val flex = Flex(
            alignSelf = AlignSelf.FLEX_START
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.alignSelf = YogaAlign.FLEX_START }
    }

    @Test
    fun makeYogaNode_should_set_alignContent_as_FLEX_START() {
        // Given
        val flex = Flex(
            alignContent = AlignContent.FLEX_START
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.alignContent = YogaAlign.FLEX_START }
    }

    @Test
    fun makeYogaNode_should_set_flex_as_1(){
        //Given
        val flex = Flex(
            flex = ONE_UNIT_VALUE
        )

        //When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        //Then
        verify (exactly = once()) { yogaNode.flex = ONE_UNIT_VALUE.toFloat() }
    }

    @Test
    fun makeYogaNode_should_set_flexGrow_as_SPACE_BETWEEN() {
        // Given
        val flex = Flex(
            grow = ONE_UNIT_VALUE
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.flexGrow = ONE_UNIT_VALUE.toFloat() }
    }

    @Test
    fun makeYogaNode_should_set_shrink_as_1() {
        // Given
        val flex = Flex(
            shrink = ONE_UNIT_VALUE
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.flexShrink = ONE_UNIT_VALUE.toFloat() }
    }

    @Test
    fun makeYogaNode_should_set_display_as_FLEX() {
        // Given
        val style = Style(
            display = Display.FLEX
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.display = YogaDisplay.FLEX }
    }

    @Test
    fun makeYogaNode_should_set_display_as_NONE() {
        // Given
        val style = Style(
            display = Display.NONE
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
            verify(exactly = once()) { yogaNode.display = YogaDisplay.NONE }
    }

    @Test
    fun makeYogaNode_should_set_width_as_100_0() {
        // Given
        val style = Style(
            size = Size(width = UnitValue(HUNDRED_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setWidth(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_widthPercent_as_100_0() {
        // Given
        val style = Style(
            size = Size(width = UnitValue(HUNDRED_UNIT_VALUE, UnitType.PERCENT))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setWidthPercent(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_height_as_100_0() {
        // Given
        val style = Style(
            size = Size(height = UnitValue(HUNDRED_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setHeight(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_heightPercent_as_100_0() {
        // Given
        val style = Style(
            size = Size(height = UnitValue(HUNDRED_UNIT_VALUE, UnitType.PERCENT))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setHeightPercent(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_maxWidth_as_100_0() {
        // Given
        val style = Style(
            size = Size(maxWidth = UnitValue(HUNDRED_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMaxWidth(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_maxWidthPercent_as_100_0() {
        // Given
        val style = Style(
            size = Size(maxWidth = UnitValue(HUNDRED_UNIT_VALUE, UnitType.PERCENT))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMaxWidthPercent(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_maxHeight_as_100_0() {
        // Given
        val style = Style(
            size = Size(maxHeight = UnitValue(HUNDRED_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMaxHeight(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_maxHeightPercent_as_100_0() {
        // Given
        val style = Style(
            size = Size(maxHeight = UnitValue(HUNDRED_UNIT_VALUE, UnitType.PERCENT))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMaxHeightPercent(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_minWidth_as_100_0() {
        // Given
        val style = Style(
            size = Size(minWidth = UnitValue(HUNDRED_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMinWidth(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_minWidthPercent_as_100_0() {
        // Given
        val style = Style(
            size = Size(minWidth = UnitValue(HUNDRED_UNIT_VALUE, UnitType.PERCENT))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMinWidthPercent(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_minHeight_as_100_0() {
        // Given
        val style = Style(
            size = Size(minHeight = UnitValue(HUNDRED_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMinHeight(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_minHeightPercent_as_100_0() {
        // Given
        val style = Style(
            size = Size(minHeight = UnitValue(HUNDRED_UNIT_VALUE, UnitType.PERCENT))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMinHeightPercent(HUNDRED_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_basis_as_1() {
        // Given
        val flex = Flex(
            basis = UnitValue(ONE_UNIT_VALUE, UnitType.REAL)
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.setFlexBasis(ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_basisPercent_as_1() {
        // Given
        val flex = Flex(
            basis = UnitValue(ONE_UNIT_VALUE, UnitType.PERCENT)
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.setFlexBasisPercent(ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_basisAuto() {
        // Given
        val flex = Flex(
            basis = UnitValue(ONE_UNIT_VALUE, UnitType.AUTO)
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(Style(flex = flex))

        // Then
        verify(exactly = once()) { yogaNode.setFlexBasisAuto() }
    }

    @Test
    fun makeYogaNode_should_set_aspectRatio_as_1() {
        // Given
        val style = Style(
            size = Size(aspectRatio = ONE_UNIT_VALUE)
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.aspectRatio = ONE_UNIT_VALUE.toFloat() }
    }

    @Test
    fun makeYogaNode_should_set_margin_as_TOP_and_1() {
        // Given
        val style = Style(
            margin = EdgeValue(top = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMargin(YogaEdge.TOP, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_margin_as_LEFT_and_1() {
        // Given
        val style = Style(
            margin = EdgeValue(left = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMargin(YogaEdge.LEFT, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_margin_as_RIGHT_and_1() {
        // Given
        val style = Style(
            margin = EdgeValue(right = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMargin(YogaEdge.RIGHT, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_margin_as_BOTTOM_and_1() {
        // Given
        val style = Style(
            margin = EdgeValue(bottom = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMargin(YogaEdge.BOTTOM, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_margin_as_START_and_1() {
        // Given
        val style = Style(
            margin = EdgeValue(left = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMargin(YogaEdge.LEFT, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_margin_as_END_and_1() {
        // Given
        val style = Style(
            margin = EdgeValue(right = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMargin(YogaEdge.RIGHT, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_margin_as_ALL_and_1() {
        // Given
        val style = Style(
            margin = EdgeValue(all = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMargin(YogaEdge.ALL, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_margin_as_VERTICAL_and_1() {
        // Given
        val style = Style(
            margin = EdgeValue(vertical = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMargin(YogaEdge.VERTICAL, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_margin_as_HORIZONTAL_and_1() {
        // Given
        val style = Style(
            margin = EdgeValue(horizontal = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMargin(YogaEdge.HORIZONTAL, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_marginPercent_as_TOP_and_1() {
        // Given
        val style = Style(
            margin = EdgeValue(top = UnitValue(ONE_UNIT_VALUE, UnitType.PERCENT))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setMarginPercent(YogaEdge.TOP, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_padding_as_TOP_and_1() {
        // Given
        val style = Style(
            padding = EdgeValue(top = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setPadding(YogaEdge.TOP, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_paddingPercent_as_TOP_and_1() {
        // Given
        val style = Style(
            padding = EdgeValue(top = UnitValue(ONE_UNIT_VALUE, UnitType.PERCENT))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setPaddingPercent(YogaEdge.TOP, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_position_as_TOP_and_1() {
        // Given
        val style = Style(
            position = EdgeValue(top = UnitValue(ONE_UNIT_VALUE, UnitType.REAL))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setPosition(YogaEdge.TOP, ONE_UNIT_VALUE.toFloat()) }
    }

    @Test
    fun makeYogaNode_should_set_positionPercent_as_TOP_and_1() {
        // Given
        val style = Style(
            position = EdgeValue(top = UnitValue(ONE_UNIT_VALUE, UnitType.PERCENT))
        )

        // When
        val yogaNode = flexMapper.makeYogaNode(style)

        // Then
        verify(exactly = once()) { yogaNode.setPositionPercent(YogaEdge.TOP, ONE_UNIT_VALUE.toFloat()) }
    }
}