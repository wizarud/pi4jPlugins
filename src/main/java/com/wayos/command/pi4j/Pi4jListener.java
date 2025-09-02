package com.wayos.command.pi4j;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import com.wayos.Session;
import com.wayos.command.wakeup.ExtensionSupportWakeupCommandNode;

@WebListener
public class Pi4jListener extends ExtensionSupportWakeupCommandNode.WebListener {

	@Override
	public void wakup(Session session) {
		
		session.commandList().add(new pi4jDigitalOutputCommandNode(session, new String[]{"pi4jOutputCMD"}));
		
		session.commandList().add(new pi4jPwmFreqOutputCommandNode(session, new String[]{"pi4jFreqOutputCMD"}));
		
		System.out.println(session + " pi4j Commands ready..");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		super.contextInitialized(sce);
		
		/**
		 * TODO: Check why too many load!!!!
		 */
		
		Map<String, Map<String, String>> logicDesignerExtToolMap = 
				(Map<String, Map<String, String>>)sce.getServletContext().getAttribute("logicDesignerExtToolMap");		
		
		Map<String, String> sampleEntity1Map = new HashMap<>();
		sampleEntity1Map.put("tool-label", "Digit");
		sampleEntity1Map.put("tool-color", "#D3D70D");
		sampleEntity1Map.put("tool-tip", "Pi4J Entity!");
		sampleEntity1Map.put("entity-resps", "["
				+ "{"
				+ "	txt: 'CMD',"
				+ "	params: [{ parameterName: 'hook', value: 'pi4jOutputCMD' }, { parameterName: 'params', value: 'GPIO hi|lo' }]"
				+ "}"
				+ "]");
		
		Map<String, String> sampleEntity2Map = new HashMap<>();
		sampleEntity2Map.put("tool-label", "Piezo");
		sampleEntity2Map.put("tool-color", "#8FA31E");
		sampleEntity2Map.put("tool-tip", "Pi4J Pwm Freq for Piezo Buzzo Entity!");
		sampleEntity2Map.put("entity-resps", "["
				+ "{"
				+ "	txt: 'CMD',"
				+ "	params: [{ parameterName: 'hook', value: 'pi4jFreqOutputCMD' }, { parameterName: 'params', value: 'GPIO 440 100' }]"
				+ "}"
				+ "]");
		/**
		 * DOM Id query pattern to apply colour
		 * extCommand-<Hook>
		 */
		logicDesignerExtToolMap.put("extCommand-pi4jOutputCMD", sampleEntity1Map);
		logicDesignerExtToolMap.put("extCommand-pi4jFreqOutputCMD", sampleEntity2Map);

		System.out.println("Loaded Sample Tools: " + sce.getServletContext().getAttribute("logicDesignerExtToolMap"));		
		
	}

}
