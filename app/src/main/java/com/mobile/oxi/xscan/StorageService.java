package com.mobile.oxi.xscan;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;
import com.microsoft.windowsazure.mobileservices.table.TableJsonOperationCallback;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mgoyeneche on 18/09/2015.
 */
public class StorageService {
    private MobileServiceClient mClient;
    private MobileServiceJsonTable mTableTables;
    private MobileServiceJsonTable mTableTableRows;
    private MobileServiceJsonTable mTableContainers;
    private MobileServiceJsonTable mTableBlobs;
    private Context mContext;
    private final String TAG = "StorageService";
    private List<Map<String, String>> mTables;
    private ArrayList<JsonElement> mTableRows;
    private List<Map<String, String>> mContainers;
    private List<Map<String, String>> mBlobNames;
    private ArrayList<JsonElement> mBlobObjects;
    private JsonObject mLoadedBlob;

    /***
     * Initialize our service
     * @param context
     */
    public StorageService(Context context) {
        mContext = context;
        try {
            mClient = new MobileServiceClient("https://mobileserviceurl.azure-mobile.net/", "applicationkey", mContext);
            mTableTables = mClient.getTable("Tables");
            mTableTableRows = mClient.getTable("TableRows");
            mTableContainers = mClient.getTable("BlobContainers");
            mTableBlobs = mClient.getTable("BlobBlobs");
        } catch (MalformedURLException e) {
            Log.e(TAG, "There was an error creating the Mobile Service. Verify the URL");
        }
    }

    public List<Map<String, String>> getLoadedTables() {
        return this.mTables;
    }

    public JsonElement[] getLoadedTableRows() {
        return this.mTableRows.toArray(new JsonElement[this.mTableRows.size()]);
    }

    public List<Map<String, String>> getLoadedContainers() {
        return this.mContainers;
    }

    public List<Map<String, String>> getLoadedBlobNames() {
        return this.mBlobNames;
    }

    public JsonElement[] getLoadedBlobObjects() {
        return this.mBlobObjects.toArray(new JsonElement[this.mBlobObjects.size()]);
    }

    public JsonObject getLoadedBlob() {
        return this.mLoadedBlob;
    }

    /***
     * Fetches all of the tables from storage
     */






    /***
     * Gets a SAS URL for a new blob so we can upload it to the server
     * @param containerName
     * @param blobName
     * NOTE THIS IS DONE AS A SEPARATE METHOD FROM getSasForNewBlob BECAUSE IT
     * BROADCASTS A DIFFERENT ACTION
     */
    public void getSasForNewBlob(String containerName, String blobName) {
        //Create the json Object we'll send over and fill it with the required
        //id property - otherwise we'll get kicked back
        JsonObject blob = new JsonObject();
        blob.addProperty("id", 0);
        //Create parameters to pass in the blob details.  We do this with params
        //because it would be stripped out if we put it on the blob object
        List<Pair<String,String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<String, String>("containerName", containerName));
        parameters.add(new Pair<String, String>("blobName", blobName));
        mTableBlobs.insert(blob, parameters, new TableJsonOperationCallback() {
            @Override
            public void onCompleted(JsonObject jsonObject, Exception exception,
                                    ServiceFilterResponse response) {
                if (exception != null) {
                    Log.e(TAG, exception.getCause().getMessage());
                    return;
                }
                //Set the loaded blob
                mLoadedBlob = jsonObject;
                //Broadcast that we are ready to upload the blob data
                Intent broadcast = new Intent();
                broadcast.setAction("blob.created");
                mContext.sendBroadcast(broadcast);
            }
        });
    }
}