package com.mobile.oxi.xscan;

import org.opencv.core.Point;

/**
 * Created by mgoyeneche on 04/09/2015.
 */
public class KeyPointsGroup {
    private Point _p1;

    public Point getP1(){
        return _p1;
    }
    public void setP1(Point p1) {
        _p1 = p1;
    }

    private Point _p2;

    public Point getP2(){
        return _p2;
    }
    public void setP2(Point p2) {
        _p2 = p2;
    }

    private int _puntosContenidos;

    public int getPuntosContenidos(){
        return _puntosContenidos;
    }
    public void setPuntosContenidos(int puntosContenidos) {
        _puntosContenidos = puntosContenidos;
    }

    public double getHeigth()
    {
        return _p2.y-_p1.y;
    }

    public double getWith()
    {
        return _p2.x-_p1.x;
    }
}
