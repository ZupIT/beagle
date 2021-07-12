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

@navigation @android @ios
Feature: Navigation Action Validation

  As a Beagle developer/user
  I'd like to make sure my navigation actions work as expected

  Background:
    Given the Beagle application did launch with the navigation screen url


  Scenario: Navigation 01 - Screen title validation
    Then validate PushStack and PushView screen titles:
      | BUTTON-1 (main screen)    | NEW SCREEN TITLE                |
      | PushStackRemote           | PushStackRemoteScreen           |
      | PushViewRemote            | PushViewRemoteScreen            |
      | PushStackRemoteExpression | PushStackRemoteExpressionScreen |
      | PushViewRemoteExpression  | PushViewRemoteExpressionScreen  |

#   Closeable screen on iOS: closeable by swiping down or click on the button close (when present). Returns to main screen
#   Not closeable screen on iOS: swiping down doesn't take effect. No close button
#   Closeable screen on Android: closeable by going back, returns to main screen
#   Not closeable screen on Android: going back closes the app
  Scenario: Navigation 02: PushStackRemote and PushViewRemote actions validation
    Then validate PushStack and PushView actions:
      | BUTTON-1 (main screen) | BUTTON-2                           | IOS-ACTION                  | ANDROID-ACTION              |
      # Push Stack Remote actions
      | PushStackRemote        | PopToViewInvalidRoute              | no action                   | no action                   |
      | PushStackRemote        | PopToView                          | no action                   | no action                   |
      | PushStackRemote        | PopStack                           | goes back to main screen    | goes back to main screen    |
      | PushStackRemote        | PopView                            | goes back to main screen    | goes back to main screen    |
      | PushStackRemote        | ResetStack                         | opens a closable screen     | opens a closable screen     |
      | PushStackRemote        | ResetApplication                   | opens a non-closable screen | opens a non-closable screen |
      | PushStackRemote        | ResetStackExpression               | opens a closable screen     | opens a closable screen     |
      | PushStackRemote        | ResetApplicationExpression         | opens a non-closable screen | opens a non-closable screen |
      | PushStackRemote        | ResetStackExpressionFallback       | opens a closable screen     | opens a closable screen     |
      | PushStackRemote        | ResetApplicationExpressionFallback | opens a non-closable screen | opens a non-closable screen |
        # Push View Remote actions
      | PushViewRemote         | PopToViewInvalidRoute              | no action                   | no action                   |
      | PushViewRemote         | PopToView                          | goes back to main screen    | goes back to main screen    |
      | PushViewRemote         | PopStack                           | no action                   | closes the app              |
      | PushViewRemote         | PopView                            | goes back to main screen    | goes back to main screen    |
      | PushViewRemote         | ResetStack                         | opens a non-closable screen | opens a non-closable screen |
      | PushViewRemote         | ResetApplication                   | opens a non-closable screen | opens a non-closable screen |
      | PushViewRemote         | ResetStackExpression               | opens a non-closable screen | opens a non-closable screen |
      | PushViewRemote         | ResetApplicationExpression         | opens a non-closable screen | opens a non-closable screen |
      | PushViewRemote         | ResetStackExpressionFallback       | opens a non-closable screen | opens a non-closable screen |
      | PushViewRemote         | ResetApplicationExpressionFallback | opens a non-closable screen | opens a non-closable screen |


  Scenario: Navigation 03 - Error route validation
    Then validate PushStack and PushView error routes:
      | BUTTON-1 (main screen)                    | IOS-BUTTON-TITTLE | ANDROID-BUTTON-TITTLE |
      | PushStackRemoteFailure                    | Try again         | RETRY                 |
      | PushViewRemoteFailure                     | Try again         | RETRY                 |
      | PushViewRemoteExpressionFailure           | Try again         | RETRY                 |
      | PushStackRemoteExpressionFailure          | Try again         | RETRY                 |
      | ResetStackOtherSDAFailsToShowButton       | Try again         | RETRY                 |
      | ResetApplicationOtherSDAFailsToShowButton | Try again         | RETRY                 |
      | ResetStackSameSDA                         | Try again         | TRY AGAIN             |
      | ResetApplicationSameSDA                   | Try again         | TRY AGAIN             |

