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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents asset {@link Document} that can be retrieved using delivery search API, provides document-specific properties.
 */
public class Asset extends Document {

    /**
     * Field contains the asset type. The value that is returned can be "document", "file", "image", or "video".
     */
    private String assetType;

    /**
     * The list of all category selections for the asset.
     */
    private ArrayList<String> categories;

    /**
     * The list of all leaf category selection elements for the asset or content.
     */
    private ArrayList<String> categoryLeaves;

    /**
     * Field contains the full JSON document for the item.
     */
    private JSONObject document;

    /**
     * Field contains the file size in bytes.
     */
    private long fileSize;

    /**
     * Field specifies whether the content is managed or not managed and whether the asset is a managed asset or a so-called non-managed web asset.
     */
    private boolean isManaged;

    /**
     * Field contains the folder path without the file name.
     * This allows for efficient queries for sibling assets.
     */
    private String location;

    /**
     * Field contains all of the path segments.
     * This allows for efficient queries that return assets in subfolders of the queried value.
     * For example, the query locationPaths:"/dxdam" will return assets that are stored in the /dxdam folder or any subfolder.
     */
    private String locationPaths;

    /**
     * Field contains the URL to the binary of the asset.
     * It is relative to the API URL for your tenant.
     */
    private String media;

    /**
     * Field contains the media type.
     */
    private String mediaType;

    /**
     * Field contains the folder path including file name.
     */
    private String path;

    /**
     * Field contains the ID of the related resource.
     * You can use this resource ID with the authoring and delivery resource service REST APIs.
     */
    private String resource;

    /**
     * Field contains the state the item is in.
     * The value of this field can be "ready" or "retired".
     */
    private String status;

    /**
     * Field contains the URL to the thumbnail of the asset.
     * It is relative to the API URL for your tenant.
     */
    private String thumbnail;

    /**
     * Field contains the server relative URL to the binary document of the asset.
     */
    private String url;

    Asset(DeliverySearchResponseDocument rawDoc) {
        super(rawDoc);
        assetType = rawDoc.assetType;
        categories = rawDoc.categories;
        categoryLeaves = rawDoc.categoryLeaves;
        document = rawDoc.document;
        fileSize = rawDoc.fileSize;
        isManaged = rawDoc.isManaged;
        location = rawDoc.location;
        locationPaths = rawDoc.locationPaths;
        media = rawDoc.media;
        mediaType = rawDoc.mediaType;
        path = rawDoc.path;
        resource = rawDoc.resource;
        status = rawDoc.status;
        thumbnail = rawDoc.thumbnail;
        url = rawDoc.url;
    }

    private Asset(Parcel source) {
        super(source);
    }

    /**
     * @return the asset type. The value that is returned can be "document", "file", "image", or "video"
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * @return list of all category selections for the asset
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * @return list of all leaf category selection elements for the asset or content
     */
    public List<String> getCategoryLeaves() {
        return categoryLeaves;
    }

    /**
     * @return the full JSON document for the item
     */
    public JSONObject getDocument() {
        return document;
    }

    /**
     * @return file size in bytes
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * @return whether the content is managed or not managed and whether the asset is a managed asset or a so-called non-managed web asset
     */
    public boolean isManaged() {
        return isManaged;
    }

    /**
     * @return contains the folder path without the file name
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return contains all of the path segments
     */
    public String getLocationPaths() {
        return locationPaths;
    }

    /**
     * @return the URL to the binary of the asset
     */
    public String getMedia() {
        return media;
    }

    /**
     * @return media type
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * @return folder path including file name
     */
    public String getPath() {
        return path;
    }

    /**
     * @return ID of the related resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * @return contains the state the item is in
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return URL to the thumbnail of the asset
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * @return server relative URL to the binary document of the asset
     */
    public String getUrl() {
        return url;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(assetType);
        ParcelableUtils.writeArrayListOfStrings(categories, dest);
        ParcelableUtils.writeArrayListOfStrings(categoryLeaves, dest);
        ParcelableUtils.writeJSONObject(document, dest);
        dest.writeLong(fileSize);
        dest.writeInt(isManaged ? 1 : 0);
        dest.writeString(location);
        dest.writeString(locationPaths);
        dest.writeString(media);
        dest.writeString(mediaType);
        dest.writeString(path);
        dest.writeString(resource);
        dest.writeString(status);
        dest.writeString(thumbnail);
        dest.writeString(url);
    }

    @Override
    void readFromParcel(Parcel source) {
        super.readFromParcel(source);
        assetType = source.readString();
        categories = ParcelableUtils.readArrayListOfStrings(source);
        categoryLeaves = ParcelableUtils.readArrayListOfStrings(source);
        document = ParcelableUtils.readJSONObject(source);
        fileSize = source.readLong();
        isManaged = source.readInt() == 1;
        location = source.readString();
        locationPaths = source.readString();
        media = source.readString();
        mediaType = source.readString();
        path = source.readString();
        resource = source.readString();
        status = source.readString();
        thumbnail = source.readString();
        url = source.readString();
    }

    public static final Creator<Asset> CREATOR = new Creator<Asset>() {
        @Override
        public Asset createFromParcel(Parcel source) {
            return new Asset(source);
        }

        @Override
        public Asset[] newArray(int size) {
            return new Asset[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Asset asset = (Asset) o;

        if (fileSize != asset.fileSize) return false;
        if (isManaged != asset.isManaged) return false;
        if (assetType != null ? !assetType.equals(asset.assetType) : asset.assetType != null)
            return false;
        if (categories != null ? !categories.equals(asset.categories) : asset.categories != null)
            return false;
        if (categoryLeaves != null ? !categoryLeaves.equals(asset.categoryLeaves) : asset.categoryLeaves != null)
            return false;
        if (document != null ? !document.toString().equals(asset.document.toString()) : asset.document != null)
            return false;
        if (location != null ? !location.equals(asset.location) : asset.location != null)
            return false;
        if (locationPaths != null ? !locationPaths.equals(asset.locationPaths) : asset.locationPaths != null)
            return false;
        if (media != null ? !media.equals(asset.media) : asset.media != null) return false;
        if (mediaType != null ? !mediaType.equals(asset.mediaType) : asset.mediaType != null)
            return false;
        if (path != null ? !path.equals(asset.path) : asset.path != null) return false;
        if (resource != null ? !resource.equals(asset.resource) : asset.resource != null)
            return false;
        if (status != null ? !status.equals(asset.status) : asset.status != null) return false;
        if (thumbnail != null ? !thumbnail.equals(asset.thumbnail) : asset.thumbnail != null)
            return false;
        return url != null ? url.equals(asset.url) : asset.url == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (assetType != null ? assetType.hashCode() : 0);
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        result = 31 * result + (categoryLeaves != null ? categoryLeaves.hashCode() : 0);
        result = 31 * result + (document != null ? document.toString().hashCode() : 0);
        result = 31 * result + (int) (fileSize ^ (fileSize >>> 32));
        result = 31 * result + (isManaged ? 1 : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (locationPaths != null ? locationPaths.hashCode() : 0);
        result = 31 * result + (media != null ? media.hashCode() : 0);
        result = 31 * result + (mediaType != null ? mediaType.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "assetType='" + assetType + '\'' +
                ", categories=" + categories +
                ", categoryLeaves=" + categoryLeaves +
                ", document=" + document +
                ", fileSize=" + fileSize +
                ", isManaged=" + isManaged +
                ", location='" + location + '\'' +
                ", locationPaths='" + locationPaths + '\'' +
                ", media='" + media + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", path='" + path + '\'' +
                ", resource='" + resource + '\'' +
                ", status='" + status + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", url='" + url + '\'' +
                "} " + super.toString();
    }
}
