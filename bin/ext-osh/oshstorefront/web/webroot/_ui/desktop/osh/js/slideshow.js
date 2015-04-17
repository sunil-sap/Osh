jQuery(document).ready(function($) {
// timeouts per slide (in seconds)
var timeouts = [];
$('.slides').children('li').each(function(index) {
timeouts[index] = $(this).data('timeout');
});
// initialize the slideshow
$('.slides').cycle({
speed: 500,
timeoutFn: calculateTimeout,
fx: 'fade',
pager: $('.indicators'),
pagerEvent: 'mouseover',
pause: 1,
pauseOnPagerHover: 1
});
function calculateTimeout(currElement, nextElement, opts, isForward) {
var index = opts.currSlide;
return timeouts[index] * 1000;
}
}); 