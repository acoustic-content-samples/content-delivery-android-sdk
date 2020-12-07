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

import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class APICallResultTester<DocumentType extends Document> {
    private final MockWebServer mockWebServer;
    private final Documents<DocumentType> liveDataDocuments;
    private final Documents<DocumentType> mockDataDocuments;
    private final String mockResponseFileName;
    private final String testMethodName;
    private final ValidateDocument<DocumentType> validateDocument;

    APICallResultTester(String testMethodName,
                        MockWebServer mockWebServer,
                        Documents<DocumentType> liveDataDocuments,
                        Documents<DocumentType> mockDataDocuments,
                        String mockResponseFileName,
                        ValidateDocument<DocumentType> validateDocument) {
        this.mockWebServer = mockWebServer;
        this.liveDataDocuments = liveDataDocuments;
        this.mockDataDocuments = mockDataDocuments;
        this.mockResponseFileName = mockResponseFileName;
        this.testMethodName = testMethodName;
        this.validateDocument = validateDocument;
    }

    void apiCallTest() {
        List<DocumentType> result = new TestCall<DocumentType>(mockWebServer).makeCall(
                liveDataDocuments,
                mockDataDocuments,
                mockResponseFileName
        ).getDocuments();

        System.err.println(testMethodName + ": apiCallTest : result isEmpty = " + result.isEmpty());

        for (DocumentType document: result) {
            validateDocument.validateDocument(document);
        }
    }

    interface ValidateDocument<DocumentType extends Document> {
        void validateDocument(DocumentType document);
    }

    private static class TestCall<DocumentType extends Document> {
        private final String testScheme;

        private final MockWebServer mockWebServer;
        private DeliverySearchResult<DocumentType> result;

        TestCall(MockWebServer mockWebServer) {
            this(mockWebServer, null);
        }

        TestCall(MockWebServer mockWebServer, String testScheme) {
            this.mockWebServer = mockWebServer;
            this.testScheme = testScheme;
        }

        /**
         * Depends on the useMockResponses parameters makes call to real remote server, or uses mock server.
         *
         * @return the instance of {@link RetrofitBlockingCall.Result} that represents received response.
         */
        DeliverySearchResult<DocumentType> makeCall(Documents<DocumentType> liveDataDocuments,
                                                    Documents<DocumentType> mockDataDocuments,
                                                    String mockResponseFileName) throws RuntimeException {
            result = null;
            String scheme = (testScheme == null)
                    ? SystemPropertiesUtils.getScheme()
                    : testScheme;

            if (TestSystemProperties.TestScheme.MOCK.equals(scheme)) {
                final MockResponse response = new MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(ResourceReader.read(mockResponseFileName));

                mockWebServer.enqueue(response);
                mockDataDocuments
                        .get()
                        .then(documentsResult -> result = documentsResult)
                        .error(error -> {
                            throw new RuntimeException(error.getMessage());
                        });
            } else if (TestSystemProperties.TestScheme.LIVE.equals(scheme)) {
                liveDataDocuments
                        .get()
                        .then(documentsResult -> result = documentsResult)
                        .error(error -> {
                            throw new RuntimeException(error.getMessage());
                        });
            }

            return result;
        }
    }
}