// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.gwt.customization.client.tests.collectionish;

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Arrayish;

import javaemul.internal.annotations.DoNotInline;

/**
 * Tests for {@link Arrayish}
 */
public class ArrayishTest extends AbstractCollectionishTest {

	@Override
	protected void tryRun() throws Exception {
		String[] a = new String[] { "a", "b", "c", "d" };
		Arrayish<String> af = new Arrayish<>(asList((Object[]) a), null, null);

		testMethods(af, a);
	}

	@DoNotInline
	private native void testMethods(Arrayish<String> aa, String[] a) /*-{
		this.assertEqual(aa.length, a.length, "length");

		if (eval("typeof Aa") != 'undefined') 
			this.assertEqual(eval("[...Aa]"), a, "spread to array");
		if (eval("typeof A") != 'undefined') 
			this.assertEqual(eval("[...A]"), a, "spread to array");
		else
			this.logWarn("Cannot test 'spread to array' because given Arrayish has an unexpected name, i.e. other than 'Aa' or 'A'");

		this.assertEqOperation(aa, a, 'a.toLocaleString()');

		// --- push/pop ---

		this.assertEqOperation(aa, a, 'a.push("E", "F")', 6);
		this.assertEqOperation(aa, a, 'a.pop()', "F"); // pop F
		this.assertEqOperation(aa, a, 'a.pop()', "E"); // pop E

		// --- concat/join ---

		this.assertEqOperation(aa, a, 'a.concat(["e", "f"], "g")', ["a", "b", "c", "d", "e", "f", "g"]);
		this.assertEqOperation(aa, a, 'a.join()', "a,b,c,d");
		this.assertEqOperation(aa, a, 'a.join("-")', "a-b-c-d");

		// --- reverse ---

		this.assertEqOperation(aa, a, 'a.reverse().toString()', "d,c,b,a");
		this.assertEqOperation(aa, a, 'a.reverse().toString()', "a,b,c,d"); // undo reverse

		// --- shift/unshift ---

		this.assertEqOperation(aa, a, 'a.shift()', "a"); 			// b, c, d
		this.assertEqOperation(aa, a, 'a.shift()', "b"); 			// c, d
		this.assertEqOperation(aa, a, 'a.shift()', "c"); 			// d
		this.assertEqOperation(aa, a, 'a.unshift("c")', 2); 		// c, d
		this.assertEqOperation(aa, a, 'a.unshift("b", "a")', 4); 	// b, a, c, d 

		// --- sort ---

		this.assertEqOperation(aa, a, 'a.sort(function(a, b){return a.localeCompare(b);}).toString()', "a,b,c,d"); 			//	a, b, c, d

		// --- slice/splice ---

		this.assertEqOperation(aa, a, 'a.slice(1, 2)');

		this.assertEqOperation(aa, a, 'a.splice(1, 2, "E", "G").toString()', "b,c"); 			//	a, E, G, d
		this.assertEqOperation(aa, a, 'a.splice(1, 2, "b", "c").toString()', "E,G"); 			//	a, b, c, d

		// --- indexOf/lastIndexOf ---

		this.assertEqOperation(aa, a, 'a.push("b")');		// a, b, c, d, b

		this.assertEqOperation(aa, a, 'a.indexOf("N/A")', -1);
		this.assertEqOperation(aa, a, 'a.indexOf("b")', 1);
		this.assertEqOperation(aa, a, 'a.indexOf("b", 2)', 4);

		this.assertEqOperation(aa, a, 'a.lastIndexOf("N/A")', -1);
		this.assertEqOperation(aa, a, 'a.lastIndexOf("b")', 4);
		this.assertEqOperation(aa, a, 'a.lastIndexOf("b", 3)', 1);

		this.assertEqOperation(aa, a, 'a.pop()', "b"); // pop b

		// --- predicates ---

		this.assertEqOperation(aa, a, 'a.every(function(x){return true})', true);
		this.assertEqOperation(aa, a, 'a.every(function(x){return x == "a"})', false);
		this.assertEqOperation(aa, a, 'a.some(function(x){return x == "a"})', true);
		this.assertEqOperation(aa, a, 'a.some(function(x){return x == "x"})', false);

		// --- streamy ---

		this.assertEqOperation(aa, a, '(i=0,a.forEach(function(x){i++}), i);', 4);
		this.assertEqOperation(aa, a, 'a.map(function(x){return x.toUpperCase()}).toString()', "A,B,C,D");
		this.assertEqOperation(aa, a, 'a.filter(function(x, i){return i < 2}).toString()', "a,b");
		this.assertEqOperation(aa, a, 'a.reduce(function(x, y){return x+y}, "").toString()', "abcd");
		this.assertEqOperation(aa, a, 'a.reduceRight(function(x, y){return x+y}, "").toString()', "dcba");

		// --- es2015.core ---

		this.assertEqOperation(aa, a, 'a.find(function(x){return x == "b"})', "b");
		this.assertEqOperation(aa, a, 'a.findIndex(function(x){return x == "b"})', 1);
		this.assertEqOperation(aa, a, 'a.findLastIndex(function(x){return (typeof x == "string");})', 3);

		this.assertEqOperation(aa, a, 'a.fill("x", -3, 3).toString()', 'a,x,x,d');
		this.assertEqOperation(aa, a, 'a.fill("x", -3, 3).toString()', 'a,x,x,d'); 
		this.assertEqOperation(aa, a, 'a.splice(1, 2, "b", "c").toString()', "x,x"); 			//	a,b,c,d

		// --- es2015.iterable ---
		this.assertIteration(aa, a, 'a.entries()', '0,a,1,b,2,c,3,d');
		this.assertIteration(aa, a, 'a.keys()', '0,1,2,3');
		this.assertIteration(aa, a, 'a.values()', 'a,b,c,d');

		// --- es2016.array ---
		this.assertEqOperation(aa, a, 'a.includes("a")', true);
		this.assertEqOperation(aa, a, 'a.includes("a", 1)', false);
	
		// --- es2022.array ---
		this.assertEqOperation(aa, a, 'a.at(0)', "a");
		this.assertEqOperation(aa, a, 'a.at(-1)', "d");
		this.assertEqOperation(aa, a, 'a.at(4)', undefined);
		this.assertEqOperation(aa, a, 'a.at(-5)', undefined);
	}-*/;

}
