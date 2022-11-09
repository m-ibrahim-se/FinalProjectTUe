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

public class ExtractModelInfo {

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

		IRPCollection modelElement = prj.getNestedElementsRecursive();

		System.out.println("ElementCount = " + modelElement.getCount());
		String allModelElement = "";
		for (int m = 1; m <= modelElement.getCount(); m++) {
			IRPModelElement element = (IRPModelElement) modelElement.getItem(m);
			JSONObject modelElementObj = new JSONObject();
			modelElementObj.put("Name", element.getName());
			modelElementObj.put("MetaClass", element.getMetaClass());
			if (element.getOwner() != null) {
				modelElementObj.put("OwnerOrParent", element.getOwner().getName());
				modelElementObj.put("OwnerOrParentIdentifier", element.getOwner().toString());
			} else {
				modelElementObj.put("OwnerOrParent", "");
				modelElementObj.put("OwnerOrParentIdentifier", "");
			}
			modelElementObj.put("Identifier", element.toString());
			modelElementObj.put("UserDefinedMetaClass", element.getUserDefinedMetaClass());
			allModelElement = allModelElement.concat(modelElementObj.toJSONString()).concat("\n");
		}
		if (allModelElement != "") {
			try {
				Path filePath = Paths.get(parsedDataFilePath, "ModelInfo.json");
				FileWriter file = new FileWriter(filePath.toString());
				// file.write(jsonObject.toJSONString());
				file.write(allModelElement);
				file.close();
				System.out.println("Package JSON file created successfully");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println(e);
			}
		}
	}

}
