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
// MARK: DSCollection Equatable

extension DSCollection: Equatable {
     public static func == (lhs: DSCollection, rhs: DSCollection) -> Bool {
        guard lhs.dataSource == rhs.dataSource else { return false }
        guard lhs.widgetProperties == rhs.widgetProperties else { return false }
        return true
    }
}
// MARK: DSCollectionDataSource Equatable

extension DSCollectionDataSource: Equatable {
     public static func == (lhs: DSCollectionDataSource, rhs: DSCollectionDataSource) -> Bool {
        guard lhs.cards == rhs.cards else { return false }
        return true
    }
}
