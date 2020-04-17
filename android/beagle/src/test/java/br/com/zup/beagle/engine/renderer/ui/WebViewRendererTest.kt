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

package br.com.zup.beagle.engine.renderer.ui

import android.content.Context
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.setup.Environment
import br.com.zup.beagle.view.BeagleActivity
import br.com.zup.beagle.view.ServerDrivenState
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.ui.WebView
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockkClass
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertNotNull

class WebViewRendererTest : BaseTest() {

    private val MOCKED_URL = "http://mocked.com"

    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var rootView: RootView
    @RelaxedMockK
    private lateinit var webView: android.webkit.WebView
    @RelaxedMockK
    private lateinit var webViewWidget: WebView

    private val urlSlot = slot<String>()

    @InjectMockKs
    private lateinit var webViewRenderer: WebViewRenderer

    override fun setUp() {
        super.setUp()
        every { BeagleEnvironment.beagleSdk.config.environment } returns Environment.DEBUG
        every { webView.loadUrl(capture(urlSlot)) } just Runs
        every { viewFactory.makeWebView(context) } returns webView
        every { webViewWidget.url } returns MOCKED_URL
        every { rootView.getContext() } returns context
    }

    @Test
    fun build_should_create_a_WebView_and_load_url_and_set_WebViewClient() {
        val actual = webViewRenderer.buildView(rootView)
        assertEquals(MOCKED_URL, urlSlot.captured)
        assertNotNull(actual.webViewClient)

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

    private fun createMockedWebViewClient(stateSlot: CapturingSlot<ServerDrivenState>): WebViewRenderer.BeagleWebViewClient {
        val mockedActivity = mockkClass(BeagleActivity::class)
        every { mockedActivity.onServerDrivenContainerStateChanged(capture(stateSlot)) } just Runs
        return WebViewRenderer.BeagleWebViewClient(mockedActivity)
    }
}