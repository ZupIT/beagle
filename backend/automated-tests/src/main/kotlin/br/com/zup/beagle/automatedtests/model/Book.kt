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

package br.com.zup.beagle.automatedtests.model

import java.util.*

data class Book(
    val title: String = "",
    val author: String = "",
    val collection: String? = null,
    val bookNumber: Int? = null,
    val genre: Int? = null,
    val rating: Double? = null,
    val characters: List<String>? = null
) {
    constructor(
        title: String,
        author: String
    ) : this(
        title = title,
        author = author,
        characters = Character.createMock(title)
    )

    companion object {
        fun createMockByCategory(category: Int): List<Book> {
            val bookList = ArrayList<Book>()

            when (category) {
                1 -> {
                    var book = Book(title = "The Final Empire", author = "Brandon Sanderson")
                    bookList.add(book)

                    book = Book(title = "The Alloy of Law", author = "Brandon Sanderson")
                    bookList.add(book)

                    book = Book(title = "A Game of Thrones", author = "George R.R. Martin")
                    bookList.add(book)

                    book = Book(title = "Words of Radiance", author = "Brandon Sanderson")
                    bookList.add(book)

                    book = Book(title = "Shadows Rising", author = "Madeleine Roux")
                    bookList.add(book)

                    book = Book(title = "Before the Storm", author = "Christie Golden")
                    bookList.add(book)

                    book = Book(title = "Harry Potter and the Philosopher's Stone", author = "Harry Potter and the Philosopher's Stone")
                    bookList.add(book)
                }
                2 -> {
                    var book = Book(title = "Starsight", author = "Brandon Sanderson")
                    bookList.add(book)

                    book = Book(title = "Heaven's River", author = "Dennis E. Taylor")
                    bookList.add(book)

                    book = Book(title = "Leviathan Wakes", author = "James S.A. Corey")
                    bookList.add(book)

                    book = Book(title = "Dune", author = "Frank Herbert")
                    bookList.add(book)

                    book = Book(title = "Dying of the Light", author = "George R.R. Martin")
                    bookList.add(book)
                }
                3 -> {
                    var book = Book(title = "The Last Tribe", author = "Brad Manuel")
                    bookList.add(book)

                    book = Book(title = "The Cuckoo's Cry", author = "Caroline Overington")
                    bookList.add(book)

                    book = Book(title = "The Handmaid's Tale", author = "Margaret Atwood")
                    bookList.add(book)
                }
            }

            return bookList
        }
    }
}