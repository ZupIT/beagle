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

package br.com.zup.beagle.android.view.mapper

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.networking.ResponseData
import org.junit.Test

class SendRequestActionMapperTest : BaseTest(){

    @Test
    fun `GIVEN response data is an integer WHEN toResponse is called THEN it should return an Int object`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "10".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data is Int)
        assert(response.data == 10)
    }

    @Test
    fun `GIVEN response data is an valid json WHEN toResponse is called THEN it should return an Any object`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "{\"result\":\"test\"}".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data is Any)
    }

    @Test
    fun `GIVEN response data is an invalid json WHEN toResponse is called THEN it should return an String object`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "{\"result\":\"test\"".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data is String)
        assert(response.data == "{\"result\":\"test\"")
    }

    @Test
    fun `GIVEN response data is an empty string WHEN toResponse is called THEN it should return an empty String object`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data is String)
        assert(response.data == "")
    }
}