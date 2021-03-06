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

public class DocumentoData extends AttributeContainer implements KvmSerializable,java.io.Serializable,android.os.Parcelable
{

    
    public String VersionCedula;
    
    public String NumeroTarjeta;
    
    public String NumeroCedula;
    
    public String PrimerApellido;
    
    public String SegundoApellido;
    
    public String PrimerNombre;
    
    public String SegundoNombre;
    
    public String NombresCompletos;
    
    public String Sexo;
    
    public String FechaNacimiento;
    
    public String RH;
    
    public String Template;
    
    public String TipoDedo1;
    
    public String TipoDedo2;
    private transient java.lang.Object __source;    
    

    public DocumentoData()
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
        if (info.name.equals("VersionCedula"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.VersionCedula = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.VersionCedula = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("NumeroTarjeta"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.NumeroTarjeta = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.NumeroTarjeta = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("NumeroCedula"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.NumeroCedula = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.NumeroCedula = (String)obj;
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
        if (info.name.equals("NombresCompletos"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.NombresCompletos = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.NombresCompletos = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Sexo"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Sexo = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.Sexo = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("FechaNacimiento"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.FechaNacimiento = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.FechaNacimiento = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("RH"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.RH = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.RH = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("Template"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.Template = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.Template = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("TipoDedo1"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.TipoDedo1 = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.TipoDedo1 = (String)obj;
                }
            }
            return true;
        }
        if (info.name.equals("TipoDedo2"))
        {
            if(obj!=null)
            {
                if (obj.getClass().equals(SoapPrimitive.class))
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    if(j.toString()!=null)
                    {
                        this.TipoDedo2 = j.toString();
                    }
                }
                else if (obj instanceof String){
                    this.TipoDedo2 = (String)obj;
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
            return this.VersionCedula!=null?this.VersionCedula:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==1)
        {
            return this.NumeroTarjeta!=null?this.NumeroTarjeta:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==2)
        {
            return this.NumeroCedula!=null?this.NumeroCedula:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==3)
        {
            return this.PrimerApellido!=null?this.PrimerApellido:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==4)
        {
            return this.SegundoApellido!=null?this.SegundoApellido:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==5)
        {
            return this.PrimerNombre!=null?this.PrimerNombre:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==6)
        {
            return this.SegundoNombre!=null?this.SegundoNombre:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==7)
        {
            return this.NombresCompletos!=null?this.NombresCompletos:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==8)
        {
            return this.Sexo!=null?this.Sexo:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==9)
        {
            return this.FechaNacimiento!=null?this.FechaNacimiento:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==10)
        {
            return this.RH!=null?this.RH:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==11)
        {
            return this.Template!=null?this.Template:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==12)
        {
            return this.TipoDedo1!=null?this.TipoDedo1:SoapPrimitive.NullSkip;
        }
        if(propertyIndex==13)
        {
            return this.TipoDedo2!=null?this.TipoDedo2:SoapPrimitive.NullSkip;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 14;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "VersionCedula";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==1)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "NumeroTarjeta";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==2)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "NumeroCedula";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==3)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "PrimerApellido";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==4)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "SegundoApellido";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==5)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "PrimerNombre";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==6)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "SegundoNombre";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==7)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "NombresCompletos";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==8)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "Sexo";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==9)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "FechaNacimiento";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==10)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "RH";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==11)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "Template";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==12)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "TipoDedo1";
            info.namespace= "http://casb.bytte.com.co/";
        }
        if(propertyIndex==13)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "TipoDedo2";
            info.namespace= "http://casb.bytte.com.co/";
        }
    }
    
    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }



    protected DocumentoData(android.os.Parcel parcel)
    {
        
        this.VersionCedula = (String)parcel.readValue(null);
        this.NumeroTarjeta = (String)parcel.readValue(null);
        this.NumeroCedula = (String)parcel.readValue(null);
        this.PrimerApellido = (String)parcel.readValue(null);
        this.SegundoApellido = (String)parcel.readValue(null);
        this.PrimerNombre = (String)parcel.readValue(null);
        this.SegundoNombre = (String)parcel.readValue(null);
        this.NombresCompletos = (String)parcel.readValue(null);
        this.Sexo = (String)parcel.readValue(null);
        this.FechaNacimiento = (String)parcel.readValue(null);
        this.RH = (String)parcel.readValue(null);
        this.Template = (String)parcel.readValue(null);
        this.TipoDedo1 = (String)parcel.readValue(null);
        this.TipoDedo2 = (String)parcel.readValue(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        
        
        parcel.writeValue(this.VersionCedula);
        parcel.writeValue(this.NumeroTarjeta);
        parcel.writeValue(this.NumeroCedula);
        parcel.writeValue(this.PrimerApellido);
        parcel.writeValue(this.SegundoApellido);
        parcel.writeValue(this.PrimerNombre);
        parcel.writeValue(this.SegundoNombre);
        parcel.writeValue(this.NombresCompletos);
        parcel.writeValue(this.Sexo);
        parcel.writeValue(this.FechaNacimiento);
        parcel.writeValue(this.RH);
        parcel.writeValue(this.Template);
        parcel.writeValue(this.TipoDedo1);
        parcel.writeValue(this.TipoDedo2);
    }

    public static final Creator< DocumentoData> CREATOR = new Creator< DocumentoData>() {
            @Override
            public DocumentoData createFromParcel(android.os.Parcel in) {
                return new DocumentoData(in);
            }

            @Override
            public DocumentoData[] newArray(int size) {
                return new DocumentoData[size];
            }
        };    
}

