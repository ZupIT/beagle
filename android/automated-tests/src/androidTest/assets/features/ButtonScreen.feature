# new feature
# Tags: optional

@button
Feature: Button Component Validation

    As a Beagle developer/user
    I'd like to make sure my button component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the button screen

    Scenario: Button 00 - Button screen opens up correctly
        Then I should see the text Beagle Button

    Scenario: Button 01 - Button component renders text attribute correctly
        When the component has a valid text attribute configured
        Then component should render the text attribute correctly

    Scenario: Button 02 - Button component renders style attribute correctly
        When the component has a valid style attribute configured
        Then component should render the style attribute correctly

    Scenario: Button 03 - Button component renders action attribute correctly
        And the component has a valid action attribute configured
        When I click on the button component
        Then component should render the action attribute correctly