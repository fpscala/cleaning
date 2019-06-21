var $stage 			= $('.info'),
	$sprayer 		= $('.sprayer'),
	$item 			= $('.item'),
	$fanWind 		= $('.fan-wind'),
	$car 			= $('.car');

var animationEnd = "animationend webkitAnimationEnd oanimationEnd MSAnimationEnd",
	animationStart = "animationstart animationstart	webkitAnimationStart oanimationstart MSAnimationStart";

$sprayer.on(animationEnd, function(){
	$stage.html("<div class='stage-number'>02</div><div class='stage-name'>" + stages.two + "</div>");
});

$fanWind.on(animationStart, function(){
	$stage.html("<div class='stage-number'>03</div><div class='stage-name'>" + stages.three + "</div>");
});

$fanWind.on(animationEnd, function(){
	window.setTimeout(function(){
		$stage.html("<div class='stage-number'>04</div><div class='stage-name'>" + stages.four + "</div>");
	}, 3000);

	window.setTimeout(function(){
		$stage.html("<div class='stage-number'>05</div><div class='stage-name'>" + stages.five + "</div>");
	}, 7000);
});

$car.on(animationEnd, function(){
	$stage.html("<div class='stage-number'>01</div><div class='stage-name'>" + stages.one + "</div>");
});

var $elements = $('[data-animation]'),
	$last = $('[data-animation-last]'),
	props = [];

	$elements.each(function(){
		props.push($(this).css('animation'));
	});

$last.on(animationEnd, function () {
	$elements.each(function(index){
		var $this = $(this);

		$this.css({"animationName":false});

		window.setTimeout(function(){
			$this.css({"animation":props[index]})
		}, 50);
	});
});


