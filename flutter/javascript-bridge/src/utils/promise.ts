export interface StaticPromise<T> {
    resolve: (value: any) => void,
    reject: (error: any) => void,
    promise: Promise<T>,
  }
  
  export function createStaticPromise<T = any>() {
    const staticPromise: Partial<StaticPromise<T>> = {}
  
    staticPromise.promise = new Promise((resolve, reject) => {
      staticPromise.resolve = resolve
      staticPromise.reject = reject
    })
  
    return staticPromise as StaticPromise<T>
  }
  