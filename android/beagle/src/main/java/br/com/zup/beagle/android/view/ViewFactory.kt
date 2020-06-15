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
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.components.utils.RoundedImageView
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.custom.BeaglePageIndicatorView
import br.com.zup.beagle.android.view.custom.BeaglePageView
import br.com.zup.beagle.android.view.custom.BeagleTabLayout
import br.com.zup.beagle.android.view.custom.BeagleView
import br.com.zup.beagle.widget.core.Flex

internal class ViewFactory {

    fun makeView(context: Context) = View(context)

    fun makeBeagleView(context: Context) =
        BeagleView(context = context)

    fun makeBeagleFlexView(context: Context) =
        BeagleFlexView(context = context)

    fun makeBeagleFlexView(context: Context, flex: Flex) =
        BeagleFlexView(context = context, flex = flex)

    fun makeScrollView(context: Context) =
        ScrollView(context).apply {
            isFillViewport = true
        }

    fun makeHorizontalScrollView(context: Context) =
        HorizontalScrollView(context).apply {
            isFillViewport = true
        }

    fun makeButton(context: Context) = AppCompatButton(context)

    fun makeTextView(context: Context) = TextView(context)

    fun makeAlertDialogBuilder(context: Context) = AlertDialog.Builder(context)

    fun makeFrameLayoutParams(width: Int, height: Int) = FrameLayout.LayoutParams(width, height)

    fun makeViewPager(context: Context) = BeaglePageView(context)

    fun makePageIndicator(context: Context) = BeaglePageIndicatorView(context)

    fun makeTabLayout(context: Context) = BeagleTabLayout(context)

    fun makeWebView(context: Context) = WebView(context)

    fun makeImageView(context: Context, cornerRadius: Double = 0.0) = RoundedImageView(context, cornerRadius)

    fun makeRecyclerView(context: Context) = RecyclerView(context)
}
