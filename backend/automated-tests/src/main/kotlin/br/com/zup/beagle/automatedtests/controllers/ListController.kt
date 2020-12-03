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

package br.com.zup.beagle.automatedtests.controllers

import br.com.zup.beagle.automatedtests.constants.BOOK_DATABASE_BOOKS
import br.com.zup.beagle.automatedtests.constants.BOOK_DATABASE_CATEGORIES
import br.com.zup.beagle.automatedtests.constants.BOOK_DATABASE_CATEGORY
import br.com.zup.beagle.automatedtests.constants.BOOK_DATABASE_CHARACTERS
import br.com.zup.beagle.automatedtests.model.Book
import br.com.zup.beagle.automatedtests.model.BookResponse
import br.com.zup.beagle.automatedtests.model.CharacterResponse
import br.com.zup.beagle.automatedtests.model.Genre
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ListController {

    @RequestMapping(
        value = [BOOK_DATABASE_CHARACTERS],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun getCharacters(@RequestParam page: Int): CharacterResponse {
        return CharacterResponse.createMock(page)
    }

    @RequestMapping(
        value = [BOOK_DATABASE_CATEGORIES],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun getCategories(): List<Genre>? {
        return Genre.createMock()
    }

    @RequestMapping(
        value = [BOOK_DATABASE_CATEGORY],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun getCategory(@PathVariable category: String): List<Book>? {
        return Book.createMockByCategory(category)
    }

    @RequestMapping(
        value = [BOOK_DATABASE_BOOKS],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun getBooks(@RequestParam page: Int): BookResponse {
        return BookResponse.createMock(page)
    }
}