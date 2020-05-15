![Release](https://github.com/ZupIT/beagle/workflows/Beagle%20Release/badge.svg)
[![codecov](https://codecov.io/gh/ZupIT/beagle/branch/master/graph/badge.svg?token=rViMmc9MYJ)](https://codecov.io/gh/ZupIT/beagle)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/ZupIT/beagle/blob/master/LICENSE.txt)

## What is Beagle?

Beagle is a **cross-platform framework** which facilitates usage of the **Server-Driven UI** concept, **natively** in iOS and Android. By using Beagle, your team could easily change application's layout and data by just changing backend code.
#
#### Goals for Beagle

-   Automatic app updates without having to rely on App Store or Play Store slow processes

-   Easily accommodate changes

-   Minimize code duplications

-   Easier maintainability and testability

-   To be flexible for UI Designers
#
#### How it works

Your application will **define** all UI components it can render (Design System) and **register** them inside Beagle. With that, now your BFF (Backend For Frontend) will be able to control how your application will be presented by returning a JSON that describes the interface for each endpoint.

![](https://blobs.gitbook.com/assets%2F-M-Qy7jZbUpzGRP5GbCZ%2F-M-RRc0XmH0zvxTIpMdW%2F-M-UyUyBv3ZqZpZl8m-E%2FWhatsApp%20Image%202020-02-06%20at%2019.21.26.jpeg?alt=media&token=7ea0eb18-0950-4a0e-af1c-91f836b1c66c)

#### Server-Driven UI

![](https://blobs.gitbook.com/assets%2F-M-Qy7jZbUpzGRP5GbCZ%2F-M-Vb8Yg30urdiTYjfB3%2F-M-VjxuYL-Q8MKfVharu%2Fimage42.png?alt=media&token=2c64711f-ba57-4754-a213-5a94ff387429)

**Server-Driven UI**  is an implementation paradigm that defines how the visual components are settle on a screen, the screen structure and how pages will flow to one another. All this information comes from a server called **Back-end For Front-end** through `JSON` objects. In the Server Driven paradigm the structure and flow between screens is incorporated into the `JSON` objects trafficked with content, which are divided into 3 basic pillars: **content, visual structure and flow (actions)**.  Check the **Beagle** example on the block below:

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
The front-end will set the visual structure and how pages connects and acts on one another natively according to the front-end system. As the application sends all actions to the back end, it responds systematically to each action, which configures our “single source of truth”.

In this model the new features and combinations of UI components can be tested without releases and updates, which practically summarizes an A / B type test.

Different types of styles and layouts can be tested in certain customer's`circles` , which implies several testing possibilities and Analytics data.

##

#### Backend For Frontend

This is the server that provides the `JSON` objects that will be rendered by the front as `views`. With this component, screens and business rules are written only once and thus rendered natively on each platform that Beagle is present on.

##

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
##

#### Serializer

The `Beagle serializer` responsibility is converting a declarative layout into a `JSON` object. In order to serialize Kotlin classes into `JSON` objects Beagle will use the [Jackson converter](https://github.com/FasterXML/jackson)_._

To serialize Kotlin classes in JSON objects, Beagle uses _Jackson_.

##

#### Deserializer

The `Beagle deserializer` responsibility is to convert a `JSON` object into a `widget` using [Moshi](https://github.com/square/moshi)_._

On Android _Moshi_ is used for this purpose.

![](https://blobs.gitbook.com/assets%2F-M-Qy7jZbUpzGRP5GbCZ%2F-M-Vb8Yg30urdiTYjfB3%2F-M-Vj__Tmdf3Px_wVuu2%2FCaptura%20de%20Tela%202020-02-07%20a%CC%80s%2014.25.17.png?alt=media&token=8f788eee-4371-4aca-b079-0ead3b6b0eb4)

Beagle BFF and Beagle Mobile

#

#### Design System Language

It is an interface that defines a set of methods that must be implemented to define the **application's theme**. The `appDesignSystem` must keep the button styles, text displays, toolbar styles, images and themes. For Beagle to work according to the application design system, it is necessary to have all of these styles implemented. They will be used the moment the visualization is rendered.

![](https://blobs.gitbook.com/assets%2F-M-Qy7jZbUpzGRP5GbCZ%2F-M-Vb8Yg30urdiTYjfB3%2F-M-VkXqAaAU8k7KAUaLW%2Fimage22.png?alt=media&token=62e91f51-a030-499d-91ca-f5131fd88790)

Design System Language for text, color and scale

#

#### Layout Engine

When rendering the screen the front-end receives a `JSON` object that provides all necessary information through **Beagle** to position the elements on the screen and deserialize it natively for Android or iOS.

When deserializing, Beagle turns a `widget` into a `view` applying the styles implemented in the `DesignSystem`. At this point, Beagle uses the [**YOGA**](https://yogalayout.com/) rendering engine internally to set the layout's view position and finally render it natively.

![](https://blobs.gitbook.com/assets%2F-M-Qy7jZbUpzGRP5GbCZ%2F-M-WAWIJGEgjFTyn57to%2F-M-WE7CKoTKbtxvMCMLt%2Fyoga.gif?alt=media&token=070dba81-176f-451f-b82e-cf3589c6e2f8)

#### Learn how to:

- [Create a Backend project](https://zup-products.gitbook.io/beagle/introducing/beagle-backend)
- [Create an Android project](https://zup-products.gitbook.io/beagle/introducing/android)
- [Create an iOS project](https://zup-products.gitbook.io/beagle/introducing/ios)

## Contributing guide

Please refer to [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to add new feature and contribute in general to Beagle
