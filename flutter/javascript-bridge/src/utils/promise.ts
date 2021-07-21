/*
 * Copyright 2021 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
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

export interface StaticPromise<T> {
    resolve: (value: any) => void,
    reject: (error: any) => void,
    promise: Promise<T>,
  }
  
  export function createStaticPromise<T = any>() {
    const staticPromise: Partial<StaticPromise<T>> = {}
  
    staticPromise.promise = new Promise((resolve, reject) => {
      staticPromise.resolve = resolve
      staticPromise.reject = reject
    })
  
    return staticPromise as StaticPromise<T>
  }
