
package hsmith2018;

import java.util.*;
import java.util.Timer;
import java.awt.*;
import javax.swing.*;  
/**
 *  Implement a graphical canvas that displays an array
 *  elements demonstrated by rectangles
 *
 *  @author  Ha Cao (modded by Sarah Abowitz)
 *  @version CSC 112, May 1st 2017
 */

// why wasn't access working? you deleted the thing that was supposed to make it cyan

public class ArrayCanvas extends JComponent {	
	private static final long serialVersionUID = 1L;
	/** The array */
	protected int[] arr;
	/** The annotations that show the indices and contents of the elements */
	protected ArrayList<String> annotations;
	boolean addingToArr = false;
	private int access = 10;
	
	/** Constructor */
	public ArrayCanvas() {
		arr = new int[10];
		annotations = new ArrayList<String>();
		setMinimumSize(new Dimension(500, 700));
		setPreferredSize(new Dimension(500, 700));
	}
	
	public void setArr(int[] array){
    	for(int i=0; i<array.length; i++){
    		arr[i] = array[i];
    	}
    }
    
    public int[] getArr(){
    	return arr;
    }
    
    public void arrSearch(int match){
    	// while this thing isn't found or we still have array in front of  us
    	ArrayCanvas.this.repaint();
    	int j = 0;
    	Timer timer = new Timer();
    	long delay = 2500;
    	boolean found = false;
    	System.err.println("Stuff?" + Arrays.toString(arr));
    	for (j = 0; j <= arr.length; j++){
    		final int i = j;
    		timer.schedule(new TimerTask(){
    			@Override
    			public void run(){
    				try {
	    				if (i > arr.length){
	    					annotations.add("");
	    				} else if (arr[i] == match){
	    					annotations.add("Query found!");
	    					// break;
	    				} else {
	    					annotations.add("Still searching...");
	    				}
	    				ArrayCanvas.this.repaint();
    				} catch (Throwable t) {
    					t.printStackTrace(System.err);
    				}
    			}
    		}, delay*(j+1));  
    		System.out.println(Arrays.toString(arr));
    		ArrayCanvas.this.repaint();
    		if (arr[j] == match){
    			break;
    		}
    	}
    	annotations.clear();
    	System.out.println(Arrays.toString(arr));
    }
    
    public void arrAccess(int index){
    	access = index;
    	ArrayCanvas.this.repaint();
    }
    
    public void arrAddition(int index,int num){
    	addingToArr = true;
    	Timer timer = new Timer();
    	int bound = arr.length;
    	long delay = 1000;
    	int j;
    	for (j = 0; j <= arr.length; j++){
    		final int i = j;
    		timer.schedule(new TimerTask(){
    			@Override 
    			public void run(){
    				if (i > index){
    	    			annotations.add("array["+i+"] = "+arr[i-1]);
    	    		} else if (i < index){
    	    			annotations.add("Remains the same");
    	    		} else {
    	    			annotations.add("array["+i+"] = "+num);
    	    		}
    				ArrayCanvas.this.repaint();
    			}
    			}, (2500*j));
    		
    	}
    	final int k = j+1;
    	timer.schedule(new TimerTask(){
    		@Override
    		public void run(){
    		try{
    				annotations.add("array["+k+"] = "+arr[-1]);
    		} catch (ArrayIndexOutOfBoundsException a){
    			a.printStackTrace();
    		}
    		ArrayCanvas.this.repaint();
    		
    		int[] temp = new int[bound+1];
        	for(int i = 0; i < bound+1; i++){
        		if (i> index){
        			temp[i]=arr[i-1];
        		} else if (i == index){
        			temp[i]=num; 
        		} else {
        			temp[i]=arr[i];
        		}
        	}
        	
        	arr = new int[bound+1];
        	
        	for(int i=0; i<arr.length; i++){
        		arr[i] = temp[i];
        	}
        	
        	annotations.clear();
        	ArrayCanvas.this.repaint();
        	
        	}}, 2500*bound);
    }
    
    public void arrRemoval(int index){
    	addingToArr=false;
    	Timer timer = new Timer();
    	int bound = arr.length;
    	long delay = 1000;
    	for (int j = 0; j < arr.length; j++){
    		final int i = j;
    		timer.schedule(new TimerTask(){
    			@Override 
    			public void run(){
    				if (i >= index && i < arr.length-1){
    	    			annotations.add("array["+i+"] = "+arr[i+1]);
    	    		} else if (i < arr.length-1){
    	    			annotations.add("Remains the same");
    	    		} else {
    	    			annotations.add("This slot does not exist in the new array");
    	    		}
    				ArrayCanvas.this.repaint();
    			}
    			}, (2500*j));
    		
    	}
    	
    	timer.schedule(new TimerTask(){
    		@Override
    		public void run(){int[] temp = new int[bound-1];
        	for(int i = 0; i < bound-1; i++){
        		if (i>= index && i < bound-1){
        			temp[i]=arr[i+1];
        		} else {
        			temp[i]=arr[i];
        		}
        	}
        	
        	arr = new int[bound-1];
        	
        	for(int i=0; i<arr.length; i++){
        		arr[i] = temp[i];
        	}
        	
        	annotations.clear();
        	ArrayCanvas.this.repaint();
        	
        	}}, 2500*bound);
    }
    
	/**
	 *  Paint a red circle 20 pixels in diameter at each point to represent a node,
	 *  a blue line to represent an edge, and a string label to represent the data of a node
	 *
	 *  @param g The graphics object to draw with
	 */
	public void paintComponent(Graphics g) {
			Color c = Color.GREEN;
			for (int i=0; i < arr.length; i++){
				if (i == access){
					c = Color.CYAN;
				} else {
					c = Color.GREEN;
				}
				// System.out.println("i: "+i+" access: "+access);
				g.setColor(c);
				g.fillRect(22, 26+(60*i), 400, 50);
				g.setColor(Color.BLACK);
				g.drawString("array["+i+"] = "+arr[i], 30, 50+(60*i));
				if (annotations.size()>i && annotations.size()>0){
					g.drawString(annotations.get(i), 150, 50+(60*i));
					
				}
				if (addingToArr && arr.length < annotations.size()){
					g.drawString(annotations.get(annotations.size()-1), 150, 50+(60*arr.length));
				}
			}
			
			access = 10;
		
	}
}