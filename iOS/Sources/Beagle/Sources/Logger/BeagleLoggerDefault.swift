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
import os.log

public protocol DependencyLogger {
    var logger: BeagleLoggerType { get }
}

public protocol DependencyLoggingCondition {
    var isLoggingEnabled: Bool { get }
}

public protocol BeagleLoggerType: SchemaLogger {
    func log(_ log: LogType)
}

public class BeagleLoggerDefault: BeagleLoggerType {

    public func log(_ log: LogType) {
        os_log("\nBeagleSDK: %@", log: osLog(for: log), type: toOsLog(log.level), log.message)
    }
    
    public func logDecodingError(type: String) {
        log(Log.decode(.decodingError(type: type)))
    }
    
    // MARK: Private

    private static var subsystem = Bundle.main.bundleIdentifier ?? "BeagleSDK"

    private func osLog(for type: LogType) -> OSLog {
        return OSLog(subsystem: BeagleLoggerDefault.subsystem, category: type.category)
    }
    
    private func toOsLog(_ level: LogLevel) -> OSLogType {
        switch level {
        case .error: return .error
        case .info: return .info
        }
    }
}
