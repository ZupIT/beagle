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

package br.com.zup.beagle.sample

import br.com.zup.beagle.android.annotation.BeagleComponent
import br.com.zup.beagle.android.setup.DesignSystem

@BeagleComponent
class AppDesignSystem : DesignSystem() {

    override fun toolbarStyle(id: String): Int? {
        return when (id) {
            "DesignSystem.Navigationbar.Style.Green" -> R.style.DesignSystem_Navigationbar_Style
            "DesignSystem.Toolbar.Center" -> R.style.DesignSystem_Toolbar_Center
            else -> R.style.DesignSystem_Toolbar
        }
    }

    override fun tabViewStyle(id: String): Int? = R.style.DesignSystem_TabView_Default

    override fun image(id: String): Int? {
        return when (id) {
            "imageBeagle" -> R.drawable.beagle_image
            "informationImage" -> android.R.drawable.ic_menu_help
            "delete" -> android.R.drawable.ic_delete
            "TestImage" -> android.R.drawable.editbox_dropdown_dark_frame
            "beagle" -> R.drawable.beagle
            else -> android.R.drawable.ic_menu_help
        }
    }

    override fun textStyle(id: String): Int? {
        return when (id) {
            "DesignSystem.Text.H2" -> R.style.DesignSystem_Text_H2
            "DesignSystem.Text.H3" -> R.style.DesignSystem_Text_H3
            "DesignSystem.Text.Action.Click" -> R.style.DesignSystem_Text_Action_Click
            "DesignSystem.Text.helloWord" -> R.style.DesignSystem_Text_helloWord
            else -> R.style.DesignSystem_Text_Default
        }
    }

    override fun inputTextStyle(id: String): Int? {
        return when(id) {
            "TextInput" -> R.style.TextInput
            else -> null
        }
    }

    override fun buttonStyle(id: String): Int? {
        return when (id) {
            "DesignSystem.Button.White" -> R.style.DesignSystem_Button_White
            "DesignSystem.Button.Text" -> R.style.DesignSystem_Button_Text
            "DesignSystem.Button.Style" -> R.style.DesignSystem_Button_Style
            "DesignSystem.Button.Black" -> R.style.DesignSystem_Button_Black
            "DesignSystem.Stylish.Button" -> R.style.DesignSystem_Stylish_Button
            "DesignSystem.Button.Orange" -> R.style.DesignSystem_Button_Default
            "DesignSystem.Stylish.ButtonAndAppearance" -> R.style.DesignSystem_Stylish_ButtonAndAppearance
            "DesignSystem.Form.Submit" -> R.style.DesignSystem_Form_Submit
            else -> android.R.style.Widget_Button
        }
    }
}