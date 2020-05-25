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

package br.com.zup.beagle.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.size
import androidx.fragment.app.Fragment
import br.com.zup.beagle.data.serializer.BeagleSerializer
import br.com.zup.beagle.engine.renderer.ActivityRootView
import br.com.zup.beagle.engine.renderer.FragmentRootView
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.utils.toView
import br.com.zup.beagle.utils.viewExtensionsViewFactory
import br.com.zup.beagle.view.BeagleView
import br.com.zup.beagle.view.ScreenRequest
import br.com.zup.beagle.view.StateChangedListener

internal var beagleSerializerFactory = BeagleSerializer()

fun ViewGroup.loadView(activity: AppCompatActivity, screenRequest: ScreenRequest) {
    loadView(this, ActivityRootView(activity), screenRequest)
}

fun ViewGroup.loadView(fragment: Fragment, screenRequest: ScreenRequest) {
    loadView(this, FragmentRootView(fragment), screenRequest)
}

private fun loadView(viewGroup: ViewGroup, rootView: RootView, screenRequest: ScreenRequest) {
    viewGroup.addView(
        viewExtensionsViewFactory.makeBeagleView(viewGroup.context).apply {
            this.loadView(rootView, screenRequest)
        }
    )
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

fun ViewGroup.setBeagleStateChangedListener(listener: StateChangedListener) {
    check(size != 0) { "Did you miss to call loadView()?" }

    val view = children.find { it is BeagleView } as? BeagleView

    if (view != null) {
        view.stateChangedListener = listener
    } else {
        throw IllegalStateException("Did you miss to call loadView()?")
    }
}
