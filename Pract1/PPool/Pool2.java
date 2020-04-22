// CSD feb 2015 Juansa Sendra

public class Pool2 extends Pool{ //max kids/instructor
    int numKids = 0;
    int numInstruct = 0;
    int k;
    public void init(int ki, int cap)           {k = ki;}
    public synchronized void kidSwims() throws InterruptedException{
         while (numInstruct == 0 || numKids>=k*numInstruct){
            log.waitingToSwim();//para visualizar la posicion del nadador
            wait();
            }
         numKids++;
         notifyAll();
         log.swimming();
        }
    public synchronized void kidRests() throws InterruptedException{numKids--;notifyAll();log.resting();}
    public synchronized void instructorSwims() throws InterruptedException {numInstruct++;notifyAll();log.swimming();}
    public synchronized void instructorRests() throws InterruptedException{ 
        while(numKids>0 && numInstruct==1 || numKids>k*(numInstruct-1)){
        log.waitingToRest();
        wait();
       }
       numInstruct--;
       notifyAll();
       log.resting();
    }
}
