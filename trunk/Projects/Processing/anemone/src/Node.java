import java.util.ArrayList;

import processing.core.*;


public class Node {
  // Oldest and most recent visit time for all nodes
  static long oldestTime;
  static long newestTime;

  String name;
  boolean nameVisible;

  float x, y;
  float dx, dy;

  long lastVisitTime;
  
  float thickness;
  static float thicknessAdd = 1;
  static float thicknessDecay = 0.999f;
  static float thicknessMax = 10;

  boolean fixed;
  boolean active;

  Node parentNode;
  ArrayList<Node> childNodes = new ArrayList<Node>();

  Anemone parent;


  public Node(Anemone anemone, String label, float x, float y) {
    this.parent = anemone;
    this.name = label;
    this.x = x;
    this.y = y;

    thickness = 1;

    lastVisitTime = 0;

    fixed = false;
    active = true;

    lastVisitTime = newestTime;
  }


  public void addVisit(long timestamp) {
    Node p = parentNode;
    while (p != null) {
      p.active = true;
      p = p.parentNode;
    }

    if (oldestTime == 0) {
      oldestTime = timestamp;
    }
    newestTime = timestamp;
    
    lastVisitTime = timestamp;
    thickness += thicknessAdd;
  }


  protected void relax() {
    // Apply some attrition to the thickness of this node
    thickness *= thicknessDecay;

    float ndx = 0; 
    float ndy = 0; 

    for (Node n : parent.activeNodes) {
      if (n != this) {
        float nvx = x - n.x;
        float nvy = y - n.y;
        float lensq = nvx*nvx + nvy*nvy;
        if (lensq == 0) {
          float comparison = name.compareTo(n.name);
          ndx += comparison;
          if ((name.length() > 1) && (n.name.length() > 1)) {
            comparison = (name.charAt(1) > n.name.charAt(1)) ? 1 : -1;
          }
          ndy += comparison;
        } else if (lensq < 400) {
          // if too close, move away
          ndx += nvx / (lensq * 1000); 
          ndy += nvy / (lensq * 1000); 
        }
      }
    }
    float dlen = PApplet.mag(ndx, ndy) / 2;
    if (dlen > 0) {
      dx += ndx / dlen;
      dy += ndy / dlen;
    }    
  } 


  protected void update() {
    if (thickness > thicknessMax) {
      thickness = thicknessMax;
    }
    if (!fixed) {
      x += PApplet.constrain(dx, -5, 5);
      y += PApplet.constrain(dy, -5, 5);

      x = PApplet.constrain(x, 0, parent.width);
      y = PApplet.constrain(y, 0, parent.height);
    } 
    dx /= 2.0f;
    dy /= 2.0f;
  }


  public void draw() {
    if (nameVisible) {
      parent.textAlign(PConstants.CENTER);
      
      /*
      // draw translucent box behind the text
      parent.fill(0, 0.2f);
      float w = parent.textWidth(name);
      float y1 = y - parent.textAscent() - 1;
      float y2 = y + parent.textDescent() + 1;
      parent.rectMode(PConstants.CORNERS);
      parent.rect(x - w/2f - 3, y1, x + w/2f + 3, y2);
      */
      
      /*
      // tv-style black outline
      parent.fill(0, 0.3f);
      parent.text(name, x-1 + thickness + 2, y);
      parent.text(name, x + thickness + 2, y-1);
      parent.text(name, x+1 + thickness + 2, y);
      parent.text(name, x + thickness + 2, y+1);
      */
      
      parent.fill(0.8f, 0.9f, 0.5f);
      parent.text(name, x + thickness + 2, y);
    }
  }


  public void setParent(Node n) {
    parentNode = n;
  }


  public void addChild(Node n) {
    childNodes.add(n);
    if (!active) {
      parent.unpruneNode(this);
    }
  }
  
  
  public int getChildCount() {
    return childNodes.size();
  }
}
