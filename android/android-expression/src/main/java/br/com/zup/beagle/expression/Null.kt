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

/**
 * This class is based on flipkart-incubator/proteus's implementation
 * link @{link https://github.com/flipkart-incubator/proteus/blob/master/proteus-core/src/main/java/com/flipkart/android/proteus/value/Null.java}
 * @author hernandazevedozup
 */
private const val NULL_STRING = "NULL"

class Null : Value() {

    /**
     * singleton for JsonNull
     *
     * @since 1.8
     */
    companion object {
        val INSTANCE = Null()
    }


    override fun copy(): Null {
        return INSTANCE
    }

    override fun toString(): String {
        return NULL_STRING
    }

    override fun getAsString(): String {
        return EMPTY
    }

    /**
     * All instances of Null have the same hash code
     * since they are indistinguishable
     */
    override fun hashCode(): Int {
        return Null::class.java.hashCode()
    }

    /**
     * All instances of JsonNull are the same
     */
    override fun equals(other: Any?): Boolean {
        return this === other || other is Null
    }
}