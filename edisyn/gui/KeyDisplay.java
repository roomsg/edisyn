/***
    Copyright 2017 by Sean Luke
    Licensed under the Apache License version 2.0
*/

package edisyn.gui;

import edisyn.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;


/**
   A Keyboard.
   @author Sean Luke
*/



public class KeyDisplay extends NumericalComponent
    {
	public void redoTitle(int state)
		{
    	if (state < minKey || state > maxKey)
    		label.setText(title);
    	else
    		label.setText(title + ":   " + state + "   " + getNote(state));
		}

    public void update(String key, Model model) 
    	{
    	redoTitle(getState());
    	repaint(); 
    	}

    public static final String[] KEYS = new String[] { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

	public String getNote(int val)
		{
		return KEYS[val % 12] + " " + (val / 12);
		}
		
    public Dimension getPreferredSize() { return new Dimension(whiteKeyVals.length * Style.KEYBOARD_DEFAULT_WHITE_KEY_WIDTH, Style.KEYBOARD_DEFAULT_WHITE_KEY_HEIGHT); }
    public Dimension getMinimumSize() { return getPreferredSize(); }
    public Dimension getMaximumSize() { return getPreferredSize(); }
        
    int minKey;
    int maxKey;
    int transpose;
    JLabel label;
    String title;
    Color staticColor;

    Rectangle2D blackKeys[];
    int blackKeyVals[];
   	Rectangle2D whiteKeys[];
   	int whiteKeyVals[];
   	
   	int dynamicKey;
   	 
    // Is the mouse pressed?  This is part of a mechanism for dealing with
    // a stupidity in Java: if you PRESS in a widget, it'll be told. But if
    // you then drag elsewhere and RELEASE, the widget is never told.
    boolean mouseDown;
        
    
    public int OCTAVES_BELOW_ZERO_YAMAHA = -2;
    public int OCTAVES_BELOW_ZERO_SPN = -1;
    public int OCTAVES_BELOW_ZERO_MIDI = 0;
   	int octavesBelowZero = OCTAVES_BELOW_ZERO_YAMAHA;		// Yamaha
   	
    
    public void setOctavesBelowZero(int val) { octavesBelowZero = val; }
    public int getOctavesBelowZero() { return octavesBelowZero; }
    	
    void mouseReleased(MouseEvent e)
        {                       
        if (mouseDown)
            {
            setState(dynamicKey + transpose);
            dynamicKey = -1;
            repaint();
            mouseDown = false;
            }
        }
        
    public KeyDisplay(String title, Synth synth, String key, Color staticColor, int minKey, int maxKey, int transpose)
        {
        super(synth, key);
        this.title = title;
        this.transpose = transpose;
        this.minKey = minKey;
        this.maxKey = maxKey;
        this.staticColor = staticColor;
        synth.getModel().setMetricMin(key, minKey + transpose);
        synth.getModel().setMetricMax(key, maxKey + transpose);
        
        setLayout(new BorderLayout());
        label = new JLabel(title);
        label.setFont(Style.SMALL_FONT);
        label.setBackground(Style.TRANSPARENT);
        label.setForeground(Style.TEXT_COLOR);
        add(label, BorderLayout.NORTH);    
        add(new KeyDisplay.Inner(), BorderLayout.CENTER); 
        setBackground(Style.TRANSPARENT);

        // count the keys
        
        int whiteCount = 0;
        int blackCount = 0;
        for(int i = minKey; i <= maxKey; i++)
        	{
        	if (isWhiteKey(i))
        		whiteCount++;
        	}
        blackCount = (maxKey - minKey + 1) - whiteCount;
        
        blackKeys = new Rectangle2D.Double[blackCount];
        blackKeyVals = new int[blackCount];
        whiteKeys = new Rectangle2D.Double[whiteCount];
        whiteKeyVals = new int[whiteCount];
                
        int b = 0;
        int w = 0;
        
        double keyWidth = 1.0 / whiteCount;
        
        // build the keys
        for(int i = minKey; i <= maxKey; i++)
        	{
        	if (isWhiteKey(i))
        		{
        		whiteKeys[w] = new Rectangle2D.Double(w * keyWidth, 0, keyWidth, 1.0);
        		whiteKeyVals[w] = i;
        		w++;
        		}
        	else
        		{
        		if (i == minKey)  // black key is the very first one, this shouldn't be but we have to handle it
        			{
        			blackKeys[b] = new Rectangle2D.Double(0, 0, 0.5 * (0.75 * keyWidth), 0.6);
        			}
        		else
        			{
	        		double x = whiteKeys[w - 1].getX() + whiteKeys[w - 1].getWidth();
					if (i == maxKey)  // black key is the very last one, this shouldn't be but we have to handle it
        				{
        				blackKeys[b] = new Rectangle2D.Double(x - 0.5 * (0.75 * keyWidth), 0, 0.5 * (0.75 * keyWidth), 0.6);
        				}
        			else
        				{
        				blackKeys[b] = new Rectangle2D.Double(x - 0.5 * (0.75 * keyWidth), 0, 0.75 * keyWidth, 0.6);
        				}
	        		}
        		blackKeyVals[b] = i;
        		b++;
        		}
        	}
        
        setState(getState());
        repaint();
        }
    
    int findKey(int x, int y)
    	{
    	if (innerBounds == null) return -1;
    	
    	for(int i = 0; i < blackKeys.length; i++)
    		{
    		if (blackKeys[i].contains(x / (double)innerBounds.getWidth(), y / (double)innerBounds.getHeight()))
    			{
    			return blackKeyVals[i];
    			}
    		}

    	for(int i = 0; i < whiteKeys.length; i++)
    		{
    		if (whiteKeys[i].contains(x / (double)innerBounds.getWidth(), y / (double)innerBounds.getHeight()))
    			{
    			return whiteKeyVals[i];
    			}
    		}
    		
    	return -1;
    	}
    
    
    boolean isWhiteKey(int key)
    	{
    	key = key % 12;
    	return (key == 0 || key == 2 || key == 4 || key == 5 || key == 7 || key == 9 || key == 11);
    	}


	Rectangle innerBounds;

	class Inner extends JComponent
		{
		public Inner()
			{
        	setBackground(Style.TRANSPARENT);
			
			addMouseListener(new MouseAdapter()
				{
				public void mousePressed(MouseEvent e)
					{
					dynamicKey = findKey(e.getX(), e.getY());
					redoTitle(dynamicKey);
					repaint();
					}
						
				public void mouseReleased(MouseEvent e)
					{
					if (dynamicKey != -1)
						{
						setState(dynamicKey + transpose);
						dynamicKey = -1;
						repaint();
						}
					}
				});
						
			addMouseMotionListener(new MouseMotionAdapter()
				{
				public void mouseDragged(MouseEvent e)
					{
					if (innerBounds.contains(e.getX(), e.getY()))
						{
						dynamicKey = findKey(e.getX(), e.getY());
						redoTitle(dynamicKey);
						repaint();
						}
					else
						{
						dynamicKey = -1;
						redoTitle(getState());
						repaint();
						}				
					}
				});

			// This gunk fixes a BAD MISFEATURE in Java: mouseReleased isn't sent to the
			// same component that received mouseClicked.  What the ... ? Asinine.
			// So we create a global event listener which checks for mouseReleased and
			// calls our own private function.  EVERYONE is going to do this.
			long eventMask = AWTEvent.MOUSE_EVENT_MASK;
				
			Toolkit.getDefaultToolkit().addAWTEventListener( new AWTEventListener()
				{
				public void eventDispatched(AWTEvent e)
					{
					if (e instanceof MouseEvent && e.getID() == MouseEvent.MOUSE_RELEASED)
						{
						mouseReleased((MouseEvent)e);
						}
					}
				}, eventMask);
			}


		public void paintComponent(Graphics g)
			{
			Style.prepareGraphics(g);
				
			Graphics2D graphics = (Graphics2D) g;
				
			Rectangle rect = getBounds();
			rect.x = 0;
			rect.y = 0;
			
			innerBounds = rect;
		
			// draw the white notes
			graphics.setPaint(Style.KEYBOARD_WHITE_COLOR);
			graphics.fill(rect);

			int selectedKey = getState() - transpose;
			
			if (isWhiteKey(selectedKey))	// otherwise don't bother with this
				{
				graphics.setPaint(staticColor);
				for(int i = 0; i < whiteKeyVals.length; i++)
					{
					if (whiteKeyVals[i] == selectedKey)
						{
						int xpos = (int)(rect.width * whiteKeys[i].getX());
						int xwidth = (int)(rect.width * whiteKeys[i].getWidth());
						int yheight = (int)(rect.height * whiteKeys[i].getHeight());
						Rectangle2D.Double r = new Rectangle2D.Double(xpos, 0, xwidth, yheight);
						graphics.fill(r);
						break;
						}
					}
				}        

			if (isWhiteKey(dynamicKey))	// otherwise don't bother with this
				{
				graphics.setPaint(Style.KEYBOARD_DYNAMIC_COLOR);
				for(int i = 0; i < whiteKeyVals.length; i++)
					{
					if (whiteKeyVals[i] == dynamicKey)
						{
						int xpos = (int)(rect.width * whiteKeys[i].getX());
						int xwidth = (int)(rect.width * whiteKeys[i].getWidth());
						int yheight = (int)(rect.height * whiteKeys[i].getHeight());
						Rectangle2D.Double r = new Rectangle2D.Double(xpos, 0, xwidth, yheight);
						graphics.fill(r);
						break;
						}
					}
				}        
		
			// draw the cracks between the white notes
			graphics.setPaint(Style.KEYBOARD_BLACK_COLOR);
            graphics.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, null, 0.0f));
			for(int i = 1; i < whiteKeys.length; i++)
				{
				double xpos = (rect.width * whiteKeys[i].getX());
				Line2D.Double line = new Line2D.Double(xpos, 0, xpos, rect.height);
				graphics.draw(line);
				}
		
			// draw the black notes
			for(int i = 0; i < blackKeys.length; i++)
				{
				double xpos = (rect.width * blackKeys[i].getX());
				double xwidth = (rect.width * blackKeys[i].getWidth());
				double yheight = (rect.height * blackKeys[i].getHeight());
				Rectangle2D.Double r = new Rectangle2D.Double(xpos, 0, xwidth, yheight);
				if (blackKeyVals[i] == dynamicKey)
					{
					graphics.setColor(Style.KEYBOARD_DYNAMIC_COLOR);
					graphics.fill(r);
					graphics.setColor(Style.KEYBOARD_BLACK_COLOR);
					graphics.draw(r);
					}
				else if (blackKeyVals[i] == selectedKey)
					{
					graphics.setColor(staticColor);
					graphics.fill(r);
					graphics.setColor(Style.KEYBOARD_BLACK_COLOR);
					graphics.draw(r);
					}
				else
					{
					graphics.fill(r);
					}
				}
			
			// draw a border of white
			graphics.setPaint(Style.KEYBOARD_WHITE_COLOR);
			graphics.draw(rect);
			}
		}
    }



