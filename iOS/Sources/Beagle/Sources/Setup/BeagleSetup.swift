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
import BeagleSchema

public var dependencies: BeagleDependenciesProtocol = BeagleDependencies() {
    didSet { BeagleSchema.dependencies = dependencies }
}

// MARK: - Public Functions

/// Register a custom component
@available(*, deprecated, message: "use decoder.register in BeagleDependencies instead")
public func registerCustomComponent<C: ServerDrivenComponent>(
    _ name: String,
    componentType: C.Type
) {
    dependencies.decoder.register(component: componentType, named: name)
}

/// Register a custom action
@available(*, deprecated, message: "use decoder.register in BeagleDependencies instead")
public func registerCustomAction<A: Action>(
    _ name: String,
    actionType: A.Type
) {
    dependencies.decoder.register(action: actionType, named: name)
}

public func screen(_ type: ScreenType, controllerId: String? = nil) -> BeagleScreenViewController {
    return BeagleScreenViewController(type, controllerId: controllerId)
}
