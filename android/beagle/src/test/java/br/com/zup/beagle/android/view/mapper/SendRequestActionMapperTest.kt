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
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SendRequestActionMapperTest : BaseTest(){

    @Test
    fun `GIVEN response data is an valid json WHEN toResponse is called THEN it should return an object of type JSONObject`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "{\"result\":\"test\"}".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data is JSONObject)
    }

    @Test
    fun `GIVEN response data is an valid json WHEN toResponse is called THEN it should return an JSONObject with correct values`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "{\"result\":\"test\"}".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)
        val jsonObject = response.data as JSONObject

        //Then
        assertEquals("test", jsonObject.getString("result"))
    }

    @Test
    fun `GIVEN response data is an invalid json WHEN toResponse is called THEN it should return an object of type String`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "{\"result\":\"test\"".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data is String)
    }

    @Test
    fun `GIVEN response data is an valid json array WHEN toResponse is called THEN it should return an object of type JSONArray`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "[{\"result\":\"test\"}]".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data is JSONArray)
    }

    @Test
    fun `GIVEN response data is an valid json array WHEN toResponse is called THEN it should return an JSONArray with correct values`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "[{\"result\":\"test\"},{\"result\":\"test2\"}]".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)
        val jsonArray = response.data as JSONArray

        //Then
        assertEquals("test2", jsonArray.getJSONObject(1).getString("result"))
    }

    @Test
    fun `GIVEN response data is an invalid json array WHEN toResponse is called THEN it should return an object of type String`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "[{\"result\":\"test\"}".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data is String)
    }

    @Test
    fun `GIVEN response data is an invalid json WHEN toResponse is called THEN it should return a String containing this invalid json`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "{\"result\":\"test\"".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data == "{\"result\":\"test\"")
    }

    @Test
    fun `GIVEN response data is an empty string WHEN toResponse is called THEN it should return an object of type String`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data is String)
    }

    @Test
    fun `GIVEN response data is an empty string WHEN toResponse is called THEN it should return an empty String`() {
        //Given
        val responseData = ResponseData(
            statusCode = 200,
            data = "".toByteArray()
        )

        //When
        val response = SendRequestActionMapper.toResponse(responseData)

        //Then
        assert(response.data == "")
    }
}