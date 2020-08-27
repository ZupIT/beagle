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

@file:Suppress("TooManyFunctions")

package br.com.zup.beagle.android.context.tokenizer

internal fun tokenOpenBracket() = GenericToken("(", TokenType.OPEN_BRACKET)
internal fun tokenOfCloseBracket() = GenericToken(")", TokenType.CLOSE_BRACKET)
internal fun tokenOfComma() = GenericToken(",", TokenType.COMMA)
internal fun tokenOfFunctionStart(value: String) = GenericToken(value, TokenType.FUNCTION_START)
internal fun tokenOfFunction(value: String, params: List<Token>) = TokenFunction(value, params)
internal fun tokenOfBinding(value: String) = TokenBinding(value)
internal fun tokenOfNull() = TokenNull()
internal fun invalidToken() = InvalidToken()
internal fun tokenOfNumber(value: Number) = TokenNumber(value)
internal fun tokenOfBoolean(value: Boolean) = TokenBoolean(value)
internal fun tokenOfString(value: String) = TokenString(value)