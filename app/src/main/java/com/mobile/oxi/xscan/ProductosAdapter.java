package com.mobile.oxi.xscan;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;





/**
 * Created by aabdel on 05/09/2015.
 */
public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {

	Context prodContext;
	OnItemClickListener prodItemClickListener;

	public ProductosAdapter(Context context) {
		this.prodContext = context;
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public RelativeLayout prodHolder;
		//public LinearLayout prodNameHolder;
		public EditText prodName;
		public View prodIcon;
		public EditText prodPrecio;
		//public LinearLayout prodOpcionesHolder;

		public ViewHolder(View itemView) {
			super(itemView);
			prodHolder = (RelativeLayout) itemView.findViewById(R.id.prodMainHolder);
			prodName = (EditText) itemView.findViewById(R.id.prodName);
			prodPrecio = (EditText) itemView.findViewById(R.id.prodPrecio);
			//prodNameHolder = (LinearLayout) itemView.findViewById(R.id.prodNameHolder);
			//prodOpcionesHolder = (LinearLayout) itemView.findViewById(R.id.prodOpcionesHolder);
			prodIcon = itemView.findViewById(R.id.prodIcon);
			prodHolder.setOnClickListener(this);

		}

		@Override
		public void onClick(View v) {
			if (prodItemClickListener != null) {
				prodItemClickListener.onItemClick(itemView, getPosition());
			}
		}



	}

	public interface OnItemClickListener {
		void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(final OnItemClickListener prodItemClickListener) {
		this.prodItemClickListener = prodItemClickListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productos_list, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public int getItemCount() {
		return new ProductosData().ListProductos().size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		final Productos productos = new ProductosData().ListProductos().get(position);
		holder.prodName.setText(productos.nomProd);
		holder.prodPrecio.setText(Integer.toString(productos.precioProd));


		GradientDrawable bgShape = (GradientDrawable) holder.prodIcon.getBackground();
		//bgShape.setColor(Color.parseColor(contact.get(Contact.Field.COLOR)));
		bgShape.setColor(prodContext.getResources().getColor(R.color.col_dark_green));

		//holder.prodPrecio.setText(productos.imageName);

		//Picasso.with(prodContext).load(productos.getImageResourceId(prodContext)).into(holder.prodImage);


		//Bitmap photo = BitmapFactory.decodeResource(prodContext.getResources(), productos.getImageResourceId(prodContext));

		//Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
		//	public void onGenerated(Palette palette) {
		//		int bgColor = palette.getMutedColor(prodContext.getResources().getColor(android.R.color.black));
		//		holder.prodNameHolder.setBackgroundColor(bgColor);
		//		holder.prodOpcionesHolder.setBackgroundColor(bgColor);
		//	}
		//});


	}

}


