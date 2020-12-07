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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Type;

public class JSONObjectJsonDeserializerTest {

    @Test
    public void testDeserialize() {
        final JSONObjectJsonDeserializer deserializer = new JSONObjectJsonDeserializer();

        final String jsonString = ResourceReader.read("assets_default_success_response.json");
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        JsonElement json = mock(JsonElement.class);
        when(json.getAsJsonObject()).thenReturn(jsonObject);

        JSONObject result = deserializer.deserialize(json, mock(Type.class), mock(JsonDeserializationContext.class));
        assertNotNull(result);
    }

    @Test (expected = JsonParseException.class)
    public void testDeserializeCrash() {
        final JSONObjectJsonDeserializer deserializer = new JSONObjectJsonDeserializer();

        JsonElement json = mock(JsonElement.class);
        when(json.getAsJsonObject()).thenThrow(new NullPointerException("TestException"));

        JSONObject result = deserializer.deserialize(json, mock(Type.class), mock(JsonDeserializationContext.class));
    }
}