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

package br.com.zup.beagle.annotation

/**
 * Annotates a method that return a Widget.
 * # Example: #
 * ```
 * class ScreenExample {
 *
 *  @BeaglePreview
 *  fun makeComponent(): Screen {
 *          return Screen(
 *              child = Container(
 *              children = listOf(
 *                  Text("Live Preview!!!")
 *                  )
 *              )
 *          )
 *  }
 *
 * }
 * ```
 *
 * # Limitations #
 * <p> The @BeaglePreview tag only works for methods that return widgets
 * and don't have any attributes as a parameter and don't be private;
 * <p>
 * The LivePreview client is able to render any change based on values, that is,
 * as long as a new feature or widget has NOT been added and there are no structural changes.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BeaglePreview