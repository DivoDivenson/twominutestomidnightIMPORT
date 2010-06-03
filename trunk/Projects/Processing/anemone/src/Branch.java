import processing.core.PApplet;


public class Branch {
  Node from;
  Node to;

  boolean active;

  Integrator length;

  Anemone parent;


  public Branch(Anemone anemone, Node from, Node to) {
    this.parent = anemone;
    this.from = from;
    this.to = to;
    
    from.addChild(to);
    to.setParent(from);

    active = true;

    // If the 'to' is in the default position, instead set it to the same 
    // location as its 'from' node to make the edge extend from that location.
    if ((to.x == parent.width/2) && (to.y == parent.height/2)) {
      to.x = from.x;
      to.y = from.y;
    }

    length = new Integrator();
  }


  protected void relax() {
    // update whether this edge is active or not
    if (!from.active || !to.active) {
      active = false;
    } else if (!active && (from.active || to.active)) {
      active = true;
    }
    if (!active) return;

    float vx = to.x - from.x;
    float vy = to.y - from.y;

    float len = PApplet.mag(vx, vy);
    float f = 0;
    // Only calculate if len is nonzero, otherwise goes to infinity
    if (len != 0) {
      f = (length.value - len) / (len * 3);
    }
    float dx = f * vx; 
    float dy = f * vy; 

    to.dx += dx;
    to.dy += dy;
    from.dx -= dx;
    from.dy -= dy;

    // Set the target length larger when there are more children involved.
    length.target((from.getChildCount() + to.getChildCount()) * 1.5f);
  }


  protected void update() {
    if (active) {
      length.update();
    }
  }

  
  protected void draw() {
    if (active) {
      float span = (float) (Node.newestTime - Node.oldestTime);
      float elapsed = (float) (to.lastVisitTime - Node.oldestTime);
      float weight = elapsed / span;
      parent.fill(1, weight);
      parent.noStroke();
      parent.drawConnection(from.x, from.y, from.thickness,  
                            to.x, to.y, to.thickness);
    }
  }
}