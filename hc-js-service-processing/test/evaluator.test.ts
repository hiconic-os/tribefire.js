import { beforeAll, describe, expect, it } from 'vitest'

import { LocalEvaluator } from '../src/evaluator.js';

import { StringComputingRequest } from '@dev.hiconic/gm_test-model';
import { reason } from '@dev.hiconic/tf.js_hc-js-api';

import Maybe = reason.Maybe

describe('Local DDSA Evaluator tests', () => {

    let evaluator: LocalEvaluator;

    beforeAll(() => {
        evaluator = new LocalEvaluator();
    })

    it('tests basic binding', async () => {
        evaluator.bindFunction(StringComputingRequest, async (_ctx, req) => {
            return Maybe.complete("Hello " + req.arg1 + "." + req.arg2);
        });

        const req = StringComputingRequest.create();
        req.arg1 = "a";
        req.arg2 = "b";

        const resultMaybe = await req.EvalAndGetReasoned(evaluator)
        expect(resultMaybe.isSatisfied()).toBe(true)
        expect(resultMaybe.get()).toBe("Hello a.b");

        const resultMaybe2 = await req.Eval(evaluator).andGetReasoned()
        expect(resultMaybe2.isSatisfied()).toBe(true)
        expect(resultMaybe2.get()).toBe("Hello a.b");

        const result = await req.EvalAndGet(evaluator)
        expect(result).toBe("Hello a.b");

        const result2 = await req.Eval(evaluator).andGet()
        expect(result2).toBe("Hello a.b");
    })

})