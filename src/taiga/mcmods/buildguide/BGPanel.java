/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.mcmods.buildguide;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class BGPanel extends JPanel {
  
  public static final String[] OPTIONS = new String[]{"Circle", "Spiral"};
  public static final int GUI_SPACING = 5;

  public BGPanel() {
    super(new BorderLayout());
    
    this.guidelist = Box.createVerticalBox();
    this.create = new JButton("Create Guide");
    this.select = new JComboBox<>(OPTIONS);
    
    select.setEditable(false);
    create.setAlignmentX(LEFT_ALIGNMENT);
    select.setAlignmentX(LEFT_ALIGNMENT);
    
    JPanel buffer = new JPanel();
    buffer.add(guidelist);
    
    JScrollPane scroll = new JScrollPane(buffer);
    scroll.setBorder(new TitledBorder("Guides"));
    add(scroll, BorderLayout.CENTER);
    
    Box temp = Box.createHorizontalBox();
    temp.add(create);
    temp.add(Box.createHorizontalStrut(GUI_SPACING));
    temp.add(select);
    
    add(temp, BorderLayout.SOUTH);
    
    create.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        createNewGuide();
      }
    });
  }
  
  private void createNewGuide() {
    JComponent guide = null;
    String opt = (String) select.getSelectedItem();
    
    if(opt == OPTIONS[0]) guide = new CircleGuidePanel();
    else if(opt == OPTIONS[1]) guide = new SpiralGuidePanel();
    else return;
    
    guide.setBorder(new EtchedBorder());
    guide.setAlignmentX(LEFT_ALIGNMENT);
    guide.setAlignmentY(TOP_ALIGNMENT);
    
    guidelist.add(guide);
    guidelist.revalidate();
  }
  
  private final Box guidelist;
  private final JButton create;
  private final JComboBox<String> select;

  private static final String locprefix = BGPanel.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
