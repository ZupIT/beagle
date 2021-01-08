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

@touchable @android @ios
Feature: Touchable Component Validation

    As a Beagle developer/user
    I'd like to make sure my touchable component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the touchable screen

    Scenario: Touchable 01 - Touchable component renders text attribute correctly
        Then touchable screen should render all text attributes correctly

    Scenario: Touchable 02 - Touchable component performs action click on text correctly
        And I have a text with touchable configured
        When I click on touchable text Click here!
        Then component should render the action attribute correctly

    Scenario: Touchable 03 - Touchable component performs action click on image correctly
        And I have an image with touchable configured
        When I click on touchable image
        Then component should render the action attribute correctly