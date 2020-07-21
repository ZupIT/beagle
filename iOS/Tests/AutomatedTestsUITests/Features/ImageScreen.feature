#@image @regression
#Feature: Image Component Validation
#
#    As a Beagle developer/user
#    I'd like to make sure my image component works as expected
#    In order to guarantee that my application never fails
#
#
#    Scenario: Image 01 - Image component renders name attribute correctly
#        Given the app will load http://localhost:8080/image
#        When the component has a valid name attribute configured
#        Then component should render the name attribute correctly


# Snapshot Test
#    Scenario: Image 02 - Image component renders local image correctly
#        When the component has a valid local path configured
#        Then component should render the local image correctly
#
#    Scenario: Image 03 - Image component renders URL image correctly
#        When the component has a valid URL path configured
#        Then component should render the remote image correctly
#
#    Scenario: Image 04 - Image component renders contentMode FIT_XY attribute correctly
#        And the component has a valid contentMode attribute configured
#        When the contentMode is set to FIT_XY
#        Then component should render the contentMode FIT_XY attribute correctly
#
#    Scenario: Image 05 - Image component renders contentMode FIT_CENTER attribute correctly
#        And the component has a valid contentMode attribute configured
#        When the contentMode is set to FIT_CENTER
#        Then component should render the contentMode FIT_CENTER attribute correctly
#
#    Scenario: Image 06 - Image component renders contentMode CENTER_CROP attribute correctly
#        And the component has a valid contentMode attribute configured
#        When the contentMode is set to CENTER_CROP
#        Then component should render the contentMode CENTER_CROP attribute correctly
#
#    Scenario: Image 07 - Image component renders contentMode CENTER attribute correctly
#        And the component has a valid contentMode attribute configured
#        When the contentMode is set to CENTER
#        Then component should render the contentMode CENTER attribute correctly
#
