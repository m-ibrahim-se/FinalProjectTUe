import com.telelogic.rhapsody.core.IRPApplication;
import com.telelogic.rhapsody.core.IRPClass;
import com.telelogic.rhapsody.core.IRPObjectModelDiagram;
import com.telelogic.rhapsody.core.IRPPackage;
import com.telelogic.rhapsody.core.IRPProject;
import com.telelogic.rhapsody.core.IRPStereotype;
import com.telelogic.rhapsody.core.RhapsodyAppServer;

public class AddBddTest {

	public static void main(String[] args) {
		IRPApplication app;
		app = RhapsodyAppServer.getActiveRhapsodyApplication();
		
		IRPProject prj;
		String modelFile = "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\SysMLProject01\\SysMLProject01.rpyx";
		app.openProject(modelFile);
		prj = app.activeProject();
		
		// check we have SysML profile added to project 
		if (app.activeProject().findNestedElementRecursive("SysML", "Profile") == null) {
			app.writeToOutputWindow("Log", "Please add the SysML profile...aborting.");
			System.exit(1);
		}
		else {
			System.out.println("Profile = "+prj.findNestedElementRecursive("SysML", "Profile"));
		}
		
		// find these SysML stereotypes now, as we need them later
		IRPStereotype blockST = (IRPStereotype)app.activeProject().findNestedElementRecursive("Block", "Stereotype");
		IRPStereotype bddST = (IRPStereotype)app.activeProject().findNestedElementRecursive("Block Definition Diagram", "Stereotype");
		
		// add a package
		IRPPackage testPkg = (IRPPackage)app.activeProject().addNewAggr("Package", "TestPkg");
		
		// add a class, set as SysML block
		IRPClass testBlock = (IRPClass)testPkg.addNewAggr("Class", "TestBlock");
		testBlock.setStereotype(blockST);
		
		// add an OMD to package, set as SysML bdd
		IRPObjectModelDiagram pkgBdd = (IRPObjectModelDiagram)testPkg.addNewAggr("ObjectModelDiagram", "pkgBdd");
		pkgBdd.setStereotype(bddST);
		
		// add an OMD to class/block, set as SysML bdd
		IRPObjectModelDiagram blockBdd = (IRPObjectModelDiagram)testBlock.addNewAggr("ObjectModelDiagram", "blockBdd");
		blockBdd.setStereotype(bddST);

	}

}