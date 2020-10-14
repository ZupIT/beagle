//
//	CCILocation.m
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



#import "CCILocation.h"

@interface CCILocation ()
@end
@implementation CCILocation




/**
 * Instantiate the instance using the passed dictionary values to set the properties values
 */

-(instancetype)initWithDictionary:(NSDictionary *)dictionary
{
	self = [super init];
	if(dictionary[@"filePath"] != nil && ![dictionary[@"filePath"] isKindOfClass:[NSNull class]]){
        _filePath = dictionary[@"filePath"];
	}

	if(dictionary[@"line"] != nil && ![dictionary[@"line"] isKindOfClass:[NSNull class]]){
		_line = [dictionary[@"line"] integerValue];
	}

	return self;
}


/**
 * Returns all the available property values in the form of NSDictionary object where the key is the approperiate json key and the value is the value of the corresponding property
 */
-(NSDictionary *)toDictionary
{
	NSMutableDictionary * dictionary = [NSMutableDictionary dictionary];
	dictionary[@"filePath"] = self.filePath;
	dictionary[@"line"] = @(self.line);
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
	[aCoder encodeObject:_filePath forKey:@"filePath"];
    [aCoder encodeObject:@(_line) forKey:@"line"];
}

/**
 * Implementation of NSCoding initWithCoder: method
 */
- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
	self = [super init];
    _filePath = [aDecoder decodeObjectForKey:@"filePath"];
    _line = [[aDecoder decodeObjectForKey:@"line"] integerValue];
	return self;
}

/**
 * Source file path for error reporting
 */
- (NSString *)filePath
{
    return [self strippedMacBundlePathPrefix:_filePath];
}

/**
 * Strip the /Contents/Resources/ path for Mac bundles
 */
- (NSString *)strippedMacBundlePathPrefix:(NSString *)path
{
    NSString *const macBundleContentsPrefix = @"/Contents/Resources";
    if ([path hasPrefix:macBundleContentsPrefix]) {
        return [path substringFromIndex:[macBundleContentsPrefix length]];
    } else {
        return path;
    }
}

@end
