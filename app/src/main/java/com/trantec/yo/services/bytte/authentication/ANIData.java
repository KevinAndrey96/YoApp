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

public class ANIData extends AttributeContainer implements KvmSerializable,java.io.Serializable,android.os.Parcelable
{

    
    public String AnioResolucion;
    
    public String CodigoErrorDatosCedula;
    
    public String DepartamentoExpedicion;
    
    public String EstadoCedula;
    
    public String FechaExpedicion;
    
    public String MunicipioExpedicion;
    
    public String NUIP;
    
    public String NumeroResolucion;
    
    public String Particula;
    
    public String PrimerApellido;
    
    public String PrimerNombre;
    
    public String SegundoApellido;
    
    public String SegundoNombre;
    private transient java.lang.Object __source;    
    

    public ANIData()
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
        if (info.name.equals("AnioResolucion"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.AnioResolucion = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.AnioResolucion = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("CodigoErrorDatosCedula"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.CodigoErrorDatosCedula = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.CodigoErrorDatosCedula = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("DepartamentoExpedicion"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.DepartamentoExpedicion = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.DepartamentoExpedicion = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("EstadoCedula"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.EstadoCedula = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.EstadoCedula = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("FechaExpedicion"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.FechaExpedicion = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.FechaExpedicion = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("MunicipioExpedicion"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.MunicipioExpedicion = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.MunicipioExpedicion = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("NUIP"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.NUIP = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.NUIP = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("NumeroResolucion"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.NumeroResolucion = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.NumeroResolucion = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Particula"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Particula = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.Particula = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("PrimerApellido"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.PrimerApellido = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.PrimerApellido = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("PrimerNombre"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.PrimerNombre = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.PrimerNombre = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("SegundoApellido"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.SegundoApellido = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.SegundoApellido = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("SegundoNombre"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.SegundoNombre = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.SegundoNombre = (String)obj;
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
            return this.AnioResolucion!=null?this.AnioResolucion:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==1)
        {
            return this.CodigoErrorDatosCedula!=null?this.CodigoErrorDatosCedula:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==2)
        {
            return this.DepartamentoExpedicion!=null?this.DepartamentoExpedicion:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==3)
        {
            return this.EstadoCedula!=null?this.EstadoCedula:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==4)
        {
            return this.FechaExpedicion!=null?this.FechaExpedicion:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==5)
        {
            return this.MunicipioExpedicion!=null?this.MunicipioExpedicion:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==6)
        {
            return this.NUIP!=null?this.NUIP:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==7)
        {
            return this.NumeroResolucion!=null?this.NumeroResolucion:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==8)
        {
            return this.Particula!=null?this.Particula:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==9)
        {
            return this.PrimerApellido!=null?this.PrimerApellido:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==10)
        {
            return this.PrimerNombre!=null?this.PrimerNombre:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==11)
        {
            return this.SegundoApellido!=null?this.SegundoApellido:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==12)
        {
            return this.SegundoNombre!=null?this.SegundoNombre:SoapPrimitive.NullSkip;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 13;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "AnioResolucion";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==1)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "CodigoErrorDatosCedula";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==2)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "DepartamentoExpedicion";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==3)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "EstadoCedula";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==4)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "FechaExpedicion";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==5)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "MunicipioExpedicion";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==6)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "NUIP";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==7)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "NumeroResolucion";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==8)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "Particula";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==9)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "PrimerApellido";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==10)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "PrimerNombre";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==11)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "SegundoApellido";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==12)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "SegundoNombre";
            info.namespace= "http://casb.bytte.com.co/";
        }
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }



    protected ANIData(android.os.Parcel parcel)
    {
        
        this.AnioResolucion = (String)parcel.readValue(null);
        this.CodigoErrorDatosCedula = (String)parcel.readValue(null);
        this.DepartamentoExpedicion = (String)parcel.readValue(null);
        this.EstadoCedula = (String)parcel.readValue(null);
        this.FechaExpedicion = (String)parcel.readValue(null);
        this.MunicipioExpedicion = (String)parcel.readValue(null);
        this.NUIP = (String)parcel.readValue(null);
        this.NumeroResolucion = (String)parcel.readValue(null);
        this.Particula = (String)parcel.readValue(null);
        this.PrimerApellido = (String)parcel.readValue(null);
        this.PrimerNombre = (String)parcel.readValue(null);
        this.SegundoApellido = (String)parcel.readValue(null);
        this.SegundoNombre = (String)parcel.readValue(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        
        
        parcel.writeValue(this.AnioResolucion);
        parcel.writeValue(this.CodigoErrorDatosCedula);
        parcel.writeValue(this.DepartamentoExpedicion);
        parcel.writeValue(this.EstadoCedula);
        parcel.writeValue(this.FechaExpedicion);
        parcel.writeValue(this.MunicipioExpedicion);
        parcel.writeValue(this.NUIP);
        parcel.writeValue(this.NumeroResolucion);
        parcel.writeValue(this.Particula);
        parcel.writeValue(this.PrimerApellido);
        parcel.writeValue(this.PrimerNombre);
        parcel.writeValue(this.SegundoApellido);
        parcel.writeValue(this.SegundoNombre);
    }

    public static final Creator< ANIData> CREATOR = new Creator< ANIData>() {
            @Override
            public ANIData createFromParcel(android.os.Parcel in) {
                return new ANIData(in);
            }

            @Override
            public ANIData[] newArray(int size) {
                return new ANIData[size];
            }
        };    
}

