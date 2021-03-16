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

package br.com.zup.beagle.builder.core

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.core.Accessibility

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version." +
    " Use class Accessibility.", ReplaceWith("Accessibility()"))
fun accessibility(block: AccessibilityBuilder.() -> Unit)
        = AccessibilityBuilder().apply(block).build()

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version." +
    " Use class Accessibility.", ReplaceWith("Accessibility()"))
class AccessibilityBuilder: BeagleBuilder<Accessibility> {
    var accessible: Boolean = true
    var isHeader: Boolean = false
    var accessibilityLabel: String? = null

    fun isHeader(isHeader: Boolean) = this.apply { this.isHeader = isHeader }
    fun accessible(accessible: Boolean) = this.apply { this.accessible = accessible }
    fun accessibilityLabel(accessibilityLabel: String?)
            = this.apply { this.accessibilityLabel = accessibilityLabel }

    fun accessible(block: () -> Boolean) {
        accessible(block.invoke())
    }

    fun isHeader(block: () -> Boolean) {
        isHeader(block.invoke())
    }

    fun accessibilityLabel(block: () -> String?) {
        accessibilityLabel(block.invoke())
    }

    override fun build() = Accessibility(accessible, accessibilityLabel)
}