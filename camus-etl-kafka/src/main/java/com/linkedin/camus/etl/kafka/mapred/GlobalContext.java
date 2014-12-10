package com.linkedin.camus.etl.kafka.mapred;

import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 *   (sic)
 *      Unable to find a way to pass the context to 
 *       our message decoders so that they are able 
 *       to increment counters, 
 *      so pass it underhandedly via a ThreadLocal
 *
 */
public class GlobalContext  {
	private static GlobalContext _context = new GlobalContext();
	private  TaskAttemptContext taskContext;

	static public GlobalContext getInstance() {
	   return _context;	
	}
	
	static public TaskAttemptContext getContext() {
		return getInstance().taskContext;
	}
	
	static public void setContext( TaskAttemptContext ctxt) {
		getInstance().taskContext =  ctxt;
	}
	
}
