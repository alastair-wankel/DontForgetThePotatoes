package edu.macalester.potato;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/**
 * 
 * @author Awankel
 * This creates a splash screen that plays before the main menu can be reached.  This simply shows the splash image for a few seconds, then 
 * moves on to the main screen.  This code is based off a tutorial found online.
 *
 */
public class Splash extends Activity {

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.splash);
/** set time to splash out */
final int welcomeScreenDisplay = 3000;
/** create a thread to show splash up to splash time */
Thread welcomeThread = new Thread() {

int wait = 0;

@Override
public void run() {
try {
super.run();
/**
* use while to get the splash time. Use sleep() to increase
* the wait variable for every 100L.
*/
while (wait < welcomeScreenDisplay) {
sleep(100);
wait += 100;
}
} catch (Exception e) {
System.out.println("EXc=" + e);
} finally {
/**
* Called after splash times up. Do some action after splash
* times up. Here we moved to another main activity class
*/
startActivity(new Intent(Splash.this,
DontForgetThePotatoes.class));
finish();
}
}
};
welcomeThread.start();

}
}