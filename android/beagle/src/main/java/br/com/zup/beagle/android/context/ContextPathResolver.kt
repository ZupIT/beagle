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

package br.com.zup.beagle.android.context

import br.com.zup.beagle.android.jsonpath.JsonPathUtils
import org.json.JSONArray
import org.json.JSONObject
import java.util.LinkedList

class ContextPathResolver {

    fun getKeysFromPath(contextId: String, path: String): LinkedList<String> {
        val newPath = removeContextFromPath(contextId, path)
        return JsonPathUtils.splitKeys(newPath)
    }

    private fun removeContextFromPath(contextId: String, path: String): String {
        val newPath = path.replace(contextId, "")

        if (newPath.isEmpty()) {
            throw JsonPathUtils.createInvalidPathException(path)
        } else if (newPath.startsWith(".")) {
            return newPath.replaceFirst(".", "")
        }

        return newPath
    }
}
