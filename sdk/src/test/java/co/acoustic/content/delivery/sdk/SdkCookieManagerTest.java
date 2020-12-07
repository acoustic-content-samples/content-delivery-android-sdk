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

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import static org.junit.Assert.*;

public class SdkCookieManagerTest {
    @Test
    public void testSaveFromResponse() {
        final List<Cookie> baseCookies = new ArrayList();
        baseCookies.add(
                new Cookie.Builder()
                        .domain("my7.test.com")
                        .name("test-cookie")
                        .value("test-test-test-test")
                        .build());
        final SdkCookieManager cookieManager = new SdkCookieManager();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("my7.test.com")
                .build();

        cookieManager.saveFromResponse(url, baseCookies);

        assertFalse(cookieManager.isEmpty());
        assertEquals(baseCookies, cookieManager.loadForRequest(url));
    }

    @Test
    public void testSaveLoadEmptyCookie() {
        final SdkCookieManager cookieManager = new SdkCookieManager();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("my7.test.com")
                .build();
        assertTrue(cookieManager.loadForRequest(url).isEmpty());
    }

    @Test
    public void testClearCookie() {
        final List<Cookie> baseCookies = new ArrayList();
        baseCookies.add(
                new Cookie.Builder()
                        .domain("my7.test.com")
                        .name("test-cookie")
                        .value("test-test-test-test")
                        .build());
        final SdkCookieManager cookieManager = new SdkCookieManager();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("my7.test.com")
                .build();

        cookieManager.saveFromResponse(url, baseCookies);
        assertFalse(cookieManager.isEmpty());

        cookieManager.clear();
        assertTrue(cookieManager.isEmpty());
    }

    @Test
    public void testHasCookie() {
        final SdkCookieManager cookieManager = new SdkCookieManager();
        assertFalse("When cookieManager was just created cookie should be null", cookieManager.hasCookie("test-coockie"));


        final List<Cookie> baseCookies = new ArrayList();
        baseCookies.add(
                new Cookie.Builder()
                        .domain("my7.test.com")
                        .name("test-cookie")
                        .value("test-test-test-test")
                        .build());


        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("my7.test.com")
                .build();

        cookieManager.saveFromResponse(url, baseCookies);
        assertTrue("added cookie should be present.", cookieManager.hasCookie("test-cookie"));
        assertFalse("new cookie should be present.", cookieManager.hasCookie("new-cookie"));
    }
}