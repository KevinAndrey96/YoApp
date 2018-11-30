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

public class ProcesoAutenticacionDocumento extends AttributeContainer implements KvmSerializable,java.io.Serializable,android.os.Parcelable
{

    
    public byte[] ImagenFrente;
    
    public String URLImagenFrente;
    
    public byte[] ImagenReverso;
    
    public String URLImagenReverso;
    private transient java.lang.Object __source;    
    

    public ProcesoAutenticacionDocumento()
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
        if (info.name.equals("ImagenFrente"))
        {
            if(obj!=null)
            {
                java.lang.Object j = obj;
                this.ImagenFrente = Helper.getBinary(j,false);
            }
            return true;
        }
        if (info.name.equals("URLImagenFrente"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.URLImagenFrente = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.URLImagenFrente = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("ImagenReverso"))
        {
            if(obj!=null)
            {
                java.lang.Object j = obj;
                this.ImagenReverso = Helper.getBinary(j,false);
            }
            return true;
        }
        if (info.name.equals("URLImagenReverso"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.URLImagenReverso = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.URLImagenReverso = (String)obj;
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
            return this.ImagenFrente!=null?org.kobjects.base64.Base64.encode((byte[])this.ImagenFrente):SoapPrimitive.NullSkip;
        }
        if(propertyIndex==1)
        {
            return this.URLImagenFrente!=null?this.URLImagenFrente:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==2)
        {
            return this.ImagenReverso!=null?org.kobjects.base64.Base64.encode((byte[])this.ImagenReverso):SoapPrimitive.NullSkip;
        }
        if(propertyIndex==3)
        {
            return this.URLImagenReverso!=null?this.URLImagenReverso:SoapPrimitive.NullSkip;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 4;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "ImagenFrente";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==1)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "URLImagenFrente";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==2)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "ImagenReverso";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==3)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "URLImagenReverso";
            info.namespace= "http://casb.bytte.com.co/";
        }
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }



    protected ProcesoAutenticacionDocumento(android.os.Parcel parcel)
    {
        
        this.ImagenFrente = (byte[])parcel.readValue(null);
        this.URLImagenFrente = (String)parcel.readValue(null);
        this.ImagenReverso = (byte[])parcel.readValue(null);
        this.URLImagenReverso = (String)parcel.readValue(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        
        
        parcel.writeValue(this.ImagenFrente);
        parcel.writeValue(this.URLImagenFrente);
        parcel.writeValue(this.ImagenReverso);
        parcel.writeValue(this.URLImagenReverso);
    }

    public static final Creator< ProcesoAutenticacionDocumento> CREATOR = new Creator< ProcesoAutenticacionDocumento>() {
            @Override
            public ProcesoAutenticacionDocumento createFromParcel(android.os.Parcel in) {
                return new ProcesoAutenticacionDocumento(in);
            }

            @Override
            public ProcesoAutenticacionDocumento[] newArray(int size) {
                return new ProcesoAutenticacionDocumento[size];
            }
        };    
}
