/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scimark2src.jnt.scimark2;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
/**
 *
 * @author sza0033
 */
public class Examples {
    public Examples()
    {
        
    }

    public void client()
    {

    }
    /*public void File()
    {
    File file = new File("C:\\MyFile.txt");
    FileInputStream fileInputStream = null;
    BufferedInputStream bufferedInputStream = null;
    DataInputStream dataInputStream = null;

    try {
     fileInputStream= new FileInputStream(file);
      bufferedInputStream  = new BufferedInputStream(fileInputStream);
       dataInputStream = new DataInputStream(bufferedInputStream);
       String line = new String();
      while (dataInputStream.available() != 0) {
          line = dataInputStream.readLine();
          if(line.startsWith("+1"))
          {
              String [] str =line.split(" ");
              for(int i=0;i<str.length;i++)
              {
                  String [] str1 = str[0].split(":");
                  for(int j=0;j<str1.length;j++)
                  {
                      positivefeaturesList.add(str1[j]);
                  }
              }
              
          }
           if(line.startsWith("-1"))
          {
              negativefeaturesList.add(line.split(" "));
          }
          System.out.println(dataInputStream.readLine());
      }
      
      fileInputStream.close();
      bufferedInputStream.close();
      dataInputStream.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    }*/
}
