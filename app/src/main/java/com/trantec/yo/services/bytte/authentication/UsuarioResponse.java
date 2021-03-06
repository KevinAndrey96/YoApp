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

public class UsuarioResponse extends Response implements KvmSerializable,java.io.Serializable,android.os.Parcelable
{

    
    public UsuarioEnrolamiento Usuario;
    

    public UsuarioResponse()
    {
    }
    
    
    @Override
    public void loadFromSoap(java.lang.Object paramObj,ExtendedSoapSerializationEnvelope __envelope)
    {
        if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;
        super.loadFromSoap(paramObj, __envelope);

    }

    @Override
    protected boolean loadProperty(PropertyInfo info,SoapObject soapObject,ExtendedSoapSerializationEnvelope __envelope)
    {
        java.lang.Object obj = info.getValue();
        if (info.name.equals("Usuario"))
        {
            if(obj!=null)
            {
                java.lang.Object j = obj;
                this.Usuario = (UsuarioEnrolamiento)__envelope.get(j,UsuarioEnrolamiento.class,false);
            }
            return true;
        }
        return super.loadProperty(info,soapObject,__envelope);
    }
    
    

    @Override
    public java.lang.Object getProperty(int propertyIndex) {
        int count = super.getPropertyCount();
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if(propertyIndex==count+0)
        {
            return this.Usuario!=null?this.Usuario:SoapPrimitive.NullSkip;
        }
        return super.getProperty(propertyIndex);
    }


    @Override
    public int getPropertyCount() {
        return super.getPropertyCount()+1;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        int count = super.getPropertyCount();
        if(propertyIndex==count+0)
        {
            info.type = UsuarioEnrolamiento.class;
            info.name = "Usuario";
            info.namespace= "http://casb.bytte.com.co/";
        }
        super.getPropertyInfo(propertyIndex,arg1,info);
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }



    protected UsuarioResponse(android.os.Parcel parcel)
    {
        super(parcel);
        this.Usuario = (UsuarioEnrolamiento)parcel.readValue(this.getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        
        super.writeToParcel(parcel, flags);
        parcel.writeValue(this.Usuario);
    }

    public static final Creator< UsuarioResponse> CREATOR = new Creator< UsuarioResponse>() {
            @Override
            public UsuarioResponse createFromParcel(android.os.Parcel in) {
                return new UsuarioResponse(in);
            }

            @Override
            public UsuarioResponse[] newArray(int size) {
                return new UsuarioResponse[size];
            }
        };    
}

