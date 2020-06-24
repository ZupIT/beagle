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

package br.com.zup.beagle.android.jsonpath

import org.json.JSONArray
import org.json.JSONObject

val COMPLEX_JSON_ARRAY_WITH_NO_VALUES =
    JSONArray(
        """
                [
                  [
                    null,
                    [
                      [
                        null,
                        null,
                        [
                          null,
                          null,
                          null,
                          null
                        ]
                      ]
                    ]
                  ]
                ]
            """
    )
val COMPLEX_JSON_ARRAY_WITH_VALUES =
    JSONArray(
        """
                [
                  [
                    {
                    "name": "Name Test"
                    },
                    [
                      [
                        {
                             "name": "Name Test two"
                        },
                        [
                          null,
                          null,
                          null,
                          null
                        ],
                        null
                      ]
                    ]
                  ]
                ]
            """
    )

val COMPLEX_JSON_ARRAY_WITH_VALUES_RESULT =
    JSONArray(
        """
                [
                  [
                    {
                    "name": "Name Test"
                    },
                    [
                      [
                        {
                             "name": "Name Test two"
                        },
                     [
                          null,
                          null,
                          null,
                          null
                        ],
                        [
                          null,
                          null,
                          null,
                          null
                        ]
                      ]
                    ]
                  ]
                ]
            """
    )


val COMPLEX_JSON_OBJECT_WITH_VALUES =
    JSONObject(
        """
                {
                  "firstName": "John",
                  "lastName" : "doe",
                  "age"      : 26,
                  "address"  : {
                    "streetAddress": "naist street",
                    "city"         : "Nara",
                    "postalCode"   : "630-0192"
                  },
                  "phoneNumbers": [
                    {
                      "type"  : "iPhone",
                      "number": "0123-4567-8888"
                    },
                    {
                      "type"  : "home",
                      "number": "0123-4567-8910"
                    }
                  ]
                }
            """
    )

val COMPLEX_JSON_OBJECT_WITH_VALUES_RESULT =
    JSONObject(
        """
                {
                    "firstName": "John",
                    "lastName" : "doe",
                    "age"      : 26,
                    "address"  : {
                        "streetAddress": "naist street",
                        "city" : {
                        "name": null
                    },
                        "postalCode"   : "630-0192"
                    },
                    "phoneNumbers": [
                    {
                        "type"  : "iPhone",
                        "number": "0123-4567-8888"
                    },
                    {
                        "type"  : "home",
                        "number": "0123-4567-8910"
                    }
                    ]
                }
            """
    )

val COMPLEX_JSON_OBJECT_WITH_NO_VALUES =
    JSONObject(
        """
                {
                  "address": {
                    "city": {
                      "name": {
                        "city": null
                      }
                    }
                  }
                }
            """
    )

val COMPLEX_JSON_OBJECT_WITH_ARRAY =
    JSONObject(
        """
                {
                  "name": {
                    "d": [
                      {
                        "e": [
                          null,
                          null,
                          null,
                          null,
                          null,
                          "teste"
                        ]
                      }
                    ]
                  },
                  "teste": "teste initial",
                  "person": {
                    "name": "dd",
                    "teste": "aaaa"
                  }
                }
            """
    )