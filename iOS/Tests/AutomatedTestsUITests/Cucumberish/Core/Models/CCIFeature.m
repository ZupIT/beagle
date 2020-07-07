//
//	CCIFeature.m
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
// THE SOFTWARE.	Model file Generated using JSONExport: https://github.com/Ahmed-Ali/JSONExport



#import "CCIFeature.h"

@interface CCIFeature ()
@property (nonatomic, strong) NSArray<NSDictionary *>* rawTags;
@end
@implementation CCIFeature




/**
 * Instantiate the instance using the passed dictionary values to set the properties values
 */

-(instancetype)initWithDictionary:(NSDictionary *)dictionary
{
    self = [super init];
    
    
    if(dictionary[@"location"] != nil && ![dictionary[@"location"] isKindOfClass:[NSNull class]]){
        self.location = [[CCILocation alloc] initWithDictionary:dictionary[@"location"]];
    }
    
    if(dictionary[@"name"] != nil && ![dictionary[@"name"] isKindOfClass:[NSNull class]]){
        self.name = dictionary[@"name"];
    }
    if(dictionary[@"description"] != nil && ![dictionary[@"description"] isKindOfClass:[NSNull class]]){
        self.docDescription = dictionary[@"description"];
    }
    if(dictionary[@"parsedTags"] != nil){
        self.tags = dictionary[@"parsedTags"];
    }else if(dictionary[@"tags"] != nil && [dictionary[@"tags"] isKindOfClass:[NSArray class]]){
        NSArray * tagsDictionaries = dictionary[@"tags"];
        [self setRawTags:dictionary[@"tags"]];
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
	if(dictionary[@"children"] != nil && [dictionary[@"children"] isKindOfClass:[NSArray class]]){
		NSArray * scenarioDefinitionsDictionaries = dictionary[@"children"];
		NSMutableArray * scenarioDefinitionsItems = [NSMutableArray array];
		for(NSDictionary * scenarioDefinitionsDictionary in scenarioDefinitionsDictionaries){
            NSMutableDictionary * scenarioData = [scenarioDefinitionsDictionary mutableCopy];
            if(self.location.filePath.length > 0){
                scenarioData[@"location"][@"filePath"] = self.location.filePath;
            }
            if([[scenarioData[@"keyword"] lowercaseString] isEqualToString:@"background"]){
                self.background = [[CCIBackground alloc] initWithDictionary:scenarioData];
            }
			CCIScenarioDefinition * scenarioDefinitionsItem = [[CCIScenarioDefinition alloc] initWithDictionary:scenarioData];
            if(scenarioDefinitionsItem.tags.count > 0 && self.tags.count > 0){
                NSMutableArray * allTags = [scenarioDefinitionsItem.tags mutableCopy];
                [allTags addObjectsFromArray:self.tags];
                scenarioDefinitionsItem.tags = allTags;
            }else if(self.tags.count > 0){
                scenarioDefinitionsItem.tags = self.tags;
            }
			[scenarioDefinitionsItems addObject:scenarioDefinitionsItem];
		}
		self.scenarioDefinitions = scenarioDefinitionsItems;
	}
    
    

	return self;
}


/**
 * Returns all the available property values in the form of NSDictionary object where the key is the approperiate json key and the value is the value of the corresponding property
 */
-(NSDictionary *)toDictionary
{
	NSMutableDictionary * dictionary = [NSMutableDictionary dictionary];
	if(self.background != nil){
		dictionary[@"background"] = [self.background toDictionary];
	}

	if(self.location != nil){
		dictionary[@"location"] = [self.location toDictionary];
	}
	if(self.name != nil){
		dictionary[@"name"] = self.name;
	}
	if(self.scenarioDefinitions != nil){
		NSMutableArray * dictionaryElements = [NSMutableArray array];
		for(CCIScenarioDefinition * scenarioDefinitionsElement in self.scenarioDefinitions){
			[dictionaryElements addObject:[scenarioDefinitionsElement toDictionary]];
		}
		dictionary[@"scenarioDefinitions"] = dictionaryElements;
	}
    if(self.tags.count > 0){
        dictionary[@"parsedTags"] = self.tags;
    }
    if (self.docDescription != nil)
    {
        dictionary[@"description"]= self.docDescription;
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
	if(self.background != nil){
		[aCoder encodeObject:self.background forKey:@"background"];
	}
	
	if(self.location != nil){
		[aCoder encodeObject:self.location forKey:@"location"];
	}
	if(self.name != nil){
		[aCoder encodeObject:self.name forKey:@"name"];
	}
	if(self.scenarioDefinitions != nil){
		[aCoder encodeObject:self.scenarioDefinitions forKey:@"scenarioDefinitions"];
	}
	if(self.tags != nil){
		[aCoder encodeObject:self.tags forKey:@"tags"];
	}
	

}

/**
 * Implementation of NSCoding initWithCoder: method
 */
- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
	self = [super init];
	self.background = [aDecoder decodeObjectForKey:@"background"];
	self.location = [aDecoder decodeObjectForKey:@"location"];
	self.name = [aDecoder decodeObjectForKey:@"name"];
	self.scenarioDefinitions = [aDecoder decodeObjectForKey:@"scenarioDefinitions"];
	self.tags = [aDecoder decodeObjectForKey:@"tags"];
	return self;

}

@end