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


#  Scenario: TextInput 02: validate textInput clicks and inputs
#    Then validate clicks and input types:
#      | PLACE-HOLDER                                  | VALIDATION                             |
#      | is a textInput in second                      | place holder keeps showing after click |
#      | is a textInput in second plan with expression | place holder keeps showing after click |
#      | writing date                                  | validate typed text                    |
#      | writing date with expression                  | validate typed text                    |
#      | writing e-mail                                | validate typed text                    |
#      | writing e-mail with expression                | validate typed text                    |
#      | writing password                              | validate typed text                    |
#      | writing password with expression              | validate typed text                    |
#      | writing number                                | validate typed text                    |
#      | writing number with expression                | validate typed text                    |
#      | writing text                                  | validate typed text                    |
#      | writing text with expression                  | validate typed text                    |
#      | is textInput type number                      | validate is number only textInput      |
#      | is textInput type number with expression      | validate is number only textInput      |
#
#  Scenario: TextInput 03: validate textInput events
#    Then validate textInput events:
#      | EVENT                          |
#      | DidOnFocus                     |
#      | DidOnChange                    |
#      | DidOnBlur                      |
#      | DidOnFocusDidOnChangeDidOnBlur |



#    Scenario Outline: TextInput 01 - validate value field if filled
#        Then I must check if the textInput value <textInputValue> appears on the screen
#
#        Examples:
#            | textInputValue            |
#            | TextInput test            |
#            | TextInput with expression |
#
#    Scenario Outline: TextInput 02 - validate placeholder field if filled
#        Then I must check if the textInput placeholder <textInputPlaceholder> appears on the screen
#
#        Examples:
#            | textInputPlaceholder                  |
#            | TextInput placeholder                 |
#            | TextInput placeholder with expression |
#
#    Scenario Outline: TextInput 03 - validate disabled field
#        When the disabled textInput <textInputDisabled> is visible
#        Then verify if <textInputDisabled> is disabled
#
#        Examples:
#            | textInputDisabled                                 |
#            | Standard text with disabled field                 |
#            | Standard text with disabled field with expression |
#
#    Scenario Outline: TextInput 04 - validate readOnly field
#        When the value <textInputReadOnly> of the readOnly field is on the screen
#        Then verify if the field with the value <textInputReadOnly> is read only
#
#        Examples:
#            | textInputReadOnly            |
#            | is Read Only                 |
#            | is Read Only with expression |
#
#    Scenario Outline: TextInput 05 - validate keyboard appears on Focus
#        When I click in the textInput with the placeholder <textInputSecondPlan>
#        Then verify if the text <textInputSecondPlan> is in the second plan
#
#        Examples:
#            | textInputSecondPlan                           |
#            | is a textInput in second plan                 |
#            | is a textInput in second plan with expression |
#
#    Scenario Outline: TextInput 06 - validate data entry when using "date" type
#        Then validate that the value of the text input component <textInputType.DATE> of type "date" is shown correctly
#
#        Examples:
#            | textInputType.DATE           |
#            | writing date                 |
#            | writing date with expression |
#
#    Scenario Outline: TextInput 07 - validate data entry when using "email" type
#        Then validate that the value of the text input component <textInputType.EMAIL> of type "email" is shown correctly
#
#        Examples:
#            | textInputType.EMAIL            |
#            | writing e-mail                 |
#            | writing e-mail with expression |
#
#    Scenario Outline: TextInput 08 - validate data entry when using "password" type
#        Then validate that the value of the text input component <textInputType.PASSWORD> of type "password" is shown correctly
#
#        Examples:
#            | textInputType.PASSWORD           |
#            | writing password                 |
#            | writing password with expression |
#
#    Scenario Outline: TextInput 09 - validate data entry when using "number" type
#        Then validate that the value of the text input component <textInputType.NUMBER> of type "number" is shown correctly
#
#        Examples:
#            | textInputType.NUMBER           |
#            | writing number                 |
#            | writing number with expression |
#
#    Scenario Outline: TextInput 10 - validate data entry when using "text" type
#        Then validate that the value of the text input component <textInputType.TEXT> of type "text" is shown correctly
#
#        Examples:
#            | textInputType.TEXT           |
#            | writing text                 |
#            | writing text with expression |
#
#    Scenario Outline: TextInput 11 - validate keyboard with type "number"
#        Then validate attribute of "type number" of textInput component <textInputType.NUMBER>
#
#        Examples:
#            | textInputType.NUMBER                     |
#            | is textInput type number                 |
#            | is textInput type number with expression |
#
#    Scenario: TextInput 12 - validate textInput with action onFocus
#        When I click the textInput with the placeholder action validation
#        Then the textInput with the placeholder "Unordered actions" will change its value to DidOnFocus
#
#    Scenario: TextInput 13 - validate textInput with action onChange
#        When I click the textInput with the placeholder action validation
#        And I type anything on textInput with the placeholder action validation
#        Then the textInput with the placeholder "Unordered actions" will change its value to DidOnChange
#
#    Scenario: TextInput 14 - validate textInput with action onBlur
#        When I click the textInput with the placeholder action validation
#        And I click the textInput with the placeholder is textInput type number
#        Then the textInput with the placeholder "Unordered actions" will change its value to DidOnBlur
#
#    Scenario: TextInput 15 - validate the actions of the textInput when they're executed in sequence
#        When I click the textInput with the placeholder action order
#        And I type anything on textInput with the placeholder action order
#        And I click the textInput with the placeholder is textInput type number
#        Then the textInput with the placeholder "Ordered actions" should have value DidOnFocusDidOnChangeDidOnBlur
#
#    Scenario Outline: TextInput 16 - validate that textInput is hidden
#        Then The hidden input fields <textInputHidden> should not be visible
#
#        Examples:
#            | textInputHidden                     |
#            | this text is hidden                 |
#            | this text is hidden with expression |