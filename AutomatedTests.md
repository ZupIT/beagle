# Running automated tests on each platform

## Backend

Here we provide two options for running the backend project, by using the IntelliJ IDE or running using the gradle wrapper
	
**IntelliJ:**
	
* 	Right click on the file br/com/zup/beagle/automatedtests/AutomatedTestsApplication.kt then click on Run option
	
**Gradle wrapper:**
	
* 	Go to the /backend folder
	Run the command: ./gradlew automated-tests:bootRun


## Android
	
Here we provide two options for running the android project, by using the Android Studio IDE or running using the gradle wrapper
	
**Android Studio:**

* 		Go to Run -> Edit Configurations and add a new configuration of "Android Instrumented Tests"
* 		select the "automated-tests" module and click OK
* 	 	Run the create configuration
		
<img src="https://i.ibb.co/3fpnNys/Captura-de-Tela-2020-09-28-a-s-14-10-31.png" height="256" />

<img src="https://i.ibb.co/HYMRG6Z/Captura-de-Tela-2020-09-28-a-s-14-10-44.png" height="256" />
			
**Gradle wrapper:**

* Go to /android/automated-tests folder
* Start the emulator
* Run the command: ./../gradlew connectedAndroidTest
	
**Selecting tests to run:**
	On file com/example/automatedTests/cucumber/steps/Instrumentation.kt add the tests tags that you wants to run on the tags field of the CucumberOptions annotation
	
<img src="https://i.ibb.co/17dn6Nw/Captura-de-Tela-2020-09-28-a-s-14-12-42.png" height="256" />

## Next steps

* Document default emulators to run the tests
* Share .feature files across platforms
