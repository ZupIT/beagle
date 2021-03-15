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

package br.com.zup.beagle.sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.TextInput
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.core.Display
import br.com.zup.beagle.core.PositionType
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.StyleComponent
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.Size

fun Column(
    children: List<ServerDrivenComponent> = listOf(),
    context: ContextData? = null,
    onInit: List<Action>? = null,
    reverse: Boolean = false,

    ): Container {
    return Styled(Container(
        children = children,
        context = context,
        onInit = onInit
    ), {
        flex.flexDirection = if (!reverse) FlexDirection.COLUMN else FlexDirection.COLUMN_REVERSE
        flex.grow = 1.0
    })
}


fun <T : StyleComponent> Center(child: T): T {
    return Styled(child, {
        flex.justifyContent = JustifyContent.CENTER
        flex.alignContent = AlignContent.CENTER
        flex.alignSelf = AlignSelf.CENTER
    })
}

fun Row(
    context: ContextData? = null,
    onInit: List<Action>? = null,
    reverse: Boolean = false,
    children: List<ServerDrivenComponent> = listOf(),
): Container {
    return Styled(Container(
        children = children,
        context = context,
        onInit = onInit
    ), {
        flex.flexDirection = if (!reverse) FlexDirection.ROW else FlexDirection.ROW_REVERSE
        flex.grow = 1.0
    })
}


class ContextOperationsFragment : Fragment() {

    private val style = Style(margin = EdgeValue(all = UnitValue(50.0, UnitType.REAL)), flex = Flex(shrink = 0.0))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val declarative = screen()

        return context?.let { declarative.toView(this) }
    }

    fun screen() = Styled(Column(
        context = ContextData("counter", 2),
        children = listOf(
            Styled(Row(children = listOf(
                Center(Text(expressionOf("Counter: @{sum(2, 1)}"))),
                Center(Text(expressionOf("Counter: @{sum(2, 1)}"))),
            )), {}),
            Button(
                text = "increment",
                onPress = listOf(SetContext("counter", "@{sum(counter, 1)}"))
            ),
            Button(
                text = "decrement",
                onPress = listOf(SetContext("counter", "@{subtract(counter, 1)}"))
            ),
            Text(text = "The text bellow will show if the counter + 2 is below 5 or not"),
            Text(expressionOf(
                "@{condition(lt(sum(counter, 2), 5), 'less then 5', 'greater then 5')}")
            ).applyStyle(Style(backgroundColor = "#00FF00")),
            Container(
                context = ContextData("cpf", ""),
                children = listOf(
                    TextInput(placeholder = "CPF", onChange = listOf(
                        SetContext(
                            contextId = "cpf",
                            value = "@{onChange.value}"
                        )
                    )),
                    Text("@{condition(isValidCpf(cpf), 'cpf is valid', 'cpf is not valid')}")
                )
            )
        )
    ), {
        backgroundColor = "#0000FF"
    })

    companion object {
        fun newInstance(): ContextOperationsFragment {
            return ContextOperationsFragment()
        }
    }
}


fun <T : StyleComponent> Styled(component: T, block: StyleBuilder.() -> Unit): T {
    component.style = StyleBuilder(component.style).apply(block).build()
    return component
}

data class StyleBuilder(private val style: Style?) {
    var backgroundColor: String? = style?.backgroundColor
    var cornerRadius: CornerRadius? = style?.cornerRadius
    var size: Size? = style?.size
    var margin: EdgeValue? = style?.margin
    var padding: EdgeValue? = style?.padding
    var position: EdgeValue? = style?.position
    var flex: Flex = style?.flex ?: Flex()
    var positionType: PositionType? = style?.positionType
    var display: Bind<Display>? = style?.display
    var borderColor: String? = style?.borderColor
    var borderWidth: Double? = style?.borderWidth

    private fun getStyle() = style ?: Style()

    fun build() = getStyle().copy(
        backgroundColor = backgroundColor,
        cornerRadius = cornerRadius,
        borderColor = borderColor,
        borderWidth = borderWidth,
        size = size,
        margin = margin,
        padding = padding,
        position = position,
        flex = flex,
        positionType = positionType,
        display = display,
    )

}
