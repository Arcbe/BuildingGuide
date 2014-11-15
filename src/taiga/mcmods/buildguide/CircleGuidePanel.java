/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.mcmods.buildguide;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import net.minecraftforge.common.MinecraftForge;

public class CircleGuidePanel extends GuidePanel {
  
  public static final int GUI_SPACING = 5;
  
  public static enum Mode {
    xaxis,
    yaxis,
    zaxis
  }

  public CircleGuidePanel() {
    mode = Mode.xaxis;
    
    sizelab = new JLabel("Radius:");
    
    sizeent = new JFormattedTextField(NumberFormat.getIntegerInstance());
    
    modebutton = new JButton("X-axis");
    remove = new JButton("Delete");
    redraw = new JButton("Redraw");
    visible = new JCheckBox("Visible",true);
    marker = new BlockMarker(Color.GREEN, true);
    
    cbutton = new JButton("Change Color");
    color = new JLabel();
    cbutton.setAlignmentX(LEFT_ALIGNMENT);
    color.setBackground(Color.GREEN);
    
    setupPanel();
    
    MinecraftForge.EVENT_BUS.register(marker);
  }
  
  private void setupPanel() { 
    //create the field specific for the circles
    Box orig = Box.createVerticalBox();
    Box temp = Box.createHorizontalBox();
    
    temp.add(sizelab);
    temp.add(Box.createHorizontalStrut(GUI_SPACING));
    temp.add(sizeent);
    
    orig.add(temp);
    orig.add(Box.createVerticalStrut(GUI_SPACING));
    temp = Box.createHorizontalBox();
    
    temp.add(modebutton);
    temp.add(Box.createHorizontalStrut(GUI_SPACING));
    temp.add(redraw);
    
    orig.add(temp);
    orig.add(Box.createVerticalStrut(GUI_SPACING));
    temp = Box.createHorizontalBox();
    
    temp.add(visible);
    temp.add(Box.createHorizontalStrut(GUI_SPACING));
    temp.add(remove);
    
    orig.add(temp);
    orig.setAlignmentX(LEFT_ALIGNMENT);
    add(orig);
    
    temp = Box.createVerticalBox();
    temp.add(color);
    temp.add(cbutton);
    temp.setAlignmentX(LEFT_ALIGNMENT);
    add(temp);
    
    modebutton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        buttonPressed();
      }
    });
    
    remove.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        remove();
      }
    });
    
    visible.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        marker.setVisible(visible.isSelected());
      }
    });
    
    redraw.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        switch(mode) {
          case xaxis: genXCirc(); return;
          case yaxis: genYCirc(); return;
          case zaxis: genZCirc();
        }
      }
    });
    
    cbutton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        Color c = JColorChooser.showDialog(CircleGuidePanel.this, "Marker Color", color.getBackground());
        color.setBackground(c);
        marker.setColor(c);
      }
    });
  }
  
  private void buttonPressed() {
    switch(mode) {
      case xaxis:
        modebutton.setText("Y-axis");
        mode = Mode.yaxis;
        genYCirc();
        break;
      case yaxis:
        modebutton.setText("Z-axis");
        mode = Mode.zaxis;
        genZCirc();
        break;
      case zaxis:
        modebutton.setText("X-axis");
        mode = Mode.xaxis;
        genXCirc();
        break;
    }
  }
  
  private void remove() {
    marker.clear();
    MinecraftForge.EVENT_BUS.unregister(marker);
    
    Container par = getParent();
    
    if(par == null) return;
    
    par.remove(this);
    par.revalidate();
  }
  
  private void genXCirc() {
    Set<Point> pts = genCircle();
    Collection<Point3D> output = new ArrayList<Point3D>(pts.size());
    
    int x = getOriginX();
    int y = getOriginY();
    int z = getOriginZ();
    
    for(Point pt : pts) {
      output.add(new Point3D(x + .5, y + pt.y + .5, z + pt.x + .5));
    }
    
    marker.setBlocks(output);
  }
  
  private void genYCirc() {
    Set<Point> pts = genCircle();
    Collection<Point3D> output = new ArrayList<Point3D>(pts.size());
    
    int x = getOriginX();
    int y = getOriginY();
    int z = getOriginZ();
    
    for(Point pt : pts) {
      output.add(new Point3D(x + pt.x + .5, y + .5, z + pt.y + .5));
    }
    
    marker.setBlocks(output);
  }
  
  private void genZCirc() {
    Set<Point> pts = genCircle();
    Collection<Point3D> output = new ArrayList<Point3D>(pts.size());
    
    int x = getOriginX();
    int y = getOriginY();
    int z = getOriginZ();
    
    for(Point pt : pts) {
      output.add(new Point3D(x + pt.x + .5, y + pt.y + .5, z + .5));
    }
    
    marker.setBlocks(output);
  }
  
  private Set<Point> genCircle() {
    float size = ((Number) sizeent.getValue()).floatValue();
    float xhelper = (float) Math.cos(1 / size);
    float yhelper = (float) Math.sin(1 / size);
    
    Set<Point> output = new HashSet<>((int) (2 * Math.PI * size) + 1);
    
    float x = 0;
    float y = 1;
    for(float angle = 0; angle < 2 * Math.PI; angle += 1 / size) {
      Point p = new Point((int) (x * size), (int) (y * size));
      output.add(p);
      
      float t = x;
      x = x * xhelper - y * yhelper;
      y = t * yhelper + y * xhelper;
    }
    
    return output;
  }
  
  private final JLabel sizelab;
   
  private final JFormattedTextField sizeent;
  
  private final JButton modebutton;
  private final JButton remove;
  private final JButton redraw;
  private final JLabel color;
  private final JButton cbutton;
  private final JCheckBox visible;
  private final BlockMarker marker;
  private Mode mode;

  private static final String locprefix = CircleGuidePanel.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
