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

import { BeagleService, BeagleView, BeagleUIElement, NetworkOptions } from '@zup-it/beagle-web'
import get from 'lodash/get'

export interface JsBridgeBeagleView extends BeagleView {
  id: string,
  executeFunction: (functionId: string, argumentsMap: Record<string, any>) => void,
}

const map: Record<string, JsBridgeBeagleView> = {}

let nextViewId = 0

function serializeFunctions(value: any, path = '__beagleFn:'): any {
  if (typeof value === 'function') {
    return path
  }

  if (value instanceof Array) {
    return value.map((item, index) => serializeFunctions(item, `${path}[${index}]`))
  }

  if (value && typeof value === 'object') {
    const result: Record<string, any> = {}
    const keys = Object.keys(value)
    keys.forEach((key) => {
      result[key] = serializeFunctions(value[key], `${path}.${key}`)
    })
    return result
  }

  return value
}

export function createBeagleView(service: BeagleService, networkOptions?: NetworkOptions, initialControllerId?: string) {
  const view = service.createView(networkOptions, initialControllerId) as JsBridgeBeagleView
  view.id = `${nextViewId++}`
  let currentTree: BeagleUIElement | null = null

  view.subscribe((tree) => {
    currentTree = tree

    sendMessage(
      'beagleView.update',
      JSON.stringify({ id: view.id, tree: serializeFunctions(tree) }),
    )
  })

  view.getNavigator().subscribe(route => sendMessage(
    'beagleNavigator',
    JSON.stringify({ viewId: view.id, route }),
  ))
  
  view.executeFunction = (functionId: string, argumentsMap: Record<string, any>) => {
    if (!currentTree) return
    const path = functionId.replace(/__beagleFn:\.?/, '')
    const fn = get(currentTree, path)
    if (typeof fn !== 'function') {
      console.log(`No function with path "${path}" for view with id "${view.id}" was found.`)
      return
    }
    fn(argumentsMap)
  }
  
  map[view.id] = view
  return view.id
}

export function getView(id: string) {
  if (!map[id]) console.log(`No view with id ${id} has been found.`)
  return map[id]
}
