/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scimark2src.jnt.scimark2;

/**
 *
 * @author sza0033
 */
public class ProbeTemp implements Runnable {
    public void run()
    {
        executeProcess("sensors");
        
    }
     public String executeProcess(String processName)
  {
      try
        {
            
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(processName);
            java.io.InputStream stderr = proc.getInputStream();
            java.io.InputStreamReader isr = new java.io.InputStreamReader(stderr);
            java.io.BufferedReader br = new java.io.BufferedReader(isr);
            String line = null;
            String temp = null;
            System.out.println("<Ouput>");
            while ( (line = br.readLine()) != null)
            {
                if(line.indexOf("°C")>0)
                temp =line.substring((line.indexOf("°C")-4),(line.indexOf("°C")));
                System.out.println(line);
            }
            System.out.println("</Output>");
            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
            return temp;
        } catch (Throwable t)
          {
            t.printStackTrace();
            return null;
          }
      
  }
}
