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
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class VisorFragment extends Fragment {

    /**
     * Key to insert the background color into the mapping of a Bundle.
     */
    private static final String BACKGROUND_COLOR = "color";

    /**
     * Key to insert the index page into the mapping of a Bundle.
     */
    private static final String INDEX = "index";

    private static final String IMGVISOR = "imagen";


    private int index;
    private int imgVisor;
    public ShapeDrawable ShapeVisor;
    private Context context;


    /**
     * Instances a new fragment with a background color and an index page.
     *
     * @param index    index page
     * @param imgVisor vector de imagen
     * @return a new page
     */
    public static VisorFragment newInstance( int index, int imgVisor ) {

        // Instantiate a new fragment
        VisorFragment fragment = new VisorFragment();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        bundle.putInt(IMGVISOR, imgVisor);

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
        this.imgVisor = (getArguments() != null) ? getArguments().getInt(IMGVISOR)
                : -1;




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_visor, container, false);

        // Show the current page index in the view
        //TextView tvIndex = (TextView) rootView.findViewById(R.id.tvIndex);
        //tvIndex.setText(String.valueOf(this.index));

        // Change the background color
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.drawrect);
        relativeLayout.addView(new DrawView(getActivity()));

        //    DrawView fondo = new DrawView(this);
        //   layoutVisor.addView(fondo);


        //ImageView visor = (ImageView) rootView.findViewById(R.id.tipVisor);
        //visor.setImageResource(imgVisor);


        return rootView;

    }


}
