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

package br.com.zup.beagle.widget.core

/**
 *  defines how the image fits the view it's in
 *
 * @property FIT_XY
 * @property FIT_CENTER
 * @property CENTER_CROP
 * @property CENTER
 */
enum class ImageContentMode {
    /**
     * Compute a scale that will maintain the original aspect ratio,
     * but will also ensure that it fits entirely inside the destination view.
     * At least one axis (X or Y) will fit exactly. The result is centered inside the destination.
     */
    FIT_XY,

    /**
     * Compute a scale that will maintain the original aspect ratio,
     * but will also ensure that it fits entirely inside the destination view.
     * At least one axis (X or Y) will fit exactly.
     * The result is centered inside the destination.
     */
    FIT_CENTER,

    /**
     * Scale the image uniformly (maintain the image's aspect ratio) so that both dimensions
     * (width and height) of the image will be equal to or larger than
     * the corresponding dimension of the view (minus padding).
     */
    CENTER_CROP,

    /**
     * Center the image in the view but perform no scaling.
     */
    CENTER
}
