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

package br.com.zup.beagle.pager

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.pager.PageIndicator
import kotlin.properties.Delegates

fun pageIndicator(block: PageIndicatorBuilder.() -> Unit) = PageIndicatorBuilder().apply(block).build()

class PageIndicatorBuilder : BeagleBuilder<PageIndicator> {
    var selectedColor: String by Delegates.notNull()
    var unselectedColor: String by Delegates.notNull()
    var numberOfPages: Int? = null
    var currentPage: Bind<Int>? = null

    fun selectedColor(selectedColor: String) = this.apply { this.selectedColor = selectedColor }
    fun unselectedColor(unselectedColor: String) = this.apply { this.unselectedColor = unselectedColor }
    fun numberOfPages(numberOfPages: Int?) = this.apply { this.numberOfPages = numberOfPages }
    fun currentPage(currentPage: Bind<Int>?) = this.apply { this.currentPage = currentPage }

    fun selectedColor(block: () -> String) {
        selectedColor(block.invoke())
    }

    fun unselectedColor(block: () -> String) {
        unselectedColor(block.invoke())
    }

    fun numberOfPages(block: () -> Int?) {
        numberOfPages(block.invoke())
    }

    fun currentPage(block: () -> Bind<Int>) {
        currentPage(block.invoke())
    }

    override fun build() = PageIndicator(
        selectedColor = selectedColor,
        unselectedColor = unselectedColor,
        numberOfPages = numberOfPages,
        currentPage = currentPage
    )
}