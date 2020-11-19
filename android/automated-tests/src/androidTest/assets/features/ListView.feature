#
# Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

@listview @regression
Feature: ListView Component Validation

    As a Beagle developer/user
    I'd like to make sure my listView component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the listView screen
#        And the view port size is 720x480

  # First ListView: characters: horizontal with pagination and custom iteratorName

    Scenario: ListView 01 - Characters ListView
        Then should render the list of characters with exactly 34 items in the horizontal plane
        And the list of characters should be scrollable only horizontally
        And the page number should be 1/2
        
    Scenario: ListView 02 - Characters ListView: going from page 1 to 2
        When I click the button next
        Then should render the list of characters with exactly 33 items in the horizontal plane
        And the page number should be 2/2

    Scenario: ListView 03 - Characters ListView: going back from page 2 to 1
        When I click the button next
        And I click the button prev
        Then should render the list of characters with exactly 34 items in the horizontal plane
        And the page number should be 1/2

    Scenario Outline: ListView 04 - Characters ListView: page 1 item by item
        Then should render character <name> of <book> in <collection> at <position> in the list of characters
#        And the book analytics should have been called with character "<name>" and source "characters-list"

        Examples:
            | position | name                    | book                                     | collection             |
            | 0        | Vin                     | The Final Empire                         | Mistborn Era 1         |
            | 1        | Kelsier                 | The Final Empire                         | Mistborn Era 1         |
            | 2        | Lord Ruler              | The Final Empire                         | Mistborn Era 1         |
            | 3        | Sazed                   | The Final Empire                         | Mistborn Era 1         |
            | 4        | Elend Venture           | The Final Empire                         | Mistborn Era 1         |
            | 5        | Waxillium "Wax" Ladrian | The Alloy of Law                         | Mistborn Era 2         |
            | 6        | Wayne                   | The Alloy of Law                         | Mistborn Era 2         |
            | 7        | Marasi Colms            | The Alloy of Law                         | Mistborn Era 2         |
            | 8        | Steris Harms            | The Alloy of Law                         | Mistborn Era 2         |
            | 9        | Miles Dagouter          | The Alloy of Law                         | Mistborn Era 2         |
            | 10       | Eddard "Ned" Stark      | A Game of Thrones                        | A Song of Ice and Fire |
            | 11       | Catelyn Stark           | A Game of Thrones                        | A Song of Ice and Fire |
            | 12       | Sansa Stark             | A Game of Thrones                        | A Song of Ice and Fire |
            | 13       | Arya Stark              | A Game of Thrones                        | A Song of Ice and Fire |
            | 14       | Bran Stark              | A Game of Thrones                        | A Song of Ice and Fire |
            | 15       | Szeth-son-son-Vallano   | Words of Radiance                        | The Stormlight Archive |
            | 16       | Shallan Davar           | Words of Radiance                        | The Stormlight Archive |
            | 17       | Kaladin                 | Words of Radiance                        | The Stormlight Archive |
            | 18       | Dalinar Kholin          | Words of Radiance                        | The Stormlight Archive |
            | 19       | Adolin Kholin           | Words of Radiance                        | The Stormlight Archive |
            | 20       | Talanji                 | Shadows Rising                           | World of Warcraft      |
            | 21       | Zekhan                  | Shadows Rising                           | World of Warcraft      |
            | 22      | Anduin Wrynn            | Shadows Rising                           | World of Warcraft      |
            | 23       | Turalyon                | Shadows Rising                           | World of Warcraft      |
            | 24       | Aleria Windrunner       | Shadows Rising                           | World of Warcraft      |
            | 25       | Anduin Wrynn            | Before the Storm                         | World of Warcraft      |
            | 26       | Sylvanas Windrunner     | Before the Storm                         | World of Warcraft      |
            | 27       | Grizzek Fizzwrench      | Before the Storm                         | World of Warcraft      |
            | 28       | Sapphronetta Flivvers   | Before the Storm                         | World of Warcraft      |
            | 29       | Calia Menethil          | Before the Storm                         | World of Warcraft      |
            | 30       | Harry Potter            | Harry Potter and the Philosopher's Stone | Harry Potter           |
            | 31       | Ronald Weasley          | Harry Potter and the Philosopher's Stone | Harry Potter           |
            | 32       | Hermione Granger        | Harry Potter and the Philosopher's Stone | Harry Potter           |
            | 33       | Rúbeo Hagrid            | Harry Potter and the Philosopher's Stone | Harry Potter           |

    Scenario Outline: ListView 05 - Characters ListView: page 2 item by item
        When I click the button next
        Then the page number should be 2/2
        And should render character <name> of <book> in <collection> at <position> in the list of characters
#        And the book analytics should have been called with character "<name>" and source "characters-list"

        Examples:
            | position | name                    | book                                     | collection             |
            | 0        | Dumbledore              | Harry Potter and the Philosopher's Stone | Harry Potter           |
            | 1        | Spensa Nightshade       | Starsight                                | Skyward                |
            | 2        | Jorgen Weight           | Starsight                                | Skyward                |
            | 3        | Admiral Cobb            | Starsight                                | Skyward                |
            | 4        | M-Bot                   | Starsight                                | Skyward                |
            | 5        | Alanik                  | Starsight                                | Skyward                |
            | 6        | Bob                     | Heaven's River                           | Bobiverse              |
            | 7        | Brigit                  | Heaven's River                           | Bobiverse              |
            | 8        | Bender                  | Heaven's River                           | Bobiverse              |
            | 9        | Howard                  | Heaven's River                           | Bobiverse              |
            | 10       | Juliette Andromeda Mao  | Leviathan Wakes                          | The Expanse            |
            | 11       | James Holden            | Leviathan Wakes                          | The Expanse            |
            | 12       | Naomi Nagata            | Leviathan Wakes                          | The Expanse            |
            | 13       | Amos Burton             | Leviathan Wakes                          | The Expanse            |
            | 14       | Shed Garvey             | Leviathan Wakes                          | The Expanse            |
            | 15       | Paul Atreides           | Dune                                     | Dune                   |
            | 16       | Duke Leto Atreides      | Dune                                     | Dune                   |
            | 17       | Lady Jessica            | Dune                                     | Dune                   |
            | 18       | Alia Atreides           | Dune                                     | Dune                   |
            | 19       | Thufir Hawat            | Dune                                     | Dune                   |
            | 20       | Dirk t'Larien           | Dying of the Light                       |                        |
            | 21       | Jaan Vikary             | Dying of the Light                       |                        |
            | 22       | Garse Janacek           | Dying of the Light                       |                        |
            | 23       | Greg Dixon              | The Last Tribe                           |                        |
            | 24       | John Dixon              | The Last Tribe                           |                        |
            | 25       | Emily Dixon             | The Last Tribe                           |                        |
            | 26       | Rebecca                 | The Last Tribe                           |                        |
            | 27       | Don Barlow              | The Cuckoo's Cry                         |                        |
            | 28       | Offred                  | The Handmaid's Tale                      | The Handmaid's Tale    |
            | 29       | The Commander           | The Handmaid's Tale                      | The Handmaid's Tale    |
            | 30       | Serena Joy              | The Handmaid's Tale                      | The Handmaid's Tale    |
            | 31       | Ofglen                  | The Handmaid's Tale                      | The Handmaid's Tale    |
            | 32       | Nick                    | The Handmaid's Tale                      | The Handmaid's Tale    |

    Scenario: ListView 06 - Characters ListView: read status
        When the read status of the list of characters is status: unread
        And I scroll the list of characters to position 34
        Then the read status of the list of characters is status: read
#
#    Scenario: ListView 07 - Characters ListView: analytics action
#        When I click the button "next"
#        And I click the button "prev"
#        And I click the button "next"
#        Then the book analytics should have been called exactly 134 times (twice per character) with the source "characters-list"

  # Second ListView: categories: nested with three levels:
  # 1. Categories: vertical with fixed height.
  # 2. Books: books for each of the categories. Horizontal.
  # 3. Characters: characters for each book. Vertical, no-scroll.

#    Scenario: ListView 08 - Categories ListView (nested): categories
#        Then the list of categories should have height equal to 307
#        And the list of categories should be scrollable only vertically
#        And should render the list of categories with exactly 3 items in the vertical plane
#        And every element in the list of categories should have a unique id or no id at all
#
#    Scenario Outline: ListView 09 - Categories ListView (nested): categories and number of books
#        Then should render "<category>" with a title and a listView
#        And the parent container in category "<category>" should have id "category:<id>"
#        And should render the list of books in "<category>" with exactly <numberOfBooks> items in the horizontal plane
#        And the list of books in "<category>" should be scrollable only horizontally
#        And the book analytics should have been called with category "<category>" and source "categories-list"
#
#        Examples:
#            | category | numberOfBooks | id      |
#            | Fantasy  | 7             | fantasy |
#            | Sci-fi   | 5             | scifi   |
#            | Other    | 3             | other   |
#
#    Scenario Outline: ListView 10 - Categories ListView (nested): books and their characters
#        Then should render "<title>" by "<author>" in "<category>" with the characters "<characters>"
#        And the parent container of the book "<title>", in "<category>", should have id "book:<categoryId>:<title>"
#        And the list of characters in "<title>" should be vertical
#        And the list of characters in "<title>" should not be scrollable
#        And each character text in "<title>" should have its id prefixed by "character:<category>:<title>:" and suffixed by its index
#        And the book analytics should have been called with book "<title>" and source "categories-books-list"
#        And for each character in [<characters>], the book analytics should have been called with the parameter "character" equal to its name and the parameter "source" equal to "categories-books-characters-list"
#
#        Examples:
#            | category | categoryId | title                                    | author              | characters                                                                                   |
#            | Fantasy  | fantasy    | The Final Empire                         | Brandon Sanderson   | Vin, Kelsier, Lord Ruler, Sazed, Elend Venture                                               |
#            | Fantasy  | fantasy    | The Alloy of Law                         | Brandon Sanderson   | Waxillium "Wax" Ladrian, Wayne, Marasi Colms, Steris Harms, Miles Dagouter                   |
#            | Fantasy  | fantasy    | A Game of Thrones                        | George R.R. Martin  | Eddard "Ned" Stark, Catelyn Stark, Sansa Stark, Arya Stark, Bran Stark                       |
#            | Fantasy  | fantasy    | Words of Radiance                        | Brandon Sanderson   | Szeth-son-son-Vallano, Shallan Davar, Kaladin, Dalinar Kholin, Adolin Kholin                 |
#            | Fantasy  | fantasy    | Shadows Rising                           | Madeleine Roux      | Talanji, Zekhan, Anduin Wrynn, Turalyon, Aleria Windrunner                                   |
#            | Fantasy  | fantasy    | Before the Storm                         | Christie Golden     | Anduin Wrynn, Sylvanas Windrunner, Grizzek Fizzwrench, Sapphronetta Flivvers, Calia Menethil |
#            | Fantasy  | fantasy    | Harry Potter and the Philosopher's Stone | J. K. Rowling       | Harry Potter, Ronald Weasley, Hermione Granger, Rúbeo Hagrid, Dumbledore                     |
#            | Sci-fi   | scifi      | Starsight                                | Brandon Sanderson   | Spensa Nightshade, Jorgen Weight, Admiral Cobb, M-Bot, Alanik                                |
#            | Sci-fi   | scifi      | Heaven's River                           | Dennis E. Taylor    | Bob, Brigit, Bender, Howard                                                                  |
#            | Sci-fi   | scifi      | Leviathan Wakes                          | James S.A. Corey    | Juliette Andromeda Mao, James Holden, Naomi Nagata, Amos Burton, Shed Garvey                 |
#            | Sci-fi   | scifi      | Dune                                     | Frank Herbert       | Paul Atreides, Duke Leto Atreides, Lady Jessica, Alia Atreides, Thufir Hawat                 |
#            | Sci-fi   | scifi      | Dying of the Light                       | George R.R. Martin  | Dirk t’Larien, Jaan Vikary, Garse Janacek                                                    |
#            | Other    | other      | The Last Tribe                           | Brad Manuel         | Greg Dixon, John Dixon, Emily Dixon, Rebecca                                                 |
#            | Other    | other      | The Cuckoo's Cry                         | Caroline Overington | Don Barlow                                                                                   |
#            | Other    | other      | The Handmaid's Tale                      | Margaret Atwood     | Offred, The Commander, Serena Joy, Ofglen, Nick                                              |
#
#    Scenario: ListView 11 - Categories ListView (nested): analytics action
#        Then the book analytics should have been called exactly 3 times (once per category) with the source "categories-list"
#        And the book analytics should have been called exactly 15 times (once per book) with the source "categories-books-list"
#        And the book analytics should have been called exactly 67 times (once per character) with the source "categories-books-characters-list"
#
#  # Third ListView: books: vertical with infinite scroll and useParentScroll true
#
#    Scenario: ListView 12 - Books ListView (infinite scroll)
#        Then should render the list of books with exactly 5 items in the vertical plane
#        And the list of books should not be scrollable
#        And the book analytics should have been called exactly 5 times (once per book) with the source "books-list"
#
#    Scenario: ListView 13 - Books ListView (infinite scroll): second set of books: scroll lower than 80% (threshold)
#        When the view is scrolled vertically to 79%
#        Then should not change the list of books (5 books)
#
#    Scenario: ListView 14 - Books ListView (infinite scroll): second set of books: scroll greater than 80% (threshold)
#        When the view is scrolled vertically to 81%
#        Then should render the list of books with exactly 10 items in the vertical plane
#        And the book analytics should have been called exactly 10 times (once per book) with the source "books-list"
#
#    Scenario: ListView 15 - Books ListView (infinite scroll): second set of books: duplicated actions
#        Given the connection has slowed down and now have a 3s delay
#        When the view is scrolled vertically to 90%
#        And the view is scrolled vertically to 70%
#        And the view is scrolled vertically to 90%
#        Then should render the list of books with exactly 10 items in the vertical plane
#
#    Scenario: ListView 16 - Books ListView (infinite scroll): third set of books: scroll lower than 80% (threshold)
#        Given that the second set of books in the list of books have been loaded
#        When the view is scrolled vertically to 70%
#        Then should not change the list of books (10 books)
#
#    Scenario: ListView 17 - Books ListView (infinite scroll): third set of books: scroll greater than 80% (threshold)
#        Given that the second set of books in the list of books have been loaded
#        When the view is scrolled vertically to 100%
#        Then should render the list of books with exactly 15 items in the vertical plane
#        And the book analytics should have been called exactly 15 times (once per book) with the source "books-list"
#
#    Scenario: ListView 18 - Books ListView (infinite scroll): no more data to load
#        Given that the third set of books in the list of books have been loaded
#        When the view is scrolled vertically to 100%
#        Then should not change the list of books (15 books)
#
#    Scenario Outline: ListView 19 - Books ListView (infinite scroll): every book rendered
#        Given that the third set of books in the list of books have been loaded
#        Then should render "<title> (<year>)" in the list of books with: Author: "<author>", Collection: "<collection>", Book number: "<bookNumber>", Genre: "<category>", Rating: "<rating>"
#        And the book analytics should have been called with book "<title>" and source "books-list"
#
#        Examples:
#            | category | title                                    | author              | collection             | bookNumber | rating |
#            | Fantasy  | The Final Empire                         | Brandon Sanderson   | Mistborn Era 1         | 1          | 4.7    |
#            | Fantasy  | The Alloy of Law                         | Brandon Sanderson   | Mistborn Era 2         | 1          | 4.5    |
#            | Fantasy  | A Game of Thrones                        | George R.R. Martin  | A Song of Ice and Fire | 1          | 4.8    |
#            | Fantasy  | Words of Radiance                        | Brandon Sanderson   | The Stormlight Archive | 2          | 4.8    |
#            | Fantasy  | Shadows Rising                           | Madeleine Roux      | World of Warcraft      | 24         | 4.3    |
#            | Fantasy  | Before the Storm                         | Christie Golden     | World of Warcraft      | 23         | 4.6    |
#            | Fantasy  | Harry Potter and the Philosopher's Stone | J. K. Rowling       | Harry Potter           | 1          | 4.9    |
#            | Sci-fi   | Starsight                                | Brandon Sanderson   | Skyward                | 2          | 4.8    |
#            | Sci-fi   | Heaven's River                           | Dennis E. Taylor    | Bobiverse              | 4          | 4.8    |
#            | Sci-fi   | Leviathan Wakes                          | James S.A. Corey    | The Expanse            | 1          | 4.7    |
#            | Sci-fi   | Dune                                     | Frank Herbert       | Dune                   | 1          | 4.6    |
#            | Sci-fi   | Dying of the Light                       | George R.R. Martin  |                        |            | 4.1    |
#            | Other    | The Last Tribe                           | Brad Manuel         |                        |            | 4.3    |
#            | Other    | The Cuckoo's Cry                         | Caroline Overington |                        |            | 4.3    |
#            | Other    | The Handmaid's Tale                      | Margaret Atwood     | The Handmaid's Tale    | 1          | 4.6    |