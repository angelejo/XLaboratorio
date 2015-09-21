package com.mobile.oxi.xscan;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

/**
 * Created by mgoyeneche on 18/09/2015.
 */
public class DALResultadoOCR {
    private SQLiteDatabase db=null;
    private AppSQLiteHelper dbhelper=null;
    private final String TAG="-.- DALResultadoOCR";
    private MobileServiceClient mClient;
    private MobileServiceTable<ResultadoOCR> capturaTable;
    private Context mContext;
    private MobileServiceJsonTable mTableBlobs;

    public void crear(Context context, final ResultadoOCR entidad) {
        mContext = context;

        try {
            mClient = new MobileServiceClient("https://xmobilidad.azure-mobile.net/", "SuFkbfXIZtePOkMUwEQpuUiJwlLgcj88", mContext);
            mTableBlobs = mClient.getTable("xscan");
            capturaTable = mClient.getTable("Captura",ResultadoOCR.class);
        } catch (MalformedURLException e) {
            Log.e(TAG, "There was an error creating the Mobile Service. Verify the URL");
        }
        dbhelper = new AppSQLiteHelper(context);
        db=dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
      //  values.put("imagenbmp", entidad.imagenBMP);
        values.put("texto", entidad.texto);
        values.put("tipo", entidad.tipo);
        values.put("xf", entidad.xf);
        values.put("xi", entidad.xi);
        values.put("yf", entidad.yf);
        values.put("yi", entidad.yi);
        values.put("creado", entidad.creado);
        values.put("usuario", entidad.usuario);
        values.put("idetiqueta", entidad.idetiqueta);
     //   values.put("id", entidad.getId());
        values.put("latitud", Double.toString(entidad.latitud));
        values.put("longitud", Double.toString(entidad.longitud));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    capturaTable.insert(entidad).get();

                } catch (Exception exception) {
                    Log.e(TAG, "Error insertando datos en windows azure mobile services: " + exception.getMessage());
                }
                try {

                    //mTableBlobs.insert(entidad);
                } catch (Exception exception) {
                    Log.e(TAG, "Error insertando imagen en windows azure blob storage services: " + exception.getMessage());
                }
                return null;
            }
        }.execute();

        db.insert("Captura", null, values);
        Log.v(TAG, "Captura almacenada : " + entidad.idetiqueta);
        db.close();


    }
}
