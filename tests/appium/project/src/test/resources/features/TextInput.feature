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

@textInput @android @ios
Feature: TextInput Validation

  As a Beagle developer/user
  I'd like to make sure my textInput work as expected

  Background:
    Given the Beagle application did launch with the textInput on screen

  Scenario: TextInput 01: validate textInput place holders and visibility
    Then validate place holders and visibility:
      | PLACE-HOLDER                                      | VISIBILITY | ENABLED |
      | TextInput test                                    | true       | true    |
      | TextInput with expression                         | true       | true    |
      | TextInput placeholder                             | true       | true    |
      | TextInput placeholder with expression             | true       | true    |
      | Standard text with disabled field                 | true       | false   |
      | Standard text with disabled field with expression | true       | false   |
      | is Read Only                                      | true       | false   |
      | is Read Only with expression                      | true       | false   |
      | this text is hidden                               | false      | false   |
      | this text is with expression                      | false      | false   |


  Scenario: TextInput 02: validate textInput clicks and inputs
    Then validate clicks and input types:
      | PLACE-HOLDER                                  | VALIDATION                             |
      | is a textInput in second plan                 | place holder keeps showing after click |
      | is a textInput in second plan with expression | place holder keeps showing after click |
      | writing date                                  | validate typed text                    |
      | writing date with expression                  | validate typed text                    |
      | writing e-mail                                | validate typed text                    |
      | writing e-mail with expression                | validate typed text                    |
      | writing password                              | validate typed text                    |
      | writing password with expression              | validate typed text                    |
      | writing number                                | validate typed text                    |
      | writing number with expression                | validate typed text                    |
      | writing text                                  | validate typed text                    |
      | writing text with expression                  | validate typed text                    |
      | is textInput type number                      | validate is number only textInput      |
      | is textInput type number with expression      | validate is number only textInput      |

  Scenario: TextInput 03: validate textInput events
    Then validate textInput events:
      | EVENT                          |
      | DidOnFocus                     |
      | DidOnChange                    |
      | DidOnBlur                      |
      | DidOnFocusDidOnChangeDidOnBlur |

