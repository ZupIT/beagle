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
@testable import BeagleSchema

enum ComponentFromJsonError: Error {
    case wrongUrlPath
    case couldNotMatchComponentType
}

func componentFromJsonFile<W: RawComponent>(
    fileName: String,
    decoder: ComponentDecoding = ComponentDecoder()
) throws -> W {
    guard let url = Bundle(for: ComponentDecoderTests.self).url(
        forResource: fileName,
        withExtension: ".json"
    ) else {
        throw ComponentFromJsonError.wrongUrlPath
    }

    let json = try Data(contentsOf: url)
    let component = try decoder.decodeComponent(from: json)

    guard let typed = component as? W else {
        throw ComponentFromJsonError.couldNotMatchComponentType
    }

    return typed
}

// TODO: Make decoding process generic
func actionFromJsonFile<W: RawAction>(
    fileName: String,
    decoder: ComponentDecoding = ComponentDecoder()
) throws -> W {
    guard let url = Bundle(for: ComponentDecoderTests.self).url(
        forResource: fileName,
        withExtension: ".json"
    ) else {
        throw ComponentFromJsonError.wrongUrlPath
    }

    let data = try Data(contentsOf: url)
    return try actionFromData(data)
}

func actionFromString<A: RawAction>(
    _ string: String,
    decoder: ComponentDecoding = ComponentDecoder()
) throws -> A {
    guard let data = string.data(using: .utf8) else {
        throw ComponentFromJsonError.wrongUrlPath
    }

    return try actionFromData(data)
}

func actionFromData<A: RawAction>(
    _ data: Data,
    decoder: ComponentDecoding = ComponentDecoder()
) throws -> A {
    let action = try decoder.decodeAction(from: data)
    guard let typed = action as? A else {
        throw ComponentFromJsonError.couldNotMatchComponentType
    }
    return typed
}

func jsonFromFile(
    fileName: String
) throws -> String {

    guard let url = Bundle(for: ComponentDecoderTests.self).url(
        forResource: fileName,
        withExtension: "json"
    ) else {
        throw ComponentFromJsonError.wrongUrlPath
    }

    let jsonData = try Data(contentsOf: url)
    let json = String(bytes: jsonData, encoding: .utf8) ?? ""

    return json
}

/// This method was only created due to some problems with Swift Type Inference.
/// So when you pass the type as a parameter, swift can infer the correct type.
func componentFromJsonFile<W: RawComponent>(
    componentType: W.Type,
    fileName: String,
    decoder: ComponentDecoding = ComponentDecoder()
) throws -> W {
    return try componentFromJsonFile(fileName: fileName, decoder: decoder)
}
