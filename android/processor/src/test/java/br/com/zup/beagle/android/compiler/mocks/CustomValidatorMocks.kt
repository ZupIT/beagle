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

const val VALID_CUSTOM_VALIDATOR =
    """
        
        import br.com.zup.beagle.android.annotation.RegisterValidator
        import br.com.zup.beagle.android.components.form.core.Validator
        
        @RegisterValidator("text-is-not-blank")
        class TextNotBlankValidator : Validator<String, String> {
            override fun isValid(input: String, widget: String): Boolean {
                return !input.isBlank()
            }
        }
    """

const val VALID_SECOND_CUSTOM_VALIDATOR =
    """
        @RegisterValidator("text-is-not-blank-two")
        class TextNotBlankValidatorTwo : Validator<String, String> {
            override fun isValid(input: String, widget: String): Boolean {
                return !input.isBlank()
            }
        }
    """

const val VALID_LIST_CUSTOM_VALIDATOR = VALID_CUSTOM_VALIDATOR + VALID_SECOND_CUSTOM_VALIDATOR

const val INTERNAL_SINGLE_CUSTOM_VALIDATOR_GENERATED_EXPECTED: String =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
        
        package br.com.test.beagle
        
        import br.com.zup.beagle.android.components.form.core.Validator
        import br.com.zup.beagle.android.components.form.core.ValidatorHandler
        import kotlin.Any
        import kotlin.String
        import kotlin.Suppress
        
        public final object RegisteredCustomValidator : ValidatorHandler {
            public override fun getValidator(name: String): Validator<Any, Any>? = when (name) {
                "text-is-not-blank" -> br.com.test.beagle.TextNotBlankValidator() as
                    Validator<Any, Any>
        
                else -> null
            }
        
        }


    """

const val INTERNAL_LIST_CUSTOM_VALIDATOR_GENERATED_EXPECTED: String =
    """
        @file:Suppress("OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_EXPRESSION")
        
        package br.com.test.beagle
        
        import br.com.zup.beagle.android.components.form.core.Validator
        import br.com.zup.beagle.android.components.form.core.ValidatorHandler
        import kotlin.Any
        import kotlin.String
        import kotlin.Suppress
        
        public final object RegisteredCustomValidator : ValidatorHandler {
            public override fun getValidator(name: String): Validator<Any, Any>? = when (name) {
                "text-is-not-blank-two" -> br.com.test.beagle.TextNotBlankValidatorTwo() as
                    Validator<Any, Any>
                "text-is-not-blank" -> br.com.test.beagle.TextNotBlankValidator() as
                    Validator<Any, Any>
        
                else -> null
            }
        
        }
        
    """

const val INVALID_CUSTOM_VALIDATOR =
    """
        import br.com.zup.beagle.android.annotation.RegisterValidator
        import br.com.zup.beagle.android.components.form.core.Validator
        
        @RegisterValidator("text-is-not-blank")
        class InvalidCustomValidator { }
    """

const val INVALID_CUSTOM_VALIDATOR_WITH_INHERITANCE =
    """
        import br.com.zup.beagle.android.annotation.RegisterValidator
        import br.com.zup.beagle.android.components.form.core.Validator

        @RegisterValidator("text-is-not-blank")
        class InvalidCustomValidator : WidgetView { }
    """

