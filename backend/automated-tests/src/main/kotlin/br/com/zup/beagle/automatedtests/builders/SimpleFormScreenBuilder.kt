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

package br.com.zup.beagle.automatedtests.builders

import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.action.SubmitForm
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.form.SimpleForm
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.TextInput


object SimpleFormScreenBuilder {
 fun build() = Screen(
  child = Container(
    children = listOf(
      Text(text = "context container"),
      SimpleForm(
        context = ContextData(id = "user", value = ""),
        children = listOf(
          Text(text = "SimpleForm"),
          TextInput(
            value = "@{user.email}",
            placeholder = "Type in your email",
            onChange = listOf(
                SetContext(
                    path = "user",
                    contextId = "email",
                    value = "@{onChange.value}"
                )
            )
          ),
          TextInput(
            value = "@{user.password}",
            placeholder = "Type in your password",

            onChange = listOf(
              SetContext(
                  path = "user",
                  contextId = "password",
                  value = "@{onChange.value}"
              )
            )
          ),
          Button(text = "Click to Submit",
            onPress = listOf(SubmitForm()
            )
          )
        ),
        onSubmit = listOf(
          Alert(
              title = "Submit",
              message = "The email is " + "@{user.email} " + "and the password is " + "@{user.password}"
          )
        )
      )
    )
  )
 )
}

