type FlutterJSChannel = (
  'httpClient.request'
  | 'beagleView.update'
  | 'action'
  | 'beagleNavigator'
  | 'storage.get'
  | 'storage.set'
  | 'storage.remove'
  | 'storage.clear'
  | 'operation'
)

declare function sendMessage(channel: FlutterJSChannel, message: String)
