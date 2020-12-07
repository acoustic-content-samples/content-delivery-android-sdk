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

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds common properties for any document that can be retrieved using delivery search APIs.
 */
public abstract class Document implements Parcelable {

    Document(@NonNull DeliverySearchResponseDocument rawDoc) {
        classification = rawDoc.classification;
        created = rawDoc.created;
        creatorId = rawDoc.creatorId;
        description = rawDoc.description;
        id = rawDoc.id;
        keywords = rawDoc.keywords;
        lastModified = rawDoc.lastModified;
        lastModifierId = rawDoc.lastModifierId;
        locale = rawDoc.locale;
        name = rawDoc.name;
        restricted = rawDoc.restricted;
        tags = rawDoc.tags;
    }

    Document(Parcel source) {
        readFromParcel(source);
    }

    /**
     * This field describes the kind of item. The value that is returned can be "asset", "category", "content" or "taxonomy".
     */
    private String classification;

    /**
     * The creation date of the item.
     * <p>
     * Examples:
     * <ul>
     * <li>2020-01-07T17:52:31.610Z</li>
     * <li>2020-01-07T17:52:31.477Z</li>
     * <li>2020-01-07T17:52:31.828Z</li>
     * </ul>
     */
    private String created;

    /**
     * The UUID of the user that created the item.
     */
    private String creatorId;

    /**
     * The description of the item.
     */
    private String description;

    /**
     * The identifier of the item. For items of the same "classification", this identifier is unique.
     * The combination of the "classification" and the "id" is unique across all items of the Watson Content Hub tenant.
     */
    private String id;

    /**
     * The list of keywords related to the item.
     */
    private ArrayList<String> keywords;

    /**
     * The last modification date of the item.
     * <p>
     * Examples:
     * <ul>
     * <li>2020-01-07T17:52:31.610Z</li>
     * <li>2020-01-07T17:52:31.477Z</li>
     * <li>2020-01-07T17:52:31.828Z</li>
     * </ul>
     */
    private String lastModified;

    /**
     * The UUID of the user that last modified the item.
     */
    private String lastModifierId;

    /**
     * The language for which the item was created.
     */
    private String locale;

    /**
     * The name of the item.
     */
    private String name;

    /**
     * This field specifies whether the item is restricted.
     */
    private boolean restricted;

    /**
     * The list of tags assigned to the item.
     */
    private ArrayList<String> tags;

    /**
     * @return string that holds the kind of item
     */
    public String getClassification() {
        return classification;
    }

    /**
     * @return string that holds creation date of the item e.g. 2020-01-07T17:52:31.610Z
     */
    public String getCreated() {
        return created;
    }

    /**
     * @return UUID of the user that created the item
     */
    public String getCreatorId() {
        return creatorId;
    }

    /**
     * @return description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns identifier of the item. For items of the same "classification", this identifier is unique.
     * The combination of the "classification" and the "id" is unique across all items of the Watson Content Hub tenant.
     *
     * @return identifier of the item
     */
    public String getId() {
        return id;
    }

    /**
     * @return list of keywords related to the item
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * @return string that holds last modification date of the item e.g. 2020-01-07T17:52:31.610Z
     */
    public String getLastModified() {
        return lastModified;
    }

    /**
     * @return UUID of the user that last modified the item
     */
    public String getLastModifierId() {
        return lastModifierId;
    }

    /**
     * @return language for which the item was created
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @return name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * @return whether the item is restricted, {@code true} - item is restricted, {@code false} - otherwise
     */
    public boolean isRestricted() {
        return restricted;
    }

    /**
     * @return list of tags assigned to the item
     */
    public List<String> getTags() {
        return tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classification);
        dest.writeString(created);
        dest.writeString(creatorId);
        dest.writeString(description);
        dest.writeString(id);
        ParcelableUtils.writeArrayListOfStrings(keywords, dest);
        dest.writeString(lastModified);
        dest.writeString(lastModifierId);
        dest.writeString(locale);
        dest.writeString(name);
        dest.writeInt(restricted ? 1 : 0);
        ParcelableUtils.writeArrayListOfStrings(tags, dest);
    }

    void readFromParcel(Parcel source) {
        classification = source.readString();
        created = source.readString();
        creatorId = source.readString();
        description = source.readString();
        id = source.readString();
        keywords = ParcelableUtils.readArrayListOfStrings(source);
        lastModified = source.readString();
        lastModifierId = source.readString();
        locale = source.readString();
        name = source.readString();
        restricted = 1 == source.readInt();
        tags = ParcelableUtils.readArrayListOfStrings(source);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        if (restricted != document.restricted) return false;
        if (classification != null ? !classification.equals(document.classification) : document.classification != null)
            return false;
        if (created != null ? !created.equals(document.created) : document.created != null)
            return false;
        if (creatorId != null ? !creatorId.equals(document.creatorId) : document.creatorId != null)
            return false;
        if (description != null ? !description.equals(document.description) : document.description != null)
            return false;
        if (id != null ? !id.equals(document.id) : document.id != null) return false;
        if (keywords != null ? !keywords.equals(document.keywords) : document.keywords != null)
            return false;
        if (lastModified != null ? !lastModified.equals(document.lastModified) : document.lastModified != null)
            return false;
        if (lastModifierId != null ? !lastModifierId.equals(document.lastModifierId) : document.lastModifierId != null)
            return false;
        if (locale != null ? !locale.equals(document.locale) : document.locale != null)
            return false;
        if (name != null ? !name.equals(document.name) : document.name != null) return false;
        return tags != null ? tags.equals(document.tags) : document.tags == null;
    }

    @Override
    public int hashCode() {
        int result = classification != null ? classification.hashCode() : 0;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (keywords != null ? keywords.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifierId != null ? lastModifierId.hashCode() : 0);
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (restricted ? 1 : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Document{" +
                "classification='" + classification + '\'' +
                ", created=" + created +
                ", creatorId='" + creatorId + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", keywords=" + keywords +
                ", lastModified=" + lastModified +
                ", lastModifierId='" + lastModifierId + '\'' +
                ", locale='" + locale + '\'' +
                ", name='" + name + '\'' +
                ", restricted=" + restricted +
                ", tags=" + tags +
                '}';
    }
}
