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

package br.com.zup.beagle.android.testutil

import java.lang.reflect.Field

@Suppress("UNCHECKED_CAST")
fun <T> Any.getPrivateField(fieldName: String): T {
    val field = getFieldFromHierarchy(this.javaClass, fieldName)
    return field.get(this) as T
}

fun Any.setPrivateField(fieldName: String, fieldValue: Any?) {
    val field = getFieldFromHierarchy(this.javaClass, fieldName)
    field.set(this, fieldValue)
}

private fun getFieldFromHierarchy(clazz: Class<*>, fieldName: String): Field {
    var clazzToSearch = clazz
    var field = getField(clazzToSearch, fieldName)
    while (field == null && clazzToSearch != Any::class.java && clazzToSearch != Object::class.java) {
        val superclass = clazzToSearch.superclass
        if (superclass != null) {
            clazzToSearch = superclass
            field = getField(clazzToSearch, fieldName)
            break
        }
    }
    if (field == null) {
        throw RuntimeException(
            "You want me to set value to this field: '" + fieldName +
                    "' on this class: '" + clazzToSearch.simpleName +
                    "' but this field is not declared withing hierarchy of this class!"
        )
    }
    return field
}

private fun getField(clazz: Class<*>, field: String): Field? {
    return try {
        val declaredField = clazz.getDeclaredField(field)
        declaredField.isAccessible = true
        declaredField
    } catch (e: NoSuchFieldException) {
        null
    }
}