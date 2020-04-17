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

package br.com.zup.beagle.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import br.com.zup.beagle.widget.layout.Screen

internal object BeagleNavigator {

    fun finish(context: Context) {
        if (context is Activity) {
            context.finish()
        }
    }

    fun pop(context: Context) {
        val f =
            (context as? FragmentActivity)?.supportFragmentManager?.fragments?.lastOrNull {
                it is DialogFragment
            } as DialogFragment?
        if (f != null) {
            f.dismiss()
            return
        }

        if (context is Activity) {
            context.onBackPressed()
        }
    }

    fun addScreen(context: Context, url: String?, screen: Screen? = null) {
        if (context is BeagleActivity) {
            context.navigateTo(ScreenRequest(url ?: ""), screen)
        } else {
            context.startActivity(BeagleActivity.newIntent(context, ScreenRequest(url ?: ""), screen))
        }
    }

    fun swapScreen(context: Context, url: String?, screen: Screen? = null) {
        context.startActivity(BeagleActivity.newIntent(context, ScreenRequest(url ?: ""), screen).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    fun popToScreen(context: Context, url: String) {
        if (context is AppCompatActivity) {
            context.supportFragmentManager
                .popBackStack(url, 0)
        }
    }

    fun presentScreen(context: Context, url: String?, screen: Screen? = null) {
        context.startActivity(BeagleActivity.newIntent(context, ScreenRequest(url ?: ""), screen))
    }
}