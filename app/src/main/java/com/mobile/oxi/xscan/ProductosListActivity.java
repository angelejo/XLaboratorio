package com.mobile.oxi.xscan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ProductosListActivity extends AppCompatActivity {


    private RecyclerView prodRecyclerView;
    private StaggeredGridLayoutManager prodStaggeredLayoutManager;
    private ProductosAdapter prodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_list);

        prodRecyclerView = (RecyclerView) findViewById(R.id.recicler_view_list);
        prodStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        prodRecyclerView.setLayoutManager(prodStaggeredLayoutManager);

        prodAdapter = new ProductosAdapter(this);
        prodRecyclerView.setAdapter(prodAdapter);
        prodAdapter.setOnItemClickListener(onItemClickListener);
    }

    ProductosAdapter.OnItemClickListener onItemClickListener = new ProductosAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Toast.makeText(ProductosListActivity.this, "Clicked " + position, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProductosListActivity.this, ProductosDetail.class);
            intent.putExtra(ProductosDetail.EXTRA_PARAM_ID, position);
            startActivity(intent);
        }



    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_productos, menu);
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
