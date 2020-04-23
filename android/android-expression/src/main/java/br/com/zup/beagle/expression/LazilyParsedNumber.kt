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

import java.math.BigDecimal

/**
 * LazilyParsedNumber
 * This class is based on Google's implementation of
 * LazilyParsedNumber which can be found at the following
 * link @{link https://github.com/google/gson/blob/master/gson/src/main/java/com/google/gson/internal/LazilyParsedNumber.java}
 * @author hernandazevedozup
 */
internal class LazilyParsedNumber(val value: String) : Number() {
    override fun toByte(): Byte =
        this.toInt().toByte()

    override fun toChar(): Char = this.toInt().toChar()

    override fun toShort(): Short = this.toInt().toShort()

    override fun toInt(): Int {
        try {
            return Integer.parseInt(value)
        } catch (e: NumberFormatException) {
            try {
                return java.lang.Long.parseLong(value).toInt()
            } catch (nfe: NumberFormatException) {
                return BigDecimal(value).toInt()
            }

        }

    }

    override fun toLong(): Long {
        try {
            return java.lang.Long.parseLong(value)
        } catch (e: NumberFormatException) {
            return BigDecimal(value).toLong()
        }

    }

    override fun toFloat(): Float {
        return java.lang.Float.parseFloat(value)
    }

    override fun toDouble(): Double {
        return java.lang.Double.parseDouble(value)
    }

    override fun toString(): String {
        return value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is LazilyParsedNumber) {
            return value == other.value
        }
        return false;
    }
}
