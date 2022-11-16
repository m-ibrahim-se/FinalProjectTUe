import java.util.Iterator;

import com.telelogic.rhapsody.core.IRPApplication;
import com.telelogic.rhapsody.core.IRPDependency;
import com.telelogic.rhapsody.core.IRPSwimlane;
import com.telelogic.rhapsody.core.IRPState;
import com.telelogic.rhapsody.core.IRPTag;
import com.telelogic.rhapsody.core.IRPTransition;
import com.telelogic.rhapsody.core.IRPStereotype;
import com.telelogic.rhapsody.core.IRPModelElement;
import com.telelogic.rhapsody.core.IRPProject;
import com.telelogic.rhapsody.core.RhapsodyAppServer;

public class ActivityDiagramInfo {

 public static void main (String[] args) {

  IRPApplication myApp = RhapsodyAppServer.getActiveRhapsodyApplication();
  IRPProject myproj = myApp.openProject("C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\ADSimulation\\ADSimulation.rpyx");
  IRPModelElement myEle = myApp.getSelectedElement();
  System.out.print("Activity Diagram --> ");
  System.out.println(myEle.getName());
  
  Iterator sterIter = myEle.getStereotypes().toList().iterator();
  while(sterIter.hasNext()){
   Object contEleObj = sterIter.next();
   IRPStereotype stereotype = (IRPStereotype) contEleObj;
   System.out.print("Stereotype --> ");
   System.out.println(stereotype.getName());
  }

  Iterator contEleIter = myEle.getNestedElementsRecursive().toList().iterator();
  while(contEleIter.hasNext()){
   Object contEleObj = contEleIter.next();
   if (contEleObj instanceof IRPTag) {
    IRPTag tag = (IRPTag) contEleObj;
    System.out.print("Tag --> ");
    System.out.print(tag.getName());
    System.out.print("(Value: ");
    System.out.print(tag.getValue());
    System.out.println(")");
   }

   if (contEleObj instanceof IRPSwimlane) {
    IRPSwimlane flow = (IRPSwimlane) contEleObj;
    System.out.print("Swimlane --> ");
    System.out.println(flow.getDisplayName());
   }

   if (contEleObj instanceof IRPTransition) {
    IRPTransition flow = (IRPTransition) contEleObj;
    System.out.print("Flow --> ");
    System.out.println(flow.getDisplayName()+ " --> "+flow.getName());
   }

   if (contEleObj instanceof IRPState) {
    IRPState action = (IRPState) contEleObj;
    if(action.getName().contains("ROOT"))
     continue;
    System.out.print("Action --> ");
    System.out.println(action.getName());
   }
   
   if (contEleObj instanceof IRPDependency) {
	   IRPDependency dependency = (IRPDependency) contEleObj;
	    System.out.print("Dependency --> ");
	    System.out.println(dependency.getName() + " Dependent: "+dependency.getDependent() + " DependsOn: "+dependency.getDependsOn());
	   }

  }
 }
}