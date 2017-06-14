<?php
	require_once 'mailchimp/MCAPI.class.php';
	require_once 'config.php'; 
	
	if($mode == 'mailchimp') {
	
		$user_email = $_REQUEST['subscribe_email']; 
		
		$api = new MCAPI($apikey);
		
		$merge_vars = array();
		
		// By default this sends a confirmation email - you will not see new members
		// until the link contained in it is clicked!
		$retval = $api->listSubscribe( $listId, $user_email, $merge_vars );
		
		if ($api->errorCode){
			echo "Unable to load listSubscribe()!\n";
			echo "\tCode=".$api->errorCode."\n";
			echo "\tMsg=".$api->errorMessage."\n";
		} 
		
	}
	
	if($mode == 'email') {

		// Configuration 
	    	$to = $adminemail; // Your email address. 
	    	$subject = "New subscriber via the Promo Landing Page"; // Email subject line 
	    
	    	$from = $_REQUEST['subscribe_email']; 
	    	$name ="New subscriber";
		$headers = "MIME-Version: 1.0" . "\r\n";
		$headers .= "Content-type:text/html;charset=UTF-8" . "\r\n";	   
	    	$headers .= "From: $from";
	
	    	$body = "Subscriber email address:\n\n".$from;  
	
	    	$send = mail($to, $subject, $body, $headers);
    
	}
?>
