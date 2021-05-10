import { HttpClient } from '@zup-it/beagle-web'
import { createStaticPromise } from './utils/promise'
import { registerPromise, resolvePromise, unregisterPromise } from './promise'
import flutterLogger from './utils/flutter-js-logger'

interface HttpClientResponse {
  body?: string,
  headers?: Record<string, string>,
  status: number,
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

function parseJsonResponse(json: string) {
  try {
    return JSON.parse(json)
  } catch (error) {
    flutterLogger.error(`Unable to parse json response:\n${json}\n`)
    throw error
  }
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
    json: () => Promise.resolve(body ? parseJsonResponse(body) : undefined),
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

export const httpClient: HttpClient = {
  fetch: (url, options) => {
    console.log(`js: fetching ${url}`)
    const staticPromise = createStaticPromise()
    const id = registerPromise(staticPromise)
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

export function respondHttpRequest(id: string, httpClientResponse: HttpClientResponse) {
  const response = createResponse(httpClientResponse)
  console.log(`js: received response with status ${response.status}`)
  resolvePromise(id, response)
  unregisterPromise(id)
  return "ok"
}
