//
//	CCIFeature.h
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
#import "CCIBackground.h"
#import "CCILocation.h"
#import "CCIScenarioDefinition.h"

/**
 Reperesents a feautre information
 
 Checkout the Feaure wiki for more information: https://github.com/Ahmed-Ali/Cucumberish/wiki/Feature
 
 */
@interface CCIFeature : NSObject

@property (nonatomic, strong) CCIBackground * background;
@property (nonatomic, strong) CCILocation * location;
@property (nonatomic, copy) NSString * name;
@property (nonatomic, copy) NSString * docDescription;//this property saves the description of the feature from the parsed feature to save it for JSON output
@property (nonatomic, strong) NSArray<CCIScenarioDefinition *> * scenarioDefinitions;
@property (nonatomic, strong) NSArray <NSString *> * tags;
@property (nonatomic, strong, readonly) NSArray <NSDictionary *> * rawTags;//the tags property loses information needed for the JSON output. This preserves the raw tag formatting
-(instancetype)initWithDictionary:(NSDictionary *)dictionary;

-(NSDictionary *)toDictionary;
@end