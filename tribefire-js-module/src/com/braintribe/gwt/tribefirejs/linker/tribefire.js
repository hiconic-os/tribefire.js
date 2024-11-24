/* hiconic.js JavaScript Library v{tf-version}
 *
 * Date: {tf-date}
 */
export function initHcJs(T, hc) {
hc.version = function(){return "{tf-version}";}
console.time('start-hiconic-js');
var $wnd = Object.create(globalThis);
$wnd.T = T;
$wnd.hc = hc;
// generated script start
{tf-script}
// generated script end
console.timeEnd('start-hiconic-js');
console.log("hiconic.js v.{tf-version}");
}