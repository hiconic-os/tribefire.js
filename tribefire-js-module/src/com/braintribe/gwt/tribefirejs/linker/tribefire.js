/* hiconic.js JavaScript Library v{tf-version}
 *
 * Date: {tf-date}
 */
export function initHcJs(T, hc) {
hc.version = function(){return "{tf-version}";}
console.time('hiconic-js-start');
var $wnd = globalThis;
var $hcjs = {T, hc}
hc.$bak = {T: $wnd.T, hc: $wnd.hc};
$wnd.T = T;
$wnd.hc = hc;
// generated script start
{tf-script}
// generated script end
if(hc.$bak.T) $wnd.T = hc.$bak.T; else delete $wnd.T;
if(hc.$bak.hc) $wnd.hc = hc.$bak.hc; else delete $wnd.hc;
delete hc.$bak
hc.initHcJs();
console.timeEnd('hiconic-js-start');
console.log("hiconic.js v.{tf-version}");
}