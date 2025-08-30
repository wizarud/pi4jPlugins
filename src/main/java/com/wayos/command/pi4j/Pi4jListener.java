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
		sampleEntity1Map.put("tool-label", "Pi Out");
		sampleEntity1Map.put("tool-color", "#D3D70D");
		sampleEntity1Map.put("tool-tip", "Pi4J Entity!");
		sampleEntity1Map.put("entity-resps", "["
				+ "{"
				+ "	txt: 'CMD',"
				+ "	params: [{ parameterName: 'hook', value: 'pi4jOutputCMD' }, { parameterName: 'params', value: 'pinNo hi|lo' }]"
				+ "}"
				+ "]");
		
		/**
		 * DOM Id query pattern to apply colour
		 * extCommand-<Hook>
		 */
		logicDesignerExtToolMap.put("extCommand-pi4jOutputCMD", sampleEntity1Map);
		
		System.out.println("Loaded Sample Tools: " + sce.getServletContext().getAttribute("logicDesignerExtToolMap"));		
		
	}

}
