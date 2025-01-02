import { beforeAll, describe, expect, it } from 'vitest'

import { hc } from '@dev.hiconic/tf.js_hc-js-api';

import * as manM from '@dev.hiconic/gm_manipulation-model';
import * as ownerM from '@dev.hiconic/gm_owner-model';
import * as rootM from '@dev.hiconic/gm_root-model';
import * as testM from '@dev.hiconic/gm_test-model';

describe('manipulation tracking tests', () => {

    let mans: manM.Manipulation[];

    const listener: hc.manipulation.ManipulationListener = {
        onMan: (manipulation: manM.Manipulation) => {
            mans.push(manipulation);
        }
    };

    beforeAll(() => {
        mans = [];
    })

    it('tracks basic manipulations', () => {
        const session = new hc.session.BasicManagedGmSession();

        session.listeners().add(listener);

        const person = session.create(testM.Person);
        person.firstName = "John"

        expectInstantiation(0, testM.Person);
        expectChangeValue(1, person, "firstName", "John");
    })

    function expectInstantiation(index: number, type: hc.reflection.EntityType<any>) {
        const man = expectManipulationType(index, manM.InstantiationManipulation);

        const entity = man.entity!;
        expect(entity).toBeDefined();
        expect(entity.EntityType()).toBe(type);
    }

    function expectChangeValue(index: number, xpEntity: rootM.GenericEntity, xpPropertyName: string, xpValue: hc.Base) {
        const man = expectManipulationType(index, manM.ChangeValueManipulation);

        const newValue = man.newValue;
        const propertyName = man.owner!.propertyName
        const entity = (man.owner as ownerM.LocalEntityProperty).entity;

        expect(newValue).toBe(xpValue);
        expect(propertyName).toBe(xpPropertyName);
        expect(entity).toBe(xpEntity);
    }

    function expectManipulationType<M extends manM.Manipulation>(index: number, manType: hc.reflection.EntityType<M>): M {
        expect(mans.length, "Too few manipulations").toBeGreaterThan(index);

        const man = mans[index];
        expect(man).toBeDefined();
        expect(man.EntityType()).toBe(manType);

        return man as M;
    }

})