//
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

import BeagleSchema
import UIKit

extension SendRequest: Action {
    public func execute(controller: BeagleController, sender: Any) {

        guard let
            view = sender as? UIView,
            let url = controller.dependencies.urlBuilder.build(path: url.evaluate(with: view) ?? "") else {
            return
        }
        let requestData = Request.RequestData(
            method: method?.rawValue,
            headers: headers,
            body: data?.get(with: view).asAny()
        )
        let request = Request(url: url, type: .rawRequest(requestData), additionalData: nil)
        controller.dependencies.networkClient.executeRequest(request, completion: { result in
            
            switch result {
            case .success(let response):
                
                let data = response.getDynamicObject()
                let statusCode = response.statusCode()
                let value: DynamicObject = ["data": data, "status": .int(statusCode), "statusText": "success"]
                let contextObject = Context(id: "onSuccess", value: value)

                DispatchQueue.main.async {
                    controller.execute(actions: self.onSuccess, with: contextObject, sender: sender)
                    controller.execute(actions: self.onFinish, with: nil, sender: sender)
                }
                
            case .failure(let error):
                
                let data = error.getDynamicObject()
                let statusCode = error.statusCode()
                let statusText = error.localizedDescription
                let message = error.localizedDescription
                
                let value: DynamicObject = [ "data": data, "status": .int(statusCode), "statusText": .string(statusText), "message": .string(message) ]
                let contextObject = Context(id: "onError", value: value)
                
                DispatchQueue.main.async {
                    controller.execute(actions: self.onError, with: contextObject, sender: sender)
                    controller.execute(actions: self.onFinish, with: nil, sender: sender)
                }
                
            }
        })
    }
}

private extension NetworkResponse {
    
    func statusCode() -> Int {
        return (response as? HTTPURLResponse)?.statusCode ?? 0
    }
    
    func getDynamicObject() -> DynamicObject {
        return _makeDynamicObject(with: data)
    }
}

private extension NetworkError {
    
    func statusCode() -> Int {
        return (response as? HTTPURLResponse)?.statusCode ?? 0
    }
    
    func getDynamicObject() -> DynamicObject {
        return _makeDynamicObject(with: data ?? Data())
    }
}

private func _makeDynamicObject(with data: Data) -> DynamicObject {
    var dynamicObject: DynamicObject = nil
    if  let jsonObject = (try? JSONSerialization.jsonObject(with: data, options: [.fragmentsAllowed])) as? [String: Any] {
        dynamicObject = DynamicObject(from: jsonObject)
    } else if let stringObject = String(bytes: data, encoding: .utf8) {
        dynamicObject = .string(stringObject)
    }
    return dynamicObject
}
