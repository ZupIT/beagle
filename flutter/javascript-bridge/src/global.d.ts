type FlutterJSChannel = (
  'httpClient.request'
  | 'beagleView.update'
  | 'action'
)

declare function sendMessage(channel: FlutterJSChannel, message: String)
