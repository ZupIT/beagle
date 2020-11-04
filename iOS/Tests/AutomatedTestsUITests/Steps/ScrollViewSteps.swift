/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Foundation
import XCTest

class ScrollViewSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    let device = XCUIDevice.shared
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("scrollview") ?? false {
                let url = "http://localhost:8080/scrollview"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given("^the Beagle application did launch with the scrollview screen url$") { _, _ -> Void in
            XCTAssertTrue(self.application.staticTexts[Seeds.screenTitle].exists)
        }
        
        // MARK: - When
        
        //Scenario 1
        When(#"^I change the screen orientation to "([^\"]*)" and I press on "([^\"]*)" scrollable text$"#) { args, _ -> Void in
            let orientation = args![0]
            let text = args![1]
            self.device.orientation = orientation == Seeds.horizontalOrientation ? .portrait : .landscapeRight
            self.application.staticTexts[text].tap()
        }
        
        // MARK: - Then
        
        // Scenario 1
        Then(#"^the current text "([^\"]*)" should be replaced for a large text and It should scroll to the "([^\"]*)" button for tapping it$"#) { args, _ -> Void in
            let text = args![0]
            let buttonTitle = args![1]            
            XCTAssertFalse(self.application.staticTexts[text].exists)
            
            let largeText = Seeds.largeText
            let predicate = NSPredicate(format: "label LIKE %@", largeText)
            let element = self.application.staticTexts.containing(predicate).element
            XCTAssertEqual(element.label, largeText)
            
            let button = self.application.buttons[buttonTitle]
            XCTAssertFalse(button.isHittable)
            
            button.tap()
            XCTAssertTrue(button.isHittable)
        }
        
        // MARK: - After
        after { _ in
            self.device.orientation = .portrait
        }
    }
}

// MARK: - Helpers
fileprivate struct Seeds {
    static let screenTitle = "Beagle ScrollView"
    static let horizontalOrientation = "horizontal"
    static let verticalOrientation = "vertical"
    static let largeText = "Lorem ipsum diam luctus mattis arcu accumsan, at curabitur hac in dictum senectus neque, orci lorem aenean euismod leo. eu nunc tellus proin eget euismod lorem curabitur habitant nisi himenaeos habitasse at quam, convallis potenti scelerisque aenean habitant viverra mollis fusce convallis dui urna aliquam. diam tristique etiam fermentum etiam nunc eget vel, ante nam eleifend habitant per senectus diam, bibendum lectus enim ultrices litora viverra. lorem fusce leo hendrerit himenaeos elementum aliquet nec, vestibulum luctus pretium diam tellus ligula conubia elit, a sodales torquent fusce massa euismod. et magna imperdiet conubia sed netus vitae justo maecenas proin lorem, sapien nisi porttitor dolor facilisis pharetra nam class. Morbi nullam odio accumsan quam urna sit tortor vulputate mi fames, elit molestie gravida ipsum dictumst aenean curabitur ultrices consectetur pharetra, auctor aenean diam pellentesque condimentum risus diam scelerisque rutrum. conubia sem tincidunt cras venenatis tristique nisl duis rhoncus blandit, sed mattis vulputate accumsan suscipit tristique imperdiet dui, ornare ipsum tempor viverra elementum consectetur euismod dapibus. ultricies in consectetur libero nam ultrices egestas quis volutpat ut nec sagittis eu, elementum malesuada ullamcorper dapibus donec aenean mattis odio mi nulla gravida. tellus metus imperdiet justo mattis eros sodales potenti nibh nisl tincidunt, metus etiam cubilia amet donec primis sapien erat dictumst. Accumsan etiam himenaeos tempor integer habitasse curae ac, tincidunt laoreet taciti nisl habitasse conubia, maecenas nec velit vitae amet varius. scelerisque vel fringilla consequat justo curabitur nam massa vitae, tempus tempor sit torquent massa malesuada ullamcorper, laoreet elementum nam pharetra tempus nam mauris. sociosqu dictum malesuada lectus suscipit ullamcorper aliquet pulvinar semper laoreet, vulputate aliquam nibh odio donec ligula bibendum suspendisse, facilisis ut lobortis lacus tortor hendrerit integer posuere. phasellus egestas dui hac auctor faucibus purus accumsan arcu, sem vivamus rhoncus pharetra aliquam ornare curabitur rutrum, ut venenatis proin iaculis orci gravida molestie. "
}
