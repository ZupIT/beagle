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

import UIKit

extension SendRequest: AsyncAction {
    
    public func execute(controller: BeagleController, origin: UIView) {
        let methodValue = method?.evaluate(with: origin)
        let headersValue = headers?.evaluate(with: origin)

        let requestData = Request.RequestData(
            method: methodValue?.rawValue,
            headers: headersValue,
            body: data?.evaluate(with: origin).asAny()
        )

        let dispatcher = RequestDispatcher(dependencies: controller.dependencies)
        dispatcher.dispatchRequest(
            path: url.evaluate(with: origin) ?? "",
            type: .rawRequest(requestData),
            additionalData: nil
        ) { result in
            switch result {
            case .success(let response):
                
                let data = response.getDynamicObject()
                let statusCode = response.statusCode()
                let value: DynamicObject = ["data": data, "status": .int(statusCode), "statusText": "success"]

                DispatchQueue.main.async {
                    controller.execute(actions: self.onSuccess, with: "onSuccess", and: value, origin: origin)
                    controller.execute(actions: self.onFinish, event: "onFinish", origin: origin)
                }
                
            case .failure(let error):
                guard case .networkError(let error) = error else {
                    self.executeError(value: .empty, controller: controller, origin: origin)
                    return
                }

                let data = error.getDynamicObject()
                let statusCode = error.statusCode()
                let statusText = error.localizedDescription
                let message = error.localizedDescription
                
                let value: DynamicObject = [ "data": data, "status": .int(statusCode), "statusText": .string(statusText), "message": .string(message) ]
                self.executeError(value: value, controller: controller, origin: origin)
            }
        }
    }

    private func executeError(value: DynamicObject, controller: BeagleController, origin: UIView) {
        DispatchQueue.main.async {
            controller.execute(actions: self.onError, with: "onError", and: value, origin: origin)
            controller.execute(actions: self.onFinish, event: "onFinish", origin: origin)
        }
    }
}

private extension NetworkResponse {
    
    func statusCode() -> Int {
        return (response as? HTTPURLResponse)?.statusCode ?? 0
    }
    
    func getDynamicObject() -> DynamicObject {
        return makeDynamicObject(with: data)
    }
}

private extension NetworkError {
    
    func statusCode() -> Int {
        return (response as? HTTPURLResponse)?.statusCode ?? 0
    }
    
    func getDynamicObject() -> DynamicObject {
        return makeDynamicObject(with: data ?? Data())
    }
}

private func makeDynamicObject(with data: Data) -> DynamicObject {
    let decoder = JSONDecoder()
    let result = try? decoder.decode(DynamicObject.self, from: data)
    return result ?? .empty
}
