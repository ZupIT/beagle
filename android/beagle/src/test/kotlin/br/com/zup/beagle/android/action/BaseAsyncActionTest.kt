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

package br.com.zup.beagle.android.action

import androidx.lifecycle.Observer
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
abstract class BaseAsyncActionTest : BaseTest() {

    internal val observer = mockk<Observer<AsyncActionStatus>>()

    private val asyncActionStatus = mutableListOf<AsyncActionStatus>()

    @BeforeEach
    override fun setUp() {
        super.setUp()

        every { observer.onChanged(capture(asyncActionStatus)) } just Runs
    }

    internal fun onActionFinishedWasCalled() = asyncActionStatus[0] == AsyncActionStatus.FINISHED &&
        asyncActionStatus[1] == AsyncActionStatus.IDLE

    internal fun onActionStartedWasCalled() = asyncActionStatus[0] == AsyncActionStatus.STARTED
}
