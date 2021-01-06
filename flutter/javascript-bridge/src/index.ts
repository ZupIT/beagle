import createBeagleService, { BeagleService, logger } from '@zup-it/beagle-web'
import { createCustomActionMap } from './action'
import { createBeagleView, getView } from './view'
import { storage } from './storage'
import { callFunction } from './function'
import { httpClient, respondHttpRequest } from './http-client'

// @ts-ignore
window.beagle = (() => {
  let service: BeagleService

  const api = {
    // todo: handle actions different than "beagle:alert"
    start: (baseUrl: string, actions: string[]) => {
      console.log(`js: baseUrl: ${baseUrl}`)
      service = createBeagleService({
        baseUrl,
        components: {},
        disableCssTransformation: true,
        fetchData: httpClient.fetch,
        customStorage: storage,
        customActions: createCustomActionMap(actions),
      })

      logger.setCustomLogFunction((_, ...messages) => {
        console.log(messages.join(' '))
      })
    },
    createBeagleView: () => createBeagleView(service),
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
  }

  return api
})()
