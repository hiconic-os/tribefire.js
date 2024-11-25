import '@dev.hiconic/tf.js_tf-js'; // needed as this import is also written in the .d.ts and thus contents of T/hc are visible
import { initHcJs } from '@dev.hiconic/tf.js_tf-js'; // this import won't be written in .d.ts

import { T, hc, VAL_TYPE } from '@dev.hiconic/hc-js-base';

initHcJs(T, hc);

export { T, hc };

// Namespaces
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

// TC builder
export import tc = hc.tc;

export namespace lang {
    // Interfaces
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

    // Classes
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

export namespace io {
    export import InputStream = hc.InputStream;
    export import OutputStream = hc.OutputStream;
}

declare module "@dev.hiconic/hc-js-base" {

    // Shortcuts for common types
    type GenericModelType = reflection.GenericModelType;
    type BaseType =  reflection.BaseType;
    type EnumType<E extends hc.Enum<E>> = reflection.EnumType<E>;
    type EntityType<E extends T.com.braintribe.model.generic.GenericEntity> = reflection.EntityType<E>;

    type GenericEntity = T.com.braintribe.model.generic.GenericEntity;
    type Enum = hc.Enum<any>;

    // Useful types
    type SimpleValue = boolean | string | integer | long | float | double | decimal | date;
    type CollectionElement = SimpleValue | GenericEntity | Enum;

    // Ensure our reflection types have unique structure (so they aren't interchangable in structural typing)
    namespace hc.reflection {
        interface BaseType { z: "object"; }
        interface BooleanType { z: "boolean"; }
        interface StringType { z: "string"; }
        interface IntegerType { z: "integer"; }
        interface LongType { z: "long"; }
        interface FloatType { z: "float"; }
        interface DoubleType { z: "double"; }
        interface DecimalType { z: "decimal"; }
        interface DateType { z: "date"; }
    }

    // Map GenerciModelType to its value type
    type VAL_TYPE<T extends GenericModelType> =
        T extends reflection.EntityType<infer E> ? E :
        T extends reflection.EnumType<infer E> ? E :
        T extends reflection.BooleanType ? boolean :
        T extends reflection.StringType ? string :
        T extends reflection.IntegerType ? integer :
        T extends reflection.LongType ? long :
        T extends reflection.FloatType ? float :
        T extends reflection.DoubleType ? double :
        T extends reflection.DecimalType ? decimal :
        T extends reflection.DateType ? date :
        T extends reflection.BaseType ? CollectionElement :
        never;

    // Declare constructor functions for collections
    namespace T {
        const Array: {
            new <E extends GenericModelType = BaseType>(e?: E): T.Array<VAL_TYPE<E>>;
        };
        const Set: {
            new <E extends GenericModelType = BaseType>(e?: E): T.Set<VAL_TYPE<E>>;
        };
        const Map: {
            new <
                K extends GenericModelType = BaseType,
                V extends GenericModelType = BaseType
            >
                (k?: K, v?: V): T.Map<VAL_TYPE<K>, VAL_TYPE<V>>;
        };
    }

}

// Implement constructor functions for collections
(T as any).Array = function createArray(e: any) /**/ { return hc.util.createArrayish(e) };
(T as any).Set = function createSet(e: any) /******/ { return hc.util.createSetish(e) };
(T as any).Map = function createMap(k: any, v: any) { return hc.util.createMapish(k, v) };

