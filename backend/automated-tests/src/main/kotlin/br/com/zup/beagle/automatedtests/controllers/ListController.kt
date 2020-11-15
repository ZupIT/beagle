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
import br.com.zup.beagle.automatedtests.model.Character
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

    @RequestMapping(value = [BOOK_DATABASE_CHARACTERS], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getCharacters(@RequestParam page: Int): CharacterResponse {
        return CharacterResponse.createMock(page)
    }

    @RequestMapping(value = [BOOK_DATABASE_CATEGORIES], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getCategories(): List<Genre>? {
        return Genre.createMock()
    }

    @RequestMapping(value = [BOOK_DATABASE_CATEGORY], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getCategory(@PathVariable category: String): List<Book>? {
        val book1 = Book(1, "Title1", "Author1", characters = Character.createMock(1))
        val book2 = Book(2, "Title2", "Author2", characters = Character.createMock(1))
        val book3 = Book(3, "Title3", "Author3", characters = Character.createMock(1))
        val list: MutableList<Book> = ArrayList()
        list.add(book1)
        list.add(book2)
        list.add(book3)
        return list
    }

    @RequestMapping(value = [BOOK_DATABASE_BOOKS], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getBooks(@RequestParam page: String): List<Book>? {
        val book1 = Book(1, "Title1", "Author1", collection = "Collection1", bookNumber = 1, genre = 1, rating = 1.1)
        val book2 = Book(2, "Title2", "Author2", collection = "Collection2", bookNumber = 2, genre = 2, rating = 2.2)
        val book3 = Book(3, "Title3", "Author3", collection = "Collection3", bookNumber = 3, genre = 3, rating = 3.3)
        val list: MutableList<Book> = ArrayList()
        list.add(book1)
        list.add(book2)
        list.add(book3)
        return list
    }
}