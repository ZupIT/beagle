// THIS IS A GENERATED FILE. DO NOT EDIT THIS
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
package br.com.zup.beagle.widget.core

import br.com.zup.beagle.core.BindAttribute
import br.com.zup.beagle.action.ui.ActionSchema

interface SendRequestSchema {
    public val url: BindAttribute<String>
    public val method: BindAttribute<HTTPMethod>?
    public val data: Any?
    public val headers: BindAttribute<Map<String, String>>?
    public val onSuccess: List<ActionSchema>?
    public val onError: List<ActionSchema>?
    public val onFinish: List<ActionSchema>?
}

enum class HTTPMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE
}
