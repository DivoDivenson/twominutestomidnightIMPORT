import java.io.*;
import java.util.*;

import processing.core.*;


public class Anemone extends PApplet {

  int backgroundColor = color(40, 69, 105);
  int maxNodeCount = 500;

  Node movingNode;
  
  Streamer streamer;
  
  ArrayList<Node> activeNodes = new ArrayList<Node>();
  ArrayList<Node> allNodes = new ArrayList<Node>();

  ArrayList<Branch> branches = new ArrayList<Branch>();

  // Nodes mapped by name
  HashMap<String, Node> nodeLookup = new HashMap<String, Node>();

  // Visitors mapped by name (ip address)
  HashMap<String,Visitor> visitors = new HashMap<String, Visitor>();
  

  public void setup() {
    //size(960, 600);
    size(720, 405);
    
    PFont font = createFont("Verdana", 11);
    textFont(font);
    
    // Add the root node
    addNode("/", width/2, height/2);

    Record.setSiteAddress("http://www.processing.org");
    Record.addSiteAlias("http://processing.org"); 
    Record.addSiteAlias("http://proce55ing.net"); 
    Record.addSiteAlias("http://www.proce55ing.net");
    
    // Start streaming new data
    BufferedReader reader = createReader("../combined_log");
    streamer = new Streamer(reader);

    colorMode(RGB, 1);
    ellipseMode(RADIUS);
    smooth();      
    frameRate(20);
  }
  
  
  public void draw() {
    background(backgroundColor);
    cursor(CROSS);

    // Read up to 10 lines of the log file (if available)
    for (int i = 0; i < 10; i++) {
      readNextRecord();
    }

    for (Branch b : branches) b.relax();
    for (Branch b : branches) b.update();
    for (Node n : activeNodes) n.relax();
    for (Node n : activeNodes) n.update();

    for (Visitor v : visitors.values()) v.draw();
    for (Branch b : branches) b.draw();
    for (Node n : activeNodes) n.draw();

    // Show status information at the bottom
    drawStatus();
    
    // Keep a constant number of nodes on screen
    pruneNodes();
  }
  
  
  public void keyPressed() {
    if (key == 'g') {
      saveFrame();
    }
  }
  
  
  public void drawStatus() {
    fill(1, 0.3f);
    final float margin = 15;

    textAlign(LEFT);
    Date date = new Date(Node.newestTime);
    text(date.toString(), margin, height - margin);

    textAlign(CENTER);
    float dataElapsedTime = Node.newestTime - Node.oldestTime;
    float appElapsedTime = millis();
    float rate = dataElapsedTime / appElapsedTime;
    text(nf(rate, 0, 1) + "x", width/2, height - margin);
    //text(nf(frameRate, 0, 1) + " fps", width/2, height - margin);

    textAlign(RIGHT);
    String msg = "Showing " + activeNodes.size() + 
      " active nodes out of " + allNodes.size() + " total"; 
    text(msg, width - margin, height - margin);    
  }

  
  public void drawConnection(float x1, float y1, float r1, 
                             float x2, float y2, float r2) {
    float angle = atan2(y2 - y1, x2 - x1);

    beginShape();
    int stepCount = (int) r1+1; 
    for (int i = 0; i <= stepCount; i++) {
      float theta = map(i, 0, stepCount, angle + HALF_PI, angle + PI*1.5f);
      float x = x1 + r1 * cos(theta);
      float y = y1 + r1 * sin(theta);
      vertex(x, y);
    }
    stepCount = (int) r2+1;
    for (int i = 0; i <= stepCount; i++) {
      float theta = map(i, stepCount, 0, angle + HALF_PI, angle - HALF_PI);
      float x = x2 + r2 * cos(theta);
      float y = y2 + r2 * sin(theta);
      vertex(x, y);
    }
    endShape(CLOSE);
  }

  
  /////////////////////////////////////////////////////////////////////////////

  
  protected Node findClosestNode() {
    // set to the minimum distance from mouse to node to care about  
    float closestDist = 8;
    Node closestNode = null;
    
    for (Node n : allNodes) {
      float d = dist(mouseX, mouseY, n.x, n.y);
      if (d < closestDist) {
        closestDist = d;
        closestNode = n;
      }
    }
    // if nothing closer than 8, this returns null
    return closestNode;
  }
  

  public void mousePressed() {
    Node closestNode = findClosestNode();

    if (mouseButton == LEFT) {
      if (closestNode != null) {
        closestNode.nameVisible = true;
        closestNode.fixed = true;
      }
      movingNode = closestNode;

    } else if (mouseButton == RIGHT) {
      if (closestNode != null) {
        closestNode.nameVisible = false;
        closestNode.fixed = false;
      }
    }
  }


  public void mouseDragged() {
    if (movingNode != null) {
      movingNode.x += mouseX - pmouseX;
      movingNode.y += mouseY - pmouseY;
    }
  }


  /////////////////////////////////////////////////////////////////////////////

  
  public void readNextRecord() {
    Record visit = streamer.nextRecord();
    if (visit == null) return;

    // Take no action if the status is not OK
    if (visit.status == 200) return;
    
    // Don't bother with extensions we're skipping (gif, jpg, etc)
    if (visit.skipExtension()) return;

    // Clean up the URL and check the info
    visit.removeQueryString();
    visit.removeIndexPage();

    Node targetNode = checkNode(visit.url, false);
    targetNode.addVisit(visit.timestamp);

    Visitor visitor = checkVisitor(visit.ip);
    visitor.addVisit(targetNode, visit.timestamp);

    String referer = visit.cleanReferer();
    if (referer.startsWith("/")) {
      // If it's a local referer, make a note of that
      checkNode(referer, true);
      
    } else {
      // For now, skip incoming links that come from elsewhere, but this could 
      // be used to show incoming searches or links from other sites.
    }
  }
  

  // NODES ////////////////////////////////////////////////////////////////////

  
  public Node checkNode(String name, boolean referer) {
    Node n = (Node) nodeLookup.get(name);
    if (n != null) return n;

    Node newbie = addNode(name, width/2, height/2);
    if (!referer) {
      // split up the name so that branches can be created
      String[] pieces = split(name, '/');
      
      String parentPath = "/";
      Node parentNode = allNodes.get(0);

      // i starts at 1 because the first element (/) is already accounted for
      // i ends just before pieces.length-1 because that's the actual node
      //for (int i = 1; i < pieces.length-1; i++) {
      for (int i = 1; i < pieces.length-1; i++) {
        String childPath = parentPath + pieces[i] + "/";
        Node childNode = checkNode(childPath, false);
        
        // Ensure that the branch connecting these two nodes exists
        checkBranch(parentNode, childNode);
        
        parentPath = childPath;
        parentNode = childNode;
      }
      if (parentNode != newbie) {
        checkBranch(parentNode, newbie);
      }
    }
    return newbie;
  }

  
  public Node addNode(String name, float x, float y) {
    Node e = new Node(this, name, x, y);
    nodeLookup.put(e.name, e);

    allNodes.add(e);
    activeNodes.add(e);

    return e;
  }


  public void pruneNodes() {
    int pruneCount = activeNodes.size() - maxNodeCount;

    while (pruneCount > 0) {
      Node crustiestNode = activeNodes.get(0);
      long crustiestTime = crustiestNode.lastVisitTime;

      for (Node node : activeNodes) {
        if (node.lastVisitTime < crustiestTime) {
          crustiestTime = node.lastVisitTime;
          crustiestNode = node;
        }
      }
      pruneNode(crustiestNode);
      pruneCount--;
    }
  }

  
  public void pruneNode(Node node) {
    Node.oldestTime = node.lastVisitTime;
    activeNodes.remove(node);
    node.active = false;
  }


  public void unpruneNode(Node lazarus) {
    // set for a split second so this guy is not pruned immediately
    // as the while() loop continues
    lazarus.lastVisitTime = Node.newestTime;
    activeNodes.add(lazarus);
    lazarus.active = true;
  }


  // BRANCHES /////////////////////////////////////////////////////////////////

  
  public Branch checkBranch(Node from, Node to) {
    for (Branch b : branches) {
      if (b.from == from && b.to == to) {
        return b;
      }
    }
    Branch b = new Branch(this, from, to);
    branches.add(b);
    return b;
  }
  
  
  // VISITOR PATHS ////////////////////////////////////////////////////////////


  protected Visitor checkVisitor(String name) {
    synchronized (visitors) {
      Visitor v = visitors.get(name);
      if (v == null) {
        v = new Visitor(this, name);
        visitors.put(name, v);
      }
      return v;
    }
  }

  
  /////////////////////////////////////////////////////////////////////////////
  
  
  static public void main(String[] args) {
    PApplet.main(new String[] { "Anemone" });
  }  
}