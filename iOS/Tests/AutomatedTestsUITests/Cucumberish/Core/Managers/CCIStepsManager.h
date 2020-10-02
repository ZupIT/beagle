//
//  CCIStepsManager.h

//
//  Created by Ahmed Ali on 03/01/16.
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

#import <Foundation/Foundation.h>



@class CCIStep;

/**
 CCIStepsManager is a singleton class and its main purpose is to manage all step definitions and execute steps.
 */
@interface CCIStepsManager : NSObject

/**
 A set containing all the steps that are not defined when dry run is enabled
 */
@property (nonatomic, strong) NSMutableSet<CCIStep *> *undefinedSteps;

/**
 The step that is being executed
 */
@property (nonatomic, strong) CCIStep * currentStep;

/**
 Returns the singleton class of CCIStepsManager
 */
+ (instancetype)instance;

/**
 Executes the passed step if it matches any previously defined implementation. Or throw an error if there is no matching definiton.
 
 @param step the to be executed
 @param testCase the test case that is being executed when this step implementation is being called
 */
- (void)executeStep:(CCIStep *)step inTestCase:(id)testCase;

- (BOOL)executeStepInDryRun:(CCIStep *)step inTestCase:(id)testCase;

@end
