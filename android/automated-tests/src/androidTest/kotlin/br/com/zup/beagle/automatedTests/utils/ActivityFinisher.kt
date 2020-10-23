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

package br.com.zup.beagle.automatedTests.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import java.util.EnumSet
import java.util.concurrent.CountDownLatch


class ActivityFinisher : Runnable {
    private val activityLifecycleMonitor = ActivityLifecycleMonitorRegistry.getInstance()
    private var latch: CountDownLatch? = null
    private var activities: MutableList<Activity>? = null

    constructor(latch: CountDownLatch?, activities: MutableList<Activity>?) {
        this.latch = latch
        this.activities = activities
    }

    constructor() {}

    override fun run() {
        val activities = if (activities != null) activities else ArrayList()
        for (stage in EnumSet.range(Stage.CREATED, Stage.STOPPED)) {
            activities!!.addAll(activityLifecycleMonitor.getActivitiesInStage(stage))
        }
        if (latch != null) {
            latch!!.countDown()
        } else {
            for (activity in activities!!) {
                if (!activity.isFinishing) {
                    activity.finish()
                }
            }
        }
    }

    companion object {
        fun finishOpenActivities() {
            Handler(Looper.getMainLooper()).post(ActivityFinisher())
        }
    }
}

