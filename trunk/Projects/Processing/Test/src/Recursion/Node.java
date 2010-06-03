package Recursion;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

public class Node {
	File file;
	Node[] children;
	int childCount;
	setup s;

	Node(File file, setup s) {
		this.file = file;
		this.s = s;
		if (file.isDirectory()) {
			s.addFolder(this);
		}
	}

	void printList() {
		printList(0);
	}

	void check() {
		String[] contents = file.list();
		if (contents != null) {
			// Sort the file names in case-insensitive order.
			MyComparator comp = new MyComparator();
			java.util.Arrays.sort(contents, comp);
			children = new Node[contents.length];
			for (int i = 0; i < contents.length; i++) {
				// Skip the . and .. directory entries on Unix systems.
				if (contents[i].equals(".") || contents[i].equals("..")) {
					continue;
				}
				File childFile = new File(file, contents[i]);
				// Skip any file that appears to be a symbolic link.
				try {
					String absPath = childFile.getAbsolutePath();
					String canPath = childFile.getCanonicalPath();
					if (!absPath.equals(canPath)) {
						continue;
					}
				} catch (IOException e) {
				}
				Node child = new Node(childFile,s);
				children[childCount++] = child;
			}
		}
	}

	void printList(int depth) {
		// Print spaces for each level of depth.
		for (int i = 0; i < depth; i++) {
			System.out.print(" ");
		}
		System.out.println(file.getName());
		// Now handle the children, if any.
		for (int i = 0; i < childCount; i++) {
			children[i].printList(depth + 1);
		}
	}
}

class MyComparator implements Comparator<String> {
	public int compare(String strA, String strB) {
		return strA.compareToIgnoreCase(strB);
	}
}
