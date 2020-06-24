![Release](https://github.com/ZupIT/beagle/workflows/Beagle%20Release/badge.svg)
[![codecov](https://codecov.io/gh/ZupIT/beagle/branch/master/graph/badge.svg?token=rViMmc9MYJ)](https://codecov.io/gh/ZupIT/beagle)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/ZupIT/beagle/blob/master/LICENSE.txt)

## What is Beagle?

Beagle is an **open source framework** for **cross-platform** development using the concept of Server-Driven UI.

This framework allow teams to build and alter layouts and data directly through a backend but yet displaying its contents natively in a mobile application and / or through web.

It is also possible to create, test and update native application components and screen paths without the need update the mobile application at the store (App Store or Play Store).

#
### Goals for Beagle

-   Automatic app updates without having to rely on App Store or Play Store slow processes

-   Easily accommodate changes

-   Minimize code duplications

-   Easier maintainability and testability

-   To be flexible for UI Designers and Developers

#
#### How it works

<img src=https://usebeagle.io/static/fdc419fbc7141bee9f3e734bc3a3fcc0/f3583/mobile-illustration.png>


#### Server-Driven UI
<img src=https://usebeagle.io/static/34afd90e97b970f68d6445e2eca28a89/0fba7/change-illustration.png>

**Server-Driven UI** is an implementation paradigm that defines an API to tell the client what components to render and with what content. This can be implemented in all three major platforms: Android, iOS, and the web.

All this information comes from a server called **BFF** or **Back-end For Front-end** through `JSON` objects. In the Server Driven paradigm the structure and flow between screens is incorporated into the `JSON` objects trafficked with content, which could be divided into 3 basic pillars: **content, visual structure and flow (actions)**.

Check a **Beagle** example on the code block below:

```javascript
{
    "beagleType": "beagle:widget:pageview",
    "pages": [
        {
            "beagleType": "beagle:widget:flexsinglewidget",
            "flex": {
                "justifyContent": "CENTER",
                "alignItems": "CENTER"
            },
            "child": {
                "beagleType": "beagle:widget:text",
                "text": "Page 1"
            }
        },
        {
            "beagleType": "beagle:widget:flexsinglewidget",
            "flex": {
                "justifyContent": "CENTER",
                "alignItems": "CENTER"
            },
            "child": {
                "beagleType": "beagle:widget:text",
                "text": "Page 2"
            }
        },
        {
            "beagleType": "beagle:widget:flexsinglewidget",
            "flex": {
                "justifyContent": "CENTER",
                "alignItems": "CENTER"
            },
            "child": {
                "beagleType": "beagle:widget:text",
                "text": "Page 3"
            }
        }
    ],
    "pageIndicator": {
        "beagleType": "beagle:widget:pageindicator",
        "selectedColor": "#FFFFFF",
        "unselectedColor": "#888888"
    }
}
```
When recieving this information the front-end will set the screen visual structure and how screen pages connects and acts on one another natively according to the front-end system. As the application sends all actions to the back end, it responds systematically to each action, which configures our Single Source of Truth.

In this model the new features and combinations of UI components can be tested without releases and updates, which practically summarizes an A / B type test.

Different types of styles and layouts can be tested in certain customer's`circles` , which implies several testing possibilities and Analytics data.

#

#### Backend For Frontend

This is a server that provides the `JSON` objects that will be rendered at the frontend as `views`. With in these components, screens and business rules are written only once and thus rendered natively on each platform using Beagle.
<br>

<img src="https://usebeagle.io/static/d01b8b04150a9f0fd80cd53c29fa4e3c/719de/framework-ilustration.png">
#

#### Declarative Views

It is the paradigm by which the layouts are declared in the back-end, in a simplified way and with a focus on "what" the layout should do, leaving the "how it should do" to Beagle. Layouts are declared using widgets on BFF in Kotlin language.

```javascript
{
    "beagleType": "beagle:widget:pageview",
    "pages": [
        {
            "beagleType": "beagle:widget:flexsinglewidget",
            "flex": {
                "justifyContent": "CENTER",
                "alignItems": "CENTER"
            },
            "child": {
                "beagleType": "beagle:widget:text",
                "text": "Page 1"
            }
        },
        {
            "beagleType": "beagle:widget:flexsinglewidget",
            "flex": {
                "justifyContent": "CENTER",
                "alignItems": "CENTER"
            },
            "child": {
                "beagleType": "beagle:widget:text",
                "text": "Page 2"
            }
        }
    ],
    "pageIndicator": {
        "beagleType": "beagle:widget:pageindicator",
        "selectedColor": "#FFFFFF",
        "unselectedColor": "#888888"
    }
}
```
#

#### Serializer

The `Beagle serializer` responsibility is converting a declarative layout into a `JSON` object. In order to serialize Kotlin classes into `JSON` objects Beagle will use the [Jackson converter](https://github.com/FasterXML/jackson)_._

To serialize Kotlin classes in JSON objects, Beagle uses _Jackson_.

#

#### Deserializer

The `Beagle deserializer` responsibility is to convert a `JSON` object into a `widget` using [Moshi](https://github.com/square/moshi)_._

On Android _Moshi_ is used for this purpose.

<img src="https://usebeagle.io/static/906d90457bb9641b21d5b8f59da9f555/40a76/web-illustration.png">

Beagle WEB ----------------------------------- Beagle BFF

#

#### Design System

With a Design System you will be able to set styles for your components and views.

It is the design system that registers styles created on the application frontend connecting them to the Server driven components received from the BFF. That is how the application will "know" what style to apply on each component.

The Design system is an interface that defines a set of methods that must be implemented to define the **application's theme** amoung other components. For example, an `appDesignSystem` will keep button styles, text displays, toolbar styles, images and themes. For Beagle to work according to the application design system, it is necessary to have all of these styles implemented. They will be used the moment the visualization is rendered.

![](https://blobs.gitbook.com/assets%2F-M-Qy7jZbUpzGRP5GbCZ%2F-M-Vb8Yg30urdiTYjfB3%2F-M-VkXqAaAU8k7KAUaLW%2Fimage22.png?alt=media&token=62e91f51-a030-499d-91ca-f5131fd88790)

#

#### Layout Engine

When rendering a screen the front-end receives a `JSON` object that provides all necessary information through **Beagle** to position the elements on the screen

When deserializing, Beagle turns a `widget` into a `view` applying the styles implemented in the `DesignSystem`. At this point, Beagle uses the [**YOGA**](https://yogalayout.com/) rendering engine internally to set the layout's view position and finally render it natively.

![](https://blobs.gitbook.com/assets%2F-M-Qy7jZbUpzGRP5GbCZ%2F-M-WAWIJGEgjFTyn57to%2F-M-WE7CKoTKbtxvMCMLt%2Fyoga.gif?alt=media&token=070dba81-176f-451f-b82e-cf3589c6e2f8)

#### Learn how to:

[Create a Backend project](https://docs.usebeagle.io/v/v1.0-en/get-started/installing-beagle/beagle-backend)
[Create an Android project](https://docs.usebeagle.io/v/v1.0-en/get-started/installing-beagle/android)
[Create an iOS project](https://docs.usebeagle.io/v/v1.0-en/get-started/installing-beagle/ios)
[Create an WEB project](https://docs.usebeagle.io/v/v1.0-en/get-started/installing-beagle/web)

<img src = "https://usebeagle.io/static/495f34d913f71babdebf6d6e08bb21dc/0fba7/migre-illustration.png">

## Contributing guide

Please refer to [CONTRIBUTING.md](https://github.com/ZupIT/beagle/blob/master/CONTRIBUTING.md) for details on how to add new feature and contribute in general to Beagle
