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

package br.com.zup.beagle.platform

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode

object BeaglePlatformUtil {

    const val BEAGLE_PLATFORM_HEADER = "beagle-platform"
    internal const val BEAGLE_PLATFORM_FIELD = "beaglePlatform"
    internal const val BEAGLE_CHILD_FIELD = "child"

    fun treatBeaglePlatform(currentPlatform: String?, jsonNode: JsonNode): JsonNode {
        if (jsonNode is ObjectNode) {
            if (currentPlatform != null && BeaglePlatform.ALL.name != currentPlatform) {
                removeObjectFromTreeWhenPlatformIsDifferToCurrentPlatform(
                    BeaglePlatform.valueOf(currentPlatform),
                    jsonNode
                )
            }
            removeBeaglePlatformField(
                jsonNode
            )
        }
        return jsonNode
    }

    private fun removeObjectFromTreeWhenPlatformIsDifferToCurrentPlatform(
        currentPlatform: BeaglePlatform,
        objectNode: ObjectNode
    ): Boolean {
        var isToRemove = false
        val fieldsToRemove = mutableSetOf<String>()
        objectNode.fields().forEach {
            if (removeObjectFromTreeWhenPlatformIsDifferToCurrentPlatform(
                    currentPlatform = currentPlatform,
                    nodeEntry = it,
                    fieldsToRemove = fieldsToRemove
                )
            ) {
                isToRemove = true
            }
        }
        objectNode.remove(fieldsToRemove)
        return isToRemove
    }

    private fun removeObjectFromTreeWhenPlatformIsDifferToCurrentPlatform(
        currentPlatform: BeaglePlatform,
        nodeEntry: MutableMap.MutableEntry<String, JsonNode>,
        fieldsToRemove: MutableSet<String>
    ): Boolean {
        val currentNode = nodeEntry.value
        when {
            checkIfCurrentPlatformIsNotAllowedToViewComponent(
                currentPlatform,
                nodeEntry
            ) -> {
                return true
            }
            currentNode is ObjectNode -> {
                if (removeObjectFromTreeWhenPlatformIsDifferToCurrentPlatform(
                        currentPlatform,
                        currentNode
                    )
                ) {
                    fieldsToRemove.add(nodeEntry.key)
                }
            }
            currentNode is ArrayNode -> {
                treatArrayNode(
                    currentPlatform,
                    currentNode
                )
            }
        }
        return false
    }

    private fun treatArrayNode(currentPlatform: BeaglePlatform, arrayNode: ArrayNode) {
        val indexToRemoveList = mutableListOf<Int>()
        arrayNode.forEachIndexed { index, node ->
            if (node is ObjectNode) {
                if (removeObjectFromTreeWhenPlatformIsDifferToCurrentPlatform(
                        currentPlatform,
                        node
                    )
                ) {
                    indexToRemoveList.add(index)
                }
            }
        }
        indexToRemoveList.forEachIndexed { index, indexToRemove ->
            arrayNode.remove(indexToRemove - index)
        }
    }

    private fun removeBeaglePlatformField(objectNode: ObjectNode) {
        if (objectNode.has(BEAGLE_PLATFORM_FIELD)) {
            objectNode.get(BEAGLE_CHILD_FIELD).let {
                objectNode.removeAll()
                objectNode.setAll<ObjectNode>(it as ObjectNode)
            }
        }
        objectNode.fields().forEach {
            val currentNode = it.value
            if (currentNode is ObjectNode) {
                removeBeaglePlatformField(
                    currentNode
                )
            } else if (currentNode is ArrayNode) {
                currentNode.filterIsInstance<ObjectNode>().forEach { node ->
                    removeBeaglePlatformField(
                        node
                    )
                }
            }
        }
    }

    private fun checkIfCurrentPlatformIsNotAllowedToViewComponent(
        currentPlatform: BeaglePlatform,
        node: MutableMap.MutableEntry<String, JsonNode>
    ) =
        node.key == BEAGLE_PLATFORM_FIELD
            && !BeaglePlatform.valueOf((node.value as TextNode).asText())
            .allowToSendComponentToPlatform(currentPlatform)
}