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

package br.com.zup.beagle.android.components

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.Environment
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.android.view.ViewFactory
import io.mockk.*
import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import kotlin.test.assertNotNull

private const val MOCKED_URL = "http://mocked.com"

class WebViewTest : BaseComponentTest() {

    private val webView: android.webkit.WebView = mockk(relaxed = true)

    private val urlSlot = slot<String>()

    private lateinit var webViewComponent: WebView

    override fun setUp() {
        super.setUp()

        every { BeagleEnvironment.beagleSdk.config.environment } returns Environment.DEBUG
        every { webView.loadUrl(capture(urlSlot)) } just Runs
        every { anyConstructed<ViewFactory>().makeWebView(rootView.getContext()) } returns webView

        webViewComponent = WebView(MOCKED_URL)
    }

    @Test
    fun build_should_create_a_WebView_and_load_url_and_set_WebViewClient() {
        webViewComponent.buildView(rootView)

        assertEquals(MOCKED_URL, urlSlot.captured)
        assertNotNull(webView.webViewClient)
    }

    @Test
    fun webViewClient_should_notify_when_page_starts_loading() {
        val stateSlot = slot<ServerDrivenState>()
        val webViewClient = createMockedWebViewClient(stateSlot)
        webViewClient.onPageStarted(null, null, null)
        assertTrue((stateSlot.captured as ServerDrivenState.Loading).loading)
    }

    @Test
    fun webViewClient_should_notify_when_page_stops_loading() {
        val stateSlot = slot<ServerDrivenState>()
        val webViewClient = createMockedWebViewClient(stateSlot)
        webViewClient.onPageFinished(null, null)
        assertFalse((stateSlot.captured as ServerDrivenState.Loading).loading)
    }

    @Test
    fun webViewClient_should_notify_when_page_WebViewError() {
        val stateSlot = slot<ServerDrivenState>()
        val webViewClient = createMockedWebViewClient(stateSlot)
        webViewClient.onReceivedError(webView, null, null)
        (stateSlot.captured as ServerDrivenState.WebViewError).retry.invoke()
        verify(exactly = once()) { webView.reload() }
    }

    private fun createMockedWebViewClient(stateSlot: CapturingSlot<ServerDrivenState>): WebView.BeagleWebViewClient {
        val mockedActivity = mockkClass(BeagleActivity::class)
        every { mockedActivity.onServerDrivenContainerStateChanged(capture(stateSlot)) } just Runs
        return WebView.BeagleWebViewClient(mockedActivity)
    }
}