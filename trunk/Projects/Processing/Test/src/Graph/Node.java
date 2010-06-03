package Graph;

class Node {
	float x;
	float y;

	float dx;
	float dy;

	boolean fixed;

	Graph g;

	String label;
	int count;

	Node(String label, Graph g) {
		this.label = label;
		x = g.random(g.width);
		y = g.random(g.height);
		this.g = g;
	}

	void relax() {
		float ddx = 0;
		float ddy = 0;
		// System.err.println(g.getNodeCount());
		for (int j = 0; j < g.nodeCount; j++) {
			// System.err.println(j);
			Node n = g.nodes[j];
			if (n != this) {
				float vx = x - n.x;
				float vy = y - n.y;
				float lensq = vx * vx + vy * vy;
				if (lensq == 0) {
					ddx += g.random(1);
					ddy += g.random(1);
				} else if (lensq < 100 * 100) {
					ddx += vx / lensq;
					ddy += vy / lensq;
				}
			}
		}
		float dlen = g.mag(ddx, ddy) / 2;
		if (dlen > 0) {
			dx += ddx / dlen;
			dy += ddy / dlen;
		}
	}

	void update() {
		if (!fixed) {
			x += g.constrain(dx, -5, 5);
			y += g.constrain(dy, -5, 5);
			x = g.constrain(x, 0, g.width);
			y = g.constrain(y, 0, g.height);
		}
		dx /= 2;
		dy /= 2;
	}

	/*
	 * void draw() { if (g.selection == this) { g.fill(g.selectColor.getRGB());
	 * } else if (fixed) { g.fill(g.fixedColor.getRGB()); } else {
	 * g.fill(g.nodeColor.getRGB()); } g.stroke(0); g.strokeWeight(0.5f);
	 * g.rectMode(g.CORNER); float w = g.textWidth(label) + 10; float h =
	 * g.textAscent() + g.textDescent() + 4; g.rect(x - w / 2, y - h / 2, w, h);
	 * g.fill(0); g.textAlign(g.CENTER, g.CENTER); g.text(label, x, y); }
	 */
	void draw() {
		int rad = (int) g.map((float) count, 1f, 55f, 6f, 100f); // This code is
																	// specific
																	// to the
																	// Data set
		if (fixed) {
			g.fill(g.nodeColor.getRGB());
			g.stroke(0);
			g.strokeWeight(0.5f);
			g.ellipseMode(g.CENTER);
			float w = g.textWidth(label) + 10;
			if (w > rad) {
				g.ellipse(x, y, w, w);

			} else {
				g.ellipse(x, y, rad, rad);
			}
			g.fill(0);
			g.textAlign(g.CENTER, g.CENTER);
			g.text(label, x, y);
		} else {
			g.fill(g.nodeColor.getRGB());
			g.stroke(0);
			g.strokeWeight(0.5f);
			g.ellipse(x, y, rad, rad);
		}
	}

	void increment() {
		count++;
	}

}