/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
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

import UIKit

import Starscream

class SSConnectionHandler: WSConnectionHandler, WebSocketDelegate {

    init() {
        self.config = BeaglePreviewConfig.buildDefaultConfig()
    }

    // MARK: WSConnectionHandler

    weak var delegate: WSConnectionHandlerDelegate?

    func start() {
        guard !isRunning else {
            return
        }
        startListeners()
    }

    func stop() {
        guard isRunning else {
            return
        }
        stopListeners()
    }

    // MARK: WebSocketDelegate

    func didReceive(event: WebSocketEvent, client: WebSocket) {

        notifyLayoutChange(event)

        switch event {
        case .connected:
            isConnected = true
        case .disconnected, .error, .cancelled:
            isConnected = false
        default:
            break
        }

        if case .text(let json) = event {
            let index = json.index(json.startIndex, offsetBy: min(json.count, 256))
            print("handling event: \(json[..<index])...")
        } else {
            print("handling event: \(event)")
        }
    }

    private func notifyLayoutChange(_ event: WebSocketEvent) {
        if let event = event.toEvent() {
            delegate?.onWebSocketEvent(event)
        }
    }

    // MARK: Private

    private var config: BeaglePreviewConfig
    private var socket: WebSocket?
    var isRunning: Bool = false
    var isConnected: Bool = false {
        didSet {
            if !isConnected {
                socket?.disconnect()
                socket = nil
            }
            startListeners()
        }
    }

    private func startListeners() {
        isRunning = true
        if !isConnected {
            tryToConnect()
        } else {
            tryToPing()
        }
    }

    private func stopListeners() {
        isRunning = false
        isConnected = false
    }

    private func tryToConnect() {

        guard isRunning else {
            print("not running skipping connect!")
            return
        }

        guard !isConnected else {
            print("already connected do nothing!")
            return
        }

        print("trying connection...")
        guard let url = URL(string: self.config.host) else {
            print("invalid host skipping start!")
            return
        }

        socket = WebSocket(request: URLRequest(url: url))
        socket?.delegate = self
        socket?.connect()

        print("re-scheduling connect...")
        DispatchQueue.global(qos: .background).asyncAfter(deadline: .now() + config.reconnectionInterval) { [unowned self] in
            self.tryToConnect()
        }
    }

    private func tryToPing() {

        guard isRunning else {
            print("not running skipping ping!")
            return
        }

        guard isConnected else {
            print("not connected ignoring ping!")
            return
        }

        print("pinging server!")
        socket?.write(ping: Data())

        print("re-scheduling ping...")
        DispatchQueue.global(qos: .background).asyncAfter(deadline: .now() + config.reconnectionInterval) { [unowned self] in
            self.tryToPing()
        }
    }
}

fileprivate extension WebSocketEvent {

    func toEvent() -> WSConnectionEvent? {

        if case .connected = self {
            return .connected
        }

        if case .disconnected = self {
            return .disconnected
        }

        if case .text(let json) = self, !json.contains("Welcome") {
            return .layoutChange(json)
        }

        return nil
    }
}
