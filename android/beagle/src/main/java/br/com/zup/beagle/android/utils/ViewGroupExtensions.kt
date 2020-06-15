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

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.components.utils.viewExtensionsViewFactory
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.engine.renderer.FragmentRootView
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.android.view.custom.OnStateChanged
import br.com.zup.beagle.android.widget.RootView

internal var beagleSerializerFactory = BeagleSerializer()

fun ViewGroup.loadView(activity: AppCompatActivity, screenRequest: ScreenRequest, listener: OnStateChanged? = null) {
    loadView(this, ActivityRootView(activity), screenRequest, listener)
}

fun ViewGroup.loadView(fragment: Fragment, screenRequest: ScreenRequest, listener: OnStateChanged? = null) {
    loadView(this, FragmentRootView(fragment), screenRequest, listener)
}

private fun loadView(
    viewGroup: ViewGroup,
    rootView: RootView,
    screenRequest: ScreenRequest,
    listener: OnStateChanged?
) {
    val view = viewExtensionsViewFactory.makeBeagleView(viewGroup.context).apply {
        loadView(rootView, screenRequest)
        stateChangedListener = listener
    }
    view.loadCompletedListener = {
        viewGroup.addView(view)
    }
}

private fun <T> isAssignableFrom(
    viewGroup: View,
    type: Class<T>
) = viewGroup.tag != null && type.isAssignableFrom(viewGroup.tag.javaClass)

private fun <T> findChildViewForType(
    viewGroup: ViewGroup,
    elementList: MutableList<View>,
    type: Class<T>
) {

    if (isAssignableFrom(viewGroup, type))
        elementList.add(viewGroup)

    viewGroup.children.forEach { childView ->
        when {
            childView is ViewGroup -> findChildViewForType(childView, elementList, type)
            isAssignableFrom(childView, type) -> {
                elementList.add(childView)
            }
        }
    }
}

internal inline fun <reified T> ViewGroup.findChildViewForType(type: Class<T>): MutableList<View> {
    val elementList = mutableListOf<View>()

    findChildViewForType(this, elementList, type)

    return elementList
}

fun ViewGroup.renderScreen(context: Context, screenJson: String) {
    removeAllViewsInLayout()
    addView(beagleSerializerFactory.deserializeComponent(screenJson).toView(context))
}
