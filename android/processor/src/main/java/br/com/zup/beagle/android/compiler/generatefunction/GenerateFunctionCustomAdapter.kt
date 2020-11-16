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

package br.com.zup.beagle.android.compiler.generatefunction

import br.com.zup.beagle.android.annotation.RegisterBeagleAdapter
import br.com.zup.beagle.android.compiler.BEAGLE_CUSTOM_ADAPTER
import br.com.zup.beagle.android.compiler.BeagleSetupProcessor.Companion.REGISTERED_CUSTOM_TYPE_ADAPTER_GENERATED
import br.com.zup.beagle.compiler.shared.BeagleGeneratorFunction
import br.com.zup.beagle.compiler.shared.elementType
import br.com.zup.beagle.compiler.shared.error
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.WildcardType
import java.lang.reflect.Type

class GenerateFunctionCustomAdapter(private val processingEnv: ProcessingEnvironment) :
    BeagleGeneratorFunction<RegisterBeagleAdapter>(
        BEAGLE_CUSTOM_ADAPTER,
        REGISTERED_CUSTOM_TYPE_ADAPTER_GENERATED,
        RegisterBeagleAdapter::class.java
    ) {

    private var allCodeMappedWithAnnotation = ""

    override fun buildCodeByElement(element: Element, annotation: Annotation): String {
        val typeElement = element as TypeElement
        if (typeElement.interfaces.isEmpty()) return ""

        val declaredType = typeElement.interfaces[0] as DeclaredType
        val elementParameterizedTypeName =
            ((typeElement.interfaces[0] as DeclaredType).elementType as DeclaredType).asElement()

        return if ((declaredType.elementType as DeclaredType).toTypeArguments().isEmpty()) {
            "$elementParameterizedTypeName$JAVA_CLASS -> " +
                "$element() as $BEAGLE_TYPE_ADAPTER_INTERFACE$BREAK_LINE"
        } else {
            val stringBuilder = StringBuilder()
            createParameterizedType(stringBuilder, (declaredType.elementType as DeclaredType), element)
            stringBuilder.append(" -> $element() as $BEAGLE_TYPE_ADAPTER_INTERFACE$BREAK_LINE")
            stringBuilder.toString()
        }
    }

    override fun validationElement(element: Element, annotation: Annotation) {
        val typeElement = element as TypeElement
        if (typeElement.interfaces.size > 1 || typeElement.interfaces.isEmpty()) {
            processingEnv.messager.error("The class $element need to inherit" +
                " only from the class $BEAGLE_TYPE_ADAPTER_INTERFACE")
        }
    }

    override fun returnStatementInGenerate(): String = """
         |return when(type) {
                |   $allCodeMappedWithAnnotation
                |   else -> null
                |}
        |""".trimMargin()

    override fun getCodeFormatted(allCodeMappedWithAnnotation: String): String {
        this.allCodeMappedWithAnnotation = allCodeMappedWithAnnotation
        return ""
    }

    override fun createFuncSpec(name: String): FunSpec.Builder {
        val returnType = ClassName(BEAGLE_CUSTOM_ADAPTER.packageName,
            "BeagleTypeAdapter").parameterizedBy(
            TypeVariableName(T_GENERIC)
        ).copy(true) as ParameterizedTypeName

        return FunSpec.builder(REGISTERED_CUSTOM_ADAPTER)
            .addTypeVariable(TypeVariableName(T_GENERIC))
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("type", Type::class.asTypeName().copy(false))
            .returns(returnType)
    }

    private fun createParameterizedType(
        adapters: StringBuilder,
        item: DeclaredType,
        element: TypeElement? = null,
    ): StringBuilder {
        val parameterName = if (element != null) {
            ((element.interfaces[0] as DeclaredType).elementType as DeclaredType).asElement().toString()
        } else {
            item.asElement().toString()
        }

        adapters.append("$TYPES_INSTANCE${parameterName.removeExtends()}$JAVA_CLASS")

        val parametrizedItems = item.toTypeArguments()
        var checkedTimes = 0

        if (parametrizedItems.isNotEmpty()) {
            adapters.append(",$BREAK_LINE")
        }

        while (checkedTimes < parametrizedItems.size) {
            createType(adapters, parametrizedItems[checkedTimes])

            if (checkedTimes != parametrizedItems.lastIndex) {
                adapters.append(",$BREAK_LINE")
            }
            checkedTimes++
        }

        adapters.append(")")

        return adapters
    }

    private fun createType(adapters: StringBuilder, typeMirror: TypeMirror): StringBuilder {
        return when {
            typeMirror is DeclaredType -> {
                checkDeclaredType(adapters, typeMirror)
            }
            ((typeMirror as WildcardType).extendsBound) is DeclaredType -> {
                checkDeclaredType(adapters, (typeMirror.extendsBound) as DeclaredType)
            }
            else -> {
                adapters.append("$BREAK_LINE${typeMirror.toString().removeExtends()}$JAVA_CLASS")
            }
        }
    }

    private fun checkDeclaredType(adapters: StringBuilder, declaredType: DeclaredType): StringBuilder {
        val typeArgumentsItem = declaredType.toTypeArguments()

        if (typeArgumentsItem.isEmpty()) {
            return adapters.append("${declaredType.toString().removeExtends()}$JAVA_CLASS")
        }

        return createParameterizedType(adapters, declaredType)
    }

    companion object {
        const val REGISTERED_CUSTOM_ADAPTER = "getAdapter"
        const val JAVA_CLASS = "::class.java"
        const val BEAGLE_TYPE_ADAPTER_INTERFACE = "BeagleTypeAdapter<T>"
        const val TYPES_INSTANCE = "ParameterizedTypeFactory.new(\n"
        const val T_GENERIC = "T"
        const val BREAK_LINE = "\n"
    }
}

private fun DeclaredType.toTypeArguments(): List<TypeMirror> {
    return try {
        this.typeArguments
    } catch (e: Exception) {
        ArrayList()
    }
}

private fun String.removeExtends() = this.replace("? extends ", "")