import '@dev.hiconic/tf.js_tf-js';
import { T, hc } from '@dev.hiconic/hc-js-base';
import * as types from '@dev.hiconic/hc-js-base';
export { T, hc };
export import attr = hc.attr;
export import async = hc.async;
export import error = hc.error;
export import eval_ = hc.eval;
export import i18n = hc.i18n;
export import manipulation = hc.manipulation;
export import math = hc.math;
export import metadata = hc.metadata;
export import model = hc.model;
export import modelpath = hc.modelpath;
export import query = hc.query;
export import reason = hc.reason;
export import reflection = hc.reflection;
export import remote = hc.remote;
export import resources = hc.resources;
export import service = hc.service;
export import session = hc.session;
export import time = hc.time;
export import util = hc.util;
export import tc = hc.tc;
export declare namespace lang {
    export import BiConsumer = hc.BiConsumer;
    export import BiFunction = hc.BiFunction;
    export import CharSequence = hc.CharSequence;
    export import Collection = hc.Collection;
    export import Comparable = hc.Comparable;
    export import Consumer = hc.Consumer;
    export import Function = hc.Function;
    export import Iterable = hc.Iterable;
    export import Iterator = hc.Iterator;
    export import ListIterator = hc.ListIterator;
    export import MapEntry = hc.Map$Entry;
    export import Supplier = hc.Supplier;
    export import List = hc.List;
    export import Map = hc.Map;
    export import Set = hc.Set;
    export import BinaryOperator = hc.BinaryOperator;
    export import Byte = hc.Byte;
    export import Character = hc.Character;
    export import Class = hc.Class;
    export import Collector = hc.Collector;
    export import CollectorCharacteristics = hc.Collector$Characteristics;
    export import Comparator = hc.Comparator;
    export import Date = hc.Date;
    export import Enum = hc.Enum;
    export import Exception = hc.Exception;
    export import Float = hc.Float;
    export import Integer = hc.Integer;
    export import Long = hc.Long;
    export import Optional = hc.Optional;
    export import Predicate = hc.Predicate;
    export import RuntimeException = hc.RuntimeException;
    export import Stack = hc.Stack;
    export import StackTraceElement = hc.StackTraceElement;
    export import Stream = hc.Stream;
    export import Throwable = hc.Throwable;
    export import UnaryOperator = hc.UnaryOperator;
    export import Void = hc.Void;
}
export declare namespace io {
    export import InputStream = hc.InputStream;
    export import OutputStream = hc.OutputStream;
}
declare module "@dev.hiconic/hc-js-base" {
    namespace hc.reflection {
        interface BaseType {
            S: "object";
        }
        interface BooleanType {
            S: "boolean";
        }
        interface StringType {
            S: "string";
        }
        interface IntegerType {
            S: "integer";
        }
        interface LongType {
            S: "long";
        }
        interface FloatType {
            S: "float";
        }
        interface DoubleType {
            S: "double";
        }
        interface DecimalType {
            S: "decimal";
        }
        interface DateType {
            S: "date";
        }
    }
    type VAL_TYPE<T extends reflection.GenericModelType> = T extends reflection.EntityType<infer E> ? E : T extends reflection.EnumType<infer E> ? E : T extends reflection.BooleanType ? boolean : T extends reflection.StringType ? string : T extends reflection.IntegerType ? types.integer : T extends reflection.LongType ? types.long : T extends reflection.FloatType ? types.float : T extends reflection.DoubleType ? types.double : T extends reflection.DecimalType ? types.decimal : T extends reflection.DateType ? types.date : T extends reflection.BaseType ? any : never;
    namespace T {
        const Array: {
            new <E extends reflection.GenericModelType = reflection.BaseType>(e?: E): T.Array<VAL_TYPE<E>>;
        };
        const Set: {
            new <E extends reflection.GenericModelType = reflection.BaseType>(e?: E): T.Set<VAL_TYPE<E>>;
        };
        const Map: {
            new <K extends reflection.GenericModelType = reflection.BaseType, V extends reflection.GenericModelType = reflection.BaseType>(k?: K, v?: V): T.Map<VAL_TYPE<K>, VAL_TYPE<V>>;
        };
    }
}
