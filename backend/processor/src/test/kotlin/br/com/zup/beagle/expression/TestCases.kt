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
data class BasicModel(val name: String)

@BeagleExpressionRoot
data class CompositeModel(val model: ComposedModel)
data class ComposedModel(val name: String)

@BeagleExpressionRoot
data class CollectionModel(val names: Collection<Int>)

@BeagleExpressionRoot
data class IterableModel(val names: Iterable<Char>)

@BeagleExpressionRoot
data class ListModel(val names: List<Boolean>)

@BeagleExpressionRoot
data class SetModel(val names: Set<Byte>)

@BeagleExpressionRoot
data class SuperIterableModel(val things: Collection<Iterable<List<Set<Any>>>>)

@BeagleExpressionRoot
data class MultiOriginModel(val model: ComposedModel)

@BeagleExpressionRoot
data class DeepMultiOriginModel(val model: CompositeModel)

@BeagleExpressionRoot
data class RecursiveModel(val model: RecursiveModel?, val done: Boolean)

@BeagleExpressionRoot
data class RecursiveListModel(val models: List<RecursiveListModel>, val done: Boolean)

@BeagleExpressionRoot
data class CyclicModel(val cycle: Cycle?, val done: Boolean)
data class Cycle(val cyclicModel: CyclicModel)

@BeagleExpressionRoot
data class LeafModel(
    val a: Any,
    val b: Boolean,
    val c: Byte,
    val d: Char,
    val e: Int,
    val f: Long,
    val g: Float,
    val h: Double,
    val i: String
//    val j: BigInteger,
//    val k: BigDecimal,
//    val l: Date,
//    val m: Instant
)

@BeagleExpressionRoot
data class NullableModel(val a: Int?, val b: BasicModel?, val c: List<String?>)

@BeagleExpressionRoot
data class VisibilityModel(private val a: Boolean, internal val b: Byte, protected val c: Int, val d: Long)

@BeagleExpressionRoot
data class EnumModel(val a: EnumWithoutFields, val b: EnumWithFields)
enum class EnumWithoutFields { VALUE }
enum class EnumWithFields(val value: Int) { VALUE(0) }

@BeagleExpressionRoot
class ChildModel(a: Boolean, override val b: Int, override val c: Double, val d: String) : ParentModel(a, b)
abstract class ParentModel(val a: Boolean, open val b: Int?) {
    abstract val c: Double?
}

@BeagleExpressionRoot
class GetModel {
    fun getA() = 0
    fun getB() = BasicModel("")
}

@BeagleExpressionRoot
object ObjectModel {
    val a = 0
    const val b = 1L
}

@BeagleExpressionRoot
data class EasyFalseCycleModel(val a: ModelA, val b: ModelB)
data class ModelA(val b: ModelB)
object ModelB

@BeagleExpressionRoot
data class HardFalseCycleModel(val a: Model2, val b: Model1)
data class Model1(val b: Model2)
object Model2
