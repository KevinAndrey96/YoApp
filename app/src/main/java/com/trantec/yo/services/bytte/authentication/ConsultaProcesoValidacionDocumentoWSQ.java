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

public class ConsultaProcesoValidacionDocumentoWSQ extends AttributeContainer implements KvmSerializable,java.io.Serializable,android.os.Parcelable
{

    
    public ConsultaProcesoValidacionDocumentoRequest request;
    private transient java.lang.Object __source;    
    

    public ConsultaProcesoValidacionDocumentoWSQ()
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
        if (info.name.equals("request"))
        {
            if(obj!=null)
            {
                java.lang.Object j = obj;
                this.request = (ConsultaProcesoValidacionDocumentoRequest)__envelope.get(j,ConsultaProcesoValidacionDocumentoRequest.class,false);
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
            return this.request!=null?this.request:SoapPrimitive.NullSkip;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 1;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = ConsultaProcesoValidacionDocumentoRequest.class;
            info.name = "request";
            info.namespace= "http://casb.bytte.com.co/";
        }
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }



    protected ConsultaProcesoValidacionDocumentoWSQ(android.os.Parcel parcel)
    {
        
        this.request = (ConsultaProcesoValidacionDocumentoRequest)parcel.readValue(this.getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        
        
        parcel.writeValue(this.request);
    }

    public static final Creator< ConsultaProcesoValidacionDocumentoWSQ> CREATOR = new Creator< ConsultaProcesoValidacionDocumentoWSQ>() {
            @Override
            public ConsultaProcesoValidacionDocumentoWSQ createFromParcel(android.os.Parcel in) {
                return new ConsultaProcesoValidacionDocumentoWSQ(in);
            }

            @Override
            public ConsultaProcesoValidacionDocumentoWSQ[] newArray(int size) {
                return new ConsultaProcesoValidacionDocumentoWSQ[size];
            }
        };    
}

