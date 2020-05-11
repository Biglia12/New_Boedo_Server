package com.example.newboedoserver.Common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.newboedoserver.Model.Request;
import com.example.newboedoserver.Model.User;
import com.example.newboedoserver.Remote.APIService;
import com.example.newboedoserver.Remote.FCMRetrofitClient;
import com.example.newboedoserver.Remote.IGeoCoordinates;
import com.example.newboedoserver.Remote.RetrofitClient;

import java.util.Calendar;
import java.util.Locale;

public class Common {
    public static User currentUser;
    public static Request currentRequest;

    public static final String UPDATE = "Actualizar";
    public static final String DELETE = "Eliminar";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static final String baseUrl = "https://maps.googleapis.com";

    public static final String BASE_URL = "https://fcm.googleapis.com/";
    public static String PHONE_TEXT="userPhone";


    public static APIService getFCMSerivice() {
        return FCMRetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static String convertCodeToStatus(String code) {
        if (code.equals("0"))
            return "Orden Recibida";
        else if (code.equals("1"))
            return "Orden enviada";
        else
            return "Puede retirar la orden";
    }

    public static IGeoCoordinates getGeoCodeService() {
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaleBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0, pivotY = 0;

        Matrix scalMatrix = new Matrix();
        scalMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaleBitmap);
        canvas.setMatrix(scalMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaleBitmap;
    }

    public static String getDate(long time)
    {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date=new StringBuilder(android.text.format.DateFormat.format("dd-MM-yyyy HH:mm"
                ,calendar)
                .toString());
        return date.toString();


    }
}



