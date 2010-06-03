package Graph;

import processing.core.PApplet;

class Edge {
	Node from;
	Node to;
	Graph p;

	float len;

	Edge(Node from, Node to, Graph p) {
		this.from = from;
		this.to = to;
		this.len = 50;
		this.p = p;
	}

	void relax() {
		float vx = to.x - from.x;
		float vy = to.y - from.y;
		float d = p.mag(vx, vy); // Calculate magnitude of vector
		if (d > 0) {
			float f = (len - d) / (d * 3);
			float dx = f * vx;
			float dy = f * vy;
			to.dx += dx;
			to.dy += dy;
			from.dx -= dx;
			from.dy -= dy;
		}
	}

	void draw() {
		p.stroke(p.edgeColor.getRGB());
		p.strokeWeight(0.35f);
		p.line(from.x, from.y, to.x, to.y);
	}

	int count;

	void increment() {
		count++;
	}

}
