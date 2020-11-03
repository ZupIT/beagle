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

import Foundation

typealias DoublyLinkedListNode<T> = DoublyLinkedList<T>.Node<T>

final class DoublyLinkedList<T> {

    private(set) var count: Int = 0
    private var head: Node<T>?
    private var tail: Node<T>?
    
    final class Node<T> {
        var payload: T
        var previous: Node<T>?
        var next: Node<T>?

        init(payload: T) {
            self.payload = payload
        }
    }
    
    func addHead(_ payload: T) -> Node<T> {
        let node = Node(payload: payload)
        defer {
            head = node
            count += 1
        }

        guard let head = head else {
            tail = node
            return node
        }

        head.previous = node

        node.previous = nil
        node.next = head

        return node
    }
    
    func moveToHead(_ node: Node<T>) {
        guard node !== head else { return }
        let previous = node.previous
        let next = node.next

        previous?.next = next
        next?.previous = previous

        node.next = head
        node.previous = nil

        if node === tail {
            tail = previous
        }

        self.head = node
    }
    
    func removeLast() -> Node<T>? {
        guard let tail = self.tail else { return nil }

        let previous = tail.previous
        previous?.next = nil
        self.tail = previous

        if count == 1 {
            head = nil
        }

        count -= 1

        return tail
    }
    
    func clear() {
        count = 0
        head = nil
        tail = nil
    }
}
