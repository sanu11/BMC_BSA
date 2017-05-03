package com.bmc.cloud.bsasimulator.internal;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
/**
 * Created by pratshin on 02-05-2017.
 */
public class ScriptForServerName {
    public ScriptForServerName() {
        super();
    }

    public static void  edit(String rootDir, String DestDir,String NewName)
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

                edit(file.getPath(),DestDir+"\\"+file.getName(),NewName);
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
                        writer.write(currline.replaceAll("rb-114", NewName));
                        writer.write("\n");
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

    public static void ChangeServerName(String NewName)
    {
        edit("C:\\Users\\pratshin\\workspace\\Simulator\\src\\Backup_Src_Response","C:\\Users\\pratshin\\workspace\\Simulator\\src",NewName);
    }
}
