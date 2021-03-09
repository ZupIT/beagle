import createBeagleService, {
  BeagleService,
  logger,
  NavigationController,
  NetworkOptions,
  Strategy,
  Operation
} from '@zup-it/beagle-web'
import { createCustomActionMap } from './action'
import { createBeagleView, getView } from './view'
import { storage } from './storage'
import { callFunction } from './function'
import { httpClient, respondHttpRequest } from './http-client'
import { resolvePromise, rejectPromise } from './promise'
import { createCustomOperationMap } from './operation'

interface StartParams {
  baseUrl: string,
  actionKeys: string[],
  customOperations: string[]
  navigationControllers: Record<string, NavigationController>,
  useBeagleHeaders: boolean,
  strategy: Strategy,
}

// @ts-ignore
window.beagle = (() => {
  let service: BeagleService

  const api = {
    start: ({ actionKeys, customOperations, ...other }: StartParams) => {
      service = createBeagleService({
        components: {},
        disableCssTransformation: true,
        fetchData: httpClient.fetch,
        customStorage: storage,
        customActions: createCustomActionMap(actionKeys),
        customOperations: createCustomOperationMap(customOperations),
        ...other,
      })

      logger.setCustomLogFunction((_, ...messages) => {
        console.log(messages.join(' '))
      })
    },
    createBeagleView: (networkOptions?: NetworkOptions, initialControllerId?: string) => createBeagleView(service, networkOptions, initialControllerId),
    httpClient: { respond: respondHttpRequest },
    call: (id: string, argumentsMap?: Record<string, any>) => {
      console.log(`js: called function with id ${id} and argument map: ${JSON.stringify(argumentsMap)}`)
      callFunction(id, argumentsMap)
    },
    callViewFunction: (viewId: string, functionId: string, argumentsMap: Record<string, any>) => {
      const view = getView(viewId)
      if (view) view.executeFunction(functionId, argumentsMap)
    },
    getViewById: getView,
    getService: () => service,
    promise: {
      resolve: resolvePromise,
      reject: rejectPromise, 
    },
  }

  return api
})()
