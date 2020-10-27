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
 * FlexWrap is set on containers and it controls what happens when children overflow
 * the size of the container along the main axis.
 * By default, children are forced into a single line (which can shrink elements).
 * If wrapping is allowed, items are wrapped into multiple lines along the main axis if needed.
 *
 * @property NO_WRAP
 * @property WRAP
 * @property WRAP_REVERSE
 */
enum class FlexWrap {
    /**
     *  The flex items are laid out in a single line which may cause the flex container to overflow.
     *  The cross-start is either equivalent to start or before depending flex-direction value.
     *  This is the default value.
     */
    NO_WRAP,

    /**
     *  The flex items break into multiple lines.
     *  The cross-start is either equivalent to start or
     *  before depending flex-direction value and the cross-end is the opposite of the specified cross-start.
     */
    WRAP,

    /**
     *  Behaves the same as wrap but cross-start and cross-end are permuted.
     */
    WRAP_REVERSE
}