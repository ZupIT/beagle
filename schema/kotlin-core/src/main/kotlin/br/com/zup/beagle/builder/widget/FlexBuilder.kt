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

package br.com.zup.beagle.builder.widget

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.FlexWrap
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitValue

fun flex(block: FlexBuilder.() -> Unit) = FlexBuilder().apply(block).build()

class FlexBuilder : BeagleBuilder<Flex> {
    var flexDirection: FlexDirection? = null
    var flexWrap: FlexWrap? = null
    var justifyContent: JustifyContent? = null
    var alignItems: AlignItems? = null
    var alignSelf: AlignSelf? = null
    var alignContent: AlignContent? = null
    var basis: UnitValue? = null
    var flex: Double? = null
    var grow: Double? = null
    var shrink: Double? = null

    fun flexDirection(flexDirection: FlexDirection?) = this.apply { this.flexDirection = flexDirection }
    fun flexWrap(flexWrap: FlexWrap?) = this.apply { this.flexWrap = flexWrap }
    fun justifyContent(justifyContent: JustifyContent?) = this.apply { this.justifyContent = justifyContent }
    fun alignItems(alignItems: AlignItems?) = this.apply { this.alignItems = alignItems }
    fun alignSelf(alignSelf: AlignSelf?) = this.apply { this.alignSelf = alignSelf }
    fun alignContent(alignContent: AlignContent?) = this.apply { this.alignContent = alignContent }
    fun basis(basis: UnitValue?) = this.apply { this.basis = basis }
    fun flex(flex: Double?) = this.apply { this.flex = flex }
    fun grow(grow: Double?) = this.apply { this.grow = grow }
    fun shrink(shrink: Double?) = this.apply { this.shrink = shrink }

    fun flexDirection(block: () -> FlexDirection?) {
        flexDirection(block.invoke())
    }

    fun flexWrap(block: () -> FlexWrap?) {
        flexWrap(block.invoke())
    }

    fun justifyContent(block: () -> JustifyContent?) {
        justifyContent(block.invoke())
    }

    fun alignItems(block: () -> AlignItems?) {
        alignItems(block.invoke())
    }

    fun alignSelf(block: () -> AlignSelf?) {
        alignSelf(block.invoke())
    }

    fun alignContent(block: () -> AlignContent?) {
        alignContent(block.invoke())
    }

    fun basis(block: () -> UnitValue?) {
        basis(block.invoke())
    }

    fun flex(block: () -> Double?) {
        flex(block.invoke())
    }

    fun grow(block: () -> Double?) {
        grow(block.invoke())
    }

    fun shrink(block: () -> Double?) {
        shrink(block.invoke())
    }

    override fun build() = Flex(
        flexDirection = flexDirection,
        flexWrap = flexWrap,
        justifyContent = justifyContent,
        alignItems = alignItems,
        alignSelf = alignSelf,
        alignContent = alignContent,
        basis = basis,
        flex = flex,
        grow = grow,
        shrink = shrink
    )
}

fun size(block: SizeBuilder.() -> Unit) = SizeBuilder().apply(block).build()

class SizeBuilder : BeagleBuilder<Size> {
    var width: UnitValue? = null
    var height: UnitValue? = null
    var maxWidth: UnitValue? = null
    var maxHeight: UnitValue? = null
    var minWidth: UnitValue? = null
    var minHeight: UnitValue? = null
    var aspectRatio: Double? = null

    fun width(width: UnitValue?) = this.apply { this.width = width }
    fun height(height: UnitValue?) = this.apply { this.height = height }
    fun maxWidth(maxWidth: UnitValue?) = this.apply { this.maxWidth = maxWidth }
    fun maxHeight(maxHeight: UnitValue?) = this.apply { this.maxHeight = maxHeight }
    fun minWidth(minWidth: UnitValue?) = this.apply { this.minWidth = minWidth }
    fun minHeight(minHeight: UnitValue?) = this.apply { this.minHeight = minHeight }
    fun aspectRatio(aspectRatio: Double?) = this.apply { this.aspectRatio = aspectRatio }

    fun width(block: () -> UnitValue?) {
        width(block.invoke())
    }

    fun height(block: () -> UnitValue?) {
        height(block.invoke())
    }

    fun maxWidth(block: () -> UnitValue?) {
        maxWidth(block.invoke())
    }

    fun maxHeight(block: () -> UnitValue?) {
        maxHeight(block.invoke())
    }

    fun minWidth(block: () -> UnitValue?) {
        minWidth(block.invoke())
    }

    fun minHeight(block: () -> UnitValue?) {
        minHeight(block.invoke())
    }

    fun aspectRatio(block: () -> Double?) {
        aspectRatio(block.invoke())
    }


    override fun build() = Size(
        width = width,
        height = height,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        minWidth = minWidth,
        minHeight = minHeight,
        aspectRatio = aspectRatio
    )
}

fun edgeValue(block: EdgeValueBuilder.() -> Unit) = EdgeValueBuilder().apply(block).build()

class EdgeValueBuilder : BeagleBuilder<EdgeValue> {
    var left: UnitValue? = null
    var top: UnitValue? = null
    var right: UnitValue? = null
    var bottom: UnitValue? = null
    var horizontal: UnitValue? = null
    var vertical: UnitValue? = null
    var all: UnitValue? = null

    fun left(left: UnitValue?) = this.apply { this.left = left }
    fun top(top: UnitValue?) = this.apply { this.top = top }
    fun right(right: UnitValue?) = this.apply { this.right = right }
    fun bottom(bottom: UnitValue?) = this.apply { this.bottom = bottom }
    fun horizontal(horizontal: UnitValue?) = this.apply { this.horizontal = horizontal }
    fun vertical(vertical: UnitValue?) = this.apply { this.vertical = vertical }
    fun all(all: UnitValue?) = this.apply { this.all = all }

    fun left(block: () -> UnitValue?) {
        left(block.invoke())
    }

    fun top(block: () -> UnitValue?) {
        top(block.invoke())
    }

    fun right(block: () -> UnitValue?) {
        right(block.invoke())
    }

    fun bottom(block: () -> UnitValue?) {
        bottom(block.invoke())
    }

    fun horizontal(block: () -> UnitValue?) {
        horizontal(block.invoke())
    }

    fun vertical(block: () -> UnitValue?) {
        vertical(block.invoke())
    }

    fun all(block: () -> UnitValue?) {
        all(block.invoke())
    }


    override fun build() = EdgeValue(
        left = left,
        top = top,
        right = right,
        bottom = bottom,
        horizontal = horizontal,
        vertical = vertical,
        all = all
    )
}