
[![codecov](https://codecov.io/gh/ZupIT/beagle/branch/master/graph/badge.svg?token=rViMmc9MYJ)](https://codecov.io/gh/ZupIT/beagle)

Beagle is a cross-platform framework that allows to apply Server-Driven UI natively in iOS, Android and Web.Then, the layouts can be created in a Backend for Frontend middleware.

## Getting started

### Setting up the Android dependencies
Getting started with Beagle for Android
#### gradle (gradle 5.4.1)

The **first step** is to set the Zup's repository (using the **credentials** bellow) to download Beagle's Library.
```javascript
// Add it in your root build.gradle at the end of repositories:
allprojects {

    repositories {
        google()
        jcenter()
        maven {
            url 'https://dl.bintray.com/zupit/repo'
        }
    }
}
```
The **second step** is to include kapt plugin and Beagle as a dependency into your dependency manager, for example, as a Gradle dependency:

-   Always check the current `beagle_version` by hovering the mouse over the version number. After that hit sync now

```javascript
// Add in your plugins
apply plugin: 'kotlin-kapt'

// Add in your app level dependency
ext.beagle_version = "0.1.3"
 
dependencies {
    implementation "br.com.zup.beagle:android:$beagle_version"
    kapt "br.com.zup.beagle:android-processor:$beagle_version"
}
```
The **third step** is to create a`AppBeagleConfig`  class, annotate this class with the `@BeagleComponent` , **extend** the`AppBeagleConfig` with the `BeagleConfig` class and provide the attributes below:
```javascript
@BeagleComponent
class AppBeagleConfig : BeagleConfig {
    override val baseUrl: String get() = "https://myapp.server.com/" // return the base url based on your environment
    override val environment: Environment get() = Environment.DEBUG // return the current build state of your app
    
}
```

> :warning: **It's very important to notice that Beagle expects classes that are annotated with `@BeagleComponent` to have empty constructors.**

The **fourth step** is to create a `AppBeagleActivity` . You will need one to handle the server driven activities. This file is part of Beagle's configuration and it must be implemented at least ounce for Beagle to work correctly. This activity **must be** annotated with `@BeagleComponent`and **extends** the `BeagleActivity` class. Check the example bellow:

```javascript
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import br.com.zup.beagle.annotation.BeagleComponent
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_app_beagle.*

@BeagleComponent
class AppBeagleActivity : BeagleActivity() {

    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val mToolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.custom_toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_beagle)
    }

    override fun getServerDrivenContainerId(): Int = R.id.server_driven_container

    override fun getToolbar(): Toolbar = custom_toolbar

    override fun onServerDrivenContainerStateChanged(state: ServerDrivenState) {
        if (state is ServerDrivenState.Loading) {
            progressBar.visibility = if (state.loading) View.VISIBLE else View.GONE
        } else if (state is ServerDrivenState.Error) {
            Snackbar.make(window.decorView, "Error", Snackbar.LENGTH_LONG).show()
        }
    }
}
```
...and the .xml would be:
```javascript
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/root_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/custom_toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <FrameLayout
            android:id="@+id/server_driven_container"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

        <ProgressBar
            android:id="@+id/progress_bar"
            android:layout_width="42dp"
            android:layout_height="42dp"
            android:layout_gravity="center"
            android:visibility="gone"/>

    </FrameLayout>

</LinearLayout>
```
  The **fifth step** is to initialize Beagle in your `Application` class. But before you do that you must make sure that your **minimum SDK Version** is no smaller than 19.

```javascript
build.gradle(Module:AppName)

defaultConfig {

minSdkVersion 19

}
```
Now you just need to build your app once and that will trigger Beagle into creating the `BeagleSetup` class (you don't have to create it, it is automatically created by Beagle) as show on the picture bellow

![](https://blobs.gitbook.com/assets%2F-M-Qy7jZbUpzGRP5GbCZ%2F-M16ZVNyVuL1WSXTL632%2F-M177wK5SHtasuAhRAS5%2Fimage.png?alt=media&token=3aac97ca-e527-4887-871a-8b3122788ef8)

After that you must **create** a `KOTLIN` class that **extends**  `Application`. The `AppApplication` class must call the `BeagleSetup().init(this)` at the `onCreate` method as listed below and you will be almost good to go. There is just one more step.

```javascript
class AppAplication:Application() {

    override fun onCreate() {
        super.onCreate()
        BeagleSetup().init(this)
    }
}
```
The **sixth and last step** is to authorize Beagle to use the local resources in your Android App. To do this, you must **create** a `KOTLIN`  `AppDesignSystem` class that **extends** the `DesignSystem` class and annotates it as a `@BeagleComponent`. In order to access the styles in your app declaratively you must assign them on the methods as listed bellow

```javascript
// This class is used by the RenderEngine in order to define the styles configured at the application.
// It must extend DesignSystem.
// These are the styles the AppDesignSystem implements: buttonStyle, toolbarStyle, textAppearance, theme and image

@BeagleComponent
class AppDesignSystem : DesignSystem {

    override fun toolbarStyle(name: String) = R.style.Toolbar

    override fun buttonStyle(name: String) = R.style.ButtonBlack

    override fun image(name: String): Int {
        return when (name) {
            "delete" -> android.R.drawable.ic_delete
            else -> android.R.drawable.ic_menu_help
        }
    }

    override fun theme(): Int {
        return R.style.AppTheme
    }

    override fun textAppearance(name: String): Int {
        return when (name) {
            "TextBlackBold" -> R.style.TextBlackBold
            else -> R.style.TextBlack
        }
    }
}
```
> :warning: **When the `DesignSystem` class is extended it will require that certain methods are implemented. It is mandatory to implement all these for Beagle to work properly.**
>
Check the res/values/styles example bellow:
```javascript
<resources>
    <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>

    <style name="TextBlackBold" parent="TextAppearance.AppCompat">
        <item name="android:textColor">#000000</item>
        <item name="android:textSize">20sp</item>
        <item name="android:textStyle">bold</item>
    </style>

    <style name="TextBlack" parent="TextAppearance.AppCompat">
        <item name="android:textColor">#000000</item>
        <item name="android:textSize">20sp</item>
    </style>

    <style name="ButtonBlack" parent="Widget.AppCompat.Button">
        <item name="android:textColor">#000000</item>
        <item name="android:textSize">18sp</item>
        <item name="android:textAllCaps">false</item>
    </style>

    <style name="Toolbar" parent="Widget.AppCompat.Toolbar">
        <item name="android:background">@color/colorPrimary</item>
        <item name="navigationIcon">?attr/homeAsUpIndicator</item>
    </style>

</resources>
```
And now **you are good to go!** Please read the details below since it will address some important information that you might need later when using Beagle.

**a)** `@BeagleComponent` responsibility is to register the following classes: `CustomActionHandler`, `DeepLinkHandler`, `HttpClient`, `DesignSystem` , `BeagleConfig`

**b)** `@RegisterWidget` is responsible to register all `widgets` at the application

**c)** `@RegisterValidator` is responsible to register all `Validators` at the `application`

**d)** Classes annotated with `@BeagleComponent` and `@RegisterValidator` cannot implement parameters on its constructors. Beagle expects constructors at these classes to be empty.
>
## How it works
The BFF will mediate the APIs that will be consumed and use Beagle to create layouts declaratively, serialize to JSON and send it to the Frontend.  
In the Frontend, Beagle will deserialize this JSON into a widget and it will be rendered using the design system to a View.

<img src="./assets/BFF.png"/>

## Resources

* [FAQ](https://docs.google.com/spreadsheets/d/1S3Xnwsnc9GnN6R6wSpSfPsFeoaovrRQ9hbl6CZ6ONZE/edit#gid=0)

* [Detailed Guide](https://zup-products.gitbook.io/beagle/)

* [Frontend example](https://github.com/ZupIT/beagle-tmdb)

* [BFF example](https://github.com/ZupIT/beagle-tmdb-backend)

## Testing
We are using the plugin [Shot](https://github.com/Karumi/Shot/) to run snapshots tests.

## Local Development
Here are some useful Gradle/adb commands for executing this example:

 * `./gradlew assembleDebug` - Builds the debug apk.
 * `./gradlew detekt` - Execute the checkstyle task to verify code style.
 * `./gradlew connectedDebugAndroidTest` - Executes the instrumented tests on connected device.
 * `./gradlew jacocoTestReport` - Executes coverage report that can be found on build folder.
 * `./gradlew executeScreenshotTests -Precord` - Executes snapshots recording(must be made using the emulator Nexus5,21,en,portrait) at folder  `"${projectDir}/screenshots/`
 * `./gradlew executeScreenshotTests` - Verifies if the current screens against the previous snapshots at folder  `"${projectDir}/screenshots/` using the current connected device
 * `./gradlew executeScreenshotTests -PrunInstrumentation=false` - Verifies if the current screens against the previous snapshots at folder  `"${projectDir}/screenshots/` without using the current connected device.  
 In that case the current screenshots(that may be generated on a remote device) must be copied to the folder `"${projectDir}/screenshots/screenshots-default`
