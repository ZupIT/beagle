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

package br.com.zup.beagle.android.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import br.com.zup.beagle.action.Route
import br.com.zup.beagle.android.logger.BeagleLogger
import br.com.zup.beagle.android.setup.BeagleEnvironment
import java.lang.Exception

internal object BeagleNavigator {

    fun openExternalURL(context: Context, url: String) {
        try {
            val webPage: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            context.startActivity(intent)
        } catch (e: Exception) {
            BeagleLogger.error(e.toString())
        }
    }

    fun openNativeRoute(context: Context, route: String, data: Map<String, String>?, shouldResetApplication: Boolean) {
        BeagleEnvironment.beagleSdk.deepLinkHandler?.getDeepLinkIntent(route, data, shouldResetApplication)?.let {
            context.startActivity(it)
        }
    }

    fun pushStack(context: Context, route: Route) {
        context.startActivity(generateIntent(context, route))
    }

    fun popStack(context: Context) {
        if (context is Activity) {
            context.finish()
        }
    }

    fun pushView(context: Context, route: Route) {
        if (context is BeagleActivity) {
            when (route) {
                is Route.Remote -> context.navigateTo(ScreenRequest(route.route), route.fallback)
                is Route.Local -> context.navigateTo(ScreenRequest(""), route.screen)
            }
        } else {
            context.startActivity(generateIntent(context, route))
        }
    }

    fun popView(context: Context) {
        val f = (context as? FragmentActivity)?.supportFragmentManager?.fragments?.lastOrNull {
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

    fun popToView(context: Context, route: String) {
        if (context is AppCompatActivity) {
            context.supportFragmentManager.popBackStack(route, 0)
        }
    }

    fun resetApplication(context: Context, route: Route) {
        context.startActivity(generateIntent(context, route).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    fun resetStack(context: Context, route: Route) {
        popStack(context)
        context.startActivity(generateIntent(context, route))
    }

    private fun generateIntent(context: Context, route: Route): Intent {
        return when (route) {
            is Route.Remote -> BeagleActivity.newIntent(context, ScreenRequest(route.route), null, route.fallback)
            is Route.Local -> BeagleActivity.newIntent(context, ScreenRequest(""), null, route.screen)
        }
    }
}
