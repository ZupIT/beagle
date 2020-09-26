# Running automated tests on each platform

#backend
Here we provide two options for running the backend project, by using the IntelliJ IDE or running using the gradle wrapper
	
IntelliJ:
	Right click on the file br/com/zup/beagle/automatedtests/AutomatedTestsApplication.kt then click on Run option
	
Gradle wrapper:
	Go to the /backend folder
	Run the command: ./gradlew automated-tests:bootRun


#android
	
Here we provide two options for running the android project, by using the Android Studio IDE or running using the gradle wrapper
	
Android Studio:
		Go to Run -> Edit Configurations and add a new configuration of "Android Instrumented Tests"
		select the "automated-tests" module and click OK		Then run the create configuration
			
Gradle wrapper:
		Go to /android/automated-tests folder
		Start the emulator
		Run the command: ./../gradlew connectedAndroidTest
	
Selecting tests to run:
	
	
#iOS
Select the AutomatedTests scheme and run the Test action (command + U)

Selecting tests to run:

#Next steps
-Document default emulators to run the tests

-Share .feature files across platforms

