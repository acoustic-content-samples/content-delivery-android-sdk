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

import android.os.Parcel;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import static org.junit.Assert.*;

public class ParcelableUtilsTest {

    @Test(expected = Exception.class)
    public void testConstructor() throws Exception {
        Constructor<ParcelableUtils> constructor = null;
        try {
            constructor = ParcelableUtils.class.getDeclaredConstructor();
            assertFalse("Utils class constructor should be not accessible", constructor.isAccessible());
            //Added reflection only to achieve 100% coverage, in general it's not necessary.
            constructor.setAccessible(true);
            constructor.newInstance((Object[]) null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testReadAndWriteArrayListOfStrings() {
        final ArrayList<String> source = new ArrayList<>();
        source.add("Test1");
        source.add("Test Name");
        source.add("Anybody");

        Parcel parcel = MockParcel.obtain();
        ParcelableUtils.writeArrayListOfStrings(source, parcel);
        parcel.setDataPosition(0);

        final ArrayList<String> result = ParcelableUtils.readArrayListOfStrings(parcel);
        assertArrayEquals(source.toArray(new String[0]), result.toArray(new String[0]));
    }

    @Test
    public void testReadAndWriteNullArrayListOfStrings() {
        Parcel parcel = MockParcel.obtain();
        ParcelableUtils.writeArrayListOfStrings(null, parcel);
        parcel.setDataPosition(0);
        assertNull(ParcelableUtils.readArrayListOfStrings(parcel));
    }

    @Test
    public void testReadAndWriteArrayListOfBoolean() {
        final ArrayList<Boolean> source = new ArrayList<>();
        source.add(true);
        source.add(false);
        source.add(true);

        Parcel parcel = MockParcel.obtain();
        ParcelableUtils.writeArrayListOfBoolean(source, parcel);
        parcel.setDataPosition(0);

        final ArrayList<Boolean> result = ParcelableUtils.readArrayListOfBoolean(parcel);
        assertArrayEquals(source.toArray(new Boolean[0]), result.toArray(new Boolean[0]));
    }

    @Test
    public void testReadAndWriteNullArrayListOfBoolean() {
        Parcel parcel = MockParcel.obtain();
        ParcelableUtils.writeArrayListOfBoolean(null, parcel);
        parcel.setDataPosition(0);
        assertNull(ParcelableUtils.readArrayListOfBoolean(parcel));
    }

    @Test
    public void testReadAndWriteArrayListOfDate() {
        final ArrayList<Date> source = new ArrayList<>();
        source.add(new Date());
        source.add(new Date());

        Parcel parcel = MockParcel.obtain();
        ParcelableUtils.writeArrayListOfDate(source, parcel);
        parcel.setDataPosition(0);

        final ArrayList<Date> result = ParcelableUtils.readArrayListOfDate(parcel);
        assertArrayEquals(source.toArray(new Date[0]), result.toArray(new Date[0]));
    }

    @Test
    public void testReadAndWriteNullArrayListOfDate() {
        Parcel parcel = MockParcel.obtain();
        ParcelableUtils.writeArrayListOfDate(null, parcel);
        parcel.setDataPosition(0);
        assertNull(ParcelableUtils.readArrayListOfDate(parcel));
    }

    @Test
    public void testReadAndWriteArrayListOfDouble() {
        final ArrayList<Double> source = new ArrayList<>();
        source.add(18.6);
        source.add(1233.44);
        source.add(-32.44);

        Parcel parcel = MockParcel.obtain();
        ParcelableUtils.writeArrayListOfDouble(source, parcel);
        parcel.setDataPosition(0);

        final ArrayList<Double> result = ParcelableUtils.readArrayListOfDouble(parcel);
        assertArrayEquals(source.toArray(new Double[0]), result.toArray(new Double[0]));
    }

    @Test
    public void testReadAndWriteNullArrayListOfDouble() {
        Parcel parcel = MockParcel.obtain();
        ParcelableUtils.writeArrayListOfDouble(null, parcel);
        parcel.setDataPosition(0);
        assertNull(ParcelableUtils.readArrayListOfDouble(parcel));
    }

    @Test
    public void testJSONObject() throws Throwable {
        String testJson = "{\"phonetype\":\"N95\",\"cat\":\"WP\"}";

        JSONObject source = new JSONObject(testJson);
        Parcel parcel = MockParcel.obtain();
        ParcelableUtils.writeJSONObject(source, parcel);
        parcel.setDataPosition(0);

        JSONObject result = ParcelableUtils.readJSONObject(parcel);
        assertEquals(source.toString(), result.toString());
    }

    @Test
    public void testNullJSONObject() {
        Parcel parcel = MockParcel.obtain();
        ParcelableUtils.writeJSONObject(null, parcel);
        parcel.setDataPosition(0);
        assertNull(ParcelableUtils.readJSONObject(parcel));
    }
}