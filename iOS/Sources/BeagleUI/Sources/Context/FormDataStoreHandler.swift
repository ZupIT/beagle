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

public protocol FormDataStoreHandling {
    func save(data: [String: String], group: String)
    func read(group: String) -> [String: String]?
    func formManagerDidSubmitForm(group: String?)
}

public protocol DependencyFormDataStoreHandler {
    var formDataStoreHandler: FormDataStoreHandling { get }
}

public class FormData {
    var data: [String: String] = [:]
    
    func save(data: [String: String]) {
        self.data.merge(data) { _, new in new }
    }
}

public class FormDataStoreHandler: FormDataStoreHandling {
    
    private var dataStore: [String: FormData] = [:]
    
    // MARK: - FormDataStoreHandling
    
    public func save(data: [String: String], group: String) {
        if let formData = dataStore[group] {
            formData.save(data: data)
        } else {
            dataStore[group] = FormData()
            save(data: data, group: group)
        }
    }
    
    public func read(group: String) -> [String: String]? {
        return dataStore[group]?.data
    }
    
    public func formManagerDidSubmitForm(group: String?) {
        if let group = group {
            dataStore.removeValue(forKey: group)
        }
    }
}
