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

extension String {
    
    func matches(pattern: String) -> [String] {
        do {
            let regex = try NSRegularExpression(pattern: pattern)
            let results = regex.matches(in: self, range: NSRange(self.startIndex..., in: self))
            return results.compactMap { self[$0.range] }
        } catch { return [] }
    }
    
    func match(pattern: String) -> String? {
        do {
            let regex = try NSRegularExpression(pattern: pattern)
            let result = regex.firstMatch(in: self, range: NSRange(self.startIndex..., in: self))
            return self[result?.range]
        } catch { return nil }
    }
    
    private subscript(r: NSRange?) -> String? {
        guard let unwrapped = r, let range = Range(unwrapped, in: self) else { return nil }
        return String(self[range])
    }
    
}
