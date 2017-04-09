import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

public class ScriptForServerName {

	public void  edit(String rootDir,String DestDir)
	{
		File Directory = new File(rootDir);
		
		File []ffile = Directory.listFiles();
		
		for (File file : ffile) {
			
			if(file.isDirectory())
			{
				//System.out.println("File is directory and path is "+file.getPath());
				//System.out.println(DestDir+"\\"+file.getName());
				File tmp = new File(DestDir+"\\"+file.getName());
				
				boolean flag = false;
				if(!tmp.exists())
					flag=tmp.mkdir();
				
				if(flag==true)
					System.out.println("created");
				else
					System.out.println("Not Created");
				
				edit(file.getPath(),DestDir+"\\"+file.getName());
			}
			else
			{
			//	System.out.println("File is not directory");
				// edit file
				File tmp = new File(DestDir+"\\"+file.getName());
				if(tmp.exists())
					tmp.delete();
			//	System.out.println("Copying from "+file.getPath());
				try {
					BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
					BufferedWriter writer = new BufferedWriter(new FileWriter(tmp.getPath()));
					String currline;
					while((currline = br.readLine())!=null)
					{
						writer.write(currline.replaceAll("rb-81", "rb-86"));
					//	System.out.println(currline);
					}
					
					br.close();
					writer.close();
					
				//	System.exit(0);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	public static void main(String args[])
	{
		ScriptForServerName script = new ScriptForServerName();
		script.edit("C:\\Users\\rbhor\\IdeaProjects\\BMC_BSA\\src\\Backup","C:\\Users\\rbhor\\Script\\Backup1");	
	}
}
