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
import com.telelogic.rhapsody.core.IRPObjectModelDiagram;
import com.telelogic.rhapsody.core.IRPProject;
import com.telelogic.rhapsody.core.IRPRequirement;
import com.telelogic.rhapsody.core.IRPTransition;
import com.telelogic.rhapsody.core.RhapsodyAppServer;

public class ExtractReqDiagramInfo {

	public static void main(String[] args) {
		System.out.println("Start Req Diagram info Parser");

		// Data parsing info
		String parsedDataFilePath = "C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\ParsedDataFiles\\";

		IRPApplication application;
		application = RhapsodyAppServer.getActiveRhapsodyApplication();

		IRPProject project;
		// String modelFile = "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\NewModels\\ADSimulation\\ADSimulation.rpyx";
		
		String modelFile = "C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\Models\\JuanSampleModel\\JuanSampleModel.rpyx";
		//String modelFile = "C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\Models\\JUAN_Original\\Juan_V1.rpyx";
		application.openProject(modelFile);
		project = application.activeProject();

		IRPCollection packageList = project.getPackages();
		System.out.println("Package Count = " + packageList.getCount());

		int packageCount = 0;
		int reqModelCount = 0;

		int createdFileCount = 0;

		for (int i = 1; i <= packageList.getCount(); i++) {
			IRPModelElement element = (IRPModelElement) packageList.getItem(i);

			if (element.getMetaClass().equals("Package")) {
				String packageName = element.getName();
				String packageId = element.toString();
				System.out.println("--------------------------------------------------------");
				System.out.println("packageName: " + packageName);

				IRPCollection packageElements = element.getNestedElementsRecursive();
				System.out.println("Package Element Count = " + packageElements.getCount());

				for (int j = 1; j <= packageElements.getCount(); j++) {
					IRPModelElement eachPkgElement = (IRPModelElement) packageElements.getItem(j);

					// Get Requirements Model Diagram
					//eachPkgElement.getMetaClass().equalsIgnoreCase("ObjectModelDiagram")
					if (eachPkgElement.getUserDefinedMetaClass().equalsIgnoreCase("Requirements Diagram")) {
						IRPObjectModelDiagram mod = (IRPObjectModelDiagram) eachPkgElement;
						String modelDiagName = mod.getName(); // Requirements Diagram
						String modelDiagIdentifier = mod.toString();
						System.out.println("Req Diagram/Model Name: " + modelDiagName);

						// Each Model Element
						String allReqElementData = ""; // model level requirements data file
						String dataFileName = packageName.concat("_" + modelDiagName).concat("_ReqDiagram.json"); // packageName_ModelName_

						// create the diagram node
						String elementIdentifier = mod.toString();
						String elementName = mod.getName();
						String desccription = mod.getDescription();
						String userDefinedMetaClass = mod.getUserDefinedMetaClass();
						String metaClass = mod.getMetaClass();
						String ownerIdentifier = mod.getOwner().toString();
						String ownerType = mod.getOwner().getUserDefinedMetaClass();
						String ownerElementName = mod.getOwner().getName();

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
						reqDiagElementProperties.put("ownerName", ownerElementName);
						reqDiagElementProperties.put("ownerType", ownerType);
						reqDiagElementProperties.put("ownerIdentifier", ownerIdentifier);

						reqDiagElement.put("properties", reqDiagElementProperties);

						allReqElementData = allReqElementData.concat(reqDiagElement.toJSONString()).concat("\n");
						System.out.println("Diagram Node >>> " + reqDiagElement.toJSONString());

						// Model Elements
						IRPCollection collection = mod.getElementsInDiagram();
						for (int k = 1; k <= collection.getCount(); k++) {
							IRPModelElement eachModelElement = (IRPModelElement) collection.getItem(k);
//							
							//System.out.println("ReqID: "+eachModelElement.getRequirementID()+"<> Spec: "+ eachModelElement.getSpecification()+"<> Desc: "+ eachModelElement.getDescription());

							// Or eachModelElement instanceof IRPRequirement
							if (eachModelElement.getMetaClass().equals("Requirement")) {
								IRPRequirement req = (IRPRequirement) eachModelElement;

								// create requirement node
								String reqElementIdentifier = req.toString();
								String reqElementName = req.getName();
//								String desccription = req.getDescription();
								String reqElementUserDefinedMetaClass = req.getUserDefinedMetaClass();
								String reqElementMetaClass = req.getMetaClass();
								// String ownerIdentifier = req.getOwner().toString();
								String reqElementOwnerIdentifier = modelDiagIdentifier;
//								String ownerElementName = req.getOwner().getName();

								JSONObject reqElement = new JSONObject();
								reqElement.put("id", reqElementIdentifier);
								reqElement.put("type", "node");

								JSONArray reqElementlabelsArray = new JSONArray();
								reqElementlabelsArray.add("Requirement");
								reqElement.put("labels", reqElementlabelsArray);

								JSONObject reqElementProperties = new JSONObject();
								reqElementProperties.put("name", reqElementName);
								reqElementProperties.put("type", reqElementUserDefinedMetaClass);
								reqElementProperties.put("genericType", reqElementMetaClass);
								// reqElementProperties.put("description", desccription);
								reqElementProperties.put("requirementId", req.getRequirementID());// requirementId
								reqElementProperties.put("specification", req.getSpecification());// requirementSpecification
								reqElementProperties.put("description", req.getDescription());// requirementDesc

								reqElement.put("properties", reqElementProperties);

								allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
								System.out.println("Node ~~> " + reqElement.toJSONString());

								// create req.node - req.diagramNode (model) relationship
								JSONObject reqRelation = new JSONObject();
								reqRelation.put("id", "");
								reqRelation.put("type", "relationship");
								reqRelation.put("label", "CONTAINS");

								JSONObject reqRelationProperties = new JSONObject();
								reqRelationProperties.put("type", "requirement");

								JSONObject reqRelationStart = new JSONObject();
								reqRelationStart.put("id", modelDiagIdentifier); // Source, Model identifier (virtually
																					// created relation, actually it is
																					// connected with package)

								JSONObject reqRelationEnd = new JSONObject();
								reqRelationEnd.put("id", reqElementIdentifier); // Target

								reqRelation.put("properties", reqRelationProperties);
								reqRelation.put("start", reqRelationStart);
								reqRelation.put("end", reqRelationEnd);

								allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
								System.out.println("Relation --> " + reqRelation.toJSONString());

								IRPCollection reqDiagramElements = eachPkgElement.getDependencies();
								System.out.println("Requirement Element Count = " + reqDiagramElements.getCount());
							}

							if (eachModelElement.getMetaClass().equals("Dependency")) {
								IRPDependency elementDependency = (IRPDependency) eachModelElement;

								// check where dependency parent identifier is a requirement or not
								if (elementDependency.getDependsOn().getMetaClass().equals("Requirement")) {
									// create dependency relationship
									JSONObject dependencyRel = new JSONObject();
									dependencyRel.put("id", "");
									dependencyRel.put("type", "relationship");
									dependencyRel.put("label", "IS_CONNECTED_WITH");

									JSONObject dependencyRelProperties = new JSONObject();
									dependencyRelProperties.put("type", elementDependency.getUserDefinedMetaClass());

									JSONObject dependencyRelStart = new JSONObject();
									dependencyRelStart.put("id", elementDependency.getDependent().toString()); // Source

									JSONObject dependencyRelEnd = new JSONObject();
									dependencyRelEnd.put("id", elementDependency.getDependsOn().toString()); // Target

									dependencyRel.put("properties", dependencyRelProperties);
									dependencyRel.put("start", dependencyRelStart);
									dependencyRel.put("end", dependencyRelEnd);

									allReqElementData = allReqElementData.concat(dependencyRel.toJSONString())
											.concat("\n");
									System.out.println("Relation --> " + dependencyRel.toJSONString());
								} else {
									// Do nothing. Because this element may be comment or constraint block and is
									// not in our scope
								}
							}

						}
						reqModelCount = reqModelCount + 1;

						if (allReqElementData != "") {
							try {
								Path filePath = Paths.get(parsedDataFilePath, dataFileName);
								FileWriter file = new FileWriter(filePath.toString());
								// file.write(jsonObject.toJSONString());
								file.write(allReqElementData);
								file.close();
								createdFileCount = createdFileCount + 1;
								System.out.println("Req model JSON file created successfully");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								// e.printStackTrace();
								System.out.println(e);
							}
						}
					}
				}

				packageCount = packageCount + 1;
			}
		}
		System.out.println("Package Count = " + packageCount + ", Model Count = " + reqModelCount
				+ " AND created requirements Diagram file count = " + createdFileCount);
		System.out.println("End Package info Parser");
	}
}
