type FlutterJSChannel = (
  'httpClient.request'
  | 'beagleView.update'
  | 'action'
  | 'beagleNavigator'
  | 'storage.get'
  | 'storage.set'
  | 'storage.remove'
  | 'storage.clear'
)

declare function sendMessage(channel: FlutterJSChannel, message: String)
