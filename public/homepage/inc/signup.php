<?php
	require_once 'mailchimp/MCAPI.class.php';
	require_once 'config.php'; 
	
	$from = $_REQUEST['email'];
	$name = $_REQUEST['name'];
	$website = $_REQUEST['website'];
	$description = $_REQUEST['description']; 
	
	if($mode == 'mailchimp') {
	
		$user_email = $from; 
		
		$api = new MCAPI($apikey);
		
		$merge_vars = array('FNAME'=>$name, 'URL'=>$website, 'DESC'=>$description);
		
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
	    	$subject = "New Signup via the Promo Landing Page"; // Email subject line 
		$headers = "MIME-Version: 1.0" . "\r\n";
		$headers .= "Content-type:text/html;charset=UTF-8" . "\r\n";	    
	    	$headers .= "From: $from";
	    	
	    	$fields = array();
	    	$fields{"name"} = "Name";
	    	$fields{"email"} = "Email";
	    	$fields{"website"} = "Website";
	    	$fields{"description"} = "Description";
		
		$body = "Details:\n\n<ul>"; 
		foreach($fields as $a => $b){   $body .= "<li>". sprintf("%20s: %s\n",$b,$_REQUEST[$a])."</li>"; }
		$body .= "\n<ul>"; 
	
	    	$send = mail($to, $subject, $body, $headers);
	
	}
?>
