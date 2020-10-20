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

import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import com.facebook.yoga.YogaAlign
import com.facebook.yoga.YogaDisplay
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaFlexDirection
import com.facebook.yoga.YogaJustify
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaPositionType
import com.facebook.yoga.YogaWrap

class FlexMapper {

    fun makeYogaNode(style: Style): YogaNode = YogaNode.create().apply {
        flexDirection = makeYogaFlexDirection(style.flex?.flexDirection) ?: YogaFlexDirection.COLUMN
        wrap = makeYogaWrap(style.flex?.flexWrap) ?: YogaWrap.NO_WRAP
        justifyContent = makeYogaJustify(style.flex?.justifyContent) ?: YogaJustify.FLEX_START
        alignItems = makeYogaAlignItems(style.flex?.alignItems) ?: YogaAlign.STRETCH
        alignSelf = makeYogaAlignSelf(style.flex?.alignSelf) ?: YogaAlign.AUTO
        alignContent = makeYogaAlignContent(style.flex?.alignContent) ?: YogaAlign.FLEX_START
        if(style.flex?.flex == null) {
            flexGrow = style.flex?.grow?.toFloat() ?: 0.0f
            flexShrink = style.flex?.shrink?.toFloat() ?: 1.0f
        }
        style.flex?.flex?.toFloat()?.let { flex = it }
        display = makeYogaDisplay(style.display) ?: YogaDisplay.FLEX
        positionType = makeYogaPositionType(style.positionType) ?: YogaPositionType.RELATIVE
        applyAttributes(style, this)
    }

    private fun applyAttributes(style: Style, yogaNode: YogaNode) {
        setWidth(style.size, yogaNode)
        setHeight(style.size, yogaNode)
        setMaxWidth(style.size, yogaNode)
        setMaxHeight(style.size, yogaNode)
        setMinWidth(style.size, yogaNode)
        setMinHeight(style.size, yogaNode)
        setBasis(style.flex?.basis, yogaNode)
        setAspectRatio(style.size?.aspectRatio, yogaNode)
        setMargin(style.margin, yogaNode)
        setPadding(style.padding, yogaNode)
        setPosition(style.position, yogaNode)
    }

    private fun setWidth(size: Size?, yogaNode: YogaNode) {
        size?.width?.let { width ->
            if (width.type == UnitType.REAL) {
                yogaNode.setWidth(width.value.dp().toFloat())
            } else if (width.type == UnitType.PERCENT) {
                yogaNode.setWidthPercent(width.value.toFloat())
            }
        }
    }

    private fun setHeight(size: Size?, yogaNode: YogaNode) {
        size?.height?.let { height ->
            if (height.type == UnitType.REAL) {
                yogaNode.setHeight(height.value.dp().toFloat())
            } else if (height.type == UnitType.PERCENT) {
                yogaNode.setHeightPercent(height.value.toFloat())
            }
        }
    }

    private fun setMaxWidth(size: Size?, yogaNode: YogaNode) {
        size?.maxWidth?.let { maxWidth ->
            if (maxWidth.type == UnitType.REAL) {
                yogaNode.setMaxWidth(maxWidth.value.dp().toFloat())
            } else if (maxWidth.type == UnitType.PERCENT) {
                yogaNode.setMaxWidthPercent(maxWidth.value.toFloat())
            }
        }
    }

    private fun setMaxHeight(size: Size?, yogaNode: YogaNode) {
        size?.maxHeight?.let { maxHeight ->
            if (maxHeight.type == UnitType.REAL) {
                yogaNode.setMaxHeight(maxHeight.value.dp().toFloat())
            } else if (maxHeight.type == UnitType.PERCENT) {
                yogaNode.setMaxHeightPercent(maxHeight.value.toFloat())
            }
        }
    }

    private fun setMinWidth(size: Size?, yogaNode: YogaNode) {
        size?.minWidth?.let { minWidth ->
            if (minWidth.type == UnitType.REAL) {
                yogaNode.setMinWidth(minWidth.value.dp().toFloat())
            } else if (minWidth.type == UnitType.PERCENT) {
                yogaNode.setMinWidthPercent(minWidth.value.toFloat())
            }
        }
    }

    private fun setMinHeight(size: Size?, yogaNode: YogaNode) {
        size?.minHeight?.let { minHeight ->
            if (minHeight.type == UnitType.REAL) {
                yogaNode.setMinHeight(minHeight.value.dp().toFloat())
            } else if (minHeight.type == UnitType.PERCENT) {
                yogaNode.setMinHeightPercent(minHeight.value.toFloat())
            }
        }
    }

    private fun setAspectRatio(aspectRatio: Double?, yogaNode: YogaNode) {
        aspectRatio?.let {
            yogaNode.aspectRatio = aspectRatio.toFloat()
        }
    }

    private fun setBasis(basis: UnitValue?, yogaNode: YogaNode) {
        when (basis?.type) {
            UnitType.REAL -> yogaNode.setFlexBasis(basis.value.toFloat())
            UnitType.PERCENT -> yogaNode.setFlexBasisPercent(basis.value.toFloat())
            else -> yogaNode.setFlexBasisAuto()
        }
    }

    private fun setMargin(margin: EdgeValue?, yogaNode: YogaNode) {
        applyEdgeValue(margin) { yogaEdge, unitValue ->
            if (unitValue.type == UnitType.REAL) {
                yogaNode.setMargin(yogaEdge, unitValue.value.dp().toFloat())
            } else if (unitValue.type == UnitType.PERCENT) {
                yogaNode.setMarginPercent(yogaEdge, unitValue.value.toFloat())
            }
        }
    }

    private fun setPadding(padding: EdgeValue?, yogaNode: YogaNode) {
        applyEdgeValue(padding) { yogaEdge, unitValue ->
            if (unitValue.type == UnitType.REAL) {
                yogaNode.setPadding(yogaEdge, unitValue.value.dp().toFloat())
            } else if (unitValue.type == UnitType.PERCENT) {
                yogaNode.setPaddingPercent(yogaEdge, unitValue.value.toFloat())
            }
        }
    }

    private fun setPosition(position: EdgeValue?, yogaNode: YogaNode) {
        applyEdgeValue(position) { yogaEdge, unitValue ->
            if (unitValue.type == UnitType.REAL) {
                yogaNode.setPosition(yogaEdge, unitValue.value.dp().toFloat())
            } else if (unitValue.type == UnitType.PERCENT) {
                yogaNode.setPositionPercent(yogaEdge, unitValue.value.toFloat())
            }
        }
    }

    private fun applyEdgeValue(
        edgeValue: EdgeValue?,
        finish: (yogaEdge: YogaEdge, unitValue: UnitValue) -> Unit
    ) {
        edgeValue?.top?.let {
            finish(YogaEdge.TOP, it)
        }
        edgeValue?.left?.let {
            finish(YogaEdge.LEFT, it)
        }
        edgeValue?.right?.let {
            finish(YogaEdge.RIGHT, it)
        }
        edgeValue?.bottom?.let {
            finish(YogaEdge.BOTTOM, it)
        }
        edgeValue?.vertical?.let {
            finish(YogaEdge.VERTICAL, it)
        }
        edgeValue?.horizontal?.let {
            finish(YogaEdge.HORIZONTAL, it)
        }
        edgeValue?.all?.let {
            finish(YogaEdge.ALL, it)
        }
    }
}
