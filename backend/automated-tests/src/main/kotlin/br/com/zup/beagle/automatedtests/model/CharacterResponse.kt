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

data class CharacterResponse(
    val currentPage: Int,
    val totalPages: Int,
    val result: List<Character>
) {
    companion object {
        fun createMock(page: Int): CharacterResponse {
            return CharacterResponse(page, 2, Character.createMock(page))
        }
    }
}

data class Character(
    val name: String = "",
    val book: String = "",
    val collection: String? = null) {

    companion object {

        fun createMock(page: Int): List<Character> {
            return when (page) {
                1 -> charactersPage1
                else -> charactersPage2
            }
        }

        fun createMock(bookTitle: String): List<String> {
            val characterList = getCharactersByBook(bookTitle)
            /*val characterList = ArrayList<String>()

            when (bookTitle) {
                "The Final Empire" -> {
                    characterList.add("Vin")
                    characterList.add("Kelsier")
                    characterList.add("Lord Ruler")
                    characterList.add("Sazed")
                    characterList.add("Elend Venture")
                }
                "The Alloy of Law" -> {
                    characterList.add("Waxillium \"Wax\" Ladrian")
                    characterList.add("Wayne")
                    characterList.add("Marasi Colms")
                    characterList.add("Steris Harms")
                    characterList.add("Miles Dagouter")
                }
                "A Game of Thrones" -> {
                    characterList.add("Eddard \"Ned\" Stark")
                    characterList.add("Catelyn Stark")
                    characterList.add("Sansa Stark")
                    characterList.add("Arya Stark")
                    characterList.add("Bran Stark")
                }
                "Words of Radiance" -> {
                    characterList.add("Szeth-son-son-Vallano")
                    characterList.add("Shallan Davar")
                    characterList.add("Kaladin")
                    characterList.add("Dalinar Kholin")
                    characterList.add("Adolin Kholin")
                }
                "Shadows Rising" -> {
                    characterList.add("Talanji")
                    characterList.add("Zekhan")
                    characterList.add("Anduin Wrynn")
                    characterList.add("Turalyon")
                    characterList.add("Aleria Windrunner")
                }
                "Before the Storm" -> {
                    characterList.add("Anduin Wrynn")
                    characterList.add("Sylvanas Windrunner")
                    characterList.add("Grizzek Fizzwrench")
                    characterList.add("Sapphronetta Flivvers")
                    characterList.add("Calia Menethil")

                }
                "Harry Potter and the Philosopher's Stone" -> {
                    characterList.add("Harry Potter")
                    characterList.add("Ronald Weasley")
                    characterList.add("Hermione Granger")
                    characterList.add("Rúbeo Hagrid")
                    characterList.add("Dumbledore")
                }
                "Starsight" -> {
                    characterList.add("Spensa Nightshade")
                    characterList.add("Jorgen Weight")
                    characterList.add("Admiral Cobb")
                    characterList.add("M-Bot")
                    characterList.add("Alanik")
                }
                "Heaven's River" -> {
                    characterList.add("Bob")
                    characterList.add("Brigit")
                    characterList.add("Bender")
                    characterList.add("Howard")
                }
                "Leviathan Wakes" -> {
                    characterList.add("Juliette Andromeda Mao")
                    characterList.add("James Holden")
                    characterList.add("Naomi Nagata")
                    characterList.add("Amos Burton")
                    characterList.add("Shed Garvey")
                }
                "Dune" -> {
                    characterList.add("Paul Atreides")
                    characterList.add("Duke Leto Atreides")
                    characterList.add("Lady Jessica")
                    characterList.add("Alia Atreides")
                    characterList.add("Thufir Hawat")
                }
                "Dying of the Light" -> {
                    characterList.add("Dirk t'Larien")
                    characterList.add("Jaan Vikary")
                    characterList.add("Garse Janacek")
                }
                "The Last Tribe" -> {
                    characterList.add("Greg Dixon")
                    characterList.add("John Dixon")
                    characterList.add("Emily Dixon")
                    characterList.add("Rebecca")
                }
                "The Cuckoo's Cry" -> {
                    characterList.add("Don Barlow")
                }
                "The Handmaid's Tale" -> {
                    characterList.add("Offred")
                    characterList.add("The Commander")
                    characterList.add("Serena Joy")
                    characterList.add("Ofglen")
                    characterList.add("Nick")
                }
            }*/

            return characterList
        }

        private fun getCharactersByBook(bookTitle: String): List<String> {
            val characters = mutableListOf<String>()

            characters.addAll(charactersPage1.filter { it.book == bookTitle }.map { it.name })
            characters.addAll(charactersPage2.filter { it.book == bookTitle }.map { it.name })

            return characters
        }

        private val charactersPage1 = listOf(
            Character(
                name = "Vin",
                book = "The Final Empire",
                collection = "Mistborn Era 1",
            ),
            Character(
                name = "Kelsier",
                book = "The Final Empire",
                collection = "Mistborn Era 1",
            ),
            Character(
                name = "Lord Ruler",
                book = "The Final Empire",
                collection = "Mistborn Era 1",
            ),
            Character(
                name = "Sazed",
                book = "The Final Empire",
                collection = "Mistborn Era 1",
            ),
            Character(
                name = "Elend Venture",
                book = "The Final Empire",
                collection = "Mistborn Era 1",
            ),
            Character(
                name = "Waxillium \"Wax\" Ladrian",
                book = "The Alloy of Law",
                collection = "Mistborn Era 2",
            ),
            Character(
                name = "Wayne",
                book = "The Alloy of Law",
                collection = "Mistborn Era 2",
            ),
            Character(
                name = "Marasi Colms",
                book = "The Alloy of Law",
                collection = "Mistborn Era 2",
            ),
            Character(
                name = "Steris Harms",
                book = "The Alloy of Law",
                collection = "Mistborn Era 2",
            ),
            Character(
                name = "Miles Dagouter",
                book = "The Alloy of Law",
                collection = "Mistborn Era 2",
            ),

            Character(
                name = "Eddard \"Ned\" Stark",
                book = "A Game of Thrones",
                collection = "A Song of Ice and Fire",
            ),
            Character(
                name = "Catelyn Stark",
                book = "A Game of Thrones",
                collection = "A Song of Ice and Fire",
            ),
            Character(
                name = "Sansa Stark",
                book = "A Game of Thrones",
                collection = "A Song of Ice and Fire",
            ),
            Character(
                name = "Arya Stark",
                book = "A Game of Thrones",
                collection = "A Song of Ice and Fire",
            ),
            Character(
                name = "Bran Stark",
                book = "A Game of Thrones",
                collection = "A Song of Ice and Fire",
            ),

            Character(
                name = "Szeth-son-son-Vallano",
                book = "Words of Radiance",
                collection = "The Stormlight Archive",
            ),
            Character(
                name = "Shallan Davar",
                book = "Words of Radiance",
                collection = "The Stormlight Archive",
            ),
            Character(
                name = "Kaladin",
                book = "Words of Radiance",
                collection = "The Stormlight Archive",
            ),
            Character(
                name = "Dalinar Kholin",
                book = "Words of Radiance",
                collection = "The Stormlight Archive",
            ),
            Character(
                name = "Adolin Kholin",
                book = "Words of Radiance",
                collection = "The Stormlight Archive",
            ),

            Character(
                name = "Talanji",
                book = "Shadows Rising",
                collection = "World of Warcraft",
            ),
            Character(
                name = "Zekhan",
                book = "Shadows Rising",
                collection = "World of Warcraft",
            ),
            Character(
                name = "Anduin Wrynn",
                book = "Shadows Rising",
                collection = "World of Warcraft",
            ),
            Character(
                name = "Turalyon",
                book = "Shadows Rising",
                collection = "World of Warcraft",
            ),
            Character(
                name = "Aleria Windrunner",
                book = "Shadows Rising",
                collection = "World of Warcraft",
            ),

            Character(
                name = "Anduin Wrynn",
                book = "Before the Storm",
                collection = "World of Warcraft",
            ),
            Character(
                name = "Sylvanas Windrunner",
                book = "Before the Storm",
                collection = "World of Warcraft",
            ),
            Character(
                name = "Grizzek Fizzwrench",
                book = "Before the Storm",
                collection = "World of Warcraft",
            ),
            Character(
                name = "Sapphronetta Flivvers",
                book = "Before the Storm",
                collection = "World of Warcraft",
            ),
            Character(
                name = "Calia Menethil",
                book = "Before the Storm",
                collection = "World of Warcraft",
            ),

            Character(
                name = "Harry Potter",
                book = "Harry Potter and the Philosopher's Stone",
                collection = "Harry Potter",
            ),
            Character(
                name = "Ronald Weasley",
                book = "Harry Potter and the Philosopher's Stone",
                collection = "Harry Potter",
            ),
            Character(
                name = "Hermione Granger",
                book = "Harry Potter and the Philosopher's Stone",
                collection = "Harry Potter",
            ),
            Character(
                name = "Rúbeo Hagrid",
                book = "Harry Potter and the Philosopher's Stone",
                collection = "Harry Potter",
            ),
        )

        private val charactersPage2 = listOf(
            Character(
                name = "Dumbledore",
                book = "Harry Potter and the Philosopher's Stone",
                collection = "Harry Potter",
            ),
            Character(
                name = "Spensa Nightshade",
                book = "Starsight",
                collection = "Skyward",
            ),
            Character(
                name = "Jorgen Weight",
                book = "Starsight",
                collection = "Skyward",
            ),
            Character(
                name = "Admiral Cobb",
                book = "Starsight",
                collection = "Skyward",
            ),
            Character(
                name = "M-Bot",
                book = "Starsight",
                collection = "Skyward",
            ),
            Character(
                name = "Alanik",
                book = "Starsight",
                collection = "Skyward",
            ),
            Character(
                name = "Bob",
                book = "Heaven's River",
                collection = "Bobiverse",
            ),
            Character(
                name = "Brigit",
                book = "Heaven's River",
                collection = "Bobiverse",
            ),
            Character(
                name = "Bender",
                book = "Heaven's River",
                collection = "Bobiverse",
            ),
            Character(
                name = "Howard",
                book = "Heaven's River",
                collection = "Bobiverse",
            ),
            Character(
                name = "Juliette Andromeda Mao",
                book = "Leviathan Wakes",
                collection = "The Expanse",
            ),
            Character(
                name = "James Holden",
                book = "Leviathan Wakes",
                collection = "The Expanse",
            ),
            Character(
                name = "Naomi Nagata",
                book = "Leviathan Wakes",
                collection = "The Expanse",
            ),
            Character(
                name = "Amos Burton",
                book = "Leviathan Wakes",
                collection = "The Expanse",
            ),
            Character(
                name = "Shed Garvey",
                book = "Leviathan Wakes",
                collection = "The Expanse",
            ),
            Character(
                name = "Paul Atreides",
                book = "Dune",
                collection = "Dune",
            ),
            Character(
                name = "Duke Leto Atreides",
                book = "Dune",
                collection = "Dune",
            ),
            Character(
                name = "Lady Jessica",
                book = "Dune",
                collection = "Dune",
            ),
            Character(
                name = "Alia Atreides",
                book = "Dune",
                collection = "Dune",
            ),
            Character(
                name = "Thufir Hawat",
                book = "Dune",
                collection = "Dune",
            ),
            Character(
                name = "Dirk t'Larien",
                book = "Dying of the Light",
                collection = "",
            ),
            Character(
                name = "Jaan Vikary",
                book = "Dying of the Light",
                collection = "",
            ),
            Character(
                name = "Garse Janacek",
                book = "Dying of the Light",
                collection = "",
            ),
            Character(
                name = "Greg Dixon",
                book = "The Last Tribe",
                collection = "",
            ),
            Character(
                name = "John Dixon",
                book = "The Last Tribe",
                collection = "",
            ),
            Character(
                name = "Emily Dixon",
                book = "The Last Tribe",
                collection = "",
            ),
            Character(
                name = "Rebecca",
                book = "The Last Tribe",
                collection = "",
            ),
            Character(
                name = "Don Barlow",
                book = "The Cuckoo's Cry",
                collection = "",
            ),
            Character(
                name = "Offred",
                book = "The Handmaid's Tale",
                collection = "The Handmaid's Tale",
            ),
            Character(
                name = "The Commander",
                book = "The Handmaid's Tale",
                collection = "The Handmaid's Tale",
            ),
            Character(
                name = "Serena Joy",
                book = "The Handmaid's Tale",
                collection = "The Handmaid's Tale",
            ),
            Character(
                name = "Ofglen",
                book = "The Handmaid's Tale",
                collection = "The Handmaid's Tale",
            ),
            Character(
                name = "Nick",
                book = "The Handmaid's Tale",
                collection = "The Handmaid's Tale",
            ),
        )
    }
}