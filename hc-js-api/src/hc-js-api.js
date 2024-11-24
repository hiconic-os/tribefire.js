import '@dev.hiconic/tf.js_tf-js'; // needed as this import is also written in the .d.ts and thus contents of T/hc are visible
import { initHcJs } from '@dev.hiconic/tf.js_tf-js'; // this import won't be written in .d.ts
import { T, hc } from '@dev.hiconic/hc-js-base';
initHcJs(T, hc);
export { T, hc };
// Namespaces
export var attr = hc.attr;
export var async = hc.async;
export var error = hc.error;
export var eval_ = hc.eval;
export var i18n = hc.i18n;
export var manipulation = hc.manipulation;
export var math = hc.math;
export var metadata = hc.metadata;
export var modelpath = hc.modelpath;
export var query = hc.query;
export var reason = hc.reason;
export var reflection = hc.reflection;
export var remote = hc.remote;
export var resources = hc.resources;
export var service = hc.service;
export var session = hc.session;
export var time = hc.time;
export var util = hc.util;
// TC builder
export var tc = hc.tc;
export var lang;
(function (lang) {
    lang.Function = hc.Function;
    // Classes
    lang.BinaryOperator = hc.BinaryOperator;
    lang.Byte = hc.Byte;
    lang.Character = hc.Character;
    lang.Class = hc.Class;
    lang.Collector = hc.Collector;
    lang.CollectorCharacteristics = hc.Collector$Characteristics;
    lang.Comparator = hc.Comparator;
    lang.Date = hc.Date;
    lang.Enum = hc.Enum;
    lang.Exception = hc.Exception;
    lang.Float = hc.Float;
    lang.Integer = hc.Integer;
    lang.Long = hc.Long;
    lang.Optional = hc.Optional;
    lang.Predicate = hc.Predicate;
    lang.RuntimeException = hc.RuntimeException;
    lang.Stack = hc.Stack;
    lang.StackTraceElement = hc.StackTraceElement;
    lang.Stream = hc.Stream;
    lang.Throwable = hc.Throwable;
    lang.UnaryOperator = hc.UnaryOperator;
    lang.Void = hc.Void;
})(lang || (lang = {}));
export var io;
(function (io) {
    io.InputStream = hc.InputStream;
    io.OutputStream = hc.OutputStream;
})(io || (io = {}));
T.Array = function createArray(e) { return hc.util.createArrayish(e); };
T.Set = function createSet(e) { return hc.util.createSetish(e); };
T.Map = function createMap(k, v) { return hc.util.createMapish(k, v); };
