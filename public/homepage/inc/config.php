<?php
/*-----------------------------------------------------------------------------------*/
/*	
/*	Select Mode
/*	
/*	Choose 'email' to send yourself an mail with the subscribers details, or specify 'mailchip' to save subsribers to a mailchimp list.
/*	Example: $mode = 'mailchimp';
/*	Example: $mode = 'email';
/*
/*-----------------------------------------------------------------------------------*/

	$mode = 'email'; // Choose email or mailchimp

/*-----------------------------------------------------------------------------------*/
/*	
/*	Settings for: Administrator Email Mode
/*	
/*	Example: $adminemail = "you@youremail.com";
/*
/*-----------------------------------------------------------------------------------*/

	$adminemail = "yourname@example.com";  // Replace with your own

/*-----------------------------------------------------------------------------------*/
/*	
/*	Settings for: Mailchimp Mode
/*	
/*	Example: $adminemail = "you@youremail.com";
/*
/*-----------------------------------------------------------------------------------*/

    // API Key - see http://admin.mailchimp.com/account/api
    // Example: $apikey = '731c311dc6c65777hud78dghe7c248e20d-us9'; 
    
    $apikey = '731c311c6c65777f40f0ac4f1248e20d-us9'; // Replace with your own
    
    // A List Id to add subscribers to. 
    // To determin the List ID, login to you MailChimp account, go to List, click on the list title. Select the Settings menu and choose 'List name & default' from the drop down menu. The List ID will be presented on the top right of the page.
    // Example: $listId = '21a0sadd1';
    
    $listId = '21a04c3731'; // Replace with your own

    //just used in xml-rpc examples
    
    $apiUrl = 'http://api.mailchimp.com/1.3/';
    
?>
