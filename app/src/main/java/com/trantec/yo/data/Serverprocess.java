package com.trantec.yo.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Serverprocess {
    private static final MediaType MEDIA_TYPE = MediaType.parse("multipart/form-data");
    private static final String IMGUR_CLIENT_ID = "9199fdef135c122";



    public static String  SendDataToServerMultipart(String AuthorizationToken,
                                                          String requestURL,
                                                          Map<String,byte[]> kMap,
                                                          String sJSonValue) {

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag = "fSnd";
        String Title = "JsonData";
        String Description = sJSonValue;
        final String respuesta;
        int iTimeOut = 900 * 1000;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(iTimeOut, TimeUnit.MILLISECONDS)
                .readTimeout(iTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(iTimeOut, TimeUnit.MILLISECONDS)
                .build();

        int iContador = 0;
        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("description", Description)
                .addFormDataPart("title",Title);

        //Recorro los Items del Map
        for (Map.Entry<String, byte[]> item : kMap.entrySet()) {
            //Incremento el contador
            iContador++;

            //Obtengo el nombre del archivo
            String iFileName = item.getKey();
            buildernew.addFormDataPart("uploadedfile" + iContador,  iFileName  , RequestBody.create(MEDIA_TYPE, item.getValue()));
        }
        MultipartBody requestBody = buildernew.build();
        Request request = new Request.Builder()
                .url(requestURL)
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Content-Type", "multipart/form-data;boundary=" + boundary)
                .post(requestBody)
                .build();

        // Read data on the worker thread
        Call call = client.newCall(request);
        try {
            Response response = call.execute();

            int serverResponseCode = response.code();
            String serverResponseMessage = response.body().string();

            if (serverResponseCode >= 200 && serverResponseCode <= 299) {

                respuesta=serverResponseMessage;

            }else{

                respuesta="Error "+serverResponseCode+" "+serverResponseMessage;

            }

        } catch (IOException e) {
            e.printStackTrace();

            return e.getMessage();
        }
        return respuesta;

    }
    public static String procesaInfoDoc_Biometric(String targetUrl,objetos.Datos_cedula datodoc) {

        String authToken = "";
        String json = "";
        String response = "";
        String ID= UUID.randomUUID().toString();
        //Retorno el Objeto

        //Hashmap para el envio de archivos
        HashMap<String, byte[]> hasFiles = new HashMap<String, byte[]>();

        try {

            //Genero el GSOn para WCF
            //Asigno q viene un DateTime .Net
            GsonBuilder b = new GsonBuilder();
            b.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Gson gson = b.create();





            //Obtengo las imágenes
            byte[] bImageDocumento = null;//"imganes que cooresponda ejemplo"
            byte[] bBarCode = null;//"barcode en byttearray";
            byte[] bmpImagen1 =null;
            byte[] bmpImagen2 =null;
            byte[] bmpImagen3 = null;
            byte[] bmpImagen4 = null;
            //https://stackoverflow.com/questions/23456488/how-to-use-okhttp-to-make-a-post-request

            //Adiciono al HashMap los archivos como los recibe bytte
            if (bImageDocumento != null)
                hasFiles.put("imgDocumento", bImageDocumento);
            if (bBarCode != null)
                hasFiles.put("bBarCode", bBarCode);
            if (bmpImagen1 != null)
                hasFiles.put("Imagen1", bmpImagen1);
            if (bmpImagen2 != null)
                hasFiles.put("Imagen2", bmpImagen2);
            if (bmpImagen3 != null)
                hasFiles.put("Imagen3", bmpImagen3);
            if (bmpImagen4 != null)
                hasFiles.put("Imagen4", bmpImagen4);


            //json = gson.toJson(obj, mp_huellas.class);//ID;
            json = ID;

            //Envio la informacion al Servidor por POST
            response = SendDataToServerMultipart(authToken, targetUrl, hasFiles, json);


            //valide si la respuesta es lo que espera

        } catch (Exception ex) {
            //Asigno la respuesta de false
            response="error";
        }
        return response;
    }




}
