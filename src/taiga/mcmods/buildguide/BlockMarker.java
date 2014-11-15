/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.mcmods.buildguide;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import net.minecraftforge.client.event.RenderHandEvent;
import org.lwjgl.opengl.GL11;

public class BlockMarker {

  public BlockMarker() {
    points = new ArrayList<>(100);
    regendisplist = true;
    displaylist = -1;
    
    visible = true;
    deletelist = false;
  }

  public BlockMarker(Color c, boolean visible) {
    points = new ArrayList<>(100);
    regendisplist = true;
    displaylist = -1;
    
    this.visible = visible;
    deletelist = false;
    color = c;
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void renderMarkers(RenderHandEvent event) {
    if(!visible) return;
    if(deletelist) {
      //GL11.glDeleteLists(displaylist, 0);
      //displaylist = -1;
      
      deletelist = false;
    }
    
    transformToPlayer();
    GL11.glPointSize(10);
    
    if(regendisplist) createDisplayList();
    else if(displaylist != -1) GL11.glCallList(displaylist);
    
    GL11.glPopMatrix();
  }
  
  public void addBlock(Point3D pt) {
    points.add(pt);
    
    regendisplist = true;
  }
  
  public void addBlocks(Collection<Point3D> pts) {
    points.addAll(pts);
    
    regendisplist = true;
  }
  
  public void setBlocks(Collection<Point3D> pts) {
    clear();
    addBlocks(pts);
  }
  
  public void clear() {
    points.clear();
    
    deletelist = true;
    regendisplist = true;
  }
  
  public void setColor(Color c) {
    color = c;
    
    regendisplist = true;
  }
  
  public void setVisible(boolean vis) {
    visible = vis;
  }
  
  private void createDisplayList() {
    //if neded create the list
    if(displaylist == -1)
      displaylist = GL11.glGenLists(1);
    
    GL11.glNewList(displaylist, GL11.GL_COMPILE_AND_EXECUTE);
    
    GL11.glDisable(GL11.GL_FOG);
    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glDisable(GL11.GL_BLEND);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glEnable(GL11.GL_POINT_SMOOTH);
    
    GL11.glBegin(GL11.GL_POINTS);
    if(color != null) GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
    for(Point3D pt : points)
      GL11.glVertex3d(pt.getX(), pt.getY(), pt.getZ());
    GL11.glEnd();
    
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    
    GL11.glEndList();
    
    regendisplist = false;
  }
  
  private void transformToPlayer() {
    FMLClientHandler ch = FMLClientHandler.instance();
    
    double x = ch.getClient().h.s;
    double y = ch.getClient().h.t;
    double z = ch.getClient().h.u;
    
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    
    GL11.glPushMatrix();
    
    GL11.glTranslated(-x, -y, -z);
  }
  
  private Collection<Point3D> points;
  private boolean regendisplist;
  private int displaylist;
  
  private Color color;
  private boolean visible;
  private boolean deletelist;

  private static final String locprefix = BlockMarker.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
