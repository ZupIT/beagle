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

@file:Suppress("TooManyFunctions")

package br.com.zup.beagle.android.view

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.components.utils.RoundedImageView
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.custom.BeaglePageIndicatorView
import br.com.zup.beagle.android.view.custom.BeaglePageView
import br.com.zup.beagle.android.view.custom.BeagleTabLayout
import br.com.zup.beagle.android.view.custom.BeagleView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.Style

internal class ViewFactory {

    fun makeView(context: Context) = View(context)

    fun makeBeagleView(rootView: RootView) = BeagleView(rootView)

    fun makeBeagleFlexView(rootView: RootView) = BeagleFlexView(rootView)

    fun makeBeagleFlexView(rootView: RootView, style: Style) = BeagleFlexView(rootView, style)

    fun makeScrollView(context: Context) = ScrollView(context).apply {
        isFillViewport = true
    }

    fun makeHorizontalScrollView(context: Context) = HorizontalScrollView(context).apply {
        isFillViewport = true
    }

    fun makeButton(context: Context, id: Int) = Button(ContextThemeWrapper(context, id), null, 0)

    fun makeTextView(context: Context) = TextView(context)

    fun makeTextView(context: Context, id: Int) = TextView(ContextThemeWrapper(context, id), null, 0)

    fun makeInputText(context: Context, id: Int) = EditText(ContextThemeWrapper(context, id), null, 0)

    fun makeAlertDialogBuilder(context: Context) = AlertDialog.Builder(context)

    fun makeFrameLayoutParams(width: Int, height: Int) = FrameLayout.LayoutParams(width, height)

    fun makeViewPager(context: Context) = BeaglePageView(context)

    fun makePageIndicator(context: Context) = BeaglePageIndicatorView(context)

    fun makeTabLayout(context: Context, id: Int) = BeagleTabLayout(ContextThemeWrapper(context, id), null, 0)

    //we use the context.applicationContext to prevent a crash on android 21
    fun makeWebView(context: Context) = WebView(context.applicationContext)

    fun makeImageView(context: Context, cornerRadius: Double = 0.0) = RoundedImageView(context, cornerRadius)

    fun makeRecyclerView(context: Context) = RecyclerView(context)
}
