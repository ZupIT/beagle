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

package br.com.zup.beagle.widget.ui

import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.ContextComponent
import br.com.zup.beagle.widget.context.ContextData

/**
 * @param context define the contextData that be set to component.
 * @param onInit allows to define a list of actions to be performed when the Widget is displayed.
 * @param dataSource it's an expression that points to a list of values used to populate the Widget.
 * @param templates Multiple templates support. The template to use will be decided according to the property `case`
 * of the template. The first template where `case` is `true` is the template chosen to render an item. If for every
 * template `case` is `false`, then, the first template where `case` is omitted (default template) is used.
 * @param onScrollEnd list of actions performed when the list is scrolled to the end.
 * @param scrollEndThreshold sets the scrolled percentage of the list to trigger onScrollEnd.
 * @param isScrollIndicatorVisible this attribute enables or disables the scroll bar.
 * @param iteratorName is the context identifier of each cell.
 * @param key points to a unique value present in each dataSource item
 * used as a suffix in the component ids within the Widget.
 * @param numColumns Defines how many columns to show.
 * @param spanCount The number of columns or rows in the grid.
 * @param direction define the grid direction.
 */
data class GridView private constructor(
    override val context: ContextData? = null,
    val onInit: List<Action>? = null,
    val dataSource: Bind<List<Any>>,
    val templates: List<Template>,
    val onScrollEnd: List<Action>? = null,
    val scrollEndThreshold: Int? = null,
    val isScrollIndicatorVisible: Boolean = false,
    val iteratorName: String = "item",
    val key: String? = null,
    val numColumns: Int? = null,
    val spanCount: Int? = null,
    val direction: GridViewDirection? = GridViewDirection.VERTICAL,
) : Widget(), ContextComponent {

    @Deprecated(message = "It was deprecated in version 1.9 and will be removed in a future version. " +
        "Use spanCount and direction instead numColumns",
        replaceWith = ReplaceWith(
            "GridView(context, onInit, dataSource, templates, onScrollEnd, scrollEndThreshold," +
                "iteratorName, key, spanCount, direction)"))
    constructor(
        context: ContextData? = null,
        onInit: List<Action>? = null,
        dataSource: Bind<List<Any>>,
        templates: List<Template>,
        onScrollEnd: List<Action>? = null,
        scrollEndThreshold: Int? = null,
        isScrollIndicatorVisible: Boolean = false,
        iteratorName: String = "item",
        key: String? = null,
        numColumns: Int,
    ) : this(
        context = context,
        onInit = onInit,
        dataSource = dataSource,
        templates = templates,
        onScrollEnd = onScrollEnd,
        scrollEndThreshold = scrollEndThreshold,
        isScrollIndicatorVisible = isScrollIndicatorVisible,
        iteratorName = iteratorName,
        key = key,
        numColumns = numColumns,
        spanCount = null,
        direction = GridViewDirection.VERTICAL,
    )

    constructor(
        context: ContextData? = null,
        onInit: List<Action>? = null,
        dataSource: Bind<List<Any>>,
        templates: List<Template>,
        onScrollEnd: List<Action>? = null,
        scrollEndThreshold: Int? = null,
        isScrollIndicatorVisible: Boolean = false,
        iteratorName: String = "item",
        key: String? = null,
        spanCount: Int,
        direction: GridViewDirection
    ) : this(
        context = context,
        onInit = onInit,
        dataSource = dataSource,
        templates = templates,
        onScrollEnd = onScrollEnd,
        scrollEndThreshold = scrollEndThreshold,
        isScrollIndicatorVisible = isScrollIndicatorVisible,
        iteratorName = iteratorName,
        key = key,
        numColumns = null,
        spanCount = spanCount,
        direction = direction,
    )
}

/**
 * The direction attribute will define the grid direction.
 *
 * @property VERTICAL
 * @property HORIZONTAL
 *
 */
enum class GridViewDirection {
    /**
     * Items list are displayed in vertical direction like LINES.
     *
     */
    VERTICAL,

    /**
     * Items list are displayed in Horizontal direction like COLUMNS.
     *
     */
    HORIZONTAL
}
