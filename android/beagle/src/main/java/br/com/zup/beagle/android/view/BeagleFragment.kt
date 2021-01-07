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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.zup.beagle.android.components.utils.applyBackgroundFromWindowBackgroundTheme
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.android.view.viewmodel.AnalyticsViewModel
import br.com.zup.beagle.android.view.viewmodel.BeagleScreenViewModel
import br.com.zup.beagle.android.widget.UndefinedWidget
import br.com.zup.beagle.core.ServerDrivenComponent

internal class BeagleFragment : Fragment() {

    private val screen: ServerDrivenComponent by lazy {
        val json = arguments?.getString(JSON_SCREEN_KEY) ?: beagleSerializer.serializeComponent(UndefinedWidget())
        beagleSerializer.deserializeComponent(json)
    }
    private val isLocalScreen by lazy {
        arguments?.getString(IS_LOCAL_SCREEN_KEY)
    }
    private val screenIdentifier by lazy {
        arguments?.getString(SCREEN_IDENTIFIER_KEY)
    }

    private val screenViewModel by lazy { ViewModelProvider(requireActivity()).get(BeagleScreenViewModel::class.java) }
    private val analyticsViewModel by lazy { ViewModelProvider(requireActivity()).get(AnalyticsViewModel::class.java) }

    companion object {

        @JvmStatic
        fun newInstance(
            component: ServerDrivenComponent,
            isLocalScreen: Boolean? = null,
            screenIdentifier: String? = null
        ) = newInstance(
            beagleSerializer.serializeComponent(component),
            isLocalScreen,
            screenIdentifier
        )

        @JvmStatic
        fun newInstance(
            json: String,
            isLocalScreen: Boolean? = null,
            screenIdentifier: String? = null
        ): BeagleFragment = BeagleFragment().apply {
            val bundle = Bundle()
            bundle.putString(JSON_SCREEN_KEY, json)
            isLocalScreen?.let {
                bundle.putString(IS_LOCAL_SCREEN_KEY, isLocalScreen.toString())
            }
            bundle.putString(SCREEN_IDENTIFIER_KEY, screenIdentifier)
            arguments = bundle
        }

        private val beagleSerializer: BeagleSerializer = BeagleSerializer()
        private const val JSON_SCREEN_KEY = "JSON_SCREEN_KEY"
        private const val IS_LOCAL_SCREEN_KEY = "IS_LOCAL_SCREEN_KEY"
        private const val SCREEN_IDENTIFIER_KEY = "SCREEN_IDENTIFIER_KEY"

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenViewModel.onScreenLoadFinished()
        isLocalScreen?.let { isLocalScreen ->
            screenIdentifier?.let { screenIdentifier ->
                analyticsViewModel.createScreenReport(isLocalScreen.toBoolean(), screenIdentifier)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.let {
            FrameLayout(it).apply {
                applyBackgroundFromWindowBackgroundTheme(it)
                addView(screen.toView(this@BeagleFragment, screenIdentifier = screenIdentifier))
            }
        }
    }
}