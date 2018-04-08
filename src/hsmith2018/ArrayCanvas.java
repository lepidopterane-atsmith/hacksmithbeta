
package hsmith2018;

import java.util.*;
import java.awt.*;
import javax.swing.*;  
/**
 *  Implement a graphical canvas that displays an array
 *  elements demonstrated by rectangles
 *
 *  @author  Ha Cao (modded by Sarah Abowitz)
 *  @version CSC 112, May 1st 2017
 */

public class ArrayCanvas extends JComponent {	
	private static final long serialVersionUID = 1L;
	/** The array */
	protected int[] arr;
	/** The annotations that show the indices and contents of the elements */
	protected ArrayList<String> annotations;
	/** The list of colors of all the nodes */
	protected ArrayList<Color> colors;	

	/** Constructor */
	public ArrayCanvas() {
		arr = new int[10];
		annotations = new ArrayList<String>();
		colors = new ArrayList<Color>();
		setMinimumSize(new Dimension(500, 700));
		setPreferredSize(new Dimension(500, 700));
	}
    
	/**
     * A method to get the color of a particular element given its index
     * 
     * @param i The index of the element
     * @return The color of the element
     */
    public Color getColor(int i) {
		return colors.get(i);
	}
    
    /**
     * Change color of a particular element given its index
     * 
     * @param i The index of the element
     * @param c The new color
     */
    public void setElementColor(int i, Color c) {
    	colors.set(i, c);
    }
    
	/**
	 *  A method to draw the rectangles that demonstate the elements
	 *
	 *  @param g The graphics object to draw with
	 */
	public void paintComponent(Graphics g) {
		Color c = Color.GREEN;
		for (int i = 0; i < arr.length; i++){
			g.setColor(c);
			g.fillRect(22, 26+(60*i), 400, 50);
			g.setColor(Color.BLACK);
			g.drawString("a["+i+"] = "+arr[i], 30, 50+(60*i));
			if (annotations.size() > i && annotations.size() > 0){
				g.drawString(annotations.get(i), 150, 50+(60*i));
			}
			if (arr.length < annotations.size()){
				g.drawString(annotations.get(annotations.size()-1), 150, 50+(60*arr.length));
			}
		}	
	}
}