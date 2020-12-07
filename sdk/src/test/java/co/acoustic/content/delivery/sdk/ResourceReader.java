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

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Utility class that allows read text file from resources. Could be used for loading test json.
 */
class ResourceReader {

    private ResourceReader() {
    }

    static File getPathFile(String fileName) {
        ClassLoader classLoader = ResourceReader.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file;
    }

    static String read(String fileName) {
        InputStream in;
        String ret = null;
        try {
            in = ResourceReader.class.getClassLoader().getResourceAsStream(fileName);
            ret = readStream(in);
        } catch (IOException e) {
            Log.e("TEST", e.toString());
        }
        return ret;
    }

    private static String readStream(InputStream is) throws IOException {
        final char[] buffer = new char[0x10000];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, "UTF-8");
        try {
            int read;
            do {
                read = in.read(buffer, 0, buffer.length);
                if (read > 0) {
                    out.append(buffer, 0, read);
                }
            } while (read >= 0);
        } finally {
            in.close();
        }
        return out.toString();
    }
}