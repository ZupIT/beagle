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

// MARK: - Style Builders
public extension Style {
    @discardableResult
    func backgroundColor(_ backgroundColor: String) -> Style {
        self.backgroundColor = backgroundColor
        return self
    }

    @discardableResult
    func cornerRadius(_ cornerRadius: CornerRadius) -> Style {
        self.cornerRadius = cornerRadius
        return self
    }
    
    @discardableResult
    func borderColor(_ borderColor: String) -> Style {
        self.borderColor = borderColor
        return self
    }
    
    @discardableResult
    func borderWidth(_ borderWidth: Double) -> Style {
        self.borderWidth = borderWidth
        return self
    }
    
    @discardableResult
    func size(_ size: Size) -> Style {
        self.size = size
        return self
    }

    @discardableResult
    func margin(_ margin: EdgeValue) -> Style {
        self.margin = margin
        return self
    }

    @discardableResult
    func padding(_ padding: EdgeValue) -> Style {
        self.padding = padding
        return self
    }

    @discardableResult
    func position(_ position: EdgeValue) -> Style {
        self.position = position
        return self
    }
    
    @discardableResult
    func positionType(_ positionType: PositionType) -> Style {
        self.positionType = positionType
        return self
    }
    
    @discardableResult
    func display(_ display: Style.Display) -> Style {
        return self.display(.value(display))
    }
    
    @discardableResult
    func display(_ display: Expression<Style.Display>) -> Style {
        self.display = display
        return self
    }

    @discardableResult
    func flex(_ flex: Flex) -> Style {
        self.flex = flex
        return self
    }
}

// MARK: - Flex Builders
import BeagleSchema

public extension Flex {
    @discardableResult
    func flexDirection(_ flexDirection: FlexDirection) -> Flex {
        self.flexDirection = flexDirection
        return self
    }
    
    @discardableResult
    func flexWrap(_ flexWrap: Wrap) -> Flex {
        self.flexWrap = flexWrap
        return self
    }
    
    @discardableResult
    func justifyContent(_ justifyContent: JustifyContent) -> Flex {
        self.justifyContent = justifyContent
        return self
    }
    
    @discardableResult
    func alignItems(_ alignItems: AlignItems) -> Flex {
        self.alignItems = alignItems
        return self
    }
    
    @discardableResult
    func alignSelf(_ alignSelf: AlignSelf) -> Flex {
        self.alignSelf = alignSelf
        return self
    }
    
    @discardableResult
    func alignContent(_ alignContent: AlignContent) -> Flex {
        self.alignContent = alignContent
        return self
    }
    
    @discardableResult
    func basis(_ basis: UnitValue) -> Flex {
        self.basis = basis
        return self
    }
    
    @discardableResult
    func flex(_ flex: Double) -> Flex {
        self.flex = flex
        return self
    }
    
    @discardableResult
    func grow(_ grow: Double) -> Flex {
        self.grow = grow
        return self
    }
    
    @discardableResult
    func shrink(_ shrink: Double) -> Flex {
        self.shrink = shrink
        return self
    }
    
}

// MARK: - Size Builders

public extension Size {
    @discardableResult
    func width(_ value: UnitValue) -> Size {
        self.width = value
        return self
    }
    
    @discardableResult
    func height(_ value: UnitValue) -> Size {
        self.height = value
        return self
    }
    
    @discardableResult
    func maxWidth(_ value: UnitValue) -> Size {
        self.maxWidth = value
        return self
    }
    
    @discardableResult
    func maxHeight(_ value: UnitValue) -> Size {
        self.maxHeight = value
        return self
    }
    
    @discardableResult
    func minWidth(_ value: UnitValue) -> Size {
        self.minWidth = value
        return self
    }
    
    @discardableResult
    func minHeight(_ value: UnitValue) -> Size {
        self.minHeight = value
        return self
    }
    
    @discardableResult
    func aspectRatio(_ aspectRatio: Double) -> Size {
        self.aspectRatio = aspectRatio
        return self
    }
}

// MARK: - EdgeValue Builders
public extension EdgeValue {
    @discardableResult
    func left(_ value: UnitValue) -> EdgeValue {
        self.left = value
        return self
    }
    
    @discardableResult
    func top(_ value: UnitValue) -> EdgeValue {
        self.top = value
        return self
    }
    
    @discardableResult
    func right(_ value: UnitValue) -> EdgeValue {
        self.right = value
        return self
    }
    
    @discardableResult
    func bottom(_ value: UnitValue) -> EdgeValue {
        self.bottom = value
        return self
    }
    
    @discardableResult
    func horizontal(_ value: UnitValue) -> EdgeValue {
        self.horizontal = value
        return self
    }
    
    @discardableResult
    func vertical(_ value: UnitValue) -> EdgeValue {
        self.vertical = value
        return self
    }
    
    @discardableResult
    func all(_ value: UnitValue) -> EdgeValue {
        self.all = value
        return self
    }
}
