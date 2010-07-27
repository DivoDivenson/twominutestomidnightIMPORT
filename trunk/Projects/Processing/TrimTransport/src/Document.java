import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Document {
	final int BUFFER = 2048;
	
	public Document() {

		
		
		File file = new File("src/data/tmp/SCSS.odt");
		File dir = new File("src/data/tmp");
		try {
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream(file);
			ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;
			while((entry = zin.getNextEntry()) != null){
				String name = entry.getName();
				String outputPath = String.format("%s%s%s",dir.getAbsolutePath(), "/", name);
				System.out.println(name);
				/*if(entry.isDirectory()){
					File tempDir = new File(outputPath);
					dir.mkdirs();
				}else{
					int count;
					byte data[] = new byte[BUFFER];
					FileOutputStream fos = new FileOutputStream(outputPath);
					dest = new BufferedOutputStream(fos, BUFFER);
					while((count = zin.read(data, 0, BUFFER)) != -1 ){
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
				}*/
				
			}
			zin.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	
	}
	
	private void open(){
		File doc = new File("src/data/tmp/doc.odt");
		File dir = new File("src/data/tmp");
		try{
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream(doc);
			ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fis));
			
		}catch(Exception e){
			
		}
	}
}
