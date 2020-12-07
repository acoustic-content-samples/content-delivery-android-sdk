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

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {
    @SerializedName("numFound")
    @Expose
    private Integer numFound;
    @SerializedName("documents")
    @Expose
    private List<TestDocument> documents = null;

    public Integer getNumFound() {
        return numFound;
    }

    public void setNumFound(Integer numFound) {
        this.numFound = numFound;
    }

    public List<TestDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<TestDocument> documents) {
        this.documents = documents;
    }
}