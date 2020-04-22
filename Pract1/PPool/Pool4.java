// CSD feb 2013 Juansa Sendra0

public class Pool4 extends Pool { //kids cannot enter if there are instructors waiting to exit
    int numKids = 0;
    int numInstruct = 0;
    int rest = 0;
    int k;
    int max;
    public void init(int ki, int cap) {k = ki; max = cap;}
    public synchronized void kidSwims() throws InterruptedException{
         while (numInstruct == 0 || numKids>=k*numInstruct || (numKids + numInstruct) == max || rest > 0){
            log.waitingToSwim();//para visualizar la posicion del nadador
            wait();
         }
         numKids++;
         
         notifyAll();
         log.swimming();
     }
    public synchronized void kidRests() throws InterruptedException{
        numKids--;
        
        notifyAll();
        log.resting();
    }
    public synchronized void instructorSwims() throws InterruptedException {
        while(numKids+numInstruct>=max){
            log.waitingToSwim();
            wait();
       }
        numInstruct++;
        
        notifyAll();
        log.swimming();
    }
    public synchronized void instructorRests() throws InterruptedException{
        rest++;
        notifyAll();
        while(numKids>0 && numInstruct==1 || numKids>k*(numInstruct-1)){
            log.waitingToRest();
            wait();
        }
        rest--;
        numInstruct--;
        
        notifyAll();
        log.resting();
    }
}
