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

@container @android @ios
Feature: Container component validation

    As a Beagle developer/user
    I'd like to make sure my container component works as expected
    In order to guarantee that my application never fails

    Background:
        Given the Beagle application did launch with the container screen url

    Scenario: Container 01 - Creates a container component with some items and verify if they are
    being rendered correctly on screen

        Then the view that contains the texts with titles item1 item2 and item3 must be displayed

    Scenario: Container 02 - Create container component with context and check if the context is available for it's
    children's

        Then the views that contains the text containerContextValue1 set via context must be displayed
        Then the views that contains the text containerContextValue2 set via context must be displayed

    Scenario: Container 03 - Create container component with actions when initializing and checking those
    actions are performed. We have tested 2 setContext actions setting the global context with different values.

        Then the views that contains the text FirstActionExecuted set via context must be displayed
        Then the views that contains the text SecondActionExecuted set via context must be displayed
