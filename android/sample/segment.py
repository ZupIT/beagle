# /*
# * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
# *
# * Licensed under the Apache License, Version 2.0 (the "License");
# * you may not use this file except in compliance with the License.
# * You may obtain a copy of the License at
# *
# *     http://www.apache.org/licenses/LICENSE-2.0
# *
# * Unless required by applicable law or agreed to in writing, software
# * distributed under the License is distributed on an "AS IS" BASIS,
# * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# * See the License for the specific language governing permissions and
# * limitations under the License.
# */

import cv2
import math
import numpy as np
import glob
import sys
extensionName = ".png"
pathRecorded = sys.argv[1]
pathRun = sys.argv[2]


def verify_screenshots():
    executed = False
    for filename in glob.glob(pathRecorded+'/*'+extensionName):
        executed = True
        fileNameWithoutPath = filename.replace(pathRecorded+"/", "")
        fileNameWithoutExtension = fileNameWithoutPath.replace(extensionName,"")
        imgSegmented = segmentate_image_with_defined_size(fileNameWithoutExtension)
        check_images_segmented_similar_to_images_on_pathRun(imgSegmented)
    if not executed:
        raise Exception("No screenshot founded")

def check_images_segmented_similar_to_images_on_pathRun(images):
    fail = False
    if len(images) <= 0:
        raise Exception("No screenshot founded")
    for i in range(len(images)):
        image, name = images[i]
        teste = cv2.imread(pathRun+"/"+name)
        if not is_similar(image, teste):
            fail = True
            print(name+" is not similar")
    if fail:
        raise Exception("Some screenshots are diferent")

def is_similar(image1, image2):
    return image1.shape == image2.shape and not(np.bitwise_xor(image1,image2).any())

def get_size_to_cut(imageName):
    img = cv2.imread(pathRun+"/"+imageName+extensionName)
    return img.shape

def segmentate_image_with_defined_size(imageName):
    image = cv2.imread(pathRecorded+"/"+imageName+extensionName)
    height, width, chanel = image.shape
    heightToSegmentate, widthToSegmentate, chanelsImage = get_size_to_cut(imageName)
    numberOfColumns= math.ceil(width/widthToSegmentate)
    numberOfRows = math.ceil(height/heightToSegmentate)
    listImageSegmanted = []
    for i in range(0, numberOfRows):
        initialRowValue = i*heightToSegmentate
        finalRowValue = (i+1)*heightToSegmentate
        if finalRowValue > height:
            finalRowValue = height
        for j in range(0, numberOfColumns):
            initialColumnValue = j*widthToSegmentate
            finalColumnValue = (j+1)*widthToSegmentate
            if finalColumnValue > width:
                finalColumnValue = width
            if i == 0 and j == 0:
                imageNewName = imageName+extensionName
            else:
                imageNewName = imageName+"_"+str(j)+"_"+str(i)+extensionName
            img = (image[initialRowValue:finalRowValue,initialColumnValue:finalColumnValue,0:chanel], imageNewName)

            listImageSegmanted.append(img)
    return listImageSegmanted

verify_screenshots()