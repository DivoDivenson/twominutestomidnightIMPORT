package Graph;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.HashMap;

import processing.core.*;
import processing.pdf.*;

public class Graph extends PApplet {

	int nodeCount = 0;
	Node[] nodes = new Node[100];
	HashMap nodeTable = new HashMap();
	int edgeCount;
	Edge[] edges = new Edge[500];

	Node selection;

	static final Color nodeColor = new Color(240, 192, 112);
	static final Color selectColor = new Color(255, 48, 28);
	static final Color fixedColor = new Color(255, 128, 128);
	static final Color edgeColor = new Color(0, 0, 0);

	PFont font;

	public void setup() {
		size(600, 600, P2D);
		loadData();
		font = createFont("Liberation Mono Bold", 15);
		textFont(font);
		smooth();
		String[] fonts = PFont.list();
		for(int i = 0; i < fonts.length; i++){
			System.out.println(fonts[i]);
		}
		writeData();
		// frameRate(30);
	}

	public void draw() {
		if (record) {
			beginRecord(PDF, "src/data/output.pdf");
		}
		textFont(font);
		smooth();

		background(255);
		for (int i = 0; i < edgeCount; i++) {
			edges[i].relax();
		}
		for (int i = 0; i < nodeCount; i++) {
			nodes[i].relax();
		}
		for (int i = 0; i < nodeCount; i++) {
			nodes[i].update();
		}
		for (int i = 0; i < edgeCount; i++) {
			edges[i].draw();
		}
		for (int i = 0; i < nodeCount; i++) {
			nodes[i].draw();
		}

		if (record) {
			endRecord();
			record = false;
		}
	}

	boolean record;

	public void keyPressed() {
		if (key == 'r') {
			record = true;
		}
	}
	
	void writeData( ) {
		  PrintWriter writer = createWriter("src/data/huckfinn.dot");
		  writer.println("digraph output {");
		  for (int i = 0; i < edgeCount; i++) {
		    String from = edges[i].from.label;
		    String to = edges[i].to.label;
		    writer.println(TAB + from + " -> " + to + ";");
		  }
		  writer.println("}");
		  writer.flush( );
		  writer.close( );
		}

	

	public void mousePressed() {
		// Ignore anything greater than this distance.
		float closest = 20;
		for (int i = 0; i < nodeCount; i++) {
			Node n = nodes[i];
			float d = dist((float) mouseX, (float) mouseY, (float) (n.x),
					(float) (n.y));
			if (d < closest) {
				selection = n;
				closest = d;
			}
		}
		if (selection != null) {
			if (mouseButton == LEFT) {
				selection.fixed = true;
			} else if (mouseButton == RIGHT) {
				selection.fixed = false;
			}
		}
	}

	public void mouseDragged() {
		if (selection != null) {
			selection.x = mouseX;
			selection.y = mouseY;
		}
	}

	public void mouseReleased() {
		selection = null;
	}

	void loadData() {
		String[] lines = loadStrings("src/data/huckfinn.old");
		// Make the text into a single String object.
		String line = join(lines, " ");
		// Replace -- with an actual em dash.
		line = line.replaceAll("--", "\u2014");
		// Split into phrases using any of the provided tokens.
		String[] phrases = splitTokens(line, ".,;:?!\u2014\"");
		for (int i = 0; i < phrases.length; i++) {
			// Make this phrase lowercase.
			String phrase = phrases[i].toLowerCase();
			// Split each phrase into individual words at one or more spaces.
			String[] words = splitTokens(phrase, " ");
			for (int w = 0; w < words.length - 1; w++) {
				addEdge(words[w], words[w + 1]);
			}
		}

		System.out.println(nodeCount);
	}

	void addEdge(String fromLabel, String toLabel) {
		// Filter out unnecessary words.
		if (ignoreWord(fromLabel) || ignoreWord(toLabel))
			return;
		Node from = findNode(fromLabel);
		Node to = findNode(toLabel);
		from.increment();
		to.increment();
		// Check to see whether this Edge already exists.
		for (int i = 0; i < edgeCount; i++) {
			if (edges[i].from == from && edges[i].to == to) {
				edges[i].increment();
				return;
			}
		}
		Edge e = new Edge(from, to, this);
		e.increment();
		if (edgeCount == edges.length) {
			edges = (Edge[]) expand(edges);
		}
		edges[edgeCount++] = e;
	}

	Node findNode(String label) {
		label = label.toLowerCase();
		Node n = (Node) nodeTable.get(label);
		if (n == null) {
			return addNode(label);
		}
		return n;
	}

	Node addNode(String label) {
		Node n = new Node(label, this);
		if (nodeCount == nodes.length) {
			nodes = (Node[]) expand(nodes);
		}
		nodeTable.put(label, n);
		nodes[nodeCount++] = n;
		return n;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	String[] ignore = { "a", "of", "the", "i", "it", "you", "and", "to" };

	boolean ignoreWord(String what) {
		for (int i = 0; i < ignore.length; i++) {
			if (what.equals(ignore[i])) {
				return true;
			}
		}
		return false;
	}

}
