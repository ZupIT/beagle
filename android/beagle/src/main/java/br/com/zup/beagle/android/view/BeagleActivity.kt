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
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.zup.beagle.R
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.BeagleRetry
import br.com.zup.beagle.android.utils.toComponent
import br.com.zup.beagle.android.view.viewmodel.BeagleViewModel
import br.com.zup.beagle.android.view.viewmodel.ViewState
import br.com.zup.beagle.core.ServerDrivenComponent
import kotlinx.android.parcel.Parcelize

sealed class ServerDrivenState {
    open class Error(val throwable: Throwable, val retry: BeagleRetry) : ServerDrivenState()
    class FormError(throwable: Throwable, retry: BeagleRetry) : Error(throwable, retry)
    class WebViewError(throwable: Throwable, retry: BeagleRetry) : Error(throwable, retry)
    data class Loading(val loading: Boolean) : ServerDrivenState()
}

@Parcelize
data class ScreenRequest(
    val url: String,
    val method: ScreenMethod = ScreenMethod.GET,
    val headers: Map<String, String> = mapOf(),
    val body: String? = null
) : Parcelable

enum class ScreenMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    PATCH
}

private val beagleSerializer: BeagleSerializer = BeagleSerializer()
private const val FIRST_SCREEN_REQUEST_KEY = "FIRST_SCREEN_REQUEST_KEY"
private const val FIRST_SCREEN_KEY = "FIRST_SCREEN_KEY"

abstract class BeagleActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(BeagleViewModel::class.java) }
    private val screenRequest by lazy { intent.extras?.getParcelable<ScreenRequest>(FIRST_SCREEN_REQUEST_KEY) }
    private val screen by lazy { intent.extras?.getString(FIRST_SCREEN_KEY) }

    companion object {
        fun newIntent(context: Context, screenJson: String): Intent {
            return newIntent(context).apply {
                putExtra(FIRST_SCREEN_KEY, screenJson)
            }
        }

        fun newIntent(context: Context, screen: Screen): Intent {
            return newIntent(context, null, screen)
        }

        fun newIntent(context: Context, screenRequest: ScreenRequest): Intent {
            return newIntent(context, screenRequest, null)
        }

        fun newIntent(
            context: Context,
            screenRequest: ScreenRequest? = null,
            screen: Screen? = null
        ): Intent {
            return newIntent(context).apply {
                screenRequest?.let {
                    putExtra(FIRST_SCREEN_REQUEST_KEY, screenRequest)
                }
                screen?.let {
                    putExtra(FIRST_SCREEN_KEY, beagleSerializer.serializeComponent(screen.toComponent()))
                }
            }
        }

        private fun newIntent(context: Context): Intent {
            val activityClass = BeagleEnvironment.beagleSdk.serverDrivenActivity
            return Intent(context, activityClass)
        }
    }

    abstract fun getToolbar(): Toolbar

    @IdRes
    abstract fun getServerDrivenContainerId(): Int

    abstract fun onServerDrivenContainerStateChanged(state: ServerDrivenState)

    open fun getFragmentTransitionAnimation() = FragmentTransitionAnimation(
        R.anim.slide_from_right,
        R.anim.none_animation,
        R.anim.none_animation,
        R.anim.slide_to_right
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onResume() {
        super.onResume()

        if (supportFragmentManager.fragments.size == 0) {
            screen?.let { screen ->
                fetch(
                    ScreenRequest(""),
                    beagleSerializer.deserializeComponent(screen) as ScreenComponent
                )
            } ?: run {
                screenRequest?.let { request -> fetch(request) }
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun navigateTo(screenRequest: ScreenRequest, screen: Screen?) {
        fetch(screenRequest, screen?.toComponent())
    }

    private fun fetch(screenRequest: ScreenRequest, screenComponent: ScreenComponent? = null) {
        val liveData = viewModel.fetchComponent(screenRequest, screenComponent)
        handleLiveData(liveData)
    }

    private fun handleLiveData(state: LiveData<ViewState>) {
        state.observe(this, Observer {
            when (it) {
              is ViewState.Error -> onServerDrivenContainerStateChanged(ServerDrivenState.Error(it.throwable,it.retry))
              is ViewState.Loading -> onServerDrivenContainerStateChanged(ServerDrivenState.Loading(it.value))
              is ViewState.DoRender -> showScreen(it.screenId, it.component)
            }
        })
    }

    private fun showScreen(screenName: String?, component: ServerDrivenComponent) {
        val transition = getFragmentTransitionAnimation()

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                transition.enter,
                transition.exit,
                transition.popEnter,
                transition.popExit
            )
            .replace(getServerDrivenContainerId(), BeagleFragment.newInstance(component))
            .addToBackStack(screenName)
            .commit()
    }
}