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

public protocol DependencySchema {
    var schemaDependencies: SchemaDependencies { get }
}

//extension DependencySchema {
//    public var loggerHelper: SchemaLoggerHelper? {
//        return schemaDependencies.loggerHelper
//    }
//
//    public var decoder: ComponentDecoding {
//        return schemaDependencies.decoder
//    }
//}

typealias SchemaAttributes = DependencyComponentDecoding & DependencyLoggerHelper

public struct SchemaDependencies: SchemaAttributes {
    public var loggerHelper: SchemaLoggerHelper?
    public var decoder: ComponentDecoding

    internal init(
        loggerHelper: SchemaLoggerHelper? = nil,
        decoder: ComponentDecoding
    ) {
        self.loggerHelper = loggerHelper
        self.decoder = decoder
    }

    public init(
        loggerHelper: SchemaLoggerHelper,
        decoder: ComponentDecoding = ComponentDecoder()
    ) {
        self.loggerHelper = loggerHelper
        self.decoder = decoder
    }
    
}
