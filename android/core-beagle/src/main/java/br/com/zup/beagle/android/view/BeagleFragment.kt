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

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.utils.BeagleConstants.styleManagerFactory
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.android.widget.UndefinedWidget

internal class BeagleFragment : Fragment() {

    private val screen: ServerDrivenComponent by lazy {
        val json = arguments?.getString(JSON_SCREEN_KEY) ?: beagleSerializer.serializeComponent(UndefinedWidget())
        beagleSerializer.deserializeComponent(json)
    }

    companion object {

        @JvmStatic
        fun newInstance(component: ServerDrivenComponent) = newInstance(
            beagleSerializer.serializeComponent(component)
        )

        @JvmStatic
        fun newInstance(json: String): BeagleFragment = BeagleFragment().apply {
            val bundle = Bundle()
            bundle.putString(JSON_SCREEN_KEY, json)
            arguments = bundle
        }

        private val beagleSerializer: BeagleSerializer = BeagleSerializer()
        private const val JSON_SCREEN_KEY = "JSON_SCREEN_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.let {
            FrameLayout(it).apply {
                applyBackgroundFromWindowBackgroundTheme(it)
                addView(screen.toView(this@BeagleFragment))
            }
        }
    }
}

internal fun View.applyBackgroundFromWindowBackgroundTheme(context: Context) {
    val typedValue = styleManagerFactory
        .getTypedValueByResId(android.R.attr.windowBackground, context)
    if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT &&
        typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT
    ) {
        setBackgroundColor(typedValue.data)
    } else {
        background = ContextCompat.getDrawable(context, typedValue.resourceId)
    }
}