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

package br.com.zup.beagle.utils

import com.github.romankh3.image.comparison.ImageComparison
import com.github.romankh3.image.comparison.ImageComparisonUtil
import com.github.romankh3.image.comparison.model.ImageComparisonResult
import java.awt.image.BufferedImage
import java.io.File


object ImageUtil {

    /**
     * If there is any difference between the images, writes the image result in file @param resultImageFile
     */
    fun compareImages(
        queryImageFile: File,
        referenceImageFile: File,
        resultImageFile: File
    ) {

        if (!queryImageFile.exists())
            throw Exception("Image ${queryImageFile} not found!")

        if (!referenceImageFile.exists())
            throw Exception("Image ${referenceImageFile} not found!")

        if (resultImageFile.exists())
            throw Exception("Image ${referenceImageFile} already exists!")

        val expectedImage: BufferedImage = ImageComparisonUtil.readImageFromResources(queryImageFile.absolutePath)
        val actualImage: BufferedImage = ImageComparisonUtil.readImageFromResources(referenceImageFile.absolutePath)

        val imageComparison = ImageComparison(expectedImage, actualImage, resultImageFile)
        imageComparison.setThreshold(10)
        imageComparison.setRectangleLineWidth(5)
        imageComparison.setDifferenceRectangleFilling(true, 30.0)
        imageComparison.setExcludedRectangleFilling(true, 30.0)
        imageComparison.setDestination(resultImageFile)
        imageComparison.setMaximalRectangleCount(10)
        imageComparison.setMinimalRectangleSize(100)
        imageComparison.setPixelToleranceLevel(0.2)
        val imageComparisonResult: ImageComparisonResult = imageComparison.compareImages()
        if ("MATCH".equals(imageComparisonResult.imageComparisonState.toString())) {
            return
        }

        ImageComparisonUtil.saveImage(resultImageFile, imageComparisonResult.result)

    }
}