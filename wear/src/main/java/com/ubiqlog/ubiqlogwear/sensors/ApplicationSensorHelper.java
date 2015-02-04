package com.ubiqlog.ubiqlogwear.sensors;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ubiqlog.ubiqlogwear.core.DataAcquisitor;
import com.ubiqlog.ubiqlogwear.utils.JsonEncodeDecode;

public class ApplicationSensorHelper {

	public  Map<String, Date> _apps;
	private static ApplicationSensorHelper _instance = null;
	
	public static ApplicationSensorHelper Instance(){
		if(_instance ==null){
			_instance = new ApplicationSensorHelper();
		}
		
		return _instance;
	}
	
	ApplicationSensorHelper(){
		_apps = new HashMap<String, Date>();
	}
	
	public synchronized void logApps(ArrayList<String> _foundApps, String friendlyName , long timeInterval, Date endDate){
		synchronized(_apps){
			
			Iterator<Map.Entry<String, Date>> entries = _apps.entrySet().iterator();
			while (entries.hasNext()) {
				 Map.Entry<String,Date> entry = (Map.Entry<String,Date>) entries.next();
				if(!_foundApps.contains(entry.getKey())){
					//Log.d("APPS", "Write to log: " + entry.getKey());
					String jsonString = JsonEncodeDecode.EncodeApplication(friendlyName,entry.getKey(), entry.getValue(), endDate);
					DataAcquisitor.dataBuffer.add(jsonString);
                    Log.i("Application-Logging", "--- Active Applications " + jsonString);
					entries.remove();
					
				}
				else if(entry.getValue().before(new Date(new Date().getTime()-timeInterval))){					
					//Log.d("APPS", "Write to log from interval: " + entry.getKey());
					String jsonString = JsonEncodeDecode.EncodeApplication(friendlyName,entry.getKey(), entry.getValue(),endDate);
					DataAcquisitor.dataBuffer.add(jsonString);
                    Log.i("Application-Logging", "--- Active Applications " + jsonString);
					entry.setValue(new Date());
					
				}
			}
			
		}
	}
	
}
