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

package br.com.zup.beagle.android.context

import android.util.Log
import br.com.zup.beagle.android.action.SetContext

object GlobalContext:GlobalcontextAPI {
    var globalContext = ContextData(id = "global", value ="Teste2")

    override fun get(path:String?): Any {

        return globalContext.value
        Log.d("TestContext", "GetContext: $globalContext")
    }

    override fun set(path: String?, value:Any){



        Log.d("TestContext", "SetContext- O global tem valor: $globalContext")
    }

    override fun clear(path: String?){

        Log.d("TestContext", "ClearContext - O global tem valor: $globalContext")
    }
}
