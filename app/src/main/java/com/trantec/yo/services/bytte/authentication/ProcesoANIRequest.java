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

public class ProcesoANIRequest extends AttributeContainer implements KvmSerializable,java.io.Serializable,android.os.Parcelable
{

    
    public String IdentificadorProceso;
    
    public String NumeroDocumento;
    private transient java.lang.Object __source;    
    

    public ProcesoANIRequest()
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
        if (info.name.equals("IdentificadorProceso"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.IdentificadorProceso = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.IdentificadorProceso = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("NumeroDocumento"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.NumeroDocumento = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.NumeroDocumento = (String)obj;
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
            return this.IdentificadorProceso!=null?this.IdentificadorProceso:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==1)
        {
            return this.NumeroDocumento!=null?this.NumeroDocumento:SoapPrimitive.NullSkip;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "IdentificadorProceso";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==1)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "NumeroDocumento";
            info.namespace= "http://casb.bytte.com.co/";
        }
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }



    protected ProcesoANIRequest(android.os.Parcel parcel)
    {
        
        this.IdentificadorProceso = (String)parcel.readValue(null);
        this.NumeroDocumento = (String)parcel.readValue(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        
        
        parcel.writeValue(this.IdentificadorProceso);
        parcel.writeValue(this.NumeroDocumento);
    }

    public static final Creator< ProcesoANIRequest> CREATOR = new Creator< ProcesoANIRequest>() {
            @Override
            public ProcesoANIRequest createFromParcel(android.os.Parcel in) {
                return new ProcesoANIRequest(in);
            }

            @Override
            public ProcesoANIRequest[] newArray(int size) {
                return new ProcesoANIRequest[size];
            }
        };    
}

