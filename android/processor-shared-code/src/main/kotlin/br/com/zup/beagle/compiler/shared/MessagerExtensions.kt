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

package br.com.zup.beagle.compiler.shared

import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

fun Messager.error(message: String, vararg args: Any) {
    printMessage(Diagnostic.Kind.ERROR, null, message, args)
}

fun Messager.error(element: Element, message: String, vararg args: Any) {
    printMessage(Diagnostic.Kind.ERROR, element, message, args)
}

fun Messager.warning(element: Element, message: String, vararg args: Any) {
    printMessage(Diagnostic.Kind.WARNING, element, message, args)
}

fun Messager.warning(message: String, vararg args: Any) {
    printMessage(Diagnostic.Kind.WARNING, null, message, args)
}

private fun Messager.printMessage(kind: Diagnostic.Kind, element: Element?, message: String, vararg args: Any) {
    var msg = message
    if (args.isNotEmpty()) {
        msg = String.format(msg, args)
    }

    if (element == null) {
        this.printMessage(kind, msg)
    } else {
        this.printMessage(kind, msg, element)
    }
}