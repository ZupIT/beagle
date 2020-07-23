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

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel

fun Fragment.setGlobalContext(path: String? = null, value: Any) {
    GlobalContext.set(path, value, this)
}

fun AppCompatActivity.setGlobalContext(path: String? = null, value: Any) {
    GlobalContext.set(path, value, this)
}

fun Fragment.clearGlobalContext(path: String? = null) {
    GlobalContext.clear(path, this)
}

fun AppCompatActivity.clearGlobalContext(path: String? = null) {
    GlobalContext.clear(path, this)
}

fun Fragment.getGlobalContext(path: String? = null): Any? {
    return GlobalContext.get(path, this)
}

fun AppCompatActivity.getGlobalContext(path: String? = null): Any? {
    return GlobalContext.get(path, this)
}

object GlobalContext : GlobalContextAPI {

    data class Casa(val endereco: String, val numero: String)

    var globalContext = ContextData(id = "global", value = Casa("Rua Joao Balbino", numero = "1153"))

    override fun get(path: String?, fragment: Fragment): Any? {

        val newPath = if (path != null && !path.startsWith("[")) {
            ".$path"
        } else {
            path
        }
        return ContextDataEvaluation().evaluateBindExpression(globalContext, expressionOf<String>("@{global$newPath}"))
    }

    override fun get(path: String?, activity: AppCompatActivity): Any {
        ActivityRootView(activity).generateViewModelInstance<ScreenContextViewModel>()

        return globalContext.value
    }

    override fun set(path: String?, value: Any, fragment: Fragment) {
        SetContext(
            contextId = "global",
            value = value,
            path = path
        ).execute(FragmentRootView(fragment), View(fragment.context))
    }

    override fun set(path: String?, value: Any, activity: AppCompatActivity) {
        SetContext(
            contextId = "global",
            value = value,
            path = path
        ).execute(ActivityRootView(activity), View(activity.applicationContext))
    }

    override fun clear(path: String?, fragment: Fragment) {
        if (path == null) {
            FragmentRootView(fragment).generateViewModelInstance<ScreenContextViewModel>().clearContextId(globalContext.id)
        }
    }

    override fun clear(path: String?, activity: AppCompatActivity) {
        if (path == null) {
            ActivityRootView(activity).generateViewModelInstance<ScreenContextViewModel>().clearContextId(globalContext.id)
        }
    }
}
