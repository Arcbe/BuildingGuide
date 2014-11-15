/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.mcmods.introspector;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import java.awt.HeadlessException;
import java.util.logging.Logger;
import javax.swing.JFrame;
import taiga.mcmods.buildguide.BGPanel;

@Mod(modid="BuildingGuide", name="BuildingGuide", version="0.0.1")
public class FMLHook extends JFrame {
  
  @Instance(value = "BuildingGuide")
  public static FMLHook instance;

  public FMLHook() throws HeadlessException {
    super("Introspector");
  }
  
  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    add(new BGPanel());
    pack();
    setVisible(true);
  }

  private static final String locprefix = FMLHook.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
