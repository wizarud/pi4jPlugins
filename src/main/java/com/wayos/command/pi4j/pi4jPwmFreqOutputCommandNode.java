package com.wayos.command.pi4j;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.pwm.*;
import com.wayos.MessageObject;
import com.wayos.Session;
import com.wayos.command.CommandNode;

public class pi4jPwmFreqOutputCommandNode extends CommandNode {

	public pi4jPwmFreqOutputCommandNode(Session session, String[] hooks) {
		super(session, hooks);
	}

	@Override
	public String execute(MessageObject messageObject) {
		
		String params = cleanHooksFrom(messageObject.toString());
		
		String [] tokens = params.split(" ");
		
		if (tokens.length!=3) {
			
			return "Invalid pi4j Parameters! Use <pin> <freq> <percent>";
		}
				
		int pin, freq, percent;
		
		try {
			
			pin = Integer.parseInt(tokens[0]);
			
			freq = Integer.parseInt(tokens[1]);
			
			percent = Integer.parseInt(tokens[2]);
			
		} catch (Exception e) {
			
			return e.getMessage() + ": Invalid pi4j Parameters! Use <pin> <freq> <percent>";
		}
		
		String id = "pi4jId:" + pin;
		
		String name = "Piezo Buzzer:" + pin;
		
        Context pi4j = Pi4J.newAutoContext();
        
        PwmConfig pwmConfig = Pwm.newConfigBuilder(pi4j)
                .id(id)
                .name(name)
                .address(pin) // GPIO number, not physical pin
                .frequency(freq) // Hz (note)
                .dutyCycle(percent)  // duty cycle
                .build();

        Pwm pwm = pi4j.create(pwmConfig);

        if (percent>0) {
        	
            // Start tone
            pwm.on();
            
    		return name + " " + freq + " " + percent;
    		
        }
        
        // Stop
        pwm.off();

        // Shutdown Pi4J
        pi4j.shutdown();
        
		return name + " " + freq + " stoped";
	}

}
