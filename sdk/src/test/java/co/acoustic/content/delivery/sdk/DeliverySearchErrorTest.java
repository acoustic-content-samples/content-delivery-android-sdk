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

import org.junit.Test;

import static org.junit.Assert.*;

public class DeliverySearchErrorTest {

    @Test
    public void testEquals() {
        /*
         * Test fully equals objects
         */
        assertEquals(createDeliverySearchInstance(), createDeliverySearchInstance());

        DeliverySearchError instance1_1 = new DeliverySearchError();
        instance1_1.code = 403;
        DeliverySearchError instance1_2 = new DeliverySearchError();
        instance1_2.code = 404;
        assertNotEquals(
                "Two objects with different codes, should not be equals",
                instance1_1,
                instance1_2
        );

        DeliverySearchError instance2_1 = new DeliverySearchError();
        instance2_1.description = "test description 1";
        DeliverySearchError instance2_2 = new DeliverySearchError();
        instance2_2.description = "test description 2";
        assertNotEquals(
                "Two objects with different descriptions, should not be equals",
                instance2_1,
                instance2_2
        );

        DeliverySearchError instance3_1 = new DeliverySearchError();
        instance3_1.message = "test message 1";
        DeliverySearchError instance3_2 = new DeliverySearchError();
        instance3_2.description = "test message 2";
        assertNotEquals(
                "Two objects with different messages, should not be equals",
                instance3_1,
                instance3_2
        );
    }

    @Test
    public void testHashCode() {
        assertEquals("hashCode() same for equal objects.", createDeliverySearchInstance(), createDeliverySearchInstance());

        DeliverySearchError instance1_1 = new DeliverySearchError();
        instance1_1.code = 403;
        DeliverySearchError instance1_2 = new DeliverySearchError();
        instance1_2.code = 404;
        assertNotEquals(
                "hashCode() should be different for objects with different codes.",
                instance1_1.hashCode(),
                instance1_2.hashCode()
        );

        DeliverySearchError instance2_1 = new DeliverySearchError();
        instance2_1.description = "test description 1";
        DeliverySearchError instance2_2 = new DeliverySearchError();
        instance2_2.description = "test description 2";
        assertNotEquals(
                "hashCode() should be different for objects with different descriptions.",
                instance2_1.hashCode(),
                instance2_2.hashCode()
        );

        DeliverySearchError instance3_1 = new DeliverySearchError();
        instance3_1.message = "test message 1";
        DeliverySearchError instance3_2 = new DeliverySearchError();
        instance3_2.description = "test message 2";
        assertNotEquals(
                "hashCode() should be different for objects with different messages.",
                instance3_1.hashCode(),
                instance3_2.hashCode()
        );
    }

    @Test
    public void testToString() {
        DeliverySearchError error = createDeliverySearchInstance();
        assertEquals(createToString(error), error.toString());
    }

    private DeliverySearchError createDeliverySearchInstance() {
        DeliverySearchError instance = new DeliverySearchError();
        instance.code = 403;
        instance.description = "Test Error";
        instance.message = "Test Message";
        return instance;
    }

    private String createToString(DeliverySearchError instance) {
        return "DeliverySearchError{" +
                "code=" + instance.code +
                ", description='" + instance.description + '\'' +
                ", message='" + instance.message + '\'' +
                '}';
    }
}