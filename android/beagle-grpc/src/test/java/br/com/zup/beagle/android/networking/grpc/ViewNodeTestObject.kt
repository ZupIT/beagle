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

package br.com.zup.beagle.android.networking.grpc

import beagle.Messages

val viewNode = Messages.ViewNode
    .newBuilder()
    .setAttributes("{\"navigationBar\":null,\"identifier\":null,\"safeArea\":null,\"screenAnalyticsEvent\":null}")
    .setBeagleComponent("beagle:screenComponent")
    .setChild(
        Messages.ViewNode
            .newBuilder()
            .setAttributes("{\"onInit\":null,\"accessibility\":null}")
            .setBeagleComponent("beagle:container")
            .addChildren(
                Messages.ViewNode
                    .newBuilder()
                    .setAttributes("{\"accessibility\":null,\"styleId\":null,\"text\":\"Hello. This is a text!\",\"alignment\":null,\"textColor\":null}")
                    .setBeagleComponent("beagle:text")
                    .build()
            )
            .build()
    )
    .build()

val viewNodeString = "{\"_beagleComponent_\":\"beagle:screenComponent\",\"child\":{\"_beagleComponent_\":\"beagle:container\",\"children\":[{\"_beagleComponent_\":\"beagle:text\",\"text\":\"Hello. This is a text!\"}]}}"