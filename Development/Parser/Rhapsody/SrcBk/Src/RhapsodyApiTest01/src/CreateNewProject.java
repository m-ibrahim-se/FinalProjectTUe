import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telelogic.rhapsody.core.IRPApplication;
import com.telelogic.rhapsody.core.IRPClass;
import com.telelogic.rhapsody.core.IRPCollection;
import com.telelogic.rhapsody.core.IRPDependency;
import com.telelogic.rhapsody.core.IRPEvent;
import com.telelogic.rhapsody.core.IRPModelElement;
import com.telelogic.rhapsody.core.IRPPackage;
import com.telelogic.rhapsody.core.IRPProject;
import com.telelogic.rhapsody.core.RhapsodyAppServer;

public class CreateNewProject {

	public static void main(String[] args) {		
		
		IRPApplication app = RhapsodyAppServer.getActiveRhapsodyApplication();
		//create a new project
		//app.createNewProject("C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\API", "Hello_World");
		// open the project
		IRPProject openedProj = app.openProject("C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\API\\Hello_World.rpyx");

		//find existing package and add class to it
		IRPProject prj = app.activeProject();
		//add a package
		IRPPackage vehiclePackage = prj.addPackage("GreeterPackage");
		//IRPPackage packageToUse = (IRPPackage)prj.findNestedElement("GreeterPackage", "Package");
		IRPPackage packageToUse = (IRPPackage)prj.findNestedElement("GreeterPackage", "Package");
		IRPClass writerClass = packageToUse.addClass("TextWriter");
		
		String currentPropertyValue =
				writerClass.getPropertyValue("CPP_CG.Class.GenerateDestructor");
		System.out.print("At the beginning, the property has its default value which is\n " + "\t" + currentPropertyValue + "\n\n");
		
		String newPropertyValue = "False";
		writerClass.setPropertyValue("CPP_CG.Class.GenerateDestructor",
				newPropertyValue);
		currentPropertyValue =
				writerClass.getPropertyValue("CPP_CG.Class.GenerateDestructor");
		System.out.print("After our call to the set function, the value of the property is\n " + "\t" + currentPropertyValue + "\n\n");
		
		currentPropertyValue =
				writerClass.getPropertyValueExplicit("CPP_CG.Class.GenerateDestructor");
				System.out.print("Because we modified the default value, the method getPropertyValueExplicit returns the value we provided, which is\n " +
				"\t"
				+ currentPropertyValue + "\nIf we had not modified the default value, it would have thrown an exception.");
		
//		//Create a new package and add a class to it
//		IRPPackage pkg = prj.addPackage("Vehicles");
//		IRPClass composerClass = (IRPClass)pkg.addNewAggr("Class","TextComposer");
//		
//		Map<String,String> theMap = new HashMap();
//		IRPCollection allColl = prj.getNestedElementsRecursive();
//		
//		for (int i = 1; i <= allColl.getCount(); i++) {
//			IRPModelElement element = (IRPModelElement)allColl.getItem(i);
//			theMap.put(element.getMetaClass(), element.getInterfaceName());
//			System.out.println("theMap: "+element.getMetaClass() + "\t" + element.getInterfaceName());
//		}
//		
//		Set<String> mapKeySet = theMap.keySet();
//		for (String strMetaClass : mapKeySet) {
//			System.out.println("mapKeySet: "+strMetaClass + "\t" + theMap.get(strMetaClass));
//		}
//		
//		System.out.println("End of Program");
		
//		prj.save();
		
//		System.out.println(prj.findNestedElementRecursive("Vehicles", "Package"));
//		prj.close();
		
		
		}
}
