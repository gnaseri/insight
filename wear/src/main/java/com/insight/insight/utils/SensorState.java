package com.insight.insight.utils;


/**
 * Created by prajnashetty on 10/30/14.
 * This classs is not being used currently.
 */
public class SensorState 
{
	public static enum Bluetooth 
	{
		NONE("none"),
		BONDING("bonding"),
		BONDED("bonded");
		
		private String state;
		
		Bluetooth(String state)
		{
			this.state = state;
		}
		
		public String getState()
		{
			return state;
		}
	}
	
	public static enum Movement 
	{
		FAST("fast"),
		SLOW("slow"),
		STEADY("steady");
		
		private String state;
		
		Movement(String state)
		{
			this.state = state;
		}
		
		public String getState()
		{
			return state;
		}
	}
}
