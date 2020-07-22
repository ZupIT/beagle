
@scrollview @regression
Feature: ScrollView Component Validation

    As a Beagle developer/user
    I'd like to make sure my scrollView component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the scrollview screen

    Scenario: ScrollView 01 - scrollView component renders text attribute correctly
        Then scrollview screen should render all text attributes correctly

    Scenario: ScrollView 02 - scrollView component performs vertical scroll correctly
        When I have a vertical scroll configured
        Then scrollview screen should perform the scroll action vertically

    Scenario: ScrollView 03 - scrollView component performs horizontal scroll correctly
        When I have a horizontal scroll configured
        Then scrollview screen should perform the scroll action horizontally