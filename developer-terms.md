By integrating your Wear OS (or Android Wear) application (or watch face) to include `com.rokasjankunas.ticktock.AMBIENT_INTERACTIVE_MODE_CHANGE` permission and/or send broadcasts to TickTock Wear application (package name `com.rokasjankunas.ticktock`) and/or interfere with the work of TickTock Wear application in other way, you agree to the following terms.

1. Broadcast with intent action `com.rokasjankunas.ticktock.TRANSITION_TO_AMBIENT_MODE` must be sent only when your application transitions to ambient mode.

2. Broadcast with intent action `com.rokasjankunas.ticktock.TRANSITION_TO_INTERACTIVE_MODE` must be sent only when your application transitions to interactive mode.

3. Your application must always send broadcasts with intent action `com.rokasjankunas.ticktock.TRANSITION_TO_AMBIENT_MODE` or `com.rokasjankunas.ticktock.TRANSITION_TO_INTERACTIVE_MODE` when the ambient mode changes.

4. Your application must not receive broadcasts with intent action `com.rokasjankunas.ticktock.TRANSITION_TO_AMBIENT_MODE` and/or `com.rokasjankunas.ticktock.TRANSITION_TO_INTERACTIVE_MODE`.

5. Your application must use `com.rokasjankunas.ticktock.AMBIENT_INTERACTIVE_MODE_CHANGE` permission.
