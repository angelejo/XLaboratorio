package com.mobile.oxi.xscan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ProductosDetail extends AppCompatActivity {

    public static final String EXTRA_PARAM_ID = "product_id";
    private Productos dProd;
    private TextView nomProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_detail);
        dProd = ProductosData.ListProductos().get(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));

        nomProd = (TextView) findViewById(R.id.nomProd);
        loadProducto();
    }

    private void loadProducto() {
        nomProd.setText(dProd.nomProd);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_productos_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
