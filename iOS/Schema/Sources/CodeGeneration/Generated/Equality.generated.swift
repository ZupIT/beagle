// Generated using Sourcery 1.0.0 — https://github.com/krzysztofzablocki/Sourcery
// DO NOT EDIT

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
// MARK: CornerRadius Equatable

extension CornerRadius: Equatable {
     public static func == (lhs: CornerRadius, rhs: CornerRadius) -> Bool {
        guard lhs.radius == rhs.radius else { return false }
        return true
    }
}
// MARK: EdgeValue Equatable

extension EdgeValue: Equatable {
     public static func == (lhs: EdgeValue, rhs: EdgeValue) -> Bool {
        guard lhs.left == rhs.left else { return false }
        guard lhs.top == rhs.top else { return false }
        guard lhs.right == rhs.right else { return false }
        guard lhs.bottom == rhs.bottom else { return false }
        guard lhs.horizontal == rhs.horizontal else { return false }
        guard lhs.vertical == rhs.vertical else { return false }
        guard lhs.all == rhs.all else { return false }
        return true
    }
}
// MARK: Flex Equatable

extension Flex: Equatable {
     public static func == (lhs: Flex, rhs: Flex) -> Bool {
        guard lhs.flexDirection == rhs.flexDirection else { return false }
        guard lhs.flexWrap == rhs.flexWrap else { return false }
        guard lhs.justifyContent == rhs.justifyContent else { return false }
        guard lhs.alignItems == rhs.alignItems else { return false }
        guard lhs.alignSelf == rhs.alignSelf else { return false }
        guard lhs.alignContent == rhs.alignContent else { return false }
        guard lhs.basis == rhs.basis else { return false }
        guard lhs.flex == rhs.flex else { return false }
        guard lhs.grow == rhs.grow else { return false }
        guard lhs.shrink == rhs.shrink else { return false }
        return true
    }
}
// MARK: Size Equatable

extension Size: Equatable {
     public static func == (lhs: Size, rhs: Size) -> Bool {
        guard lhs.width == rhs.width else { return false }
        guard lhs.height == rhs.height else { return false }
        guard lhs.maxWidth == rhs.maxWidth else { return false }
        guard lhs.maxHeight == rhs.maxHeight else { return false }
        guard lhs.minWidth == rhs.minWidth else { return false }
        guard lhs.minHeight == rhs.minHeight else { return false }
        guard lhs.aspectRatio == rhs.aspectRatio else { return false }
        return true
    }
}
// MARK: Style Equatable

extension Style: Equatable {
     public static func == (lhs: Style, rhs: Style) -> Bool {
        guard lhs.backgroundColor == rhs.backgroundColor else { return false }
        guard lhs.cornerRadius == rhs.cornerRadius else { return false }
        guard lhs.borderColor == rhs.borderColor else { return false }
        guard lhs.borderWidth == rhs.borderWidth else { return false }
        guard lhs.size == rhs.size else { return false }
        guard lhs.margin == rhs.margin else { return false }
        guard lhs.padding == rhs.padding else { return false }
        guard lhs.position == rhs.position else { return false }
        guard lhs.positionType == rhs.positionType else { return false }
        guard lhs.display == rhs.display else { return false }
        guard lhs.flex == rhs.flex else { return false }
        return true
    }
}
