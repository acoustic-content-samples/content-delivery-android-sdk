# Acoustic Content Delivery Android SDK

Android SDK for the Acoustic Content Delivery API.

## Motivation

This SDK is designed to help utilize the content hosted in Acoustic Content libraries in mobile applications (headlessly).

## Getting started

Acoustic Content Delivery SDK is a lightweight library that provides the ability to get content from Acoustic Content Hub via the delivery search API.

## Adding Acoustic Content Delivery SDK to your project

In order to start using Acoustic Content Delivery SDK perform following steps:

1. Clone latest version of Acoustic Content SDK source code.
2. Import **sdk** module of Acoustic Content Delivery SDK to your Android project. Using AndroidStudio main menu, select **"File" -> "New" -> "Import Module..."**, on the **"Import module from source"** window that will appear set **"Source directory"** field to location of **AcousticContentDeliverySDK/sdk** folder and import **sdk** module.
3. Add imported **sdk** module dependency to application module, open **build.gradle** of application module, which typically is located in application module root folder, and add **implementation project(":sdk")** line to **dependencies** section.

```
apply plugin: 'com.android.application'

android {
    ...
}

dependencies {
    ...

    implementation project(":sdk")

    ...
}
```

4. Configure application module for Java 8 features support, open **build.gradle** of application module, which typically is located in application module root folder, and add **compileOptions** section to **android** section of **build.gradle** file, in **compileOptions** section add **sourceCompatibility** and **targetCompatibility** properties set to **1.8**.

```
apply plugin: 'com.android.application'

android {
    ...

    compileOptions {
        sourceCompatibility = 1.8
        targetCompatibility = 1.8
    }

    ...
}

dependencies {
    ...
}

```

5. Specify that application requires permission to use Internet, open **AndroidManifest.xml** file of application module, typically located in **application_module_root_folder/src/main** folder, and add **uses-permission** element with **android:name** attribute set to **android.permission.INTERNET**.

```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.app.package">

    <uses-permission android:name="android.permission.INTERNET"/>

    ...

</manifest>
```

## Using Acoustic Content Delivery SDK

In order to start using Acoustic Content Delivery SDK create instance of `SDKConfig` using `SDKConfig.Builder`. In order to obtain instance of `SDKConfig.Builder` use `SDKConfig.builder()` method, afterwards set Acoustic Content Hub API URL to `SDKConfig.Builder` via `setApiUrl()` method and call `build()` method to obtain `SDKConfig` instance.

```
SDKConfig sdkConfig = SDKConfig.builder().setApiUrl("API_URL").build();
```

After instance of `SDKConfig` is created, create `ContentDeliverySDK` instance using `ContentDeliverySDK.create()` method.

```
SDKConfig sdkConfig = ...

ContentDeliverySDK sdk = ContentDeliverySDK.create(sdkConfig);
```

**NOTE:**

Acoustic Content Delivery SDK does not enforce single instance of `ContentDeliverySDK` by default, each invocation of `ContentDeliverySDK.create()` method will return new instance of `ContentDeliverySDK`, which means that multiple instances may exist in scope of single Android application if necessary.

If multiple instances of `ContentDeliverySDK` are not necessary, then common approach would be to create single global instance of `ContentDeliverySDK` in corresponding `Application` class implementation for given Android application.

```
public class SampleApplication extends Application {
    private static final String API_URL = "YOUR_CONTENT_HUB_API_URL";

    private ContentDeliverySDK sdk;

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize ContentDeliverySDK.
        this.sdk = ContentDeliverySDK.create(
            SDKConfig.builder().setApiUrl(API_URL).build()
        );
    }

    public ContentDeliverySDK getSDK() {
        return this.sdk;
    }
}
```

In order to obtain delivery search query builder instances that can be further used to configure delivery search queries for supported content types obtain `DeliverySearch` instance using `dleiverySearch()` method on initialized `ContentDeliverySDK` instance. 

```
ContentDeliverySDK sdk = ...

sdk.deliverySearch();
```

Acoustic Content Delivery SDK allows querying for following kinds of content: `Assets`, `ContentItems`, `Categories` and `ContentTypes`.
Access to each type of content is provided by `DeliverySearch` instance via corresponding methods:

```
sdk.deliverySearch().assets();

sdk.deliverySearch().contentItems();

sdk.deliverySearch().categories();

sdk.deliverySearch().contentTypes();
```

In order to configure delivery search query use APIs that are provided by query builder classes: `Assets`, `ContentItems`, `Categories` and `ContentTypes`.

```
sdk.deliverySearch().assets()
    .start(0)
    .rows(10)
    .filterByName("J*")
    .sortBy("name", true);
```

In order to initiate configured query invoke method `get()` on corresponding query builder instance.

**NOTE:** calling `get()` method does not block current thread execution.

In order to get query results use `then()` and `error()` methods, `then()` method is used to get results of successful delivery search query, `error()` method is used to get `Throwable` error in case of failed delivery search query.

```
sdk.deliverySearch().assets()

    ...
    
    .get()
    .then(result -> {
        ...

        int numFound = result.getNumFound();
        List<Asset> documents = result.getDocuments();
        
        Log.d("DELIVERY_SEARCH_RESULT", "Found Assets: numFound = " + numFound + ", documents = " + documents);

        ...
    })
    .error(error -> {
        ...

        Log.e("DELIVERY_SEARCH_RESULT", "Failed to get assets", error);

        ...
    });

```

## Pagination of delivery search query results

`DeliverySearchResult` instance, which is passed into `then()` method callback for each type of query builder classes: `Assets`, `ContentItems`, `Categories` and `ContentTypes`, -
provides API to query next page and previous page for initialy configured delivery search query via `nextPage()` and `previousPage()` methods.

```
sdk.deliverySearch().assets()

    ...

    .get()
    .then(result -> {
        ...
        
        final Assets nextPage = result.nextPage();
        nextPage.get()
            .then(result -> {
                ...
            })
            .error(error -> {
                ...
            });
      
        ...
      
        final Assets prevPage = result.previousPage();
        prevPage.get()
            .then(result -> {
                ...
            })
            .error(error -> {
                ...
            });

        ...

    });

```

## Providing user credentials to ContentDeliverySDK

In order to provide `ContentDeliverySDK` with user credentials use `login()` method of corresponding `ContentDeliverySDK` instance.

```
sdk.login(
    userName,
    password,
    error -> {
        // process Throwable error in case of unsuccessful login
        // if login successful then error is set to null
    }
);
```

In order to logout current `ContentDeliverySDK` user after successful login use `logout()` method of corresponding `ContentDeliverySDK` instance.

```
sdk.logout();
```

## Obtaining Parcelable state of Assets, ContentItems, Categories, ContentTypes and DeliverySearchResult instances

Query builder classes: `Assets`, `ContentItems`, `Categories`, `ContentTypes`, - and `DeliverySearchResult` class provide ability to obtain `Parcelable` snapshot of theyre current state, via `getState()` method.
This feature is handy to save / restore state of corresponding instances or pass state of corresponding instnaces between Android components e.g. in case delivery search query should be built in one Activity but executed by other Activity.

```
Documents.State state = sdk.deliverySearch().assets()

    ...
    
    .getState();
    
...

DeliverySearchResult result = ...

DeliverySearchResult.State resultState = result.getState();
```

## Restoring Assets, ContentItems, Categories, ContentTypes and DeliverySearchResult instances using Parcelable state

In order to create new instance of `Assets`, `ContentItems`, `Categories`, `ContentTypes` using `Parcelable` state instance use `createDocuments()` method of `DeliverySearch` class.

```
Documents.State state = sdk.deliverySearch().assets()

    ...
    
    .getState();

...

Assets assets = sdk.deliverySearch().createDocuments(state);
```

In order to create new instance of `DeliverySearchResult` using `Parcelable` state instance use `createDeliverySearchResult()` method of `DeliverySearch` class.

```
DeliverySearchResult.State resultState = ...

...

DeliverySearchResult<Asset> result = sdk.deliverySearch()
    .createDeliverySearchResult(resultState);
```

## Parcelable state usage example: save/restore Activity state

Typical usage for `Parcelable` state of `Assets`, `ContentItems`, `Categories`, `ContentTypes` and `DeliverySearchResult` instances would be saving and restoring state of `Activity` e.g. on device rotation.

```
public class SampleActivity extends AppCompatActivity {

    private static final String QUERY_STATE = "query_state";
    private static final String QUERY_RESULT_STATE = "query_result_state";

    private Assets assets;
    private DeliverySearchResult<Asset> assetsDeliverySearchResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentDeliverySDK sdk = ...

        if (null != savedInstanceState) {
            final Documents.State queryState = savedInstanceState
                .getParcelable(QUERY_STATE);
                
            assets = sdk.deliverySearch().createDocuments(queryState);

            final DeliverySearchResult.State resultState = savedInstanceState
                .getParcelable(QUERY_RESULT_STATE);
                
            assetsDeliverySearchResult = sdk.deliverySearch()
                .createDeliverySearchResult(resultState);
        }

        ...
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        
        outState.putParcelable(
            QUERY_STATE,
            assets.getState()
        );
        outState.putParcelable(
            QUERY_RESULT_STATE,
            assetsDeliverySearchResult.getState()
        );
        
        ...
    }
    
    ...

}
```

## Acoustic Content Delivery SDK sample app

Acoustic Content Delivery SDK provides sample application that demonstrates possible SDK use cases.

In order to configure Acoustic Content Delivery SDK to use your content hub API URL open `SampleApplication` and change value of `API_URL` constant.

```
public class SampleApplication extends Application {

    private static final String API_URL = "YOUR_CONTENT_HUB_API_URL";

    ...

}
```
