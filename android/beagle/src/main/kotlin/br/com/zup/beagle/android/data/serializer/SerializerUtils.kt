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

package br.com.zup.beagle.android.data.serializer

import br.com.zup.beagle.annotation.RegisterAction
import br.com.zup.beagle.annotation.RegisterWidget
import java.util.Locale

private const val BEAGLE_NAMESPACE = "beagle"

internal fun generateNameSpaceToDefaultAction(clazz: Class<*>, name: String = ""): String {
    var typeName = name
    clazz.getAnnotation(RegisterAction::class.java)?.let {
        typeName = it.name
    }
    return createNamespace(BEAGLE_NAMESPACE, clazz, typeName)
}

internal fun generateNameSpaceToAction(appNameSpace: String, clazz: Class<*>): String {
    var typeName = ""
    clazz.getAnnotation(RegisterAction::class.java)?.let {
        typeName = it.name
    }
    return createNamespace(appNameSpace, clazz, typeName)
}


internal fun generateNameSpaceToDefaultWidget(clazz: Class<*>, name: String = ""): String {
    var typeName = name
    clazz.getAnnotation(RegisterWidget::class.java)?.let {
        typeName = it.name
    }
    return createNamespace(BEAGLE_NAMESPACE, clazz, typeName)
}

internal fun generateNameSpaceToWidget(appNameSpace: String, clazz: Class<*>): String {
    var typeName = ""
    clazz.getAnnotation(RegisterWidget::class.java)?.let {
        typeName = it.name
    }
    return createNamespace(appNameSpace, clazz, typeName)
}


internal fun createNamespace(appNameSpace: String, clazz: Class<*>, name: String): String {
    val typeName = if (name.isEmpty()) clazz.simpleName else name

    return "$appNameSpace:${typeName.toLowerCase(Locale.getDefault())}"
}