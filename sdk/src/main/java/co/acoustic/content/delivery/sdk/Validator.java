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

import androidx.annotation.Nullable;

/**
 * Contains validation methods.
 */
final class Validator {

    private Validator() {
    }

    /**
     * Check whether passed object is null. If it's null throws exception.
     *
     * @param object  an object reference.
     * @param message the exception message to use if the check fails; will be converted to a string using String.valueOf(Object).
     * @param <T>     the reference type.
     * @return the non-null reference that was validated.
     * @throws NullPointerException if passed object not null.
     */
    static <T> T checkNotNull(T object, @Nullable String message) {
        if (object == null) {
            throw new NullPointerException(String.valueOf(message));
        }
        return object;
    }

    /**
     * Check whether passed object is null. If it's null throws exception.
     *
     * @param object an object reference.
     * @param <T>    the reference type.
     * @return the non-null reference that was validated.
     * @throws NullPointerException if passed object not null.
     */
    static <T> T checkNotNull(T object) {
        return checkNotNull(object, "Object cannot be null");
    }

    /**
     * Check whether passed object satisfies require condition.
     *
     * @param object    an object reference.
     * @param message   the exception message to use if the check fails; will be converted to a string using String.valueOf(Object).
     * @param predicate the {@link Predicate} implementation responsible for condition check.
     * @param <T>       the reference type.
     * @return the non-null reference that was validated.
     * @throws IllegalArgumentException if passed object not not satisfies condition.
     */
    static <T> T checkCondition(T object, @Nullable String message, Predicate<T> predicate) {
        checkNotNull(predicate);
        checkNotNull(object);
        if (!predicate.check(object)) {
            throw new IllegalArgumentException(String.valueOf(message));
        }
        return object;
    }

    interface Predicate<T> {

        boolean check(T value);

    }
}