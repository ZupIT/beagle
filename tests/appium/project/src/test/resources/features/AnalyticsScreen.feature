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
    When I press the button with title Alert with no specific analytics configuration
    Then an alert dialog should appear on the screen
    When I press the OK button
    Then no analytics record should be created

  Scenario: Analytics 02 - Action with no remote analytics config and declared in the local config (should create record with params in config)
    When I press the button with title Confirm with analytics local configuration
    Then a confirm dialog should appear on the screen
    When I press the ACCEPT button
    Then an analytics record should be created with {"type":"action","platform":"WEB Angular","event":"onPress","component":{"type":"beagle:button","id":"_beagle_5","position":{"x":8,"y":44},"xPath":"BODY/APP-ROOT[1]/BEAGLE-REMOTE-VIEW/BEAGLE-SCREEN[20]/BEAGLE-CONTAINER/BEAGLE-BUTTON[4]/"},"beagleAction":"beagle:confirm","title":"Confirm Title","message":"Confirm Message","url":"/analytics2.0"}

  Scenario: Analytics 03 - Action with remote analytics config and not declared in the local config (should create record with params from remote config)
    When I press the button with title Alert with remote analytics configuration
    Then an alert dialog should appear on the screen
    When I press the OK button
    Then an analytics record should be created with {"type":"action","platform":"WEB Angular","event":"onPress","component":{"type":"beagle:button","id":"_beagle_6","position":{"x":8,"y":80},"xPath":"BODY/APP-ROOT[1]/BEAGLE-REMOTE-VIEW/BEAGLE-SCREEN[20]/BEAGLE-CONTAINER/BEAGLE-BUTTON[8]/"},"beagleAction":"beagle:alert","message":"AlertMessage","url":"/analytics2.0"}

  Scenario: Analytics 04 - Action with analytics disabled in the remote config (should not create record)
    When I press the button with title Confirm with disabled analytics configuration
    Then a confirm dialog should appear on the screen
    When I press the ACCEPT button
    Then no analytics record should be created

  Scenario: Analytics 05 - View loaded, screen analytics enabled in the config (should create record)
    Then an analytics record should be created with [{"type":"screen","platform":"WEB Angular","url":"analytics2.0"}]