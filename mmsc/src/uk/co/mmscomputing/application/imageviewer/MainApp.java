package uk.co.mmscomputing.application.imageviewer;


import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import uk.co.mmscomputing.util.UtilMainApp;

public class MainApp extends UtilMainApp{
    private static final long serialVersionUID = 7572377649157491141L;

 
  ScannerTab scannerTab=null;

  public MainApp(){                     // need this in every applet application class
    super();
    ImageIO.scanForPlugins();
  }

  public MainApp(String title, String[] argv){
    super(title,argv);
    ImageIO.scanForPlugins();
  }

  @Override
  protected JPanel getCenterPanel(Properties properties)throws Exception{
    scannerTab = new ScannerTab(properties);
    return scannerTab;
  }

//  protected void setFrameSize(JFrame frame, Rectangle bounds){
//    frame.setSize(bounds.width*95/100,bounds.height*95/100);
//  }

  @Override
  public void stop(){  
    if(scannerTab!=null){
      scannerTab.stop();
    }                             // make sure user waits for scanner to finish!
    super.stop();
  }

  public static void main(String[] argv){
    try{
      MainApp app=new MainApp("Multi Page Image Viewer [2005-09-22]", argv);
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}


