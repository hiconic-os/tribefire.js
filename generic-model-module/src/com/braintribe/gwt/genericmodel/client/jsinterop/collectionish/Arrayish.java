package com.braintribe.gwt.genericmodel.client.jsinterop.collectionish;

import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

import com.braintribe.gwt.browserfeatures.client.interop.JsConcatArray;
import com.braintribe.gwt.browserfeatures.client.interop.JsIterableIterator;
import com.braintribe.gwt.genericmodel.client.reflect.TypePackage;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.Messages.Optional;

import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.annotations.custom.TsIgnore;
import jsinterop.utils.Lambdas.JsUnaryFunction;

/**
 * {@link List} wrapper that fulfills (most of) the JS Array contract.
 */
@TsIgnore
@JsType(namespace = TypePackage.GM_TYPE_NAMESPACE)
public class Arrayish<T> extends AbstractCollectionish<T> {

	private final List<Object> list;

	public Arrayish(List<Object> list, JsUnaryFunction<Object, T> javaToJs, JsUnaryFunction<T, Object> jsToJava) {
		super(javaToJs, jsToJava);
		this.list = list;
	}

	@Override
	public Object wrappedCollection() {
		return list;
	}

	// ############################################
	// ## . . . . . . . . es5 . . . . . . . . . .##
	// ############################################

	@JsProperty(name = "length")
	public final int length() {
		return list.size();
	}

	@Override
	public String toString() {
		return join(",");
	}

	public native String toLocaleString() /*-{
		return this.valueOf().toLocaleString();
	}-*/;

	public int push(T... items) {
		for (T item : items)
			list.add(toJavaIfNeeded(item));
		return list.size();
	}

	private void pushSingle(T item) {
		list.add(toJavaIfNeeded(item));
	}

	public T pop() {
		return removeIfPossible(list.size() - 1);
	}

	// TODO the actual .d.ts signature should be: concat(...items: (T | ConcatArray<T>)[]): T[];
	public native T[] concat(JsConcatArray<T>... arrays) /*-{
		var result = this.valueOf();
		for (var i in arguments)
			result = result.concat(arguments[i]);
		return result;
	}-*/;

	public String join(String separator) {
		if (separator == null)
			separator = ",";
		StringJoiner sj = new StringJoiner(separator);
		for (Object e : list)
			sj.add(e == null ? "" : e.toString());
		return sj.toString();
	}

	public Arrayish<T> reverse() {
		int len = list.size();
		int l_half = len >> 1;

		for (int start = 0; start < l_half; start++) {
			int end = len - 1 - start;
			Object tmp = list.get(start);
			list.set(start, list.get(end));
			list.set(end, tmp);
		}

		return this;
	}

	public T shift() {
		return removeIfPossible(0);
	}

	public int unshift(T... items) {
		insert(0, items);
		return list.size();
	}

	private T removeIfPossible(int i) {
		return list.isEmpty() ? undefined() : toJsIfNeeded(list.remove(i));
	}

	public native T[] slice(Double start, Double end) /*-{
		return this.valueOf().slice(start, end);
	}-*/;

	public Arrayish<T> sort(CompratorFunction<T> compareFn) {
		list.sort((o1, o2) -> compareFn.compare(toJsIfNeeded(o1), toJsIfNeeded(o2)));
		return this;
	}

	public T[] splice(int start, int deleteCount, T... items) {
		start = normalizeIndex(start);
		if (deleteCount < 0)
			deleteCount = 0;
		int end = start + deleteCount;
		if (end > list.size())
			end = list.size();

		// get items to remove
		List<Object> subList = list.subList(start, end);

		// prepare result
		Object[] jResult = subList.toArray();

		// remove items
		subList.clear();

		// add new items
		insert(start, items);

		return toJsArrayIfNeeded(jResult);
	}

	public int indexOf(T searchElement, Double fromIndex) {
		Object jsSearchElement = toJavaIfNeeded(searchElement);
		if (fromIndex == null)
			return list.indexOf(jsSearchElement);

		int startIndex = toIndex(fromIndex, 0);
		List<Object> l = list.subList(startIndex, list.size());
		int i = l.indexOf(jsSearchElement);

		return i < 0 ? -1 : startIndex + i;
	}

	public int lastIndexOf(T searchElement, Double fromIndex) {
		List<Object> l = list;
		if (fromIndex != null)
			l = list.subList(0, toIndex(fromIndex, 0));

		return l.lastIndexOf(toJavaIfNeeded(searchElement));
	}

	public native boolean every(JavaScriptObject predicate) /*-{
		return this.valueOf().every(predicate);
	}-*/;

	public native boolean some(JavaScriptObject predicate) /*-{
		return this.valueOf().some(predicate);
	}-*/;

	public native void forEach(JavaScriptObject callbackfn, @Optional JavaScriptObject thisArg) /*-{
		return this.valueOf().forEach(callbackfn, thisArg);
	}-*/;

	public native <U> U[] map(JavaScriptObject callbackfn) /*-{
		return this.valueOf().map(callbackfn);
	}-*/;

	public native T[] filter(JavaScriptObject predicate, @JsOptional JavaScriptObject thisArg) /*-{
		return this.valueOf().filter(predicate, thisArg);
	}-*/;

	public native <U> U reduce(JavaScriptObject callbackfn, @JsOptional JavaScriptObject initialValue) /*-{
		return this.valueOf().reduce(callbackfn, initialValue);
	}-*/;

	public native <U> U reduceRight(JavaScriptObject callbackfn, @JsOptional JavaScriptObject initialValue) /*-{
		return this.valueOf().reduceRight(callbackfn, initialValue);
	}-*/;

	// ############################################
	// ## . . . . . . . es2015.core . . . . . . .##
	// ############################################

	public native T find(JavaScriptObject predicate, @JsOptional JavaScriptObject thisArg) /*-{
		return this.valueOf().find(predicate, thisArg);
	}-*/;

	public native Double findIndex(JavaScriptObject predicate, @JsOptional JavaScriptObject thisArg) /*-{
		return this.valueOf().findIndex(predicate, thisArg);
	}-*/;

	// This is not even es2015.code, VS Code doesn't even know this method exists
	public native Double findLastIndex(JavaScriptObject predicate, @JsOptional JavaScriptObject thisArg) /*-{
		return this.valueOf().findLastIndex(predicate, thisArg);
	}-*/;

	public Arrayish<T> fill(T jsValue, @JsOptional Double _start, @JsOptional Double _end) {
		int start = toIndex(_start, 0);
		int end = toIndex(_end, list.size());

		Object jValue = toJavaIfNeeded(jsValue);

		for (int i = start; i < end; i++)
			list.set(i, jValue);

		return this;
	}

	// TODO TEST
	public Arrayish<T> copyWithin(int target, @JsOptional Double _start, @JsOptional Double _end) {
		if (target == 0)
			return this;

		int size = list.size();

		int start = toIndex(_start, 0);
		int end = toIndex(_end, size);

		List<Object> sub = newList(list.subList(start, end));
		Iterator<Object> it = sub.iterator();

		while (target < size && it.hasNext())
			list.set(target++, it.next());

		return this;
	}

	// ############################################
	// ## . . . . . . es2015.iterable . . . . . .##
	// ############################################

	// Real signature: entries(): IterableIterator<[number, T]>;
	public native JsIterableIterator<JavaScriptObject> entries() /*-{
		return this.valueOf().entries();
	}-*/;

	public native JsIterableIterator<Double> keys() /*-{
		return this.valueOf().keys();
	}-*/;

	public native JsIterableIterator<T> values() /*-{
		return this.valueOf().values();
	}-*/;

	// ############################################
	// ## . . . . . . es2016.array . . . . . . . ##
	// ############################################

	public boolean includes(T jsElement, Double start) {
		List<Object> l = list;
		if (start != null)
			l = l.subList(toIndex(start, 0), list.size());

		return l.contains(toJavaIfNeeded(jsElement));
	}

	// ############################################
	// ## . . . . . . es2022.array . . . . . . . ##
	// ############################################

	public T at(Double _index) {
		int index = _index == null ? 0 : castToInt(_index);
		if (index < 0)
			index += list.size();

		if (index < 0 || index >= list.size())
			return undefined();

		return toJsIfNeeded(list.get(index));
	}

	// ############################################
	// ## . . . . . . . . OTHER . . . . . . . . .##
	// ############################################

	// ES2019:
	// flat(), flatMap() are not needed, because our Arrayish never contains nested arrays

	// ES2023:
	// toReversed()
	// toSorted()
	// toSpliced()

	// ############################################
	// ## . . . . . . . Helpers . . . . . . . . .##
	// ############################################

	private void insert(int pos, T... items) {
		List<Object> jList = Arrays.asList(toJArrayIfNeeded(items));
		list.addAll(pos, jList);
	}

	private int toIndex(Double index, int defaultValue) {
		return index == null ? defaultValue : normalizeIndex(castToInt(index));
	}

	private int normalizeIndex(int index) {
		if (index >= 0)
			return Math.min(index, list.size());
		else
			return Math.max(list.size() + index, 0);
	}

	public T[] valueOf() {
		return toJsArray();
	}

	private T[] toJsArray() {
		return toJsArrayIfNeeded(list.toArray());
	}

	private static native int castToInt(Double index) /*-{
		return index;
	}-*/;

}

@JsFunction
interface CompratorFunction<T> {
	int compare(T t1, T t2);
}