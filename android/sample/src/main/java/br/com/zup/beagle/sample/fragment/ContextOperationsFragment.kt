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
import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.Image
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.TextInput
import br.com.zup.beagle.android.components.center
import br.com.zup.beagle.android.components.column
import br.com.zup.beagle.android.components.layout.Center
import br.com.zup.beagle.android.components.layout.Column
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Row
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.components.styled
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.TextAlignment

fun MyButton(text: String) = styled(Button(text = text), {
    backgroundColor = "#00FFFF"
    padding = EdgeValue(all = UnitValue(10.0, UnitType.REAL))
})

fun MyButtonSecond(text: String) = styled(Button(text = text), {
    backgroundColor = "#0000A0"
    padding = EdgeValue(all = UnitValue(10.0, UnitType.REAL))
})

fun MyButtonSecondWithPadding(text: String) = styled(MyButtonSecond(text), {
    padding = EdgeValue(all = UnitValue(20.0, UnitType.REAL))
})

fun MyCenter(child: ServerDrivenComponent) = styled(center(child), {
    padding = EdgeValue(all = UnitValue(20.0, UnitType.REAL))
})

fun MyImage(
    path: Bind<ImagePath>,
    mode: ImageContentMode? = null,
) = styled(Image(path, mode), {
    padding = EdgeValue(all = UnitValue(20.0, UnitType.REAL))
})

fun MySecondImage(
    path: Bind<ImagePath>,
    mode: ImageContentMode? = null,
) = styled(MyImage(path, mode), {
    padding = EdgeValue(all = UnitValue(20.0, UnitType.REAL))
})

class ContextOperationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val declarative = screen()

        return context?.let { declarative.toView(this) }
    }

    fun screen() = column(
        listOf(
            Text("text"),
            MyImage(path = valueOf(ImagePath.Remote(url = "blabla"))),
            MyButton("UZIAS"),
            MyButtonSecond("UZIAS"),
            MyButtonSecondWithPadding("UZIAS"),
            center(Text("text")),
            center(Text("text"))
        ),
    )

    companion object {
        fun newInstance(): ContextOperationsFragment {
            return ContextOperationsFragment()
        }
    }
}