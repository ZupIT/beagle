@simpleform @regression
Feature: SimpleForm Component Validation

    As a Beagle developer/user
    I'd like to make sure my SimpleForm component works as expected
    In order to guarantee that my application never fails


    Background:
        Given that I'm on the simple form screen

    Scenario: SimpleForm 01 - SimpleForm component renders text attribute correctly
        Then all my simple form components should render their respective text attributes correctly

    Scenario Outline: SimpleForm 02 - SimpleForm component renders action attribute correctly
        When I click on input with hint <textFieldText>
        And insert text <textValue>
        And I click on input with hint Street
        And hide keyboard
        And I click on button Enviar
        Then confirm popup should appear correctly

        Examples:
            |textFieldText  | textValue |
            |ZIP            | 38408480  |
    
 #   |Street         |
 #   |Number         |
 #   |Neighborhood   |
 #   |City           |
 #   |State          |
 #   |Complement     |



