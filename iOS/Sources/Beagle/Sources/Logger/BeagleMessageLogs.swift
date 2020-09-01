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

public protocol LogType {
    var category: String { get }
    var message: String { get }
    var level: LogLevel { get }
}

public enum LogLevel {
    case error
    case info
}

public enum Log {

    case network(_ network: Network)
    case decode(_ decoding: Decoding)
    case form(_ form: Form)
    case navigation(_ navigator: Navigator)
    case cache(_ cache: Cache)
    case expression(_ expression: Expression)

    public enum Decoding {
        case decodingError(type: String)
    }

    public enum Network {
        case httpRequest(request: NetworkRequest)
        case httpResponse(response: NetworkResponse)
        case couldNotBuildUrl(url: String)
    }

    public enum Form {
        case validatorNotFound(named: String)
        case validationInputNotValid(inputName: String)
        case submitNotFound(form: Deprecated.Form)
        case inputsNotFound(form: Deprecated.Form)
        case divergentInputViewAndValueCount(form: Deprecated.Form)
        case submittedValues(values: [String: String])
        case keyDuplication(data: [String: String])
        case unableToSaveData
    }

    public enum Navigator {
        case didReceiveAction(Navigate)
        case errorTryingToPopScreenOnNavigatorWithJustOneScreen
        case didNotFindDeepLinkScreen(path: String)
        case cantPopToAlreadyCurrentScreen(identifier: String)
        case didNavigateToExternalUrl(path: String)
        case invalidExternalUrl(path: String)
        case unableToOpenExternalUrl(path: String)
    }
    
    public enum Cache {
        case saveContext(description: String)
        case loadPersistentStores(description: String)
        case fetchData(description: String)
        case removeData(description: String)
        case clear(description: String)
    }

    public struct NetworkResponse {
        public let data: Data?
        public let response: URLResponse?

        public var logMessage: String {
            let response = (self.response as? HTTPURLResponse)
            let string = """
            ***HTTP RESPONSE***
            StatusCode= \(response?.statusCode ?? 0)
            Body= \(String(data: data ?? Data(), encoding: .utf8) ?? "")
            Headers= \(response?.allHeaderFields ?? [:])
            """
            return string
        }
        
        public init(
            data: Data? = nil,
            response: URLResponse? = nil
        ) {
            self.data = data
            self.response = response
        }
    }

    public struct NetworkRequest {
        public let url: URLRequest?

        public var logMessage: String {
            let string = """
            ***HTTP REQUEST***:
            Url= \(url?.url?.absoluteString ?? "")
            HttpMethod= \(url?.httpMethod ?? "")
            Headers= \(url?.allHTTPHeaderFields ?? [:])
            Body= \(String(data: url?.httpBody ?? Data(), encoding: .utf8) ?? "empty")
            """
            return string
        }
        
        public init(
            url: URLRequest? = nil
        ) {
            self.url = url
        }
        
    }

    public enum Expression {
        case invalidSyntax
    }
}

extension Log: LogType {

    public var category: String {
        switch self {
        case .decode: return "Decoding"
        case .form: return "Form"
        case .navigation: return "Navigation"
        case .network: return "Network"
        case .cache: return "Cache"
        case .expression: return "Expression"
        }
    }

    public var message: String {
        switch self {
        case .network(.httpRequest(let request)):
            return request.logMessage
        case .network(.httpResponse(let response)):
            return response.logMessage
        case .network(let log):
            return String(describing: log)

        case .form(.submitNotFound(let form)):
            return "You probably forgot to declare your Submit widget in form: \n\t \(form)"
        case .form(.inputsNotFound(let form)):
            return "You probably forgot to declare your FormInput widgets in form: \n\t \(form)"
        case .form(.divergentInputViewAndValueCount(let form)):
            return "Number of formInput and values are different. You probably declared formInputs with the same name in form: \n\t \(form)"
        case .form(.unableToSaveData):
            return "Unable to save form data. A group name must be given"
        case .form(.keyDuplication(let data)):
            return "Found a key duplication when merging form data:\n\(data)"
        case .form(let log):
            return String(describing: log)

        case .navigation(.didNotFindDeepLinkScreen(let path)):
            return "Beagle Navigator couldn't find a deep link screen with path: \(path). Check your deep link handler, or the path in the navigate action"
        case .navigation(let log):
            return String(describing: log)

        case .decode(.decodingError(let type)):
            return "Could not decode: \(type). Check if that type has been registered."
            
        case .cache(.saveContext(let description)):
            return "Cold not save data in current core data context. error: \(description)"
        case .cache(.loadPersistentStores(let description)):
            return "Cold not load persistent container: \(description)"
        case .cache(.fetchData(let description)):
            return "Cold not load fetch data from cache: \(description)"
        case .cache(.removeData(let description)):
            return "Cold not load remove register from cache: \(description)"
        case .cache(.clear(let description)):
            return "Cold clear registers from cache: \(description)"

        case .expression(.invalidSyntax):
            return "Using Expressions without proper syntax"
        }
    }

    public var level: LogLevel {
        switch self {
        case .network(let net):
            switch net {
            case .httpRequest, .httpResponse: return .info
            case .couldNotBuildUrl: return .error
            }

        case .decode(.decodingError): return .error

        case .form(let form):
            switch form {
            case .validatorNotFound, .submitNotFound, .inputsNotFound, .divergentInputViewAndValueCount:
                return .error
            case .submittedValues, .validationInputNotValid, .unableToSaveData, .keyDuplication:
                return .info
            }

        case .navigation(let nav):
            switch nav {
            case .errorTryingToPopScreenOnNavigatorWithJustOneScreen, .didNotFindDeepLinkScreen, .cantPopToAlreadyCurrentScreen, .invalidExternalUrl, .unableToOpenExternalUrl:
                return .error
            case .didReceiveAction, .didNavigateToExternalUrl:
                return .info
            }
        
        case .cache:
            return .error

        case .expression(.invalidSyntax):
            return .info
        }
    }
}
