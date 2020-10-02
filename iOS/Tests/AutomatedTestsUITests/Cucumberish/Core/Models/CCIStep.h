//
//	CCIStep.h
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
#import "CCILocation.h"
#import "CCIArgument.h"

typedef NS_ENUM(NSInteger,CCIStepStatus) {
    CCIStepStatusNotExecuted = 0,
    CCIStepStatusPassed,
    CCIStepStatusFailed
};

/**
 Represents a step in a scenario
 Step contains information about its argument, location and keyword (When, Then, Given, etc...)
 */
@interface CCIStep : NSObject<NSCopying>

/**
 Step argument in case it is a DocString or DataTable step
 */
@property (nonatomic, strong) CCIArgument * argument;

/**
 Set to the keyword of the previous step when the keyword for this step is And
 */
@property (nonatomic, copy) NSString * contextualKeyword;

/**
 Can be  When, Then, Given, etc...
 */
@property (nonatomic, copy) NSString * keyword;

/**
 The location where this step has been written
 */
@property (nonatomic, strong) CCILocation * location;

/**
 The text of the step that comes after the keyword
 */
@property (nonatomic, copy) NSString * text;

/**
 The step status
 */
@property (nonatomic, assign) CCIStepStatus status;

/**
 The time it took to run the step
 */
@property (nonatomic, assign) NSTimeInterval duration;

/**
 All embedded objects added to this step in runtime
 */
@property (nonatomic, strong) NSMutableArray * embeddings;

/**
 The match where this step was called from
 */
@property (nonatomic, strong) NSDictionary * match;

/**
 If this step is a substep
 */
@property (nonatomic, assign) BOOL isSubstep;

/**
 Creates an instance with properties filled from the passed dictionary
 
 @param dictionary the dictionary that contains all the step data
 
 @return step instance
 */
-(instancetype)initWithDictionary:(NSDictionary *)dictionary;

/**
 Creates a dictionary from the class properties
 
 @return the created dictionary
 */
-(NSDictionary *)toDictionary;

/**
 @return a string composed of the keyword and the text of the step
 */
- (NSString *)fullName;
@end
