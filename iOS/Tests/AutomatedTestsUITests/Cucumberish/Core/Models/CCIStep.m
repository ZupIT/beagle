//
//	CCIStep.m
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



#import "CCIStep.h"


@interface CCIStep ()
@end
@implementation CCIStep




/**
 * Instantiate the instance using the passed dictionary values to set the properties values
 */

-(instancetype)initWithDictionary:(NSDictionary *)dictionary
{
	self = [super init];
	if(dictionary[@"keyword"] != nil && ![dictionary[@"keyword"] isKindOfClass:[NSNull class]]){
        self.keyword = [dictionary[@"keyword"] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
	}
    if(dictionary[@"location"] != nil && ![dictionary[@"location"] isKindOfClass:[NSNull class]]){
        self.location = [[CCILocation alloc] initWithDictionary:dictionary[@"location"]];
    }
    if(dictionary[@"match"] != nil && ![dictionary[@"match"] isKindOfClass:[NSNull class]]){
        self.match = dictionary[@"match"];
    }
	if(dictionary[@"text"] != nil && ![dictionary[@"text"] isKindOfClass:[NSNull class]]){
		self.text = dictionary[@"text"];
	}
    if(dictionary[@"argument"] != nil && ![dictionary[@"argument"] isKindOfClass:[NSNull class]]){
        self.argument = [[CCIArgument alloc] initWithDictionary:dictionary[@"argument"]];
    }
    if(dictionary[@"embeddings"] != nil && ![dictionary[@"embeddings"] isKindOfClass:[NSNull class]]){
        self.embeddings = [[NSMutableArray alloc] initWithArray:dictionary[@"embeddings"]];
    } else {
        self.embeddings = [[NSMutableArray alloc] init];
    }
    if(dictionary[@"duration"] != nil && ![dictionary[@"duration"] isKindOfClass:[NSNull class]]){
        self.duration = [dictionary[@"duration"] integerValue];
    }
	return self;
}


/**
 * Returns all the available property values in the form of NSDictionary object where the key is the approperiate json key and the value is the value of the corresponding property
 */
-(NSDictionary *)toDictionary
{
	NSMutableDictionary * dictionary = [NSMutableDictionary dictionary];
	if(self.keyword != nil){
		dictionary[@"keyword"] = self.keyword;
	}
    if(self.location != nil){
        dictionary[@"location"] = [self.location toDictionary];
    }
    if(self.match != nil){
        dictionary[@"match"] = self.match;
    }
    if(self.text != nil){
        dictionary[@"text"] = self.text;
    }
    if(self.argument != nil){
        dictionary[@"argument"] = [self.argument toDictionary];
    }
    if(self.embeddings != nil && self.embeddings.count > 0){
        dictionary[@"embeddings"] = self.embeddings;
    }
    dictionary[@"duration"] = @(self.duration);
	return dictionary;

}

- (NSString *)fullName
{
    return [NSString stringWithFormat:@"%@ %@", self.contextualKeyword ?: self.keyword, self.text];
}


- (NSString *)description
{
    return [NSString stringWithFormat:@"Step text: %@", self.text];
}

- (id)copyWithZone:(NSZone *)zone
{
    return [[CCIStep alloc] initWithDictionary:[self toDictionary]];
}
@end
