//
//	CCIScenarioDefinition.h
//
//	Created by Ahmed Ali on 17/1/2016
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
#import "CCIExample.h"
#import "CCIStep.h"

extern const NSString * kScenarioOutlineKeyword;
extern const NSString * kBackgroundKeyword;

/**
 Represents one scenario or one outline with its example found in your .feature file
 */
@interface CCIScenarioDefinition : NSObject <NSCopying>

/**
 Array of found examples in case it is an outline
 */
@property (nonatomic, strong) NSArray<CCIExample *> * examples;

@property (nonatomic, strong, readonly) NSArray<CCIScenarioDefinition*> *outlineChildScenarios;//this is necessary to output each executed scenario from the scenario outline in the JSON. This keeps track of the individual tests cases generated from an outline to ensure proper JSON output

-(void)addOutlineChildScenario:(CCIScenarioDefinition*)scenario;

/**
 Keyword is usually Scenario or Scenario Outline
 */
@property (nonatomic, copy) NSString * keyword;

/**
 The location of this scenario in its file
 */
@property (nonatomic, strong) CCILocation * location;

/**
 The name of the scenario
 */
@property (nonatomic, copy) NSString * name;

/**
 Array of steps the defines the scenario, in the same order they originally found in the scenario
 */
@property (nonatomic, strong) NSArray <CCIStep *> * steps;

/**
 Array of tags found on top of this scenario
 */
@property (nonatomic, strong) NSArray <NSString *> * tags;

@property (nonatomic, strong) NSArray<NSDictionary*>* rawTags;

/**
 Can be Scenario or ScenarioOutline
 */
@property (nonatomic, copy) NSString * type;

/**
 In case the execution of this scenario is failed, this property will have the failure message
 */
@property (nonatomic, strong) NSString * failureReason;

/**
 Bool determines the success or failure of the scenario.
 */
@property (nonatomic, assign) BOOL success;

/**
 Creates an instance with properties filled from the passed dictionary
 
 @param dictionary the dictionary that contains all the scenario data
 
 @return scenario instance
 */
-(instancetype)initWithDictionary:(NSDictionary *)dictionary;

/**
 Creates a dictionary from the class properties
 
 @return the created dictionary
 */
-(NSDictionary *)toDictionary;
@end
