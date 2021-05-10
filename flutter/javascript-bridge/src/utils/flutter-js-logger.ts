type LoggerLevel = 'info' | 'warning' | 'error' | 'errorWithException'

function createLogger() {
  
  function log(level: LoggerLevel, message: String, exception?: String) {
    
    let toLogMessage = {
      level,
      message,
      exception
    }
    sendMessage('logger', JSON.stringify(toLogMessage))
  }

  return {
    log,
    info: (message: String) => log('info', message),
    error: (message: String) => log('error', message),
    warning: (message: String) => log('warning', message),
    errorWithException: (message:String, exception: String) => log('errorWithException', message, exception)
  }
}

export default createLogger()