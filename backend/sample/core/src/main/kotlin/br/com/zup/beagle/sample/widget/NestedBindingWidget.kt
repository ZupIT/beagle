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

package br.com.zup.beagle.sample.widget

import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.expression.BindingExpr
import br.com.zup.beagle.widget.Widget

@RegisterWidget
data class NestedBindingWidget(
    val abc: BindingExpr<Int>,
    val abd: BindingExpr<String>,
    val abe0f: BindingExpr<String>,
    val abg0: BindingExpr<Any>,
    val abg1: BindingExpr<Any>,
    val abg2: BindingExpr<Any>
) : Widget()