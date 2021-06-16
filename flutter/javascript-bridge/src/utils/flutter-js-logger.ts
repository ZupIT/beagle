import { LogFunction, LogType } from '@zup-it/beagle-web/logger/types'
type FlutterLoggerLevel = 'info' | 'warning' | 'error'

const levelMap: Record<LogType, FlutterLoggerLevel> = {
  error: 'error',
  info: 'info',
  warn: 'warning',
  lifecycle: 'info',
  expression: 'info',
}

const logToFlutter: LogFunction = (level, ...messages) => {
  const logMessage = { level: levelMap[level], message: messages.join(' ') }
  sendMessage('logger', JSON.stringify(logMessage))
}

export default logToFlutter