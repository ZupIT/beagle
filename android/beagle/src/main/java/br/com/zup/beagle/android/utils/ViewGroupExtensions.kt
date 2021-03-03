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

package br.com.zup.beagle.android.utils

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.components.utils.viewExtensionsViewFactory
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.view.BeagleFragment
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.android.view.custom.OnServerStateChanged
import br.com.zup.beagle.android.view.custom.OnStateChanged
import br.com.zup.beagle.android.view.mapper.toRequestData
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView

internal var beagleSerializerFactory = BeagleSerializer()

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property activity that is parent of this view
 * @property requestData to create your request data to fetch the component
 */
fun ViewGroup.loadView(
    activity: AppCompatActivity,
    requestData: RequestData,
) {
    loadView(
        viewGroup = this,
        rootView = ActivityRootView(activity, this.id, requestData.url ?: ""),
        requestData = requestData,
        listener = null,
        newListener = object : OnServerStateChanged {
            override fun invoke(serverState: ServerDrivenState) {}
        },
    )
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property fragment that is parent of this view
 * @property requestData to create your request data to fetch the component
 */
fun ViewGroup.loadView(
    fragment: Fragment,
    requestData: RequestData,
) {
    loadView(
        viewGroup = this,
        rootView = FragmentRootView(fragment, this.id, requestData.url ?: ""),
        requestData = requestData,
        listener = null,
        newListener = object : OnServerStateChanged {
            override fun invoke(serverState: ServerDrivenState) {}
        },
    )
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property activity that is parent of this view
 * @property requestData to create your request data to fetch the component
 * @property listener is called when the loading is Started, Finished and Success
 */
fun ViewGroup.loadView(
    activity: AppCompatActivity,
    requestData: RequestData,
    listener: OnServerStateChanged? = null,
) {
    loadView(
        this,
        ActivityRootView(activity, this.id, requestData.url ?: ""),
        requestData,
        null,
        listener,
    )
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property fragment that is parent of this view
 * @property requestData to create your request data to fetch the component
 * @property listener is called when the loading is Started, Finished and Success
 */
fun ViewGroup.loadView(
    fragment: Fragment,
    requestData: RequestData,
    listener: OnServerStateChanged? = null,
) {
    loadView(this,
        FragmentRootView(fragment, this.id, requestData.url ?: ""),
        requestData,
        null,
        listener
    )
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property activity that is parent of this view
 * @property screenRequest to create your request data to fetch the component
 */
@Deprecated("This method was deprecated in version 1.7.0 and will be removed in a future version." +
    " Use the method with request data.",
    replaceWith = ReplaceWith("loadView(activity = activity, requestData = requestData)"))
fun ViewGroup.loadView(
    activity: AppCompatActivity,
    screenRequest: ScreenRequest,
) {
    loadView(
        viewGroup = this,
        rootView = ActivityRootView(activity, this.id, screenRequest.url),
        requestData = screenRequest.toRequestData(),
        listener = null,
        newListener = object : OnServerStateChanged {
            override fun invoke(serverState: ServerDrivenState) {}
        }
    )
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property fragment that is parent of this view
 * @property screenRequest to create your request data to fetch the component
 */
@Deprecated("This method was deprecated in version 1.7.0 and will be removed in a future version." +
    " Use the method with request data.",
    replaceWith = ReplaceWith("loadView(fragment = fragment, requestData = requestData)"))
fun ViewGroup.loadView(
    fragment: Fragment,
    screenRequest: ScreenRequest,
) {
    loadView(
        viewGroup = this,
        rootView = FragmentRootView(fragment, this.id, screenRequest.url),
        requestData = screenRequest.toRequestData(),
        listener = null,
        newListener = object : OnServerStateChanged {
            override fun invoke(serverState: ServerDrivenState) {}
        }
    )
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property activity that is parent of this view
 * @property screenRequest to create your request data to fetch the component
 * @property listener is called when the loading is Started, Finished and Success
 */
@Deprecated("This method was deprecated in version 1.7.0 and will be removed in a future version." +
    " Use the method with request data.",
    replaceWith = ReplaceWith(
        "loadView(activity = activity, requestData = requestData, listener = OnServerStateChanged)"))
@JvmName("loadView2")
fun ViewGroup.loadView(
    activity: AppCompatActivity,
    screenRequest: ScreenRequest,
    listener: OnServerStateChanged? = null,
) {
    loadView(
        this,
        ActivityRootView(activity, this.id, screenRequest.url),
        screenRequest.toRequestData(),
        null,
        listener
    )
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property fragment that is parent of this view
 * @property screenRequest to create your request data to fetch the component
 * @property listener is called when the loading is Started, Finished and Success
 */
@JvmName("loadView2")
@Deprecated("This method was deprecated in version 1.7.0 and will be removed in a future version." +
    " Use the method with request data.",
    replaceWith = ReplaceWith(
        "loadView(fragment = fragment, requestData = requestData, listener = OnServerStateChanged)"))
fun ViewGroup.loadView(
    fragment: Fragment,
    screenRequest: ScreenRequest,
    listener: OnServerStateChanged? = null,
) {
    loadView(this,
        FragmentRootView(fragment, this.id, screenRequest.url),
        screenRequest.toRequestData(),
        null,
        listener
    )
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property activity that is parent of this view
 * @property screenRequest to create your request data to fetch the component
 * @property listener is called when the loading is started and finished
 */
@Deprecated("This method was deprecated in version 1.2.0 and will be removed in a future version." +
    " Use the method with listener attribute of type ServerDrivenState instead.",
    replaceWith = ReplaceWith("loadView(activity=activity, requestData=requestData,listener=listener)"))
fun ViewGroup.loadView(
    activity: AppCompatActivity,
    screenRequest: ScreenRequest,
    listener: OnStateChanged? = null,
) {
    loadView(this,
        ActivityRootView(activity, this.id, screenRequest.url),
        screenRequest.toRequestData(),
        listener
    )
}

/**
 * Load a ServerDrivenComponent into this ViewGroup
 * @property fragment that is parent of this view
 * @property screenRequest to create your request data to fetch the component
 * @property listener is called when the loading is started and finished
 */
@Deprecated("This method was deprecated in version 1.2.0 and will be removed in a future version." +
    " Use the method with listener attribute of type ServerDrivenState instead.",
    replaceWith = ReplaceWith("loadView(fragment=fragment, requestData=requestData,listener=listener)"))
fun ViewGroup.loadView(
    fragment: Fragment,
    screenRequest: ScreenRequest,
    listener: OnStateChanged? = null,
) {
    loadView(this,
        FragmentRootView(fragment, this.id, screenRequest.url),
        screenRequest.toRequestData(),
        listener
    )
}

@Suppress("LongParameterList")
private fun loadView(
    viewGroup: ViewGroup,
    rootView: RootView,
    requestData: RequestData,
    listener: OnStateChanged? = null,
    newListener: OnServerStateChanged? = null,
    generateIdManager: GenerateIdManager = GenerateIdManager(rootView),
) {
    generateIdManager.createSingleManagerByRootViewId()
    val view = viewExtensionsViewFactory.makeBeagleView(rootView).apply {
        stateChangedListener = listener
        serverStateChangedListener = newListener
        loadView(requestData)
    }
    view.loadCompletedListener = {
        viewGroup.removeAllViews()
        viewGroup.addView(view)
    }
    view.listenerOnViewDetachedFromWindow = {
        generateIdManager.onViewDetachedFromWindow(view)
    }
}

/**
 * Render a ServerDrivenComponent into this ViewGroup
 * @property activity that is parent of this view.
 * Make sure to use this method if you are inside a Activity because of the lifecycle
 * @property screenJson that represents your component
 * @property screenId that represents an screen identifier to create the analytics when the screen is created
 * the screen is created
 */
fun ViewGroup.renderScreen(
    activity: AppCompatActivity,
    screenJson: String,
    screenId: String = "",
) {
    this.renderScreen(ActivityRootView(activity, this.id, screenId), screenJson)
}

/**
 * Render a ServerDrivenComponent into this ViewGroup
 * @property fragment <p>that is parent of this view.
 * Make sure to use this method if you are inside a Fragment bec
 * ause of the lifecycle</p>
 * @property screenJson that represents your component
 * @property screenId that represents an screen identifier to create the analytics when the screen is created
 * the screen is created
 */
fun ViewGroup.renderScreen(
    fragment: Fragment,
    screenJson: String,
    screenId: String = "",
) {
    this.renderScreen(FragmentRootView(fragment, this.id, screenId), screenJson)
}

internal fun ViewGroup.renderScreen(
    rootView: RootView,
    screenJson: String,
) {
    val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
    viewModel.clearContexts()
    val component = beagleSerializerFactory.deserializeComponent(screenJson)
    (rootView.getContext() as AppCompatActivity)
        .supportFragmentManager
        .beginTransaction()
        .replace(this.id, BeagleFragment.newInstance(component, rootView.getScreenId()))
        .addToBackStack(null)
        .commit()
}
