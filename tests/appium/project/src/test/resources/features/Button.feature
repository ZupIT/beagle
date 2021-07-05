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

@button @android @ios
Feature: Button Component Validation

  As a Beagle developer/user
  I'd like to make sure my button component works as expected

  Background:
    Given that I'm on the button screen

  Scenario: Button 01 - Button components render action attributes correctly
    Then validate button clicks:
      | BUTTON-TEXT                      | ACTION   |
      | Button                           | New page |
      | Button with style                | New page |
      | Button with Appearance           | New page |
      | BUTTON WITH APPEARANCE AND STYLE | New page |
      | Disabled Button                  | Disabled |
      | Disabled Button by context       | Disabled |
