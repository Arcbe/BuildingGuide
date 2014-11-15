/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.mcmods.buildguide;

import cpw.mods.fml.client.FMLClientHandler;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import net.minecraftforge.common.MinecraftForge;

public class GuidePanel extends Box {
  
  public static final int GUI_SPACING = 5;

  public GuidePanel() {
    super(BoxLayout.LINE_AXIS);
    
    xlab = new JLabel("X:");
    ylab = new JLabel("Y:");
    zlab = new JLabel("Z:");
    
    xent = new JFormattedTextField(NumberFormat.getIntegerInstance());
    yent = new JFormattedTextField(NumberFormat.getIntegerInstance());
    zent = new JFormattedTextField(NumberFormat.getIntegerInstance());
    
    Dimension size = new Dimension(100, 0);
    xent.setMinimumSize(size);
    yent.setMinimumSize(size);
    zent.setMinimumSize(size);
    
    grabcoord = new JButton("Player Position");
    grabcoord.setFocusable(false);
    grabcoord.setAlignmentX(CENTER_ALIGNMENT);
    grabcoord.setDefaultCapable(false);
    
    setupOrigin();
    
    grabcoord.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        int x = (int) FMLClientHandler.instance().getClient().h.s;
        int y = (int) FMLClientHandler.instance().getClient().h.t;
        int z = (int) FMLClientHandler.instance().getClient().h.u;
        
        xent.setValue(x);
        yent.setValue(y);
        zent.setValue(z);
      }
    });
  }
  
  protected int getOriginX() {
    return ((Number)xent.getValue()).intValue();
  }
  
  protected int getOriginY() {
    return ((Number)yent.getValue()).intValue();
  }
  
  protected int getOriginZ() {
    return ((Number)zent.getValue()).intValue();
  }
  
  private void setupOrigin() {
    Box left = Box.createVerticalBox();
    Box right = Box.createVerticalBox();
    
    left.add(xlab);
    left.add(Box.createVerticalStrut(GUI_SPACING));
    right.add(xent);
    right.add(Box.createVerticalStrut(GUI_SPACING));
    
    left.add(ylab);
    left.add(Box.createVerticalStrut(GUI_SPACING));
    right.add(yent);
    right.add(Box.createVerticalStrut(GUI_SPACING));
    
    left.add(zlab);
    left.add(Box.createVerticalStrut(GUI_SPACING));
    right.add(zent);
    right.add(Box.createVerticalStrut(GUI_SPACING));
    
    Box orig = Box.createHorizontalBox();
    orig.add(left);
    orig.add(Box.createHorizontalStrut(GUI_SPACING));
    orig.add(right);
    
    Box panel = Box.createVerticalBox();
    panel.add(orig);
    panel.add(grabcoord);
    panel.setBorder(new TitledBorder("Origin"));
    
    add(panel);
  }
  
  private final JLabel xlab;
  private final JLabel ylab;
  private final JLabel zlab;
  
  private final JFormattedTextField xent;
  private final JFormattedTextField yent;
  private final JFormattedTextField zent;
  
  private final JButton grabcoord;
  
  private static final long serialVersionUID = 1L;
  private static final String locprefix = GuidePanel.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
