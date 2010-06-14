package FileSize;

import java.io.File;
import java.io.IOException;

import processing.core.PApplet;

import treemap.*;

class FolderItem extends FileItem implements MapModel {
	MapLayout algorithm = new PivotBySplitSize();
	//algorithm = new SquarifiedLayout( );
	Mappable[] items;
	//boolean contentsVisible;
	boolean layoutValid;
	setup p;

	public FolderItem(FolderItem parent, File folder, int level, int order,
			setup p) {
		super(parent, folder, level, order, p);
		this.p = p;
		String[] contents = folder.list();
		if (contents != null) {
			contents = p.sort(contents);
			items = new Mappable[contents.length];
			int count = 0;
			for (int i = 0; i < contents.length; i++) {
				if (contents[i].equals(".") || contents[i].equals("..")) {
					continue;
				}
				File fileItem = new File(folder, contents[i]);
				try {
					String absolutePath = fileItem.getAbsolutePath();
					String canonicalPath = fileItem.getCanonicalPath();
					if (!absolutePath.equals(canonicalPath)) {
						continue;
					}
				} catch (IOException e) {
				}
				FileItem newItem = null;
				if (fileItem.isDirectory()) {
					newItem = new FolderItem(this, fileItem, level + 1, count, p);
				} else {
					newItem = new FileItem(this, fileItem, level + 1, count, p);
				}
				items[count++] = newItem;
				size += newItem.getSize();
			}
			if (count != items.length) {
				items = (Mappable[]) p.subset(items, 0, count);
			}
		} else {
			// If no items found in this folder, create a dummy array so that
			// items will not be null, which will ensure that items.length will
			// return 0 rather than causing a NullPointerException.
			items = new Mappable[0];
		}
	}

	void checkLayout() {
		if (!layoutValid) {
			if (getItemCount() != 0) {
				algorithm.layout(this, bounds);
			}
			layoutValid = true;
		}
	}

	public void draw() {
		checkLayout();
		calcBox();
		if (contentsVisible) {
			for (int i = 0; i < items.length; i++) {
				items[i].draw();
			}
		} else {
			super.draw();
		}
	}
	
	public void drawTitle(){
		if(!contentsVisible){
			super.drawTitle();
		}
	}

	public Mappable[] getItems() {
		return items;
	}

	public int getItemCount() {
		return items.length;
	}

	void showContents() {
		contentsVisible = true;
	}


}
