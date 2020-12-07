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
import java.lang.reflect.Constructor;

public class SolrQueryUtilsTest {

    @Test(expected = Exception.class)
    public void testConstructor() throws Exception {
        Constructor<SolrQueryUtils> constructor = null;
        try {
            constructor = SolrQueryUtils.class.getDeclaredConstructor();
            assertFalse("Utils class constructor should be not accessible", constructor.isAccessible());
            //Added reflection only to achieve 100% coverage, in general it's not necessary.
            constructor.setAccessible(true);
            constructor.newInstance((Object[]) null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEscapeQueryChars() {
        final String testValue = "Characters_to_escape_\\,_+,_-,_!,_(,_),_:,_^,_[,_],_\",_{,_},_~,_*,_?,_|,_&,_;,_/, ";
        final String expectedValue = "Characters_to_escape_\\\\,_\\+,_\\-,_\\!,_\\(,_\\),_\\:,_\\^,_\\[,_\\],_\\\",_\\{,_\\},_\\~,_\\*,_\\?,_\\|,_\\&,_\\;,_\\/,\\ ";

        assertEquals(expectedValue, SolrQueryUtils.escapeQueryChars(testValue));
    }

    @Test
    public void testEscapeQueryCharsWithEmptyValue() {
        final String testValue = "";
        final String expectedValue = "";

        assertEquals(expectedValue, SolrQueryUtils.escapeQueryChars(testValue));
    }

    @Test(expected = NullPointerException.class)
    public void testEscapeQueryCharsWithNullValue() {
        SolrQueryUtils.escapeQueryChars(null);
    }
}