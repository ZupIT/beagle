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

package br.com.zup.beagle.expression

import br.com.zup.beagle.annotation.BeagleExpressionRoot

@BeagleExpressionRoot
data class BasicContext(val name: String)

@BeagleExpressionRoot
data class CompositeContext(val context: ComposedContext)
data class ComposedContext(val name: String)

@BeagleExpressionRoot
data class CollectionContext(val names: Collection<Int>)

@BeagleExpressionRoot
data class IterableContext(val names: Iterable<Char>)

@BeagleExpressionRoot
data class ListContext(val names: List<Boolean>)

@BeagleExpressionRoot
data class SetContext(val names: Set<Byte>)

@BeagleExpressionRoot
data class SuperIterableContext(val things: Collection<Iterable<List<Set<Any>>>>)

@BeagleExpressionRoot
data class MultiOriginContext(val first: FirstOriginContext, val second: SecondOriginContext)
data class FirstOriginContext(val target: TargetContext, val b : Double)
data class SecondOriginContext(val target: TargetContext, val c : Long)
data class TargetContext(val a: Int)

@BeagleExpressionRoot
data class OtherRootAsSubexpressionContext(val context: CompositeContext)

@BeagleExpressionRoot
data class RecursiveContext(val context: RecursiveContext?, val done: Boolean)

@BeagleExpressionRoot
data class RecursiveListContext(val contexts: List<RecursiveListContext>, val done: Boolean)

@BeagleExpressionRoot
data class CyclicContext(val cycle: Cycle?, val done: Boolean)
data class Cycle(val cyclicContext: CyclicContext)

@BeagleExpressionRoot
data class LeafContext(
    val a: Any,
    val b: Boolean,
    val c: Byte,
    val d: Char,
    val e: Int,
    val f: Long,
    val g: Float,
    val h: Double,
    val i: String
)

@BeagleExpressionRoot
data class NullableContext(val a: Int?, val b: BasicContext?, val c: List<String?>)

@BeagleExpressionRoot
data class VisibilityContext(private val a: Boolean, internal val b: Byte, protected val c: Int, val d: Long)

@BeagleExpressionRoot
data class EnumContext(val a: EnumWithoutFields, val b: EnumWithFields)
enum class EnumWithoutFields { VALUE }
enum class EnumWithFields(val value: Int) { VALUE(0) }

@BeagleExpressionRoot
class ChildContext(a: Boolean, override val b: Int, override val c: Double, val d: String) : ParentContext(a, b)
abstract class ParentContext(val a: Boolean, open val b: Int?) {
    abstract val c: Double?
}

@BeagleExpressionRoot
class GetContext {
    fun getA() = 0
    fun getB() = BasicContext("")
}

@BeagleExpressionRoot
object ObjectContext {
    val a = 0
    const val b = 1L
}

@BeagleExpressionRoot
data class EasyFalseCycleContext(val a: ContextA, val b: ContextB)
data class ContextA(val c: ContextB)
object ContextB {
    val done = true
}

@BeagleExpressionRoot
data class HardFalseCycleContext(val a: Context2, val b: Context1)
data class Context1(val c: Context2)
object Context2 {
    val done = true
}
