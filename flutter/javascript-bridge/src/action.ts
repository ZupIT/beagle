import { ActionHandler, ActionHandlerParams } from '@zup-it/beagle-web/action/types'
import { registerFunctionForSingleExecution } from './function'
import { JsBridgeBeagleView } from './view'

function isBeagleAction(data: any) {
  return data && (data._beagleAction_ || (Array.isArray(data) && data[0] && data[0]._beagleAction_))
}

function convertSubActionsToFunctions(
  executeAction: ActionHandlerParams['executeAction'],
  data: any,
  isRoot = true,
  key = '',
): any {
  if (!isRoot && isBeagleAction(data)) {
    const fn = (argumentMap?: Record<string, any>) => executeAction(data, key, argumentMap)
    return registerFunctionForSingleExecution(fn)
  }

  if (Array.isArray(data)) {
    return data.map(item => convertSubActionsToFunctions(executeAction, item, false, key))
  }

  if (data && typeof data === 'object') {
    const keys = Object.keys(data)
    return keys.reduce((result, key) => ({
      ...result, [key]: convertSubActionsToFunctions(executeAction, data[key], false, key)
    }), {})
  }

  return data
}

const flutterActionHandler: ActionHandler = ({ action, beagleView, element, executeAction }) => {
  const viewId = (beagleView as JsBridgeBeagleView).id
  const actionWithFunctions = convertSubActionsToFunctions(executeAction, action)
  sendMessage('action', JSON.stringify({
    action: actionWithFunctions,
    viewId,
    element,
  }))
}

export function createCustomActionMap(actionNames: string[]): Record<string, ActionHandler> {
  return actionNames.reduce((result, actionName) => (
    { ...result, [actionName]: flutterActionHandler }
  ), {})
}
