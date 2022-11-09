//package documentation.rhapsodyapi;
import java.util.List;
import com.telelogic.rhapsody.core.*;
public class CollectionHandler {
	static IRPApplication app = RhapsodyAppServer.getActiveRhapsodyApplication();	
	// main method
	public static void main(String[] args) {		
		if(app != null) {
			System.out.println("app");
			doCollectionTricks();
		}
		else {
			System.out.println("No app");
		}
	}
	// this method demonstrates IRPCollection's methods
	private static void doCollectionTricks() {
		// create and open project
		app.createNewProject("C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\API4", "Collection_Tricks");
		IRPProject prj = app.openProject("C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\API4\\Collection_Tricks.rpyx");
		System.out.println("Prj: "+prj);
		
		// create packages and classes
		IRPPackage vehiclePackage = prj.addPackage("Vehicles");
		IRPClass carClass = vehiclePackage.addClass("Car");
		IRPClass jeepClass = vehiclePackage.addClass("Jeep");
		IRPClass convertibleClass = vehiclePackage.addClass("Convertible");
		IRPClass busClass = vehiclePackage.addClass("Bus");
		IRPClass truckClass = vehiclePackage.addClass("Truck");
		IRPPackage airVehiclePackage = prj.addPackage("Air_Vehicles");
		airVehiclePackage.addClass("Helicopter");
		
		// add inheritance relationship
		convertibleClass.addGeneralization(carClass);
		prj.save();
		
		// create collection of all classes in the Vehicles package and then display contents of collection
		IRPCollection classCollection = vehiclePackage.getClasses();
		List currentContentsOfCollection = classCollection.toList();
		int numberOfItemsInList = currentContentsOfCollection.size();
		IRPClass tempClass = null;
		
		System.out.println("The collection currently contains the following " + classCollection.getCount() + " items:");
		// this block uses the get method of List so index starts at 0; note that for the get method of IRPCollection
		// (used later on), the index starts at 1
		
		for(int i = 0; i < numberOfItemsInList ; i++) {
			tempClass = (IRPClass)(currentContentsOfCollection.get(i));
			System.out.println(" " + tempClass.getDisplayName());
		}
		
		// now we'll empty out the collection
		classCollection.empty();
		System.out.println("After calling the empty method, the collection now contains " + classCollection.getCount() + " items.");
		// note that addItem increases the size by one each time - to add multiple elements, it's preferable to
		// call setSize and then use setModelElement to place elements in an open spot
		classCollection.addItem(carClass);
		classCollection.addItem(jeepClass);
		System.out.println("After adding classes, the collection now contains the following " + classCollection.getCount() + " items:"
		+ "\n " +
		((IRPClass)classCollection.getItem(1)).getDisplayName() + "\n "
		+
		((IRPClass)classCollection.getItem(2)).getDisplayName());
		
		// now extend size of collection and add additional classes
		classCollection.setSize(5);
		// important: note that index count begins at one
		classCollection.setModelElement(3, busClass);
		classCollection.setModelElement(4, convertibleClass);
		classCollection.setModelElement(5, truckClass);
		numberOfItemsInList = classCollection.getCount();
		System.out.println("After adding more classes, the collection now contains the following " + classCollection.getCount() + " items:");
		
		for(int i = 1; i < numberOfItemsInList+1 ; i++) {
			tempClass = (IRPClass)(classCollection.getItem(i));
			System.out.println(" " + tempClass.getDisplayName() + " at position " + (i));
		}
		// this section creates a new class diagram and populates it. The populateDiagram method takes 3 parameters,
		// the first two being collections: a collection of model elements and a collection of strings
		IRPDiagram classDiagramToCreate = vehiclePackage.addObjectModelDiagram("Classes in Vehicles package");
		IRPCollection classesToAddToDiagram = vehiclePackage.getClasses();
		IRPCollection typesOfRelationsToShow = app.createNewCollection();
		typesOfRelationsToShow.setSize(2);
		typesOfRelationsToShow.setString(1, "Inheritance");
		typesOfRelationsToShow.setString(2, "Dependency");
		classDiagramToCreate.populateDiagram(classesToAddToDiagram, typesOfRelationsToShow, "fromto");
	} //end of method doCollectionTricks
}
			