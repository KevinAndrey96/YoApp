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

public class ValidacionEnrolamientoDocumentoResponse extends AttributeContainer implements KvmSerializable,java.io.Serializable,android.os.Parcelable
{

    
    public ProcesoValidacionDocumentoResponse ValidacionEnrolamientoDocumentoResult;
    private transient java.lang.Object __source;    
    

    public ValidacionEnrolamientoDocumentoResponse()
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
        if (info.name.equals("ValidacionEnrolamientoDocumentoResult"))
        {
            if(obj!=null)
            {
                java.lang.Object j = obj;
                this.ValidacionEnrolamientoDocumentoResult = (ProcesoValidacionDocumentoResponse)__envelope.get(j,ProcesoValidacionDocumentoResponse.class,false);
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
            return this.ValidacionEnrolamientoDocumentoResult!=null?this.ValidacionEnrolamientoDocumentoResult:SoapPrimitive.NullSkip;
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
            info.type = ProcesoValidacionDocumentoResponse.class;
            info.name = "ValidacionEnrolamientoDocumentoResult";
            info.namespace= "http://casb.bytte.com.co/";
        }
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }



    protected ValidacionEnrolamientoDocumentoResponse(android.os.Parcel parcel)
    {
        
        this.ValidacionEnrolamientoDocumentoResult = (ProcesoValidacionDocumentoResponse)parcel.readValue(this.getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        
        
        parcel.writeValue(this.ValidacionEnrolamientoDocumentoResult);
    }

    public static final Creator< ValidacionEnrolamientoDocumentoResponse> CREATOR = new Creator< ValidacionEnrolamientoDocumentoResponse>() {
            @Override
            public ValidacionEnrolamientoDocumentoResponse createFromParcel(android.os.Parcel in) {
                return new ValidacionEnrolamientoDocumentoResponse(in);
            }

            @Override
            public ValidacionEnrolamientoDocumentoResponse[] newArray(int size) {
                return new ValidacionEnrolamientoDocumentoResponse[size];
            }
        };    
}
