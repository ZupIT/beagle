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
