package com.wayos.command.pi4j;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.wayos.MessageObject;
import com.wayos.Session;
import com.wayos.command.CommandNode;

public class pi4jDigitalOutputCommandNode extends CommandNode {

	public pi4jDigitalOutputCommandNode(Session session, String[] hooks) {
		super(session, hooks);
	}

	@Override
	public String execute(MessageObject messageObject) {
		
		String params = cleanHooksFrom(messageObject.toString());
		
		String [] tokens = params.split(" ");
		
		if (tokens.length!=2) {
			
			return "Invalid pi4j Parameters! Use <pin> <hi|lo>";
		}
				
		int pin;
		
		try {
			
			pin = Integer.parseInt(tokens[0]);
			
		} catch (Exception e) {
			
			return e.getMessage() + ": Invalid pi4j Parameters! Use <pin> <hi|lo>";
		}
		
		String action = tokens[1];
		
		String id = "pi4jId:" + pin;
		
		String name = id;
		
        Context pi4j = Pi4J.newAutoContext();
        
        DigitalOutput output = pi4j.create(DigitalOutput.newConfigBuilder(pi4j)
                .id(id)
                .name(name)
                .address(pin) // mock pin address
//                .provider("mock-digital-output") // IMPORTANT for mock mode
                .build());
        
        if (action.equals("hi")) {
        	
            output.high();
            
    		return action;
    		
        } else if (action.equals("lo")) {
        	
            output.low();
            
    		return action;
            
        }
		
		return "Unknown action! Use hi|lo";
	}

}
