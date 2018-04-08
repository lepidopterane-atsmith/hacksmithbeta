package hsmith2018;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.Queue;
import java.io.*;
import javax.swing.*;

/**
 *  A panel to display array
 *  @author  Ha Cao (modded for Array support by Sarah Abowitz)
 *  @version Apr 7th, 2018
 */

public class ArrayPanel {	
	/** The array to be displayed */
	private static ArrayCanvas canvas;

	/** Label for the input mode instructions */
	private JLabel instr;

	/** The input mode */
	private InputMode mode = InputMode.CREATE_ARRAY;

	/** Remember point where last mouse-down event occurred */
	private Point pointUnderMouse;

	/** Graph display fields */
	private JPanel panel1;
	private GraphMouseListener gml;

	/** Control field */
	private JPanel panel2;

	/** Constructor */
	public ArrayPanel(JComponent panel) {
		// Initialize the array display and control fields
		canvas = new ArrayCanvas();
		panel1 = new JPanel();
		gml = new GraphMouseListener();
		instr = new JLabel("");
		panel2 = new JPanel();
		createComponents(panel);
	}
	
	/** Puts content in the GUI window */
	public void createComponents(JComponent panel) {
		// Array display
		panel.setLayout(new FlowLayout());
		panel1.setLayout(new BorderLayout());
		canvas.addMouseListener(gml);
		canvas.addMouseMotionListener(gml);
		panel1.add(canvas);
		panel1.add(instr, BorderLayout.NORTH);
		
		panel.add(panel1);

		// Controls
		panel2.setLayout(new GridLayout(5,2));	
		
		JButton createArrayButton = new JButton("Create a new array");
		panel2.add(createArrayButton);
		createArrayButton.addActionListener(new createArrayListener());

		JButton changeValueButton = new JButton("Change value of an element");
		panel2.add(changeValueButton);
		changeValueButton.addActionListener(new changeValueListener());

		JButton accessButton = new JButton("Access an element by index");
		panel2.add(accessButton);
		accessButton.addActionListener(new accessListener());
		
		JButton findButton = new JButton("Find an element in the array");
		panel2.add(findButton);
		findButton.addActionListener(new findListener());
		
		panel.add(panel2);	
	}

	public boolean zoneClicked(int x1, int x2, int y1, int y2, Point pt){
		int xPrime = (int) pt.getX();
		int yPrime = (int) pt.getY();
		boolean horiz = false, vert = false;
		if (x1 < xPrime && x2 > xPrime){
			horiz = true;
		}
		if (y1 < yPrime && y2 > yPrime){
			vert = true;
		}
		if (horiz && vert){
			return true;
		}
		return false;
	}
	
	/** Constants for recording the input mode */
	enum InputMode {
		CREATE_ARRAY, CHANGE_VALUE, ACCESS, FIND
	}

	/** Listener for createArray button */
	private class createArrayListener implements ActionListener {
		/** Event handler for createArray button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.CREATE_ARRAY;
			addPtInstr = "Click above, between, or below slots to add an element.";
			instr.setText(addPtInstr);
			//defaultVar(canvas);
		}
	}

	/** Listener for RmvNode button */ 
	private class RmvNodeListener implements ActionListener {
		/** Event handler for RmvNode button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.RMV_NODES;
			rmvPtInstr = "Click an array element to remove it.";
			instr.setText(rmvPtInstr);
			//defaultVar(canvas);
		}
	}
	
	/** Listener for AddEdge button */
	private class AddEdgeListener implements ActionListener {
		/** Event handler for AddEdge button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.ADD_EDGES;
			addEdgeInstr = "Click an array element to access it.";
			instr.setText(addEdgeInstr);
			//defaultVar(canvas);
		}
	}

	/** Listener for RmvEdge button */
	private class RmvEdgeListener implements ActionListener {
		/** Event handler for RmvEdge button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.RMV_EDGES;
			rmvEdgeInstr = "Click an array element to search for it.";
			instr.setText(rmvEdgeInstr);
			//defaultVar(canvas);
		}
	}

	/** Listener for BFT button */
	private class BFTListener implements ActionListener {
		/** Event handler for BFT button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.BFT;
			instr.setText("Click a node to broad-traverse the graph.");
			//defaultVar(canvas);
		}
	}


	/** Mouse listener for ArrayCanvas element */
	private class GraphMouseListener extends MouseAdapter
	implements MouseMotionListener {

		/** Responds to click event depending on mode */
		public void mouseClicked(MouseEvent e) {
			switch (mode) {
			case ADD_NODES:
				// If the click is not on top of an existing node, create a new node and add it to the canvas.
				// Otherwise, emit a beep, as shown below:
			if(arrayMode && canvas.getArr().length < 10){
				JFrame addQuery = new JFrame("Add an entry");
				String insertPlace = JOptionPane.showInputDialog(addQuery, "Type the index of where you want to add your element.");
				int index = Integer.valueOf(insertPlace);
				String number = JOptionPane.showInputDialog(addQuery, "Type the integer value of your element.");
				int num = Integer.valueOf(number);
				instr.setText("Copying array into new array with new item.");
				if(index < canvas.getArr().length+1){
					canvas.arrAddition(index, num);
				} else {
					instr.setText("Addition out of bounds.");
				}
			}
			else{
						Toolkit.getDefaultToolkit().beep();
						JFrame frame = new JFrame("");
						// Warning
						JOptionPane.showMessageDialog(frame,
								"Failed click on empty space. Start adding nodes again.",
								"Click Warning",
								JOptionPane.WARNING_MESSAGE);
					
				}
				canvas.repaint();
				break;
			case RMV_NODES:
				// If the click is on top of an existing node, remove it from the canvas's graph.
				// Otherwise, emit a beep.
				Point rmvClick = new Point((int) e.getX(), (int) e.getY());
				if (arrayMode && canvas.getArr().length > 0){
					int arrLen = canvas.getArr().length;
					int itemClicked = 10; // if itemClicked stays at 10, no hitbox clicked
					for (int i = 0; i < arrLen; i++){
						int y1 = 26+(60*i);
						int y2 = 76+(60*i);
						if (zoneClicked(22,422,y1,y2,rmvClick)) {itemClicked = i;}
					}
					
					if (itemClicked < 10){
						instr.setText("Copying array into new array without item.");
						canvas.arrRemoval(itemClicked);
					}
					
				}
				canvas.repaint();
				break;		
			case ADD_EDGES:
				// If the click is not on top of an existing node, emit a beep, as shown below.
				// Otherwise, check how many nodes have been clicked;
				// If only 1, save the node (which is supposed to be the head).
				// If already 2, create an edge between the two nodes. 
				Point accClick = new Point((int) e.getX(), (int) e.getY());
				if(arrayMode){
					int arrLen = canvas.getArr().length;
					int itemClicked = 10; // if itemClicked stays at 10, no hitbox clicked
					for (int i = 0; i < arrLen; i++){
						int y1 = 26+(60*i);
						int y2 = 76+(60*i);
						if (zoneClicked(22, 422, y1, y2, accClick)) {itemClicked = i;}
					}
					instr.setText("The item, if accessed, is in cyan.");
					if(itemClicked < canvas.getArr().length){
						canvas.arrAccess(itemClicked);
					} else {
						instr.setText("Called element out of bounds.");
					}
				} else{
				
				
				canvas.repaint();
				}
				break ;		
			case RMV_EDGES:
				// TODO Traversal Coming Soon!!!!!!
				canvas.repaint();
				break;	
			case BFT:
				// TODO Editing Coming Soon!!!!!
				
				canvas.repaint();
				break;		
				
			case OUT_PUT:
				break;
			}
		}

		/** Records point under mouse-down event in anticipation of possible drag */
		public void mousePressed(MouseEvent e) {
			// Record point under mouse, if any
			;
		}

		/** Responds to mouse-up event */
		public void mouseReleased(MouseEvent e) {
			// Clear record of point under mouse, if any
			pointUnderMouse = null;
		}

		/** Responds to mouse drag event */
		public void mouseDragged(MouseEvent e) {
			// If mode allows node motion, and there is a point under the mouse, 
			// then change its coordinates to the current mouse coordinates & update display
			if ((mode == InputMode.ADD_NODES) && (pointUnderMouse!=null)) {
				pointUnderMouse.setLocation((int) e.getX(), (int) e.getY());
				// Loop through the edge list of this particular node to update the distances
				
			canvas.repaint();
		}
	}

	// Empty but necessary to comply with MouseMotionListener interface
	public void mouseMoved(MouseEvent e) {}
	}
}	