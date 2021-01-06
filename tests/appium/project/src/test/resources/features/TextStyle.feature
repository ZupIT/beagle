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

@textStyle @android @ios
Feature: Text Component Style Validation

  As a Beagle developer/user
  I'd like to make sure my text component style displays as expected
  In order to guarantee that the platform renders the elements correctly

  Background:
    Given the Beagle application did launch with the texts on the screen

  Scenario: TextStyle - Snapshot test 01
    Then take a screenshot and assert it is identical to the TextStyleScreen01 image

