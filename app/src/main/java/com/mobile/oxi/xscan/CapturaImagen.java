package com.mobile.oxi.xscan;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import utils.DrawView;


public class CapturaImagen extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    protected CameraBridgeViewBase cameraPreview;
    protected Mat mRgbaHSV;
    protected Mat mRgba;
    protected Mat mRgbaOriginal;
    protected Mat mRgbaTemp;

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/XScan/";
    public static final String lang = "spa";

    private static final String TAG = "Calculo ROIs";

    private String idetiqueta ="";

    public List<ResultadoOCR> resultadoOCR;

    private FloatingActionButton fabListaProductos;
    ViewPager pager = null;
    VisorFragmentAdapter pagerAdapter;


    Bitmap bitmapOCR;

    TessBaseAPI baseApi = new TessBaseAPI();

    Handler mHandler;
    Mat hierarchy;
    Mat hierarchy2;

    boolean frameActual=false;
    boolean primeraCarga=true;
    Mat Sample;

    List<MatOfPoint> contours;
    List<MatOfPoint> contours2;

    Rect Template = new Rect(1,1,1,1);

    List<Double> factorn1 = new ArrayList<Double>();
    List<Rect> RectsROIs;
    List<Rect> preRectsROIs;
    List<Bitmap> imagenesCandidatas;
    List<Mat> textBlockHistogram;
    TextView tv_ocrphotosCandidatas;

    ImageView iv_photo;
    ImageView iv_template1;
    ImageView iv_template2;
    ImageView iv_template3;
    ImageView iv_photosCandidatas;

    public LinearLayout layoutVisor;


    protected BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {

                    cameraPreview.enableView();
                //    inicializarTextBlockValidator();
                }
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();


        preRectsROIs = new ArrayList<Rect>();
        setContentView(R.layout.activity_captura_imagen);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setNavigationIcon(R.drawable.ic_bolsa_lista);
       // toolbar.setNavigationOnClickListener();

        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cameraPreview = (CameraBridgeViewBase) findViewById(R.id.camaraPrevisualizar);
        cameraPreview.setCvCameraViewListener(this);
        cameraPreview.setFocusable(true);

        cameraPreview.setFocusableInTouchMode(true);
        cameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        cameraPreview.setMaxFrameSize(width,height);

        tv_ocrphotosCandidatas = (TextView) findViewById(R.id.tv_ocrphotosCandidatas);
        iv_photo = (FloatingActionButton) findViewById(R.id.iv_photo);


        iv_template1 = (ImageView) findViewById(R.id.iv_template1);
        iv_template2 = (ImageView) findViewById(R.id.iv_template2);
        iv_template3 = (ImageView) findViewById(R.id.iv_template3);

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    extraerImagenesROI();
                } catch (Exception e) {
                    Toast.makeText(CapturaImagen.this, "Error "+e.getMessage() + ", "+e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_template1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int y = (int) (mRgba.rows() - (mRgba.rows() * 0.95)) / 2;
                    int x = (int) (mRgba.cols() - (mRgba.cols() * 0.95) * 0.33) / 2;
                    pager.setCurrentItem(1);
                    Template = new Rect(x, y, (int) ((mRgba.rows() * 0.95) * 0.33), (int) (mRgba.rows() * 0.95));
                } catch (Exception e) {

                }
            }
        });
        iv_template3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int y = (int) (mRgba.rows() - (mRgba.rows() * 0.95)) / 2;
                    int x = (int) (mRgba.cols() - (mRgba.cols() * 0.62)) / 2;
                    pager.setCurrentItem(3);
                    Template = new Rect(x, y, ((int) (mRgba.cols() * 0.62)), (int) (mRgba.rows() * 0.95));
                } catch (Exception e) {

                }
            }
        });
        iv_template2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int y = (int) (mRgba.rows() - (mRgba.rows() * 0.95)) / 2;
                    int x = (int) (mRgba.cols() - (mRgba.cols() * 0.42)) / 2;
                    pager.setCurrentItem(2);
                    Template = new Rect(x, y, ((int) (mRgba.cols() * 0.42)), (int) (mRgba.rows() * 0.95));
                } catch (Exception e) {

                }
            }
        });


        inicializarTessTwo();

        inicializarViewPagerVistas();

        layoutVisor = (LinearLayout) findViewById(R.id.content2);

        DrawView fondo = new DrawView(this);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        int y = (int) (mRgba.rows() - (mRgba.rows() * 0.95)) / 2;
                        int x = (int) (mRgba.cols() - (mRgba.cols() * 0.95) * 0.33) / 2;

                        Template = new Rect(x, y, (int) ((mRgba.rows() * 0.95) * 0.33), (int) (mRgba.rows() * 0.95));
                        Log.v(TAG, "Template 1 posicion "+position);
                        break;
                    case 1:
                        y = (int) (mRgba.rows() - (mRgba.rows() * 0.95)) / 2;
                        x = (int) (mRgba.cols() - (mRgba.cols() * 0.95) * 0.33) / 2;

                        Template = new Rect(x, y, (int) ((mRgba.rows() * 0.95) * 0.33), (int) (mRgba.rows() * 0.95));
                        Log.v(TAG, "Template 1 posicion "+position);
                        break;
                    case 2:
                        y = (int) (mRgba.rows() - (mRgba.rows() * 0.95)) / 2;
                        x = (int) (mRgba.cols() - (mRgba.cols() * 0.42)) / 2;

                        Template = new Rect(x, y, ((int) (mRgba.cols() * 0.42)), (int) (mRgba.rows() * 0.95));
                        Log.v(TAG, "Template 2 posicion "+position);
                        break;
                    case 3:
                        y = (int) (mRgba.rows() - (mRgba.rows() * 0.95)) / 2;
                        x = (int) (mRgba.cols() - (mRgba.cols() * 0.62)) / 2;

                        Template = new Rect(x, y, ((int) (mRgba.cols() * 0.62)), (int) (mRgba.rows() * 0.95));
                        Log.v(TAG, "Template 3 posicion "+position);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        RectsROIs = new ArrayList<Rect>();
    }

    protected void inicializarAzureMobileServices()  {


    }

    public void inicializarViewPagerVistas() {

        // Instantiate a ViewPager
        this.pager = (ViewPager) this.findViewById(R.id.pagerVisores);

        // Set a custom animation

        this.pager.setPageTransformer(true, new VisorZOPageTransformer());
        // this.pager.setCurrentItem(3);
        // Create an adapter with the fragments we show on the ViewPager
        VisorFragmentAdapter adapter = new VisorFragmentAdapter(getSupportFragmentManager());

        Display display = getWindowManager().getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        double width = size.x;
        double height = size.y;
        int x =0;
        int y=0;
        y=(int)((height-(height*0.42))/2);
        x=(int)(width-(width*0.80))/2;
        adapter.addFragment(VisorFragment.newInstance(0, x + 25, y + 236, (int) (width * 0.88), (int) (height * 0.56)));
        y=(int)((height-(height*0.42))/2);
        x=(int)(width-(width*0.80))/2;
        adapter.addFragment(VisorFragment.newInstance(1, x + 25, y + 236, (int) (width * 0.88), (int) (height * 0.56)));
        y=(int)((height-(height*0.42))/2);
        x=(int)(width-(width*0.80))/2;
        adapter.addFragment(VisorFragment.newInstance(2, x + 25, y + 170, (int) (width * 0.88), (int) (height * 0.62)));

        y=(int)((height-(height*0.42))/2);
        x=(int)(width-(width*0.80))/2;
        adapter.addFragment(VisorFragment.newInstance(3, x + 25, y + 120, (int) (width * 0.88), (int) (height * 0.66)));

        this.pager.setAdapter(adapter);


    }

    protected void inicializarTessTwo()
    {
        String[] paths = new String[]{DATA_PATH, DATA_PATH + "tessdata/"};

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {

                    return;
                } else {

                }
            }
        }
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".traineddata");
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (IOException e) {
            }
        }

        baseApi.setDebug(true);
        baseApi.init(DATA_PATH, lang);
        Log.v(TAG, "TessBaseAPI Inicializado");
    }

    protected void inicializarTextBlockValidator()
    {
        Sample = new Mat();
        String out="";
        textBlockHistogram = new ArrayList<Mat>();
        AssetManager assetManager = getAssets();
        boolean ArchivosEncontrados=true;
        try {
            String[] fileNames =getAssets().list("textdetector");
            Mat bmtToHist=  new Mat();
            for(String name:fileNames) {
                out = out + "," + name;

                InputStream istr;
                istr = assetManager.open(("textdetector/"+name));
                if(name.indexOf("scan")<1) {
                    Bitmap bitmap = BitmapFactory.decodeStream(istr);
                    Utils.bitmapToMat(bitmap, bmtToHist);
                    Imgproc.cvtColor(bmtToHist, bmtToHist, Imgproc.COLOR_RGB2GRAY, CvType.CV_32F);
                    java.util.List<Mat> matList = new LinkedList<Mat>();
                    matList.add(bmtToHist);
                    Mat histogram = new Mat();
                    MatOfFloat ranges = new MatOfFloat(0, 256);
                    MatOfInt histSize = new MatOfInt(255);
                    Imgproc.calcHist(
                            matList,
                            new MatOfInt(0),
                            new Mat(),
                            histogram,
                            histSize,
                            ranges);
                    Mat histImage = Mat.zeros(100, (int) histSize.get(0, 0)[0], CvType.CV_32F);
                    Core.normalize(histogram, histogram, 1, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
                    textBlockHistogram.add(histogram);
                }
                else
                {
                    //   Bitmap bitmap = BitmapFactory.decodeStream(istr);


                    //   Bitmap myBitmap32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    //   Utils.bitmapToMat(myBitmap32,Sample);

                    //   Utils.bitmapToMat(bitmap, Sample);
                }

            }

        } catch (IOException ex) {
            ArchivosEncontrados=false;
        }
        if (ArchivosEncontrados) {
            Toast.makeText(CapturaImagen.this, "Text detector inicializado." + out, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(CapturaImagen.this, "Text detector archivos de inicializacion no encontrados en "+(DATA_PATH + "textdetector/precio-s1.bmp"), Toast.LENGTH_LONG).show();
        }
    }

    protected void extraerImagenesROI()
    {
        Mat imagenOCR;
        resultadoOCR = new ArrayList<ResultadoOCR>();

        String textoOCR = "";
        Toast.makeText(CapturaImagen.this, "Caracteres encontrados: "+ RectsROIs.size(), Toast.LENGTH_SHORT).show();
        for( int i = 0; i< RectsROIs.size() ; i++ ) {
            Rect roi=RectsROIs.get(i);
            roi.x=roi.x+Template.x;
            roi.y=roi.y+Template.y;

            if(roi.x>5) {
                roi.x = roi.x - 4;
            }
            if(roi.y>5) {
                roi.y = roi.y - 4;
            }
            Matrix mtx = new Matrix();
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
                mtx.postRotate(90);
                if (roi.height + 9 < mRgba.cols()) {
                    roi.height = roi.height + 8;
                }
                if (roi.width + 9 < mRgba.rows()) {
                    roi.width = roi.width + 8;
                }
            }
            else {
                if (roi.y+ roi.height + 9 < mRgba.rows()) {
                    roi.height = roi.height + 8;
                }
                if (roi.x + roi.width + 9 < mRgba.cols()) {
                    roi.width = roi.width + 8;
                }
                mtx.postRotate(90);
            }

            imagenOCR = mRgba.submat(roi);
            Size sz = new Size(imagenOCR.width()*1.3,imagenOCR.height() * 1.3);
            Imgproc.resize(imagenOCR, imagenOCR, sz);
            Imgproc.cvtColor(imagenOCR, imagenOCR, Imgproc.COLOR_RGB2GRAY, CvType.CV_8UC1);
            //   Imgproc.;
            Imgproc.threshold(imagenOCR,imagenOCR,155,255,Imgproc.THRESH_OTSU);

            bitmapOCR = Bitmap.createBitmap(imagenOCR.cols(), imagenOCR.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(imagenOCR, bitmapOCR);
            bitmapOCR = Bitmap.createBitmap(bitmapOCR, 0, 0, bitmapOCR.getWidth(), bitmapOCR.getHeight(), mtx, true);
        //    iv_photosCandidatas.setImageBitmap(bitmapOCR);
            ResultadoOCR _rOCR = new ResultadoOCR();
            //   _rOCR.imagenOCR=bitmapOCR;

            textoOCR = OCR(bitmapOCR);
            CharSequence cs = "ii";
            if(textoOCR.contains(cs))
            {/*
                BarcodeDetector detector =
                        new BarcodeDetector.Builder(getApplicationContext())
                                .setBarcodeFormats(Barcode.DATA_MATRIX)
                                .build();
                Frame frame = new Frame.Builder().setBitmap(bitmapOCR).build();
                SparseArray<Barcode> barcodes = detector.detect(frame);
                if(barcodes.size()>0) {
                    Barcode thisCode = barcodes.valueAt(0);

                    textoOCR = textoOCR + thisCode.rawValue;
                }
                */
            }//

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            _rOCR.latitud=1.1;
            _rOCR.longitud=1.1;
            _rOCR.tipo=0;
            _rOCR.creado=currentDateandTime;
            _rOCR.idetiqueta=idetiqueta;
            _rOCR.usuario="1";
            _rOCR.texto=textoOCR;
            _rOCR.xi=roi.x;
            _rOCR.xf=roi.x + roi.width;
            _rOCR.yi=roi.y;
            _rOCR.yf=roi.y + roi.height;
            _rOCR.factorn1=Double.toString(factorn1.get(i));
            DALResultadoOCR dalResultado = new DALResultadoOCR();

            dalResultado.crear(this,_rOCR);

            resultadoOCR.add(_rOCR);
        }


    }

    protected String OCR(Bitmap imagenOCR)
    {
        String textReconocido="";
        baseApi.setImage(imagenOCR);
        textReconocido = baseApi.getUTF8Text();
        Log.v(TAG, "-.- " + textReconocido);
        // recognizedText = recognizedText.replace("|", "i").replace("\\", "v").replace("{", "").replace("}", "").replace("_", "").replace("Ã‚Â´", "").replace("'", "").replace("Ã‚Â¨", "").replace("#", "");
        return textReconocido;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_capture_imagen, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         switch (item.getItemId()) {
            case R.id.lista_productos:
                Log.i("ActionBar", "Lista_Productos");
                invocarActividad(ProductosListActivity.class);
                return true;
            case R.id.action_search:
                Log.i("ActionBar", "Buscar!");;
                return true;
            case R.id.action_settings:
                Log.i("ActionBar", "Settings!");;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        // TODO Auto-generated method stub

        frameActual=false;
            if(Template.width>100)
            {
                mRgbaOriginal = inputFrame.rgba().submat(Template);
            }
            else {
                mRgbaOriginal = inputFrame.rgba();
            }

        RectsROIs.clear();
        if (!mRgbaOriginal.empty()) {
            mRgbaHSV = new Mat();


            idetiqueta = UUID.randomUUID().toString();

            contours = new ArrayList<MatOfPoint>();

            hierarchy = new Mat();


            Imgproc.cvtColor(mRgbaOriginal, mRgbaHSV, Imgproc.COLOR_RGB2HSV, CvType.CV_8UC3);
            //     Core.inRange(mRgbaHSV, new Scalar(0, 0, 245, 0), new Scalar(180, 255, 255, 0), mRgbaTemp);

            factorn1 = new ArrayList<Double>();
            Rect rt = new Rect();

            Core.inRange(mRgbaHSV, new Scalar(0, 0, 0), new Scalar(180, 255, 75), mRgbaOriginal);
       //     Imgproc.erode(mRgbaOriginal, mRgbaOriginal, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)), new Point(-1, -1), 1);
            Imgproc.dilate(mRgbaOriginal, mRgbaOriginal, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 87)), new Point(-1, -1), 3);
        //    Imgproc.threshold(mRgbaOriginal, mRgbaOriginal, 70, 255, Imgproc.THRESH_BINARY);
            String resultadoProbabilidades = "";

            Imgproc.findContours(mRgbaOriginal, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE, new Point(0, 0));
             mRgba = inputFrame.rgba();



            Mat m = new Mat();
            for (int i = 0; i < contours.size(); i++) {
                rt = Imgproc.boundingRect(contours.get(i));
       /*         mRgba = mRgbaTemp.submat(rt);
                Core.extractChannel(mRgba, m, 0);
                double n = Core.countNonZero(m);
                double contourarea = Imgproc.contourArea(contours.get(i));

                double fn1=n/contourarea;
                */
                if (rt.width > 13 && rt.height > 53&&rt.height>rt.width)// &&fn1>(1))//&&fn1<(2) )//(approxCurve_temp.total() >= 3)
                {

                    RectsROIs.add(rt);
                    factorn1.add(0.0);
                    if (Template.width > 100) {
                        Imgproc.rectangle(mRgba, new Point(rt.x + Template.x, rt.y + Template.y), new Point(rt.x + rt.width + Template.x, rt.y + rt.height + Template.y), new Scalar(255, 255, 1), 2);
                    } else {
                        Imgproc.rectangle(mRgba, new Point(rt.x, rt.y), new Point(rt.x + rt.width, rt.y + rt.height), new Scalar(255, 255, 111), 2);
                    }
                }
            }

         /*
            if(RectsROIs!=null) {

                for (int i = 0; i < RectsROIs.size(); i++) {
                    Rect _rt = RectsROIs.get(i);
                    if (Template.width > 100) {
                        Imgproc.rectangle(mRgbaOriginal, new Point(_rt.x + Template.x, _rt.y + Template.y), new Point(_rt.x + _rt.width + Template.x, _rt.y + _rt.height + Template.y), new Scalar(111, 255, 255), 2);
                    } else {
                        Imgproc.rectangle(mRgbaOriginal, new Point(_rt.x, _rt.y), new Point(_rt.x + _rt.width, _rt.y + _rt.height), new Scalar(111, 255, 255), 2);
                    }
                }
            }
        */
            m.release();
            contours.clear();
            hierarchy.release();

        }

        mRgbaOriginal.release();

        mRgbaHSV.release();
      //  Imgproc.rectangle(mRgbaOriginal, new Point(Template.x, Template.y), new Point(Template.x + Template.width, Template.y + Template.height), new Scalar(255, 255, 255), 1);
        return mRgba;
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        // TODO Auto-generated method stub
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        int y = (int) (mRgba.rows() - (mRgba.rows() * 0.95)) / 2;
        int x = (int) (mRgba.cols() - (mRgba.cols() * 0.95) * 0.33) / 2;
        pager.setCurrentItem(1);
        Template = new Rect(x, y, (int) ((mRgba.rows() * 0.95) * 0.33), (int) (mRgba.rows() * 0.95));
    }

    @Override
    public void onCameraViewStopped() {
        // TODO Auto-generated method stub
        mRgba.release();
        baseApi.end();

    }

    private void invocarActividad(Class ClaseActividad) {
        Intent intent = new Intent(this, ClaseActividad);
        startActivity(intent);
    }

}
