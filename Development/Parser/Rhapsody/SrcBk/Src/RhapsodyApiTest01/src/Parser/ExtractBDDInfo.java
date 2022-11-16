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
import com.telelogic.rhapsody.core.IRPPackage;
import com.telelogic.rhapsody.core.IRPProject;
import com.telelogic.rhapsody.core.IRPRequirement;
import com.telelogic.rhapsody.core.IRPTransition;
import com.telelogic.rhapsody.core.RhapsodyAppServer;

public class ExtractBDDInfo {

	public static void main(String[] args) {
		System.out.println("Start BDD info Parser");

		// Data parsing info
		String parsedDataFilePath = "C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\ParsedDataFiles\\";

		IRPApplication application;
		application = RhapsodyAppServer.getActiveRhapsodyApplication();

		IRPProject project;
		// String modelFile =
		// "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\NewModels\\ADSimulation\\ADSimulation.rpyx";

		String modelFile = "C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\Models\\JuanSampleModel\\JuanSampleModel.rpyx";
		// String modelFile = "C:\\Users\\20204920\\OneDrive - TU
		// Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\Models\\JUAN_Original\\Juan_V1.rpyx";
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

				IRPCollection classes = ((IRPPackage) element).getClasses();
				int classCount = classes.getCount();
				System.out.println("Package Class Count = " + classCount);

				for (int j = 1; j <= classes.getCount(); j++) {
					IRPModelElement eachClassElement = (IRPModelElement) classes.getItem(j);

					// Get BDD Diagram
					System.out.println("Model Element ---> MetaClass: " + eachClassElement.getMetaClass() + "<> Name: "
							+ eachClassElement.getName() + "<> FullPath: " + eachClassElement.getFullPathName()
							+ "<> Owner: " + eachClassElement.getOwner().getName() + " <> OwnerFullPath: "
							+ eachClassElement.getOwner().getFullPathName() + "<>UserDefinedMetaClass: "
							+ eachClassElement.getUserDefinedMetaClass());
					
					System.out.println("Block Name: "+eachClassElement.getName());
					
					IRPCollection relations = eachClassElement.getAnnotations();
					for (int k = 1; k <= relations.getCount(); k++) {
						IRPModelElement eachRelationElement = (IRPModelElement) relations.getItem(j);
						System.out.println("Model Relation ---> MetaClass: " + eachRelationElement.getMetaClass() + "<> Name: "
								+ eachRelationElement.getName() + "<> FullPath: " + eachRelationElement.getFullPathName()
								+ "<> Owner: " + eachRelationElement.getOwner().getName() + " <> OwnerFullPath: "
								+ eachRelationElement.getOwner().getFullPathName() + "<>UserDefinedMetaClass: "
								+ eachRelationElement.getUserDefinedMetaClass());
					}

					
				}
				
			}
				packageCount = packageCount + 1;			
		}
//		System.out.println("Package Count = " + packageCount + ", Model Count = " + reqModelCount
//				+ " AND created requirements Diagram file count = " + createdFileCount);
//		System.out.println("End Package info Parser");
	}
}
