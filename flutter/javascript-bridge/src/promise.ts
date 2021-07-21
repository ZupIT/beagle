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

import { StaticPromise } from './utils/promise'

const map: Record<string, StaticPromise<any>> = {}
let nextId = 0

export function registerPromise(promise: StaticPromise<any>) {
  const id = `${nextId++}`
  map[id] = promise
  return id
}

export function unregisterPromise(id: string){
  delete map[id]
}

export function resolvePromise(id: string, value: any) {
  if (!map[id]) throw new Error(`Can't resolve promise with id ${id}. It doesn't exist.`)
  map[id].resolve(value)
  delete map[id]
}

export function rejectPromise(id: string, value: any) {
  if (!map[id]) throw new Error(`Can't reject promise with id ${id}. It doesn't exist.`)
  map[id].reject(value)
  delete map[id]
}
