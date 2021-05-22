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

@listView @android @ios
Feature: ListView Component Validation

  As a Beagle developer/user
  I'd like to make sure my listView component works as expected
  In order to guarantee that my application never fails

  Background:
    Given that I'm on the listView screen
        
    # --------------------------------------------------------------------------------------------------    
    # ListView charactersList: horizontal with pagination and custom iteratorName    
    # --------------------------------------------------------------------------------------------------

#  Scenario: ListView 01 - Characters ListView on pagination 1
#    When I scroll left the listView with id charactersList on pagination 1
#    Then the listView with id charactersList on pagination 1 should have exactly 34 items
#    And  the screen should show text: status: read
#    And  the screen should show text: 1/2
#    And  the values of the listView with id charactersList on pagination 1 should be:
#      | Name: Vin                     | Book: The Final Empire                         | Collection: Mistborn Era 1         |
#      | Name: Kelsier                 | Book: The Final Empire                         | Collection: Mistborn Era 1         |
#      | Name: Lord Ruler              | Book: The Final Empire                         | Collection: Mistborn Era 1         |
#      | Name: Sazed                   | Book: The Final Empire                         | Collection: Mistborn Era 1         |
#      | Name: Elend Venture           | Book: The Final Empire                         | Collection: Mistborn Era 1         |
#      | Name: Waxillium "Wax" Ladrian | Book: The Alloy of Law                         | Collection: Mistborn Era 2         |
#      | Name: Wayne                   | Book: The Alloy of Law                         | Collection: Mistborn Era 2         |
#      | Name: Marasi Colms            | Book: The Alloy of Law                         | Collection: Mistborn Era 2         |
#      | Name: Steris Harms            | Book: The Alloy of Law                         | Collection: Mistborn Era 2         |
#      | Name: Miles Dagouter          | Book: The Alloy of Law                         | Collection: Mistborn Era 2         |
#      | Name: Eddard "Ned" Stark      | Book: A Game of Thrones                        | Collection: A Song of Ice and Fire |
#      | Name: Catelyn Stark           | Book: A Game of Thrones                        | Collection: A Song of Ice and Fire |
#      | Name: Sansa Stark             | Book: A Game of Thrones                        | Collection: A Song of Ice and Fire |
#      | Name: Arya Stark              | Book: A Game of Thrones                        | Collection: A Song of Ice and Fire |
#      | Name: Bran Stark              | Book: A Game of Thrones                        | Collection: A Song of Ice and Fire |
#      | Name: Szeth-son-son-Vallano   | Book: Words of Radiance                        | Collection: The Stormlight Archive |
#      | Name: Shallan Davar           | Book: Words of Radiance                        | Collection: The Stormlight Archive |
#      | Name: Kaladin                 | Book: Words of Radiance                        | Collection: The Stormlight Archive |
#      | Name: Dalinar Kholin          | Book: Words of Radiance                        | Collection: The Stormlight Archive |
#      | Name: Adolin Kholin           | Book: Words of Radiance                        | Collection: The Stormlight Archive |
#      | Name: Talanji                 | Book: Shadows Rising                           | Collection: World of Warcraft      |
#      | Name: Zekhan                  | Book: Shadows Rising                           | Collection: World of Warcraft      |
#      | Name: Anduin Wrynn            | Book: Shadows Rising                           | Collection: World of Warcraft      |
#      | Name: Turalyon                | Book: Shadows Rising                           | Collection: World of Warcraft      |
#      | Name: Aleria Windrunner       | Book: Shadows Rising                           | Collection: World of Warcraft      |
#      | Name: Anduin Wrynn            | Book: Before the Storm                         | Collection: World of Warcraft      |
#      | Name: Sylvanas Windrunner     | Book: Before the Storm                         | Collection: World of Warcraft      |
#      | Name: Grizzek Fizzwrench      | Book: Before the Storm                         | Collection: World of Warcraft      |
#      | Name: Sapphronetta Flivvers   | Book: Before the Storm                         | Collection: World of Warcraft      |
#      | Name: Calia Menethil          | Book: Before the Storm                         | Collection: World of Warcraft      |
#      | Name: Harry Potter            | Book: Harry Potter and the Philosopher's Stone | Collection: Harry Potter           |
#      | Name: Ronald Weasley          | Book: Harry Potter and the Philosopher's Stone | Collection: Harry Potter           |
#      | Name: Hermione Granger        | Book: Harry Potter and the Philosopher's Stone | Collection: Harry Potter           |
#      | Name: Rúbeo Hagrid            | Book: Harry Potter and the Philosopher's Stone | Collection: Harry Potter           |
#
#
#  Scenario: ListView 02 - Characters ListView on pagination 2
#    When I click on button next
#    And  I scroll left the listView with id charactersList on pagination 2
#    Then the listView with id charactersList on pagination 2 should have exactly 33 items
#    And  the screen should show text: status: read
#    And  the screen should show text: 2/2
#    And  the values of the listView with id charactersList on pagination 2 should be:
#      | Name: Dumbledore             | Book: Harry Potter and the Philosopher's Stone | Collection: Harry Potter        |
#      | Name: Spensa Nightshade      | Book: Starsight                                | Collection: Skyward             |
#      | Name: Jorgen Weight          | Book: Starsight                                | Collection: Skyward             |
#      | Name: Admiral Cobb           | Book: Starsight                                | Collection: Skyward             |
#      | Name: M-Bot                  | Book: Starsight                                | Collection: Skyward             |
#      | Name: Alanik                 | Book: Starsight                                | Collection: Skyward             |
#      | Name: Bob                    | Book: Heaven's River                           | Collection: Bobiverse           |
#      | Name: Brigit                 | Book: Heaven's River                           | Collection: Bobiverse           |
#      | Name: Bender                 | Book: Heaven's River                           | Collection: Bobiverse           |
#      | Name: Howard                 | Book: Heaven's River                           | Collection: Bobiverse           |
#      | Name: Juliette Andromeda Mao | Book: Leviathan Wakes                          | Collection: The Expanse         |
#      | Name: James Holden           | Book: Leviathan Wakes                          | Collection: The Expanse         |
#      | Name: Naomi Nagata           | Book: Leviathan Wakes                          | Collection: The Expanse         |
#      | Name: Amos Burton            | Book: Leviathan Wakes                          | Collection: The Expanse         |
#      | Name: Shed Garvey            | Book: Leviathan Wakes                          | Collection: The Expanse         |
#      | Name: Paul Atreides          | Book: Dune                                     | Collection: Dune                |
#      | Name: Duke Leto Atreides     | Book: Dune                                     | Collection: Dune                |
#      | Name: Lady Jessica           | Book: Dune                                     | Collection: Dune                |
#      | Name: Alia Atreides          | Book: Dune                                     | Collection: Dune                |
#      | Name: Thufir Hawat           | Book: Dune                                     | Collection: Dune                |
#      | Name: Dirk t'Larien          | Book: Dying of the Light                       | Collection:                     |
#      | Name: Jaan Vikary            | Book: Dying of the Light                       | Collection:                     |
#      | Name: Garse Janacek          | Book: Dying of the Light                       | Collection:                     |
#      | Name: Greg Dixon             | Book: The Last Tribe                           | Collection:                     |
#      | Name: John Dixon             | Book: The Last Tribe                           | Collection:                     |
#      | Name: Emily Dixon            | Book: The Last Tribe                           | Collection:                     |
#      | Name: Rebecca                | Book: The Last Tribe                           | Collection:                     |
#      | Name: Don Barlow             | Book: The Cuckoo's Cry                         | Collection:                     |
#      | Name: Offred                 | Book: The Handmaid's Tale                      | Collection: The Handmaid's Tale |
#      | Name: The Commander          | Book: The Handmaid's Tale                      | Collection: The Handmaid's Tale |
#      | Name: Serena Joy             | Book: The Handmaid's Tale                      | Collection: The Handmaid's Tale |
#      | Name: Ofglen                 | Book: The Handmaid's Tale                      | Collection: The Handmaid's Tale |
#      | Name: Nick                   | Book: The Handmaid's Tale                      | Collection: The Handmaid's Tale |
#
#
#  Scenario: ListView 03 - Characters ListView: going back from page 2 to 1
#    When I click on button next
#    And I click on button prev
#    Then the screen should show text: 1/2
#    And the listView with id charactersList should be on pagination 1


    # --------------------------------------------------------------------------------------------------
    # Second ListView: categories: nested with three levels:
    # 1. Categories: vertical with fixed height.
    # 2. Books: books for each of the categories. Horizontal.
    # 3. Characters: characters for each book. Vertical, no-scroll.
    # --------------------------------------------------------------------------------------------------

    Scenario: ListView 04 - Categories ListView
    When I read all the elements of the listView with id categoriesList
    Then the listView with id categoriesList should have exactly 3 items
    And the listView with id category:1 should have exactly 7 items
    And the listView with id category:2 should have exactly 5 items
    And the listView with id category:3 should have exactly 3 items
    And  the values of the listView with id categoriesList and its items should be:
      | categoryListPosition | bookListPosition | title                                    | author              | characterListPosition | character                 |
      | 0                    | 0                | The Final Empire                         | Brandon Sanderson   | 0                     | - Vin                     |
      | 0                    | 0                | The Final Empire                         | Brandon Sanderson   | 1                     | - Kelsier                 |
      | 0                    | 0                | The Final Empire                         | Brandon Sanderson   | 2                     | - Lord Ruler              |
      | 0                    | 0                | The Final Empire                         | Brandon Sanderson   | 3                     | - Sazed                   |
      | 0                    | 0                | The Final Empire                         | Brandon Sanderson   | 4                     | - Elend Venture           |
      | 0                    | 1                | The Alloy of Law                         | Brandon Sanderson   | 0                     | - Waxillium "Wax" Ladrian |
      | 0                    | 1                | The Alloy of Law                         | Brandon Sanderson   | 1                     | - Wayne                   |
      | 0                    | 1                | The Alloy of Law                         | Brandon Sanderson   | 2                     | - Marasi Colms            |
      | 0                    | 1                | The Alloy of Law                         | Brandon Sanderson   | 3                     | - Steris Harms            |
      | 0                    | 1                | The Alloy of Law                         | Brandon Sanderson   | 4                     | - Miles Dagouter          |
      | 0                    | 2                | A Game of Thrones                        | George R.R. Martin  | 0                     | - Eddard "Ned" Stark      |
      | 0                    | 2                | A Game of Thrones                        | George R.R. Martin  | 1                     | - Catelyn Stark           |
      | 0                    | 2                | A Game of Thrones                        | George R.R. Martin  | 2                     | - Sansa Stark             |
      | 0                    | 2                | A Game of Thrones                        | George R.R. Martin  | 3                     | - Arya Stark              |
      | 0                    | 2                | A Game of Thrones                        | George R.R. Martin  | 4                     | - Bran Stark              |
      | 0                    | 3                | Words of Radiance                        | Brandon Sanderson   | 0                     | - Szeth-son-son-Vallano   |
      | 0                    | 3                | Words of Radiance                        | Brandon Sanderson   | 1                     | - Shallan Davar           |
      | 0                    | 3                | Words of Radiance                        | Brandon Sanderson   | 2                     | - Kaladin                 |
      | 0                    | 3                | Words of Radiance                        | Brandon Sanderson   | 3                     | - Dalinar Kholin          |
      | 0                    | 3                | Words of Radiance                        | Brandon Sanderson   | 4                     | - Adolin Kholin           |
      | 0                    | 4                | Shadows Rising                           | Madeleine Roux      | 0                     | - Talanji                 |
      | 0                    | 4                | Shadows Rising                           | Madeleine Roux      | 1                     | - Zekhan                  |
      | 0                    | 4                | Shadows Rising                           | Madeleine Roux      | 2                     | - Anduin Wrynn            |
      | 0                    | 4                | Shadows Rising                           | Madeleine Roux      | 3                     | - Turalyon                |
      | 0                    | 4                | Shadows Rising                           | Madeleine Roux      | 4                     | - Aleria Windrunner       |
      | 0                    | 5                | Before the Storm                         | Christie Golden     | 0                     | - Anduin Wrynn            |
      | 0                    | 5                | Before the Storm                         | Christie Golden     | 1                     | - Sylvanas Windrunner     |
      | 0                    | 5                | Before the Storm                         | Christie Golden     | 2                     | - Grizzek Fizzwrench      |
      | 0                    | 5                | Before the Storm                         | Christie Golden     | 3                     | - Sapphronetta Flivvers   |
      | 0                    | 5                | Before the Storm                         | Christie Golden     | 4                     | - Calia Menethil          |
      | 0                    | 6                | Harry Potter and the Philosopher's Stone | J. K. Rowling       | 0                     | - Harry Potter            |
      | 0                    | 6                | Harry Potter and the Philosopher's Stone | J. K. Rowling       | 1                     | - Ronald Weasley          |
      | 0                    | 6                | Harry Potter and the Philosopher's Stone | J. K. Rowling       | 2                     | - Hermione Granger        |
      | 0                    | 6                | Harry Potter and the Philosopher's Stone | J. K. Rowling       | 3                     | - Rúbeo Hagrid            |
      | 0                    | 6                | Harry Potter and the Philosopher's Stone | J. K. Rowling       | 4                     | - Dumbledore              |
      | 1                    | 0                | Starsight                                | Brandon Sanderson   | 0                     | - Spensa Nightshade       |
      | 1                    | 0                | Starsight                                | Brandon Sanderson   | 1                     | - Jorgen Weight           |
      | 1                    | 0                | Starsight                                | Brandon Sanderson   | 2                     | - Admiral Cobb            |
      | 1                    | 0                | Starsight                                | Brandon Sanderson   | 3                     | - M-Bot                   |
      | 1                    | 0                | Starsight                                | Brandon Sanderson   | 4                     | - Alanik                  |
      | 1                    | 1                | Heaven's River                           | Dennis E. Taylor    | 0                     | - Bob                     |
      | 1                    | 1                | Heaven's River                           | Dennis E. Taylor    | 1                     | - Brigit                  |
      | 1                    | 1                | Heaven's River                           | Dennis E. Taylor    | 2                     | - Bender                  |
      | 1                    | 1                | Heaven's River                           | Dennis E. Taylor    | 3                     | - Howard                  |
      | 1                    | 2                | Leviathan Wakes                          | James S.A. Corey    | 0                     | - Juliette Andromeda Mao  |
      | 1                    | 2                | Leviathan Wakes                          | James S.A. Corey    | 1                     | - James Holden            |
      | 1                    | 2                | Leviathan Wakes                          | James S.A. Corey    | 2                     | - Naomi Nagata            |
      | 1                    | 2                | Leviathan Wakes                          | James S.A. Corey    | 3                     | - Amos Burton             |
      | 1                    | 2                | Leviathan Wakes                          | James S.A. Corey    | 4                     | - Shed Garvey             |
      | 1                    | 3                | Dune                                     | Frank Herbert       | 0                     | - Paul Atreides           |
      | 1                    | 3                | Dune                                     | Frank Herbert       | 1                     | - Duke Leto Atreides      |
      | 1                    | 3                | Dune                                     | Frank Herbert       | 2                     | - Lady Jessica            |
      | 1                    | 3                | Dune                                     | Frank Herbert       | 3                     | - Alia Atreides           |
      | 1                    | 3                | Dune                                     | Frank Herbert       | 4                     | - Thufir Hawat            |
      | 1                    | 4                | Dying of the Light                       | George R.R. Martin  | 0                     | - Dirk t'Larien           |
      | 1                    | 4                | Dying of the Light                       | George R.R. Martin  | 1                     | - Jaan Vikary             |
      | 1                    | 4                | Dying of the Light                       | George R.R. Martin  | 2                     | - Garse Janacek           |
      | 2                    | 0                | The Last Tribe                           | Brad Manuel         | 0                     | - Greg Dixon              |
      | 2                    | 0                | The Last Tribe                           | Brad Manuel         | 1                     | - John Dixon              |
      | 2                    | 0                | The Last Tribe                           | Brad Manuel         | 2                     | - Emily Dixon             |
      | 2                    | 0                | The Last Tribe                           | Brad Manuel         | 3                     | - Rebecca                 |
      | 2                    | 1                | The Cuckoo's Cry                         | Caroline Overington | 0                     | - Don Barlow              |
      | 2                    | 2                | The Handmaid's Tale                      | Margaret Atwood     | 0                     | - Offred                  |
      | 2                    | 2                | The Handmaid's Tale                      | Margaret Atwood     | 1                     | - The Commander           |
      | 2                    | 2                | The Handmaid's Tale                      | Margaret Atwood     | 2                     | - Serena Joy              |
      | 2                    | 2                | The Handmaid's Tale                      | Margaret Atwood     | 3                     | - Ofglen                  |
      | 2                    | 2                | The Handmaid's Tale                      | Margaret Atwood     | 4                     | - Nick                    |

#
##  # Third ListView: books: vertical with infinite scroll and useParentScroll true
#
#    Scenario: ListView 10 - Books ListView (infinite scroll)
#        Then listView with id booksList should have exactly 5 items
#        And listView with id booksList should be in vertical orientation
#
#    Scenario: ListView 11 - Books ListView (infinite scroll): check onInit calls
#        When I scroll to view with id booksList
#        And I scroll listView with id booksList to position 5
#        Then the screen should show text: Books List View (infinite scroll): 5 items initialized
#
#    Scenario: ListView 12 - Books ListView (infinite scroll): second set of books: scroll lower than 80% (threshold)
#        When I scroll to view with id booksList
#        And I scroll listView with id booksList to 79 percent
#        Then listView with id booksList should have exactly 5 items
#
#    Scenario: ListView 13 - Books ListView (infinite scroll): second set of books: scroll greater than 80% (threshold)
#        When I scroll to view with id booksList
#        And I scroll listView with id booksList to 81 percent
#        Then listView with id booksList should have exactly 10 items
#
#    Scenario: ListView 14 - Books ListView (infinite scroll): second set of books: check onInit calls
#        When I scroll to view with id booksList
#        And I scroll listView with id booksList to position 5
#        And I scroll listView with id booksList to position 10
#        Then the screen should show text: Books List View (infinite scroll): 10 items initialized
#
#   # In this test we scroll the list up to 81 percent, to fetch books data source second page
#   # then we scroll the new concatenated list up to 70 percent.
#   # The list must not call onScrollEnd action again
#    Scenario: ListView 15 - Books ListView (infinite scroll): third set of books: scroll lower than 80% (threshold)
#        When I scroll to view with id booksList
#        And I scroll listView with id booksList to 81 percent
#        And I scroll listView with id booksList to 70 percent
#        Then listView with id booksList should have exactly 10 items
#
#   # In this test we scroll the list up to 81 percent, to fetch books data source second page
#   # then we scroll the new concatenated list up to 81 percent, to fetch books data source third page
#    Scenario: ListView 16 - Books ListView (infinite scroll): third set of books: scroll greater than 80% (threshold)
#        When I scroll to view with id booksList
#        And I scroll listView with id booksList to 81 percent
#        And I scroll listView with id booksList to 81 percent
#        Then listView with id booksList should have exactly 15 items
#
#   # In this test we scroll the list up to 81 percent, to fetch books data source second page
#   # then we scroll the new concatenated list up to 81 percent, to fetch books data source third page
#   # then we scroll the new concatenates list up to 100 percent
#   # The list must not call onScrollEnd action for the last scroll
#    Scenario: ListView 17 - Books ListView (infinite scroll): no more data to load1
#        When I scroll to view with id booksList
#        And I scroll listView with id booksList to 81 percent
#        And I scroll listView with id booksList to 81 percent
#        And I scroll listView with id booksList to 100 percent
#        Then listView with id booksList should have exactly 15 items
#
#   # In this test we scroll the list up to 81 percent, to fetch books data source second page
#   # then we scroll the new concatenated list up to 81 percent, to fetch books data source third page
#    Scenario Outline: ListView 18 - Books ListView (infinite scroll): every book rendered
#        When I scroll to view with id booksList
#        And I scroll listView with id booksList to 81 percent
#        And I scroll listView with id booksList to 81 percent
#        And I scroll listView with id booksList to position <position>
#        Then listView with id booksList at position <position> should show text: <title>
#        And listView with id booksList at position <position> should show text: <author>
#        And listView with id booksList at position <position> should show text: <collection>
#        And listView with id booksList at position <position> should show text: <bookNumber>
#        And listView with id booksList at position <position> should show text: <category>
#        And listView with id booksList at position <position> should show text: <rating>
#
#        Examples:
#            | position | category       | title                                    | author                      | collection                         | bookNumber        | rating      |
#            | 0        | Genre: Fantasy | The Final Empire                         | Author: Brandon Sanderson   | Collection: Mistborn Era 1         | Book Number: 1.0  | Rating: 4.7 |
#            | 1        | Genre: Fantasy | The Alloy of Law                         | Author: Brandon Sanderson   | Collection: Mistborn Era 2         | Book Number: 1.0  | Rating: 4.5 |
#            | 2        | Genre: Fantasy | A Game of Thrones                        | Author: George R.R. Martin  | Collection: A Song of Ice and Fire | Book Number: 1.0  | Rating: 4.8 |
#            | 3        | Genre: Fantasy | Words of Radiance                        | Author: Brandon Sanderson   | Collection: The Stormlight Archive | Book Number: 2.0  | Rating: 4.8 |
#            | 4        | Genre: Fantasy | Shadows Rising                           | Author: Madeleine Roux      | Collection: World of Warcraft      | Book Number: 24.0 | Rating: 4.3 |
#            | 5        | Genre: Fantasy | Before the Storm                         | Author: Christie Golden     | Collection: World of Warcraft      | Book Number: 23.0 | Rating: 4.6 |
#            | 6        | Genre: Fantasy | Harry Potter and the Philosopher's Stone | Author: J. K. Rowling       | Collection: Harry Potter           | Book Number: 1.0  | Rating: 4.9 |
#            | 7        | Genre: Sci-fi  | Starsight                                | Author: Brandon Sanderson   | Collection: Skyward                | Book Number: 2.0  | Rating: 4.8 |
#            | 8        | Genre: Sci-fi  | Heaven's River                           | Author: Dennis E. Taylor    | Collection: Bobiverse              | Book Number: 4.0  | Rating: 4.8 |
#            | 9        | Genre: Sci-fi  | Leviathan Wakes                          | Author: James S.A. Corey    | Collection: The Expanse            | Book Number: 1.0  | Rating: 4.7 |
#            | 10       | Genre: Sci-fi  | Dune                                     | Author: Frank Herbert       | Collection: Dune                   | Book Number: 1.0  | Rating: 4.6 |
#            | 11       | Genre: Sci-fi  | Dying of the Light                       | Author: George R.R. Martin  | 'Collection: '                     | 'Book Number: '   | Rating: 4.1 |
#            | 12       | Genre: Other   | The Last Tribe                           | Author: Brad Manuel         | 'Collection: '                     | 'Book Number: '   | Rating: 4.3 |
#            | 13       | Genre: Other   | The Cuckoo's Cry                         | Author: Caroline Overington | 'Collection: '                     | 'Book Number: '   | Rating: 4.3 |
#            | 14       | Genre: Other   | The Handmaid's Tale                      | Author: Margaret Atwood     | Collection: The Handmaid's Tale    | Book Number: 1.0  | Rating: 4.6 |
#
#    Scenario Outline: ListView 19 - Characters ListView: renders all items correctly after screen rotation
#        When I scroll listView with id charactersList to position 33
#        And I change the device orientation to landscape
#        And I scroll listView with id charactersList to position 0
#        And I change the device orientation to portrait
#        And I scroll listView with id charactersList to position <position>
#        Then listView with id charactersList at position <position> should show text: <name>
#        And listView with id charactersList at position <position> should show text: <book>
#        And listView with id charactersList at position <position> should show text: <collection>
#
#        Examples:
#            | position | name                        | book                                           | collection                         |
#            | 0        | Name: Vin                   | Book: The Final Empire                         | Collection: Mistborn Era 1         |
#            | 1        | Name: Kelsier               | Book: The Final Empire                         | Collection: Mistborn Era 1         |
#            | 2        | Name: Lord Ruler            | Book: The Final Empire                         | Collection: Mistborn Era 1         |
#            | 3        | Name: Sazed                 | Book: The Final Empire                         | Collection: Mistborn Era 1         |
#            | 4        | Name: Elend Venture         | Book: The Final Empire                         | Collection: Mistborn Era 1         |
#            | 6        | Name: Wayne                 | Book: The Alloy of Law                         | Collection: Mistborn Era 2         |
#            | 7        | Name: Marasi Colms          | Book: The Alloy of Law                         | Collection: Mistborn Era 2         |
#            | 8        | Name: Steris Harms          | Book: The Alloy of Law                         | Collection: Mistborn Era 2         |
#            | 9        | Name: Miles Dagouter        | Book: The Alloy of Law                         | Collection: Mistborn Era 2         |
#            | 11       | Name: Catelyn Stark         | Book: A Game of Thrones                        | Collection: A Song of Ice and Fire |
#            | 12       | Name: Sansa Stark           | Book: A Game of Thrones                        | Collection: A Song of Ice and Fire |
#            | 13       | Name: Arya Stark            | Book: A Game of Thrones                        | Collection: A Song of Ice and Fire |
#            | 14       | Name: Bran Stark            | Book: A Game of Thrones                        | Collection: A Song of Ice and Fire |
#            | 15       | Name: Szeth-son-son-Vallano | Book: Words of Radiance                        | Collection: The Stormlight Archive |
#            | 16       | Name: Shallan Davar         | Book: Words of Radiance                        | Collection: The Stormlight Archive |
#            | 17       | Name: Kaladin               | Book: Words of Radiance                        | Collection: The Stormlight Archive |
#            | 18       | Name: Dalinar Kholin        | Book: Words of Radiance                        | Collection: The Stormlight Archive |
#            | 19       | Name: Adolin Kholin         | Book: Words of Radiance                        | Collection: The Stormlight Archive |
#            | 20       | Name: Talanji               | Book: Shadows Rising                           | Collection: World of Warcraft      |
#            | 21       | Name: Zekhan                | Book: Shadows Rising                           | Collection: World of Warcraft      |
#            | 22       | Name: Anduin Wrynn          | Book: Shadows Rising                           | Collection: World of Warcraft      |
#            | 23       | Name: Turalyon              | Book: Shadows Rising                           | Collection: World of Warcraft      |
#            | 24       | Name: Aleria Windrunner     | Book: Shadows Rising                           | Collection: World of Warcraft      |
#            | 25       | Name: Anduin Wrynn          | Book: Before the Storm                         | Collection: World of Warcraft      |
#            | 26       | Name: Sylvanas Windrunner   | Book: Before the Storm                         | Collection: World of Warcraft      |
#            | 27       | Name: Grizzek Fizzwrench    | Book: Before the Storm                         | Collection: World of Warcraft      |
#            | 28       | Name: Sapphronetta Flivvers | Book: Before the Storm                         | Collection: World of Warcraft      |
#            | 29       | Name: Calia Menethil        | Book: Before the Storm                         | Collection: World of Warcraft      |
#            | 30       | Name: Harry Potter          | Book: Harry Potter and the Philosopher's Stone | Collection: Harry Potter           |
#            | 31       | Name: Ronald Weasley        | Book: Harry Potter and the Philosopher's Stone | Collection: Harry Potter           |
#            | 32       | Name: Hermione Granger      | Book: Harry Potter and the Philosopher's Stone | Collection: Harry Potter           |
#            | 33       | Name: Rúbeo Hagrid          | Book: Harry Potter and the Philosopher's Stone | Collection: Harry Potter           |
#
#   Scenario Outline: ListView 20 - Books ListView: evaluates context correctly after screen rotation
#       When I scroll listView with id categoriesList to position <categoriesListPosition>
#       And I scroll listView with id categoriesBooksList:<categoryId> to position 6
#       And I change the device orientation to landscape
#       And I scroll to view with id categoriesList
#       And I scroll listView with id categoriesList to position <categoriesListPosition>
#       And I scroll listView with id categoriesBooksList:<categoryId> to position 0
#       And I click on view with id cartButton:<categoryId>:<bookTitle> at position 0 of listView with id categoriesBooksList:<categoryId>
#       And I change the device orientation to portrait
#       And I scroll listView with id categoriesBooksList:<categoryId> to position <booksListPosition>
#       Then listView with id categoriesBooksList:<categoryId> at position <booksListPosition> should show text: <buttonText>
#
#       Examples:
#           |categoriesListPosition | categoryId | booksListPosition | bookTitle        | buttonText |
#           | 0                     | 1          | 0                 | The Final Empire | REMOVE     |
#           | 0                     | 1          | 1                 | The Final Empire | BUY        |
#           | 0                     | 1          | 2                 | The Final Empire | BUY        |
#           | 0                     | 1          | 3                 | The Final Empire | BUY        |
#           | 0                     | 1          | 4                 | The Final Empire | BUY        |
#           | 0                     | 1          | 5                 | The Final Empire | BUY        |
#           | 0                     | 1          | 6                 | The Final Empire | BUY        |
#           | 1                     | 2          | 0                 | Starsight        | REMOVE     |
#           | 1                     | 2          | 1                 | Starsight        | BUY        |
#           | 1                     | 2          | 2                 | Starsight        | BUY        |
#           | 1                     | 2          | 3                 | Starsight        | BUY        |
#           | 1                     | 2          | 4                 | Starsight        | BUY        |
#           | 2                     | 3          | 0                 | The Last Tribe   | REMOVE     |
#           | 2                     | 3          | 1                 | The Last Tribe   | BUY        |
#           | 2                     | 3          | 2                 | The Last Tribe   | BUY        |
#
#    Scenario: ListView 21 - Books ListView: evaluates context correctly
#        When I scroll listView with id categoriesBooksList:1 to position 6
#        And I scroll listView with id categoriesBooksList:1 to position 0
#        And I click on view with id cartButton:1:The Final Empire at position 0 of listView with id categoriesBooksList:1
#        And I scroll listView with id categoriesBooksList:1 to position 6
#        And I scroll listView with id categoriesBooksList:1 to position 0
#        Then listView with id categoriesBooksList:1 at position 0 should show text: REMOVE
