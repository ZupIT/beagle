//
//	CCIScenarioDefinition.m
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
// THE SOFTWARE.	Model file Generated using JSONExport: https://github.com/Ahmed-Ali/JSONExport



#import "CCIScenarioDefinition.h"

const NSString * kScenarioOutlineKeyword = @"Scenario Outline";
const NSString * kBackgroundKeyword = @"Background";

@interface CCIScenarioDefinition ()
@property (nonatomic, strong) NSArray<CCIScenarioDefinition*> * outlineChildScenarios;
@end
@implementation CCIScenarioDefinition




/**
 * Instantiate the instance using the passed dictionary values to set the properties values
 */

-(void)addOutlineChildScenario:(CCIScenarioDefinition*)scenario
{
    if (![self outlineChildScenarios])
    {
        [self setOutlineChildScenarios:[NSMutableArray array]];
    }
    [(NSMutableArray*)self.outlineChildScenarios addObject:scenario];
}

-(instancetype)initWithDictionary:(NSDictionary *)dictionary
{
	self = [super init];
    
    if(dictionary[@"examples"] != nil && [dictionary[@"examples"] isKindOfClass:[NSArray class]]){
        NSArray * examplesDictionaries = dictionary[@"examples"];
        NSMutableArray * examplesItems = [NSMutableArray array];
        for(NSDictionary * examplesDictionary in examplesDictionaries){
            CCIExample * examplesItem = [[CCIExample alloc] initWithDictionary:examplesDictionary];
            [examplesItems addObject:examplesItem];
        }
        self.examples = examplesItems;
    }
	if(dictionary[@"keyword"] != nil && ![dictionary[@"keyword"] isKindOfClass:[NSNull class]]){
		self.keyword = dictionary[@"keyword"];
	}

	if(dictionary[@"location"] != nil && ![dictionary[@"location"] isKindOfClass:[NSNull class]]){
		self.location = [[CCILocation alloc] initWithDictionary:dictionary[@"location"]];
	}

	if(dictionary[@"name"] != nil && ![dictionary[@"name"] isKindOfClass:[NSNull class]]){
		self.name = dictionary[@"name"];
	}
  

	if(dictionary[@"steps"] != nil && [dictionary[@"steps"] isKindOfClass:[NSArray class]]){
		NSArray * stepDictionaries = dictionary[@"steps"];
		NSMutableArray * stepsItems = [NSMutableArray array];
		for(NSDictionary * stepDictionary in stepDictionaries){
            NSMutableDictionary * stepData = [stepDictionary mutableCopy];
            if(self.location.filePath.length > 0){
                stepData[@"location"][@"filePath"] = self.location.filePath;
            }
			CCIStep * stepsItem = [[CCIStep alloc] initWithDictionary:stepData];
			[stepsItems addObject:stepsItem];
		}
		self.steps = stepsItems;
	}
    if(dictionary[@"parsedTags"] != nil){
        self.tags = dictionary[@"parsedTags"];
    }else if(dictionary[@"tags"] != nil && [dictionary[@"tags"] isKindOfClass:[NSArray class]]){
        NSArray * tagsDictionaries = dictionary[@"tags"];
        self.rawTags = dictionary[@"tags"];
        NSMutableArray * tagsItems = [NSMutableArray array];
        for(NSDictionary * tagDictionary in tagsDictionaries){
            NSString * tagName = tagDictionary[@"name"];
            if([tagName hasPrefix:@"@"]){
                tagName = [tagName stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:@""];
            }
            [tagsItems addObject:tagName];
            
        }
        self.tags = tagsItems;
    }

	if(dictionary[@"type"] != nil && ![dictionary[@"type"] isKindOfClass:[NSNull class]]){
		self.type = dictionary[@"type"];
	}
    
    
    if(dictionary[@"success"] != nil && ![dictionary[@"success"] isKindOfClass:[NSNull class]]){
        self.success = [dictionary[@"success"] boolValue];
    }else{
        //By default scenario considered as success
        self.success = YES;
    }
    if(dictionary[@"failureReason"] != nil && ![dictionary[@"failureReason"] isKindOfClass:[NSNull class]]){
        self.failureReason = dictionary[@"failureReason"];
    }
	return self;
}


/**
 * Returns all the available property values in the form of NSDictionary object where the key is the approperiate json key and the value is the value of the corresponding property
 */
-(NSDictionary *)toDictionary
{
    NSMutableDictionary * dictionary = [NSMutableDictionary dictionary];
    if(self.examples != nil){
        NSMutableArray * dictionaryElements = [NSMutableArray array];
        for(CCIExample * examplesElement in self.examples){
            [dictionaryElements addObject:[examplesElement toDictionary]];
        }
        dictionary[@"examples"] = dictionaryElements;
    }
    if(self.keyword != nil){
        dictionary[@"keyword"] = self.keyword;
    }
    if(self.location != nil){
        dictionary[@"location"] = [self.location toDictionary];
    }
    if(self.name != nil){
        dictionary[@"name"] = self.name;
    }
    if(self.steps != nil){
        NSMutableArray * dictionaryElements = [NSMutableArray array];
        for(CCIStep * stepsElement in self.steps){
            [dictionaryElements addObject:[stepsElement toDictionary]];
        }
        dictionary[@"steps"] = dictionaryElements;
    }
    if(self.tags.count > 0 ){
        
        dictionary[@"parsedTags"] = self.tags;
    }
    if(self.type != nil){
        dictionary[@"type"] = self.type;
    }
    
    
    
    dictionary[@"success"] = @(self.success);
    if(self.failureReason.length > 0){
        dictionary[@"failureReason"] = self.failureReason;
    }
    
    
    return dictionary;
    
}

/**
 * Implementation of NSCoding encoding method
 */
/**
 * Returns all the available property values in the form of NSDictionary object where the key is the approperiate json key and the value is the value of the corresponding property
 */
- (void)encodeWithCoder:(NSCoder *)aCoder
{
    if(self.examples != nil){
        [aCoder encodeObject:self.examples forKey:@"examples"];
    }
    if(self.keyword != nil){
        [aCoder encodeObject:self.keyword forKey:@"keyword"];
    }
    if(self.location != nil){
        [aCoder encodeObject:self.location forKey:@"location"];
    }
    if(self.name != nil){
        [aCoder encodeObject:self.name forKey:@"name"];
    }
    if(self.steps != nil){
        [aCoder encodeObject:self.steps forKey:@"steps"];
    }
    if(self.tags != nil){
        [aCoder encodeObject:self.tags forKey:@"tags"];
    }
    if(self.type != nil){
        [aCoder encodeObject:self.type forKey:@"type"];
    }
    [aCoder encodeObject:@(self.success) forKey:@"success"];
    if(self.failureReason.length > 0){
        [aCoder encodeObject:self.failureReason forKey:@"failureReason"];
    }
    
}

/**
 * Implementation of NSCoding initWithCoder: method
 */
- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super init];
    self.examples = [aDecoder decodeObjectForKey:@"examples"];
    self.keyword = [aDecoder decodeObjectForKey:@"keyword"];
    self.location = [aDecoder decodeObjectForKey:@"location"];
    self.name = [aDecoder decodeObjectForKey:@"name"];
    self.steps = [aDecoder decodeObjectForKey:@"steps"];
    self.tags = [aDecoder decodeObjectForKey:@"tags"];
    self.type = [aDecoder decodeObjectForKey:@"type"];
    self.success = [[aDecoder decodeObjectForKey:@"success"] boolValue];
    self.failureReason = [aDecoder decodeObjectForKey:@"failureReason"];
    
    return self;
}

#pragma mark - NSCopying
- (instancetype)copyWithZone:(NSZone *)zone
{
    CCIScenarioDefinition * copy = [[CCIScenarioDefinition alloc] initWithDictionary:[self toDictionary]];
    return copy;
}
@end
