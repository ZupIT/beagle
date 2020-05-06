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

package br.com.zup.beagle.utils

import br.com.zup.beagle.enums.BeagleChannel
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode

object ChannelUtil {

    const val BEAGLE_CHANNEL_HEADER = "beagle-channel"
    private const val CHILDREN_FIELD = "children"
    private const val BEAGLE_CHANNEL_FIELD = "beagleChannel"

    fun treatBeagleChannel(currentChannel: String?, objectNode: ObjectNode): ObjectNode {
        if (currentChannel != null) {
            removeObjectFromTreeWhenChannelIsDifferToCurrentChannel(BeagleChannel.valueOf(currentChannel), objectNode)
        }
        removeBeagleChannelField(objectNode)
        return objectNode
    }

    private fun removeObjectFromTreeWhenChannelIsDifferToCurrentChannel(
        currentChannel: BeagleChannel,
        objectNode: ObjectNode
    ): Boolean {
        var isToRemove = false
        objectNode.fields().forEach {
            if (removeObjectFromTreeWhenChannelIsDifferToCurrentChannel(
                    currentChannel = currentChannel,
                    nodeEntry = it,
                    objectNode = objectNode
                )) {
                isToRemove = true
            }
        }
        return isToRemove
    }

    private fun removeObjectFromTreeWhenChannelIsDifferToCurrentChannel(
        currentChannel: BeagleChannel,
        nodeEntry: MutableMap.MutableEntry<String, JsonNode>,
        objectNode: ObjectNode
    ): Boolean {
        when {
            checkIfCurrentChannelIsDifferFromObjectChannel(currentChannel, nodeEntry) -> {
                return true
            }
            nodeEntry.value is ObjectNode -> {
                if (removeObjectFromTreeWhenChannelIsDifferToCurrentChannel(
                        currentChannel = currentChannel,
                        objectNode = nodeEntry.value as ObjectNode
                    )) {
                    treatChildrenItems(
                        currentChannel = currentChannel,
                        objectNode = nodeEntry.value as ObjectNode,
                        parentNode = objectNode
                    )
                    objectNode.remove(nodeEntry.key)
                }
            }
            nodeEntry.value is ArrayNode -> {
                treatArrayNode(
                    currentChannel = currentChannel,
                    arrayNode = nodeEntry.value as ArrayNode,
                    parentNode = objectNode
                )
            }
        }
        return false
    }

    private fun treatChildrenItems(currentChannel: BeagleChannel, objectNode: ObjectNode, parentNode: ObjectNode) {
        if (objectNode.has(CHILDREN_FIELD)) {
            treatArrayNode(
                currentChannel = currentChannel,
                arrayNode = objectNode.get(CHILDREN_FIELD) as ArrayNode,
                parentNode = parentNode
            )
            if (parentNode.has(CHILDREN_FIELD)) {
                (parentNode.get(CHILDREN_FIELD) as ArrayNode).addAll(objectNode.get(CHILDREN_FIELD) as ArrayNode)
            } else {
                parentNode.set(CHILDREN_FIELD, objectNode.get(CHILDREN_FIELD) as ArrayNode)
            }
        }
    }

    private fun treatArrayNode(currentChannel: BeagleChannel, arrayNode: ArrayNode, parentNode: ObjectNode) {
        val indexToRemoveList = mutableListOf<Int>()
        arrayNode.forEachIndexed { index, node ->
            if (node is ObjectNode) {
                if (removeObjectFromTreeWhenChannelIsDifferToCurrentChannel(
                        currentChannel = currentChannel,
                        objectNode = node
                    )) {
                    indexToRemoveList.add(index)
                }
            }
        }
        indexToRemoveList.forEach {
            treatChildrenItems(
                currentChannel = currentChannel,
                objectNode = arrayNode.get(it) as ObjectNode,
                parentNode = parentNode
            )
            arrayNode.remove(it)
        }
    }

    private fun removeBeagleChannelField(objectNode: ObjectNode) {
        if (objectNode.has(BEAGLE_CHANNEL_FIELD)) {
            objectNode.remove(BEAGLE_CHANNEL_FIELD)
        }
        objectNode.fields().forEach {
            if (it.value is ObjectNode) {
                removeBeagleChannelField(it.value as ObjectNode)
            } else if (it.value is ArrayNode) {
                (it.value as ArrayNode).forEach { node ->
                    if (node is ObjectNode) {
                        removeBeagleChannelField(node)
                    }
                }
            }
        }
    }

    private fun checkIfCurrentChannelIsDifferFromObjectChannel(
        currentChannel: BeagleChannel,
        node: MutableMap.MutableEntry<String, JsonNode>
    ) =
        node.key == BEAGLE_CHANNEL_FIELD
            && (node.value as TextNode).asText() != BeagleChannel.ALL.name
            && (node.value as TextNode).asText() != currentChannel.name
}