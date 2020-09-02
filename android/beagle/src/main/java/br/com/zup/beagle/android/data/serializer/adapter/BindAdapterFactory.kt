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

package br.com.zup.beagle.android.data.serializer.adapter

import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.isExpression
import br.com.zup.beagle.android.context.tokenizer.ExpressionToken
import br.com.zup.beagle.android.context.tokenizer.TokenParser
import br.com.zup.beagle.android.utils.getExpressions
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.Exception
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class BindAdapterFactory : JsonAdapter.Factory {

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        val rawType = Types.getRawType(type)
        if (rawType == Bind::class.java && type is ParameterizedType) {
            val subType = type.actualTypeArguments.first()
            val adapter: JsonAdapter<Any> = moshi.adapter(subType)
            return BindAdapter(adapter, subType)

        }
        return null
    }
}

private class BindAdapter(
    private val adapter: JsonAdapter<Any>,
    private val type: Type,
    private val tokenParser: TokenParser = TokenParser()
) : JsonAdapter<Bind<Any>>() {

    override fun fromJson(reader: JsonReader): Bind<Any>? {
        val expressionText = reader.peekJson().readJsonValue()
        if (expressionText != null && expressionText is String && expressionText.isExpression()) {
            reader.skipValue()
            val valueType = if (type is ParameterizedType) {
                type.rawType
            } else {
                type
            }
            val expressionTokens = expressionText.getExpressions().map { expression ->
                tokenParser.parse(expression)
            }
            return Bind.Expression(expressionTokens, expressionText, valueType)
        }

        val value = adapter.fromJson(reader)
        return if (value != null) {
            Bind.Value(value)
        } else {
            null
        }
    }

    override fun toJson(writer: JsonWriter, bind: Bind<Any>?) {
        if (bind != null) {
            if (bind is Bind.Value) {
                adapter.toJson(writer, bind.value)
            } else {
                writer.value(bind.value as String)
            }
        } else {
            writer.nullValue()
        }
    }
}