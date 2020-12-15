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

data class Genre(
    val id: Int,
    val name: String = ""
) {
    companion object {
        fun createMock(): List<Genre> {
            val genreList = ArrayList<Genre>()
            var genre = Genre(1, "Fantasy")
            genreList.add(genre)

            genre = Genre(2, "Sci-fi")
            genreList.add(genre)

            genre = Genre(3, "Other")
            genreList.add(genre)

            return genreList
        }
    }
}