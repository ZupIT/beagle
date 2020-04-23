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

package br.zup.com.beagle.expression

import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.expression.JsonParser
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JsonParserTest {

    private val JSON = """{ "a": 
	{ "b": 
		{
		"c": 10, 
		"d": "olá",
		"e": [{"f": 15, "k": "18"}],
		"g": ["olá mundo", 2.5, true],
        "h": "11",
        "i": [{"j": "16"}]
		}
	}
}"""

    @Test
    fun json_parse_test() {
        JsonParser().parseJsonToValue(JSON)
    }

}