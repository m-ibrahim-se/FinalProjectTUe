package Parser;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.telelogic.rhapsody.core.IRPApplication;
import com.telelogic.rhapsody.core.IRPCollection;
import com.telelogic.rhapsody.core.IRPDependency;
import com.telelogic.rhapsody.core.IRPDiagram;
import com.telelogic.rhapsody.core.IRPModelElement;
import com.telelogic.rhapsody.core.IRPProject;
import com.telelogic.rhapsody.core.IRPRequirement;
import com.telelogic.rhapsody.core.IRPTransition;
import com.telelogic.rhapsody.core.RhapsodyAppServer;

public class ExtractBDDInfo {

	public static void main(String[] args) {
		System.out.println("Start Req Diagram info Parser");

		// Data parsing info

		String parsedDataFilePath = "C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\ParsedDataFiles\\";

		IRPApplication app;
		app = RhapsodyAppServer.getActiveRhapsodyApplication();

		IRPProject prj;
		// String modelFile =
		// "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\ADSimulation\\ADSimulation.rpyx";
		// String modelFile =
		// "D:\\Development\\Final_Project\\TUe_Project_Dev\\DT_truck\\Standalone IBM
		// Rhapsody\\Project.rpyx";
		// String modelFile =
		// "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\NewModels\\ADSimulation\\ADSimulation.rpyx";
		String modelFile = "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\NewModels\\JuanSampleModel\\JuanSampleModel.rpyx";
		app.openProject(modelFile);
		prj = app.activeProject();

		IRPCollection packages = prj.getPackages();
		System.out.println("Package Count = " + packages.getCount());
		int packageCount = 1;
		String packageName = "";
		int createdFileCount = 0;

		for (int i0 = 1; i0 <= packages.getCount(); i0++) {
			IRPModelElement element = (IRPModelElement) packages.getItem(i0);

			if (element.getMetaClass().equals("Package")) {
				packageName = element.getName();
				String packageId = element.toString();
				boolean isPkgElementCreated = false;
				System.out.println("--------------------------------------------------------");
				System.out.println("packageName: " + packageName);
				
				String allReqElementData = ""; // package level requirements data file
				String dataFileName = packageName.concat("_BDDiagram.json");

				IRPCollection packageElements = element.getNestedElementsRecursive();
				System.out.println("Package Element Count = " + packageElements.getCount());

				for (int i1 = 1; i1 <= packageElements.getCount(); i1++) {
					IRPModelElement eachPkgElement = (IRPModelElement) packageElements.getItem(i1);
					
					/*
					 // there is no direct relationship with model diagram
					if (eachPkgElement.getUserDefinedMetaClass().equalsIgnoreCase("Requirements Diagram")) {
						System.out.println("Req Diagram Name: " + eachPkgElement.getName());
						//IRPCollection colll = eachPkgElement.getNestedElementsRecursive();
						//System.out.println("Model Element: "+colll.getCount());
						
//						IRPDiagram diag = (IRPDiagram) eachPkgElement;
//						System.out.println("Source/Dependent: "+diag.get+"<> DependentName: "+ diag.getDependent().getName());//Source
//						System.out.println("Target/DependsOn: "+diag.getDependsOn()+"<> DependsOnName: "+ diag.getDependsOn().getName());//Target

						
						//create the diagram node
						String elementIdentifier = eachPkgElement.toString();
						String elementName = eachPkgElement.getName();
						String desccription = eachPkgElement.getDescription();
						String userDefinedMetaClass = eachPkgElement.getUserDefinedMetaClass();
						String metaClass = eachPkgElement.getMetaClass();
						String ownerIdentifier = eachPkgElement.getOwner().toString();
						String ownerType = eachPkgElement.getOwner().getUserDefinedMetaClass();
						String ownerElementName = eachPkgElement.getOwner().getName();

						JSONObject reqDiagElement = new JSONObject();
						reqDiagElement.put("id", elementIdentifier);
						reqDiagElement.put("type", "node");

						JSONArray labelsArray = new JSONArray();
						labelsArray.add("Diagram");
						reqDiagElement.put("labels", labelsArray);

						JSONObject reqDiagElementProperties = new JSONObject();
						reqDiagElementProperties.put("name", elementName);
						reqDiagElementProperties.put("type", userDefinedMetaClass);
						reqDiagElementProperties.put("genericType", metaClass);
						reqDiagElementProperties.put("description", desccription);
						reqDiagElementProperties.put("owner", ownerElementName);
						reqDiagElementProperties.put("ownerType", ownerType);
						reqDiagElementProperties.put("ownerIdentifier", ownerIdentifier);

						reqDiagElement.put("properties", reqDiagElementProperties);

						allReqElementData = allReqElementData.concat(reqDiagElement.toJSONString()).concat("\n");
						System.out.println("Diagram Node >>> " + reqDiagElement.toJSONString());
					} 
					*/
					if (eachPkgElement.getUserDefinedMetaClass().equalsIgnoreCase("Requirements Diagram") && isPkgElementCreated == false) {
						//create Model node only for one times based on package name. Because requirements are not related with requirements Diagram. It's related with package
						JSONObject reqDiagElement = new JSONObject();
						reqDiagElement.put("id", packageId);
						reqDiagElement.put("type", "node");

						JSONArray labelsArray = new JSONArray();
						labelsArray.add("Diagram");
						reqDiagElement.put("labels", labelsArray);

						JSONObject reqDiagElementProperties = new JSONObject();
						reqDiagElementProperties.put("name", packageName.concat("_Requirement_Node"));
						reqDiagElementProperties.put("type", "Virtual Requirement Diagram");
						reqDiagElementProperties.put("desc", "This is a virtual diagram created for requirements.");

						reqDiagElement.put("properties", reqDiagElementProperties);

						allReqElementData = allReqElementData.concat(reqDiagElement.toJSONString()).concat("\n");
						System.out.println("Diagram Virtual Node >>> " + reqDiagElement.toJSONString());
						isPkgElementCreated = true;
					}
					
				if (eachPkgElement instanceof IRPRequirement) {
						IRPRequirement req = (IRPRequirement) eachPkgElement;
//							System.out.println("MetaClass: "+req.getMetaClass()+"<> Name: "+ req.getName() +"<> FullPath: "+ req.getFullPathName() + 
//									"<> Owner: "+ req.getOwner().getName() + " <> OwnerFullPath: "+ req.getOwner().getFullPathName() + 
//									"<>UserDefinedMetaClass: "+req.getUserDefinedMetaClass());
//							System.out.println("ReqID: "+req.getRequirementID()+"<> Spec: "+ req.getSpecification()+"<> Desc: "+ req.getDescription());

						// create requirement node
						String elementIdentifier = req.toString();
						String elementName = req.getName();
//						String desccription = req.getDescription();
						String userDefinedMetaClass = req.getUserDefinedMetaClass();
						String metaClass = req.getMetaClass();
						String ownerIdentifier = req.getOwner().toString();
						// String ownerElementName = req.getOwner().getName();

						JSONObject reqElement = new JSONObject();
						reqElement.put("id", elementIdentifier);
						reqElement.put("type", "node");

						JSONArray labelsArray = new JSONArray();
						labelsArray.add("Requirement");
						reqElement.put("labels", labelsArray);

						JSONObject reqElementProperties = new JSONObject();
						reqElementProperties.put("name", elementName);
						reqElementProperties.put("type", userDefinedMetaClass);
						reqElementProperties.put("genericType", metaClass);
						// reqElementProperties.put("description", desccription);
						reqElementProperties.put("id", req.getRequirementID());// requirementId
						reqElementProperties.put("specification", req.getSpecification());// requirementSpecification
						reqElementProperties.put("description", req.getDescription());// requirementDesc

						reqElement.put("properties", reqElementProperties);

						allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
						System.out.println("Node ~~> " + reqElement.toJSONString());
						
						// create req.node - req.diagramNode (virtual) relationship
						JSONObject reqRelation = new JSONObject();
						reqRelation.put("id", "");
						reqRelation.put("type", "relationship");
						reqRelation.put("label", "CONTAINS");

						JSONObject reqRelationProperties = new JSONObject();
						reqRelationProperties.put("type", "requirement");

						JSONObject reqRelationStart = new JSONObject();
						reqRelationStart.put("id", ownerIdentifier); // Source

						JSONObject reqRelationEnd = new JSONObject();
						reqRelationEnd.put("id", elementIdentifier); // Target

						reqRelation.put("properties", reqRelationProperties);
						reqRelation.put("start", reqRelationStart);
						reqRelation.put("end", reqRelationEnd);

						allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
						System.out.println("Relation --> " + reqRelation.toJSONString());
						
						IRPCollection reqDiagramElements = eachPkgElement.getDependencies();
						System.out.println("Requirement Element Count = " + reqDiagramElements.getCount());
						for (int i2 = 1; i2 <= reqDiagramElements.getCount(); i2++) {
							IRPModelElement coll = (IRPModelElement) reqDiagramElements.getItem(i2);
//								System.out.println("MetaClass: "+coll.getMetaClass()+"<> Name: "+ coll.getName() +"<> FullPath: "+ coll.getFullPathName() + 
//										"<> Owner: "+ coll.getOwner().getName() + " <> OwnerFullPath: "+ coll.getOwner().getFullPathName() + "<>UserDefinedMetaClass: "+coll.getUserDefinedMetaClass());
							IRPDependency elementDependency = (IRPDependency) coll;
//								System.out.println("Source/Dependent: "+elementDependency.getDependent()+"<> DependentName: "+ elementDependency.getDependent().getName());//Source
//								System.out.println("Target/DependsOn: "+elementDependency.getDependsOn()+"<> DependsOnName: "+ elementDependency.getDependsOn().getName());//Target

							// create dependency relationship
							JSONObject dependencyRel = new JSONObject();
							dependencyRel.put("id", "");
							dependencyRel.put("type", "relationship");
							dependencyRel.put("label", "IS_CONNECTED_WITH");

							JSONObject dependencyRelProperties = new JSONObject();
							dependencyRelProperties.put("type", coll.getUserDefinedMetaClass());

							JSONObject dependencyRelStart = new JSONObject();
							dependencyRelStart.put("id", elementDependency.getDependent().toString()); // Source

							JSONObject dependencyRelEnd = new JSONObject();
							dependencyRelEnd.put("id", elementDependency.getDependsOn().toString()); // Target

							dependencyRel.put("properties", dependencyRelProperties);
							dependencyRel.put("start", dependencyRelStart);
							dependencyRel.put("end", dependencyRelEnd);

							allReqElementData = allReqElementData.concat(dependencyRel.toJSONString()).concat("\n");
							System.out.println("Relation --> " + dependencyRel.toJSONString());
						}
//							System.out.println("END");
					}
				}
				if(allReqElementData!="") {
					try {
						Path filePath = Paths.get(parsedDataFilePath, dataFileName);
						FileWriter file = new FileWriter(filePath.toString());
						// file.write(jsonObject.toJSONString());
						file.write(allReqElementData);
						file.close();
						createdFileCount = createdFileCount + 1;
						System.out.println("Package JSON file created successfully");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
						System.out.println(e);
					}					
				}				
				packageCount = packageCount + 1;
			}
			System.out.println("Package Count = " + packageCount + " AND created requirements Diagram file count = " + createdFileCount);
			System.out.println("End Package info Parser");
		}
	}
}
