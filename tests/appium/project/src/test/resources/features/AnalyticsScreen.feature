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

@analytics2.0 @android @ios
Feature: Analytics validation
  As a Beagle developer/user
  I'd like to make sure my Analytics works as expected
  In order to guarantee that my application never fails

  Background:
    Given the Beagle application did launch with the Analytics screen url

  Scenario: Analytics 01 - Action with no remote analytics config and not declared in the local config (should not create record)
    When I click on text Alert with no specific analytics configuration
    Then a dialog should appear on the screen with text AlertMessage
    When I click on text OK
    Then no analytics record should be created

  Scenario: Analytics 02 - Action with no remote analytics config and declared in the local config (should create record with params in config)
    When I click on text Confirm with analytics local configuration
    Then a dialog should appear on the screen with text Confirm Message
    When I click on text ACCEPT
    Then an analytics record should be created for Analytics 02

  Scenario: Analytics 03 - Action with remote analytics config and not declared in the local config (should create record with params from remote config)
    When I click on text Alert with remote analytics configuration
    Then a dialog should appear on the screen with text AlertMessage
    When I click on text OK
    Then an analytics record should be created for Analytics 03

  Scenario: Analytics 04 - Action with analytics disabled in the remote config (should not create record)
    When I click on text Confirm with disabled analytics configuration
    Then a dialog should appear on the screen with text Confirm Message
    When I click on text ACCEPT
    Then no analytics record should be created

  Scenario: Analytics 05 - View loaded, screen analytics enabled in the config (should create record)
    When I click on text navigateToPage2
    When I click on text navigate to local screen
    Then an analytics record should be created for Analytics 05