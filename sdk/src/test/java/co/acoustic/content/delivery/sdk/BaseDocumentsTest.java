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

/**
 * Contains comment instruments for testing {@link Documents} class.
 **/
abstract class BaseDocumentsTest {
    protected static class DocumentsTestImpl extends Documents<DocumentTestImpl> {

        DocumentsTestImpl(@NonNull DeliverySearch deliverySearch, @Nullable State state) {
            super(deliverySearch, state);
        }

        DocumentsTestImpl(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder deliverySearchQueryBuilder) {
            super(deliverySearch, state, deliverySearchQueryBuilder);
        }

        public DocumentsTestImpl(@NonNull DeliverySearch deliverySearch, @Nullable State state, @NonNull DeliverySearchQueryBuilder deliverySearchQueryBuilder, @NonNull NetworkingCallsExecutor callsExecutor) {
            super(deliverySearch, state, deliverySearchQueryBuilder, callsExecutor);
        }

        @NonNull
        @Override
        String getTargetDocumentClassification() {
            return "test";
        }

        @NonNull
        @Override
        DocumentTestImpl createDocument(@NonNull DeliverySearchResponseDocument rawDoc) {
            return new DocumentTestImpl(rawDoc);
        }

        @NonNull
        @Override
        Documents<DocumentTestImpl> createInstance(@NonNull DeliverySearch deliverySearch, @NonNull DeliverySearchQueryBuilder queryBuilder) {
            return new DocumentsTestImpl(deliverySearch, null, queryBuilder);
        }
    }

    protected static class DocumentTestImpl extends Document {

        DocumentTestImpl(DeliverySearchResponseDocument rawDoc) {
            super(rawDoc);
        }

        DocumentTestImpl(Parcel source) {
            super(source);
        }
    }
}