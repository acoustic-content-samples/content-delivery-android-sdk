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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

class ParcelableUtils {

    static void writeArrayListOfStrings(@Nullable ArrayList<String> target, @NonNull Parcel dest) {
        final int size = null == target ? -1 : target.size();
        dest.writeInt(size);
        if (null == target) {
            return;
        }
        for (int i = 0; i < size; i++) {
            dest.writeString(target.get(i));
        }
    }

    @Nullable
    static ArrayList<String> readArrayListOfStrings(@NonNull Parcel source) {
        ArrayList<String> result = null;
        final int size = source.readInt();
        if (size >= 0) {
            result = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                result.add(source.readString());
            }
        }
        return result;
    }

    static void writeArrayListOfBoolean(@Nullable ArrayList<Boolean> target, @NonNull Parcel dest) {
        final int size = null == target ? -1 : target.size();
        dest.writeInt(size);
        if (null == target) {
            return;
        }
        for (int i = 0; i < size; i++) {
            dest.writeSerializable(target.get(i));
        }
    }

    @Nullable
    static ArrayList<Boolean> readArrayListOfBoolean(@NonNull Parcel source) {
        ArrayList<Boolean> result = null;
        final int size = source.readInt();
        if (size >= 0) {
            result = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                result.add((Boolean) source.readSerializable());
            }
        }
        return result;
    }

    static void writeArrayListOfDate(@Nullable ArrayList<Date> target, @NonNull Parcel dest) {
        final int size = null == target ? -1 : target.size();
        dest.writeInt(size);
        if (null == target) {
            return;
        }
        for (int i = 0; i < size; i++) {
            dest.writeSerializable(target.get(i));
        }
    }

    @Nullable
    static ArrayList<Date> readArrayListOfDate(@NonNull Parcel source) {
        ArrayList<Date> result = null;
        final int size = source.readInt();
        if (size >= 0) {
            result = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                result.add((Date) source.readSerializable());
            }
        }
        return result;
    }

    static void writeArrayListOfDouble(@Nullable ArrayList<Double> target, @NonNull Parcel dest) {
        final int size = null == target ? -1 : target.size();
        dest.writeInt(size);
        if (null == target) {
            return;
        }
        for (int i = 0; i < size; i++) {
            dest.writeSerializable(target.get(i));
        }
    }

    @Nullable
    static ArrayList<Double> readArrayListOfDouble(@NonNull Parcel source) {
        ArrayList<Double> result = null;
        final int size = source.readInt();
        if (size >= 0) {
            result = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                result.add((Double) source.readSerializable());
            }
        }
        return result;
    }

    static void writeJSONObject(@Nullable JSONObject target, @NonNull Parcel dest) {
        dest.writeString(null == target ? "" : target.toString());
    }

    @Nullable
    static JSONObject readJSONObject(@NonNull Parcel source) {
        JSONObject result = null;
        try {
            result = new JSONObject(source.readString());
        } catch (Exception e) {
            // silent...
        }
        return result;
    }

    private ParcelableUtils() {
        throw new UnsupportedOperationException();
    }
}
