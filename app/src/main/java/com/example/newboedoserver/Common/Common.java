package com.example.newboedoserver.Common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.newboedoserver.Model.Request;
import com.example.newboedoserver.Model.User;
import com.example.newboedoserver.Remote.IGeoCoordinates;
import com.example.newboedoserver.Remote.RetrofitClient;

public class Common {
    public static User currentUser;
    public static Request currentRequest;

    public static final String UPDATE = "Actualizar";
    public static final String DELETE = "Eliminar";

    public static final int PICK_IMAGE_REQUEST=71;

    public static final String baseUrl = "https://maps.googleapis.com";

    public static String convertCodeToStatus (String code)
    {
        if(code.equals("0"))
            return "En lugar";
        else if (code.equals("1"))
            return "En camino";
        else
            return "Entregado";
    }

    public static IGeoCoordinates getGeoCodeService(){
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap,int newWidth,int newHeight)
    {
        Bitmap scaleBitmap = Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_8888);

        float scaleX=newWidth/(float)bitmap.getWidth();
        float scaleY=newHeight/(float) bitmap.getHeight();
        float pivotX=0,pivotY=0;

        Matrix scalMatrix= new Matrix();
        scalMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas=new Canvas(scaleBitmap);
        canvas.setMatrix(scalMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaleBitmap;
    }

}
