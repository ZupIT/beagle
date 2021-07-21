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

type SerializableFunction = (argumentsMap?: Record<string, any>) => void

const functionMap: Record<string, SerializableFunction> = {}
const isSingleExecution: Record<string, boolean> = {}
let nextId = 0

export function countFunctions() {
  return Object.keys(functionMap).length
}

/**
 * Registers a new function and provides an id for serialization. This function will be erased from
 * memory as soon as its first call ends. i.e. The function registered through this must be executed
 * only once.
 * 
 * @param fn the function to be registered
 * @return the id for serialization of the function
 */
export function registerFunctionForSingleExecution(fn: SerializableFunction) {
  const id = `__beagleFn:${nextId++}`
  functionMap[id] = fn
  isSingleExecution[id] = true
  return id
}

/**
 * Registers a new function and provides an id for serialization. This function will never be erased
 * from memory unless `unregisterFunctionById` is called specifically for its id. Use it carefully.
 * 
 * @param fn the function to be registered
 * @return the id for serialization of the function
 */
export function registerPersistentFunction(fn: SerializableFunction) {
  const id = `__beagleFn:${nextId++}`
  functionMap[id] = fn
  return id
}

/**
 * Unregister the function with the provided id. Freeing the memory used by it.
 * 
 * @param id the id of the function
 */
export function unregisterFunctionById(id: string) {
  delete functionMap[id]
  delete isSingleExecution[id]
}

/**
 * Calls the function registered with the provided id. If no function is found with the provided id
 * an error is thrown. If the function is a single-use function, it will be unregistered after used.
 * 
 * @param id the id of the function to execute
 * @param argumentsMap optional. a map with the parameters to pass to the function
 */
export function callFunction(id: string, argumentsMap?: Record<string, any>) {
  if (!functionMap[id]) throw new Error(`Can't call function with id ${id}. It doesn't exist.`)
  functionMap[id](argumentsMap)
  if (isSingleExecution[id]) unregisterFunctionById(id)
}
