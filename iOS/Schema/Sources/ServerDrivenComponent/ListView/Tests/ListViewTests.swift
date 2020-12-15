//
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
@testable import BeagleSchema
import SnapshotTesting

class ListViewTests: XCTestCase {

    func test_whenDecodingJson_thenItShouldReturnAListView() throws {
        let component: ListView = try componentFromJsonFile(fileName: "listViewComponent")
        _assertInlineSnapshot(matching: component, as: .dump, with: """
        ▿ ListView
          - context: Optional<Context>.none
          ▿ dataSource: Expression<Array<DynamicObject>>
            ▿ value: 1 element
              - 
          ▿ direction: Optional<Direction>
            - some: Direction.vertical
          ▿ iteratorName: Optional<String>
            - some: "\(component.iteratorName ?? "")"
          - key: Optional<String>.none
          - onInit: Optional<Array<RawAction>>.none
          - onScrollEnd: Optional<Array<RawAction>>.none
          - scrollEndThreshold: Optional<Int>.none
          ▿ template: Container
            ▿ children: 3 elements
              ▿ UnknownComponent
                - type: "custom:beagleschematestscomponent"
              ▿ UnknownComponent
                - type: "custom:beagleschematestscomponent"
              ▿ UnknownComponent
                - type: "custom:beagleschematestscomponent"
            - context: Optional<Context>.none
            - onInit: Optional<Array<RawAction>>.none
            ▿ widgetProperties: WidgetProperties
              - accessibility: Optional<Accessibility>.none
              - id: Optional<String>.none
              ▿ style: Optional<Style>
                ▿ some: Style
                  - backgroundColor: Optional<String>.none
                  - borderColor: Optional<String>.none
                  - borderWidth: Optional<Double>.none
                  - cornerRadius: Optional<CornerRadius>.none
                  - display: Optional<Expression<Display>>.none
                  ▿ flex: Optional<Flex>
                    ▿ some: Flex
                      - alignContent: Optional<AlignContent>.none
                      - alignItems: Optional<AlignItems>.none
                      - alignSelf: Optional<AlignSelf>.none
                      - basis: Optional<UnitValue>.none
                      - flex: Optional<Double>.none
                      ▿ flexDirection: Optional<FlexDirection>
                        - some: FlexDirection.column
                      - flexWrap: Optional<Wrap>.none
                      - grow: Optional<Double>.none
                      - justifyContent: Optional<JustifyContent>.none
                      - shrink: Optional<Double>.none
                  - margin: Optional<EdgeValue>.none
                  - padding: Optional<EdgeValue>.none
                  - position: Optional<EdgeValue>.none
                  - positionType: Optional<PositionType>.none
                  - size: Optional<Size>.none
          ▿ widgetProperties: WidgetProperties
            - accessibility: Optional<Accessibility>.none
            - id: Optional<String>.none
            ▿ style: Optional<Style>
              ▿ some: Style
                - backgroundColor: Optional<String>.none
                - borderColor: Optional<String>.none
                - borderWidth: Optional<Double>.none
                - cornerRadius: Optional<CornerRadius>.none
                - display: Optional<Expression<Display>>.none
                - flex: Optional<Flex>.none
                - margin: Optional<EdgeValue>.none
                - padding: Optional<EdgeValue>.none
                - position: Optional<EdgeValue>.none
                - positionType: Optional<PositionType>.none
                - size: Optional<Size>.none
        """)
    }

}
