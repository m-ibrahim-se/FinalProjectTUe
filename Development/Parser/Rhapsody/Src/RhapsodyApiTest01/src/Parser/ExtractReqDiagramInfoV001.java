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

public class ExtractReqDiagramInfoV001 {

	public static void main(String[] args) {
			System.out.println("Start Req Diagram info Parser");

			// Data parsing info
			
			String parsedDataFilePath = "C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\ParsedDataFiles\\";

			IRPApplication app;
			app = RhapsodyAppServer.getActiveRhapsodyApplication();

			IRPProject prj;
			//String modelFile = "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\ADSimulation\\ADSimulation.rpyx";
			//String modelFile = "D:\\Development\\Final_Project\\TUe_Project_Dev\\DT_truck\\Standalone IBM Rhapsody\\Project.rpyx";
			//String modelFile = "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\NewModels\\ADSimulation\\ADSimulation.rpyx";
			String modelFile = "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\NewModels\\JuanSampleModel\\JuanSampleModel.rpyx";
			app.openProject(modelFile);
			prj = app.activeProject();
			
//			Iterator myIter1 = prj.getObjectModelDiagrams().toList().iterator();
//			while(myIter1.hasNext()) {
//				IRPDiagram myDia = (IRPDiagram) myIter1.next();
//				System.out.println(myDia.getName());
//			}
			
//			IRPCollection collection = prj.getNestedElementsRecursive();
//			System.out.println("Count: "+collection.getCount());
//			IRPCollection modelDiagramElement = prj.getNestedElementsByMetaClass("ObjectModelDiagram", 1);
//			System.out.println("Count: "+modelDiagramElement.getCount());
//			for (int i11 = 1; i11 <= modelDiagramElement.getCount(); i11++) {
//				IRPModelElement eachModelDiagramElement = (IRPModelElement)modelDiagramElement.getItem(i11);
//				
//				if(eachModelDiagramElement.getUserDefinedMetaClass().equalsIgnoreCase("Requirements Diagram")) {
//					IRPCollection reqElement = eachModelDiagramElement.getNestedElementsRecursive();
//					System.out.println("reqElement Count: "+reqElement.getCount());
//					if (reqElement instanceof IRPRequirement) {
//						IRPCollection reqDiagramElements = ((IRPModelElement) reqElement).getDependencies();	
//						System.out.println("Requirement Element Count = "+reqDiagramElements.getCount());
//						
//						for (int i3 = 1; i3 <= reqDiagramElements.getCount(); i3++) {
//							IRPModelElement coll = (IRPModelElement) reqDiagramElements.getItem(i3);
//							System.out.println("MetaClass: "+coll.getMetaClass()+"<> Name: "+ coll.getName() +"<> FullPath: "+ coll.getFullPathName() + 
//									"<> Owner: "+ coll.getOwner().getName() + "<>UserDefinedMetaClass: "+coll.getUserDefinedMetaClass());
////							IRPDependency elementDependency = (IRPDependency) coll;
//							IRPDependency elementDependency1 = (IRPDependency) coll;
////							System.out.println("Count: "+((IRPCollection) elementDependency1).getCount());
//							System.out.println("Source/Dependent: "+elementDependency1.getDependent()+"<> DependentName: "+ elementDependency1.getDependent().getName());//Source
//							System.out.println("Target/DependsOn: "+elementDependency1.getDependsOn()+"<> DependsOnName: "+ elementDependency1.getDependsOn().getName());//Target
//					}
//				}
//				}
//			}
//			
//			
////			IRPCollection iReq = prj.getNestedElementsRecursive();
////			//Step1: Retrieve all the references related to requirement iReq
////			IRPCollection iRefCollect = ((IRPModelElement) iReq).getReferences();
////
////			//Step2: Run a loop to get each referenced elements in turn
////			for(int i=1 ; i<=iRefCollect.getCount() ; i++) {
////				//Step3: Check if the current referenced element is the instance of dependency
////				if (iRefCollect.getItem(i)  instanceof IRPDependency) {
////					// Step4: Perform type cast and continue the further user operation
////					IRPDependency iDep = (IRPDependency)(iRefCollect.getItem(i));
////					System.out.println("MetaClass: "+iDep.getMetaClass()+"<> Name: "+iDep.getName() + "<>UserDefinedMetaClass: "+iDep.getUserDefinedMetaClass());
////				}
////			}
			
			IRPCollection packages = prj.getPackages();
			System.out.println("Package Count = "+packages.getCount());
			//System.out.println("Name||MetaClass||Owner/Parent||Identifier");
			int packageCount = 1;
			String packageName = "";
			int createdFileCount = 0;
			
			String allReqElementData = "";
			
			for (int i0 = 1; i0 <= packages.getCount(); i0++) {
				IRPModelElement element = (IRPModelElement)packages.getItem(i0);
//				System.out.println("MetaClass: "+element.getMetaClass()+"<><>>><><><>"+element.getName());				
				
				if(element.getMetaClass().equals("Package")) {
					packageName = element.getName();
					
					IRPCollection packageElements = element.getNestedElementsRecursive();	
					System.out.println("Package Element Count = "+packageElements.getCount());					
					
					//String allBlockData = "";
					
					for (int i1 = 1; i1 <= packageElements.getCount(); i1++) {
						IRPModelElement eachPkgElement = (IRPModelElement)packageElements.getItem(i1);
						
						if(eachPkgElement.getUserDefinedMetaClass().equalsIgnoreCase("Requirements Diagram")) {
							System.out.println("Req Diagram Name: "+eachPkgElement.getName());
							
							//create the diagram node	
							String elementIdentifier = eachPkgElement.toString();
							String elementName = eachPkgElement.getName();
							String desccription = eachPkgElement.getDescription();	
							String userDefinedMetaClass = eachPkgElement.getUserDefinedMetaClass();
							String metaClass = eachPkgElement.getMetaClass();
							String ownerIdentifier = eachPkgElement.getOwner().toString();
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
							reqDiagElementProperties.put("ownerIdentifier", ownerIdentifier);
							
							reqDiagElement.put("properties", reqDiagElementProperties);
							
							allReqElementData = allReqElementData.concat(reqDiagElement.toJSONString()).concat("\n");
							System.out.println("Diagram Node >>> "+reqDiagElement.toJSONString());
						}
						else if (eachPkgElement instanceof IRPRequirement) {
							IRPRequirement req = (IRPRequirement) eachPkgElement;
//							System.out.println("MetaClass: "+req.getMetaClass()+"<> Name: "+ req.getName() +"<> FullPath: "+ req.getFullPathName() + 
//									"<> Owner: "+ req.getOwner().getName() + " <> OwnerFullPath: "+ req.getOwner().getFullPathName() + 
//									"<>UserDefinedMetaClass: "+req.getUserDefinedMetaClass());
//							System.out.println("ReqID: "+req.getRequirementID()+"<> Spec: "+ req.getSpecification()+"<> Desc: "+ req.getDescription());
							
							//create requirement node							
							String elementIdentifier = req.toString();
							String elementName = req.getName();
							String desccription = req.getDescription();	
							String userDefinedMetaClass = req.getUserDefinedMetaClass();
							String metaClass = req.getMetaClass();
							String ownerIdentifier = req.getOwner().toString();
							//String ownerElementName = req.getOwner().getName();							

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
							//reqElementProperties.put("description", desccription);
							reqElementProperties.put("id", req.getRequirementID());//requirementId
							reqElementProperties.put("specification", req.getSpecification());//requirementSpecification
							reqElementProperties.put("description", req.getDescription());//requirementDesc
							
							reqElement.put("properties", reqElementProperties);
							
							allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
							System.out.println("Node ~~> "+reqElement.toJSONString());
							
							//create req.node - req.diagramNode relationship
							JSONObject reqRelation = new JSONObject();
							reqRelation.put("id", "");							
							reqRelation.put("type", "relationship");	
							reqRelation.put("label", "CONTAINS");		
							
							JSONObject reqRelationProperties = new JSONObject();	
							reqRelationProperties.put("type", "requirement");		
							
							JSONObject reqRelationStart= new JSONObject();	
							reqRelationStart.put("id", ownerIdentifier);	//Source	
							
							JSONObject reqRelationEnd = new JSONObject();	
							reqRelationEnd.put("id", elementIdentifier);	//Target
							
							reqRelation.put("properties", reqRelationProperties);
							reqRelation.put("start", reqRelationStart);
							reqRelation.put("end", reqRelationEnd);
							
							allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
							System.out.println("Relation --> "+reqRelation.toJSONString());
							
							IRPCollection reqDiagramElements = eachPkgElement.getDependencies();	
							System.out.println("Requirement Element Count = "+reqDiagramElements.getCount());
							for (int i2 = 1; i2 <= reqDiagramElements.getCount(); i2++) {
								IRPModelElement coll = (IRPModelElement) reqDiagramElements.getItem(i2);
//								System.out.println("MetaClass: "+coll.getMetaClass()+"<> Name: "+ coll.getName() +"<> FullPath: "+ coll.getFullPathName() + 
//										"<> Owner: "+ coll.getOwner().getName() + " <> OwnerFullPath: "+ coll.getOwner().getFullPathName() + "<>UserDefinedMetaClass: "+coll.getUserDefinedMetaClass());
								IRPDependency elementDependency = (IRPDependency) coll;
//								System.out.println("Source/Dependent: "+elementDependency.getDependent()+"<> DependentName: "+ elementDependency.getDependent().getName());//Source
//								System.out.println("Target/DependsOn: "+elementDependency.getDependsOn()+"<> DependsOnName: "+ elementDependency.getDependsOn().getName());//Target
								
								//create dependency relationship
								JSONObject dependencyRel = new JSONObject();
								dependencyRel.put("id", "");							
								dependencyRel.put("type", "relationship");	
								dependencyRel.put("label", "IS_CONNECTED_WITH");		
								
								JSONObject dependencyRelProperties = new JSONObject();	
								dependencyRelProperties.put("type", coll.getUserDefinedMetaClass());		
								
								JSONObject dependencyRelStart = new JSONObject();	
								dependencyRelStart.put("id", elementDependency.getDependent().toString());	//Source	
								
								JSONObject dependencyRelEnd = new JSONObject();	
								dependencyRelEnd.put("id", elementDependency.getDependsOn().toString());	//Target
								
								dependencyRel.put("properties", dependencyRelProperties);
								dependencyRel.put("start", dependencyRelStart);
								dependencyRel.put("end", dependencyRelEnd);
								
								allReqElementData = allReqElementData.concat(dependencyRel.toJSONString()).concat("\n");
								System.out.println("Relation --> "+dependencyRel.toJSONString());
							}
												
						
							System.out.println("END");
							
//							JSONObject jsonObjReqElement = new JSONObject();						
//							
//							String ownerElementName = eachPkgElement.getOwner().getName();	
//							String metaClass = eachPkgElement.getMetaClass();
//							String elementName = eachPkgElement.getName();
//							String elementIdentifier = eachPkgElement.toString();
//							String userDefinedMetaClass = eachPkgElement.getUserDefinedMetaClass();
//							jsonObjElement.put("Name", elementName);
//							jsonObjElement.put("MetaClass", metaClass);
//							jsonObjElement.put("OwnerOrParent", ownerElementName);
//							jsonObjElement.put("OwnerOrParentIdentifier", eachPkgElement.getOwner().toString());
//							jsonObjElement.put("Identifier", elementIdentifier);
//							jsonObjElement.put("UserDefinedMetaClass", userDefinedMetaClass);
//							allPkgElementData = allPkgElementData.concat(jsonObjElement.toJSONString()).concat("\n");
//							System.out.println(jsonObjElement.toJSONString());
						}
						
						
						//Block Data
//						if(metaClass.equals("Class")) {
//							JSONObject jsonObjBlockElement = new JSONObject();
//							jsonObjBlockElement.put("Name", elementName);
//							jsonObjBlockElement.put("Identifier", elementIdentifier);
//						}else if(eachPkgElement.getInterfaceName().equals("IRPRelation")) {
//							JSONObject jsonObjBlockPath = new JSONObject();
//							jsonObjBlockPath.put("Source", elementName);
//							jsonObjBlockPath.put("SourceIdentifier", elementName);
//							jsonObjBlockPath.put("Target", elementIdentifier);
//							jsonObjBlockPath.put("TargetIdentifier", elementIdentifier);
//							jsonObjBlockPath.put("PathType", eachPkgElement.getMetaClass());
//							jsonObjBlockPath.put("PathTypeIdentifier", elementIdentifier);
//						}
						
						//System.out.println(eachPkgElement.getName() + "||" + eachPkgElement.getMetaClass() +"||"+ ownerElementName +"||"+eachPkgElement.toString());
					}	
					try {
						Path filePath = Paths.get(parsedDataFilePath, packageName + ".json");
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
			System.out.println("Package Count = "+ packageCount + " AND created file count = "+createdFileCount);
			System.out.println("End Package info Parser");
	}

}
