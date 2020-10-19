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
import android.view.Window
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
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.BeagleRetry
import br.com.zup.beagle.android.utils.DeprecationMessages.DEPRECATED_STATE_LOADING
import br.com.zup.beagle.android.utils.NewIntentDeprecatedConstants
import br.com.zup.beagle.android.utils.toComponent
import br.com.zup.beagle.android.view.viewmodel.BeagleViewModel
import br.com.zup.beagle.android.view.viewmodel.ViewState
import br.com.zup.beagle.core.ServerDrivenComponent
import kotlinx.android.parcel.Parcelize

sealed class ServerDrivenState {

    class FormError(throwable: Throwable, retry: BeagleRetry) : Error(throwable, retry)
    class WebViewError(throwable: Throwable, retry: BeagleRetry) : Error(throwable, retry)

    @Deprecated(DEPRECATED_STATE_LOADING)
    data class Loading(val loading: Boolean) : ServerDrivenState()

    /**
     * indicates that a server-driven component fetch has begun
     */
    object Started : ServerDrivenState()

    /**
     * indicates that a server-driven component fetch has finished
     */
    object Finished : ServerDrivenState()

    /**
     * indicates a success state while fetching a server-driven component
     */
    object Success : ServerDrivenState()

    /**
     * indicates that a server-driven component fetch has cancel
     */
    object Canceled : ServerDrivenState()

    /**
     * indicates an error state while fetching a server-driven component
     *
     * @param throwable error occurred. See {@link br.com.zup.beagle.android.exception.BeagleApiException},
     * See {@link br.com.zup.beagle.android.exception.BeagleException}
     * @param retry action to be performed when an error occurs
     */
    open class Error(val throwable: Throwable, val retry: BeagleRetry) : ServerDrivenState()
}

/**
 * ScreenRequest is used to do requests.
 *
 * @param url  Server URL.
 * @param method HTTP method.
 * @param headers Header items for the request.
 * @param body Content that will be deliver with the request.
 */
@Parcelize
data class ScreenRequest(
    val url: String,
    val method: ScreenMethod = ScreenMethod.GET,
    val headers: Map<String, String> = mapOf(),
    val body: String? = null
) : Parcelable

/**
 * Screen method to indicate the desired action to be performed for a given resource.
 *
 */
enum class ScreenMethod {
    /**
     * The GET method requests a representation of the specified resource. Requests using GET should only retrieve
     * data.
     */
    GET,

    /**
     * The POST method is used to submit an entity to the specified resource, often causing
     * a change in state or side effects on the server.
     */
    POST,

    /**
     * The PUT method replaces all current representations of the target resource with the request payload.
     */
    PUT,

    /**
     * The DELETE method deletes the specified resource.
     */
    DELETE,

    /**
     * The HEAD method asks for a response identical to that of a GET request, but without the response body.
     */
    HEAD,

    /**
     * The PATCH method is used to apply partial modifications to a resource.
     */
    PATCH
}

private val beagleSerializer: BeagleSerializer = BeagleSerializer()
private const val FIRST_SCREEN_REQUEST_KEY = "FIRST_SCREEN_REQUEST_KEY"
private const val FIRST_SCREEN_KEY = "FIRST_SCREEN_KEY"

abstract class BeagleActivity : AppCompatActivity(), OnFragmentCallback {

    private val viewModel by lazy { ViewModelProvider(this).get(BeagleViewModel::class.java) }
    private val screenRequest by lazy { intent.extras?.getParcelable<ScreenRequest>(FIRST_SCREEN_REQUEST_KEY) }
    private val screen by lazy { intent.extras?.getString(FIRST_SCREEN_KEY) }

    companion object {
        @Deprecated(
            message = NewIntentDeprecatedConstants.DEPRECATED_NEW_INTENT,
            replaceWith = ReplaceWith(
                "context.newServerDrivenIntent<YourBeagleActivity>(screenJson)",
                imports = [NewIntentDeprecatedConstants.NEW_INTENT_NEW_IMPORT]
            )
        )
        fun newIntent(context: Context, screenJson: String): Intent {
            return newIntent(context).apply {
                putExtra(FIRST_SCREEN_KEY, screenJson)
            }
        }

        @Deprecated(
            message = NewIntentDeprecatedConstants.DEPRECATED_NEW_INTENT,
            replaceWith = ReplaceWith(
                "context.newServerDrivenIntent<YourBeagleActivity>(screen)",
                imports = [NewIntentDeprecatedConstants.NEW_INTENT_NEW_IMPORT]
            )
        )
        fun newIntent(context: Context, screen: Screen): Intent {
            return newIntent(context, null, screen)
        }

        @Deprecated(
            message = NewIntentDeprecatedConstants.DEPRECATED_NEW_INTENT,
            replaceWith = ReplaceWith(
                "context.newServerDrivenIntent<YourBeagleActivity>(screenRequest)",
                imports = [NewIntentDeprecatedConstants.NEW_INTENT_NEW_IMPORT]
            )
        )
        fun newIntent(context: Context, screenRequest: ScreenRequest): Intent {
            return newIntent(context, screenRequest, null)
        }

        internal fun newIntent(
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

        fun bundleOf(screenRequest: ScreenRequest): Bundle {
            return Bundle(1).apply {
                putParcelable(FIRST_SCREEN_REQUEST_KEY, screenRequest)
            }
        }

        fun bundleOf(screenRequest: ScreenRequest, fallbackScreen: Screen): Bundle {
            return Bundle(2).apply {
                putParcelable(FIRST_SCREEN_REQUEST_KEY, screenRequest)
                putAll(bundleOf(fallbackScreen))
            }
        }

        fun bundleOf(screen: Screen): Bundle {
            return Bundle(1).apply {
                putString(FIRST_SCREEN_KEY, beagleSerializer.serializeComponent(screen.toComponent()))
            }
        }

        fun bundleOf(screenJson: String): Bundle {
            return Bundle(1).apply {
                putString(FIRST_SCREEN_KEY, screenJson)
            }
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

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onResume() {
        super.onResume()

        if (supportFragmentManager.fragments.size == 0) {
            screen?.let { screen ->
                fetch(
                    ScreenRequest(""),
                    beagleSerializer.deserializeComponent(screen)
                )
            } ?: run {
                screenRequest?.let { request -> fetch(request) }
            }
        }
    }

    override fun onBackPressed() {
        if (viewModel.isFetchComponent()) {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        } else if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun hasServerDrivenScreen(): Boolean = supportFragmentManager.backStackEntryCount > 0

    fun navigateTo(screenRequest: ScreenRequest, screen: Screen?) {
        fetch(screenRequest, screen?.toComponent())
    }

    private fun fetch(screenRequest: ScreenRequest, screenComponent: ServerDrivenComponent? = null) {
        val liveData = viewModel.fetchComponent(screenRequest, screenComponent)
        handleLiveData(liveData)
    }

    override fun fragmentResume() {
        onServerDrivenContainerStateChanged(ServerDrivenState.Success)
        onServerDrivenContainerStateChanged(ServerDrivenState.Finished)
    }

    private fun handleLiveData(state: LiveData<ViewState>) {
        state.observe(this, Observer {
            when (it) {
                is ViewState.Error -> {
                    onServerDrivenContainerStateChanged(ServerDrivenState.Error(it.throwable, it.retry))
                    onServerDrivenContainerStateChanged(ServerDrivenState.Finished)
                }

                is ViewState.Loading -> {
                    onServerDrivenContainerStateChanged(ServerDrivenState.Loading(it.value))

                    if (it.value) {
                        onServerDrivenContainerStateChanged(ServerDrivenState.Started)
                    }
                }

                is ViewState.DoCancel -> {
                    onServerDrivenContainerStateChanged(ServerDrivenState.Canceled)
                }

                is ViewState.DoRender -> {
                    showScreen(it.screenId, it.component)
                }
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