import processing.core.PApplet;

// a path is a series of jumps made by an actor
// the path is rendered as a curve that fades over time

public class Visitor {
  // Time out after 30 minutes (1,800,000 milliseconds)
  static final long TIMEOUT = 30 * 60 * 1000; 

  // Parent sketch for drawing
  Anemone parent;
  
  // Name (ip address) of user taking this path
  String name;

  int jumpCount;
  Node[] jumpNodes;
  long[] jumpTimes;
  

  public Visitor(Anemone parent, String name) {
    this.parent = parent;
    this.name = name;

    jumpCount = 0;
    jumpNodes = new Node[10];
    jumpTimes = new long[10];
  }


  public void addVisit(Node node, long time) {
    if (jumpCount == jumpNodes.length) {
      jumpNodes = (Node[]) PApplet.expand(jumpNodes);
      jumpTimes = (long[]) PApplet.expand(jumpTimes);
    }
    jumpNodes[jumpCount] = node;
    jumpTimes[jumpCount] = time;
    jumpCount++;
  }

  
  public void draw() {
    if (jumpCount > 2) {
      parent.noFill();
      parent.strokeWeight(0.5f);
      for (int i = 0; i < jumpCount-1; i++) {
        long timeoutTime = Node.newestTime - TIMEOUT;
        int elapsed = (int) (jumpTimes[i+1] - timeoutTime);
        float weight = PApplet.map(elapsed, 0, (int) TIMEOUT, 0, 1);
        if (weight < 0) weight = 0;
        parent.stroke(0.9f, 0.7f, 0.3f, weight);
        
        Node n1;
        // If this is the first point, use the first node twice
        if (i == 0) {
          n1 = jumpNodes[0];
        } else {
          n1 = jumpNodes[i-1];
        }
        
        Node n2 = jumpNodes[i];
        Node n3 = jumpNodes[i+1];
        
        Node n4;
        // If this is the end of the list of jumps, double the last point
        if (i == jumpCount-2) {
          n4 = jumpNodes[jumpCount-1];
        } else {
          n4 = jumpNodes[i+2];
        }
        
        parent.curve(n1.x, n1.y, n2.x, n2.y, n3.x, n3.y, n4.x, n4.y);
      }
    }    
  }
}