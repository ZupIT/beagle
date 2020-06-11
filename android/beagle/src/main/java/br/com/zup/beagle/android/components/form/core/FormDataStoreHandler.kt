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

package br.com.zup.beagle.android.components.form.core

class FormDataStoreHandler {
    private val groups: HashMap<String, HashMap<String, String>> = HashMap()

    fun put(group: String, key: String, value: String) {
        var groupValues = groups[group]
        if(groupValues == null) {
            groupValues = HashMap()
        }
        groupValues[key] = value
        groups[group] = groupValues
    }

    fun getAllValues(group: String): Map<String, String> {
        return groups[group] ?: HashMap()
    }

    fun clear(group: String) {
        groups[group] = HashMap()
    }

    companion object {
        val shared = FormDataStoreHandler()
    }
}
