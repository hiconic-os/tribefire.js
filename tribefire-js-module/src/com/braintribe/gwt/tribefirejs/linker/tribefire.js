/* hiconic.js JavaScript Library v{tf-version}
 *
 * Date: {tf-date}
 */
export function initHcJs(T, hc) {
hc.version = function(){return "{tf-version}";}
console.time('hiconic-js-start');
var $wnd = Object.create(globalThis);
$wnd.T = T;
$wnd.hc = hc;
// generated script start
{tf-script}
// generated script end
hc.initHcJs();
console.timeEnd('hiconic-js-start');
console.log("hiconic.js v.{tf-version}");
}