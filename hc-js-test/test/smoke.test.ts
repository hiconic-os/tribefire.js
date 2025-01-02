import { describe, expect, it } from 'vitest'

import { hc } from '@dev.hiconic/tf.js_hc-js-api';

import * as rootM from '@dev.hiconic/gm_root-model';
import * as testM from '@dev.hiconic/gm_test-model';


/**
 * Test the most fundamental functionality.
 */
describe('hiconic.js smoke test', () => {

    it('supports EntityType literals', () => {
        const geType = rootM.GenericEntity;

        expect(geType).toBeDefined();
        expect(geType.getShortName()).toBe('GenericEntity');
        expect(geType.getTypeSignature()).toBeDefined();

        const props = geType.getProperties();
        expect(props).toBeDefined();
        expect(props.size()).toBe(3);
    })

    it('supports GenericModelTypeReflection', () => {
        const typeReflection = hc.reflection.typeReflection();

        const geSignature = rootM.GenericEntity.getTypeSignature();

        const geType = typeReflection.findEntityType(geSignature);
        expect(geType).toBe(rootM.GenericEntity);
    })

    it('supports simple property manipulation', () => {
        const person = testM.Person.create();

        person.firstName = "John"
        person.lastName = "Smith"

        expect(person.firstName).toBe("John");
        expect(person.lastName).toBe("Smith");
    })

    it('supports collection property manipulation', () => {
        const entity = testM.CollectionEntity.create();

        const strings = entity.stringSet;
        expect(strings).toBeDefined();
        expect(strings.size).toBe(0);

        strings
            .add("meat")
            .add("veggies")
            .add("cheese");

        expect(strings.size).toBe(3);
        expect([...strings]).toEqual(["meat", "veggies", "cheese"]);
    })

    it('supports shortened property reflection', () => {
        const PERSON_PROP_NAMES = [
            "address", "age", "children", "father", "firstName", "friends", "globalId", "id", "lastName", "mother", "partition"];

        const person = testM.Person.create();

        const props = person.Properties();
        const propNames = person.PropertyNames();

        expect(props).toBeDefined();
        expect(propNames).toBeDefined();

        expect(props.length).toBe(propNames.length);

        const propNames_ = props.map(p => p.getName());

        // This comparison would still pass even if new properties were added to Person
        expect(propNames).toEqual(expect.arrayContaining(PERSON_PROP_NAMES))
        expect(propNames_).toEqual(expect.arrayContaining(PERSON_PROP_NAMES))

        console.log(propNames);
        console.log(propNames_);

        expect(propNames).toEqual(propNames_)
    })

})
