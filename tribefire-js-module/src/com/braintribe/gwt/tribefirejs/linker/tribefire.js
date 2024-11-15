/* hiconic.js JavaScript Library v{tf-version}
 *
 * Date: {tf-date}
 */
import {T, hc} from '@dev.hiconic/hc-js-base';

hc.version = function(){return "{tf-version}";}
console.time('start-hiconic-js');
var $wnd = Object.create(globalThis);
$wnd.hc = hc;
$wnd.T = T;
// generated script start
{tf-script}
// generated script end
console.timeEnd('start-hiconic-js');
console.log("hiconic.js v.{tf-version}");