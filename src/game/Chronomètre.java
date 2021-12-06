package game;

public class Chronomètre {
    private long begin;
    private long end;
    private long current;
    private int limite;

    public Chronomètre(int limite) {
        //intialisation
        this.limite=limite;
        begin=0;
        end=System.currentTimeMillis()+limite;
        current=System.currentTimeMillis();

    }
    
    public void start(){
        begin = System.currentTimeMillis();
        end=System.currentTimeMillis()+limite;
    }
 
    public void stop(){
        end = System.currentTimeMillis();
        // end = current;
    }
 
    public long getTime() {
        return end-begin;
    }
 
    public long getMilliseconds() {
        return end-begin;
    }
 
    public int getSeconds() {
         return (int) ((end - begin) / 1000.0);
    }
 
    public double getMinutes() {
        return (end - begin) / 60000.0;
    }
 
    public double getHours() {
        return (end - begin) / 3600000.0;
    }
    
    public int getRemaining(){
        current = System.currentTimeMillis();
        return (int) ((end - current) / 1000.0);
    }
    /**
    * Method to know if it remains time.
    */
    public boolean remainsTime() {
        current = System.currentTimeMillis();
        int timeSpent;
        timeSpent = (int) ((current - begin)/1000.0);
        //  System.out.println("time spent:"+timeSpent+"< limite: "+limite );
        return (timeSpent*1000) < limite;
    }
     
}
