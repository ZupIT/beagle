import createBeagleService, {
  HttpClient,
  BeagleService,
  logger,
} from '@zup-it/beagle-web'
import { ActionHandlerParams, AlertAction, BeagleAction } from '@zup-it/beagle-web/action/types'
import { createStaticPromise, StaticPromise } from './utils/promise'

interface HttpClientResponse {
  body?: string,
  headers?: Record<string, string>,
  status: number,
}

const promiseMap = (() => {
  const map: Record<string, StaticPromise<any>> = {}
  let nextId = 0

  return {
    register: (promise: StaticPromise<any>) => {
      const id = `${nextId++}`
      map[id] = promise
      return id
    },
    unregister: (id: string) => delete map[id],
    resolve: (id: string, value: any) => {
      if (!map[id]) throw new Error(`Can't resolve promise with id ${id}. It doesn't exist.`)
      map[id].resolve(value)
    },
    reject: (id: string, value: any) => {
      if (!map[id]) throw new Error(`Can't reject promise with id ${id}. It doesn't exist.`)
      map[id].reject(value)
    },
  }
})()

const functionMap = (() => {
  const map: Record<string, (argumentsMap?: Record<string, any>) => void> = {}
  let nextId = 0

  return {
    register: (fn: (argumentsMap?: Record<string, any>) => void) => {
      const id = `__beagleFn:${nextId++}`
      map[id] = fn
      return id
    },
    unregister: (id: string) => delete map[id],
    call: (id: string, argumentsMap?: Record<string, any>) => {
      if (!map[id]) throw new Error(`Can't call function with id ${id}. It doesn't exist.`)
      map[id](argumentsMap)
    },
  }
})()

const storage = (() => {
  let map: Record<string, string> = {}

  const api: Storage = {
    clear: () => map = {},
    getItem: key => map[key],
    key: index => Object.keys(map)[index],
    length: 0,
    removeItem: (key) => {
      delete map[key]
      // @ts-ignore
      api.length--
    },
    setItem: (key, value) => {
      map[key] = value
      // @ts-ignore
      api.length++
    }
  }

  return api
})()

const httpClient: HttpClient = {
  fetch: (url, options) => {
    console.log(`js: fetching ${url}`)
    const staticPromise = createStaticPromise()
    const id = promiseMap.register(staticPromise)
    const request = {
      id,
      method: options?.method,
      url: url.toString(),
      headers: options?.headers,
      body: options?.body,
    }
    sendMessage('httpClient.request', JSON.stringify(request))
    return staticPromise.promise
  },
}

function createResponseHeaders(headers: Record<string, string> = {}) {
  // @ts-ignore
  const result: Headers = {
    append: (name, value) => headers[name] = value,
    delete: (name) => delete headers[name],
    entries: () => {
      throw new Error('not implemented yet!')
    },
    forEach: fn => Object.keys(headers).forEach(key => fn(headers[key], key, result)),
    get: name => headers[name],
    has: name => !!headers[name],
    keys: () => {
      throw new Error('not implemented yet!')
    },
    set: (name, value) => headers[name] = value,
    values: () => {
      throw new Error('not implemented yet!')
    },
  }

  return result
}

function createResponse({ status, body, headers }: HttpClientResponse): Response {
  const responseHeaders = createResponseHeaders(headers)

  return {
    arrayBuffer: () => {
      throw new Error('not implemented yet!')
    },
    blob: () => {
      throw new Error('not implemented yet!')
    },
    clone: () => {
      throw new Error('not implemented yet!')
    },
    formData: () => {
      throw new Error('not implemented yet!')
    },
    json: () => Promise.resolve(body ? JSON.parse(body) : undefined),
    text: () => Promise.resolve(body || ''),
    body: null,
    bodyUsed: false,
    headers: responseHeaders,
    ok: status >= 200 && status < 400,
    redirected: status >= 300 && status < 400,
    status,
    statusText: '',
    trailer: Promise.resolve(responseHeaders),
    type: 'default',
    url: '',
  }
}

function serializeFunctions(value: any): any {
  if (typeof value === 'function') {
    return functionMap.register(value)
  }

  if (value instanceof Array) {
    return value.map(serializeFunctions)
  }

  if (value && typeof value === 'object') {
    const result: Record<string, any> = {}
    const keys = Object.keys(value)
    keys.forEach((key) => {
      result[key] = serializeFunctions(value[key])
    })
    return result
  }

  return value
}

// @ts-ignore
window.beagle = (() => {
  let service: BeagleService
  let nextViewId = 0

  const api = {
    // todo: handle actions different than "beagle:alert"
    start: (baseUrl: string, actions: String[]) => {
      console.log(`js: baseUrl: ${baseUrl}`)
      service = createBeagleService({
        baseUrl,
        components: {},
        fetchData: httpClient.fetch,
        customStorage: storage,
        customActions: {
          'beagle:alert': ({ action, executeAction }) => {
            const { _beagleAction_, message, title, onPressOk } = action as AlertAction & { title: string }
            const fnId = onPressOk && functionMap.register(() => executeAction(onPressOk, 'onPressOk'))
            sendMessage('action', JSON.stringify({
              _beagleAction_,
              message,
              title,
              onPressOk: fnId,
            }))
          },
        },
      })

      logger.setCustomLogFunction((_, ...messages) => {
        console.log(messages.join(' '))
      })
    },
    createBeagleView: (route: string) => {
      const view = service.createView()
      const id = `${nextViewId++}`
      view.subscribe((tree) => {
        sendMessage('beagleView.update', JSON.stringify({ id, tree: serializeFunctions(tree) }))
      })
      view.getNavigator().pushView({ url: route })
      console.log(`js: route: ${route}`)
      return id
    },
    httpClient: {
      respond: (id: string, httpClientResponse: HttpClientResponse) => {
        const response = createResponse(httpClientResponse)
        console.log(`js: received response with status ${response.status}`)
        promiseMap.resolve(id, response)
        promiseMap.unregister(id)
      }
    },
    call: (id: string, argumentsMap?: Record<string, any>) => {
      console.log(`js: called function with id ${id} and argument map: ${JSON.stringify(argumentsMap)}`)
      functionMap.call(id, argumentsMap)
    },
  }

  return api
})()
