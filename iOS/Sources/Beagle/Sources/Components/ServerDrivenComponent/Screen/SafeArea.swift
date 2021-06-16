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

/// The SafeArea will enable Safe areas to help you place your views within the visible portion of the overall interface.
public struct SafeArea: Equatable, Decodable, AutoInitiable {

    /// Enables the safeArea constraint only on the TOP of the screen view.
    public let top: Bool?
    
    /// Enables the safeArea constraint only on the LEFT side of the screen view.
    public let leading: Bool?
    
    /// Enables the safeArea constraint only on the BOTTOM of the screen view.
    public let bottom: Bool?
    
    /// Enables the safeArea constraint only on the RIGHT of the screen view.
    public let trailing: Bool?
    
// sourcery:inline:auto:SafeArea.Init
    public init(
        top: Bool? = nil,
        leading: Bool? = nil,
        bottom: Bool? = nil,
        trailing: Bool? = nil
    ) {
        self.top = top
        self.leading = leading
        self.bottom = bottom
        self.trailing = trailing
    }
// sourcery:end
}
