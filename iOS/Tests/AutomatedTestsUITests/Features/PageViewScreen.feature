@pageview @regression
Feature: PageView Component Validation

    As a Beagle developer/user
    I'd like to make sure my pageView component works as expected
    In order to guarantee that my application never fails


    Scenario: PageView 01 - PageView component renders text attribute correctly
        Given the app will load http://localhost:8080/pageview
        Then my pageview components should render their respective pages attributes correctly
