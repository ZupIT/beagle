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


data class CharacterResponse(
    val currentPage: Int,
    val totalPages: Int,
    val result: ArrayList<Character>
) {
    companion object {
        fun createMock(page: Int): CharacterResponse {
            return CharacterResponse(page, 2, Character.createMock(page))
        }
    }
}

data class Character(
    var name: String = "",
    var book: String = "",
    var collection: String? = null) {

    companion object {
        fun createMock(page: Int): ArrayList<Character> {
            val characters = ArrayList<Character>()
            when (page) {
                1 -> {
                    characters.add(Character("Vin", "The Final Empire", "Mistborn Era 1"))
                    characters.add(Character("Kelsier", "The Final Empire", "Mistborn Era 1"))
                    characters.add(Character("Lord Ruler", "The Final Empire", "Mistborn Era 1"))
                    characters.add(Character("Sazed", "The Final Empire", "Mistborn Era 1"))
                    characters.add(Character("Elend Venture", "The Final Empire", "Mistborn Era 1"))

                    characters.add(Character("Waxillium \"Wax\" Ladrian", "The Alloy of Law", "Mistborn Era 2"))
                    characters.add(Character("Wayne", "The Alloy of Law", "Mistborn Era 2"))
                    characters.add(Character("Marasi Colms", "The Alloy of Law", "Mistborn Era 2"))
                    characters.add(Character("Steris Harms", "The Alloy of Law", "Mistborn Era 2"))
                    characters.add(Character("Miles Dagouter", "The Alloy of Law", "Mistborn Era 2"))

                    characters.add(Character("Eddard \"Ned\" Stark", "A Game of Thrones", "A Song of Ice and Fire"))
                    characters.add(Character("Catelyn Stark", "A Game of Thrones", "A Song of Ice and Fire"))
                    characters.add(Character("Sansa Stark", "A Game of Thrones", "A Song of Ice and Fire"))
                    characters.add(Character("Arya Stark", "A Game of Thrones", "A Song of Ice and Fire"))
                    characters.add(Character("Bran Stark", "A Game of Thrones", "A Song of Ice and Fire"))

                    characters.add(Character("Szeth-son-son-Vallano", "Words of Radiance", "The Stormlight Archive"))
                    characters.add(Character("Shallan Davar", "Words of Radiance", "The Stormlight Archive"))
                    characters.add(Character("Kaladin", "Words of Radiance", "The Stormlight Archive"))
                    characters.add(Character("Dalinar Kholin", "Words of Radiance", "The Stormlight Archive"))
                    characters.add(Character("Adolin Kholin", "Words of Radiance", "The Stormlight Archive"))

                    characters.add(Character("Talanji", "Shadows Rising", "World of Warcraft"))
                    characters.add(Character("Zekhan", "Shadows Rising", "World of Warcraft"))
                    characters.add(Character("Anduin Wrynn", "Shadows Rising", "World of Warcraft"))
                    characters.add(Character("Turalyon", "Shadows Rising", "World of Warcraft"))
                    characters.add(Character("Aleria Windrunner", "Shadows Rising", "World of Warcraft"))

                    characters.add(Character("Anduin Wrynn", "Before the Storm", "World of Warcraft"))
                    characters.add(Character("Sylvanas Windrunner", "Before the Storm", "World of Warcraft"))
                    characters.add(Character("Grizzek Fizzwrench", "Before the Storm", "World of Warcraft"))
                    characters.add(Character("Sapphronetta Flivvers", "Before the Storm", "World of Warcraft"))
                    characters.add(Character("Calia Menethil", "Before the Storm", "World of Warcraft"))

                    characters.add(Character("Harry Potter", "Harry Potter and the Philosopher's Stone", "Harry Potter"))
                    characters.add(Character("Ronald Weasley", "Harry Potter and the Philosopher's Stone", "Harry Potter"))
                    characters.add(Character("Hermione Granger", "Harry Potter and the Philosopher's Stone", "Harry Potter"))
                    characters.add(Character("Rúbeo Hagrid", "Harry Potter and the Philosopher's Stone", "Harry Potter"))
                }
                2 -> {
                    characters.add(Character("Dumbledore", "Harry Potter and the Philosopher's Stone", "Harry Potter"))

                    characters.add(Character("Spensa Nightshade", "Starsight", "Skyward"))
                    characters.add(Character("Jorgen Weight", "Starsight", "Skyward"))
                    characters.add(Character("Admiral Cobb", "Starsight", "Skyward"))
                    characters.add(Character("M-Bot", "Starsight", "Skyward"))
                    characters.add(Character("Alanik", "Starsight", "Skyward"))

                    characters.add(Character("Bob", "Heaven's River", "Bobiverse"))
                    characters.add(Character("Brigit", "Heaven's River", "Bobiverse"))
                    characters.add(Character("Bender", "Heaven's River", "Bobiverse"))
                    characters.add(Character("Howard", "Heaven's River", "Bobiverse"))

                    characters.add(Character("Juliette Andromeda Mao", "Leviathan Wakes", "The Expanse"))
                    characters.add(Character("James Holden", "Leviathan Wakes", "The Expanse"))
                    characters.add(Character("Naomi Nagata", "Leviathan Wakes", "The Expanse"))
                    characters.add(Character("Amos Burton", "Leviathan Wakes", "The Expanse"))
                    characters.add(Character("Shed Garvey", "Leviathan Wakes", "The Expanse"))

                    characters.add(Character("Paul Atreides", "Dune", "Dune"))
                    characters.add(Character("Duke Leto Atreides", "Dune", "Dune"))
                    characters.add(Character("Lady Jessica", "Dune", "Dune"))
                    characters.add(Character("Alia Atreides", "Dune", "Dune"))
                    characters.add(Character("Thufir Hawat", "Dune", "Dune"))

                    characters.add(Character("Dirk t'Larien", "Dying of the Light"))
                    characters.add(Character("Jaan Vikary", "Dying of the Light"))
                    characters.add(Character("Garse Janacek", "Dying of the Light"))

                    characters.add(Character("Greg Dixon", "The Last Tribe"))
                    characters.add(Character("John Dixon", "The Last Tribe"))
                    characters.add(Character("Emily Dixon", "The Last Tribe"))
                    characters.add(Character("Rebecca", "The Last Tribe"))

                    characters.add(Character("Don Barlow", "The Cuckoo's Cry"))

                    characters.add(Character("Offred", "The Handmaid's Tale", "The Handmaid's Tale"))
                    characters.add(Character("The Commander", "The Handmaid's Tale", "The Handmaid's Tale"))
                    characters.add(Character("Serena Joy", "The Handmaid's Tale", "The Handmaid's Tale"))
                    characters.add(Character("Ofglen", "The Handmaid's Tale", "The Handmaid's Tale"))
                    characters.add(Character("Nick", "The Handmaid's Tale", "The Handmaid's Tale"))
                }
            }
            return characters
        }
    }
}