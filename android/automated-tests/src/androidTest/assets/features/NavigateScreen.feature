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

@navigation @regression
Feature: Navigation Action Validation

    As a Beagle developer/user
    I'd like to make sure my navigation actions work as expected
    In order to guarantee that my application never fails

    Background:
        Given the Beagle application did launch with the navigation screen url

    Scenario Outline: Navigation 01 - The push actions navigate to a valid route
        When I press a navigation button <title>
        Then the screen should navigate to another screen with the text label <text>

        Examples:
            | title                     | text                            |
            | PushStackRemote           | PushStackRemoteScreen           |
            | PushViewRemote            | PushViewRemoteScreen            |
            | PushStackRemoteExpression | PushStackRemoteExpressionScreen |
            | PushViewRemoteExpression  | PushViewRemoteExpressionScreen  |

    Scenario Outline: Navigation 02 - The push actions navigate to an invalid route
        When I press a navigation failure button <title>
        Then the screen should not navigate to another screen with the text label <text>

        Examples:
            | title                            | text                                   |
            | PushStackRemoteFailure           | PushStackRemoteFailureScreen           |
            | PushViewRemoteFailure            | PushViewRemoteFailureScreen            |
            | PushViewRemoteExpressionFailure  | PushViewRemoteExpressionFailureScreen  |
            | PushStackRemoteExpressionFailure | PushStackRemoteExpressionFailureScreen |


    Scenario Outline: Navigation 03 - 'popView' action dismisses the current screen
        When I press a navigation button <title>
        Then the screen should navigate to another screen with the text label <text>
        And I click on <pop> button
        Then the app should dismiss the view that contains <text>

        Examples:
            | title           | text                  | pop     |
            | PushStackRemote | PushStackRemoteScreen | PopView |

    Scenario Outline: Navigation 04 - 'popToView' action navigates to a specified route of a screen
    on the stack and cleans up the navigation that was generated from this screen
        When I press a navigation button <title>
        Then the screen should navigate to another screen with the text label <text>
        And I click on <pop> button
        Then the app should dismiss the view that contains <text>
        Then the view that contains the <title> must still exist

        Examples:
            | title           | text                  | pop       |
            | PushViewRemote  | PushViewRemoteScreen  | PopToView |

    Scenario Outline: Navigation 05 - 'popStack' action removes the current stack of views
        When I press a navigation button <title>
        Then the screen should navigate to another screen with the text label <text>
        And I click on <pop> button
        Then the app should dismiss the view that contains <text>
        Then the view that contains the <title> must still exist

        Examples:
            | title           | text                  | pop      |
            | PushStackRemote | PushStackRemoteScreen | PopStack |

    Scenario Outline: Navigation 06 - 'ResetStack' Opens a new screen with an informed
    route for a new Stack flow and cleans the previous stack where the previous screen were contained.

        When I press a navigation button <title>
        Then the screen should navigate to another screen with the text label <text>
        And I click on <reset> button
        Then the screen should navigate to another screen with the text label <resetPage>

        Examples:
            | title           | text                  | reset            | resetPage    |
            | PushViewRemote  | PushViewRemoteScreen  | ResetStack       | Reset Screen |
            | PushStackRemote | PushStackRemoteScreen | ResetApplication | Reset Screen |

    Scenario Outline: Navigation 07 - 'ResetApplication' Opens a new screen with an informed
    route for a new Stack flow and cleans all previous stacks.

        When I press a navigation button <title>
        Then the screen should navigate to another screen with the text label <text>
        And I click on <reset> button
        Then the screen should navigate to another screen with the text label <resetPage>

        Examples:
            | title           | text                  | reset                      | resetPage    |
            | PushStackRemote | PushStackRemoteScreen | ResetApplication           | Reset Screen |
            | PushStackRemote | PushStackRemoteScreen | ResetApplicationExpression | Reset Screen |

    Scenario Outline: Navigation 08 - 'ResetApplicationExpressionFallback' Try to open a new screen with an invalid
    route for a new Stack flow cleaning all previous stacks and navigate into a fallbackScreen

        When I press a navigation button <title>
        Then the screen should navigate to another screen with the text label <text>
        And I click on <reset> button
        Then the screen should navigate to another screen with the text label <resetPage>

        Examples:
            | title           | text                  | reset                      | resetPage    |
            | PushStackRemote | PushStackRemoteScreen | ResetApplication           | Reset Screen |
            | PushStackRemote | PushStackRemoteScreen | ResetApplicationExpression | Reset Screen |

    Scenario Outline: Navigation 09 - 'PushStack, ResetStack, ResetApplication' This component could be called
    from an other Beagle Server Driven Activity (SDA) rather than the basic one. The new Beagle Server Driven Activity must be annotated with the
    "@RegisterController" annotation that will hold de controllerId that must be listed on the action above.
    The retry button is called only on this other Beagle Activity

        When I press a navigation failure button <title>
        Then There must be a retry button with text <retry>

        Examples:
            | title                                     | retry |
            | PushStackRemoteFailure                    | RETRY |
            | ResetStackOtherSDAFailsToShowButton       | RETRY |
            | ResetApplicationOtherSDAFailsToShowButton | RETRY |


    Scenario Outline: Navigation 10 - 'ResetStack, ResetApplication' These components could be called
    from a different Beagle Activity rather than the basic one. Here they are called from the
    Default Beagle Server Driven Activity. When this happens they must trigger
    a TRY AGAIN button on a failed attempt.

        When I press a navigation failure button <title>
        Then There must be a retry button with text <tryAgain>

        Examples:
            | title                   | tryAgain  |
            | ResetStackSameSDA       | TRY AGAIN |
            | ResetApplicationSameSDA | TRY AGAIN |

    Scenario Outline: Navigation 11 - 'PopToView' Must not open a new page since the route is invalid
    The expected behaviour is to remain on the same page.
        When I press a navigation button <title>
        Then the screen should navigate to another screen with the text label <text>
        When I click on button <popToViewInvalidRoute>
        Then the view that contains the <text> must still exist

        Examples:
            | title           | text                  | popToViewInvalidRoute |
            | PushViewRemote  | PushViewRemoteScreen  | PopToViewInvalidRoute |