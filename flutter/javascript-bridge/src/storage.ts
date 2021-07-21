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

import { AsynchronousStorage } from '@zup-it/beagle-web'
import { createStaticPromise } from './utils/promise'
import { registerPromise } from './promise'

export const storage: AsynchronousStorage = {
  clear: () => {
    const sp = createStaticPromise<void>()
    const promiseId = registerPromise(sp)
    sendMessage('storage.clear', JSON.stringify({ promiseId }))
    return sp.promise
  },
  getItem: (key) => {
    const sp = createStaticPromise<string>()
    const promiseId = registerPromise(sp)
    sendMessage('storage.get', JSON.stringify({ key, promiseId }))
    return sp.promise
  },
  removeItem: (key) => {
    const sp = createStaticPromise<void>()
    const promiseId = registerPromise(sp)
    sendMessage('storage.remove', JSON.stringify({ key, promiseId }))
    return sp.promise
  },
  setItem: (key, value) => {
    const sp = createStaticPromise<void>()
    const promiseId = registerPromise(sp)
    sendMessage('storage.set', JSON.stringify({ key, value, promiseId }))
    return sp.promise
  }
}
