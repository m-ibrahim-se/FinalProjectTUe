package Parser;

import java.io.IOException;
import java.util.ArrayList;

import com.telelogic.rhapsody.core.IRPClass;
import com.telelogic.rhapsody.core.IRPFlowchart;
import com.telelogic.rhapsody.core.IRPObjectModelDiagram;
import com.telelogic.rhapsody.core.IRPPackage;
import com.telelogic.rhapsody.core.IRPSequenceDiagram;
import com.telelogic.rhapsody.core.IRPStatechart;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;

public class MainClass {
	static PullGraph project;

	public static void main(String[] args) throws IOException {

		project = new PullGraph(); // Create a new object PullGraph

		// If the model is not SysML create an error
		if (project.prj.findNestedElementRecursive("SysML", "Profile") == null) {
			project.app.writeToOutputWindow("Log", "Please add the SysML profile...aborting");
			System.exit(1);
		}

		JFrame frame = new JFrame("Rhapsody Model Parser GUI App");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 500);
		JPanel panel = new JPanel();
		frame.add(panel);

		JButton pullModelPackages = new JButton("Parse Model and Packages info");
		panel.add(pullModelPackages);
		pullModelPackages.addActionListener(new modelPackageActionListener());

		JButton pullClasses = new JButton("Parse BDD");
		panel.add(pullClasses);
		pullClasses.addActionListener(new classActionListener());

//		JButton pullClassRelations = new JButton("Pull Class Relations");
//		panel.add(pullClassRelations);
//		pullClassRelations.addActionListener(new classRelationActionListener());

		JButton pullSeqDiagrams = new JButton("Parse Sequence Diagrams"); // FIX THAT IT ONLY PULLS 1 DIAGRAM WHEN
																			// AVAILABLE
		panel.add(pullSeqDiagrams);
		pullSeqDiagrams.addActionListener(new seqDiagramActionListener());

		JButton pullUseCaseDiagram = new JButton("Parse UseCase Diagrams");
		panel.add(pullUseCaseDiagram);
		pullUseCaseDiagram.addActionListener(new useCaseDiagramActionListener());

		JButton pullStateChart = new JButton("Parse State Charts Diagram");
		panel.add(pullStateChart);
		pullStateChart.addActionListener(new stateChartActionListener());

		JButton pullActivityDiag = new JButton("Parse Activity Diagrams");
		panel.add(pullActivityDiag);
		pullActivityDiag.addActionListener(new activityDiagramActionListener());

		// JButton printAll = new JButton("Print Everything");
		// panel.add(printAll);
		// printAll.addActionListener(new printAllActionListener());

		/*
		 * JButton writeToFile = new JButton("Write Everything to File");
		 * panel.add(writeToFile); writeToFile.addActionListener(new
		 * toFileActionListener());
		 */

//		JButton writeBDDToFile = new JButton("Write BDD to File");
//		panel.add(writeBDDToFile);
//		writeBDDToFile.addActionListener(new printBDDActionListener());

//		JButton writeBlocksToFile = new JButton("Write Blocks to File");
//		panel.add(writeBlocksToFile);
//		writeBlocksToFile.addActionListener(new printBlocksActionListener());

		JButton writeIBDToFile = new JButton("Parse IBD to File");
		panel.add(writeIBDToFile);
		writeIBDToFile.addActionListener(new IBDtoFileActionListener());

		JButton writeReqToFile = new JButton("Parse Requirements Diagram");
		panel.add(writeReqToFile);
		writeReqToFile.addActionListener(new reqToFileActionListener());
	}

	static class reqToFileActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Process Requirements Diagram");
			ArrayList<IRPPackage> packageList = project.getPackages();
			for (IRPPackage pack : packageList) {
				if (pack != null) {
					try {
						// if(pack.getName().equals("REQUIREMENTS")) {
						project.printRequirements(pack);
						// }
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

//	static class classActionListener implements ActionListener{
//		public void actionPerformed (ActionEvent e) {
//			System.out.println("Process BDD Classes");
//			ArrayList<IRPClass> classList = project.getClasses();
//			for(IRPClass classItem : classList) {
//				System.out.println("class: " + classItem.getDisplayName());
//			}
//		}
//	}

	static class modelPackageActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Process Model and Packages");
			try {
				project.printModelPackages();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	static class classActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Process BDD Diagram");
			ArrayList<IRPObjectModelDiagram> modelDiagramList = project.getModelDiagrams();
			System.out.println("Model Count: " + modelDiagramList.size());
			for (IRPObjectModelDiagram modelDiagram : modelDiagramList) {
				if (modelDiagram != null) {
					try {
						// if(pack.getName().equals("REQUIREMENTS")) {
						System.out.println("Model Name: " + modelDiagram.getName());
						project.printBDD(modelDiagram);
						// }
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

	static class classRelationActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ArrayList<IRPClass> classList = project.getClasses();
			project.printClassRelations(classList);
		}
	}

	static class seqDiagramActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println();
			ArrayList<IRPSequenceDiagram> seqDiagrams = project.getSeqDiagrams();

			for (IRPSequenceDiagram seq : seqDiagrams) {
				// System.out.println("Sequence Diagram: " + seq.getName());
				project.printElementsSeqDiagram(seq);
			}
		}
	}

	static class useCaseDiagramActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// ArrayList<IRPUseCaseDiagram> useCaseDiagrams =
			// project.getUseCaseDiagrams(project.getPackages());
			project.getUseCaseDiagrams(project.getPackages());
//			for(IRPUseCaseDiagram useCaseDiag : useCaseDiagrams) {
//				System.out.println();
//				System.out.println(useCaseDiag.getName() + ":");
//				
//				
//				IRPCollection elements = useCaseDiag.getElementsInDiagram();
//				for(int i = 1; i <= elements.getCount(); i++) {
//					System.out.println(elements.getItem(i));
//				}
//			}
		}
	}

	static class printBlocksActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				project.writeBlocksToFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

//	static class stateChartActionListener implements ActionListener{
//		public void actionPerformed(ActionEvent e) {
//			System.out.println();
//			ArrayList<IRPClass> classList = project.getClasses();
//			for(IRPClass classItem : classList) {	// For each class list all State Charts
//				if(project.getStatecharts(classItem) != null) {
//					System.out.println(project.getStatecharts(classItem).getName() + ":");
//					project.printElementsStatechart(project.getStatecharts(classItem));
//				}
//			}
//		}
//	}

//	static class stateChartActionListener implements ActionListener{
//		public void actionPerformed(ActionEvent e) {
//			System.out.println();
//			ArrayList<IRPClass> classList = project.getClasses();
//			for(IRPClass classItem : classList) {	// For each class list all State Charts
//				project.getStatecharts(classItem);
//			}
//		}
//	}
	static class stateChartActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Process State Charts Diagram");
			ArrayList<IRPStatechart> modelDiagramList = project.getStateChartDiagrams();
			System.out.println("Model Count: " + modelDiagramList.size());
			for (IRPStatechart modelDiagram : modelDiagramList) {
				if (modelDiagram != null) {
					try {
						// if(pack.getName().equals("REQUIREMENTS")) {
						System.out.println("Model Name: " + modelDiagram.getName());
						// project.printBDD(modelDiagram);
						project.printStateChart(modelDiagram);
						// }
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

	static class printAllActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println();
			ArrayList<IRPPackage> packageList = project.getPackages();
			for (IRPPackage pack : packageList) {
				if (pack != null) {
					project.printAll(pack);
				}
			}
		}
	}

	static class printBDDActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				project.writeBDDToFile2();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	static class toFileActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println();
			ArrayList<IRPPackage> packageList = project.getPackages();
			for (IRPPackage pack : packageList) {
				if (pack != null) {
					project.writeAllToFile(pack);
				}
			}
		}
	}

	static class IBDtoFileActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println();
			try {
				project.printIBD();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

//	static class activityDiagramActionListener implements ActionListener{
//		public void actionPerformed(ActionEvent e) {
//			System.out.println();
//			project.printElementsActivityDiag(project.getActivityDiagrams());
//		}
//	}
	static class activityDiagramActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Process Activity Diagram");
			ArrayList<IRPFlowchart> modelDiagramList = project.getActivityDiagrams();
			System.out.println("Model Count: " + modelDiagramList.size());
			for (IRPStatechart modelDiagram : modelDiagramList) {
				if (modelDiagram != null) {
					try {
						// if(pack.getName().equals("REQUIREMENTS")) {
						System.out.println("Model Name: " + modelDiagram.getName());
						// project.printBDD(modelDiagram);
						project.printActivityDiagram(modelDiagram);
						// }
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
