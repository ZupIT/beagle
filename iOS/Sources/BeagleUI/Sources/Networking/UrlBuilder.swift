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

public protocol DependencyUrlBuilder {
    var urlBuilder: UrlBuilderProtocol { get }
}

public protocol UrlBuilderProtocol {
    var baseUrl: URL? { get set }

    func build(path: String) -> URL?
}

public class UrlBuilder: UrlBuilderProtocol {

    public var baseUrl: URL?

    public init(baseUrl: URL? = nil) {
        self.baseUrl = baseUrl
    }

    public func build(path: String) -> URL? {
        switch getUrlType(path: path, baseUrl: baseUrl) {
        case .noStripePrefix:
            return URL(string: path)
        case .stripePrefix:
            guard var absolute = baseUrl?.absoluteString else {
                return URL(string: path)
            }
            if absolute.hasSuffix("/") {
                absolute.removeLast()
            }
            return URL(string: absolute + path)
        case .regularBuild:
            return URL(string: path, relativeTo: baseUrl)
        }
    }
    
    private func getUrlType(path: String, baseUrl: URL?) -> UrlType {
        if !path.hasPrefix("/") {
            return .noStripePrefix
        } else if path.hasPrefix("/") || path.hasPrefix("//") {
            return .stripePrefix
        } else {
            return .regularBuild
        }
    }
}

private enum UrlType {
    case noStripePrefix
    case stripePrefix
    case regularBuild
}
