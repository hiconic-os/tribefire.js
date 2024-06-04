/*!
 * hiconic.js JavaScript Library v{tf-version}
 *
 * Date: {tf-date}
 */
console.time('start-hiconic-js');
var $wnd = window;
var $doc = $wnd.document;
{tf-script}
$wnd.$tf.version = function(){
	return "{tf-version}";
}
console.timeEnd('start-hiconic-js');
console.log("hiconic.js JavaScript Library {tf-version}");