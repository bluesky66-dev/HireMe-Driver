package com.driver.hire_me;

public interface Config {

	// CONSTANTS
	//static final String YOUR_SERVER_URL =  "http://127.0.0.1/gcm_server_files/register.php";
	// YOUR_SERVER_URL : Server url where you have placed your server files
    // Google project id
   // static final String GOOGLE_SENDER_ID = "391226754417";  // Place here your Google project id
    static final String GOOGLE_SENDER_ID = "915598348361";  // Place here your Google project id

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.driver.hire_me.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";
		
	
}
