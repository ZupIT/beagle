
@tabview @regression
Feature: TabView Component Validation

    As a Beagle developer/user
    I'd like to make sure my tabView component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the tabview screen

    Scenario: Tabview 01 - Tabview component renders name attribute correctly
        When the component has a valid tabItems attribute configured
        Then component should render the tabItems attribute correctly