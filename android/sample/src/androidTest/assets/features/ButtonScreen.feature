@button @regression
Feature: Button Component Validation

    As a Beagle developer/user
    I'd like to make sure my button component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the button screen

    Scenario: Button 01 - Button component renders text attribute correctly
        Then all my button components should render their respective text attributes correctly

    Scenario Outline: Button 02 - Button component renders action attribute correctly
        When I click on button <buttonText>
        Then component should render the action attribute correctly

        Examples:
            |buttonText                       |
            |Button                           |
            |Button with style                |
            |Button with Appearance           |