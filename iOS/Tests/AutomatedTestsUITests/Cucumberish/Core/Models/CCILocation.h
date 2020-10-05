//
//	CCILocation.h
//
//	Created by Ahmed Ali on 2/1/2016
//  Copyright © 2016 Ahmed Ali. All rights reserved.
//
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

//	Model file Generated using JSONExport: https://github.com/Ahmed-Ali/JSONExport

#import <Foundation/Foundation.h>

/**
 Represents a location of a step, scenario or a feature
 */
@interface CCILocation : NSObject

/**
 The line where the associate node (step, scenario or a feature) is located
 */
@property (nonatomic, assign) NSInteger line;

/**
 The exact file path where the node exists
 */
@property (nonatomic, strong) NSString * filePath;


/**
 Creates an instance with properties filled from the passed dictionary
 
 @param dictionary the dictionary that contains all the location data
 
 @return location instance
 */
-(instancetype)initWithDictionary:(NSDictionary *)dictionary;

/**
 Creates a dictionary from the class properties
 
 @return the created dictionary
 */
-(NSDictionary *)toDictionary;
@end