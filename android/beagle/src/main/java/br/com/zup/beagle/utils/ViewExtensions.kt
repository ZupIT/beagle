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

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.zup.beagle.core.AppearanceComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.data.BeagleViewModel
import br.com.zup.beagle.data.serializer.BeagleSerializer
import br.com.zup.beagle.engine.renderer.ActivityRootView
import br.com.zup.beagle.engine.renderer.FragmentRootView
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.view.BeagleImageView
import br.com.zup.beagle.view.BeagleView
import br.com.zup.beagle.view.ScreenRequest
import br.com.zup.beagle.view.StateChangedListener
import br.com.zup.beagle.view.ViewFactory

internal var viewExtensionsViewFactory = ViewFactory()
internal var styleManagerFactory = StyleManager()
internal var beagleSerializerFactory = BeagleSerializer()
const val FLOAT_ZERO = 0.0f

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

internal fun View.hideKeyboard() {
    val activity = context as AppCompatActivity
    val view = activity.currentFocus ?: viewExtensionsViewFactory.makeView(activity)
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

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

private fun <T> isAssignableFrom(
    viewGroup: View,
    type: Class<T>
) = viewGroup.tag != null && type.isAssignableFrom(viewGroup.tag.javaClass)

internal inline fun <reified T> ViewGroup.findChildViewForType(type: Class<T>): MutableList<View> {
    val elementList = mutableListOf<View>()

    findChildViewForType(this, elementList, type)

    return elementList
}

internal fun RootView.generateViewModelInstance(): BeagleViewModel {
    return when (this) {
        is ActivityRootView -> {
            val activity = this.activity
            ViewModelProvider(activity).get(BeagleViewModel::class.java)
        }
        else -> {
            val fragment = (this as FragmentRootView).fragment
            ViewModelProvider(fragment).get(BeagleViewModel::class.java)
        }
    }
}

internal fun View.applyAppearance(component: ServerDrivenComponent) {
    (component as? AppearanceComponent)?.let {
        if (it.appearance?.backgroundColor != null) {
            this.background = GradientDrawable()
            applyBackgroundColor(it)
            applyCornerRadius(it)
        } else {
            val backgroundColor: Int? = styleManagerFactory.getBackgroundColor(
                context = context,
                component = component,
                view = this
            )

            if (backgroundColor != null) {
                this.background = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(backgroundColor, backgroundColor)
                )

                applyCornerRadius(it)
            }
        }
    }
}

internal fun View.applyBackgroundColor(appearanceWidget: AppearanceComponent) {
    appearanceWidget.appearance?.backgroundColor?.let {
        (this.background as? GradientDrawable)?.setColor(it.toAndroidColor())
    }
}

internal fun String.toAndroidColor(): Int {
    val hexColor = if (this.startsWith("#")) this else "#$this"
    return Color.parseColor(hexColor)
}

internal fun View.applyCornerRadius(appearanceWidget: AppearanceComponent) {
    appearanceWidget.appearance?.cornerRadius?.let { cornerRadius ->
        if (cornerRadius.radius > FLOAT_ZERO) {
            (this as? BeagleImageView)?.cornerRadius = cornerRadius.radius.toFloat()
            (this.background as? GradientDrawable)?.cornerRadius = cornerRadius.radius.toFloat()
        }
    }
}

internal fun Canvas.applyRadius(radius: Float) {
    if (radius > FLOAT_ZERO) {
        val path = Path()
        val rect = RectF(FLOAT_ZERO, FLOAT_ZERO, this.width.toFloat(), this.height.toFloat())
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        this.clipPath(path)
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
