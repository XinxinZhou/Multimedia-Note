package com.example.multimedianote;

public class Tools {
	
	public Tools()
	{
		
	}
	public int indexOfFlag(String context, String flag,int start)
	{
		int i=context.indexOf(flag,start);
		return i;
	}
	
	public String separateContext(String context, int indexStart, int indexEnd)
	{
		context=context.substring(0, indexStart)+context.substring(indexEnd, context.length());
		return context;
	}
	public String combineContext(String context, int indexStart, int indexEnd)
	{
		context=context.substring(indexStart,indexEnd);
		return context;
	}
	
}
