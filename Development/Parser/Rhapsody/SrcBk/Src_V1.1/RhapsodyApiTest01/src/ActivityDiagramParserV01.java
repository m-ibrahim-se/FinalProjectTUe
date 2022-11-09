import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.telelogic.rhapsody.core.IRPApplication;
import com.telelogic.rhapsody.core.IRPCollection;
import com.telelogic.rhapsody.core.IRPDependency;
import com.telelogic.rhapsody.core.IRPDiagram;
import com.telelogic.rhapsody.core.IRPInstanceValue;
import com.telelogic.rhapsody.core.IRPModelElement;
import com.telelogic.rhapsody.core.IRPPackage;
import com.telelogic.rhapsody.core.IRPPort;
import com.telelogic.rhapsody.core.IRPProfile;
import com.telelogic.rhapsody.core.IRPProject;
import com.telelogic.rhapsody.core.IRPState;
import com.telelogic.rhapsody.core.IRPStereotype;
import com.telelogic.rhapsody.core.IRPSwimlane;
import com.telelogic.rhapsody.core.IRPTag;
import com.telelogic.rhapsody.core.IRPTransition;
import com.telelogic.rhapsody.core.RhapsodyAppServer;

public class ActivityDiagramParserV01 {
	public static void main(String[] args) {
		System.out.println("Start of Parser");		
		IRPApplication app;
		app = RhapsodyAppServer.getActiveRhapsodyApplication();
		
		IRPProject prj;
		String modelFile = "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\ADSimulation\\ADSimulation.rpyx";
		app.openProject(modelFile);
		prj = app.activeProject();
		System.out.println("Model File: "+modelFile);
		System.out.println("Project: "+prj);				
		
		
		IRPModelElement modelElement = app.getSelectedElement();
		//System.out.println(modelElement); 
		System.out.println("Model Name: "+modelElement.getDisplayName());
		System.out.println("Model Desc: "+modelElement.getDescription());
		
//		System.out.println(modelElement.getNestedElementsByMetaClass("State", 1) );
		
		//Extracting dependency information both to/from in Rational Rhapsody model
//		IRPModelElement myEle = app.getSelectedElement();
//		IRPDependency myDep = (IRPDependency) myEle;
//
//		System.out.println(myDep.getDependent().getName());
//		System.out.println(myDep.getDependsOn().getName());
						
		
		Map<String,String> theMap = new HashMap();
		IRPCollection allColl = prj.getNestedElementsRecursive();
		System.out.println("LineCount	||	Name	|| MetaClass	||	FullPathName	|| Owner/Parent	|| InterfaceName	|| Id");
		int lineCount = 1;
		for (int i = 1; i <= allColl.getCount(); i++) {
			IRPModelElement element = (IRPModelElement)allColl.getItem(i);
			theMap.put(element.getMetaClass(), element.getInterfaceName());
			System.out.println(lineCount+"||"+element.getName() + "||" + element.getMetaClass() +"||"+ element.getFullPathName() + "||" +element.getOwner() +"||"+ element.getInterfaceName()+"||"+ element.toString());
			lineCount = lineCount  + 1;
		}
		
		System.out.println("<><><><><><><><><><><><><><><><><><><><><><>");
		Iterator packIter= prj.getPackages().toList().iterator();
		while(packIter.hasNext()) {
			Object contEleObj = packIter.next();	
			System.out.println("packIter: "+contEleObj);
			
			if (contEleObj instanceof IRPProfile) {		;
				IRPProfile irpProfile = (IRPProfile) contEleObj;
				System.out.println("irpProfile: "+irpProfile.getName());
			}
			else if (contEleObj instanceof IRPPackage) {
				IRPPackage irpPackage = (IRPPackage) contEleObj;
				System.out.println("irpPackage: "+irpPackage.getName()+"; "+irpPackage.getMetaClass()+"; "+irpPackage.getOwner().getName()+"; "+contEleObj);
			}
		}
		
		System.out.println("===============================================================================");
		System.out.println("get Activity Diagram elements information using JAVA API in Rational Rhapsody");
		//https://www.ibm.com/support/pages/how-get-activity-diagram-elements-information-using-java-api-rational-rhapsody
		System.out.println("===============================================================================");
		
		
		System.out.print("Activity Diagram --> ");
		System.out.println(modelElement.getName());
		  
		  Iterator sterIter = modelElement.getStereotypes().toList().iterator();
		  
		  while(sterIter.hasNext()){
		   Object contEleObj = sterIter.next();
		   IRPStereotype stereotype = (IRPStereotype) contEleObj;
		   System.out.print("Stereotype --> ");
		   System.out.println(stereotype.getName());
		  }

		  Iterator contEleIter = modelElement.getNestedElementsRecursive().toList().iterator();
		  while(contEleIter.hasNext()){
		   Object contEleObj = contEleIter.next();
		   if (contEleObj instanceof IRPTag) {
		    IRPTag tag = (IRPTag) contEleObj;
		    System.out.print("Tag --> ");
		    System.out.print(tag.getName());
		    System.out.print("(Value: ");
		    System.out.print(tag.getValue());
		    System.out.println(")");
		   }

		   if (contEleObj instanceof IRPSwimlane) {
		    IRPSwimlane flow = (IRPSwimlane) contEleObj;
		    System.out.print("Swimlane --> ");
		    System.out.println(flow.getDisplayName());
		   }

		   if (contEleObj instanceof IRPTransition) {
		    IRPTransition flow = (IRPTransition) contEleObj;
		    System.out.print("Flow --> ");
		    System.out.println(flow.getName());
		   }

		   if (contEleObj instanceof IRPState) {
		    IRPState action = (IRPState) contEleObj;
		    if(action.getName().contains("ROOT"))
		     continue;
		    System.out.print("Action --> ");
		    System.out.println(action.getName());
		   }

		  }
		
		//if the selected element is an instance of Tag
//		  System.out.println("------ Selected element is an instance of Tag ------");
//
//		  if (modelElement instanceof IRPTag){
//
//			  IRPTag tag = (IRPTag)modelElement;
//	
//			  //call getValueSpecifications() to retrieve tag value collection
//	
//			  IRPCollection valSpecs = tag.getValueSpecifications();
//	
//			  Iterator looper = valSpecs.toList().iterator();
//	
//			  //call getValue() to retrieve each element instance set as the tag value
//	
//			  while (looper.hasNext()){
//	
//			  IRPInstanceValue ins = (IRPInstanceValue)looper.next();
//	
//			  System.out.println("element = " +ins.getValue()); 
//			  }
//		  }
		  
		System.out.println("End of Parser");
		
	}
}
