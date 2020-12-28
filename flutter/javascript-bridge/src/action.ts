import { ActionHandler, AlertAction } from '@zup-it/beagle-web/action/types'
import { registerFunctionForSingleExecution } from './function'
  

export const customActions: Record<string, ActionHandler> = {
  'beagle:alert': ({ action, executeAction }) => {
    const { _beagleAction_, message, title, onPressOk } = action as AlertAction & { title: string }
    const fnId = (
      onPressOk
      && registerFunctionForSingleExecution(() => executeAction(onPressOk, 'onPressOk'))
    )
    sendMessage('action', JSON.stringify({
      _beagleAction_,
      message,
      title,
      onPressOk: fnId,
    }))
  },
}
