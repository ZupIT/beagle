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

package br.com.zup.beagle.android.networking

import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.CoroutineTestRule
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.EOFException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

private val BYTE_ARRAY_DATA = byteArrayOf()
private const val STATUS_CODE = 200

@ExperimentalCoroutinesApi
class HttpClientDefaultTest {

    @get:Rule
    val scope = CoroutineTestRule()

    @MockK
    private lateinit var httpURLConnection: HttpURLConnection

    @MockK
    private lateinit var uri: URI

    @MockK
    private lateinit var url: URL

    @MockK
    private lateinit var inputStream: InputStream

    private lateinit var urlRequestDispatchingDefault: HttpClientDefault

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(BeagleEnvironment)

        urlRequestDispatchingDefault = HttpClientDefault()

        mockkStatic("kotlin.io.ByteStreamsKt")

        every { uri.toURL() } returns url
        every { url.openConnection() } returns httpURLConnection
        every { httpURLConnection.requestMethod = any() } just Runs
        every { httpURLConnection.setRequestProperty(any(), any()) } just Runs
        every { httpURLConnection.disconnect() } just Runs
        every { httpURLConnection.headerFields } returns mapOf()
        every { httpURLConnection.responseCode } returns STATUS_CODE
        every { httpURLConnection.getSafeResponseCode() } returns STATUS_CODE
        every { httpURLConnection.inputStream } returns inputStream
        every { httpURLConnection.getSafeResponseMessage() } returns ""
        every { httpURLConnection.getSafeError() } returns BYTE_ARRAY_DATA
        every { inputStream.readBytes() } returns BYTE_ARRAY_DATA
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun execute_should_be_executed_successfully() = runBlockingTest {
        // Given
        val headerName = RandomData.string()
        val headerValue = RandomData.string()
        val headers = mapOf(headerName to listOf(headerValue))
        every { httpURLConnection.headerFields } returns headers

        urlRequestDispatchingDefault.execute(makeSimpleRequestData(), onSuccess = {
            assertEquals(STATUS_CODE, it.statusCode)
            assertEquals(BYTE_ARRAY_DATA, it.data)
            assertEquals(headerName, it.headers.keys.elementAt(0))
            assertEquals(headerValue, it.headers[headerName])
        }, onError = {
            fail("Test failed, should execute successfully")
        })

    }

    @Test
    fun execute_should_disconnect_after_response() = runBlockingTest {
        urlRequestDispatchingDefault.execute(makeSimpleRequestData(), onSuccess = {
            verify(exactly = once()) { httpURLConnection.disconnect() }
        }, onError = { })
    }

    @Test
    fun execute_should_set_headers() = runBlockingTest {
        // Given
        val headers = mapOf(
            Pair(RandomData.string(), RandomData.string()),
            Pair(RandomData.string(), RandomData.string())
        )
        val requestData = RequestData(
            uri = uri,
            headers = headers
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {
            // Then
            headers.forEach {
                verify(exactly = once()) { httpURLConnection.setRequestProperty(it.key, it.value) }
            }
        }, onError = {})


    }

    @Test
    fun execute_should_set_requestBody() = runBlockingTest {
        // Given
        val data = RandomData.string()
        val outputStream = mockk<OutputStream>()
        val requestData = RequestData(
            uri = uri,
            body = data,
            method = HttpMethod.POST
        )
        every { httpURLConnection.outputStream } returns outputStream
        every { outputStream.write(any<ByteArray>()) } just Runs

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {

            // Then
            verify(exactly = once()) { outputStream.write(data.toByteArray()) }
            verify(exactly = once()) {
                httpURLConnection.setRequestProperty(
                    "Content-Length",
                    data.length.toString()
                )
            }

        }, onError = {})

    }

    @Test
    fun execute_should_throw_IllegalArgumentException_when_data_is_set_for_HttpMethod_GET() = runBlockingTest {
        // Given
        val method = HttpMethod.GET
        val requestData = RequestData(
            uri = uri,
            body = RandomData.string(),
            method = method
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {}, onError = {
            // Then
            assertEquals(-1, it.statusCode)
            assertEquals(0, it.data.size)
            assertEquals(0, it.headers.size)
        })
    }

    @Test
    fun execute_should_throw_IllegalArgumentException_when_data_is_set_for_HttpMethod_DELETE() = runBlockingTest {
        // Given
        val method = HttpMethod.DELETE
        val requestData = RequestData(
            uri = uri,
            body = RandomData.string(),
            method = method
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {}, onError = {
            // Then
            assertEquals(-1, it.statusCode)
            assertEquals(0, it.data.size)
            assertEquals(0, it.headers.size)
        })
    }

    @Test
    fun execute_should_throw_IllegalArgumentException_when_data_is_set_for_HttpMethod_HEAD() = runBlockingTest {
        // Given
        val method = HttpMethod.HEAD
        val requestData = RequestData(
            uri = uri,
            body = RandomData.string(),
            method = method
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {}, onError = {
            // Then
            assertEquals(-1, it.statusCode)
            assertEquals(0, it.data.size)
            assertEquals(0, it.headers.size)
        })
    }

    @Test
    fun `GIVEN request data with invalid uri when execute request THEN it should throw exception`() = runBlockingTest {
        // Given
        every { uri.toURL() } throws IllegalArgumentException("URI is not absolute")
        val method = HttpMethod.GET
        val requestData = RequestData(
            uri = uri,
            body = RandomData.string(),
            method = method
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {}, onError = {
            // Then
            assertEquals(-1, it.statusCode)
            assertEquals(0, it.data.size)
            assertEquals(0, it.headers.size)
        })
    }

    @Test
    fun execute_should_throw_IllegalStateException_when_data_is_set_for_HttpMethod_DELETE() = runBlockingTest {
        // Given
        val method = HttpMethod.GET
        val requestData = RequestData(
            uri = uri,
            body = RandomData.string(),
            method = method
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {}, onError = {
            // Then
            assertEquals(-1, it.statusCode)
            assertEquals(0, it.data.size)
            assertEquals(0, it.headers.size)
        })
    }

    @Test
    fun execute_should_set_HttpMethod_GET() = runBlockingTest {
        // Given
        val requestData = RequestData(
            uri = uri,
            method = HttpMethod.GET
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {

            // Then
            verify(exactly = 0) {
                httpURLConnection.setRequestProperty(
                    "X-HTTP-Method-Override",
                    "GET"
                )
            }
            verify(exactly = once()) { httpURLConnection.requestMethod = "GET" }

        }, onError = {})
    }

    @Test
    fun execute_should_set_HttpMethod_POST() = runBlockingTest {
        // Given
        val requestData = RequestData(
            uri = uri,
            method = HttpMethod.POST
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {

            // Then
            verify(exactly = 0) {
                httpURLConnection.setRequestProperty(
                    "X-HTTP-Method-Override",
                    "POST"
                )
            }
            verify(exactly = once()) { httpURLConnection.requestMethod = "POST" }

        }, onError = {})

    }

    @Test
    fun execute_should_set_HttpMethod_PUT() = runBlockingTest {
        // Given
        val requestData = RequestData(
            uri = uri,
            method = HttpMethod.PUT
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {

            // Then
            verify(exactly = 0) {
                httpURLConnection.setRequestProperty(
                    "X-HTTP-Method-Override",
                    "PUT"
                )
            }
            verify(exactly = once()) { httpURLConnection.requestMethod = "PUT" }

        }, onError = {})

    }

    @Test
    fun execute_should_set_HttpMethod_DELETE() = runBlockingTest {
        // Given
        val requestData = RequestData(
            uri = uri,
            method = HttpMethod.DELETE
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {

            // Then
            verify(exactly = 0) {
                httpURLConnection.setRequestProperty(
                    "X-HTTP-Method-Override",
                    "DELETE"
                )
            }
            verify(exactly = once()) { httpURLConnection.requestMethod = "DELETE" }

        }, onError = {})

    }

    @Test
    fun execute_should_set_HttpMethod_HEAD() = runBlockingTest {
        // Given
        val requestData = RequestData(
            uri = uri,
            method = HttpMethod.HEAD
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {

            // Then
            verify(exactly = once()) {
                httpURLConnection.setRequestProperty(
                    "X-HTTP-Method-Override",
                    "HEAD"
                )
            }
            verify(exactly = once()) { httpURLConnection.requestMethod = "POST" }

        }, onError = {})

    }

    @Test
    fun execute_should_set_HttpMethod_PATCH() = runBlockingTest {
        // Given
        val requestData = RequestData(
            uri = uri,
            method = HttpMethod.PATCH
        )

        // When
        urlRequestDispatchingDefault.execute(requestData, onSuccess = {

            // Then
            verify(exactly = once()) {
                httpURLConnection.setRequestProperty(
                    "X-HTTP-Method-Override",
                    "PATCH"
                )
            }
            verify(exactly = once()) { httpURLConnection.requestMethod = "POST" }

        }, onError = {})

    }

    @Test
    fun execute_should_be_executed_with_error() {
        // Given
        val expectedData = ResponseData(statusCode = 404,
            data = BYTE_ARRAY_DATA, statusText = "error")
        val runtimeException = RuntimeException()
        every { httpURLConnection.inputStream } throws runtimeException
        every { httpURLConnection.responseCode } returns expectedData.statusCode!!
        every { httpURLConnection.responseMessage } returns expectedData.statusText
        every { httpURLConnection.errorStream } returns inputStream

        // When
        urlRequestDispatchingDefault.execute(makeSimpleRequestData(), onSuccess = {
            fail("Test failed, should execute with error")
        }, onError = {
            // Then
            assertEquals(it, expectedData)
        })

    }

    @Test
    fun execute_should_read_empty_response() = runBlockingTest {
        // Given
        val headerName = RandomData.string()
        val headerValue = RandomData.string()
        val headers = mapOf(headerName to listOf(headerValue))
        every { httpURLConnection.headerFields } returns headers
        every { inputStream.readBytes() } throws EOFException()

        urlRequestDispatchingDefault.execute(makeSimpleRequestData(), onSuccess = {
            assertEquals(STATUS_CODE, it.statusCode)
            assertTrue(it.data.isEmpty())
            assertEquals(headerName, it.headers.keys.elementAt(0))
            assertEquals(headerValue, it.headers[headerName])
        }, onError = {
            fail("Test failed, should execute successfully")
        })

    }

    private fun makeSimpleRequestData() = RequestData(uri)
}