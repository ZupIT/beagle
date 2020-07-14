# new feature
# Tags: optional

@button
Feature: Image Component Validation

    As a Beagle developer/user
    I'd like to make sure my image component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the image screen

    Scenario: Button 00 - Image screen opens up correctly
        Then I should see the text Beagle Button

    Scenario: Button 01 - Image component renders name attribute correctly
        When the component has a valid name attribute configured
        Then component should render the name attribute correctly

    Scenario: Button 02 - Image component renders local image correctly
        When the component has a valid local path configured
        Then component should render the local image correctly

    Scenario: Button 03 - Image component renders URL image correctly
        When the component has a valid URL path configured
        Then component should render the remote image correctly

    Scenario: Button 04 - Image component renders contentMode FIT_XY attribute correctly
        And the component has a valid contentMode attribute configured
        When the contentMode is set to FIT_XY
        Then component should render the contentMode FIT_XY attribute correctly

    Scenario: Button 05 - Image component renders contentMode FIT_CENTER attribute correctly
        And the component has a valid contentMode attribute configured
        When the contentMode is set to FIT_CENTER
        Then component should render the contentMode FIT_CENTER attribute correctly

    Scenario: Button 06 - Image component renders contentMode CENTER_CROP attribute correctly
        And the component has a valid contentMode attribute configured
        When the contentMode is set to CENTER_CROP
        Then component should render the contentMode CENTER_CROP attribute correctly

    Scenario: Button 07 - Image component renders contentMode CENTER attribute correctly
        And the component has a valid contentMode attribute configured
        When the contentMode is set to CENTER
        Then component should render the contentMode CENTER attribute correctly

