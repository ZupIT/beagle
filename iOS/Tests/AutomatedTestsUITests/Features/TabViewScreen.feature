@tabview @regression
Feature: TabView Component Validation

    As a Beagle developer/user
    I'd like to make sure my tabView component works as expected
    In order to guarantee that my application never fails


    Scenario: TabView 01 - TabView component renders text attribute correctly
        Given the app will load http://localhost:8080/tabview
        Then my tabview components should render their respective tabs attributes correctly
        
#    Scenario: TabView 02 - Tabs render all text attribute correctly
#        Given the app will load http://localhost:8080/tabview
#        When I click on "Tab 2" text
#        Then my tab should render the text "Welcome to Tab 1" correctly

    Scenario Outline: TabView 02 - Tabs render all text attribute correctly
        Given the app will load http://localhost:8080/tabview
        When I click on "<text1>"
        Then my tab should render the text "<text2>" and "<text3>" correctly

        Examples:
        | text1 | text2            | text3                      |
        |Tab 1  | Welcome to Tab 1 | This is Tab1's second text |
        |Tab 2  | Welcome to Tab 2 | This is Tab2's second text |
        |TAB 3  | Welcome to Tab 3 | This is Tab3's second text |
        |TAB 4  | Welcome to Tab 4 | This is Tab4's second text |
