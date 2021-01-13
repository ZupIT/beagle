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
