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
import android.os.Parcelable;

import org.mockito.stubbing.Answer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockParcel {

    public static Parcel obtain() {
        return new MockParcel().mParcel;
    }

    private int mPosition = 0;
    private List<Object> mStore = new LinkedList<>();
    private Parcel mParcel = mock(Parcel.class);

    private MockParcel() {
        setupWrites();
        setupReads();
        setupOthers();
    }

    // uncomment when needed for the first time
    private void setupWrites() {
        final Answer<Object> answer = i -> {
            final Object arg = i.getArgument(0);
            mStore.add(arg);
            return arg;
        };
        doAnswer(answer).when(mParcel).writeByte(anyByte());
        doAnswer(answer).when(mParcel).writeInt(anyInt());
        doAnswer(answer).when(mParcel).writeString(anyString());
        doAnswer(answer).when(mParcel).writeParcelable(any(Parcelable.class), anyInt());
        doAnswer(answer).when(mParcel).writeBoolean(anyBoolean());
        doAnswer(answer).when(mParcel).writeSerializable(any(Class.class));
        doAnswer(answer).when(mParcel).writeSerializable(any(Integer.class));
        doAnswer(answer).when(mParcel).writeSerializable(any(Boolean.class));
        doAnswer(answer).when(mParcel).writeSerializable(any(Date.class));
        doAnswer(answer).when(mParcel).writeSerializable(any(Double.class));
        doAnswer(answer).when(mParcel).writeLong(anyLong());
        // doAnswer(answer).when(mParcel).writeFloat(anyFloat());
        // doAnswer(answer).when(mParcel).writeDouble(anyDouble());
    }

    // uncomment when needed for the first time
    private void setupReads() {
        final Answer<Object> answer = i -> mStore.get(mPosition++);
        when(mParcel.readByte()).thenAnswer(answer);
        when(mParcel.readInt()).thenAnswer(answer);
        when(mParcel.readString()).thenAnswer(answer);
        when(mParcel.readParcelable(any(ClassLoader.class))).then(answer);
        when(mParcel.readBoolean()).thenAnswer(answer);
        when(mParcel.readSerializable()).thenAnswer(answer);
         when(mParcel.readLong()).thenAnswer(answer);
        // when(mParcel.readFloat()).thenAnswer(answer);
        // when(mParcel.readDouble()).thenAnswer(answer);
    }

    private void setupOthers() {
        doAnswer(i -> {
            mPosition = i.getArgument(0);
            return null;
        }).when(mParcel).setDataPosition(anyInt());
    }

}