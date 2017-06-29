<?php
require_once 'Mobile_Detect.php';
$detect = new Mobile_Detect;
?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>BitPlus - Send mobile money to Somalia</title>
	<meta name="author" content="Salman Khan">
	<meta name="description" content="BitPlus - Send mobile money to Somalia from anywhere around the world with lightening quick speed.">
	<meta name="keywords" content="bitcoin, somalia, send money to somalia, bitplus">
	<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />  
	<link href="assets/homepage/css/style.css" rel="stylesheet"/>  
	<link rel="stylesheet" href="assets/homepage/css/flexslider.css" type="text/css" media="screen" />
</head> 
<body> 
	
		<div id="navigation">
			
			<div class="nav-container">
		
				<h3><a href="#" class="close"><span aria-hidden="true" class="icon_close_alt2 medium" ></span></a></h3>
				
				<ul>
					<li><a href="#">Home</a></li>
					<li><a href="#">Login</a></li>
					<li><a href="#">Signup</a></li>
					<li><a href="#">Help</a></li>
					<li><a href="#">FAQ</a></li>
					<li><a href="#">About</a></li>
					<li><a href="#">Contact</a></li>
	
				</ul>				
				
			</div>
		
		</div>
	
		<div id="header">
		
			<div class="row">
				<div class="span12">
				
					<h1><a href="/"><img class="logo" oncontextmenu="return false" src="assets/homepage/images/logo.png" alt=""/></a></h1>
					<h2 class="menulink"><a href="#">Menu</a></h2> 
					
				</div>
				<div class="clearfix"></div>
			</div>
				
		</div>
		
	<div id="hero">
	
		<!-- Start Content -->
		
			<div id="content" class="desktop hero">
		
				<div class="container"> 
						
					<div class="intro align-centered pd-t-20">
						<h2>Send Mobile Money to <span>Somalia</span>!</h2>		
						<h4 class="pd-t-3">Introducing BitPlus, the hottest new way to send mobile money to Somalia using Bitcoin.</h4>
						<h4 class="pd-t-3"><a href="#" class="button"> Download Whitepaper</a></h4>		       
					</div>
			        <div class="clearfix"></div>

				</div>
			
			</div>
		
		
		<div class="clearfix"></div>
		
	</div>

	<?php /* Start - How it Works */ ?>
	<div class="panel panel-minimal">
	
		<div class="container">
	
			<div class="row">
			
				<div class="align-centered">
					<h1>See how it works</h1>
					<?php
					//visual representation to what CFMaBiSmAd said
					if( $detect->isMobile() ) {
					?>
					<img oncontextmenu="return false" src="assets/homepage/images/theprocess-m.png" alt="" class="promoimg"/>
					<?php
					} else {
					?>
					<img oncontextmenu="return false" src="assets/homepage/images/theprocess.png" alt="" class="promoimg"/>
					<?php
					}
					?>
				</div>

			</div>
		
		</div>
		
		<div class="clearfix"></div>
	</div>
	<?php /* END - How it Works */ ?>

	<?php /* Start - Features */ ?>
	<div class="panel bg-alt">

		<div class="container">
	
			<div class="row">
			
				<div class="span3">
					<div class="inner align-centered">
						<div aria-hidden="true" class="icon_heart_alt large"></div>
						<h3>Simple Process</h3>
						<p>BitPlus makes sending mobile money to Somalia using Bitcoin  much easier than you thought possible.</p>
						<div class="clearfix"></div>
						
					</div>
				</div>
				
				<div class="span3">
					<div class="inner align-centered">
						<div aria-hidden="true" class="arrow_carrot-2right large" ></div>
						<h3>Fast Transactions</h3>
						<p>Send mobile money to Somalia from anywhere around the world with lightening quick speed.</p>
						<div class="clearfix"></div>
						
					</div>
				</div>
				
				<div class="span3">
					<div class="inner align-centered">
						<div aria-hidden="true" class="icon_currency large" ></div>	
						<h3>Low Fee</h3>
						<p>BitPlus charges a small 2.5% fee per transaction. Dahabshiil and other competitors charge 6% or more.</p>
						<div class="clearfix"></div>
						
					</div>
				</div>
			
				<div class="span3">
					<div class="inner align-centered">
						<div aria-hidden="true" class="icon_wallet large" ></div>					
						<h3>No Minimum Limit</h3>
						<p>There are no minimum limits when sending money. Send as little or as much as you want with BitPlus.</p>
						<div class="clearfix"></div>
						
					</div>
				</div>
			
			</div>
		
		</div>
		
		<div class="clearfix"></div>
	</div>
	<?php /* END - Features */ ?>

	<?php /* Start - Countdown */ ?>
	<div class="panel panel-minimal bg-countdown">
	
		<div class="container">
	
			<div class="row">
			
				<div class="align-centered">
					<h1 style="color: #fff">Token Pre-Sale launching in</h1>
					  <div class="countdown align-centered">

					    <div class="time-circle">
					      <div class="progress"></div>
					      <span class="time days"></span>
					      <span class="time-name">days</span>
					    </div>
					    <span class="dots">:</span>
					    <div class="time-circle">
					      <div class="progress"></div>
					      <span class="time hours"></span>
					      <span class="time-name">hrs</span>
					    </div>
					    <span class="dots">:</span>
					    <div class="time-circle">
					      <div class="progress"></div>
					      <span class="time minutes"></span>
					      <span class="time-name">min</span>
					    </div>
					    <span class="dots">:</span>
					    <div class="time-circle">
					      <div class="progress"></div>
					      <span class="time seconds"></span>
					      <span class="time-name">sec</span>
					    </div>
					  </div>
				</div>

			</div>
		
		</div>
		
		<div class="clearfix"></div>
	</div>
	<?php /* END - Countdown */ ?>

	<div class="clearfix"></div>	

	<?php /* Start - Terms */ ?>
	<div class="panel bg-alt">

		<div class="container">
				<div class="row">

					<div class="span6">
						<div class="align-centered pd-b-3">
							<h2>Features</h2>
						</div>
						<ul>
							<li>Ability to control backend clearing processes</li>
							<li>Limited supply of tokens during the crowdfunding period, amounting to max. 10,000,000</li>
							<li>BPNT is a token on the Ethereum platform. It’s design follows common standards adopted in token crowdfunding, making it easy for users of Ethereum Wallet</li>
						</ul>
					</div>

					<div class="span6">
						<div class="align-centered pd-b-3">
							<h2>Conditions</h2>
						</div>
						<ul>
							<li>Minimum ether: 5000</li>
							<li>Maximum ether: 50000</li>
							<li>Percentage reserved to crowdfunding participants: 75%</li>							
							<li>Percentage reserved to Bitplus Team: 10%</li>
							<li>Percentage reserved to Early Token Buyers: 15%</li>
							<li>BPNT creation ration is 1,000 BPNT to 1 ETH.</li>
							<li>In case the minimal funding goal is not reached, the contract allows buyers to issue a refund.</li>
						</ul>
					</div>
					
				</div>
		
		</div>
		
		<div class="clearfix"></div>
	</div>
	<?php /* END - Token Terms */ ?>

	<?php /* Start - Signup */ ?>

	<div id="content" class="signup hero">

		<div class="container pd-t-3 pd-b-3">
				 
			<div class="row"> 
		
					<div class="span2">&nbsp;</div>
		
		        	<div class="span8"><br />	
			
		        		<div class="align-centered pd-b-3">
			        		<h2>Signup for <span>Beta</span></h2>
			        	</div> 
			        	
			        </div>
	    
			</div>
			
			<div class="clearfix"></div>
					 
	            <div class="row"> 
    	
    			<div class="span3">&nbsp;</div>
    	
			        	<div class="span6">
			        	
			        		<div class="inner">
			        		
			        			<div class="form-box boxed">
			        			
			        				<div class="top">
		
				        				<h3>Please complete the form below to register for our beta version</h3>
				        				
				        				<span class="spacer"></span>
				        				
			        				</div>
			        			
			        				<div class="bottom"> 
										
										<div id="success">
										    <div class="green textcenter">
										        <p>Your message was sent successfully!</p>
										    </div>
										</div>
		
										<div id="error">
										    <div>
										        <p>Something went wrong. Please refresh and try again.</p>
										    </div>
										</div>
																			        				
										<form id="signup" name="signup" method="post" novalidate="novalidate">
											
											<div class="form-row">	
												<div class="span3">
													<div class="inner align-right">
														
													</div>
												</div>
												<div class="span7">
													<div class="input-wrapper">
														<input type="text" name="name" id="name" size="30" value="" required="" class="text login_input"  placeholder="Your name">
													</div>
												</div>
												<div class="clearfix"></div>
											</div>  
											
											<div class="form-row">	
												<div class="span3">
													<div class="inner align-right">
														
													</div>
												</div>
												<div class="span7">
													<div class="input-wrapper">
														<input type="text" name="email" id="email" size="30" value="" required="" class="text login_input"  placeholder="Email Address">
													</div>
												</div>
												<div class="clearfix"></div>
											</div>
											
											<div class="form-row">	
												<div class="span3">
													<div class="inner align-right">
														
													</div>
												</div>
												<div class="span7">
													<div class="input-wrapper">
														<input type="password" name="password" id="password" size="30" value="" required="" class="text login_input"  placeholder="Choose a Password">
													</div>
												</div>
												<div class="clearfix"></div>
											</div> 
															
											<div class="form-row">	
												<div class="span3 hidden">
													&nbsp;
												</div>
												<div class="span7">
													<input id="submit" type="submit" name="submit" value="Sign up now!" class="btn btn-wide btn-extrawide" data-loading-text="Registering...">
												</div>
												<div class="clearfix"></div>
											</div>   
											
										</form>
										
									</div> 
			        			
			        			</div> 
			        			<div class="clearfix"></div>
			        			
			        			<p class="padded align-centered">By clicking you agree to our Terms of Service, Privacy Policy &amp; Refund Policy.</p>
			        			
			        		</div>
			        		
			        	</div>
			        	
		            	</div>  
	            	
	            <div class="clearfix"></div>
		
		</div>
	
	</div>
		
		
	<?php /* End - Signup */ ?>
	
	<div class="clearfix"></div>			
	
		<div class="panel" id="footer">

			<div class="container">
					 
				<div class="row">
					
					<div class="span12">
					
						<div class="inner align-centered">
							
							<a href="#"><span aria-hidden="true" class="social_facebook medium" ></span></a> 
							<a href="#"><span aria-hidden="true" class="social_twitter medium" ></span></a> 
							<a href="#"><span aria-hidden="true" class="social_googleplus medium" ></span></a> 
							<div class="clearfix"></div>

							<div class="copyright">
								&copy; 2015 <a href="/">BitPlus</a>.
							</div>
							
							<div class="clearfix"></div>
							<br /><img src="assets/homepage/images/btc-evc.png" oncontextmenu="return false" alt="" />
							<div class="clearfix"></div>
						
						</div>  
					
					</div>  
	
				</div>
				
			</div>
		
		</div>
	
	<script src="assets/homepage/js/jquery.min.js"></script>
	<script src="assets/homepage/js/respond.min.js"></script>
	<script src="assets/homepage/js/scripts.js"></script> 
	<script defer src="assets/homepage/js/jquery.flexslider-min.js"></script> 
	<script src="assets/homepage/js/jquery.form.js"></script>
	<script src="assets/homepage/js/jquery.validate.min.js"></script>
	<script src="assets/homepage/js/contact.js"></script> 
	<!--[if lte IE 7]><script src="assets/homepage/css/lte-ie7.js"></script><![endif]-->
	
</body>
</html>
