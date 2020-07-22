
@listview @regression
Feature: ListView Component Validation

    As a Beagle developer/user
    I'd like to make sure my listView component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the listview screen

    Scenario: ScrollView 01 - listView component renders text attribute correctly
        Then listview screen should render all text attributes correctly

    Scenario: ScrollView 02 - listView component performs vertical scroll correctly
        When I have a vertical list configured
        Then listview screen should perform the scroll action vertically

#    Scenario: ScrollView 03 - listView component performs horizontal scroll correctly
#        When I have a horizontal list configured
#        Then listview screen should perform the scroll action horizontally