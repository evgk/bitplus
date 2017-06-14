var hoverOutTimer = null;
 
jQuery(document).ready(function($) { 
	$(".menulink a").click(function() {
  		$("#navigation").stop(true, true).fadeToggle('fast'); 
	});
	$("a.close").click(function() {
  		$("#navigation").stop(true, true).fadeToggle('fast'); 
	});
	$("a.play").click(function() {
  		$(".video").stop(true, true).fadeToggle('fast');  
	});
	resize_intro(); 
}); 

jQuery(window).load(function(){
	'use strict';
  	$('.flexslider').flexslider({
    	animation: "slide",
   	 	start: function(slider){ 
  		$("#loadingoverlay").stop(true, true).hide(); 
      		$('body').removeClass('loading');
		resize_intro(); 
    	}
  	}); 
}); 

jQuery(window).resize(function () {  
	resize_intro(); 
});

function resize_intro(){
	page_height =  jQuery(window).height(); 
	content_height =  jQuery('#content').height();
 	if(jQuery('#top').height() < (page_height)) { 
	top_margin = (page_height - jQuery('#top').height()) / 2; 
		if(jQuery(window).width() > 770) {   
			jQuery("#top").css('margin-top',(top_margin+15)+'px');  
			jQuery("#top").css('margin-bottom',(top_margin)+'px');  
			jQuery(".flexslider .slides > li").css('height',(content_height)+'px');  
		}
	} else {
		jQuery("#top").css('margin-top','0px');  
		jQuery("#top").css('margin-bottom','0px');  
			jQuery(".flexslider .slides > li").css('height','300px');  	
	}
}

//Countdown JS
$(function() {

  var Countdown = function(options) {
    $.extend(this, {
      endDate: new Date(2017, 06, 19, 00, 02, 20, 0)
    }, options);

    this.cache();
    this.bind();

    return this;
  };
  $.extend(Countdown.prototype, {
    cache: function() {
      this.date = new Date();
      this.secCounter = parseInt((this.endDate - this.date) / 1000);
    },
    bind: function() {
      this.timer();
    },
    timer: function() {
      var timeInSec = this.secCounter,
        $secondsCircle = $('.seconds').closest('.time-circle').find('.progress'),
        $minutesCircle = $('.minutes').closest('.time-circle').find('.progress'),
        $hoursCircle = $('.hours').closest('.time-circle').find('.progress'),
        $daysCircle = $('.days').closest('.time-circle').find('.progress'),
        $seconds = $('.seconds'),
        $minutes = $('.minutes'),
        $hours = $('.hours');
        $days = $('.days')

      function pad(number) {
        return (number < 10 ? '0' : '') + number
      }

      function setScale(type, degree) {
        type.css({
          '-webkit-transform': 'rotate(' + -degree * 6 + 'deg)',
          '-moz-transform': 'rotate(' + -degree * 6 + 'deg)',
          '-ms-transform': 'rotate(' + -degree * 6 + 'deg)',
          '-o-transform': 'rotate(' + -degree * 6 + 'deg)',
          'transform': 'rotate(' + -degree * 6 + 'deg)'
        });
      };

      var setInterv = setInterval(function() {

        (timeInSec > 0 ? timeInSec-- : timeInSec = 0);

        var getSeconds = timeInSec % 60,
          getMinutes = Math.floor(timeInSec / 60 % 60),
          getHours = Math.floor(timeInSec / 3600 % 24),
          getDays = Math.floor(timeInSec / 86400 % 7),
          getWeeks = Math.floor(timeInSec / 604800);

        $seconds.text(pad(getSeconds));
        $minutes.text(pad(getMinutes));
        $hours.text(pad(getHours));
        $days.text(pad(getDays));

        setScale($secondsCircle, getSeconds);
        setScale($minutesCircle, getMinutes);
        setScale($hoursCircle, getHours);
        setScale($daysCircle, getDays);

        if (timeInSec <= 0) {
          clearInterval(setInterv);
          console.log('End of counting');
          $('.bg-countdown').hide();
        }

      }, 1000);
    }
  });
  window.Countdown = Countdown;
});

$(function() {
  var app = new Countdown({
    //properties
    //endDate: new Date(year, month, day, hour, minute, second, miliseco)
  });
});