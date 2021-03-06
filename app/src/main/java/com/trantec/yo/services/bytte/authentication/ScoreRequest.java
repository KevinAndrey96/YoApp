package com.trantec.yo.services.bytte.authentication;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 5.5.1.5
//
// Created by Quasar Development 
//
//---------------------------------------------------


import java.util.Hashtable;
import org.ksoap2.serialization.*;

public class ScoreRequest extends AttributeContainer implements KvmSerializable,java.io.Serializable,android.os.Parcelable
{

    
    public Integer Score_AutenticacionDactilar=0;
    
    public Integer Score_ConANI_ANIOCR=0;
    
    public Integer Score_ConANI_OCRBarCode=0;
    
    public Integer Score_ConANI_ANIBarCode=0;
    
    public Integer Score_ConANI_Fingerprint=0;
    
    public Integer Score_SinANI_OCRBarCode=0;
    
    public Integer Score_SinANI_Fingerprint=0;
    
    public Integer Porcentaje_ConANI_AniOCR=0;
    
    public Integer Porcentaje_ConANI_OCRBarcode=0;
    
    public Integer Porcentaje_ConANI_AniBarCode=0;
    
    public Integer Porcentaje_ConANI_FaceMatch=0;
    
    public Integer Porcentaje_SinANI_OCRBarcode=0;
    
    public Integer Porcentaje_SinAni_FaceMatch=0;
    
    public Integer Porcentaje_General_Minimo=0;
    
    public Integer Porcentaje_General_Minimo_AniOCR=0;
    
    public Integer Porcentaje_General_Minimo_OCRBarcode=0;
    
    public Integer Porcentaje_General_Minimo_AniBarCode=0;
    private transient java.lang.Object __source;    
    

    public ScoreRequest()
    {
    }
    
    
    
    public void loadFromSoap(java.lang.Object paramObj,ExtendedSoapSerializationEnvelope __envelope)
    {
        if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;
        __source=inObj; 
        if(inObj instanceof SoapObject)
        {
            SoapObject soapObject=(SoapObject)inObj;
            int size = soapObject.getPropertyCount();
            for (int i0=0;i0< size;i0++)
            {
                PropertyInfo info=soapObject.getPropertyInfo(i0);
                if(!loadProperty(info,soapObject,__envelope))
                {
                }
            } 
        }

    }

    
    protected boolean loadProperty(PropertyInfo info,SoapObject soapObject,ExtendedSoapSerializationEnvelope __envelope)
    {
        java.lang.Object obj = info.getValue();
        if (info.name.equals("Score_AutenticacionDactilar"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Score_AutenticacionDactilar = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Score_AutenticacionDactilar = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Score_ConANI_ANIOCR"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Score_ConANI_ANIOCR = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Score_ConANI_ANIOCR = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Score_ConANI_OCRBarCode"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Score_ConANI_OCRBarCode = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Score_ConANI_OCRBarCode = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Score_ConANI_ANIBarCode"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Score_ConANI_ANIBarCode = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Score_ConANI_ANIBarCode = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Score_ConANI_Fingerprint"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Score_ConANI_Fingerprint = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Score_ConANI_Fingerprint = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Score_SinANI_OCRBarCode"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Score_SinANI_OCRBarCode = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Score_SinANI_OCRBarCode = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Score_SinANI_Fingerprint"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Score_SinANI_Fingerprint = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Score_SinANI_Fingerprint = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Porcentaje_ConANI_AniOCR"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Porcentaje_ConANI_AniOCR = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Porcentaje_ConANI_AniOCR = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Porcentaje_ConANI_OCRBarcode"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Porcentaje_ConANI_OCRBarcode = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Porcentaje_ConANI_OCRBarcode = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Porcentaje_ConANI_AniBarCode"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Porcentaje_ConANI_AniBarCode = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Porcentaje_ConANI_AniBarCode = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Porcentaje_ConANI_FaceMatch"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Porcentaje_ConANI_FaceMatch = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Porcentaje_ConANI_FaceMatch = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Porcentaje_SinANI_OCRBarcode"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Porcentaje_SinANI_OCRBarcode = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Porcentaje_SinANI_OCRBarcode = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Porcentaje_SinAni_FaceMatch"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Porcentaje_SinAni_FaceMatch = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Porcentaje_SinAni_FaceMatch = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Porcentaje_General_Minimo"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Porcentaje_General_Minimo = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Porcentaje_General_Minimo = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Porcentaje_General_Minimo_AniOCR"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Porcentaje_General_Minimo_AniOCR = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Porcentaje_General_Minimo_AniOCR = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Porcentaje_General_Minimo_OCRBarcode"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Porcentaje_General_Minimo_OCRBarcode = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Porcentaje_General_Minimo_OCRBarcode = (Integer)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Porcentaje_General_Minimo_AniBarCode"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Porcentaje_General_Minimo_AniBarCode = Integer.parseInt(j.toString());
                    }
                }
                else if (obj instanceof Integer){
                    this.Porcentaje_General_Minimo_AniBarCode = (Integer)obj;
                }
            }
            return true;
        }
        return false;
    }
    
    public java.lang.Object getOriginalXmlSource()
    {
        return __source;
    }    
    

    @Override
    public java.lang.Object getProperty(int propertyIndex) {
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if(propertyIndex==0)
        {
            return Score_AutenticacionDactilar;
        }
        if(propertyIndex==1)
        {
            return Score_ConANI_ANIOCR;
        }
        if(propertyIndex==2)
        {
            return Score_ConANI_OCRBarCode;
        }
        if(propertyIndex==3)
        {
            return Score_ConANI_ANIBarCode;
        }
        if(propertyIndex==4)
        {
            return Score_ConANI_Fingerprint;
        }
        if(propertyIndex==5)
        {
            return Score_SinANI_OCRBarCode;
        }
        if(propertyIndex==6)
        {
            return Score_SinANI_Fingerprint;
        }
        if(propertyIndex==7)
        {
            return Porcentaje_ConANI_AniOCR;
        }
        if(propertyIndex==8)
        {
            return Porcentaje_ConANI_OCRBarcode;
        }
        if(propertyIndex==9)
        {
            return Porcentaje_ConANI_AniBarCode;
        }
        if(propertyIndex==10)
        {
            return Porcentaje_ConANI_FaceMatch;
        }
        if(propertyIndex==11)
        {
            return Porcentaje_SinANI_OCRBarcode;
        }
        if(propertyIndex==12)
        {
            return Porcentaje_SinAni_FaceMatch;
        }
        if(propertyIndex==13)
        {
            return Porcentaje_General_Minimo;
        }
        if(propertyIndex==14)
        {
            return Porcentaje_General_Minimo_AniOCR;
        }
        if(propertyIndex==15)
        {
            return Porcentaje_General_Minimo_OCRBarcode;
        }
        if(propertyIndex==16)
        {
            return Porcentaje_General_Minimo_AniBarCode;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 17;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Score_AutenticacionDactilar";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==1)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Score_ConANI_ANIOCR";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==2)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Score_ConANI_OCRBarCode";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==3)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Score_ConANI_ANIBarCode";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==4)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Score_ConANI_Fingerprint";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==5)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Score_SinANI_OCRBarCode";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==6)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Score_SinANI_Fingerprint";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==7)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Porcentaje_ConANI_AniOCR";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==8)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Porcentaje_ConANI_OCRBarcode";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==9)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Porcentaje_ConANI_AniBarCode";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==10)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Porcentaje_ConANI_FaceMatch";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==11)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Porcentaje_SinANI_OCRBarcode";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==12)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Porcentaje_SinAni_FaceMatch";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==13)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Porcentaje_General_Minimo";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==14)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Porcentaje_General_Minimo_AniOCR";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==15)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Porcentaje_General_Minimo_OCRBarcode";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==16)
        {
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Porcentaje_General_Minimo_AniBarCode";
            info.namespace= "http://casb.bytte.com.co/";
        }
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }



    protected ScoreRequest(android.os.Parcel parcel)
    {
        
        this.Score_AutenticacionDactilar = (Integer)parcel.readValue(null);
        this.Score_ConANI_ANIOCR = (Integer)parcel.readValue(null);
        this.Score_ConANI_OCRBarCode = (Integer)parcel.readValue(null);
        this.Score_ConANI_ANIBarCode = (Integer)parcel.readValue(null);
        this.Score_ConANI_Fingerprint = (Integer)parcel.readValue(null);
        this.Score_SinANI_OCRBarCode = (Integer)parcel.readValue(null);
        this.Score_SinANI_Fingerprint = (Integer)parcel.readValue(null);
        this.Porcentaje_ConANI_AniOCR = (Integer)parcel.readValue(null);
        this.Porcentaje_ConANI_OCRBarcode = (Integer)parcel.readValue(null);
        this.Porcentaje_ConANI_AniBarCode = (Integer)parcel.readValue(null);
        this.Porcentaje_ConANI_FaceMatch = (Integer)parcel.readValue(null);
        this.Porcentaje_SinANI_OCRBarcode = (Integer)parcel.readValue(null);
        this.Porcentaje_SinAni_FaceMatch = (Integer)parcel.readValue(null);
        this.Porcentaje_General_Minimo = (Integer)parcel.readValue(null);
        this.Porcentaje_General_Minimo_AniOCR = (Integer)parcel.readValue(null);
        this.Porcentaje_General_Minimo_OCRBarcode = (Integer)parcel.readValue(null);
        this.Porcentaje_General_Minimo_AniBarCode = (Integer)parcel.readValue(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        
        
        parcel.writeValue(this.Score_AutenticacionDactilar);
        parcel.writeValue(this.Score_ConANI_ANIOCR);
        parcel.writeValue(this.Score_ConANI_OCRBarCode);
        parcel.writeValue(this.Score_ConANI_ANIBarCode);
        parcel.writeValue(this.Score_ConANI_Fingerprint);
        parcel.writeValue(this.Score_SinANI_OCRBarCode);
        parcel.writeValue(this.Score_SinANI_Fingerprint);
        parcel.writeValue(this.Porcentaje_ConANI_AniOCR);
        parcel.writeValue(this.Porcentaje_ConANI_OCRBarcode);
        parcel.writeValue(this.Porcentaje_ConANI_AniBarCode);
        parcel.writeValue(this.Porcentaje_ConANI_FaceMatch);
        parcel.writeValue(this.Porcentaje_SinANI_OCRBarcode);
        parcel.writeValue(this.Porcentaje_SinAni_FaceMatch);
        parcel.writeValue(this.Porcentaje_General_Minimo);
        parcel.writeValue(this.Porcentaje_General_Minimo_AniOCR);
        parcel.writeValue(this.Porcentaje_General_Minimo_OCRBarcode);
        parcel.writeValue(this.Porcentaje_General_Minimo_AniBarCode);
    }

    public static final Creator< ScoreRequest> CREATOR = new Creator< ScoreRequest>() {
            @Override
            public ScoreRequest createFromParcel(android.os.Parcel in) {
                return new ScoreRequest(in);
            }

            @Override
            public ScoreRequest[] newArray(int size) {
                return new ScoreRequest[size];
            }
        };    
}

