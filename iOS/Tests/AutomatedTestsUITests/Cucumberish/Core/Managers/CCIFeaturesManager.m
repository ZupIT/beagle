//
//  CCIFeaturesManager.m

//
//  Created by Ahmed Ali on 02/01/16.
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

#import "CCIFeaturesManager.h"
#import "GHParser+Extensions.h"
#import "NSObject+Dictionary.h"
#import "CCIFeature.h"
#import "CCIStepsManager.h"
#import "Cucumberish.h"
#import "CCIStepDefinition.h"



@interface CCIFeaturesManager()

@property NSMutableDictionary * featureClassMap;

@end

@implementation CCIFeaturesManager
+ (instancetype)instance {
    static CCIFeaturesManager * instance = nil;
    @synchronized(self) {
        if(instance == nil){
            instance = [[CCIFeaturesManager alloc] init];
        }
    }
    return instance;
}


- (instancetype) init
{
    self = [super init];
    self.featureClassMap = [@{} mutableCopy];
    return self;
}

- (void)parseFeatureFiles:(NSArray *)featureFiles bundle:(NSBundle *)bundle withTags:(NSArray *)tags execludeFeaturesWithTags:(NSArray *)execludedFeatures
{
    NSMutableArray * parsedFeatures = [NSMutableArray array];

    GHParser * featureParser = [[GHParser alloc] init];
    
    NSSortDescriptor * urlSortDescriptor = [NSSortDescriptor sortDescriptorWithKey:@"absoluteString" ascending:YES];
    NSArray * sortedFeatureFiles = [featureFiles sortedArrayUsingDescriptors:@[urlSortDescriptor]];
    
    for (NSURL * filePath in sortedFeatureFiles) {
        id result = [featureParser parse:filePath.path];
        if(result == nil){
            //Nothing to do here...
            //Need to think about how to report this error in non-blocker way
            continue;
        }
        NSMutableDictionary * featureData = [[result dictionary] mutableCopy];

        NSString * testBundlePath = [bundle bundlePath];
        NSString * localPath = [[[filePath.absoluteString stringByRemovingPercentEncoding]
                                 stringByReplacingOccurrencesOfString:testBundlePath withString:@""]
                                stringByReplacingOccurrencesOfString:@"file://" withString:@""];

        featureData[@"location"][@"filePath"] = localPath;

        CCIFeature * feature = [[CCIFeature alloc] initWithDictionary:featureData];
        feature = [self cleanedUpFeature:feature includeTags:tags excludeTags:execludedFeatures];

        if(feature != nil){
            [parsedFeatures addObject:feature];
        }

    }

    _features = parsedFeatures;
}

- (NSArray * __nullable)cleanedUpExcludedExamples:(NSArray *)examples excludeTags:(NSArray<NSString *> *)excludeTags
{
    NSMutableArray * cleanedUpExamples = [NSMutableArray new];

    // Won't be called unless there are excludeTags
    for(CCIExample * example in examples) {
        if([self tags:example.tags doesNotIntersectWithTags:excludeTags]){
            [cleanedUpExamples addObject:example];
        }
    }
    return cleanedUpExamples;
}

- (NSArray * __nullable)cleanedUpIncludedExamples:(NSArray *)examples includeTags:(NSArray<NSString *> *)includeTags
{
    NSMutableArray * cleanedUpExamples = [NSMutableArray new];

    // Will only be called if there are includeTags
    for(CCIExample * example in examples) {
        if([self tags:example.tags intersectWithTags:includeTags]) {
            [cleanedUpExamples addObject:example];
        }
    }
    return cleanedUpExamples;
}

- (NSArray * __nullable)cleanedUpScenarios:(NSArray *)scenarios includeTags:(NSArray<NSString *> *)includeTags excludeTags:(NSArray<NSString *> *)excludeTags
{
    NSMutableArray * cleanedUpScenarios = [NSMutableArray new];

    if(excludeTags.count > 0){
        for(CCIScenarioDefinition * s in scenarios){
            if([self tags:s.tags doesNotIntersectWithTags:excludeTags]){
                // The scenario has not been excluded, but perhaps some (or all) of the examples have
                if([s.keyword isEqualToString:(NSString *)kScenarioOutlineKeyword]) {
                    s.examples = [self cleanedUpExcludedExamples:s.examples excludeTags:excludeTags];
                    if(s.examples.count > 0) {
                        [cleanedUpScenarios addObject:s];
                    }
                }else{
                    [cleanedUpScenarios addObject:s];
                }
            }
        }
    }else{
        //If there is no excluding tags, nothing should be excluded
        cleanedUpScenarios = [scenarios mutableCopy];
    }

    //At this point, cleanedUpScenarios holds the scenarios that have not been excluded
    //Now we need to include only the scenarios that should be included
    if(includeTags.count > 0){
        NSMutableArray * matchingScnarios = [NSMutableArray new];
        for(CCIScenarioDefinition * s in cleanedUpScenarios){
            if([self tags:s.tags intersectWithTags:includeTags]){
                [matchingScnarios addObject:s];
            }else{
                // The scenario has not been included, but perhaps some (or all) of the examples have
                if([s.keyword isEqualToString:(NSString *)kScenarioOutlineKeyword]) {
                    s.examples = [self cleanedUpIncludedExamples:s.examples includeTags:includeTags];
                    if (s.examples.count > 0) {
                        [matchingScnarios addObject:s];
                    }
                }
            }
        }
        cleanedUpScenarios = matchingScnarios;
    }

    return cleanedUpScenarios;
}

- (CCIFeature * __nullable)cleanedUpFeature:(CCIFeature *)feature includeTags:(NSArray<NSString *> *)includeTags excludeTags:(NSArray<NSString *> *)excludeTags
{
    if(excludeTags.count > 0){
        if([self tags:feature.tags intersectWithTags:excludeTags]){
            //No need to check on the scenario level here, as the scenarios will inherit all the feature tags anyway.
            feature = nil;
        }
    }

    feature.scenarioDefinitions = [self cleanedUpScenarios:feature.scenarioDefinitions includeTags:includeTags excludeTags:excludeTags];
    if(feature.scenarioDefinitions.count == 0){
        feature = nil;
    }
    return feature;
}

- (BOOL)tags:(NSArray *)tags intersectWithTags:(NSArray *)tagsToCheckAgainst
{
    BOOL intersect = NO;
    for(NSString * t in tags){
        if([tagsToCheckAgainst containsObject:t]){
            intersect = YES;
            break;
        }
    }

    return intersect;
}

- (BOOL)tags:(NSArray *)tags doesNotIntersectWithTags:(NSArray *)tagsToCheckAgainst
{
    return ![self tags:tags intersectWithTags:tagsToCheckAgainst];
}


- (void)setClass:(Class)klass forFeature:(CCIFeature *)feature
{
    NSString * className = NSStringFromClass(klass);
    self.featureClassMap[className] = feature;
}

- (CCIFeature *)getFeatureForClass:(Class)klass
{
    NSString * className = NSStringFromClass(klass);
    return self.featureClassMap[className];
}

@end
