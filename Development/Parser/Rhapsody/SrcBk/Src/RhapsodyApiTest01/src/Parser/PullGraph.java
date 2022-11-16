package Parser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telelogic.rhapsody.core.IRPAction;
import com.telelogic.rhapsody.core.IRPActivityDiagram;
import com.telelogic.rhapsody.core.IRPApplication;
import com.telelogic.rhapsody.core.IRPClass;
import com.telelogic.rhapsody.core.IRPClassifier;
import com.telelogic.rhapsody.core.IRPClassifierRole;
import com.telelogic.rhapsody.core.IRPCollection;
import com.telelogic.rhapsody.core.IRPConnector;
import com.telelogic.rhapsody.core.IRPDependency;
import com.telelogic.rhapsody.core.IRPEvent;
import com.telelogic.rhapsody.core.IRPFlow;
import com.telelogic.rhapsody.core.IRPFlowchart;
import com.telelogic.rhapsody.core.IRPLink;
import com.telelogic.rhapsody.core.IRPMessage;
import com.telelogic.rhapsody.core.IRPModelElement;
import com.telelogic.rhapsody.core.IRPObjectModelDiagram;
import com.telelogic.rhapsody.core.IRPPackage;
import com.telelogic.rhapsody.core.IRPPin;
import com.telelogic.rhapsody.core.IRPPort;
import com.telelogic.rhapsody.core.IRPProject;
import com.telelogic.rhapsody.core.IRPRelation;
import com.telelogic.rhapsody.core.IRPRequirement;
import com.telelogic.rhapsody.core.IRPSequenceDiagram;
import com.telelogic.rhapsody.core.IRPState;
import com.telelogic.rhapsody.core.IRPStatechart;
import com.telelogic.rhapsody.core.IRPStereotype;
import com.telelogic.rhapsody.core.IRPSysMLPort;
import com.telelogic.rhapsody.core.IRPTag;
import com.telelogic.rhapsody.core.IRPTransition;
import com.telelogic.rhapsody.core.IRPUseCase;
import com.telelogic.rhapsody.core.IRPUseCaseDiagram;
import com.telelogic.rhapsody.core.RPAttribute;
import com.telelogic.rhapsody.core.RhapsodyAppServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PullGraph {
	IRPApplication app;
	IRPProject prj;
	String parsedDataFilePath = "C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\ParsedDataFiles\\";

	public PullGraph() {
		app = RhapsodyAppServer.getActiveRhapsodyApplication();
		// app.openProject("..\\JUAN\\Juan_V1.rpy");
		app.openProject(
				//"C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\Models\\DT_Truck\\Standalone_IBM_Rhapsody\\Project.rpyx");
				"C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\Models\\JSampleModel\\JSampleModel.rpyx");
		prj = app.activeProject();
	}

	public ArrayList<IRPPackage> getPackages() {
		IRPCollection packages = prj.getPackages();
		int amountOfPackages = packages.getCount();
		ArrayList<IRPPackage> packagesList = new ArrayList<IRPPackage>();
		for (int i = 1; i <= amountOfPackages; i++) {
			// Finding all packages and putting them in ArrayList packagesList
			packagesList.add((IRPPackage) packages.getItem(i));
		}
		return packagesList;
	}

	// Get all model object info in project
	public ArrayList<IRPObjectModelDiagram> getModelDiagrams() {
		IRPCollection modelElement = prj.getNestedElementsRecursive();

		ArrayList<IRPObjectModelDiagram> modelDiagramList = new ArrayList<IRPObjectModelDiagram>();

		for (int m = 1; m <= modelElement.getCount(); m++) {
			IRPModelElement element = (IRPModelElement) modelElement.getItem(m);
			if (element.getUserDefinedMetaClass().equalsIgnoreCase("Block Definition Diagram")) {
				modelDiagramList.add((IRPObjectModelDiagram) element);
				System.out.println("Added Diagram: " + element.getName());
			}

		}
		// IRPCollection modelDiagrams = prj.getObjectModelDiagrams();
		// IRPCollection packages = prj.getPackages();
		// int amountOfModelDiagrams = modelDiagrams.getCount();
		// System.out.println("ModelDiagrams Count:"+amountOfModelDiagrams);
		// ArrayList<IRPObjectModelDiagram> modelDiagramList = new
		// ArrayList<IRPObjectModelDiagram>();
//		for (int i = 1; i <= amountOfModelDiagrams; i++) {
//			// Finding all packages and putting them in ArrayList packagesList
//			modelDiagramList.add((IRPObjectModelDiagram) modelDiagrams.getItem(i));
//			System.out.println("Added Diagram: "+modelDiagrams.getItem(i).getClass().getName());
//		}
		return modelDiagramList;
	}
	
	public ArrayList<IRPStatechart> getStateChartDiagrams() {
		IRPCollection modelElement = prj.getNestedElementsRecursive();

		ArrayList<IRPStatechart> modelDiagramList = new ArrayList<IRPStatechart>();

		for (int m = 1; m <= modelElement.getCount(); m++) {
			IRPModelElement element = (IRPModelElement) modelElement.getItem(m);
			if (element.getUserDefinedMetaClass().equalsIgnoreCase("Statechart")) {
				modelDiagramList.add((IRPStatechart) element);
				System.out.println("Added STM Diagram: " + element.getName());
			}

		}
		return modelDiagramList;
	}
	
	public ArrayList<IRPFlowchart> getActivityDiagrams() {
		IRPCollection modelElement = prj.getNestedElementsRecursive();

		ArrayList<IRPFlowchart> modelDiagramList = new ArrayList<IRPFlowchart>();

		for (int m = 1; m <= modelElement.getCount(); m++) {
			IRPModelElement element = (IRPModelElement) modelElement.getItem(m);
			if (element.getUserDefinedMetaClass().equalsIgnoreCase("ActivityDiagram")) {
				modelDiagramList.add((IRPFlowchart) element);
				System.out.println("Added STM Diagram: " + element.getName());
			}

		}
		// IRPCollection modelDiagrams = prj.getObjectModelDiagrams();
		// IRPCollection packages = prj.getPackages();
		// int amountOfModelDiagrams = modelDiagrams.getCount();
		// System.out.println("ModelDiagrams Count:"+amountOfModelDiagrams);
		// ArrayList<IRPObjectModelDiagram> modelDiagramList = new
		// ArrayList<IRPObjectModelDiagram>();
//		for (int i = 1; i <= amountOfModelDiagrams; i++) {
//			// Finding all packages and putting them in ArrayList packagesList
//			modelDiagramList.add((IRPObjectModelDiagram) modelDiagrams.getItem(i));
//			System.out.println("Added Diagram: "+modelDiagrams.getItem(i).getClass().getName());
//		}
		return modelDiagramList;
	}

	/*
	 * This function retrieves an ArrayList of all Use Cases in the diagram
	 */
	public ArrayList<IRPUseCase> getUseCases() {

		ArrayList<IRPPackage> packagesList = getPackages();

		ArrayList<IRPUseCase> useCasesList = new ArrayList<IRPUseCase>();
		for (int i = 0; i < packagesList.size(); i++) {
			// For each package find all occurrences of a use case
			IRPCollection useCases = packagesList.get(i).getUseCases();
			int amountOfUseCases = useCases.getCount();

			for (int j = 1; j <= amountOfUseCases; j++) {
				// Add use case to useCasesList
				useCasesList.add((IRPUseCase) useCases.getItem(j));
			}
		}

		return useCasesList;
	}

	/*
	 * get's the use case diagrams in the model
	 */
	public ArrayList<IRPUseCaseDiagram> getUseCaseDiagramsBk(ArrayList<IRPPackage> packages) {

		ArrayList<IRPUseCaseDiagram> useCaseDiagramList = new ArrayList<IRPUseCaseDiagram>();
		for (IRPPackage pack : packages) {
			IRPCollection useCaseDiagrams = pack.getUseCaseDiagrams();
			int amountOfUseCases = useCaseDiagrams.getCount();

			for (int j = 1; j <= amountOfUseCases; j++) {
				// Add use case to useCasesList
				useCaseDiagramList.add((IRPUseCaseDiagram) useCaseDiagrams.getItem(j));
			}
		}
		// For each package find all occurrences of a use case diagram
		IRPCollection useCaseDiagrams = prj.getUseCaseDiagrams();
		int amountOfUseCases = useCaseDiagrams.getCount();

		for (int j = 1; j <= amountOfUseCases; j++) {
			// Add use case to useCasesList
			useCaseDiagramList.add((IRPUseCaseDiagram) useCaseDiagrams.getItem(j));
		}
		return useCaseDiagramList;
	}

	public void getUseCaseDiagrams(ArrayList<IRPPackage> packages) {

		ArrayList<IRPUseCaseDiagram> useCaseDiagramList = new ArrayList<IRPUseCaseDiagram>();
		for (IRPPackage pack : packages) {
			IRPCollection useCaseDiagrams = pack.getUseCaseDiagrams();
			int amountOfUseCases = useCaseDiagrams.getCount();

			for (int j = 1; j <= amountOfUseCases; j++) {
				// Add use case to useCasesList
				useCaseDiagramList.add((IRPUseCaseDiagram) useCaseDiagrams.getItem(j));
			}
		}
		// For each package find all occurrences of a use case diagram
		IRPCollection useCaseDiagrams = prj.getUseCaseDiagrams();
		int amountOfUseCases = useCaseDiagrams.getCount();

		for (int j = 1; j <= amountOfUseCases; j++) {
			// Add use case to useCasesList
			useCaseDiagramList.add((IRPUseCaseDiagram) useCaseDiagrams.getItem(j));
		}

		for (IRPUseCaseDiagram useCaseDiag : useCaseDiagramList) {
			System.out.println();
//			System.out.println("UseCase Diagram ---> MetaClass: "+useCaseDiag.getMetaClass()+"<> Name: "+ useCaseDiag.getName() +"<> FullPath: "+ useCaseDiag.getFullPathName() + 
//					"<> Owner: "+ useCaseDiag.getOwner().getName() + " <> OwnerFullPath: "+ useCaseDiag.getOwner().getFullPathName() + 
//					"<>OwnerIdentifier: "+useCaseDiag.getOwner().toString()+" <>UserDefinedMetaClass: "+useCaseDiag.getUserDefinedMetaClass()+
//					"<>Identifier: "+useCaseDiag.toString());
			IRPUseCaseDiagram mod = (IRPUseCaseDiagram) useCaseDiag;
			String modelDiagName = mod.getName(); // Requirements Diagram
			String modelDiagIdentifier = mod.toString();
			System.out.println("Use Case Diagram/Model Name: " + modelDiagName);

			String packageName = "";
			if (!mod.getOwner().getName().isEmpty()) {
				packageName = mod.getOwner().getName();
			}

			// Each Model Element
			String allReqElementData = ""; // model level requirements data file
			String dataFileName = packageName.concat("_" + modelDiagName).concat("_UcDiagram.json").replace(" ", "-"); // packageName_ModelName_

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
			labelsArray.add("UseCaseDiagram");
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
			
			// create reqModel.node - reqModel.Parent (model) relationship
			JSONObject modelRelation = new JSONObject();
			modelRelation.put("id", "");
			modelRelation.put("type", "relationship");
			modelRelation.put("label", "CONTAINS_DIAGRAM");

			JSONObject modelRelationProperties = new JSONObject();
			modelRelationProperties.put("type", userDefinedMetaClass); //mod.getOwner().getUserDefinedMetaClass()

			JSONObject modelRelationStart = new JSONObject();
			modelRelationStart.put("id", ownerIdentifier); // Source, created a relationship with package or project

			JSONObject modelRelationEnd = new JSONObject();
			modelRelationEnd.put("id", elementIdentifier); // Target

			modelRelation.put("properties", modelRelationProperties);
			modelRelation.put("start", modelRelationStart);
			modelRelation.put("end", modelRelationEnd);

			allReqElementData = allReqElementData.concat(modelRelation.toJSONString()).concat("\n");
			System.out.println("Relation --> " + modelRelation.toJSONString());

			// IRPCollection elements = useCaseDiag.getElementsInDiagram();

//			for(int i = 1; i <= elements.getCount(); i++) {
//				IRPModelElement eachModelElement = (IRPModelElement)elements.getItem(i);
//				
//				System.out.println("Model Element ---> MetaClass: "+eachModelElement.getMetaClass()+"<> Name: "+ eachModelElement.getName() +"<> FullPath: "+ eachModelElement.getFullPathName() + 
//						"<> Owner: "+ eachModelElement.getOwner().getName() + " <> OwnerFullPath: "+ eachModelElement.getOwner().getFullPathName() + 
//						"<>OwnerIdentifier: "+eachModelElement.getOwner().toString()+" <>UserDefinedMetaClass: "+eachModelElement.getUserDefinedMetaClass()+
//						"<>Identifier: "+eachModelElement.toString());
//			}
			// Model Elements
			IRPCollection collection = mod.getElementsInDiagram();
			for (int k = 1; k <= collection.getCount(); k++) {
				IRPModelElement eachModelElement = (IRPModelElement) collection.getItem(k);
//					System.out.println("Model Element ---> MetaClass: "+eachModelElement.getMetaClass()+"<> Name: "+ eachModelElement.getName() +"<> FullPath: "+ eachModelElement.getFullPathName() + 
//							"<> Owner: "+ eachModelElement.getOwner().getName() + " <> OwnerFullPath: "+ eachModelElement.getOwner().getFullPathName() + 
//							"<>UserDefinedMetaClass: "+eachModelElement.getUserDefinedMetaClass());
				// System.out.println("ReqID: "+eachModelElement.getRequirementID()+"<> Spec: "+
				// eachModelElement.getSpecification()+"<> Desc: "+
				// eachModelElement.getDescription());

				// Or eachModelElement instanceof IRPRequirement
				if (eachModelElement.getMetaClass().equals("Actor")
						|| eachModelElement.getMetaClass().equals("UseCase")) {
					// IRPClass req = (IRPClass) eachModelElement;

					// create requirement node
					String reqElementIdentifier = eachModelElement.toString();
					String reqElementName = eachModelElement.getName();
//						String desccription = req.getDescription();
					String reqElementUserDefinedMetaClass = eachModelElement.getUserDefinedMetaClass();
					String reqElementMetaClass = eachModelElement.getMetaClass();
					// String ownerIdentifier = req.getOwner().toString();
					String reqElementOwnerIdentifier = modelDiagIdentifier;
//						String ownerElementName = req.getOwner().getName();

					JSONObject reqElement = new JSONObject();
					reqElement.put("id", reqElementIdentifier);
					reqElement.put("type", "node");

					JSONArray reqElementlabelsArray = new JSONArray();
					reqElementlabelsArray.add(reqElementMetaClass); // Actor, Use Case
					reqElement.put("labels", reqElementlabelsArray);

					JSONObject reqElementProperties = new JSONObject();
					reqElementProperties.put("name", reqElementName);
					reqElementProperties.put("type", reqElementUserDefinedMetaClass);
					reqElementProperties.put("genericType", reqElementMetaClass);

					reqElement.put("properties", reqElementProperties);

					allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
					System.out.println(reqElementlabelsArray + " Node ~~> " + reqElement.toJSONString());

					// create req.node - req.diagramNode (model) relationship
					JSONObject reqRelation = new JSONObject();
					reqRelation.put("id", "");
					reqRelation.put("type", "relationship");
					reqRelation.put("label", "CONTAINS_"+reqElementMetaClass.toUpperCase()); //CONTAINS_USECASE, CONTAINS_ACTOR

					JSONObject reqRelationProperties = new JSONObject();
					reqRelationProperties.put("type", reqElementMetaClass);// Actor, Use Case

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
				} else if (eachModelElement.getMetaClass().equalsIgnoreCase("AssociationEnd")) {
					IRPRelation relation = (IRPRelation) eachModelElement;
					// create dependency relationship
					JSONObject dependencyRel = new JSONObject();
					dependencyRel.put("id", "");
					dependencyRel.put("type", "relationship");
					// dependencyRel.put("label", "IS_PART_OF");
					String labelName = "HAS_" + relation.getRelationType().toUpperCase() + "_WITH";
					dependencyRel.put("label", labelName);

					JSONObject dependencyRelProperties = new JSONObject();
					dependencyRelProperties.put("type", relation.getRelationType());
					dependencyRelProperties.put("name", relation.getName());

					JSONObject dependencyRelStart = new JSONObject();
					dependencyRelStart.put("id", relation.getOfClass().toString()); // Source

					JSONObject dependencyRelEnd = new JSONObject();
					dependencyRelEnd.put("id", relation.getOtherClass().toString()); // Target

					dependencyRel.put("properties", dependencyRelProperties);
					dependencyRel.put("start", dependencyRelStart);
					dependencyRel.put("end", dependencyRelEnd);

					allReqElementData = allReqElementData.concat(dependencyRel.toJSONString()).concat("\n");
					System.out.println("Relation --> " + dependencyRel.toJSONString());
				} else if (eachModelElement.getMetaClass().equals("Dependency")) {
					IRPDependency elementDependency = (IRPDependency) eachModelElement;
					
//					System.out.println("Dependency Info-----> : Name: " + elementDependency.getName() + " Type:" + elementDependency.getMetaClass());
					// check where dependency parent identifier is a requirement or not
//					if (elementDependency.getDependsOn().getMetaClass().equals("Requirement")) {
					// create dependency relationship
					JSONObject dependencyRel = new JSONObject();
					dependencyRel.put("id", "");
					dependencyRel.put("type", "relationship");
					dependencyRel.put("label", "IS_CONNECTED_WITH_USECASE"); //IS_CONNECTED_WITH

					JSONObject dependencyRelProperties = new JSONObject();
					dependencyRelProperties.put("type", elementDependency.getUserDefinedMetaClass());

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

			}
		if (allReqElementData != "") {
			try {
				Path filePath = Paths.get(parsedDataFilePath, dataFileName);
				FileWriter file = new FileWriter(filePath.toString());
				// file.write(jsonObject.toJSONString());
				file.write(allReqElementData);
				file.close();
				// createdFileCount = createdFileCount + 1;
				System.out.println("Requirements model JSON file created successfully!!!");
				System.out.println("File Path: " + filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println(e);
			}
			}
		}

	// return useCaseDiagramList;
	}

	/*
	 * This function gets all classes available in the current model
	 */
	public ArrayList<IRPClass> getClasses() {
		ArrayList<IRPPackage> packagesList = getPackages();

		ArrayList<IRPClass> classList = new ArrayList<IRPClass>();
		for (int i = 0; i < packagesList.size(); i++) {
			// For each package find all occurrences of a use case
			IRPCollection classes = packagesList.get(i).getClasses();
			int amountOfClasses = classes.getCount();

			for (int j = 1; j <= amountOfClasses; j++) {
				// Add use case to useCasesList
				classList.add((IRPClass) classes.getItem(j));
			}
		}
		return classList;
	}

	/*
	 * This function gets the stateCharts from a block
	 */
	public IRPStatechart getStatechartsBk(IRPClassifier block) {
		IRPStatechart statechart = block.getStatechart();
		return statechart;
	}
	
	public void getStatecharts(IRPClassifier block) {
		IRPStatechart statechart = block.getStatechart();
		
		if(statechart != null) {
			IRPModelElement eachModelElement = (IRPModelElement) statechart;
			
			System.out.println(eachModelElement.getName() + ":");
			System.out.println("Stm Diagram ---> MetaClass: "+eachModelElement.getMetaClass()+"<> Name: "+ eachModelElement.getName() +"<> FullPath: "+ eachModelElement.getFullPathName() + 
					"<> Owner: "+ eachModelElement.getOwner().getName() + " <> OwnerFullPath: "+ eachModelElement.getOwner().getFullPathName() + 
					"<>OwnerIdentifier: "+eachModelElement.getOwner().toString()+" <>UserDefinedMetaClass: "+eachModelElement.getUserDefinedMetaClass()+
					"<>Identifier: "+eachModelElement.toString());
			
			printElementsStatechart(statechart);
		}
		//return statechart;
	}
	
	public void printStateChart(IRPModelElement eachPkgElement) throws IOException {

		// String modelDigramName = eachPkgElement.getName();

//		FileWriter requirementsCsvWriter = new FileWriter("requirements.csv");  
//		FileWriter reqDependencyCsvWriter = new FileWriter("requirementsDependency.csv");
//		IRPCollection allNested = element.getNestedElementsRecursive();
//		int count = allNested.getCount();

		// for (int i = 1; i < count + 1; i++) {
		// IRPModelElement eachPkgElement = (IRPModelElement) allNested.getItem(i);
		// Get Requirements Model Diagram
		// if(nested.getMetaClass().equals("ObjectModelDiagram")) {
		//Note: "SimulinkBlockTemplate" comes from Simulink Profile
		if (eachPkgElement.getUserDefinedMetaClass().equalsIgnoreCase("Statechart") && (!eachPkgElement.getOwner().getName().equalsIgnoreCase("SimulinkBlockTemplate"))) {
			IRPStatechart mod = (IRPStatechart) eachPkgElement;
			String modelDiagName = mod.getName(); // Requirements Diagram
			String modelDiagIdentifier = mod.toString();
			System.out.println("State Chart Diagram/Model Name: " + modelDiagName);

			String packageName = "";
			if (!mod.getOwner().getName().isEmpty()) {
				packageName = mod.getOwner().getName();
			}

			// Each Model Element
			String allReqElementData = ""; // model level requirements data file
			String dataFileName = packageName.concat("_" + modelDiagName).concat("_StmDiagram.json").replace(" ", "-"); // packageName_ModelName_

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
			labelsArray.add("StateMachineDiagram");
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
			System.out.println("Stm Diagram Node >>> " + reqDiagElement.toJSONString());
			
			// create relationship with Class/BDD Block
			if(mod.getOwner().getMetaClass().equalsIgnoreCase("Class")) {
				// create Class/Block - State Machine Diagram (model) relationship
				JSONObject reqRelation = new JSONObject();
				reqRelation.put("id", "");
				reqRelation.put("type", "relationship");
				reqRelation.put("label", "CONTAINS_DIAGRAM");

				JSONObject reqRelationProperties = new JSONObject();
				reqRelationProperties.put("type", "State Machine");

				JSONObject reqRelationStart = new JSONObject();
				reqRelationStart.put("id", ownerIdentifier); // Source, class or block

				JSONObject reqRelationEnd = new JSONObject();
				reqRelationEnd.put("id", elementIdentifier); // Target

				reqRelation.put("properties", reqRelationProperties);
				reqRelation.put("start", reqRelationStart);
				reqRelation.put("end", reqRelationEnd);

				allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
				System.out.println("Block to State Machine Relation --> " + reqRelation.toJSONString());
			}else { 
				// NEED TO CHECK WHERE THIS Section is required or NOT
				// ----------------------------------------------------
				
				// create reqModel.node - reqModel.Parent (model) relationship
				JSONObject modelRelation = new JSONObject();
				modelRelation.put("id", "");
				modelRelation.put("type", "relationship");
				modelRelation.put("label", "CONTAINS_DIAGRAM");

				JSONObject modelRelationProperties = new JSONObject();
				modelRelationProperties.put("type", userDefinedMetaClass); //mod.getOwner().getUserDefinedMetaClass()

				JSONObject modelRelationStart = new JSONObject();
				modelRelationStart.put("id", ownerIdentifier); // Source, created a relationship with package or project

				JSONObject modelRelationEnd = new JSONObject();
				modelRelationEnd.put("id", elementIdentifier); // Target

				modelRelation.put("properties", modelRelationProperties);
				modelRelation.put("start", modelRelationStart);
				modelRelation.put("end", modelRelationEnd);

				allReqElementData = allReqElementData.concat(modelRelation.toJSONString()).concat("\n");
				System.out.println("Relation --> " + modelRelation.toJSONString());
			}
			
//			IRPCollection reqDiagramElements = eachPkgElement.getDependencies();
//			System.out.println("Requirement Element Count = " + reqDiagramElements.getCount());

			// Model Elements
			IRPCollection collection = mod.getElementsInDiagram();
			for (int k = 1; k <= collection.getCount(); k++) {
				IRPModelElement eachModelElement = (IRPModelElement) collection.getItem(k);
//						System.out.println("Model Element ---> MetaClass: "+eachModelElement.getMetaClass()+"<> Name: "+ eachModelElement.getName() +"<> FullPath: "+ eachModelElement.getFullPathName() + 
//								"<> Owner: "+ eachModelElement.getOwner().getName() + " <> OwnerFullPath: "+ eachModelElement.getOwner().getFullPathName() + 
//								"<>UserDefinedMetaClass: "+eachModelElement.getUserDefinedMetaClass());
				// System.out.println("ReqID: "+eachModelElement.getRequirementID()+"<> Spec: "+
				// eachModelElement.getSpecification()+"<> Desc: "+
				// eachModelElement.getDescription());

				// Or eachModelElement instanceof IRPRequirement
				if (eachModelElement.getMetaClass().equals("State")) {
					IRPState  req = (IRPState) eachModelElement;		

					// create requirement node
					String reqElementIdentifier = req.toString();
					String reqElementName = req.getName();
//							String desccription = req.getDescription();
					String reqElementUserDefinedMetaClass = req.getUserDefinedMetaClass();
					String reqElementMetaClass = req.getMetaClass();
					// String ownerIdentifier = req.getOwner().toString();
					String reqElementOwnerIdentifier = modelDiagIdentifier;
//							String ownerElementName = req.getOwner().getName();

					JSONObject reqElement = new JSONObject();
					reqElement.put("id", reqElementIdentifier);
					reqElement.put("type", "node");

					JSONArray reqElementlabelsArray = new JSONArray();
					reqElementlabelsArray.add(reqElementMetaClass); // Block
					reqElement.put("labels", reqElementlabelsArray);

					JSONObject reqElementProperties = new JSONObject();
					reqElementProperties.put("name", reqElementName);
					reqElementProperties.put("type", reqElementUserDefinedMetaClass);
					reqElementProperties.put("genericType", reqElementMetaClass);
					// reqElementProperties.put("description", desccription);
//						reqElementProperties.put("requirementId", req.getRequirementID());// requirementId
//						reqElementProperties.put("specification", req.getSpecification());// requirementSpecification
//						reqElementProperties.put("description", req.getDescription());// requirementDesc

					reqElement.put("properties", reqElementProperties);

					allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
					System.out.println("Node ~~> " + reqElement.toJSONString());

					// create req.node - req.diagramNode (model) relationship
					JSONObject reqRelation = new JSONObject();
					reqRelation.put("id", "");
					reqRelation.put("type", "relationship");
					reqRelation.put("label", "CONTAINS_STATEMACHINE_ELEMENT"); // CONTAINS_ELEMENT

					JSONObject reqRelationProperties = new JSONObject();
					reqRelationProperties.put("type", reqElementMetaClass); // State

					JSONObject reqRelationStart = new JSONObject();
					reqRelationStart.put("id", modelDiagIdentifier); // Source

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
				else if (eachModelElement.getMetaClass().equals("TerminationConnector")) {
					IRPConnector  req = (IRPConnector) eachModelElement;		

					// create requirement node
					String reqElementIdentifier = req.toString();
					String reqElementName = req.getName();
//							String desccription = req.getDescription();
					String reqElementUserDefinedMetaClass = req.getUserDefinedMetaClass();
					String reqElementMetaClass = req.getMetaClass();
					// String ownerIdentifier = req.getOwner().toString();
					String reqElementOwnerIdentifier = modelDiagIdentifier;
//							String ownerElementName = req.getOwner().getName();

					JSONObject reqElement = new JSONObject();
					reqElement.put("id", reqElementIdentifier);
					reqElement.put("type", "node");

					JSONArray reqElementlabelsArray = new JSONArray();
					reqElementlabelsArray.add("State"); // Block
					reqElement.put("labels", reqElementlabelsArray);

					JSONObject reqElementProperties = new JSONObject();
					reqElementProperties.put("name", reqElementName);
					reqElementProperties.put("type", reqElementUserDefinedMetaClass);
					reqElementProperties.put("genericType", reqElementMetaClass);
					// reqElementProperties.put("description", desccription);
//						reqElementProperties.put("requirementId", req.getRequirementID());// requirementId
//						reqElementProperties.put("specification", req.getSpecification());// requirementSpecification
//						reqElementProperties.put("description", req.getDescription());// requirementDesc

					reqElement.put("properties", reqElementProperties);

					allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
					System.out.println("Node ~~> " + reqElement.toJSONString());

					// create req.node - req.diagramNode (model) relationship
					JSONObject reqRelation = new JSONObject();
					reqRelation.put("id", "");
					reqRelation.put("type", "relationship");
					reqRelation.put("label", "CONTAINS_STATECHART_ELEMENT"); // CONTAINS_ELEMENT

					JSONObject reqRelationProperties = new JSONObject();
					reqRelationProperties.put("type", reqElementMetaClass); // State

					JSONObject reqRelationStart = new JSONObject();
					reqRelationStart.put("id", modelDiagIdentifier); // Source

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
				
				else if (eachModelElement.getMetaClass().equals("Transition") || eachModelElement.getMetaClass().equals("DefaultTransition")) {
					IRPTransition transition = (IRPTransition) eachModelElement;
//					for (int j = 1; j < relations.getCount() + 1; j++) {
						//IRPRelation relation = (IRPRelation) relations.getItem(j);

//							System.out.println(relation.getOfClass().getName()+","+relation.getOfClass().toString() + "," + relation.getOtherClass().getName() + ","
//									+ relation.getOtherClass().toString()+ ","+relation.getRelationType());		
						// create dependency relationship
						JSONObject dependencyRel = new JSONObject();
						dependencyRel.put("id", "");
						dependencyRel.put("type", "relationship");
						// dependencyRel.put("label", "IS_PART_OF");
						String labelName = "HAS_TRANSITION_WITH";
						dependencyRel.put("label", labelName);

						JSONObject dependencyRelProperties = new JSONObject();
						dependencyRelProperties.put("type", transition.getMetaClass());
						dependencyRelProperties.put("name", transition.getName());
//						dependencyRelProperties.put("label", transition.getItsLabel());
						String  action = "";
						if(transition.getItsAction() != null) {
							action = transition.getItsAction().getBody().replace('"', '\'').replaceAll("[\\t\\n\\r]+"," ");
						}
						dependencyRelProperties.put("action", action);
						String  guard = "";
						if(transition.getItsGuard() != null) {
							guard = transition.getItsGuard().getBody().replace('"', '\'').replaceAll("[\\t\\n\\r]+"," ");
						}
						dependencyRelProperties.put("guard", guard);						
						

						JSONObject dependencyRelStart = new JSONObject();
						dependencyRelStart.put("id", transition.getItsSource().toString()); // Source

						JSONObject dependencyRelEnd = new JSONObject();
						dependencyRelEnd.put("id", transition.getItsTarget().toString()); // Target

						dependencyRel.put("properties", dependencyRelProperties);
						dependencyRel.put("start", dependencyRelStart);
						dependencyRel.put("end", dependencyRelEnd);

						allReqElementData = allReqElementData.concat(dependencyRel.toJSONString()).concat("\n");
						System.out.println("Relation --> " + dependencyRel.toJSONString());		
						
//						IRPEvent event = IRPEvent
						
				}

			}
			if (allReqElementData != "") {
				try {
					Path filePath = Paths.get(parsedDataFilePath, dataFileName);
					FileWriter file = new FileWriter(filePath.toString());
					// file.write(jsonObject.toJSONString());
					file.write(allReqElementData);
					file.close();
					// createdFileCount = createdFileCount + 1;
					System.out.println("Requirements model JSON file created successfully!!!");
					System.out.println("File Path: " + filePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out.println(e);
				}
			}
		}
	}
	

	
	public void printActivityDiagram(IRPModelElement eachPkgElement) throws IOException {
		if (eachPkgElement.getUserDefinedMetaClass().equalsIgnoreCase("ActivityDiagram")) {
			IRPStatechart mod = (IRPStatechart) eachPkgElement;
			String modelDiagName = mod.getName(); // Activity Diagram
			String modelDiagIdentifier = mod.toString();
			System.out.println("Activity Diagram/Model Name: " + modelDiagName);

			String packageName = "";
			if (!mod.getOwner().getName().isEmpty()) {
				packageName = mod.getOwner().getName();
			}

			// Each Model Element
			String allReqElementData = ""; // model level requirements data file
			String dataFileName = packageName.concat("_" + modelDiagName).concat("_ActDiagram.json").replace(" ", "-"); // packageName_ModelName_

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
			labelsArray.add("ActivityDiagram");
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
			System.out.println("Act Diagram Node >>> " + reqDiagElement.toJSONString());
			
			// create relationship with Class/BDD Block
			if(mod.getOwner().getMetaClass().equalsIgnoreCase("Class")) {
				// create Class/Block - State Machine Diagram (model) relationship
				JSONObject reqRelation = new JSONObject();
				reqRelation.put("id", "");
				reqRelation.put("type", "relationship");
				reqRelation.put("label", "CONTAINS_DIAGRAM");

				JSONObject reqRelationProperties = new JSONObject();
				reqRelationProperties.put("type", "activity diagram");

				JSONObject reqRelationStart = new JSONObject();
				reqRelationStart.put("id", ownerIdentifier); // Source, class or block

				JSONObject reqRelationEnd = new JSONObject();
				reqRelationEnd.put("id", elementIdentifier); // Target

				reqRelation.put("properties", reqRelationProperties);
				reqRelation.put("start", reqRelationStart);
				reqRelation.put("end", reqRelationEnd);

				allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
				System.out.println("Block to State Machine Relation --> " + reqRelation.toJSONString());
			}

			// Model Elements
			IRPCollection collection = mod.getElementsInDiagram();
			for (int k = 1; k <= collection.getCount(); k++) {
				IRPModelElement eachModelElement = (IRPModelElement) collection.getItem(k);
				
				System.out.println("Class Name: "+eachModelElement.getClass().getName()+ " MetaClass: "+eachModelElement.getMetaClass() +
						" Name: "+eachModelElement.getName());
				// Or eachModelElement instanceof IRPRequirement
				if (eachModelElement.getMetaClass().equals("State")) {
					IRPState  req = (IRPState) eachModelElement;		

					// create requirement node
					String reqElementIdentifier = req.toString();
					String reqElementName = req.getName();
//							String desccription = req.getDescription();
					String reqElementUserDefinedMetaClass = req.getUserDefinedMetaClass();
					String reqElementMetaClass = req.getMetaClass();
					// String ownerIdentifier = req.getOwner().toString();
					String reqElementOwnerIdentifier = modelDiagIdentifier;
//							String ownerElementName = req.getOwner().getName();

					JSONObject reqElement = new JSONObject();
					reqElement.put("id", reqElementIdentifier);
					reqElement.put("type", "node");

					JSONArray reqElementlabelsArray = new JSONArray();
					reqElementlabelsArray.add(reqElementMetaClass); //Block
					reqElement.put("labels", reqElementlabelsArray);

					JSONObject reqElementProperties = new JSONObject();
					reqElementProperties.put("name", reqElementName);
					reqElementProperties.put("type", reqElementUserDefinedMetaClass);
					reqElementProperties.put("genericType", reqElementMetaClass);	
					
					reqElementProperties.put("entryAction", req.getEntryAction());
					reqElementProperties.put("exitAction", req.getExitAction());
//						reqElementProperties.put("requirementId", req.getRequirementID());// requirementId
//						reqElementProperties.put("specification", req.getSpecification());// requirementSpecification
//						reqElementProperties.put("description", req.getDescription());// requirementDesc

					reqElement.put("properties", reqElementProperties);

					allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
					System.out.println("Node ~~> " + reqElement.toJSONString());

					// create req.node - req.diagramNode (model) relationship
					JSONObject reqRelation = new JSONObject();
					reqRelation.put("id", "");
					reqRelation.put("type", "relationship");
					reqRelation.put("label", "CONTAINS_ACTIVITY_ELEMENT"); // CONTAIN_ELEMENT

					JSONObject reqRelationProperties = new JSONObject();
					reqRelationProperties.put("type", reqElementMetaClass); // State

					JSONObject reqRelationStart = new JSONObject();
					reqRelationStart.put("id", modelDiagIdentifier); // Source

					JSONObject reqRelationEnd = new JSONObject();
					reqRelationEnd.put("id", reqElementIdentifier); // Target

					reqRelation.put("properties", reqRelationProperties);
					reqRelation.put("start", reqRelationStart);
					reqRelation.put("end", reqRelationEnd);

					allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
					System.out.println("Relation --> " + reqRelation.toJSONString());
					
					//Find for Pin Source
//					IRPCollection pinCollection = eachModelElement.getNestedElements();		
//					for(int n = 1; n <= pinCollection.getCount(); n ++) {
//						IRPModelElement eachModelElement11 = (IRPModelElement) collection.getItem(n);
//						if (eachModelElement11.getMetaClass().equalsIgnoreCase("Pin")) {
//						IRPPin pin = (IRPPin) eachModelElement11;
//						System.out.println("Pin Element ----------------> MetaClass: "+pin.getMetaClass()+"<> Name: "+ pin.getName() +"<> FullPath: "+ pin.getFullPathName() + 
//								"<> Owner: "+ pin.getOwner().getName() + " <> OwnerFullPath: "+ pin.getOwner().getFullPathName() + 
//								"<>UserDefinedMetaClass: "+pin.getUserDefinedMetaClass());
//						}
//					}
					
					
				}
				else if (eachModelElement.getClass().getName().contains("RPConnector")) {
					System.out.println("%%%%%%% Inside Connector %%%%%%");
					IRPConnector  req = (IRPConnector) eachModelElement;		

					// create requirement node
					String reqElementIdentifier = req.toString();
					String reqElementName = req.getName();
//							String desccription = req.getDescription();
					String reqElementUserDefinedMetaClass = req.getUserDefinedMetaClass();
					String reqElementMetaClass = req.getMetaClass();
					// String ownerIdentifier = req.getOwner().toString();
					String reqElementOwnerIdentifier = modelDiagIdentifier;
//							String ownerElementName = req.getOwner().getName();

					JSONObject reqElement = new JSONObject();
					reqElement.put("id", reqElementIdentifier);
					reqElement.put("type", "node");

					JSONArray reqElementlabelsArray = new JSONArray();
					reqElementlabelsArray.add(reqElementMetaClass); // Block
					reqElement.put("labels", reqElementlabelsArray);

					JSONObject reqElementProperties = new JSONObject();
					reqElementProperties.put("name", reqElementName);
					reqElementProperties.put("type", reqElementUserDefinedMetaClass);
					reqElementProperties.put("genericType", reqElementMetaClass);
					// reqElementProperties.put("description", desccription);
//						reqElementProperties.put("requirementId", req.getRequirementID());// requirementId
//						reqElementProperties.put("specification", req.getSpecification());// requirementSpecification
//						reqElementProperties.put("description", req.getDescription());// requirementDesc

					reqElement.put("properties", reqElementProperties);

					allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
					System.out.println("Node ~~> " + reqElement.toJSONString());

					// create req.node - req.diagramNode (model) relationship
					JSONObject reqRelation = new JSONObject();
					reqRelation.put("id", "");
					reqRelation.put("type", "relationship");
					reqRelation.put("label", "CONTAINS_ACTIVITY_ELEMENT"); // CONTAIN_ELEMENT

					JSONObject reqRelationProperties = new JSONObject();
					reqRelationProperties.put("type", reqElementMetaClass); // State

					JSONObject reqRelationStart = new JSONObject();
					reqRelationStart.put("id", modelDiagIdentifier); // Source

					JSONObject reqRelationEnd = new JSONObject();
					reqRelationEnd.put("id", reqElementIdentifier); // Target

					reqRelation.put("properties", reqRelationProperties);
					reqRelation.put("start", reqRelationStart);
					reqRelation.put("end", reqRelationEnd);

					allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
					System.out.println("Relation --> " + reqRelation.toJSONString());
				}
				else if (eachModelElement.getMetaClass().equalsIgnoreCase("Pin")) {
					System.out.println("%%%%%%% Inside Pin %%%%%%");
					IRPPin  req = (IRPPin) eachModelElement;		

					// create Pin node
					String reqElementIdentifier = req.toString();
					String reqElementName = req.getName();
//							String desccription = req.getDescription();
					String reqElementUserDefinedMetaClass = req.getUserDefinedMetaClass();
					String reqElementMetaClass = req.getMetaClass();
					String reqElementOwnerIdentifier = req.getDependencies().toString(); // Need to fixed
										
					//String reqElementOwnerIdentifier = modelDiagIdentifier;
//							String ownerElementName = req.getOwner().getName();

					JSONObject reqElement = new JSONObject();
					reqElement.put("id", reqElementIdentifier);
					reqElement.put("type", "node");

					JSONArray reqElementlabelsArray = new JSONArray();
					reqElementlabelsArray.add(reqElementMetaClass); // Pin
					reqElement.put("labels", reqElementlabelsArray);

					JSONObject reqElementProperties = new JSONObject();
					reqElementProperties.put("name", reqElementName);
					reqElementProperties.put("type", reqElementUserDefinedMetaClass);
					reqElementProperties.put("genericType", reqElementMetaClass);
					reqElementProperties.put("pinType", req.getConnectorType());
					reqElementProperties.put("pinDirection", req.getPinDirection());
					// reqElementProperties.put("description", desccription);
//						reqElementProperties.put("requirementId", req.getRequirementID());// requirementId
//						reqElementProperties.put("specification", req.getSpecification());// requirementSpecification
//						reqElementProperties.put("description", req.getDescription());// requirementDesc

					reqElement.put("properties", reqElementProperties);

					allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
					System.out.println("Node ~~> " + reqElement.toJSONString());
					
					// create req.node - req.diagramNode (model) relationship
					JSONObject reqRelation = new JSONObject();
					reqRelation.put("id", "");
					reqRelation.put("type", "relationship");
					reqRelation.put("label", "HAS_PIN");

					JSONObject reqRelationProperties = new JSONObject();
					reqRelationProperties.put("type", reqElementMetaClass); // Pin
					//reqRelationProperties.put("type", req.get); // Pin

					JSONObject reqRelationStart = new JSONObject();
					reqRelationStart.put("id", req.getParent().toString()); // Source State

					JSONObject reqRelationEnd = new JSONObject();
					reqRelationEnd.put("id", reqElementIdentifier); // Target

					reqRelation.put("properties", reqRelationProperties);
					reqRelation.put("start", reqRelationStart);
					reqRelation.put("end", reqRelationEnd);

					allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
					System.out.println("Relation --> " + reqRelation.toJSONString());		
					
				}
				
				else if (eachModelElement.getMetaClass().equals("Transition") || eachModelElement.getMetaClass().equals("DefaultTransition")) {
					IRPTransition transition = (IRPTransition) eachModelElement;
//					for (int j = 1; j < relations.getCount() + 1; j++) {
						//IRPRelation relation = (IRPRelation) relations.getItem(j);

//							System.out.println(relation.getOfClass().getName()+","+relation.getOfClass().toString() + "," + relation.getOtherClass().getName() + ","
//									+ relation.getOtherClass().toString()+ ","+relation.getRelationType());		
						// create dependency relationship
						JSONObject dependencyRel = new JSONObject();
						dependencyRel.put("id", "");
						dependencyRel.put("type", "relationship");
						// dependencyRel.put("label", "IS_PART_OF");
						String labelName = "SEND_CONTROLDATA_TO";//HAS_TRANSITION_WITH
						if(transition.getUserDefinedMetaClass().equalsIgnoreCase("ObjectFlow")) {
							labelName = "SEND_OBJECTDATA_TO";
						}
						dependencyRel.put("label", labelName);

						JSONObject dependencyRelProperties = new JSONObject();
						dependencyRelProperties.put("type", transition.getUserDefinedMetaClass()); // ControlFlow, ObjectFlow
						dependencyRelProperties.put("name", transition.getName());
//						dependencyRelProperties.put("label", transition.getItsLabel());
						String  action = "";
						if(transition.getItsAction() != null) {
							action = transition.getItsAction().getBody().replace('"', '\'').replaceAll("[\\t\\n\\r]+"," ");
						}
						dependencyRelProperties.put("action", action);
						String  guard = "";
						if(transition.getItsGuard() != null) {
							guard = transition.getItsGuard().getBody().replace('"', '\'').replaceAll("[\\t\\n\\r]+"," ");
						}
						dependencyRelProperties.put("guard", guard);						
						

						JSONObject dependencyRelStart = new JSONObject();
						dependencyRelStart.put("id", transition.getItsSource().toString()); // Source

						JSONObject dependencyRelEnd = new JSONObject();
						dependencyRelEnd.put("id", transition.getItsTarget().toString()); // Target

						dependencyRel.put("properties", dependencyRelProperties);
						dependencyRel.put("start", dependencyRelStart);
						dependencyRel.put("end", dependencyRelEnd);

						allReqElementData = allReqElementData.concat(dependencyRel.toJSONString()).concat("\n");
						System.out.println("Relation --> " + dependencyRel.toJSONString());		
						
//						IRPEvent event = IRPEvent
						
				}
				
				else if(eachModelElement.getMetaClass().equals("Action")) {
					IRPAction action = (IRPAction) eachModelElement;
					System.out.println("@@@ Action: "+action.getName() + "identifier: "+action.toString());
					
					IRPModelElement eachModelElement2 = (IRPModelElement) action;
					if(eachModelElement2 !=null) {
						IRPDependency dep = (IRPDependency) eachModelElement;					
						String reqElementOwnerIdentifier = dep.getDependent().toString(); // Source
						System.out.println("<><><><><> "+dep.getDependent());
					}
				}

			}
			if (allReqElementData != "") {
				try {
					Path filePath = Paths.get(parsedDataFilePath, dataFileName);
					FileWriter file = new FileWriter(filePath.toString());
					// file.write(jsonObject.toJSONString());
					file.write(allReqElementData);
					file.close();
					// createdFileCount = createdFileCount + 1;
					System.out.println("Requirements model JSON file created successfully!!!");
					System.out.println("File Path: " + filePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out.println(e);
				}
			}
		}
	}
//	public ArrayList<IRPFlowchart> getActivityDiagrams() {
//		ArrayList<IRPClass> classesList = getClasses();
//
//		ArrayList<IRPFlowchart> activityDiagramList = new ArrayList<IRPFlowchart>();
//		for (int i = 0; i < classesList.size(); i++) {
//			// For each package find all occurrences of a use case
//			IRPClass pack = (IRPClass) classesList.get(i);
//			IRPFlowchart flowchart = pack.getActivityDiagram();
//
//			System.out.println(flowchart);
//			if (flowchart != null) {
//				activityDiagramList.add(flowchart);
//			}
//		}
//		return activityDiagramList;
//	}

	public void printAll(IRPModelElement element) {
		IRPCollection allNested = element.getNestedElementsRecursive();
		int count = allNested.getCount();
		for (int i = 1; i < count + 1; i++) {
			IRPModelElement nested = (IRPModelElement) allNested.getItem(i);
			System.out.println();
			System.out.println(nested.getName());
			System.out.println(nested.getMetaClass());
			System.out.println(nested.getDependencies());
			System.out.println(nested);
		}
	}

	public void writeAllToFile(IRPModelElement element) {
		IRPCollection allNested = element.getNestedElementsRecursive();
		int count = allNested.getCount();
		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet(element.getName());

		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", new Object[] { "Name", "Metaclass", "Owner/parent", "Identifier", "Dependencies" });

		for (int i = 1; i < count + 1; i++) {
			IRPModelElement nested = (IRPModelElement) allNested.getItem(i);
			IRPCollection dependencies = nested.getDependencies();
			String collective = "";
			for (int j = 1; j < dependencies.getCount(); j++) {
				collective += dependencies.getItem(j) + "";
			}
			data.put("" + (i + 1), new Object[] { nested.getName(), nested.getMetaClass(), "" + nested.getOwner(),
					"" + nested, collective });
			if (element.getName().equals("REQUIREMENTS")) {
				if (nested.getMetaClass().equals("ObjectModelDiagram")) {
					IRPObjectModelDiagram mod = (IRPObjectModelDiagram) nested;
					IRPCollection collection = mod.getElementsInDiagram();
					for (int j = 1; j <= collection.getCount(); j++) {
						IRPModelElement ele = (IRPModelElement) collection.getItem(j);
						System.out.println(ele.getMetaClass());
						if (ele.getMetaClass().equals("Dependency")) {
							IRPDependency dependency = (IRPDependency) ele;
							System.out.println("Dependent: " + dependency.getDependent().getName());
							System.out.println("Dependent on: " + dependency.getDependsOn().getName());
						}
					}
				}
			}
		}

		// Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(element.getName() + ".xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeBDDToFile2() throws IOException {

		FileWriter csvWriter = new FileWriter("BDD.csv");
		csvWriter.append("Name");
		csvWriter.append(",");
		csvWriter.append("Related To");
		csvWriter.append(",");
		csvWriter.append("Type");
		csvWriter.append("\n");
		ArrayList<IRPClass> classes = getClasses();

		for (IRPClass classItem : classes) {
			IRPCollection relations = classItem.getRelations();

			if (relations.getCount() == 0) {
				csvWriter.append("" + classItem.getName() + "," + ",");
				csvWriter.append("\n");

			}
			for (int j = 1; j < relations.getCount() + 1; j++) {
				IRPRelation relation = (IRPRelation) relations.getItem(j);
				csvWriter.append(relation.getOfClass().getName() + "," + relation.getOtherClass().getName() + ","
						+ relation.getRelationType());
				csvWriter.append("\n");
			}
		}

		csvWriter.flush();
		csvWriter.close();

		System.out.println("File has been written");

	}

	public void writeBlocksToFile() throws IOException {
		FileWriter csvWriter = new FileWriter("blocks.csv");
		ArrayList<IRPClass> classes = getClasses();
		for (IRPClass classItem : classes) {
			csvWriter.append(classItem.getName());
			csvWriter.append(",");
		}

		csvWriter.flush();
		csvWriter.close();

		System.out.println("File has been written");
	}

	/*
	 * Get's all value attributes in a block
	 */
	public ArrayList<RPAttribute> getValues(IRPClass block) {
		ArrayList<RPAttribute> valueList = new ArrayList<RPAttribute>();
		IRPCollection collection = block.getAttributes();

		if (collection.getCount() == 0) {
			return null;
		}

		for (int i = 1; i <= collection.getCount(); i++) {
			RPAttribute attribute = (RPAttribute) collection.getItem(i);
			System.out.println("Attribute: " + attribute.getName());
		}

		return valueList;
	}

	/*
	 * Writes the Internal Block Diagram to a file
	 * 
	 */
	public void printIBD() throws IOException {
		// IBD diagram info
		String projectName = prj.getName();
		String packageName = "abcd";
		String allReqElementData = ""; // model level requirements data file
		String dataFileName = projectName.concat("_IbdDiagram.json"); // projectName_fileName.json	

		ArrayList<IRPClass> classes = getClasses();
		for (IRPClass classItem : classes) {
			IRPCollection ports = classItem.getPorts();
			//IRPCollection allNested = classItem.getNestedElementsRecursive();
			IRPCollection flows = classItem.getFlows();
			IRPCollection links = classItem.getLinks();
			
			//int count = allNested.getCount();
			
			/*
			//* Not required at this moment
			// Process Parts
			for (int i = 1; i < count + 1; i++) {
				IRPModelElement nested = (IRPModelElement) allNested.getItem(i);
				if (nested.getMetaClass().equals("Object")) {
					JSONObject reqElement = new JSONObject();
					reqElement.put("id", nested.toString());
					reqElement.put("type", "node"); //Lifeline

					JSONArray reqElementlabelsArray = new JSONArray();
					reqElementlabelsArray.add("Part");
					reqElement.put("labels", reqElementlabelsArray);

					JSONObject reqElementProperties = new JSONObject();
					reqElementProperties.put("name", nested.getName());
					reqElementProperties.put("type", nested.getUserDefinedMetaClass());
					reqElementProperties.put("genericType", nested.getMetaClass());

					reqElement.put("properties", reqElementProperties);

					allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
					System.out.println("Part Node ~~> " + reqElement.toJSONString());

					// create part.node - req.diagramNode (model) relationship
					JSONObject reqRelation = new JSONObject();
					reqRelation.put("id", "");
					reqRelation.put("type", "relationship");
					reqRelation.put("label", "IS_PART_OF");

					JSONObject reqRelationProperties = new JSONObject();
					reqRelationProperties.put("type", nested.getMetaClass()); // State

					JSONObject reqRelationStart = new JSONObject();
					reqRelationStart.put("id", nested.toString()); // Source

					JSONObject reqRelationEnd = new JSONObject();
					reqRelationEnd.put("id", nested.getOwner().toString()); // Target

					reqRelation.put("properties", reqRelationProperties);
					reqRelation.put("start", reqRelationStart);
					reqRelation.put("end", reqRelationEnd);

					allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
					System.out.println("Part Relation --> " + reqRelation.toJSONString());
				}
			}
			*/
			// Process ports
			int amountOfPorts = ports.getCount();
			for (int j = 1; j <= amountOfPorts; j++) {
				IRPModelElement portElement1 = (IRPModelElement) ports.getItem(j);
				if (portElement1.getMetaClass().equalsIgnoreCase("SysMLPort")){
					IRPSysMLPort port = (IRPSysMLPort) ports.getItem(j);	
					
					JSONObject portElement = new JSONObject();
					portElement.put("id", port.toString());
					portElement.put("type", "node"); //

					JSONArray portElementlabelsArray = new JSONArray();
					portElementlabelsArray.add("Port");
					portElement.put("labels", portElementlabelsArray);

					JSONObject portElementProperties = new JSONObject();
					portElementProperties.put("name", port.getName());
					portElementProperties.put("type", port.getUserDefinedMetaClass());
					portElementProperties.put("genericType", port.getMetaClass());
					portElementProperties.put("direction", port.getPortDirection());
					portElementProperties.put("dataType", port.getType().getName());

					portElement.put("properties", portElementProperties);

					allReqElementData = allReqElementData.concat(portElement.toJSONString()).concat("\n");
					System.out.println("Port Node ~~> " + portElement.toJSONString());

					// create port.node - port.block relationship
					JSONObject portRelation = new JSONObject();
					portRelation.put("id", "");
					portRelation.put("type", "relationship");
					portRelation.put("label", "HAS_PORT");

					JSONObject portRelationProperties = new JSONObject();
					portRelationProperties.put("type", port.getMetaClass()); // State

					JSONObject portRelationStart = new JSONObject();
					portRelationStart.put("id", port.getOwner().toString()); // Source

					JSONObject portRelationEnd = new JSONObject();
					portRelationEnd.put("id", port.toString()); // Target

					portRelation.put("properties", portRelationProperties);
					portRelation.put("start", portRelationStart);
					portRelation.put("end", portRelationEnd);

					allReqElementData = allReqElementData.concat(portRelation.toJSONString()).concat("\n");
					System.out.println("Port Relation --> " + portRelation.toJSONString());							
		
					
					
					//Create reference for Simulink Model in/outport node
					
					IRPCollection tags = port.getOwner().getAllTags();
					System.out.println("TagCount: "+tags.getCount());
					String fileNameWithoutExt = "";
					for (int jj = 1; jj < tags.getCount() + 1; jj++) {
//						System.out.println("Inside Tag");
						IRPTag tag1 = (IRPTag) tags.getItem(jj);
						if (tag1.getName().equalsIgnoreCase("SimulinkProjectFile") && !tag1.getValue().equalsIgnoreCase("")) {
							// create block.node - Simulink (reference model) relationship
							// Prepare Simulink model id from filePath
							String filePath = tag1.getValue().toString();
							Path path = Paths.get(filePath);
							String fileNameOnly = path.getFileName().toString();
//							String fileNameWithoutExt = "";

							if (fileNameOnly.indexOf(".") > 0) {
								fileNameWithoutExt = fileNameOnly.substring(0, fileNameOnly.lastIndexOf("."));
								} else {
									fileNameWithoutExt = fileNameOnly;
								}
						}
					}
					
					if(!fileNameWithoutExt.equalsIgnoreCase("")) {
					// create SysML port.node - Simulink in/outport block relationship
					JSONObject crossPortRelation = new JSONObject();
					crossPortRelation.put("id", "");
					crossPortRelation.put("type", "relationship");
					crossPortRelation.put("label", "IS_CONNECTED_WITH");

					JSONObject crossPortRelationProperties = new JSONObject();
					crossPortRelationProperties.put("type", port.getMetaClass()); // State

					JSONObject crossPortRelationStart = new JSONObject();
					crossPortRelationStart.put("id", port.toString()); // Source
					
					String nodeFullPath =  fileNameWithoutExt.concat("/").concat(port.getName());
					//System.out.println("@@@@@ FullPath: "+nodeFullPath);
					JSONObject crossPortRelationEnd = new JSONObject();
					crossPortRelationEnd.put("id", nodeFullPath); // Target

					crossPortRelation.put("properties", crossPortRelationProperties);
					crossPortRelation.put("start", crossPortRelationStart);
					crossPortRelation.put("end", crossPortRelationEnd);

					allReqElementData = allReqElementData.concat(crossPortRelation.toJSONString()).concat("\n");
					System.out.println("Port Relation --> " + crossPortRelation.toJSONString());					
					}
				}else if(portElement1.getMetaClass().equalsIgnoreCase("Port")) {
					IRPPort port = (IRPPort) ports.getItem(j);
					
					JSONObject portElement = new JSONObject();
					portElement.put("id", port.toString());
					portElement.put("type", "node"); //

					JSONArray portElementlabelsArray = new JSONArray();
					portElementlabelsArray.add("Port");
					portElement.put("labels", portElementlabelsArray);

					JSONObject portElementProperties = new JSONObject();
					portElementProperties.put("name", port.getName());
					portElementProperties.put("type", port.getUserDefinedMetaClass());
					portElementProperties.put("genericType", port.getMetaClass());
					portElementProperties.put("direction", "");
					portElementProperties.put("dataType", port.getRelationType());

					portElement.put("properties", portElementProperties);

					allReqElementData = allReqElementData.concat(portElement.toJSONString()).concat("\n");
					System.out.println("Port Node ~~> " + portElement.toJSONString());

					// create port.node - port.block relationship
					JSONObject portRelation = new JSONObject();
					portRelation.put("id", "");
					portRelation.put("type", "relationship");
					portRelation.put("label", "HAS_PORT");

					JSONObject portRelationProperties = new JSONObject();
					portRelationProperties.put("type", port.getMetaClass()); // State

					JSONObject portRelationStart = new JSONObject();
					portRelationStart.put("id", port.getOwner().toString()); // Source

					JSONObject portRelationEnd = new JSONObject();
					portRelationEnd.put("id", port.toString()); // Target

					portRelation.put("properties", portRelationProperties);
					portRelation.put("start", portRelationStart);
					portRelation.put("end", portRelationEnd);
					
					allReqElementData = allReqElementData.concat(portRelation.toJSONString()).concat("\n");
					System.out.println("Port Relation --> " + portRelation.toJSONString());
				}						
			}

			int amountOfFlows = flows.getCount();
			for (int j = 1; j <= amountOfFlows; j++) {
				IRPFlow flow = (IRPFlow) flows.getItem(j);
				IRPPort end1 = flow.getEnd1Port();
				IRPPort end2 = flow.getEnd2Port();
				
				String flowName = flow.getName();
				String source_element = "";
				String target_element = "";
				
				if (flow.getDirection() == "toEnd2") {
					source_element = end1.toString();
					target_element = end2.toString();
				} else {
					source_element = end2.toString();
					target_element = end1.toString();
				}
				// create flow relationship
				JSONObject flowRelation = new JSONObject();
				flowRelation.put("id", "");
				flowRelation.put("type", "relationship");
				flowRelation.put("label", "HAS_FLOWS_TO");

				JSONObject flowRelationProperties = new JSONObject(); 
				flowRelationProperties.put("name", flow.getName());
				flowRelationProperties.put("type", flow.getMetaClass());

				JSONObject flowRelationStart = new JSONObject();
				flowRelationStart.put("id", source_element); // Source

				JSONObject flowRelationEnd = new JSONObject();
				flowRelationEnd.put("id", target_element); // Target

				flowRelation.put("properties", flowRelationProperties);
				flowRelation.put("start", flowRelationStart);
				flowRelation.put("end", flowRelationEnd);

				allReqElementData = allReqElementData.concat(flowRelation.toJSONString()).concat("\n");
				System.out.println("Flow Relation --> " + flowRelation.toJSONString());
			}
			
			//Links or connectors
			int amountOflinks = links.getCount();
			System.out.println("amountOflinks--------------->: "+amountOflinks);
			for (int l = 1; l <= amountOflinks; l++) {
				IRPLink link = (IRPLink) links.getItem(l);
				
				String linkName = link.getName();
				String source_element = "";
				String target_element = "";
				
				if(link.getFromSysMLPort() != null && link.getToSysMLPort() != null) {
					source_element = link.getFromSysMLPort().toString();
					target_element = link.getToSysMLPort().toString();
				}else if (link.getFromPort() != null && link.getToPort() != null){
					source_element = link.getFromElement().toString();
					target_element = link.getToPort().toString();
				}
//				
//				System.out.println("Links  ---> MetaClass: "+link.getMetaClass()+"<> Name: "+ link.getName() +"<> FullPath: "+ link.getFullPathName() + 
//						"<> Owner: "+ link.getOwner().getName() + " <> OwnerFullPath: "+ link.getOwner().getFullPathName() + 
//						"<>OwnerIdentifier: "+link.getOwner().toString()+" <>UserDefinedMetaClass: "+link.getUserDefinedMetaClass()+
//						"<>Identifier: "+link.toString() + "<>sourcePort: "+link.getFromSysMLPort() + "<>dstPort: "+link.getToSysMLPort()+
//						"<>link.getOwner().getMetaClass()"+link.getOwner().getMetaClass());
//				if (link.getOwner().getMetaClass().equalsIgnoreCase("Port")) {
//					source_element = link.getFromElement().getName();
//					target_element = link.getToPort().getName();
//				} else if (link.getOwner().getMetaClass().equalsIgnoreCase("SysMLPort")){
//					source_element = link.getFromSysMLPort().getName();
//					target_element = link.getToSysMLPort().getName();
//				}
				
				// create link/connection relationship
				JSONObject linkRelation = new JSONObject();
				linkRelation.put("id", "");
				linkRelation.put("type", "relationship");
				linkRelation.put("label", "HAS_LINKS_TO");

				JSONObject linkRelationProperties = new JSONObject(); 
				linkRelationProperties.put("name", linkName);
				linkRelationProperties.put("type", link.getMetaClass());

				JSONObject linkRelationStart = new JSONObject();
				linkRelationStart.put("id", source_element); // Source

				JSONObject linkRelationEnd = new JSONObject();
				linkRelationEnd.put("id", target_element); // Target

				linkRelation.put("properties", linkRelationProperties);
				linkRelation.put("start", linkRelationStart);
				linkRelation.put("end", linkRelationEnd);

				allReqElementData = allReqElementData.concat(linkRelation.toJSONString()).concat("\n");
				System.out.println("Link Relation --> " + linkRelation.toJSONString());
			}

		}
		if (allReqElementData != "") {
			try {
				Path filePath = Paths.get(parsedDataFilePath, dataFileName);
				FileWriter file = new FileWriter(filePath.toString());
				// file.write(jsonObject.toJSONString());
				file.write(allReqElementData);
				file.close();
				// createdFileCount = createdFileCount + 1;
				System.out.println("IBD Diagram JSON file created successfully!!!");
				System.out.println("File Path: " + filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println(e);
			}
		}
	}
	/*
	public void printIBD() throws IOException {
		FileWriter partCsvWriter = new FileWriter("parts.csv");

		FileWriter portCsvWriter = new FileWriter("ports.csv");

		FileWriter flowCsvWriter = new FileWriter("flows.csv");

		ArrayList<IRPClass> classes = getClasses();
		for (IRPClass classItem : classes) {
			IRPCollection ports = classItem.getPorts();
			IRPCollection allNested = classItem.getNestedElementsRecursive();
			IRPCollection flows = classItem.getFlows();
			int count = allNested.getCount();

			for (int i = 1; i < count + 1; i++) {
				IRPModelElement nested = (IRPModelElement) allNested.getItem(i);
				if (nested.getMetaClass().equals("Object")) {

					partCsvWriter.append(nested.getName());
					partCsvWriter.append(",");
					partCsvWriter.append(nested.getOwner().getName());
					partCsvWriter.append("\n");
					System.out.println(nested.getMetaClass());
				}
			}

			int amountOfPorts = ports.getCount();
			for (int j = 1; j <= amountOfPorts; j++) {
				IRPPort port = (IRPPort) ports.getItem(j);
				portCsvWriter.append(port.getName());
				portCsvWriter.append(",");
				portCsvWriter.append(port.getOwner().getName());
				portCsvWriter.append("\n");
			}

			int amountOfFlows = flows.getCount();
			for (int j = 1; j <= amountOfFlows; j++) {
				IRPFlow flow = (IRPFlow) flows.getItem(j);
				IRPPort end1 = flow.getEnd1Port();
				IRPPort end2 = flow.getEnd2Port();
				flowCsvWriter.append(flow.getName());
				flowCsvWriter.append(",");
				if (flow.getDirection() == "toEnd2") {
					flowCsvWriter.append(end1.getName());
					flowCsvWriter.append(",");
					flowCsvWriter.append(end2.getName());
				} else {
					flowCsvWriter.append(end2.getName());
					flowCsvWriter.append(",");
					flowCsvWriter.append(end1.getName());
				}
				flowCsvWriter.append("\n");
			}

		}

		System.out.println("csv file have been written to a file");
		partCsvWriter.flush();
		portCsvWriter.flush();
		flowCsvWriter.flush();
		partCsvWriter.close();
		portCsvWriter.close();
		flowCsvWriter.close();
	}
	*/
	
	public void printRequirements(IRPModelElement element) throws IOException {

		String packageName = element.getName();

//		FileWriter requirementsCsvWriter = new FileWriter("requirements.csv");  
//		FileWriter reqDependencyCsvWriter = new FileWriter("requirementsDependency.csv");
		IRPCollection allNested = element.getNestedElementsRecursive();
		int count = allNested.getCount();

		for (int i = 1; i < count + 1; i++) {
			IRPModelElement eachPkgElement = (IRPModelElement) allNested.getItem(i);
			// Get Requirements Model Diagram
			// if(nested.getMetaClass().equals("ObjectModelDiagram")) {
			if (eachPkgElement.getUserDefinedMetaClass().equalsIgnoreCase("Requirements Diagram")) {
				IRPObjectModelDiagram mod = (IRPObjectModelDiagram) eachPkgElement;
				String modelDiagName = mod.getName(); // Requirements Diagram
				String modelDiagIdentifier = mod.toString();
				System.out.println("Req Diagram/Model Name: " + modelDiagName);

				// Each Model Element
				String allReqElementData = ""; // model level requirements data file
				String dataFileName = packageName.concat("_" + modelDiagName).concat("_ReqDiagram.json").replace(" ","-"); // packageName_ModelName_

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
				labelsArray.add("RequirementDiagram"); // changed from Diagram to RequirementDiagram
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
				
				// create reqModel.node - reqModel.Parent (model) relationship
				JSONObject modelRelation = new JSONObject();
				modelRelation.put("id", "");
				modelRelation.put("type", "relationship");
				modelRelation.put("label", "CONTAINS_DIAGRAM");

				JSONObject modelRelationProperties = new JSONObject();
				modelRelationProperties.put("type", userDefinedMetaClass); //mod.getOwner().getUserDefinedMetaClass()

				JSONObject modelRelationStart = new JSONObject();
				modelRelationStart.put("id", ownerIdentifier); // Source, created a relationship with package or project

				JSONObject modelRelationEnd = new JSONObject();
				modelRelationEnd.put("id", modelDiagIdentifier); // Target

				modelRelation.put("properties", modelRelationProperties);
				modelRelation.put("start", modelRelationStart);
				modelRelation.put("end", modelRelationEnd);

				allReqElementData = allReqElementData.concat(modelRelation.toJSONString()).concat("\n");
				System.out.println("Relation --> " + modelRelation.toJSONString());

				// Model Elements
				IRPCollection collection = mod.getElementsInDiagram();
				for (int k = 1; k <= collection.getCount(); k++) {
					IRPModelElement eachModelElement = (IRPModelElement) collection.getItem(k);
//						System.out.println("Model Element ---> MetaClass: "+eachModelElement.getMetaClass()+"<> Name: "+ eachModelElement.getName() +"<> FullPath: "+ eachModelElement.getFullPathName() + 
//								"<> Owner: "+ eachModelElement.getOwner().getName() + " <> OwnerFullPath: "+ eachModelElement.getOwner().getFullPathName() + 
//								"<>UserDefinedMetaClass: "+eachModelElement.getUserDefinedMetaClass());
					// System.out.println("ReqID: "+eachModelElement.getRequirementID()+"<> Spec: "+
					// eachModelElement.getSpecification()+"<> Desc: "+
					// eachModelElement.getDescription());

					// Or eachModelElement instanceof IRPRequirement
					if (eachModelElement.getMetaClass().equals("Requirement")) {
						IRPRequirement req = (IRPRequirement) eachModelElement;

						// create requirement node
						String reqElementIdentifier = req.toString();
						String reqElementName = req.getName();
//							String desccription = req.getDescription();
						String reqElementUserDefinedMetaClass = req.getUserDefinedMetaClass();
						String reqElementMetaClass = req.getMetaClass();
						// String ownerIdentifier = req.getOwner().toString();
						String reqElementOwnerIdentifier = modelDiagIdentifier;
//							String ownerElementName = req.getOwner().getName();

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
						reqRelation.put("label", "CONTAINS_"+reqElementUserDefinedMetaClass.toUpperCase()); //CONTAINS_REQUIREMENT

						JSONObject reqRelationProperties = new JSONObject();
						reqRelationProperties.put("type", reqElementUserDefinedMetaClass);

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
							dependencyRel.put("label", "IS_CONNECTED_WITH_REQUIREMENT"); //IS_CONNECTED_WITH

							JSONObject dependencyRelProperties = new JSONObject();
							dependencyRelProperties.put("type", elementDependency.getUserDefinedMetaClass());

							JSONObject dependencyRelStart = new JSONObject();
							dependencyRelStart.put("id", elementDependency.getDependent().toString()); // Source

							JSONObject dependencyRelEnd = new JSONObject();
							dependencyRelEnd.put("id", elementDependency.getDependsOn().toString()); // Target

							dependencyRel.put("properties", dependencyRelProperties);
							dependencyRel.put("start", dependencyRelStart);
							dependencyRel.put("end", dependencyRelEnd);

							allReqElementData = allReqElementData.concat(dependencyRel.toJSONString()).concat("\n");
							System.out.println("Relation --> " + dependencyRel.toJSONString());
						} else {
							// Do nothing. Because this element may be comment or constraint block and is
							// not in our scope
						}
					}
				}
				if (allReqElementData != "") {
					try {
						Path filePath = Paths.get(parsedDataFilePath, dataFileName);
						FileWriter file = new FileWriter(filePath.toString());
						// file.write(jsonObject.toJSONString());
						file.write(allReqElementData);
						file.close();
						// createdFileCount = createdFileCount + 1;
						System.out.println("Requirements model JSON file created successfully!!!");
						System.out.println("File Path: " + filePath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
						System.out.println(e);
					}
				}
			}
		}
	}
	
	public void printModelPackages() throws IOException {
		// Project Package info
		String projectName = prj.getName();
		String allElementData = ""; // model level requirements data file
		String dataFileName = projectName.concat("_ProjectPackageInfo.json"); // projectName_fileName.json	
		
		// create the Project node		
		JSONObject projectElement = new JSONObject();
		projectElement.put("id", prj.toString());
		projectElement.put("type", "node");

		JSONArray labelsArray = new JSONArray();
		labelsArray.add("Project");
		projectElement.put("labels", labelsArray);

		JSONObject reqDiagElementProperties = new JSONObject();
		reqDiagElementProperties.put("name", prj.getName());
		reqDiagElementProperties.put("type", prj.getUserDefinedMetaClass());
		reqDiagElementProperties.put("genericType", prj.getMetaClass());
		reqDiagElementProperties.put("description", prj.getDescription());

		projectElement.put("properties", reqDiagElementProperties);

		allElementData = allElementData.concat(projectElement.toJSONString()).concat("\n");
		System.out.println("Project Node >>> " + projectElement.toJSONString());

		// Process packages
		ArrayList<IRPPackage> packageList = getPackages();
		System.out.println("----------> PackageCount = "+packageList.size());
		for(IRPPackage pack : packageList) {
			
			if(pack != null) {
				IRPCollection allNested = pack.getNestedElementsRecursive();
				int count = allNested.getCount();
				for (int i = 1; i < count + 1; i++) {
					IRPModelElement nested = (IRPModelElement) allNested.getItem(i);	
					if(nested.getMetaClass().equalsIgnoreCase("Package") && nested.getOwner().getName().equalsIgnoreCase(projectName)){
						// create the Packages node		
						JSONObject packageElement = new JSONObject();
						packageElement.put("id", nested.toString());
						packageElement.put("type", "node");

						JSONArray packageLabelsArray = new JSONArray();
						packageLabelsArray.add("Package");
						packageElement.put("labels", packageLabelsArray);

						JSONObject packageElementProperties = new JSONObject();
						String packageName = nested.getName();
						packageElementProperties.put("name", packageName);
						packageElementProperties.put("type", nested.getUserDefinedMetaClass());
						packageElementProperties.put("genericType", nested.getMetaClass());
						packageElementProperties.put("description", nested.getDescription());
						packageElementProperties.put("ownerName", nested.getOwner().getName());
						packageElementProperties.put("ownerType", nested.getOwner().getMetaClass());
						packageElementProperties.put("ownerIdentifier", nested.getOwner().toString());

						packageElement.put("properties", packageElementProperties);

						allElementData = allElementData.concat(packageElement.toJSONString()).concat("\n");
						System.out.println("Package Node >>> " + packageElement.toJSONString());	
						
						// create Project Package relationship
						JSONObject dependencyRel = new JSONObject();
						dependencyRel.put("id", "");
						dependencyRel.put("type", "relationship");
						// dependencyRel.put("label", "IS_PART_OF");
						dependencyRel.put("label", "CONTAINS_PACKAGE"); //CONTAINS

						JSONObject dependencyRelProperties = new JSONObject();
						dependencyRelProperties.put("type", "package");

						JSONObject dependencyRelStart = new JSONObject();
						dependencyRelStart.put("id", prj.toString()); // Source

						JSONObject dependencyRelEnd = new JSONObject();
						dependencyRelEnd.put("id", nested.toString()); // Target

						dependencyRel.put("properties", dependencyRelProperties);
						dependencyRel.put("start", dependencyRelStart);
						dependencyRel.put("end", dependencyRelEnd);

						allElementData = allElementData.concat(dependencyRel.toJSONString()).concat("\n");
						System.out.println("Project Package --> " + dependencyRel.toJSONString());
						
						//get recursive packages
						IRPCollection allNestedPackages = nested.getNestedElementsRecursive();
						int nestedPackCount = allNestedPackages.getCount();
						for (int n = 1; n < nestedPackCount + 1; n++) {
							IRPModelElement nestedPack = (IRPModelElement) allNestedPackages.getItem(n);	
							if(nestedPack.getMetaClass().equalsIgnoreCase("Package") && !nestedPack.getName().equalsIgnoreCase(packageName)){
								// create the Packages node		
								JSONObject nestedPackageElement = new JSONObject();
								nestedPackageElement.put("id", nestedPack.toString());
								nestedPackageElement.put("type", "node");

								JSONArray nestedPackageLabelsArray = new JSONArray();
								nestedPackageLabelsArray.add("Package");
								nestedPackageElement.put("labels", nestedPackageLabelsArray);

								JSONObject nestedPackageElementProperties = new JSONObject();
								//String nestedPackageName = nestedPack.getName();
								nestedPackageElementProperties.put("name", nestedPack.getName());
								nestedPackageElementProperties.put("type", nestedPack.getUserDefinedMetaClass());
								nestedPackageElementProperties.put("genericType", nestedPack.getMetaClass());
								nestedPackageElementProperties.put("description", nestedPack.getDescription());
								nestedPackageElementProperties.put("ownerName", nestedPack.getOwner().getName());
								nestedPackageElementProperties.put("ownerType", nestedPack.getOwner().getMetaClass());
								nestedPackageElementProperties.put("ownerIdentifier", nestedPack.getOwner().toString());

								nestedPackageElement.put("properties", nestedPackageElementProperties);

								allElementData = allElementData.concat(nestedPackageElement.toJSONString()).concat("\n");
								System.out.println("Nested Package Node >>> " + nestedPackageElement.toJSONString());	
								
								// create Project Package relationship
								JSONObject nestedDependencyRel = new JSONObject();
								nestedDependencyRel.put("id", "");
								nestedDependencyRel.put("type", "relationship");
								// nestedDependencyRel.put("label", "IS_PART_OF");
								nestedDependencyRel.put("label", "CONTAINS_PACKAGE"); //CONTAINS

								JSONObject nestedDependencyRelProperties = new JSONObject();
								nestedDependencyRelProperties.put("type", "package");
								
								String ownerPackageId = nestedPack.getOwner().toString();
								JSONObject nestedDependencyRelStart = new JSONObject();
								nestedDependencyRelStart.put("id", ownerPackageId); // Source

								JSONObject nestedDependencyRelEnd = new JSONObject();
								nestedDependencyRelEnd.put("id", nestedPack.toString()); // Target

								nestedDependencyRel.put("properties", nestedDependencyRelProperties);
								nestedDependencyRel.put("start", nestedDependencyRelStart);
								nestedDependencyRel.put("end", nestedDependencyRelEnd);

								allElementData = allElementData.concat(nestedDependencyRel.toJSONString()).concat("\n");
								System.out.println("Nested Package Rel --> " + nestedDependencyRel.toJSONString());
							}
						}
					}	
				}
			
			}
		}
		if (allElementData != "") {
			try {
				Path filePath = Paths.get(parsedDataFilePath, dataFileName);
				FileWriter file = new FileWriter(filePath.toString());
				// file.write(jsonObject.toJSONString());
				file.write(allElementData);
				file.close();
				// createdFileCount = createdFileCount + 1;
				System.out.println("Project Package JSON file created successfully!!!");
				System.out.println("File Path: " + filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println(e);
			}
		}
		
	}

	public void printBDD(IRPModelElement eachPkgElement) throws IOException {

		// String modelDigramName = eachPkgElement.getName();

//		FileWriter requirementsCsvWriter = new FileWriter("requirements.csv");  
//		FileWriter reqDependencyCsvWriter = new FileWriter("requirementsDependency.csv");
//		IRPCollection allNested = element.getNestedElementsRecursive();
//		int count = allNested.getCount();

		// for (int i = 1; i < count + 1; i++) {
		// IRPModelElement eachPkgElement = (IRPModelElement) allNested.getItem(i);
		// Get Requirements Model Diagram
		// if(nested.getMetaClass().equals("ObjectModelDiagram")) {
		if (eachPkgElement.getUserDefinedMetaClass().equalsIgnoreCase("Block Definition Diagram")) {
			IRPObjectModelDiagram mod = (IRPObjectModelDiagram) eachPkgElement;
			String modelDiagName = mod.getName(); // Requirements Diagram
			String modelDiagIdentifier = mod.toString();
			System.out.println("BDD Diagram/Model Name: " + modelDiagName);

			String packageName = "";
			if (!mod.getOwner().getName().isEmpty()) {
				packageName = mod.getOwner().getName();
			}

			// Each Model Element
			String allReqElementData = ""; // model level requirements data file
			String dataFileName = packageName.concat("_" + modelDiagName).concat("_BddDiagram.json").replace(" ", "-"); // packageName_ModelName_

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
			labelsArray.add("BDDDiagram");
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
			
			// create reqModel.node - reqModel.Parent (model) relationship
			JSONObject modelRelation = new JSONObject();
			modelRelation.put("id", "");
			modelRelation.put("type", "relationship");
			modelRelation.put("label", "CONTAINS_DIAGRAM");

			JSONObject modelRelationProperties = new JSONObject();
			modelRelationProperties.put("type", userDefinedMetaClass); //mod.getOwner().getUserDefinedMetaClass()

			JSONObject modelRelationStart = new JSONObject();
			modelRelationStart.put("id", ownerIdentifier); // Source, created a relationship with package or project

			JSONObject modelRelationEnd = new JSONObject();
			modelRelationEnd.put("id", modelDiagIdentifier); // Target

			modelRelation.put("properties", modelRelationProperties);
			modelRelation.put("start", modelRelationStart);
			modelRelation.put("end", modelRelationEnd);

			allReqElementData = allReqElementData.concat(modelRelation.toJSONString()).concat("\n");
			System.out.println("Relation --> " + modelRelation.toJSONString());

			// Model Elements
			IRPCollection collection = mod.getElementsInDiagram();
			for (int k = 1; k <= collection.getCount(); k++) {
				IRPModelElement eachModelElement = (IRPModelElement) collection.getItem(k);
//						System.out.println("Model Element ---> MetaClass: "+eachModelElement.getMetaClass()+"<> Name: "+ eachModelElement.getName() +"<> FullPath: "+ eachModelElement.getFullPathName() + 
//								"<> Owner: "+ eachModelElement.getOwner().getName() + " <> OwnerFullPath: "+ eachModelElement.getOwner().getFullPathName() + 
//								"<>UserDefinedMetaClass: "+eachModelElement.getUserDefinedMetaClass());
				// System.out.println("ReqID: "+eachModelElement.getRequirementID()+"<> Spec: "+
				// eachModelElement.getSpecification()+"<> Desc: "+
				// eachModelElement.getDescription());

				// Or eachModelElement instanceof IRPRequirement
				if (eachModelElement.getMetaClass().equals("Class")) {
					IRPClass classItem = (IRPClass) eachModelElement;

					// create requirement node
					String reqElementIdentifier = classItem.toString();
					String reqElementName = classItem.getName();
//							String desccription = req.getDescription();
					String reqElementUserDefinedMetaClass = classItem.getUserDefinedMetaClass();
					String reqElementMetaClass = classItem.getMetaClass();
					// String ownerIdentifier = req.getOwner().toString();
					String reqElementOwnerIdentifier = modelDiagIdentifier;
//							String ownerElementName = req.getOwner().getName();

					JSONObject reqElement = new JSONObject();
					reqElement.put("id", reqElementIdentifier);
					reqElement.put("type", "node");
					
					JSONArray reqElementlabelsArray = new JSONArray();
					IRPCollection stereotypes = classItem.getStereotypes();
					for (int s = 1; s < stereotypes.getCount() + 1; s++) {
						IRPStereotype stereo = (IRPStereotype) stereotypes.getItem(s);
						// Find block types except general Block type						
						if(!stereo.getName().equalsIgnoreCase("Block")) {
							reqElementlabelsArray.add(stereo.getName());
							System.out.println("---> StereoType: "+stereo.getName());
						}
						
						//reqElementlabelsArray.add(stereo.getName()); // all Block types
					}					
//					JSONArray reqElementlabelsArray = new JSONArray();
					if(reqElementlabelsArray.isEmpty()) {
						reqElementlabelsArray.add("Block");						
					}
					reqElement.put("labels", reqElementlabelsArray);

					JSONObject reqElementProperties = new JSONObject();
					reqElementProperties.put("name", reqElementName);
					reqElementProperties.put("type", reqElementUserDefinedMetaClass);
					reqElementProperties.put("genericType", reqElementMetaClass);
					// reqElementProperties.put("description", desccription);
//						reqElementProperties.put("requirementId", req.getRequirementID());// requirementId
//						reqElementProperties.put("specification", req.getSpecification());// requirementSpecification
//						reqElementProperties.put("description", req.getDescription());// requirementDesc

					reqElement.put("properties", reqElementProperties);

					allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
					System.out.println("Node ~~> " + reqElement.toJSONString());

					// create req.node - req.diagramNode (model) relationship
					JSONObject reqRelation = new JSONObject();
					reqRelation.put("id", "");
					reqRelation.put("type", "relationship");
					reqRelation.put("label", "CONTAINS_BDD_ELEMENT"); // CONTAINS_BLOCK

					JSONObject reqRelationProperties = new JSONObject();
					reqRelationProperties.put("type", reqElementUserDefinedMetaClass);

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
					
					//Create reference for Simulink Model
					IRPCollection tags = classItem.getAllTags();
					System.out.println("TagCount: "+tags.getCount());
					for (int j = 1; j < tags.getCount() + 1; j++) {
//						System.out.println("Inside Tag");
						IRPTag tag1 = (IRPTag) tags.getItem(j);
						if (tag1.getName().equalsIgnoreCase("SimulinkProjectFile") && !tag1.getValue().equalsIgnoreCase("")) {
							// create block.node - Simulink (reference model) relationship
							JSONObject refRelation = new JSONObject();
							refRelation.put("id", "");
							refRelation.put("type", "relationship");
							refRelation.put("label", "CONTAINS_SIMULINK_MODEL");

							JSONObject refRelationProperties = new JSONObject();
							refRelationProperties.put("type", "Simulink Reference");

							JSONObject refRelationStart = new JSONObject();
							refRelationStart.put("id", reqElementIdentifier); // Source, Block							
							
							// Prepare Simulink model id from filePath
							String filePath = tag1.getValue().toString();
							Path path = Paths.get(filePath);
							String fileNameOnly = path.getFileName().toString();
							String fileNameWithoutExt = "";

							if (fileNameOnly.indexOf(".") > 0) {
								fileNameWithoutExt = fileNameOnly.substring(0, fileNameOnly.lastIndexOf("."));
								} else {
									fileNameWithoutExt = fileNameOnly;
								}
							
							JSONObject refRelationEnd = new JSONObject();
							refRelationEnd.put("id", fileNameWithoutExt); // Target; Simulink Model

							refRelation.put("properties", refRelationProperties);
							refRelation.put("start", refRelationStart);
							refRelation.put("end", refRelationEnd);

							allReqElementData = allReqElementData.concat(refRelation.toJSONString()).concat("\n");
							System.out.println("Simulink Ref Relation --> " + refRelation.toJSONString());
						}
					}
					
					IRPCollection relations = classItem.getRelations();

					for (int j = 1; j < relations.getCount() + 1; j++) {
						IRPRelation relation = (IRPRelation) relations.getItem(j);

//							System.out.println(relation.getOfClass().getName()+","+relation.getOfClass().toString() + "," + relation.getOtherClass().getName() + ","
//									+ relation.getOtherClass().toString()+ ","+relation.getRelationType());		
						// create dependency relationship
						JSONObject dependencyRel = new JSONObject();
						dependencyRel.put("id", "");
						dependencyRel.put("type", "relationship");
						// dependencyRel.put("label", "IS_PART_OF");
						String labelName = "HAS_" + relation.getRelationType().toUpperCase() + "_WITH";
						dependencyRel.put("label", labelName);

						JSONObject dependencyRelProperties = new JSONObject();
						dependencyRelProperties.put("type", relation.getRelationType());
						dependencyRelProperties.put("name", relation.getName());

						JSONObject dependencyRelStart = new JSONObject();
						dependencyRelStart.put("id", relation.getOfClass().toString()); // Source

						JSONObject dependencyRelEnd = new JSONObject();
						dependencyRelEnd.put("id", relation.getOtherClass().toString()); // Target

						dependencyRel.put("properties", dependencyRelProperties);
						dependencyRel.put("start", dependencyRelStart);
						dependencyRel.put("end", dependencyRelEnd);

						allReqElementData = allReqElementData.concat(dependencyRel.toJSONString()).concat("\n");
						System.out.println("Relation --> " + dependencyRel.toJSONString());
					}
					
//					//Ports, flows, links/connectors information OR IBD information
//					IRPCollection ports = classItem.getPorts();
//					//IRPCollection allNested = classItem.getNestedElementsRecursive();
//					IRPCollection flows = classItem.getFlows();
//					IRPCollection links = classItem.getLinks();
//					
//					//int count = allNested.getCount();
//					
//					/*
//					//* Not required at this moment
//					// Process Parts
//					for (int i = 1; i < count + 1; i++) {
//						IRPModelElement nested = (IRPModelElement) allNested.getItem(i);
//						if (nested.getMetaClass().equals("Object")) {
//							JSONObject reqElement = new JSONObject();
//							reqElement.put("id", nested.toString());
//							reqElement.put("type", "node"); //Lifeline
//
//							JSONArray reqElementlabelsArray = new JSONArray();
//							reqElementlabelsArray.add("Part");
//							reqElement.put("labels", reqElementlabelsArray);
//
//							JSONObject reqElementProperties = new JSONObject();
//							reqElementProperties.put("name", nested.getName());
//							reqElementProperties.put("type", nested.getUserDefinedMetaClass());
//							reqElementProperties.put("genericType", nested.getMetaClass());
//
//							reqElement.put("properties", reqElementProperties);
//
//							allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
//							System.out.println("Part Node ~~> " + reqElement.toJSONString());
//
//							// create part.node - req.diagramNode (model) relationship
//							JSONObject reqRelation = new JSONObject();
//							reqRelation.put("id", "");
//							reqRelation.put("type", "relationship");
//							reqRelation.put("label", "IS_PART_OF");
//
//							JSONObject reqRelationProperties = new JSONObject();
//							reqRelationProperties.put("type", nested.getMetaClass()); // State
//
//							JSONObject reqRelationStart = new JSONObject();
//							reqRelationStart.put("id", nested.toString()); // Source
//
//							JSONObject reqRelationEnd = new JSONObject();
//							reqRelationEnd.put("id", nested.getOwner().toString()); // Target
//
//							reqRelation.put("properties", reqRelationProperties);
//							reqRelation.put("start", reqRelationStart);
//							reqRelation.put("end", reqRelationEnd);
//
//							allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
//							System.out.println("Part Relation --> " + reqRelation.toJSONString());
//						}
//					}
//					*/
//					// Process ports
//					int amountOfPorts = ports.getCount();
//					for (int j = 1; j <= amountOfPorts; j++) {
//						IRPModelElement portElement1 = (IRPModelElement) ports.getItem(j);
//						if (portElement1.getMetaClass().equalsIgnoreCase("SysMLPort")){
//							IRPSysMLPort port = (IRPSysMLPort) ports.getItem(j);	
//							
//							JSONObject portElement = new JSONObject();
//							portElement.put("id", port.toString());
//							portElement.put("type", "node"); //
//
//							JSONArray portElementlabelsArray = new JSONArray();
//							portElementlabelsArray.add("Port");
//							portElement.put("labels", portElementlabelsArray);
//
//							JSONObject portElementProperties = new JSONObject();
//							portElementProperties.put("name", port.getName());
//							portElementProperties.put("type", port.getUserDefinedMetaClass());
//							portElementProperties.put("genericType", port.getMetaClass());
//
//							portElement.put("properties", portElementProperties);
//
//							allReqElementData = allReqElementData.concat(portElement.toJSONString()).concat("\n");
//							System.out.println("Port Node ~~> " + portElement.toJSONString());
//
//							// create port.node - port.block relationship
//							JSONObject portRelation = new JSONObject();
//							portRelation.put("id", "");
//							portRelation.put("type", "relationship");
//							portRelation.put("label", "IS_PORT_OF");
//
//							JSONObject portRelationProperties = new JSONObject();
//							portRelationProperties.put("type", port.getMetaClass()); // State
//
//							JSONObject portRelationStart = new JSONObject();
//							portRelationStart.put("id", port.toString()); // Source
//
//							JSONObject portRelationEnd = new JSONObject();
//							portRelationEnd.put("id", port.getOwner().toString()); // Target
//
//							portRelation.put("properties", portRelationProperties);
//							portRelation.put("start", portRelationStart);
//							portRelation.put("end", portRelationEnd);
//
//							allReqElementData = allReqElementData.concat(portRelation.toJSONString()).concat("\n");
//							System.out.println("Port Relation --> " + portRelation.toJSONString());							
////							
//						}else if(portElement1.getMetaClass().equalsIgnoreCase("Port")) {
//							IRPPort port = (IRPPort) ports.getItem(j);
//							
//							JSONObject portElement = new JSONObject();
//							portElement.put("id", port.toString());
//							portElement.put("type", "node"); //
//
//							JSONArray portElementlabelsArray = new JSONArray();
//							portElementlabelsArray.add("Port");
//							portElement.put("labels", portElementlabelsArray);
//
//							JSONObject portElementProperties = new JSONObject();
//							portElementProperties.put("name", port.getName());
//							portElementProperties.put("type", port.getUserDefinedMetaClass());
//							portElementProperties.put("genericType", port.getMetaClass());
//
//							portElement.put("properties", portElementProperties);
//
//							allReqElementData = allReqElementData.concat(portElement.toJSONString()).concat("\n");
//							System.out.println("Port Node ~~> " + portElement.toJSONString());
//
//							// create port.node - port.block relationship
//							JSONObject portRelation = new JSONObject();
//							portRelation.put("id", "");
//							portRelation.put("type", "relationship");
//							portRelation.put("label", "IS_PORT_OF");
//
//							JSONObject portRelationProperties = new JSONObject();
//							portRelationProperties.put("type", port.getMetaClass()); // State
//
//							JSONObject portRelationStart = new JSONObject();
//							portRelationStart.put("id", port.toString()); // Source
//
//							JSONObject portRelationEnd = new JSONObject();
//							portRelationEnd.put("id", port.getOwner().toString()); // Target
//
//							portRelation.put("properties", portRelationProperties);
//							portRelation.put("start", portRelationStart);
//							portRelation.put("end", portRelationEnd);
//							
//							allReqElementData = allReqElementData.concat(portRelation.toJSONString()).concat("\n");
//							System.out.println("Port Relation --> " + portRelation.toJSONString());
//						}						
//					}
//
//					int amountOfFlows = flows.getCount();
//					for (int j = 1; j <= amountOfFlows; j++) {
//						IRPFlow flow = (IRPFlow) flows.getItem(j);
//						IRPPort end1 = flow.getEnd1Port();
//						IRPPort end2 = flow.getEnd2Port();
//						
//						String flowName = flow.getName();
//						String source_element = "";
//						String target_element = "";
//						
//						if (flow.getDirection() == "toEnd2") {
//							source_element = end1.toString();
//							target_element = end2.toString();
//						} else {
//							source_element = end2.toString();
//							target_element = end1.toString();
//						}
//						// create flow relationship
//						JSONObject flowRelation = new JSONObject();
//						flowRelation.put("id", "");
//						flowRelation.put("type", "relationship");
//						flowRelation.put("label", "HAS_FLOWS_TO");
//
//						JSONObject flowRelationProperties = new JSONObject(); 
//						flowRelationProperties.put("name", flow.getName());
//						flowRelationProperties.put("type", flow.getMetaClass());
//
//						JSONObject flowRelationStart = new JSONObject();
//						flowRelationStart.put("id", source_element); // Source
//
//						JSONObject flowRelationEnd = new JSONObject();
//						flowRelationEnd.put("id", target_element); // Target
//
//						flowRelation.put("properties", flowRelationProperties);
//						flowRelation.put("start", flowRelationStart);
//						flowRelation.put("end", flowRelationEnd);
//
//						allReqElementData = allReqElementData.concat(flowRelation.toJSONString()).concat("\n");
//						System.out.println("Flow Relation --> " + flowRelation.toJSONString());
//					}
//					
//					//Links or connectors
//					int amountOflinks = links.getCount();
//					System.out.println("amountOflinks--------------->: "+amountOflinks);
//					for (int l = 1; l <= amountOflinks; l++) {
//						IRPLink link = (IRPLink) links.getItem(l);
//						
//						String linkName = link.getName();
//						String source_element = "";
//						String target_element = "";
//						
//						if(link.getFromSysMLPort() != null && link.getToSysMLPort() != null) {
//							source_element = link.getFromSysMLPort().toString();
//							target_element = link.getToSysMLPort().toString();
//						}else if (link.getFromPort() != null && link.getToPort() != null){
//							source_element = link.getFromElement().toString();
//							target_element = link.getToPort().toString();
//						}
////						
////						System.out.println("Links  ---> MetaClass: "+link.getMetaClass()+"<> Name: "+ link.getName() +"<> FullPath: "+ link.getFullPathName() + 
////								"<> Owner: "+ link.getOwner().getName() + " <> OwnerFullPath: "+ link.getOwner().getFullPathName() + 
////								"<>OwnerIdentifier: "+link.getOwner().toString()+" <>UserDefinedMetaClass: "+link.getUserDefinedMetaClass()+
////								"<>Identifier: "+link.toString() + "<>sourcePort: "+link.getFromSysMLPort() + "<>dstPort: "+link.getToSysMLPort()+
////								"<>link.getOwner().getMetaClass()"+link.getOwner().getMetaClass());
////						if (link.getOwner().getMetaClass().equalsIgnoreCase("Port")) {
////							source_element = link.getFromElement().getName();
////							target_element = link.getToPort().getName();
////						} else if (link.getOwner().getMetaClass().equalsIgnoreCase("SysMLPort")){
////							source_element = link.getFromSysMLPort().getName();
////							target_element = link.getToSysMLPort().getName();
////						}
//						
//						// create link/connection relationship
//						JSONObject linkRelation = new JSONObject();
//						linkRelation.put("id", "");
//						linkRelation.put("type", "relationship");
//						linkRelation.put("label", "HAS_LINKS_TO");
//
//						JSONObject linkRelationProperties = new JSONObject(); 
//						linkRelationProperties.put("name", linkName);
//						linkRelationProperties.put("type", link.getMetaClass());
//
//						JSONObject linkRelationStart = new JSONObject();
//						linkRelationStart.put("id", source_element); // Source
//
//						JSONObject linkRelationEnd = new JSONObject();
//						linkRelationEnd.put("id", target_element); // Target
//
//						linkRelation.put("properties", linkRelationProperties);
//						linkRelation.put("start", linkRelationStart);
//						linkRelation.put("end", linkRelationEnd);
//
//						allReqElementData = allReqElementData.concat(linkRelation.toJSONString()).concat("\n");
//						System.out.println("Link Relation --> " + linkRelation.toJSONString());
//					}
					
				}

			}
			if (allReqElementData != "") {
				try {
					Path filePath = Paths.get(parsedDataFilePath, dataFileName);
					FileWriter file = new FileWriter(filePath.toString());
					// file.write(jsonObject.toJSONString());
					file.write(allReqElementData);
					file.close();
					// createdFileCount = createdFileCount + 1;
					System.out.println("Requirements model JSON file created successfully!!!");
					System.out.println("File Path: " + filePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out.println(e);
				}
			}
		}
	}

	public ArrayList<IRPSequenceDiagram> getSeqDiagrams() {
		ArrayList<IRPPackage> packagesList = getPackages();

		ArrayList<IRPSequenceDiagram> sequenceDiagramList = new ArrayList<IRPSequenceDiagram>();
		for (int i = 0; i < packagesList.size(); i++) {
			// For each package find all occurrences of a sequence diagram
			IRPCollection diagrams = packagesList.get(i).getSequenceDiagrams();
			int amountOfDiagrams = diagrams.getCount();

			for (int j = 1; j <= amountOfDiagrams; j++) {
				// Add sequence diagram to the List
				sequenceDiagramList.add((IRPSequenceDiagram) diagrams.getItem(j));
			}
		}
		return sequenceDiagramList;
	}

	/*
	 * Prints all the elements of the state chart
	 */
	public void printElementsStatechart(IRPStatechart statechart) {

		IRPCollection test = statechart.getElementsInDiagram();
		int count = test.getCount();
		for (int i = 1; i <= count; i++) {
			IRPModelElement eachModelElement = (IRPModelElement) test.getItem(i);
			//System.out.println(trigger);
			System.out.println("Stm Element ---> MetaClass: "+eachModelElement.getMetaClass()+"<> Name: "+ eachModelElement.getName() +"<> FullPath: "+ eachModelElement.getFullPathName() + 
					"<> Owner: "+ eachModelElement.getOwner().getName() + " <> OwnerFullPath: "+ eachModelElement.getOwner().getFullPathName() + 
					"<>OwnerIdentifier: "+eachModelElement.getOwner().toString()+" <>UserDefinedMetaClass: "+eachModelElement.getUserDefinedMetaClass()+
					"<>Identifier: "+eachModelElement.toString());
		}

	}

	/*
	 * Prints all the elements of the activity diagram
	 */
	public void printElementsActivityDiag(ArrayList<IRPFlowchart> flowcharts) {
		for (IRPFlowchart flowchart : flowcharts) {
			IRPCollection test = flowchart.getElementsInDiagram();
			int count = test.getCount();
			for (int i = 1; i <= count; i++) {
				IRPModelElement trigger = (IRPModelElement) test.getItem(i);
				System.out.println(trigger);
			}
		}
	}

	/*
	 * Prints all the elements of the sequence diagram
	 */
	public void printElementsSeqDiagram(IRPSequenceDiagram mod) {
		
		System.out.println("Sequence Diagram: " + mod.getName());
		
		String packageName = "";
		if (!mod.getOwner().getName().isEmpty()) {
			packageName = mod.getOwner().getName();
		}

		// Each Model Element
		String allReqElementData = ""; // model level requirements data file
		String dataFileName = packageName.concat("_" + mod.getName()).concat("_SeqDiagram.json").replace(" ", "-"); // packageName_ModelName_

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
		labelsArray.add("SequenceDiagram");
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
		System.out.println("Seq Diagram Node >>> " + reqDiagElement.toJSONString());
		
		// create reqModel.node - reqModel.Parent (model) relationship
		JSONObject modelRelation = new JSONObject();
		modelRelation.put("id", "");
		modelRelation.put("type", "relationship");
		modelRelation.put("label", "CONTAINS_DIAGRAM");

		JSONObject modelRelationProperties = new JSONObject();
		modelRelationProperties.put("type", userDefinedMetaClass); //mod.getOwner().getUserDefinedMetaClass()

		JSONObject modelRelationStart = new JSONObject();
		modelRelationStart.put("id", ownerIdentifier); // Source, created a relationship with package or project

		JSONObject modelRelationEnd = new JSONObject();
		modelRelationEnd.put("id", elementIdentifier); // Target

		modelRelation.put("properties", modelRelationProperties);
		modelRelation.put("start", modelRelationStart);
		modelRelation.put("end", modelRelationEnd);

		allReqElementData = allReqElementData.concat(modelRelation.toJSONString()).concat("\n");
		System.out.println("Relation --> " + modelRelation.toJSONString());
		
//		IRPCollection test = mod.getElementsInDiagram();
//		int count = test.getCount();
//		for (int i = 1; i <= count; i++) {
//			IRPModelElement trigger = (IRPModelElement) test.getItem(i);
//			System.out.println(trigger);
//		}
		
		// Model Elements
		IRPCollection collection = mod.getElementsInDiagram();
		for (int k = 1; k <= collection.getCount(); k++) {
			IRPModelElement eachModelElement = (IRPModelElement) collection.getItem(k);
//								System.out.println("Model Element ---> MetaClass: "+eachModelElement.getMetaClass()+"<> Name: "+ eachModelElement.getName() +"<> FullPath: "+ eachModelElement.getFullPathName() + 
//										"<> Owner: "+ eachModelElement.getOwner().getName() + " <> OwnerFullPath: "+ eachModelElement.getOwner().getFullPathName() + 
//										"<>UserDefinedMetaClass: "+eachModelElement.getUserDefinedMetaClass());
			// System.out.println("ReqID: "+eachModelElement.getRequirementID()+"<> Spec: "+
			// eachModelElement.getSpecification()+"<> Desc: "+
			// eachModelElement.getDescription());

			// Or eachModelElement instanceof IRPRequirement
			if (eachModelElement.getMetaClass().equals("ClassifierRole")) {
				IRPClassifierRole  req = (IRPClassifierRole) eachModelElement;		

				// create requirement node
				String reqElementIdentifier = req.toString();
				String reqElementName = req.getName();
//									String desccription = req.getDescription();
				String reqElementUserDefinedMetaClass = req.getUserDefinedMetaClass();
				String reqElementMetaClass = req.getMetaClass();
				// String ownerIdentifier = req.getOwner().toString();
				String reqElementOwnerIdentifier = elementIdentifier;
//				String ownerElementName = req.getOwner().getName();

				JSONObject reqElement = new JSONObject();
				reqElement.put("id", reqElementIdentifier);
				reqElement.put("type", "node"); //Lifeline

				JSONArray reqElementlabelsArray = new JSONArray();
				reqElementlabelsArray.add(reqElementUserDefinedMetaClass); //Lifeline
				reqElement.put("labels", reqElementlabelsArray);

				JSONObject reqElementProperties = new JSONObject();
				reqElementProperties.put("name", reqElementName);
				reqElementProperties.put("type", reqElementUserDefinedMetaClass);
				reqElementProperties.put("genericType", reqElementMetaClass);
				// reqElementProperties.put("description", desccription);
//								reqElementProperties.put("requirementId", req.getRequirementID());// requirementId
//								reqElementProperties.put("specification", req.getSpecification());// requirementSpecification
//								reqElementProperties.put("description", req.getDescription());// requirementDesc

				reqElement.put("properties", reqElementProperties);

				allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
				System.out.println("Lifeline Node ~~> " + reqElement.toJSONString());

				// create req.node - req.diagramNode (model) relationship
				JSONObject reqRelation = new JSONObject();
				reqRelation.put("id", "");
				reqRelation.put("type", "relationship");
				reqRelation.put("label", "CONTAINS_SEQUENCE_ELEMENT"); // CONTAINS_ELEMENT

				JSONObject reqRelationProperties = new JSONObject();
				reqRelationProperties.put("type", reqElementUserDefinedMetaClass); // Lifeline

				JSONObject reqRelationStart = new JSONObject();
				reqRelationStart.put("id", elementIdentifier); // Source

				JSONObject reqRelationEnd = new JSONObject();
				reqRelationEnd.put("id", reqElementIdentifier); // Target

				reqRelation.put("properties", reqRelationProperties);
				reqRelation.put("start", reqRelationStart);
				reqRelation.put("end", reqRelationEnd);

				allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
				System.out.println("Relation --> " + reqRelation.toJSONString());

				IRPCollection reqDiagramElements = eachModelElement.getDependencies();
				System.out.println("Classifier Element Count = " + reqDiagramElements.getCount());
			}
			else if (eachModelElement.getMetaClass().equals("Message")) {
				IRPMessage  req = (IRPMessage) eachModelElement;		

				// create requirement node
				String reqElementIdentifier = req.toString();
				String reqElementName = req.getName();
//									String desccription = req.getDescription();
				String reqElementUserDefinedMetaClass = req.getUserDefinedMetaClass();
				String reqElementMetaClass = req.getMetaClass();
				String sourceIdentifier = req.getSource().toString();
				String targetIdentifier = req.getTarget().toString();
				String sequenceNumber = req.getSequenceNumber();
				String messageType = req.getMessageType();
				// String ownerIdentifier = req.getOwner().toString();
				//String reqElementOwnerIdentifier = modelDiagIdentifier;
//									String ownerElementName = req.getOwner().getName();

//				JSONObject reqElement = new JSONObject();
//				reqElement.put("id", reqElementIdentifier);
//				reqElement.put("type", "node");
//
//				JSONArray reqElementlabelsArray = new JSONArray();
//				reqElementlabelsArray.add("Block");
//				reqElement.put("labels", reqElementlabelsArray);
//
//				JSONObject reqElementProperties = new JSONObject();
//				reqElementProperties.put("name", reqElementName);
//				reqElementProperties.put("type", reqElementUserDefinedMetaClass);
//				reqElementProperties.put("genericType", reqElementMetaClass);
//				// reqElementProperties.put("description", desccription);
////								reqElementProperties.put("requirementId", req.getRequirementID());// requirementId
////								reqElementProperties.put("specification", req.getSpecification());// requirementSpecification
////								reqElementProperties.put("description", req.getDescription());// requirementDesc
//
//				reqElement.put("properties", reqElementProperties);
//
//				allReqElementData = allReqElementData.concat(reqElement.toJSONString()).concat("\n");
//				System.out.println("Node ~~> " + reqElement.toJSONString());

				// create req.node - req.diagramNode (model) relationship
				JSONObject reqRelation = new JSONObject();
				reqRelation.put("id", "");
				reqRelation.put("type", "relationship");
				reqRelation.put("label", "SEND_MESSAGE_TO");

				JSONObject reqRelationProperties = new JSONObject();
				reqRelationProperties.put("name", reqElementName); // Message name 
				reqRelationProperties.put("type", reqElementMetaClass);
				reqRelationProperties.put("messageType", messageType);
				reqRelationProperties.put("sequenceNumber", sequenceNumber.replace(".", ""));

				JSONObject reqRelationStart = new JSONObject();
				reqRelationStart.put("id", sourceIdentifier); // Source

				JSONObject reqRelationEnd = new JSONObject();
				reqRelationEnd.put("id", targetIdentifier); // Target

				reqRelation.put("properties", reqRelationProperties);
				reqRelation.put("start", reqRelationStart);
				reqRelation.put("end", reqRelationEnd);

				allReqElementData = allReqElementData.concat(reqRelation.toJSONString()).concat("\n");
				System.out.println("Relation --> " + reqRelation.toJSONString());
			}
		}
		
		if (allReqElementData != "") {
			try {
				Path filePath = Paths.get(parsedDataFilePath, dataFileName);
				FileWriter file = new FileWriter(filePath.toString());
				// file.write(jsonObject.toJSONString());
				file.write(allReqElementData);
				file.close();
				// createdFileCount = createdFileCount + 1;
				System.out.println("Seq Diagram JSON file created successfully!!!");
				System.out.println("File Path: " + filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println(e);
			}
		}
	}

	public void printClassRelations(ArrayList<IRPClass> classes) {
		for (IRPClass classItem : classes) {
			IRPCollection relations = classItem.getRelations();
			for (int j = 1; j < relations.getCount() + 1; j++) {
				System.out.println();
				IRPRelation relation = (IRPRelation) relations.getItem(j);
				System.out.println("Relation name: " + relation.getName());
				System.out.println("From Class: " + relation.getOfClass().getName());
				System.out.println("To Class: " + relation.getOtherClass().getName());
				System.out.println("Relation type: " + relation.getRelationType());
			}
		}
	}

	/*
	 * Sends a string over localhost through TCP, this can be received by anyone
	 * listening to the TCP port
	 */
	public void sendMessageToUnity(String message) throws UnknownHostException, IOException {
		String sentence;
		String modifiedSentence;

		Socket clientSocket = new Socket("localhost", 8888); // Create new socket on the localhost
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // Create output stream for
																								// outgoing data
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(message); // Send message to server in Bytes
		clientSocket.close();
	}

}
