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

import android.content.Context
import br.com.zup.beagle.action.ActionListener
import br.com.zup.beagle.action.CustomAction
import br.com.zup.beagle.action.CustomActionHandler
import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.action.Route
import br.com.zup.beagle.annotation.BeagleComponent
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Text
import java.lang.Thread.sleep

@BeagleComponent
class AppCustomActionHandler : CustomActionHandler {

    override fun handle(context: Context, action: CustomAction, listener: ActionListener) {
        listener.onStart()
        when (action.name) {
            "formAsyncSubmit" -> Thread().run {
                print("I'm doing something clever here")
                sleep(3000)
                listener.onSuccess(Navigate.PushView(
                    Route.Local(
                        Screen(
                            child = Text("Testing CustomAction"),
                            navigationBar = NavigationBar(title = "After CustomAction")
                        )
                    )
                ))
            }
        }
        print("Custom Action executed")
    }
}