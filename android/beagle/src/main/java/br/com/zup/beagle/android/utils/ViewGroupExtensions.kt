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

package br.com.zup.beagle.android.utils

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.components.utils.viewExtensionsViewFactory
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.utils.DeprecationMessages.DEPRECATED_LOADING_VIEW
import br.com.zup.beagle.android.view.BeagleFragment
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.android.view.custom.OnServerStateChanged
import br.com.zup.beagle.android.view.custom.OnStateChanged
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView

internal var beagleSerializerFactory = BeagleSerializer()

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property activity that is parent of this view
 * @property screenRequest to create your request data to fetch the component
 * @property listener is called when the loading is Started, Finished and Success
 */
@JvmName("loadView2")
fun ViewGroup.loadView(
    activity: AppCompatActivity,
    screenRequest: ScreenRequest,
    listener: OnServerStateChanged? = null
) {
    loadView(this, ActivityRootView(activity, this.id), screenRequest, listener)
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property fragment that is parent of this view
 * @property screenRequest to create your request data to fetch the component
 * @property listener is called when the loading is Started, Finished and Success
 */
@JvmName("loadView2")
fun ViewGroup.loadView(
    fragment: Fragment,
    screenRequest: ScreenRequest,
    listener: OnServerStateChanged? = null
) {
    loadView(this, FragmentRootView(fragment, this.id), screenRequest, listener)
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property activity that is parent of this view
 * @property screenRequest to create your request data to fetch the component
 * @property listener is called when the loading is started and finished
 */
@Deprecated(DEPRECATED_LOADING_VIEW)
fun ViewGroup.loadView(activity: AppCompatActivity, screenRequest: ScreenRequest, listener: OnStateChanged? = null) {
    loadView(this, ActivityRootView(activity, this.id), screenRequest, listener)
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property fragment that is parent of this view
 * @property screenRequest to create your request data to fetch the component
 * @property listener is called when the loading is started and finished
 */
@Deprecated(DEPRECATED_LOADING_VIEW)
fun ViewGroup.loadView(fragment: Fragment, screenRequest: ScreenRequest, listener: OnStateChanged? = null) {
    loadView(this, FragmentRootView(fragment, this.id), screenRequest, listener)
}

@Deprecated(DEPRECATED_LOADING_VIEW)
private fun loadView(
    viewGroup: ViewGroup,
    rootView: RootView,
    screenRequest: ScreenRequest,
    listener: OnStateChanged?
) {
    val viewModel = rootView.generateViewModelInstance<GenerateIdViewModel>()
    viewModel.createIfNotExisting(rootView.getParentId())
    val view = viewExtensionsViewFactory.makeBeagleView(rootView).apply {
        stateChangedListener = listener
        loadView(screenRequest)
    }
    view.loadCompletedListener = {
        viewGroup.addView(view)
        viewModel.setViewCreated(rootView.getParentId())
    }
}

@JvmName("loadView2")
private fun loadView(
    viewGroup: ViewGroup,
    rootView: RootView,
    screenRequest: ScreenRequest,
    listener: OnServerStateChanged?
) {
    val viewModel = rootView.generateViewModelInstance<GenerateIdViewModel>()
    viewModel.createIfNotExisting(rootView.getParentId())
    val view = viewExtensionsViewFactory.makeBeagleView(rootView).apply {
        serverStateChangedListener = listener
        loadView(screenRequest)
    }
    view.loadCompletedListener = {
        viewGroup.addView(view)
        viewModel.setViewCreated(rootView.getParentId())
    }
}

/**
 * Render a ServerDrivenComponent into this ViewGroup
 * @property activity that is parent of this view.
 * Make sure to use this method if you are inside a Activity because of the lifecycle
 * @property screenJson that represents your component
 */
fun ViewGroup.renderScreen(activity: AppCompatActivity, screenJson: String) {
    this.renderScreen(ActivityRootView(activity, this.id), screenJson)
}

/**
 * Render a ServerDrivenComponent into this ViewGroup
 * @property fragment <p>that is parent of this view.
 * Make sure to use this method if you are inside a Fragment because of the lifecycle</p>
 * @property screenJson that represents your component
 */
fun ViewGroup.renderScreen(fragment: Fragment, screenJson: String) {
    this.renderScreen(FragmentRootView(fragment, this.id), screenJson)
}

internal fun ViewGroup.renderScreen(rootView: RootView, screenJson: String) {
    val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
    viewModel.clearContexts()
    val component = beagleSerializerFactory.deserializeComponent(screenJson)
    (rootView.getContext() as AppCompatActivity)
        .supportFragmentManager
        .beginTransaction()
        .replace(this.id, BeagleFragment.newInstance(component))
        .addToBackStack(null)
        .commit()
}
