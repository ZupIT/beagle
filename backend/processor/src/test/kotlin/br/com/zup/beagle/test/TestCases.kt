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

package br.com.zup.beagle.test

import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.LayoutComponent
import br.com.zup.beagle.widget.Widget

@RegisterWidget
object BlankWidget : Widget()

@RegisterWidget
data class ConstructorWidget(val something: Any) : Widget()

@RegisterWidget
data class InterfaceWidget(val something: Any) : Widget(), LayoutComponent

@RegisterWidget
data class GenericsWidget(
//    override val child: ServerDrivenComponent,
    val a: List<Int>,
    val b: Map<String, Any>,
    val c: Collection<List<Set<Double>>>
) : Widget()

@RegisterWidget
class FieldOnlyWidget : Widget() {
    val a = true
    val b = 123L
    val c = "Hello"
}

@RegisterWidget
data class MixedWidget(val something: Any) : Widget() {
    val other = 10.0
}

@RegisterWidget
class TransformingWidget(name: String) : Widget() {
    val message = "Hello, $name!"
}

@RegisterWidget
class MultiConstructorWidget(val value: String) : Widget() {
    constructor(value: Number) : this(value.toString())
    constructor(vararg values: Any) : this(values.joinToString())
    constructor(value1: Boolean, value2: Long, value3: Double) : this("${if (value1) value2 else value3}")
    constructor(value1: CharSequence, value2: String = ""): this(value2 + value1)
}

@RegisterWidget
data class VisibilityWidget(private val a: Boolean, internal val b: Byte, protected val c: Int, val d: Long): Widget()