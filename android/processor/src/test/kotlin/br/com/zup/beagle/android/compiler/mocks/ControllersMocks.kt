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

package br.com.zup.beagle.android.compiler.mocks

const val VALID_DEFAULT_CONTROLLER =
    """
        import br.com.zup.beagle.android.annotation.RegisterController
        import br.com.zup.beagle.android.view.BeagleActivity
        
        @RegisterController()
        class AppDefaultBeagleActivity : BeagleActivity 
    """

const val VALID_DEFAULT_CONTROLLER_BEAGLE_COMPONENT =
    """
        @BeagleComponent
        class AppDefaultBeagleActivityBeagleComponent : BeagleActivity 
    """

const val VALID_SECOND_DEFAULT_CONTROLLER =
    """
        @RegisterController()
        class AppDefaultBeagleActivityTwo : BeagleActivity 
    """


const val DEFAULT_IMPORTS =
    """
        import br.com.zup.beagle.android.annotation.RegisterController
        import br.com.zup.beagle.android.view.BeagleActivity
    """

const val VALID_CONTROLLER =
    """
        @RegisterController("otherController")
        class AppBeagleActivity : BeagleActivity 
    """

const val VALID_SECOND_CONTROLLER =
    """
        @RegisterController("otherControllerTwo")
        class AppBeagleActivityTwo : BeagleActivity 
    """

const val VALID_LIST_CONTROLLERS = VALID_DEFAULT_CONTROLLER + VALID_CONTROLLER + VALID_SECOND_CONTROLLER

const val INTERNAL_SINGLE_CONTROLLER_GENERATED_EXPECTED: String =
    """
        @file:Suppress("UNCHECKED_CAST")
        
        package br.com.test.beagle
        
        import br.com.zup.beagle.android.navigation.BeagleControllerReference
        import br.com.zup.beagle.android.view.BeagleActivity
        import java.lang.Class
        import kotlin.String
        import kotlin.Suppress
        
        public final class RegisteredControllers : BeagleControllerReference {
            public override fun classFor(id: String?): Class<BeagleActivity> = when (id) {
                "otherController" -> br.com.test.beagle.AppBeagleActivity::class.java as
                    Class<BeagleActivity>
                else -> br.com.zup.beagle.android.view.ServerDrivenActivity::class.java as
                    Class<BeagleActivity>
            }
        
        }

    """

const val INTERNAL_UNDEFINED_DEFAULT_CONTROLLER_GENERATED_EXPECTED: String =
    """
        @file:Suppress("UNCHECKED_CAST")
        
        package br.com.test.beagle
        
        import br.com.zup.beagle.android.navigation.BeagleControllerReference
        import br.com.zup.beagle.android.view.BeagleActivity
        import java.lang.Class
        import kotlin.String
        import kotlin.Suppress
        
        public final class RegisteredControllers : BeagleControllerReference {
            public override fun classFor(id: String?): Class<BeagleActivity> =  
                br.com.zup.beagle.android.view.ServerDrivenActivity::class.java as Class<BeagleActivity>
        
        }

    """

const val INTERNAL_DEFAULT_CONTROLLER_GENERATED_EXPECTED: String =
    """
        @file:Suppress("UNCHECKED_CAST")
        
        package br.com.test.beagle
        
        import br.com.zup.beagle.android.navigation.BeagleControllerReference
        import br.com.zup.beagle.android.view.BeagleActivity
        import java.lang.Class
        import kotlin.String
        import kotlin.Suppress
        
        public final class RegisteredControllers : BeagleControllerReference {
        
            public override fun classFor(id: String?): Class<BeagleActivity> =
                br.com.test.beagle.AppDefaultBeagleActivity::class.java as Class<BeagleActivity>
        }

    """

const val INTERNAL_DEFAULT_CONTROLLER_BEAGLE_COMPONENT_GENERATED_EXPECTED: String =
    """
        @file:Suppress("UNCHECKED_CAST")

        package br.com.test.beagle
        
        import br.com.zup.beagle.android.navigation.BeagleControllerReference
        import br.com.zup.beagle.android.view.BeagleActivity
        import java.lang.Class
        import kotlin.String
        import kotlin.Suppress
        
        public final class RegisteredControllers : BeagleControllerReference {
            public override fun classFor(id: String?): Class<BeagleActivity> = when(id) {
                "otherController" -> br.com.test.beagle.AppBeagleActivity::class.java as Class<BeagleActivity>
                else -> br.com.test.beagle.AppDefaultBeagleActivityBeagleComponent::class.java as Class<BeagleActivity>
            }
        
        }
    """

const val INTERNAL_LIST_CONTROLLER_GENERATED_EXPECTED: String =
    """
        @file:Suppress("UNCHECKED_CAST")
        
        package br.com.test.beagle
        
        import br.com.zup.beagle.android.navigation.BeagleControllerReference
        import br.com.zup.beagle.android.view.BeagleActivity
        import java.lang.Class
        import kotlin.String
        import kotlin.Suppress
        
        public final class RegisteredControllers : BeagleControllerReference {
            public override fun classFor(id: String?): Class<BeagleActivity> = when (id) {
                "otherControllerTwo" -> br.com.test.beagle.AppBeagleActivityTwo::class.java as
                    Class<BeagleActivity>
                "otherController" -> br.com.test.beagle.AppBeagleActivity::class.java as
                    Class<BeagleActivity>
                else -> br.com.test.beagle.AppDefaultBeagleActivity::class.java as
                    Class<BeagleActivity>
            }
        
        }

    """

const val INTERNAL_LIST_CONTROLLER_WITH_REGISTRAR_GENERATED_EXPECTED: String =
    """
        @file:Suppress("UNCHECKED_CAST")

        package br.com.test.beagle
        
        import br.com.zup.beagle.android.navigation.BeagleControllerReference
        import br.com.zup.beagle.android.view.BeagleActivity
        import java.lang.Class
        import kotlin.String
        import kotlin.Suppress
        
        public final class RegisteredControllers : BeagleControllerReference {
          public override fun classFor(id: String?): Class<BeagleActivity> = when(id) {
              "otherControllerTwo" -> br.com.test.beagle.AppBeagleActivityTwo::class.java as
              Class<BeagleActivity>
              "otherController" -> br.com.test.beagle.AppBeagleActivity::class.java as Class<BeagleActivity>
        
              "controllerFromOtherModule" -> br.com.test.beagle.otherModule.ModuleBeagleActivity::class.java
              as Class<BeagleActivity>
              else -> br.com.test.beagle.AppDefaultBeagleActivity::class.java as Class<BeagleActivity>
          }
        
        }
    """

const val INTERNAL_CONTROLLER_REGISTRAR_SINGLE_CONTROLLER_EXPECTED =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
    
        package br.com.zup.beagle.android.registrar
        
        import kotlin.Pair
        import kotlin.String
        import kotlin.Suppress
        import kotlin.collections.List
        
        public final object RegisteredControllersRegistrarTest {
          public fun classFor(): List<Pair<String, String>> {
            val registeredComponents = listOf<Pair<String, String>>(
               
                Pair(""${'"'}otherController""${'"'},"br.com.test.beagle.AppBeagleActivity"),
            )
            return registeredComponents
          }
        }
    """

const val INTERNAL_CONTROLLER_REGISTRAR_DEFAULT_CONTROLLER_EXPECTED =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
    
        package br.com.zup.beagle.android.registrar
        
        import kotlin.Pair
        import kotlin.String
        import kotlin.Suppress
        import kotlin.collections.List
        
        public final object RegisteredControllersRegistrarTest {
          public fun classFor(): List<Pair<String, String>> {
            val registeredComponents = listOf<Pair<String, String>>(
               
                Pair(""${'"'}${'"'}${'"'}${'"'},"br.com.test.beagle.AppDefaultBeagleActivity"),
            )
            return registeredComponents
          }
        }
    """

const val INTERNAL_CONTROLLER_REGISTRAR_UNDEFINED_DEFAULT_CONTROLLER_EXPECTED =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")

        package br.com.zup.beagle.android.registrar
        
        import kotlin.Pair
        import kotlin.String
        import kotlin.Suppress
        import kotlin.collections.List
        
        public final object RegisteredControllersRegistrarTest {
          public fun classFor(): List<Pair<String, String>> {
            val registeredComponents = listOf<Pair<String, String>>(
               
            )
            return registeredComponents
          }
        }
    """

const val INTERNAL_CONTROLLER_REGISTRAR_LIST_CONTROLLER_EXPECTED =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")

        package br.com.zup.beagle.android.registrar
        
        import kotlin.Pair
        import kotlin.String
        import kotlin.Suppress
        import kotlin.collections.List
        
        public final object RegisteredControllersRegistrarTest {
          public fun classFor(): List<Pair<String, String>> {
            val registeredComponents = listOf<Pair<String, String>>(
               
                Pair(""${'"'}otherControllerTwo""${'"'},"br.com.test.beagle.AppBeagleActivityTwo"),
                Pair(""${'"'}otherController""${'"'},"br.com.test.beagle.AppBeagleActivity"),
                Pair(""${'"'}${'"'}${'"'}${'"'},"br.com.test.beagle.AppDefaultBeagleActivity"),
            )
            return registeredComponents
          }
        }
    """