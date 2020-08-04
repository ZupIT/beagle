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

package br.com.zup.beagle.builder

@BeagleDsl
interface BeagleBuilder<T> {

    fun <F> listBuilder(block: BeagleListBuilder<F>.() -> Unit)
            = BeagleListBuilder<F>().apply(block).build()

    fun <F> listBuilderNullable(block: BeagleListBuilder<F>.() -> Unit)
            = BeagleListBuilder<F>().apply(block).buildNullable()

    fun <K : Any, F> mapBuilder(block: BeagleMapBuilder<K, F>.() -> Unit)
            = BeagleMapBuilder<K, F>().apply(block).build()

    fun <K : Any, F> mapBuilderNullable(block: BeagleMapBuilder<K, F>.() -> Unit)
            = BeagleMapBuilder<K, F>().apply(block).buildNullable()

    fun build(): T
}

class BeagleListBuilder<T> {
    private var list: MutableList<T>? = null

    fun list(list: List<T>?) {
        if (list != null) addList(list)
    }

    fun entry(value: T?) {
        if (value != null) addEntry(value)
    }

    fun list(block: () -> List<T>?) {
        list(block.invoke())
    }

    fun entry(block: () -> T?) {
        entry(block.invoke())
    }

    operator fun T.unaryPlus() {
        entry(this)
    }

    operator fun List<T>.unaryPlus() {
        list(this)
    }

    fun build() = list?.toList()
        ?: throw IllegalStateException("Assigning a nullable value to a non nullable list field")

    fun buildNullable() = list?.toList()

    private fun addEntry(value: T) {
        checkList()
        this.list!!.add(value)
    }

    private fun addList(list: List<T>) {
        checkList()
        this.list!!.addAll(list)
    }

    private fun checkList() {
        if (this.list.isNullOrEmpty()) this.list = mutableListOf()
    }
}

class BeagleMapBuilder<T : Any, F> {
    private var map: MutableMap<T, F>? = null

    fun entry(pair: Pair<T, F>?) {
        if (pair != null) addEntry(pair)
    }

    fun entry(key: T, value: F) {
        addEntry(Pair(key, value))
    }

    fun map(map: Map<T, F>?) {
        if (map != null) addMap(map)
    }

    fun entry(block: () -> Pair<T, F>?) {
        entry(block.invoke())
    }

    fun map(block: () -> Map<T, F>?) {
        map(block.invoke())
    }

    operator fun Pair<T, F>.unaryPlus() {
        entry(this)
    }

    operator fun Map<T, F>.unaryPlus() {
        map(this)
    }

    fun build() = map?.toMap()
        ?: throw IllegalStateException("Assigning a nullable value to a non nullable map field")

    fun buildNullable() = map?.toMap()

    private fun addEntry(pair: Pair<T, F>) {
        checkMap()
        this.map!![pair.first] = pair.second
    }

    private fun addMap(map: Map<T, F>) {
        checkMap()
        this.map!!.putAll(map)
    }

    private fun checkMap() {
        if (this.map == null) this.map = mutableMapOf()
    }
}