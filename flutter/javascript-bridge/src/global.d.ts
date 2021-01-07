type FlutterJSChannel = (
  'httpClient.request'
  | 'beagleView.update'
  | 'action'
  | 'beagleNavigator'
)

declare function sendMessage(channel: FlutterJSChannel, message: String)
