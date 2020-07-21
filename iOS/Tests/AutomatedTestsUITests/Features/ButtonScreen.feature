@button @regression
Feature: Button Component Validation

    As a Beagle developer/user
    I'd like to make sure my button component works as expected
    In order to guarantee that my application never fails


    Scenario: Button 01 - Button component renders text attribute correctly
    Given the app will load http://localhost:8080/button
    Then all my button components should render their respective text attributes correctly

    Scenario: Button 02 - Button component renders action attribute correctly
    Given the app will load http://localhost:8080/button
    When I click on a component with a valid action attribute configured
    Then component should render the action attribute correctly


# Snapshot Test
#    Scenario: Button 00 - Button component renders style attribute correctly
#    Given that I'm on the button screen
#    When I click on a component with a valid style attribute configured
#    Then component should render the style attribute correctly
