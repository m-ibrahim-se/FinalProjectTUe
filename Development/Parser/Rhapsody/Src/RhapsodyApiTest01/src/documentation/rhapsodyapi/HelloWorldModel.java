package documentation.rhapsodyapi;
import java.lang.*;
import com.telelogic.rhapsody.core.*;

public class HelloWorldModel {
	//static IRPApplication app = RhapsodyAppServer.getActiveRhapsodyApplication();
	// create main function
	public static void main(String[] args) {
		openAndCloseProject();
//		if(app != null){
//			System.out.println("app");
//			openAndCloseProject();
//		}
//		else {
//			System.out.println("No app");
//		}
			
		}
		// create second function that the main function calls
		private static void openAndCloseProject() {
			IRPApplication app = RhapsodyAppServer.getActiveRhapsodyApplication();
			System.out.println("Start");
			app.createNewProject("C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\API3", "Open_Close");
			System.out.println(app);
			IRPProject prj = app.openProject("C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\API3\\Open_Close.rpyx");
			System.out.println(prj);
			IRPPackage vehiclePackage = prj.addPackage("Vehicles");
			vehiclePackage.addClass("Car");
			prj.save();
			prj.close();
			System.out.println("End");
		}

}
