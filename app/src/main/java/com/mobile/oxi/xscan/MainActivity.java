package com.mobile.oxi.xscan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity {


    View.OnClickListener invProductosActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        invocarActividad(CapturaImagen.class);

     }


    private void invocarActividad(Class ClaseActividad) {
        Intent intent = new Intent(this, ClaseActividad);
        startActivity(intent);
    }

    private void invocarActividadExtra(Class ClaseActividad) {
        // Intent intent = new Intent(this, DetailActivity.class);
        // intent.putExtra(DetailActivity.EXTRA_PARAM_ID, 1);
        //  startActivity(intent);
    }

    private void setUpActionBar() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }


}
