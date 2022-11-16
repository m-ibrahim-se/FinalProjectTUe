package Parser;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONObject;

import com.telelogic.rhapsody.core.IRPApplication;
import com.telelogic.rhapsody.core.IRPCollection;
import com.telelogic.rhapsody.core.IRPModelElement;
import com.telelogic.rhapsody.core.IRPProject;
import com.telelogic.rhapsody.core.RhapsodyAppServer;

public class ExtractPackageInfo {

	public static void main(String[] args) {
		System.out.println("Start Package info Parser");

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
		String modelFile = "C:\\Users\\20204920\\OneDrive - TU Eindhoven\\PDEng\\Projects\\Final_Project\\TUeProject\\Development\\Parser\\Rhapsody\\Models\\JuanSampleModel\\JuanSampleModel.rpyx";
		app.openProject(modelFile);
		prj = app.activeProject();

		IRPCollection packages = prj.getPackages();
		System.out.println("Package Count = " + packages.getCount());
		// System.out.println("Name||MetaClass||Owner/Parent||Identifier");
		int packageCount = 1;
		String packageName = "";
		int createdFileCount = 0;

		for (int i0 = 1; i0 <= packages.getCount(); i0++) {
			IRPModelElement element = (IRPModelElement) packages.getItem(i0);
			System.out.println("MetaClass: " + element.getMetaClass() + "<><>>><><><>" + element.getName());
			if (element.getMetaClass().equals("Package")) {
				packageName = element.getName();

				IRPCollection packageElements = element.getNestedElementsRecursive();
				System.out.println("Package Element Count = " + packageElements.getCount());

				String allPkgElementData = "";

				String allBlockData = "";

				for (int i1 = 1; i1 <= packageElements.getCount(); i1++) {
					IRPModelElement eachPkgElement = (IRPModelElement) packageElements.getItem(i1);
					JSONObject jsonObjElement = new JSONObject();

					String ownerElementName = eachPkgElement.getOwner().getName();
					String metaClass = eachPkgElement.getMetaClass();
					String elementName = eachPkgElement.getName();
					String elementIdentifier = eachPkgElement.toString();
					String userDefinedMetaClass = eachPkgElement.getUserDefinedMetaClass();
					jsonObjElement.put("Name", elementName);
					jsonObjElement.put("MetaClass", metaClass);
					jsonObjElement.put("OwnerOrParent", ownerElementName);
					jsonObjElement.put("OwnerOrParentIdentifier", eachPkgElement.getOwner().toString());
					jsonObjElement.put("Identifier", elementIdentifier);
					jsonObjElement.put("UserDefinedMetaClass", userDefinedMetaClass);
					allPkgElementData = allPkgElementData.concat(jsonObjElement.toJSONString()).concat("\n");
					System.out.println(jsonObjElement.toJSONString());
				}
				try {
					Path filePath = Paths.get(parsedDataFilePath, packageName + ".json");
					FileWriter file = new FileWriter(filePath.toString());
					// file.write(jsonObject.toJSONString());
					file.write(allPkgElementData);
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
		System.out.println("Package Count = " + packageCount + " AND created file count = " + createdFileCount);
		System.out.println("End Package info Parser");
	}

}
