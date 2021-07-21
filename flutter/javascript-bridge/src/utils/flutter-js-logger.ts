/*
 * Copyright 2021 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
