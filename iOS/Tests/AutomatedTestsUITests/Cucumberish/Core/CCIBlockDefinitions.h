//
//  CCIBlockDefinitions.h
//  Pods
//
//  Created by Ahmed Ali on 23/01/16.
//
//

#import <Foundation/Foundation.h>
@class CCIScenarioDefinition;

extern const NSString * kDataTableKey;
extern const NSString * kDocStringKey;
extern const NSString * kXCTestCaseKey;

#pragma mark - Block types
/**
 You pass this block when ever you define an implementation for a step.
 
 @param args strings array which corresponds to your regular express capturing groups.
 @param userInfo is a dictionary that currently can have one of two keys kDataTableKey or kDocStringKey. If your step definition is expected to match a data table or a doc string, then you can expect this user info dictionary to contain kDataTableKey or kDocStringKey key respectively. Moreover, it will contain the key kXCTestCaseKey which will hold reference to the XCTestCase that currently executing this step. The value of kXCTestCaseKey will be nil if the step is being executed by a call of "step" or "SStep" C functions and you did not pass a reference to the XCTestCase.
 */
typedef void(^CCIStepBody)(NSArray <NSString *>* args, NSDictionary * userInfo);

/**
 This block is passed to all the hocks (except around hock).
 @param scenario the scenario the current scenario
 */
typedef void (^CCIScenarioHockBlock)(CCIScenarioDefinition * scenario);

/**
 This block is passed to around hocks.
 @param scenario the scenario the current scenario.
 @param scenarioExectionBlock the block that will execute the scenario. If you did not call it, the scenario will never bee executed.
 */
typedef void (^CCIScenarioExecutionHockBlock)(CCIScenarioDefinition * scenario, void (^scenarioExectionBlock)(void) );


#pragma mark - Step definitions and prepositions

@class CCIStep;
@class CCIStepDefinition;

/**
 Defines a Given implementations.
 
 @code
 Given(@"the app is running" ,  ^void(NSArray *args, id userInfo) {
     //Your step implementation goes here
     //The definition string in this case will match only the following step
     //Given the app is running
 });
 @endcode
 
 @Note
 Step definitions are checked in a "Last In First Out" order. If it happens that there are more than one definition matches a step, the last registered definition will be used.
 
 @param definitionString the regular expression that will checked against each Given step line.
 @param body the code block that will be executed if match is occured.
 */
OBJC_EXTERN void Given(NSString * definitionString, CCIStepBody body);

/**
 Defines a When step implementations.
 
 @b Example:
 @code
 When(@"^I tap (?:the )?\"([^\\\"]*)\" (?:button|view|label)$" ,  ^void(NSArray *args, id userInfo) {
     //Step implementation goes here
     //The definition string in this case can match all the following steps
     //When I tap the "MyButton" button
     //When I tap "MyButton" button
     //When I tap "Header" view
     //When I tap "FAQ" label
 });
 @endcode
 
 @Note
 Step definitions are checked in a "Last In First Out" order. If it happens that there are more than one definition matches a step, the last registered definition will be used.
 
 @param definitionString the regular expression that will checked against each Given step line.
 @param body the code block that will be executed if match is occured.
 */
OBJC_EXTERN void When(NSString * definitionString, CCIStepBody body);

/**
 Defines a Then step implementations.
 
 @b Example:
 @code
 Then(@"^I should see \"([^\\\"]*)\" in (?:the )?\"([^\\\"]*)\" (?:button|view|label)$" ,  ^void(NSArray *args, id userInfo) {
     //Step implementation goes here
     //The definition string in this case can match all the following steps
     //Then I should see "Button Title" in "MyButton" button
     //Then I should see "Button Title" in the "MyButton" Button
     //Then I should see "The Screen Title" in "Header" view
     //Then I should see "FAQ" in "FAQ" label
 });
 @endcode
 
 @Note
 Step definitions are checked in a "Last In First Out" order. If it happens that there are more than one definition matches a step, the last registered definition will be used.
 
 @param definitionString the regular expression that will checked against each Given step line.
 @param body the code block that will be executed if match is occured.
 */
OBJC_EXTERN void Then(NSString * definitionString, CCIStepBody body);

/**
 Defines an And step implementations.
 
 @b Example:
 @code
 And(@"^I should see \"([^\\\"]*)\" in (?:the )?\"([^\\\"]*)\" (?:button|view|label)$" ,  ^void(NSArray *args, id userInfo) {
     //Step implementation goes here
     //The definition string in this case can match all the following steps
     //And I should see "Button Title" in "MyButton" button
     //And I should see "Button Title" in the "MyButton" Button
     //And I should see "The Screen Title" in "Header" view
     //And I should see "FAQ" in "FAQ" label
 });
 @endcode
 
 @Note
 Step definitions are checked in a "Last In First Out" order. If it happens that there are more than one definition matches a step, the last registered definition will be used.

 @param definitionString the regular expression that will checked against each Given step line.
 @param body the code block that will be executed if match is occured.
 */
__attribute__((deprecated("And is deprecated. Implement Given, When, Then or But instead")))
OBJC_EXTERN void And(NSString * definitionString, CCIStepBody body);

/**
 Defines a But step implementations.
 
 @code
 But(@"^I should see \"([^\\\"]*)\" in (?:the )?\"([^\\\"]*)\" (?:button|view|label)$" ,  ^void(NSArray *args, id userInfo) {
     //Step implementation goes here
     //The definition string in this case can match all the following steps
     //But I should see "Button Title" in "MyButton" button
     //But I should see "Button Title" in the "MyButton" Button
     //But I should see "The Screen Title" in "Header" view
     //But I should see "FAQ" in "FAQ" label
 });
 @endcode
 
 @Note
 Step definitions are checked in a "Last In First Out" order. If it happens that there are more than one definition matches a step, the last registered definition will be used.
 
 @param definitionString the regular expression that will checked against each Given step line.
 @param body the code block that will be executed if match is occured.
 */
OBJC_EXTERN void But(NSString * definitionString, CCIStepBody body);

/**
 Defines a step implementations that will be registered with When, Then, And, and But.
 The implementation of this function simply calls:
 @code
 When(definitionString, body);
 Then(definitionString, body);
 And(definitionString, body);
 But(definitionString, body);
 @endcode
 Which concluds that the registered definitions will be checked with any of these four prepositions.
 
 @b Example:
 @code
 MatchAll(@"^I should see \"([^\\\"]*)\" in (?:the )?\"([^\\\"]*)\" (?:button|view|label)$" ,  ^void(NSArray *args, id userInfo) {
     //Step implementation goes here
     //The definition string in this case can match all the following steps
     //When I should see "Button Title" in "MyButton" button
     //Then I should see "Button Title" in "MyButton" button
     //And I should see "Button Title" in "MyButton" button
     //But I should see "Button Title" in "MyButton" button
 });
 @endcode
 
 @Note
 Step definitions are checked in a "Last In First Out" order. If it happens that there are more than one definition matches a step, the last registered definition will be used.
 
 @param definitionString the regular expression that will checked against each Given step line.
 @param body the code block that will be executed if match is occured.
 */
OBJC_EXTERN void MatchAll(NSString * definitionString, CCIStepBody body);

/**
 Defines a step implementations that will be registered with specified prepositions.
 
 @b Example:
 @code
 Match(@[@"When", @"And", @"Then"], @"^I should see \"([^\\\"]*)\" in (?:the )?\"([^\\\"]*)\" (?:button|view|label)$" ,  ^void(NSArray *args, id userInfo) {
     //Step implementation goes here
     //The definition string in this case can match all the following steps
     //When I should see "Button Title" in "MyButton" button
     //And I should see "Button Title" in "MyButton" button
     //Then I should see "Button Title" in "MyButton" button
 });
 @endcode
 
 @Note
 Step definitions are checked in a "Last In First Out" order. If it happens that there are more than one definition matches a step, the last registered definition will be used.
 
 @param prepositions array of strings to be used as the preposition of the step definiton
 @param definitionString the regular expression that will checked against each Given step line.
 @param body the code block that will be executed if match is occured.
 */
OBJC_EXTERN void Match(NSArray *prepositions, NSString * definitionString, CCIStepBody body);

/**
 Step implementation can be also a useable code.
 If it the case that you want to call a previously defined step implementation, you can call this definition using this special step function.
 
 @Note
 Using this function, you do not need to worry about the preposition of the step; you just pass in the step line without defining the preposition.
 
 @b Example:
 @code
     //The first step definiton
 Match(@[@"When", @"And", @"Then"], @"^I should see \"([^\\\"]*)\" in (?:the )?\"([^\\\"]*)\" (?:button|view|label)$" ,  ^void(NSArray *args, id userInfo) {
     //Step implementation goes here
 });
 
     //The another step definiton that will make use of the previousely defined step
 When(@"^I write \"([^\\\"]*)\" (?:in|into) \"([^\\\"]*)\" field$" ,  ^void(NSArray *args, id userInfo) {
     //Step implementation goes here
     //Here you can call the previously defined step.
 step(@"I should see \"%@\" in the \"%@\" label", args[0], args[1]);
 });
 @endcode

 @param testCase the test case that will execute this step. Can be nil.
 @param stepLine the step line string to be executed
 */
OBJC_EXPORT void step(id testCase, NSString * stepLine, ...);

/**
 Swift alias for step(stepLine) function.
 @param testCase the test case that will execute this step. Can be nil.
 @param stepLine the step line string to be executed
 */
OBJC_EXTERN void SStep(id testCase, NSString * stepLine);


#pragma mark - Hocks
/**
 C function that registers a code block to be called only once before executing any test case.
 You can call this function as much as you want, but only the last registerd code block will be used.
 
 @Note
 This function should not be called after calling @a beginExecution.
 
 @param beforeStartBlock code block that will be executed once before any test cases.
 */
OBJC_EXTERN void beforeStart(void(^beforeStartBlock)(void));


/**
 C function that registers a code block to be called only once after executing all test cases.
 You can call this function as much as you want, but only the last registerd code block will be used.
 
 @Note
 This function should not be called after calling @a beginExecution.
 
 @param afterFinishBlock code block that will be executed once after all test cases finish execution.
 */
OBJC_EXTERN void afterFinish(void(^afterFinishBlock)(void));


/**
 C function that registers a code block to be called before each scenario.
 Code blocks registerd with this function will run before each scenario in the same order they were orignally registered; that's it, FIFO (First In First Out).
 
 @Note
 This function should not be called after calling @a beginExecution.
 
 @param beforeEachBlock code block that will be executed before executing the scenario, this block receives an instance of the scenario that will be executed.
 */
OBJC_EXTERN void before(CCIScenarioHockBlock beforeEachBlock);


/**
 C function that registers a code block to be called after each scenario.
 Code blocks registerd with this function will run after each scenario in reversed order compared to the order they were orignally registered; that's it, LIFO (Last In First Out)
 
 @Note
 All code blocks registerd with this function, will run regardless the scenario has been passed or not
 
 @Note
 This function should not be called after calling @a beginExecution.
 
 @param afterEachBlock code block that will be executed after executing the scenario, this block receives an instance of the scenario that has been executed.
 */
OBJC_EXTERN void after(CCIScenarioHockBlock afterEachBlock);


/**
 C function that registers a code block to be called before each scenario that has one or more tag that matches one or more tags passed to this function.
 Code blocks registerd with this function will run before each matching scenario in the same order they were orignally registered; that's it, FIFO (First In First Out)
 
 @Note
 Do not prefix any tag you pass with @ symbol
 
 @Note
 This function should not be called after calling @a beginExecution.
 
 @param tags array of strings that will be used to match specific scenarios
 @param beforeTaggedBlock code block that will be executed before executing the scenario, this block receives an instance of the scenario that will be executed.
 */
OBJC_EXTERN void beforeTagged(NSArray * tags, CCIScenarioHockBlock beforeTaggedBlock);


/**
 C function that registers a code block to be called after each scenario that has one or more tag that matches one or more tags passed to this function.
 Code blocks registerd with this function will run after each matching scenario in reversed order compared to the order they were orignally registered; that's it, LIFO (Last In First Out)
 
 @Note
 This function should not be called after calling @a beginExecution.
 
 @Note
 All code blocks registerd with this function, will run regardless the matching scenario has been passed or not
 
 @Note
 Do not prefix any tag you pass with @ symbol
 
 @param tags array of strings that will be used to match specific scenarios
 @param afterTaggedBlock code block that will be executed after executing the scenario, this block receives an instance of the scenario that has been executed.
 */
OBJC_EXTERN void afterTagged(NSArray * tags, CCIScenarioHockBlock afterTaggedBlock);


/**
 C function that registers a code block to be used to call the scenario execution block.
 Code blocks registerd with this function will receive two parameters: scenario instance and scenario execution block as a parameter.
 
 @Note
 This function should not be called after calling @a beginExecution. 
 
 @param aroundScenarioBlock code block that will be executed for each scenario, this block receives an instance of the scenario and the scenario execution block.
 */
OBJC_EXTERN void around(CCIScenarioExecutionHockBlock aroundScenarioBlock);

/**
 C function that registers a code block to be used to call the scenario execution block.
 Code blocks registerd with this function will receive two parameters: scenario instance and scenario execution block as a parameter.

 If more than one code block matches the scenario, you are still required to call the scenario execution from each registered code block. However, your scenario will be executed once as it is supposed to be.

 Matching against around blocks happens in FIFO (First In First Out) order; in case more than one block has matched the same scenario, then they are nested.

 @Note
 Failing to call the scenario execution block, will prevent the scenario from being executed.

 @b Example of more than one match

 There are three registerd blocks with tags that matches the same scenario, the followin nesting calls will happen:

 @code
 Third Around Match
 Block = contains code block that executes the Second Around Match
 Second Around Match
 Block = contains code block that executes the First Around Match
 First Around Match
 Block = A call Scenario Exection Block@endcode


 @Note
 Do not prefix any tag you pass with @@ symbol

 @Note
 This function should not be called after calling @a beginExecution.


 @param tags array of strings that will be used to match specific scenarios
 @param aroundScenarioBlock code block that will be executed for each scenario, this block receives an instance of the scenario and the scenario execution block.
 */
OBJC_EXTERN void aroundTagged(NSArray * tags, CCIScenarioExecutionHockBlock aroundScenarioBlock);

/**
 Embed function, to embed data to your step that can be readen in a reporter.
 
 @param mimeType the mimeType used for your data encoding "image/png"
 @param dataString a image, json or other data object written to a String variable
 */
OBJC_EXTERN void CCIEmbed(NSString * mimeType, NSString * dataString);

#pragma mark - Assertion and Errors
/**
 Boolean assertion function. Use it where you usually use NSAssert or assert.
 
 Using this assertion function in your step implementations, guarantees proper error reporting
 
 @param expression boolean expression
 @param failureMessage formatted a string describe what went wrong in case expression is evaluated to false
 */
OBJC_EXTERN void CCIAssert(BOOL expression, NSString * failureMessage, ...);

/**
 Swift alias for CCIAssert
 @param expression boolean expression
 @param failureMessage a string describe what went wrong in case expression is evaluated to false
 */
OBJC_EXTERN void CCISAssert(BOOL expression, NSString * failureMessage);


/**
 Throws an exception with the specified reason.
 Cucumberish will handle this exception and show it as an issue with executing the current step
 
 @param reason the failure reason
 */

OBJC_EXTERN void throwCucumberishException(NSString *reason, ...);


/**
 Swift alias for throwCucumberishException;
 @param reason the failure reason
 */
OBJC_EXTERN void SThrowCucumberishException(NSString *reason);

