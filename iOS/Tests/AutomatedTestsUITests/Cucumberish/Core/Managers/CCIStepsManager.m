//
//  CCIStepsManager.m

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

#import "CCIStepsManager.h"
#import "Cucumberish.h"
#import "CCIStep.h"
#import "CCIStepDefinition.h"

typedef void (^XCTContextActivityBlock)(id _Nullable activity);

static CCIStepsManager * instance = nil;


const NSString * kDataTableKey = @"DataTable";
const NSString * kDocStringKey = @"DocString";
const NSString * kXCTestCaseKey = @"XCTestCase";

@interface CCIStepsManager()

@property NSMutableDictionary * definitions;
@property (copy) NSString *currentContextKeyword;

@end

@implementation CCIStepsManager

+ (instancetype)instance {
    
    @synchronized(self) {
        if(instance == nil){
            instance = [[CCIStepsManager alloc] init];
        }
    }
    return instance;
}

- (instancetype)init
{
    self = [super init];
   
    self.definitions = [NSMutableDictionary dictionary];
    self.undefinedSteps = [NSMutableSet new];
    
    return self;
}


- (NSMutableArray *)definitionsCluster:(NSString *)type
{
    NSMutableArray * cluster = self.definitions[type];
    if(cluster == nil){
        cluster = [NSMutableArray array];
        self.definitions[type] = cluster;
    }
    
    return cluster;
}

- (CCIStepDefinition *)findMatchDefinitionForStep:(CCIStep *)step inTestCase:(id)testCase
{
    if(step.keyword == nil){
        //We should try to match it using all available definitions
        NSMutableArray * allDefinitions = [NSMutableArray array];
        for(NSArray * definitions in self.definitions.allValues){
            [allDefinitions addObjectsFromArray:definitions];
        }
        return [self findDefinitionForStep:step amongDefinitions:allDefinitions inTestCase:testCase];
    }

    return [self findDefinitionForStep:step amongDefinitions:[self definitionGroupForStep:step] inTestCase:testCase];
}

- (NSArray *)definitionGroupForStep:(CCIStep *)step
{
    NSArray *definitionGroup = self.definitions[step.keyword] ?: @[];
    if ([step.keyword isEqualToString:@"And"]) {
        step.contextualKeyword = self.currentContextKeyword;
        NSArray *contextDefinitionGroup = self.definitions[self.currentContextKeyword];
        definitionGroup = [definitionGroup arrayByAddingObjectsFromArray:contextDefinitionGroup];
    }

    return definitionGroup;
}

- (CCIStepDefinition *)findDefinitionForStep:(CCIStep *)step amongDefinitions:(NSArray *)definitions inTestCase:(id)testCase
{
    NSError * error;
    CCIStepDefinition * retDefinition = nil;
    
    for(CCIStepDefinition * d in definitions){
        NSString * pattern = d.regexString;
        
        if ([d.regexString isEqualToString:step.text]) {
            //It has no params, it is just plain perfect match
            retDefinition = [d copy];
            break;
        }
        NSRegularExpression * regex = [NSRegularExpression regularExpressionWithPattern:pattern options:NSRegularExpressionAnchorsMatchLines error:&error];
        if(error && d == definitions.lastObject){
            //Only return nil if we reached the last definition without finding a match
            break;
        }
        NSRange searchRange = NSMakeRange(0, [step.text length]);
        NSTextCheckingResult * match = [[regex matchesInString:step.text options:NSMatchingReportCompletion range:searchRange] firstObject];
        
        if (match.numberOfRanges > 1) {
            //Looks like a perfect match!
            CCIStepDefinition * definition = [d copy];
            NSMutableArray * values = [NSMutableArray arrayWithCapacity:match.numberOfRanges - 1];
            for(int i = 1; i < match.numberOfRanges; i++){
                NSRange range = [match rangeAtIndex:i];
                if (range.location != NSNotFound) {
                    NSString * value = [step.text substringWithRange:range];
                    [values addObject:value];
                }
            }
            
            definition.matchedValues = values;
            retDefinition = definition;
            break;
        } else if (match != nil && match.numberOfRanges == 1) {
            //this case means there was a match of just the text. This can occur if the regex has the start and end characters ^foobar$
            retDefinition = [d copy];
            break;
        }
    }
    
    if (retDefinition) {
        if(step.argument.rows.count > 0){
            retDefinition.additionalContent = @{kDataTableKey : step.argument.rows};
        }else if(step.argument.content.length > 0){
            retDefinition.additionalContent = @{kDocStringKey : step.argument.content};
        }
    }
    if([testCase isKindOfClass:[XCTestCase class]]){
        NSMutableDictionary * additionalContent = [retDefinition.additionalContent mutableCopy] ? :[NSMutableDictionary new];
        additionalContent[kXCTestCaseKey] = testCase;
        retDefinition.additionalContent = additionalContent;
    }
    
    
    return retDefinition;
}

- (BOOL)executeStepInDryRun:(CCIStep *)step inTestCase:(id)testCase
{
    if (![step.keyword isEqualToString:@"And"]) {
        self.currentContextKeyword = step.keyword;
    }

    return [self findMatchDefinitionForStep:step inTestCase:testCase] != nil;
}


- (void)executeStep:(CCIStep *)step inTestCase:(id)testCase
{
    if (!step.isSubstep) {
        self.currentStep = step;
    }

    if (step.keyword && ![step.keyword isEqualToString:@"And"]) {
        self.currentContextKeyword = step.keyword;
    }

    CCIStepDefinition * implementation = [self findMatchDefinitionForStep:step inTestCase:testCase];
    NSString * errorMessage = nil;
    if(step.keyword.length > 0){
        errorMessage = [NSString stringWithFormat:@"The step \"%@ %@\" is not implemented", step.keyword, [step.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]]];
    }else{
        //It is a step that is called from an step definition
        errorMessage = [NSString stringWithFormat:@"The implementation of this step, calls another step that is not implemented: %@", [step.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]]];
    }
    CCIAssert(implementation != nil, errorMessage);
    step.match = @{@"location": implementation.location};

    if(step.keyword.length > 0){
        NSLog(@"Currently executing: \"%@ %@\"", step.keyword, step.text);
    }

    if ([step.keyword isEqualToString:@"And"]) {
        implementation.type = @"And";
    }
    
    XCTContextActivityBlock activityBlock = ^(id activity) {
        NSDate *startDate = [NSDate date];
        implementation.body(implementation.matchedValues, implementation.additionalContent);
        step.duration = [[NSDate date] timeIntervalSinceDate:startDate] * 1000000000;
    };

    id xctContextClass = NSClassFromString(@"XCTContext");
    if (xctContextClass) {
        SEL aSelector = NSSelectorFromString(@"runActivityNamed:block:");

        if ([xctContextClass respondsToSelector:aSelector]) {
            NSInvocation *inv = [NSInvocation invocationWithMethodSignature:[xctContextClass methodSignatureForSelector:aSelector]];
            [inv setSelector:aSelector];
            [inv setTarget:xctContextClass];

            NSString *name = [NSString stringWithFormat:@"%@ %@", implementation.type, step.text];
            [inv setArgument:&(name) atIndex:2];
            [inv setArgument:&(activityBlock) atIndex:3];

            [inv invoke];
        }
    } else {
        activityBlock(nil);
    }

    //Clean up the step additional content to avoid keeping unwanted objects in memory
    implementation.additionalContent = nil;
    if(step.keyword.length > 0){
        NSLog(@"Step: \"%@ %@\" passed", step.keyword, step.text);
    }
    step.status = CCIStepStatusPassed;
}



@end


void addDefinition(NSString * definitionString, CCIStepBody body, NSString * type);

#pragma mark - C Routines

void Given(NSString * definitionString, CCIStepBody body)
{
    addDefinition(definitionString, body, @"Given");
}
void When(NSString * definitionString, CCIStepBody body)
{
    addDefinition(definitionString, body, @"When");
}
void Then(NSString * definitionString, CCIStepBody body)
{
    addDefinition(definitionString, body, @"Then");
}
void And(NSString * definitionString, CCIStepBody body)
{
    addDefinition(definitionString, body, @"And");
}
void But(NSString * definitionString, CCIStepBody body)
{
    addDefinition(definitionString, body, @"But");
}

void MatchAll(NSString * definitionString, CCIStepBody body)
{
    When(definitionString, body);
    Then(definitionString, body);
    But(definitionString, body);
}

void Match(NSArray *types, NSString * definitionString, CCIStepBody body)
{
    for (NSString * type in types) {
        addDefinition(definitionString, body, type);
    }
}
void addDefinition(NSString * definitionString, CCIStepBody body, NSString * type)
{
    NSString *stepDefinitionLocation = [[NSThread callStackSymbols] description];
    CCIStepDefinition * definition = [CCIStepDefinition definitionWithType:type regexString:definitionString location:stepDefinitionLocation implementationBody:body];
    NSMutableArray * cluster = [[CCIStepsManager instance] definitionsCluster:type];
    [cluster insertObject:definition atIndex:0];
}

void step(id testCase, NSString * stepLine, ...)
{
    va_list args;
    va_start(args, stepLine);
    NSString * line = [[NSString alloc] initWithFormat:stepLine arguments:args];
    va_end(args);
    
    CCIStep * step = [CCIStep new];
    step.text = line;
    step.isSubstep = YES;
    
    NSDate *startDate = [NSDate date];
    [[CCIStepsManager instance] executeStep:step inTestCase:testCase];
    step.duration = [[NSDate date] timeIntervalSinceDate:startDate] * 1000000000;
}

void SStep(id testCase, NSString * stepLine)
{
    step(testCase, stepLine);
}

