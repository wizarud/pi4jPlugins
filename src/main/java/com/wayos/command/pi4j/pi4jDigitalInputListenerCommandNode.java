package com.wayos.command.pi4j;

import java.util.HashMap;
import java.util.Map;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.wayos.Configuration;
import com.wayos.MessageObject;
import com.wayos.Session;
import com.wayos.command.CommandNode;
import com.wayos.util.API;

public class pi4jDigitalInputListenerCommandNode extends CommandNode {
	
	class SingleDigitalStateChangeListener implements DigitalStateChangeListener {
		
		int pin;
		String domain;
		String contextName;
		String sessionId;
		String message;
		
		SingleDigitalStateChangeListener(int pin, String domain, String contextName, String sessionId, String message) {
			this.pin = pin;
			this.domain = domain;
			this.contextName = contextName;
			this.sessionId = sessionId;
			this.message = message;
		}

		@Override
		public void onDigitalStateChange(DigitalStateChangeEvent event) {
			
    	    System.out.println("Input changed from: " + pin + " -> " + event.state());
    	    
			String responseText = API.call(Configuration.api_domain, contextName, sessionId, message);
			
			System.out.println("Response: " + responseText);
						
		}
		
	}
	
	static Map<Integer, SingleDigitalStateChangeListener> singleDigitalStateChangeListenerMap;
	
	public pi4jDigitalInputListenerCommandNode(Session session, String[] hooks) {
		super(session, hooks);
		
		singleDigitalStateChangeListenerMap = new HashMap<>();
	}

	@Override
	public String execute(MessageObject messageObject) {
		
		String params = cleanHooksFrom(messageObject.toString());
		
		String [] tokens = params.split(" ");
		
		if (tokens.length!=2) {
			
			return "Invalid pi4j Parameters! Use <pin> <message>";
		}
				
		int pin;
		
		try {
			
			pin = Integer.parseInt(tokens[0]);
			
		} catch (Exception e) {
			
			return e.getMessage() + ": Invalid pi4j Parameters! Use <pin> <message>";
		}
		
		String message = tokens[1];
		
        Context pi4j = Pi4J.newAutoContext();
        
        try {
        	
        	String contextName = session.context().name();
        	
        	String sessionId = session.vars("#sessionId");
        	
        	DigitalInput input = pi4j.create(DigitalInput.newConfigBuilder(pi4j)
        	        .address(pin)
        	        .build());
        	
        	/**
        	 * Cancel If there are pending listener for this pin
        	 */
        	SingleDigitalStateChangeListener singleDigitalStateChangeListener = singleDigitalStateChangeListenerMap.get(pin);
        	
        	if (singleDigitalStateChangeListener!=null) {
        		
        		input.removeListener(singleDigitalStateChangeListener);
        		
        	}
        	
        	/**
        	 * Override listener for new context, sessionId or message for this pin
        	 */
        	singleDigitalStateChangeListener = new SingleDigitalStateChangeListener(pin, Configuration.api_domain, contextName, sessionId, message);

        	input.addListener(singleDigitalStateChangeListener);
        	
        	singleDigitalStateChangeListenerMap.put(pin, singleDigitalStateChangeListener);
            
    		return pin + " is listening";
    		
        } catch (Exception e) {
        	
        	return e.getMessage();
        }

	}

}
