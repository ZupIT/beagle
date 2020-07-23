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

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

interface GlobalContextAPI {

    fun get(path: String? = null, fragment: Fragment): Any?

    fun get(path: String? = null, activity: AppCompatActivity): Any?

    fun set(path: String? = null, value: Any, fragment: Fragment)

    fun set(path: String? = null, value: Any, activity: AppCompatActivity)

    fun clear(path: String? = null, fragment: Fragment)

    fun clear(path: String? = null, activity: AppCompatActivity)
}


