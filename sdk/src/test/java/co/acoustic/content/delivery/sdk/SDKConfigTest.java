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
import static org.junit.Assert.assertTrue;

public class SDKConfigTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithEmptyApiUrl() {
        SDKConfig.builder()
                .setApiUrl("")
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void testBuilderWithNullApiUrl() {
        SDKConfig.builder()
                .setApiUrl(null)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithApiUrlInNotAppropriateFormat() {
        SDKConfig.builder()
                .setApiUrl("testtesttest")
                .build();
    }

    @Test
    public void testApiUrlConstructorWithAppropriateValues() {
        String testApiUrl = "https://my7.test.com/";
        String testPreviewApiUrl = "https://my7-preview.test.com/";
        SDKConfig config = SDKConfig.builder()
                .setApiUrl(testApiUrl)
                .build();

        assertEquals("testApiUrl should be equal with an apiUrl from config", testApiUrl, config.getApiUrl().toString());
        assertEquals("testApiUrl should be equal with an apiUrl from config", testPreviewApiUrl, config.getPreviewApiUrl().toString());

        String testApiUrlNoBackSlashAtTheEnd = "https://my7.test.com";

        SDKConfig configNoBackSlashAtTheEndUrl = SDKConfig.builder()
                .setApiUrl(testApiUrlNoBackSlashAtTheEnd)
                .build();

        assertTrue("API URL should end with a back slash after it is set to builder", configNoBackSlashAtTheEndUrl.getApiUrl().toString().endsWith("/"));

    }
    @Test(expected = IllegalArgumentException.class)
    public void testBuildEmptyCredentialsConfig() {
        SDKConfig.builder().build();
    }

    @Test
    public void testGetBuilderForConfig() {
        SDKConfig.Builder builder = SDKConfig.builder();
        assertNotNull(builder);
    }
}