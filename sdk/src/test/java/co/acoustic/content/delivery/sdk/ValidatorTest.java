/*
 * Copyright 2020 Acoustic, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Apache License, Version 2.0
 * www.apache.org
 * Home page of The Apache Software Foundation
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package co.acoustic.content.delivery.sdk;

import android.text.TextUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorTest {

    @Test(expected = NullPointerException.class)
    public void testPassNullToCheckNotNullMethod() {
        Validator.checkNotNull(null, "Object cannot be null");
    }

    @Test(expected = NullPointerException.class)
    public void testPassNullToCheckNotNullMethodWithOneArgument() {
        Validator.checkNotNull(null);
    }

    @Test
    public void testPassObjectToCheckNotNullMethod() {
        Object testObject = new Object();

        Object validateObject = Validator.checkNotNull(testObject, "testObject cannot be null");
        assertSame("testObject and validateObject should be the same reference", testObject, validateObject);
    }

    @Test
    public void testPassObjectToCheckNotNullMethodWithOneArgument() {
        Object testObject = new Object();

        Object validateObject = Validator.checkNotNull(testObject);
        assertSame("testObject and validateObject should be the same reference", testObject, validateObject);
    }

    @Test(expected = NullPointerException.class)
    public void testPassNullPredicateToCheckConditionMethod() {
        String testString = "test";
        Validator.checkCondition(testString, "testString cannot be empty", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotAppropriateValueToCheckConditionMethod() {
        String testString = "";
        Validator.checkCondition(testString, "testString cannot be empty", str -> !TextUtils.isEmpty(str));
    }

    @Test
    public void testAppropriateValueToCheckConditionMethod() {
        String testString = "Test";
        Validator.checkCondition(testString, "testString cannot be empty", str -> !TextUtils.isEmpty(str));
    }
}