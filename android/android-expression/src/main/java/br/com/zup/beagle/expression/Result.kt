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

package br.com.zup.beagle.expression

/**
 * This class is based on flipkart-incubator/proteus's implementation
 * link @{link https://github.com/flipkart-incubator/proteus/blob/master/proteus-core/src/main/java/com/flipkart/android/proteus/toolbox/Result.java}
 * @author hernandazevedozup
 */
internal class Result(
    /**
     * Indicates the return status of the method for a given data path. The return value
     * will be `RESULT_SUCCESS` if and only if the data path exists and contains
     * a valid [com.flipkart.android.proteus.value.Value].
     */
    val resultCode: Int,
    /**
     * The value at the specified data path.
     * `value` will be null if `RESULT_CODE` != `RESULT_SUCCESS`
     */

    val value: Value
) {

    /**
     * @return true if and only if `RESULT_CODE` == `RESULT_SUCCESS`.
     */
    fun isSuccess(): Boolean = this.resultCode == RESULT_SUCCESS

    companion object {

        /**
         * Indicates that a valid [Value] was found at the specified data path.
         */
        val RESULT_SUCCESS = 0

        /**
         * Indicates that the object does not have the specified data path.
         */
        val RESULT_NO_SUCH_DATA_PATH_EXCEPTION = -1

        /**
         * Indicates that the data path specified is invalid. As an example, looking for a
         * property inside a [com.flipkart.android.proteus.value.Primitive] or [com.flipkart.android.proteus.value.Array].
         */
        val RESULT_INVALID_DATA_PATH_EXCEPTION = -2

        /**
         * Indicates that the data path prematurely led to a [com.flipkart.android.proteus.value.Null]
         */
        val RESULT_NULL_EXCEPTION = -3

        /**
         * singleton for No Such Data Path Exception.
         */
        val NO_SUCH_DATA_PATH_EXCEPTION =
            Result(RESULT_NO_SUCH_DATA_PATH_EXCEPTION, Null.INSTANCE)

        /**
         * singleton for Invalid Data Path Exception.
         */
        val INVALID_DATA_PATH_EXCEPTION =
            Result(RESULT_INVALID_DATA_PATH_EXCEPTION, Null.INSTANCE)

        /**
         * singleton for Null Exception.
         */
        val NULL_EXCEPTION = Result(RESULT_NULL_EXCEPTION, Null.INSTANCE)

        /**
         * This method return a [Result] object with `RESULT_CODE` == `RESULT_SUCCESS`
         * and `Result#value` == `value`.
         *
         * @param value The [Value] to be wrapped.
         * @return A [Result] object with with `RESULT_CODE` == `RESULT_SUCCESS`.
         */
        fun success(value: Value): Result {
            return Result(RESULT_SUCCESS, value)
        }
    }

}