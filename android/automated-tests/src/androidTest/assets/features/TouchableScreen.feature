#@touchable @regression
#Feature: Touchable Component Validation
#
#    As a Beagle developer/user
#    I'd like to make sure my touchable component works as expected
#    In order to guarantee that my application never fails
#
#    Background:
#        Given that I'm on the touchable screen
#
#    Scenario: Touchable 01 - Touchable component renders text attribute correctly
#        Then touchable screen should render all text attributes correctly
#
#    Scenario: Touchable 02 - Touchable component performs action click on text correctly
#        And I have a text with touchable configured
#        When I click on touchable text Click here!
#        Then component should render the action attribute correctly
#
#    Scenario: Touchable 03 - Touchable component performs action click on image correctly
#        And I have an image with touchable configured
#        When I click on touchable image
#        Then component should render the action attribute correctly