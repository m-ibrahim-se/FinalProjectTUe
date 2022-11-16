import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.telelogic.rhapsody.core.IRPAction;
import com.telelogic.rhapsody.core.IRPApplication;
import com.telelogic.rhapsody.core.IRPCollection;
import com.telelogic.rhapsody.core.IRPDependency;
import com.telelogic.rhapsody.core.IRPDiagram;
import com.telelogic.rhapsody.core.IRPFlowchart;
import com.telelogic.rhapsody.core.IRPGuard;
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

//Algorithm 1: Example data extraction.
//The actual extraction of data is quite simple, we first extract all packages then loop over the
//packages and extract the diagram with a get command. For example:
//ArrayList packages = project.getPackages();
//foreach pack in packages do
//	ArrayList useCaseDiagrams = pack.getUseCaseDiagrams();
//	foreach diagram in useCaseDiagrams do
//		print diagram elements;
//	end
//end


public class ActivityDiagramParserV02 {
	public static void main(String[] args) {
		System.out.println("Start of Parser");

		// Data parsing info
		String allParsedData = "";
		String parsedDataFilePath = "C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\ParsedDataFiles\\";

		IRPApplication app;
		app = RhapsodyAppServer.getActiveRhapsodyApplication();

		IRPProject prj;
		//String modelFile = "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\ADSimulation\\ADSimulation.rpyx";
		//String modelFile = "D:\\Development\\Final_Project\\TUe_Project_Dev\\DT_truck\\Standalone IBM Rhapsody\\Project.rpyx";
		String modelFile = "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\NewModels\\ADSimulation\\ADSimulation.rpyx";

		int index = modelFile.lastIndexOf('.');
		String modelFileExtension = "";
		if (index > 0) {
			modelFileExtension = modelFile.substring(index + 1);
			System.out.println("Model File Extension: " + modelFileExtension);
		}
		if (modelFileExtension.equalsIgnoreCase("rpyx")){
			app.openProject(modelFile);
			prj = app.activeProject();
			
			IRPModelElement ele = app.getSelectedElement();
			System.out.println("MetaClass: "+ele.getMetaClass());
			if(ele.getMetaClass().equals("UseCase")) {
				System.out.println("UseCase");
			}
			
			IRPCollection packages = prj.getPackages();
			System.out.println("Package Count = "+packages.getCount());
			System.out.println("LineCount	||	Name	|| MetaClass	||	FullPathName	|| Owner/Parent	|| InterfaceName	|| Id");
			int lineCount0 = 1;
			for (int i = 1; i <= packages.getCount(); i++) {
				IRPModelElement element = (IRPModelElement)packages.getItem(i);
				System.out.println(lineCount0+"||"+element.getName() + "||" + element.getMetaClass() +"||"+ element.getFullPathName() + "||" +element.getOwner() +"||"+ element.getInterfaceName()+"||"+ element.toString());
				lineCount0 = lineCount0 + 1;
				
				
				if(element.getMetaClass().equalsIgnoreCase("Package")) {					
					System.out.println("In side package");
					IRPCollection allCollection1 = element.getNestedElementsRecursive();	
					System.out.println("Package Element Count: "+allCollection1.getCount());
					
					Map<String,String> theMap1 = new HashMap();
					
					
					int lineCount1 = 1;
					for (int i1 = 1; i1 <= allCollection1.getCount(); i1++) {
						IRPModelElement element1 = (IRPModelElement)allCollection1.getItem(i1);
						
						//theMap1.put(element1.getMetaClass(), element1.getName());
						System.out.println("LineCount: "+lineCount1+ " -> "+element1.getMetaClass()+"<------>"+element1.getName());
						
						if(element1.getMetaClass().equalsIgnoreCase("Class")) {
							System.out.println("In side class Diagram");
							IRPCollection allCollection2 = element1.getNestedElementsRecursive();	
							System.out.println("Block Element Count: "+allCollection2.getCount());
							
							int lineCount2 = 1;
							for (int i2 = 1; i2 <= allCollection1.getCount(); i2++) {
								IRPModelElement element2 = (IRPModelElement)allCollection2.getItem(i2);
								
								System.out.println(lineCount2+"||"+element2.getName() + "||" + element2.getMetaClass() +"||"+ element2.getFullPathName() + "||" +element2.getOwner() +"||"+ element2.getInterfaceName()+"||"+ element2.toString());
								System.out.print("||"+element2.getDescription());
								System.out.print("||"+element2.getDisplayName());//Display label(name) of the element
								System.out.print("||"+element2.getGUID());
								System.out.print("||"+element2.getPropertyValue("Value"));
								System.out.print("||"+element2.getRequirementTraceabilityHandle());
								System.out.print("||"+element2.getUserDefinedMetaClass());// ActivityFinal_State will be mentioned as "ActivityFinal", Transition type "ObjectFlow"/"ControlFlow"
								System.out.println();
								lineCount2 = lineCount2 + 1;
//								if(element2.getMetaClass().equals("Action")) {
//									IRPAction action = (IRPAction)element2.getAllTags();
//								}								
							}
						}
						System.out.println(lineCount1+"||"+element1.getName() + "||" + element1.getMetaClass() +"||"+ element1.getFullPathName() + "||" +element1.getOwner() +"||"+ element1.getInterfaceName()+"||"+ element1.toString());
						lineCount1 = lineCount1 + 1;
					}	
					
//					System.out.println("===== Set =====");
//					Set<String> mapKeySet = theMap1.keySet();
//					for (String strMetaClass : mapKeySet) {
//						System.out.println(strMetaClass + "<<---->>" + theMap1.get(strMetaClass));
//					}			
				}
				
			}
			
			/*
			IRPCollection allCollection1 = prj.getNestedElementsRecursive();
			Map<String,String> theMap1 = new HashMap();
			IRPCollection allCollection1 = prj.getNestedElementsRecursive();
			System.out.println("===== Map =====");
			for (int i = 1; i <= allCollection1.getCount(); i++) {
				IRPModelElement element = (IRPModelElement)allCollection1.getItem(i);
				theMap1.put(element.getMetaClass(), element.getInterfaceName());
				System.out.println("MetaClass:"+element.getMetaClass() +"--> "+"InterfaceName: "+element.getInterfaceName());
			}
			System.out.println("===== Set =====");
			Set<String> mapKeySet = theMap1.keySet();
			for (String strMetaClass : mapKeySet) {
				System.out.println(strMetaClass + "<<---->>" + theMap1.get(strMetaClass));
			}
			
			//Algorithm 1: Example data extraction.
//			ArrayList packages = project.getPackages();
//			foreach pack in packages do
//			ArrayList useCaseDiagrams = pack.getUseCaseDiagrams();
//			foreach diagram in useCaseDiagrams do
//			print diagram elements;
//			end
//			end
			 */
			
			
			Map<String,String> theMap = new HashMap();
			IRPCollection allCollection = prj.getNestedElementsRecursive();
			System.out.println("LineCount	||	Name	|| MetaClass	||	FullPathName	|| Owner/Parent	|| InterfaceName	|| Id");
			int lineCount = 1;
			for (int i = 1; i <= allCollection.getCount(); i++) {
				IRPModelElement element = (IRPModelElement)allCollection.getItem(i);
				theMap.put(element.getMetaClass(), element.getInterfaceName());
				System.out.println(lineCount+"||"+element.getName() + "||" + element.getMetaClass() +"||"+ element.getFullPathName() + "||" +element.getOwner() +"||"+ element.getInterfaceName()+"||"+ element.toString());
				lineCount = lineCount  + 1;
			}
			
			IRPModelElement modelElement = app.getSelectedElement();

			String modelFileName = "";

			System.out.println("---- Activity Diagram Information Analysis ---- ");

			if (modelElement.getInterfaceName().equals("IRPProject")) {				
				JSONObject jsonObjModel = new JSONObject();

				jsonObjModel.put("id", modelElement.toString());
				jsonObjModel.put("type", "node");
				
				JSONArray labelsArray = new JSONArray();
				labelsArray.add("Model");

				jsonObjModel.put("labels", labelsArray);

				JSONObject jsonObjProperties = new JSONObject();
				jsonObjProperties.put("name", modelElement.getName());
				modelFileName = modelElement.getName();
				jsonObjProperties.put("description", modelElement.getDescription());
				jsonObjProperties.put("type", modelElement.getMetaClass());
//				jsonObjProperties.put("type", modelElement.getInterfaceName());
				jsonObjProperties.put("parent", modelElement.getOwner());
//				jsonObjProperties.put("interfaceName", modelElement.getInterfaceName());

				jsonObjModel.put("properties", jsonObjProperties);

				allParsedData = allParsedData.concat(jsonObjModel.toJSONString()).concat("\n");
				System.out.println(allParsedData);
				

				System.out.println("id:" + modelElement.toString() + " ,Type: 'Node'" + ", labels: 'Project, '"
						+ "Properties: Name: " + modelElement.getName() + ", Description: " + modelElement.getDescription()
						+ " ,MetaClass: " + modelElement.getMetaClass() + ", Parent: " + modelElement.getOwner()
						+ ", InterfaceName: " + modelElement.getInterfaceName());
			}

			try {
				FileWriter file = new FileWriter(
						"C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\ParsedDataFiles\\"
								+ modelFileName + ".json");
				// file.write(jsonObject.toJSONString());
				file.write(allParsedData);
				file.close();
				System.out.println("JSON file created successfully");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println(e);
			}
			
			Iterator sterIter = modelElement.getStereotypes().toList().iterator();

			while (sterIter.hasNext()) {
				Object contEleObj = sterIter.next();
				IRPStereotype stereotype = (IRPStereotype) contEleObj;
				System.out.println("Stereotype --> " + stereotype.getName());
			}

			Iterator contEleIter = modelElement.getNestedElementsRecursive().toList().iterator();
			System.out.println(contEleIter);

			while (contEleIter.hasNext()) {
				Object contEleObj = contEleIter.next();
//				if (contEleObj instanceof IRPTag) {
//					IRPTag tag = (IRPTag) contEleObj;
//					System.out.print("Tag --> ");
//					System.out.print(tag.getName());
//					System.out.print("(Value: ");
//					System.out.print(tag.getValue());
//					System.out.println(")");
//				}

				if (contEleObj instanceof IRPFlowchart) {
					IRPFlowchart flowchart = (IRPFlowchart) contEleObj;
					System.out.println("Flowchart --> " + flowchart.getDisplayName());
				}

				if (contEleObj instanceof IRPState) {
					IRPState action = (IRPState) contEleObj;
					if (action.getName().contains("ROOT")) {
						System.out.println("Root - Initial Action");
						continue;
					}
					System.out.println("Action --> " + action.getName());
				}

//				if (contEleObj instanceof IRPSwimlane) {
//					IRPSwimlane flow = (IRPSwimlane) contEleObj;
//					System.out.print("Swimlane --> ");
//					System.out.println(flow.getDisplayName());
//				}

				if (contEleObj instanceof IRPTransition) {
					IRPTransition flow = (IRPTransition) contEleObj;
					System.out.println("Flow --> " + flow.getName());
				}

			}
		}

		

		

		

		// if the selected element is an instance of Tag
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
