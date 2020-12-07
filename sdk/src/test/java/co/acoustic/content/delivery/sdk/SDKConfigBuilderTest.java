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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SDKConfigBuilderTest {

    @Test(expected = NullPointerException.class)
    public void testSetNullAPIUrl() {
        new SDKConfig.Builder().setApiUrl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyAPIUrl() {
        new SDKConfig.Builder().setApiUrl("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMalformedAPIUrl() {
        new SDKConfig.Builder().setApiUrl("p://");
    }

    @Test
    public void testSetAPIUrl() {
        final String apiUrl = "http://test.blah.com/";
        final String previewApiUrl = "http://test-preview.blah.com/";

        final SDKConfig.Builder builder = new SDKConfig.Builder().setApiUrl(apiUrl);

        assertEquals("API URLs should be equal", apiUrl, builder.getApiUrl().toString());
        assertEquals("Preview API URLs should be of expected format", previewApiUrl, builder.getPreviewApiUrl().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildSDKNotValidConfig() {
        new SDKConfig.Builder().build();
    }

    @Test
    public void testBuildSDKValidConfig() {
        assertNotNull(
                "Builder should never return null SDKConfig",
                new SDKConfig.Builder().setApiUrl("http://test.blah.com/").build()
        );
    }
}