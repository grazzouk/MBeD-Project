import shed.mbed.*;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.PointerInfo;
import java.awt.MouseInfo;
import java.awt.Point;


/**
 * A program to make the air mouse mbed device click and move.
 * 
 * @author gbr4 & djb
 * @version 22/03/2017
 */
public class Program
{
    // The object for interacting with the FRDM/MBED.
    private MBed mbed;
    private int sensitivity;
    /**
     * Open a connection to the MBED.
     */
    public Program()
    {
        mbed = MBedUtils.getMBed();
    }
    
    /**
     * A script to make translate actions on the mbed to mouse actions.
     */
    public void run() throws Exception 
    {
        LCD lcd = mbed.getLCD();  
        Robot bot = new Robot(); 
        Accelerometer acc = mbed.getAccelerometerShield();
        Potentiometer pot1 = mbed.getPotentiometer1();
        double originalX = acc.getAcceleration().getX();
        double originalY = acc.getAcceleration().getY();
        double originalPot = pot1.getValue();
        double sensitivityDouble = round(originalPot*50,1);
        sensitivity = (int) sensitivityDouble;
        //Left click
        mbed.getJoystickFire().addListener(
            isPressed -> {
                if(isPressed) {
                   bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                }
                else if(!isPressed){
                   bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                
            }); 
        
        //Right click
        mbed.getSwitch2().addListener(
            isPressed -> {
                  if(isPressed) {
                   bot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                }
                else if(!isPressed){
                   bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                }
                
            });
            
        //Middle click
        mbed.getSwitch3().addListener(
            isPressed -> {
                  if(isPressed) {
                   bot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                }
                else if(!isPressed){
                   bot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                }
                
            });
            
        // Joystick Buttons
        mbed.getJoystickRight().addListener(
            isPressed -> {
                if (isPressed) {
                   for (int i=0 ; i<10 ; i++){
                   bot.keyPress(KeyEvent.VK_RIGHT);
                  }
                  bot.keyRelease(KeyEvent.VK_RIGHT);
                }
                
            });    
        mbed.getJoystickLeft().addListener(
            isPressed -> {
                if (isPressed) {
                   for (int i=0 ; i<10 ; i++){
                   bot.keyPress(KeyEvent.VK_LEFT);
                  }
                  bot.keyRelease(KeyEvent.VK_LEFT);
                }
                
            });    
        mbed.getJoystickUp().addListener(
            isPressed -> {
                if (isPressed) {
                   for (int i=0 ; i<10 ; i++){
                   bot.keyPress(KeyEvent.VK_UP);
                  }
                  bot.keyRelease(KeyEvent.VK_UP);
                }
                
            });    
        mbed.getJoystickDown().addListener(
            isPressed -> {
                if (isPressed) {
                   for (int i=0 ; i<10 ; i++){
                   bot.keyPress(KeyEvent.VK_DOWN);
                  }
                  bot.keyRelease(KeyEvent.VK_DOWN);
                }
            });   
        
            
        System.out.println("The AirMouse has sucessfully loaded and is ready to use.");
         for(;;){       
            double currentX = acc.getAcceleration().getX();
            double currentY = acc.getAcceleration().getY();
            double currentSensitivity = (round(pot1.getValue()*50, 1));
            if(currentSensitivity != sensitivityDouble)
            {
                sensitivity = (int) currentSensitivity;
            }
            if (currentY > originalY +.2){
                PointerInfo a = MouseInfo.getPointerInfo();
                Point b = a.getLocation();
                int y = (int) b.getY();
                int x = (int) b.getX();
                bot.mouseMove(x - sensitivity,y);
            }
            else if (currentY  < originalY-.2){
                PointerInfo a = MouseInfo.getPointerInfo();
                Point b = a.getLocation();
                int y = (int) b.getY();
                int x = (int) b.getX();
                bot.mouseMove(x + sensitivity,y);
            }
              if (currentX > originalX +.2 ){
                PointerInfo a = MouseInfo.getPointerInfo();
                Point b = a.getLocation();
                int y = (int) b.getY();
                int x = (int) b.getX();
                bot.mouseMove(x,y + sensitivity);
            }
            else if (currentX < originalX -.2){
                PointerInfo a = MouseInfo.getPointerInfo();
                Point b = a.getLocation();
                int y = (int) b.getY();
                int x = (int) b.getX();
                bot.mouseMove(x,y - sensitivity);
            }
               sleep(50);
        }

    }
    /**
     * Close the connection to the MBED.
     */
    public void finish()
    {
        mbed.close();
    }
    
    /**
     * A simple support method for sleeping the program.
     * @param millis The number of milliseconds to sleep for.
     */
    private void sleep(long millis)
    {
        try {
            Thread.sleep(millis);
        } 
        catch (InterruptedException ex) {
            // Nothing we can do.
        }
    }
    
        /**
     * A function that rounds doubles.
     * @param the value to be rounded
     * @param the number of decimal places to be rounded to.
     */
    public static double round(double value, int places) {
        assert places >= 0;
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
   }
    
}
