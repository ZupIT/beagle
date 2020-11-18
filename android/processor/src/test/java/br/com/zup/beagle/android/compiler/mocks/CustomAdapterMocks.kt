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

const val VALID_CUSTOM_ADAPTER =
    """
        
        import br.com.zup.beagle.android.annotation.RegisterBeagleAdapter
        import br.com.zup.beagle.android.data.serializer.adapter.generic.BeagleTypeAdapter
         
        interface Person
        
        @RegisterBeagleAdapter
        class PersonAdapter : BeagleTypeAdapter<Person> {
        
            override fun fromJson(json: String): Person = object: Person {}
        
            override fun toJson(type: Person): String  = ""
        }

    """

const val VALID_SECOND_CUSTOM_ADAPTER =
    """
        
        @RegisterBeagleAdapter
        class PersonTwoAdapter : BeagleTypeAdapter<List<List<List<Person>>>> {
        
            override fun fromJson(json: String): List<List<List<Person>>> =
                listOf(
                    listOf(
                        listOf(object : Person {})
                    )
                )
        
            override fun toJson(type: List<List<List<Person>>>): String = ""
        }
    """

const val VALID_LIST_CUSTOM_ADAPTER = VALID_CUSTOM_ADAPTER + VALID_SECOND_CUSTOM_ADAPTER

const val INTERNAL_SINGLE_CUSTOM_ADAPTER_GENERATED_EXPECTED: String =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
        package br.com.test.beagle
        import br.com.zup.beagle.android.`data`.serializer.adapter.generic.BeagleTypeAdapter
        import br.com.zup.beagle.android.`data`.serializer.adapter.generic.ParameterizedTypeFactory
        import br.com.zup.beagle.android.`data`.serializer.adapter.generic.TypeAdapterResolver
        import java.lang.reflect.Type
        import kotlin.Suppress
        
        public final object RegisteredCustomTypeAdapter : TypeAdapterResolver {
            public override fun <T> getAdapter(type: Type): BeagleTypeAdapter<T>? = when (type) {
                br.com.test.beagle.Person::class.java -> br.com.test.beagle.PersonAdapter() as BeagleTypeAdapter<T>
                else -> null
            }
        
        }

    """

const val INTERNAL_LIST_CUSTOM_ADAPTER_GENERATED_EXPECTED: String =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
        package br.com.test.beagle 
        import br.com.zup.beagle.android.`data`.serializer.adapter.generic.BeagleTypeAdapter
        import br.com.zup.beagle.android.`data`.serializer.adapter.generic.ParameterizedTypeFactory
        import br.com.zup.beagle.android.`data`.serializer.adapter.generic.TypeAdapterResolver
        import java.lang.reflect.Type
        import kotlin.Suppress
        
        public final object RegisteredCustomTypeAdapter : TypeAdapterResolver { 
            public override fun <T> getAdapter(type:Type) : BeagleTypeAdapter<T>? = 
                when(type) { 
                    ParameterizedTypeFactory.new(
                        java.util.List::class.java,
                        ParameterizedTypeFactory.new(
                            java.util.List::class.java,
                            ParameterizedTypeFactory.new(
                                java.util.List::class.java, br.com.test.beagle.Person::class.java
                            )
                        )
                    ) 
                    -> br.com.test.beagle.PersonTwoAdapter() as BeagleTypeAdapter<T>
                    br.com.test.beagle.Person::class.java -> br.com.test.beagle.PersonAdapter() as BeagleTypeAdapter<T>
                    else -> null 
            }
        }
        
    """

const val INVALID_CUSTOM_ADAPTER =
    """
        package br.com.test.beagle
        import br.com.zup.beagle.android.annotation.RegisterBeagleAdapter

        @RegisterBeagleAdapter
        class InvalidCustomAdapter { }
    """

const val INVALID_CUSTOM_ADAPTER_WITH_INHERITANCE =
    """
        import br.com.zup.beagle.android.annotation.RegisterBeagleAdapter

        @RegisterBeagleAdapter
        class InvalidCustomAdapter : WidgetView { }
    """

