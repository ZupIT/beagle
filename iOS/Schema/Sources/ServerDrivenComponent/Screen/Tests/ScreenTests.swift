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

import XCTest
@testable import Schema
import SnapshotTesting

class ScreenTests: XCTestCase {
    
    func testFirstScreenSample() {
        // given
        let screen = Screen(
            safeArea: .init(top: true, leading: false, bottom: true, trailing: false),
            navigationBar: .init(
                title: "Title of Screen",
                style: nil,
                showBackButton: true,
                backButtonAccessibility: .init(accessibilityLabel: "Back button", accessible: true),
                navigationBarItems: [
                    .init(text: "Edit", action: ShowNativeDialog(title: "You will be redirect to edit screen", message: "", buttonText: "Next"))
                ]
            ),
            child: ScrollView(
                children: [
                    Container(
                        children: [
                            NetworkImage(
                                path: "path",
                                contentMode: .center,
                                widgetProperties: .init()
                            ),
                            Text("Description of Screen",
                                 style: "some style",
                                 alignment: .center,
                                 textColor: "#FFFFFFFF",
                                 widgetProperties: .init()
                            )
                        ]),
                    Button(
                        text: "Button Title",
                        style: "some style",
                        action: CustomAction(name: "Some Custom Action",
                                             data: ["dataKey": "dataValue"]),
                        clickAnalyticsEvent: AnalyticsClick(category: "some category", label: "some label", value: "some value"),
                        widgetProperties: WidgetProperties()),
                    Spacer(10.0)
                ],
                scrollDirection: .horizontal,
                scrollBarEnabled: false,
                appearance: nil)
        )
        
        // then
        assertSnapshot(matching: screen, as: .dump)
    }
    
    func testSecondScreenSample() {
        // given
        let screen = Screen(
            screenAnalyticsEvent: AnalyticsScreen(screenName: "Screen Name"),
            child: ScrollView(
                children: [
                    Touchable(
                        action: FormValidation(
                            errors: [.init(inputName: "name", message: "message")]
                        ),
                        child: Text("Form validation action",
                                    style: "some style",
                                    alignment: .center,
                                    textColor: "#FFFFFFFF",
                                    widgetProperties: .init()
                    )),
                    Button(
                        text: "Button Title",
                        style: "some style",
                        action: CustomAction(name: "Some Custom Action",
                                             data: ["dataKey": "dataValue"]),
                        clickAnalyticsEvent: AnalyticsClick(category: "some category", label: "some label", value: "some value"),
                        widgetProperties: WidgetProperties()),
                    Form(
                        action: FormRemoteAction(path: "some path", method: .post),
                        child: ListView(
                            rows: [
                                Text("first row"),
                                Text("second row"),
                                LazyComponent(
                                    path: "image",
                                    initialState: Image(
                                        name: "image",
                                        contentMode: .fitCenter,
                                        widgetProperties: .init()
                                    )
                                )
                            ],
                            direction: .horizontal) ,
                        group: "",
                        additionalData: ["additionalDataKey": "additionalDataValue"],
                        shouldStoreFields: true
                    )
                ],
                scrollDirection: .horizontal,
                scrollBarEnabled: false,
                appearance: nil)
        )
        
        // then
        assertSnapshot(matching: screen, as: .dump)
    }
    
    func testThirdScreenSample() {
        // given
        let screen = Screen(
            screenAnalyticsEvent: AnalyticsScreen(screenName: "Screen Name"),
            child: PageView(
                pages: [
                    WebView(url: "some url", flex: Flex.createMock())
                ],
                pageIndicator: nil
            )
        )
        
        // then
        assertSnapshot(matching: screen, as: .dump)
    }
    
    func testFourthScreenSample() {
        // given
        let screen = Screen(
            screenAnalyticsEvent: AnalyticsScreen(screenName: "Screen Name"),
            child: TabView(
                tabItems: [
                    TabItem(
                        icon: "",
                        title: "",
                        content: Container(
                            children: [
                                Text("")
                            ])
                    ),
                    TabItem(
                        content: Container(
                            children: [
                                Text("")
                            ])
                    )
                ],
                style: nil)
        )
        
        // then
        assertSnapshot(matching: screen, as: .dump)
    }
}
