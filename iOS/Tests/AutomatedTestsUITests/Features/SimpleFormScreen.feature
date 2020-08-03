@simpleform @regression
Feature: SimpleForm Component Validation

    As a Beagle developer/user
    I'd like to make sure my SimpleForm component works as expected
    In order to guarantee that my application never fails


    Scenario: SimpleForm 01 - SimpleForm component renders text attribute correctly
    Given the app will load http://localhost:8080/simpleform
    Then all my simple form components should render their respective text attributes correctly
    
    Scenario Outline: SimpleForm 02 - SimpleForm component renders action attribute correctly
    Given the app will load http://localhost:8080/simpleform
    When I click on text field "<textFieldText>"
    And insert text "<textValue>"
    And I click on text field "Street"
    And I click on button "Enviar"
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



