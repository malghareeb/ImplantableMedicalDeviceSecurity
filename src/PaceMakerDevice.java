/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alghareebm
 */
public class PaceMakerDevice {
    
    private byte[] serialNumber;
    private byte[] modelNumber;
    private byte[] log;
    private byte[] measurement;
    
    
   public PaceMakerDevice()
   {
       serialNumber = null;
       modelNumber = null;
       log = null;
       measurement = null; 
   }
   
   public PaceMakerDevice(byte[] devSN, byte[] devModN, byte[] devLog, byte[] devMeasurement)
   {
        serialNumber = devSN;
        modelNumber = devModN;
        log = devLog;
        measurement = devMeasurement;
   }
   
    public void setSerialNumber (byte[] devSNum)
    {
        serialNumber = devSNum;
    }
    
    public void setModelNumber (byte[] devModNum)
    {
        modelNumber = devModNum;
    }
    
    public void setLog (byte[] devLog)
    {
        log = devLog;
    }
    
    public void setMeasurement (byte[] devMeasurements)
    {
        measurement = devMeasurements;
    }
  
    public byte[] getSerialNumber ()
    {
        return serialNumber;
    }
    
    public byte[] getModelNumber ()
    {
        return modelNumber;
    }
    
    public byte[] getLog ()
    {
        return log;
    }
    
    public byte[] getMeasurement ()
    {
        return measurement;
    }
    

}
