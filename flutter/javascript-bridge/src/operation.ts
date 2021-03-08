import { Operation } from '@zup-it/beagle-web'

function handlerWrapper(operationName: string) {
  
  const flutterOperationHandler: Operation = (...args: any[]) => {
    sendMessage('operation', JSON.stringify({ operation: operationName, params: args }))
  }

  return flutterOperationHandler
}

export function createCustomOperationMap(operations: string[]): Record<string, Operation> {
  return operations.reduce((result, operationName) => {
    return { ...result, [operationName]: handlerWrapper(operationName) }
  }, {})
}
