// THIS IS A GENERATED FILE. DO NOT EDIT THIS
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
 *
 * The flex is a Layout component that will handle your visual component positioning at the screen.
 * Internally Beagle uses a Layout engine called Yoga Layout to position elements on screen.
 * In fact it will use the HTML Flexbox properties applied on the visual components and its children.
 *
 * @param flexDirection
 *                          controls the direction in which the children of a node are laid out.
 *                          This is also referred to as the main axis
 * @param flexWrap
 *                  set on containers and controls what happens when children
 *                  overflow the size of the container along the main axis.
 * @param justifyContent align children within the main axis of their container.
 * @param alignItems Align items describes how to align children along the cross axis of their container.
 * @param alignSelf
 *                      Align self has the same options and effect as align items
 *                      but instead of affecting the children within a container.
 * @param alignContent Align content defines the distribution of lines along the cross-axis..
 * @param basis is an axis-independent way of providing the default size of an item along the main axis.
 * @param flex TODO.
 * @param grow describes how any space within a container should be distributed among its children along the main axis.
 * @param shrink
 *              describes how to shrink children along the main axis in the case that
 *              the total size of the children overflow the size of the container on the main axis.
 *
 */
data class Flex(
  override val flexDirection: FlexDirection? = null,
  override val flexWrap: FlexWrap? = null,
  override val justifyContent: JustifyContent? = null,
  override val alignItems: AlignItems? = null,
  override val alignSelf: AlignSelf? = null,
  override val alignContent: AlignContent? = null,
  override val basis: UnitValue? = null,
  override val flex: Double? = null,
  override val grow: Double? = null,
  override val shrink: Double? = null
) : FlexSchema 