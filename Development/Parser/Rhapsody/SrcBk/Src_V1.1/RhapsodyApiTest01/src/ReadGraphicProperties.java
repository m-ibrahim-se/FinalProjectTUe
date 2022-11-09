import java.util.List;

import com.telelogic.rhapsody.core.*;

public class ReadGraphicProperties {

    public static void main(String[] args) {

        IRPApplication app = RhapsodyAppServer.getActiveRhapsodyApplication();
        IRPProject prj;
		String modelFile = "C:\\ProgramData\\IBM\\Rhapsody\\9.0\\RhapsodyModels\\ADSimulation\\ADSimulation.rpyx";
		app.openProject(modelFile);
		prj = app.activeProject();
		
        IRPModelElement el = app.getSelectedElement();
        System.out.println("I am here....");
        System.out.println("I am here...."+el.getInterfaceName());
        if (el instanceof IRPDiagram) {
            IRPDiagram diagram = (IRPDiagram)el;
            System.out.println(diagram.getMetaClass());
            List<IRPGraphElement> diagramElements = diagram.getGraphicalElements().toList();

            for (IRPGraphElement graphElement : diagramElements) {


                List<IRPGraphicalProperty> graphicalPropeties = null;

                try { /// in case the diagram graphic has no corresponding model element

                    System.out.println(graphElement.getModelObject().getMetaClass());
                    System.out.println("==========");


                    graphicalPropeties = graphElement.getAllGraphicalProperties().toList();

                    for (IRPGraphicalProperty graphProp : graphicalPropeties) {

                        System.out.println(graphProp.getKey() + "------>" + graphProp.getValue());
                    }
                }
                catch (Exception e) {	// diagram graphic has no corresponding model element.
                    // graphic element may contain a "Type" graphical property
                    System.out.println("<Unknown> Model Element");
                    System.out.println("<><>>><><>><>>><>");

                    IRPCollection gps = graphElement.getAllGraphicalProperties();
                    for (int i=0; i < gps.getCount(); i++) {
                        IRPGraphicalProperty gp = (IRPGraphicalProperty) gps.getItem(i);
                        if (gp != null) // Null graphical properties may exist
                            System.out.println(gp.getKey() + "======>" + gp.getValue());
                        }

                    }
                }
            }
        }
    }