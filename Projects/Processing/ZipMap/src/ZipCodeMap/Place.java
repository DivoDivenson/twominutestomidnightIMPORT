package ZipCodeMap;

import java.awt.Color;

import processing.core.*;

class Place {
	int code;
	String name;
	float x;
	float y;
	ZipMap p;

	int partial[];
	int matchDepth;

	public Place(int code, String name, float x, float y, ZipMap p) {
		this.code = code;
		this.name = name;
		this.x = x;
		this.y = y;
		this.p = p;

		partial = new int[6];
		partial[5] = code;
		partial[4] = partial[5] / 10;
		partial[3] = partial[4] / 10;
		partial[2] = partial[3] / 10;
		partial[1] = partial[2] / 10;

	}

	void check() {
		// Default to zero levels of depth that match
		matchDepth = 0;
		if (p.typedCount != 0) {
			// Start from the greatest depth, and work backwards to see how many
			// items match. Want to figure out the maximum match, so better to
			// begin from the end.
			for (int j = p.typedCount; j > 0; --j) {
				if (p.typedPartials[j] == partial[j]) {
					matchDepth = j;
					break; // Since starting at end, can stop now.
				}
			}
		}
		if (matchDepth == p.typedCount) {
			p.foundCount++;
		}
	}

	void draw() {
		int xx = (int) p.TX(x);
		int yy = (int) p.TY(y);
		p.set(xx, yy, p.color(0, 0, 0));

		Color c = p.dormantColor;
		if (p.typedCount != 0) {
			if (matchDepth == p.typedCount) {
				c = p.highlightedColor;
			} else {
				c = p.unhighlightedColor;
			}
		}
		p.set(xx, yy, c.getRGB());

	}

	void drawChosen() {
		p.noStroke();
		p.fill(p.highlightedColor.getRGB());
		int size = 4;
		p.rect(p.TX(x), p.TY(y), size, size);
		// Calculate position to draw the text, offset slightly from the main
		// point.
		float textX = p.TX(x);
		float textY = p.TY(y) - size - 4;
		// Don't go off the top (e.g., 59544).
		if (textY < 20) {
			textY = p.TY(y) + 20;
		}
		// Don't run off the bottom (e.g., 33242).
		if (textY > p.height - 5) {
			textY = p.TY(y) - 20;
		}
		String location = name + " " + p.nf(code, 5);
		System.out.println(location);
		float wide = p.textWidth(location);
		if (textX > p.width / 3) {
			textX -= wide + 8;
		} else {
			textX += 8;
		}
		p.textAlign(p.LEFT);
		p.fill(p.highlightedColor.getRGB());
		p.text(location, textX, textY);
	}

}
