//
//  CCIJSONDumper.m
//  AMBluetooth
//
//  Created by ereynolds on 11/11/16.
//  Copyright © 2016 Ahmed Ali. All rights reserved.
//

#import "CCIJSONDumper.h"
#import "CCIFeature.h"

#if TARGET_OS_IPHONE || TARGET_OS_SIMULATOR
#import <UIKit/UIKit.h>
#endif

@import Darwin;
@implementation CCIJSONDumper
+ (instancetype)instance {
    static CCIJSONDumper * instance = nil;
    @synchronized(self) {
        if(instance == nil){
            instance = [[CCIJSONDumper alloc] init];
        }
    }
    return instance;
}

+(NSString*)testEnv
{
#if TARGET_OS_IPHONE || TARGET_OS_SIMULATOR
    return [[UIDevice currentDevice] systemVersion];
#elif TARGET_OS_OSX
    NSOperatingSystemVersion version = [[NSProcessInfo processInfo] operatingSystemVersion];
    return ([NSString stringWithFormat:@"%ld.%ld.%ld", version.majorVersion, version.minorVersion, version.patchVersion]);
#endif
    
}
+(NSData*)buildJSONOutputData:(NSArray<CCIFeature *> *)features
{
    NSArray*rawOutput = [self convertMultipleFeaturesToJSONDictionary:features];
    return [NSJSONSerialization dataWithJSONObject:rawOutput options:NSJSONWritingPrettyPrinted error:nil];
}

+(NSString*)buildJSONOutputString:(NSArray<CCIFeature*> *)features
{
    NSString* retVal = [[NSString alloc] initWithData:[self buildJSONOutputData:features] encoding:NSUTF8StringEncoding];
    NSLog(@"%@", retVal);
    return retVal;
}

+(NSString*)writeJSONToFile:(NSString*)filename
                forFeatures:(NSArray<CCIFeature*>*)features
{
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];

    return [self writeJSONToFile:filename inDirectory:documentsDirectory forFeatures:features];
    
}

+(NSString*)writeJSONToFile:(NSString*)filename
                inDirectory:(NSString *)directory
                forFeatures:(NSArray<CCIFeature*>*)features
{
    
    NSData *data = [self buildJSONOutputData:features];
    NSString* fileName = [NSString stringWithFormat:@"%@.json", filename];
    NSString *dataPath = [directory stringByAppendingPathComponent:fileName];
    
    if (![self directoryExists:directory]) {
        [self createDirectory:directory];
    }
    
    [data writeToFile:dataPath atomically:YES];
    return dataPath;
}

+(NSArray*)convertMultipleFeaturesToJSONDictionary:(NSArray<CCIFeature*>*)features
{
    NSMutableArray *retVal = [NSMutableArray array];
    for (CCIFeature *feature in features) {
        [retVal addObject:[self convertFeatureToJSONDictionary:feature]];
    }
    return retVal;
}

+(NSDictionary*)convertFeatureToJSONDictionary:(CCIFeature*)feature
{
    
    NSDictionary *retVal = [NSDictionary dictionary];
    
    retVal = [self addKeywordToDictionary:retVal forFeature:feature];
    retVal = [self addTagsToDictionary:retVal forFeature:feature];
    retVal = [self addNameToDictionary:retVal forFeature:feature];
    retVal = [self addDescriptionToDictionary:retVal forFeature:feature];
    retVal = [self addURIToDictionary:retVal forFeature:feature];
    retVal = [self addLineToDictionary:retVal forFeature:feature];
    retVal = [self addIdToDictionary:retVal forFeature:feature];
    
    retVal = [self addElementsToDictionary:retVal forFeature:feature];
    
    
    
    
    return retVal;
    
}

#pragma mark - private functions
+(BOOL)directoryExists:(NSString *)path {
    BOOL isDir;
    return ([[NSFileManager defaultManager]
             fileExistsAtPath:path isDirectory:&isDir] && isDir) ;
}

+(void)createDirectory:(NSString *)path {
    [[NSFileManager defaultManager] createDirectoryAtPath: path
                              withIntermediateDirectories:YES
                                               attributes:nil
                                                    error:NULL];
}

#pragma mark - ID Formatting
+(NSDictionary*)addIdToDictionary:(NSDictionary*)currentDictionary forFeature:(CCIFeature*)feature
{
    NSDictionary * idDictionary= [self convertIdToOutputDictionaryForFeature:feature];
    if (!idDictionary)
    {
        return currentDictionary;
    }
    else
    {
        NSMutableDictionary* retVal = [currentDictionary mutableCopy];
        [retVal addEntriesFromDictionary:idDictionary];
        return retVal;
    }
}

+(NSDictionary*)convertIdToOutputDictionaryForFeature:(CCIFeature*)feature
{
    if (feature.name != nil && feature.name.length > 0)
    {
        NSDictionary *keywordDictionary = @{@"id":[feature.name.lowercaseString stringByReplacingOccurrencesOfString:@" " withString:@"-"]};
        return keywordDictionary;
    }
    return nil;
}


#pragma mark - Keyword Formatting
+(NSDictionary*)addKeywordToDictionary:(NSDictionary*)currentDictionary forFeature:(CCIFeature*)feature
{
    NSDictionary * keywordDictionary= [self convertKeywordToOutputDictionaryForFeature:feature];
    if (!keywordDictionary)
    {
        return currentDictionary;
    }
    else
    {
        NSMutableDictionary* retVal = [currentDictionary mutableCopy];
        [retVal addEntriesFromDictionary:keywordDictionary];
        return retVal;
    }
}

+(NSDictionary*)convertKeywordToOutputDictionaryForFeature:(CCIFeature*)feature
{
    NSDictionary *keywordDictionary = @{@"keyword":@"Feature"};
    return keywordDictionary;
}

#pragma mark - Name Formatting
+(NSDictionary*)addNameToDictionary:(NSDictionary*)currentDictionary forFeature:(CCIFeature*)feature
{
    NSDictionary * nameDictionary= [self convertNameToOutputDictionary:feature];
    if (!nameDictionary)
    {
        return currentDictionary;
    }
    else
    {
        NSMutableDictionary* retVal = [currentDictionary mutableCopy];
        [retVal addEntriesFromDictionary:nameDictionary];
        return retVal;
    }
}

+(NSDictionary*)convertNameToOutputDictionary:(CCIFeature*)feature
{
    if ((feature.name != nil) && (feature.name.length > 0))
    {
        NSDictionary *nameDictionary = @{@"name":feature.name};
        return nameDictionary;
    }
    return nil;
    
}

#pragma mark - Description Formatting
+(NSDictionary*)addDescriptionToDictionary:(NSDictionary*)currentDictionary forFeature:(CCIFeature*)feature
{
    NSDictionary * descriptionDictionary= [self convertDescriptionToOutputDictionary:feature];
    if (!descriptionDictionary)
    {
        return currentDictionary;
    }
    else
    {
        NSMutableDictionary* retVal = [currentDictionary mutableCopy];
        [retVal addEntriesFromDictionary:descriptionDictionary];
        return retVal;
    }
}

+(NSDictionary*)convertDescriptionToOutputDictionary:(CCIFeature*)feature
{
    if ((feature.docDescription != nil) && (feature.docDescription.length > 0))
    {
        NSDictionary *descriptionDictionary = @{@"description":feature.docDescription};
        return descriptionDictionary;
    }
    return nil;
    
}

#pragma mark - URI Formatting
+(NSDictionary*)addURIToDictionary:(NSDictionary*)currentDictionary forFeature:(CCIFeature*)feature
{
    NSDictionary * uriDictionary= [self convertURIToOutputDictionary:feature];
    if (!uriDictionary)
    {
        return currentDictionary;
    }
    else
    {
        NSMutableDictionary* retVal = [currentDictionary mutableCopy];
        [retVal addEntriesFromDictionary:uriDictionary];
        return retVal;
    }
}

+(NSDictionary*)convertURIToOutputDictionary:(CCIFeature*)feature
{
    
    NSDictionary *uriDictionary = @{@"uri":[feature.location.filePath stringByReplacingOccurrencesOfString:@"\\/" withString:@"/"]};
    return uriDictionary;
    
    
}

#pragma mark - Line Formatting
+(NSDictionary*)addLineToDictionary:(NSDictionary*)currentDictionary forFeature:(CCIFeature*)feature
{
    NSDictionary * lineDictionary= [self convertURIToOutputDictionary:feature];
    if (!lineDictionary)
    {
        return currentDictionary;
    }
    else
    {
        NSMutableDictionary* retVal = [currentDictionary mutableCopy];
        [retVal addEntriesFromDictionary:lineDictionary];
        return retVal;
    }
}

+(NSDictionary*)convertLineToOutputDictionary:(CCIFeature*)feature
{
    
    NSDictionary *lineDictionary = @{@"uri":[NSNumber numberWithInteger:feature.location.line]};
    return lineDictionary;
    
    
}

#pragma mark - Tag Formatting
+(NSDictionary*)addTagsToDictionary:(NSDictionary*)currentDictionary forFeature:(CCIFeature*)feature
{
    NSDictionary * tagsDictionary = [self convertTagsToOutputDictionaryFromFeature:feature];
    if (!tagsDictionary)
    {
        return currentDictionary;
    }
    else
    {
        NSMutableDictionary* retVal = [currentDictionary mutableCopy];
        [retVal addEntriesFromDictionary:tagsDictionary];
        return retVal;
    }
}
+(NSDictionary*)convertTagsToOutputDictionaryFromFeature:(CCIFeature*)feature
{
    NSArray *tagDictionaries = [self convertTagsToOutputArrayFromFeature:feature];
    if (tagDictionaries)
    {
        NSDictionary *retVal = @{@"tags":tagDictionaries};
        return retVal;
    }
    return nil;
}
+(NSArray*)convertTagsToOutputArrayFromFeature:(CCIFeature*)feature
{
    NSMutableArray *retVal= [NSMutableArray array];
    if ([feature.rawTags count] > 0)
    {
        for(NSDictionary*tag in feature.rawTags)
        {
            [retVal addObject:[self convertRawTagToOutputDictionary:tag]];
        }
    }
    else if ([feature.tags count] > 0)
    {
        for(NSString*strTag in feature.rawTags)
        {
            [retVal addObject:[self convertTagToOutputDictionary:strTag]];
        }
    }
    if ([retVal count] >0)
    {
        return retVal;
    }
    return nil;
    
}
+(NSDictionary*)convertTagsToOutputDictionaryFromScenario:(CCIScenarioDefinition*)scenario
{
    NSArray *tagDictionaries = [self convertTagsToOutputArrayFromScenario:scenario];
    if (tagDictionaries)
    {
        NSDictionary *retVal = @{@"tags":tagDictionaries};
        return retVal;
    }
    return nil;
}
+(NSArray*)convertTagsToOutputArrayFromScenario:(CCIScenarioDefinition*)scenario
{
    NSMutableArray *retVal= [NSMutableArray array];
    if ([scenario.rawTags count] > 0)
    {
        for(NSDictionary*tag in scenario.rawTags)
        {
            [retVal addObject:[self convertRawTagToOutputDictionary:tag]];
        }
    }
    else if ([scenario.tags count] > 0)
    {
        for(NSString*strTag in scenario.tags)
        {
            [retVal addObject:[self convertTagToOutputDictionary:strTag]];
        }
    }
    if ([retVal count] >0)
    {
        return retVal;
    }
    return nil;
    
}

+(NSDictionary*)convertRawTagToOutputDictionary:(NSDictionary*)tag
{
    return @{@"name": tag[@"name"],
             @"line": tag[@"location"][@"line"]};
}
+(NSDictionary*)convertTagToOutputDictionary:(NSString*)tag
{
    return @{@"name": tag};
    
}

#pragma mark - Elements Formatting
+(NSDictionary*)addElementsToDictionary:(NSDictionary*)currentDictionary forFeature:(CCIFeature*)feature
{
    NSDictionary * elementsDictionary = [self convertElementsToOutputDictionary:feature];
    if (!elementsDictionary)
    {
        return currentDictionary;
    }
    else
    {
        NSMutableDictionary* retVal = [currentDictionary mutableCopy];
        [retVal addEntriesFromDictionary:elementsDictionary];
        return retVal;
    }
}

+(NSDictionary*)convertElementsToOutputDictionary:(CCIFeature*)feature
{
    NSArray *elementDictionaries = [self convertElementsToOutputArray:feature];
    if (elementDictionaries)
    {
        NSDictionary *retVal = @{@"elements":elementDictionaries};
        return retVal;
    }
    return nil;
}
+(NSArray*)convertElementsToOutputArray:(CCIFeature*)feature
{
    NSMutableArray *retVal= [NSMutableArray array];
    
    if ([feature.scenarioDefinitions count] > 0)
    {
        for(CCIScenarioDefinition*scenario in feature.scenarioDefinitions)
        {
            if ([scenario.type isEqualToString:@"Scenario"])
            {
                [retVal addObject:[self convertScenarioToJSONDictionary:scenario inFeature: (CCIFeature*)feature]];
            }
            else
            {
                [retVal addObjectsFromArray:[self convertScenarioOutlineToJSONDictionary:scenario inFeature: (CCIFeature*)feature]];
            }
        }
    }
    if ([retVal count] >0)
    {
        return retVal;
    }
    return nil;
}

+(NSDictionary*)convertScenarioToJSONDictionary:(CCIScenarioDefinition*)scenario inFeature: (CCIFeature*)feature
{
    NSMutableDictionary* retVal = [NSMutableDictionary dictionary];
    
    [retVal addEntriesFromDictionary:@{@"name":scenario.name}];
    [retVal addEntriesFromDictionary:@{@"keyword":scenario.keyword}];
    [retVal addEntriesFromDictionary:@{@"line": @(scenario.location.line)}];
    [retVal addEntriesFromDictionary:@{@"description":@""}];
    [retVal addEntriesFromDictionary:@{@"id":[[feature.name.lowercaseString stringByReplacingOccurrencesOfString:@" "
                                                                                                      withString:@"-"]
                                              stringByAppendingString:[NSString stringWithFormat:@";%@",[scenario.name.lowercaseString
                                                                                                         stringByReplacingOccurrencesOfString:@" "
                                                                                                         withString:@"-"]]]}];

    [retVal addEntriesFromDictionary:@{@"type": [scenario.keyword.lowercaseString stringByReplacingOccurrencesOfString:@" " withString:@"_"]}];
    
    [retVal addEntriesFromDictionary:[self convertTagsToOutputDictionaryFromScenario:scenario]];
    [retVal addEntriesFromDictionary:@{@"test_env":[self testEnv]}];
    
    
    [retVal addEntriesFromDictionary:[self convertStepsToOutputDictionary:scenario]];
    
    return retVal;
}
+(NSDictionary*)convertStepsToOutputDictionary:(CCIScenarioDefinition*)scenario
{
    NSArray* stepsArray = [self convertStepsToOutputArray:scenario];
    if (!stepsArray)
    {
        return nil;
    }
    return @{@"steps":stepsArray};
}
+(NSArray*)convertStepsToOutputArray:(CCIScenarioDefinition*)scenario
{
    NSMutableArray *retVal= [NSMutableArray array];
    
    if ([scenario.steps count] > 0)
    {
        for(CCIStep*step in scenario.steps)
        {
            [retVal addObject:[self convertStepToOutputDictionary:step fromScenario:scenario]];
            
        }
    }
    if ([retVal count] >0)
    {
        return retVal;
    }
    return nil;
}

+(NSDictionary*)convertStepToOutputDictionary:(CCIStep*)step fromScenario:(CCIScenarioDefinition*)scenario
{
    NSMutableDictionary *retVal= [NSMutableDictionary dictionary];
    [retVal addEntriesFromDictionary:@{@"keyword": step.keyword}];
    [retVal addEntriesFromDictionary:@{@"name": step.text}];
    [retVal addEntriesFromDictionary:@{@"line": @(step.location.line)}];
    
    NSMutableDictionary* result = [NSMutableDictionary dictionary];
    switch ([step status])
    {
        case CCIStepStatusNotExecuted:
            result[@"status"] = @"skipped";
            break;
            
        case CCIStepStatusPassed:
            result[@"status"] = @"passed";
            break;
            
        case CCIStepStatusFailed:
            result[@"status"] = @"failed";
            result[@"error_message"] = scenario.failureReason;
            break;
    }
    
    if (step.embeddings.count > 0) {
        [retVal addEntriesFromDictionary:@{@"embeddings":step.embeddings}];
    }
    
    if (step.match != nil) {
        [retVal addEntriesFromDictionary:@{@"match":step.match}];
    }
    result[@"duration"] = @(step.duration);
    
    [retVal addEntriesFromDictionary:@{@"result":result}];
    
    return retVal;
}
+(NSArray*)convertScenarioOutlineToJSONDictionary:(CCIScenarioDefinition*)scenarioOutline inFeature: (CCIFeature*)feature
{
    NSArray * scenarios = [scenarioOutline outlineChildScenarios];
    NSMutableArray* retVal = [NSMutableArray array];
    for (CCIScenarioDefinition* scenario in scenarios)
    {
        NSMutableDictionary * jsonDict =[[self convertScenarioToJSONDictionary:scenario inFeature: (CCIFeature*)feature] mutableCopy];
        jsonDict[@"type"] = @"scenario";
        [retVal addObject:jsonDict];
    }
    
    return retVal;
}

@end
