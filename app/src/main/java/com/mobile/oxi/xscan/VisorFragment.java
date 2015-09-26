package com.mobile.oxi.xscan;

/**
 * Created by aabdel on 14/09/2015.
 */

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import utils.DrawView;


public class VisorFragment extends Fragment {

    /**
     * Key to insert the background color into the mapping of a Bundle.
     */
    private static final String BACKGROUND_COLOR = "color";

    /**
     * Key to insert the index page into the mapping of a Bundle.
     */
    private static final String INDEX = "index";
    private static final String POSX = "posX";
    private static final String POSY = "posY";
    private static final String ANCHO = "ancho";
    private static final String ALTO = "alto";

    private int index;
    private float posX;
    private float posY;
    private float ancho;
    private float alto;

    public ShapeDrawable ShapeVisor;
    private Context context;


    public static VisorFragment newInstance(int index, float posX, float posY, float ancho, float alto) {

        // Instantiate a new fragment
        VisorFragment fragment = new VisorFragment();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        bundle.putFloat(POSX, posX);
        bundle.putFloat(POSY, posY);
        bundle.putFloat(ANCHO, ancho);
        bundle.putFloat(ALTO, alto);


        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Load parameters when the initial creation of the fragment is done
        this.index = (getArguments() != null) ? getArguments().getInt(INDEX)
                : -1;
        this.posX = (getArguments() != null) ? getArguments().getFloat(POSX)
                : -1;
        this.posY = (getArguments() != null) ? getArguments().getFloat(POSY)
                : -1;
        this.ancho = (getArguments() != null) ? getArguments().getFloat(ANCHO)
                : -1;
        this.alto = (getArguments() != null) ? getArguments().getFloat(ALTO)
                : -1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_visor, container, false);

        // Show the current page index in the view
        TextView tvIndex = (TextView) rootView.findViewById(R.id.tvIndex);
        tvIndex.setText(String.valueOf(this.index));
        //ImageView visor = (ImageView) rootView.findViewById(R.id.tipVisor);
        //visor.setImageResource(imgVisor);


        // Change the background color
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.drawrect);
        DrawView DV = new DrawView(getActivity());
        DV.rectVisor(this.posX,this.posY,this.ancho,this.alto);

        relativeLayout.addView(DV);





        return rootView;

    }


}