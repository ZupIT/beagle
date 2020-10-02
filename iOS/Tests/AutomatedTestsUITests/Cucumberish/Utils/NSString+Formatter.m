//
//  NSString+Formatter.m

//  Created by Ahmed Ali on 11/01/16.
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

#import "NSString+Formatter.h"

@implementation NSString (Formatter)
- (NSString *)camleCaseStringWithFirstUppercaseCharacter:(BOOL)firstUppercaseCharacter
{
    static NSString * sep = @"AASSAAAKKKAALLLAL";
    NSString * str = [[self stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] stringByReplacingOccurrencesOfString:@" " withString:sep];
    str = [str stringByReplacingCharactersInSet:[[NSCharacterSet alphanumericCharacterSet] invertedSet] withString:@""];
    str = [str stringByReplacingOccurrencesOfString:sep withString:@"_"];
    NSMutableString * output = [NSMutableString string];
    BOOL makeNextCharacterInUpperCase = firstUppercaseCharacter;
    for (int i = 0; i < str.length; i++) {
        
        NSString * substr = [str substringWithRange:NSMakeRange(i, 1)];
        if(i == 0 && !makeNextCharacterInUpperCase){
            substr = [substr lowercaseString];
        }
        if([substr isEqualToString:@"_"]){
            makeNextCharacterInUpperCase = YES;
            continue;
        }else if(makeNextCharacterInUpperCase){
            substr = [substr uppercaseString];
            
        }
        [output appendString:substr];
        makeNextCharacterInUpperCase = NO;
    }
    
    return output;
}

- (NSString *)stringByReplacingCharactersInSet:(NSCharacterSet *)charSet withString:(NSString *)aString {
    NSMutableString *s = [NSMutableString stringWithCapacity:self.length];
    for (NSUInteger i = 0; i < self.length; ++i) {
        unichar c = [self characterAtIndex:i];
        if (![charSet characterIsMember:c]) {
            [s appendFormat:@"%C", c];
        } else {
            [s appendString:aString];
        }
    }
    return s;
}

@end
