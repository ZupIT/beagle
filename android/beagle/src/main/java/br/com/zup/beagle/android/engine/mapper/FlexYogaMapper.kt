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

import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.FlexDisplay
import br.com.zup.beagle.widget.core.FlexPositionType
import br.com.zup.beagle.widget.core.FlexWrap
import br.com.zup.beagle.widget.core.JustifyContent
import com.facebook.yoga.YogaAlign
import com.facebook.yoga.YogaDisplay
import com.facebook.yoga.YogaFlexDirection
import com.facebook.yoga.YogaJustify
import com.facebook.yoga.YogaPositionType
import com.facebook.yoga.YogaWrap


internal fun makeYogaWrap(flexWrap: FlexWrap?): YogaWrap? = when (flexWrap) {
    FlexWrap.NO_WRAP -> YogaWrap.NO_WRAP
    FlexWrap.WRAP -> YogaWrap.WRAP
    FlexWrap.WRAP_REVERSE -> YogaWrap.WRAP_REVERSE
    else -> null
}

internal fun makeYogaAlignContent(alignContent: AlignContent?): YogaAlign? =
    when (alignContent) {
        AlignContent.CENTER -> YogaAlign.CENTER
        AlignContent.FLEX_START -> YogaAlign.FLEX_START
        AlignContent.FLEX_END -> YogaAlign.FLEX_END
        AlignContent.SPACE_BETWEEN -> YogaAlign.SPACE_BETWEEN
        AlignContent.SPACE_AROUND -> YogaAlign.SPACE_AROUND
        AlignContent.STRETCH -> YogaAlign.STRETCH
        else -> null
    }

internal fun makeYogaAlignItems(alignItems: AlignItems?): YogaAlign? =
    when (alignItems) {
        AlignItems.CENTER -> YogaAlign.CENTER
        AlignItems.FLEX_START -> YogaAlign.FLEX_START
        AlignItems.FLEX_END -> YogaAlign.FLEX_END
        AlignItems.BASELINE -> YogaAlign.BASELINE
        AlignItems.STRETCH -> YogaAlign.STRETCH
        else -> null
    }

internal fun makeYogaAlignSelf(alignSelf: AlignSelf?): YogaAlign? =
    when (alignSelf) {
        AlignSelf.CENTER -> YogaAlign.CENTER
        AlignSelf.FLEX_START -> YogaAlign.FLEX_START
        AlignSelf.FLEX_END -> YogaAlign.FLEX_END
        AlignSelf.BASELINE -> YogaAlign.BASELINE
        AlignSelf.AUTO -> YogaAlign.AUTO
        AlignSelf.STRETCH -> YogaAlign.STRETCH
        else -> null
    }

internal fun makeYogaJustify(justifyContent: JustifyContent?): YogaJustify? =
    when (justifyContent) {
        JustifyContent.FLEX_START -> YogaJustify.FLEX_START
        JustifyContent.CENTER -> YogaJustify.CENTER
        JustifyContent.FLEX_END -> YogaJustify.FLEX_END
        JustifyContent.SPACE_BETWEEN -> YogaJustify.SPACE_BETWEEN
        JustifyContent.SPACE_AROUND -> YogaJustify.SPACE_AROUND
        JustifyContent.SPACE_EVENLY -> YogaJustify.SPACE_EVENLY
        else -> null
    }

internal fun makeYogaFlexDirection(flexDirection: FlexDirection?): YogaFlexDirection? =
    when (flexDirection) {
        FlexDirection.COLUMN -> YogaFlexDirection.COLUMN
        FlexDirection.ROW -> YogaFlexDirection.ROW
        FlexDirection.COLUMN_REVERSE -> YogaFlexDirection.COLUMN_REVERSE
        FlexDirection.ROW_REVERSE -> YogaFlexDirection.ROW_REVERSE
        else -> null
    }

internal fun makeYogaDisplay(display: FlexDisplay?): YogaDisplay? = when (display) {
    FlexDisplay.FLEX -> YogaDisplay.FLEX
    FlexDisplay.NONE -> YogaDisplay.NONE
    else -> null
}

internal fun makeYogaPositionType(positionType: FlexPositionType?): YogaPositionType? =
    when (positionType) {
        FlexPositionType.RELATIVE -> YogaPositionType.RELATIVE
        FlexPositionType.ABSOLUTE -> YogaPositionType.ABSOLUTE
        else -> null
    }