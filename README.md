# TickTock Wear

TickTock wear is a semi-open source Wear OS (Android Wear) application that adds a ticking sound to your Android smart watch. Add time, battery percentage, and charging status restrictions. Premium users can also change the ticking sound and enable hourly chime.

<url>

<screenshots>

## Table of Contents
<change to URLS>

1. Installation
    1. Prerequisites
    2. Installing
2. For Developers
3. Donate
4. Versioning
5. Author
6. Acknowledgments
7. Privacy Policy
8. License

## Installation

You can install TickTock Wear directly from your computer. Method below describes how to run TickTock Wear directly from the code and not how to sideload already precompiled .apk file. Instructions assume your watch is connected to your computer (either via USB, Wi-Fi, Bluetooth or else). You can always install TickTock Wear directly from your watch (Wear OS and Android Wear version 2.0 and greater).

### Prerequisites

* Android SDK v28
* Latest Android Build Tools
* Android Support Repository
* Latest Android Studio (recommended) or other IDE
* Developer options and ADB debugging enabled

### Installing

1. Download the source code or clone the repository.
2. Open Android Studio and choose "Open an Existing Android Studio Project".
3. Choose module "app".
4. Click the Run button.

## For Developers

Developers can easily integrate their existing Wear OS (Android Wear) watch faces with TickTock Wear to provide more functionality. Currently watch faces are allowed to send broadcasts when the ambient mode changes. Depending on user settings, TickTock Wear then may pause ticking on ambient or active modes.

To integrate your watch face:

1. Paste these lines to the top of your watch face class(-es):
```
private static final String TRANSITION_TO_AMBIENT_MODE = "com.rokasjankunas.ticktock.TRANSITION_TO_AMBIENT_MODE";
private static final String TRANSITION_TO_INTERACTIVE_MODE = "com.rokasjankunas.ticktock.TRANSITION_TO_INTERACTIVE_MODE";
```

2. In `onAmbientModeChanged(boolean inAmbientMode)` method(s) add these lines:
```
if (inAmbientMode) {
    Intent intent = new Intent();
    intent.setAction(TRANSITION_TO_AMBIENT_MODE);
    intent.putExtra("package", getPackageName());
    sendBroadcast(intent, "com.rokasjankunas.ticktock.AMBIENT_INTERACTIVE_MODE_CHANGE");
} else {
    Intent intent = new Intent();
    intent.setAction(TRANSITION_TO_INTERACTIVE_MODE);
    intent.putExtra("package", getPackageName());
    sendBroadcast(intent, "com.rokasjankunas.ticktock.AMBIENT_INTERACTIVE_MODE_CHANGE");
}
```

3. To your AndroidManifest.xml file add this permission:
```
<uses-permission android:name="com.rokasjankunas.ticktock.AMBIENT_INTERACTIVE_MODE_CHANGE"/>
```

By integrating your Wear OS (or Android Wear) application (or watch face) to include `com.rokasjankunas.ticktock.AMBIENT_INTERACTIVE_MODE_CHANGE` permission and/or send broadcasts to TickTock Wear application (package name `com.rokasjankunas.ticktock`) and/or interfere with the work of TickTock Wear application in other way, you agree to [these terms](https://github.com/LayoutXML/TickTock-Wear/blob/master/developer-terms.md).

## Donate
You can now donate to me (LayouXML) on **[Google Play](https://play.google.com/store/apps/details?id=com.layoutxml.support)** or **[PayPal](https://www.paypal.me/RJankunas)**.

## Versioning

TickTock Wear versioning follows this scheme:

* Version code: number of commits to the repository with the full code (including premium parts).

* Version name: x.y.z, where z increases by 1 with at least one fix compared to the previous version, y increases by 1 with at least one new feature. y and x additionally increase by 1 if the number after it reaches 10. In that case the number that reaches 10 reverts back to 0.

## Author

More information about me and my projects: https://rokasjankunas.com

## Acknowledgments

Logo: [@elawhatson](https://github.com/elawhatson)

## Privacy policy

TickTock Wear does not send any anonymous or personally identifiable information - TickTock Wear does not have an internet permission. TickTock Wear may locally store user preferences for 3rd party integrations. This information is not send anywhere else and may be deleted in the OS settings (by pressing "Clear data").

## License

This application uses "Android In-App Billing v3" library by anjlab. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 or in the "LICENSE-ANDROID-IN-APP-BILLING-V3.txt" file.

Copyright laws apply.

Copyright © 2018 Rokas Jankūnas (LayoutXML)
