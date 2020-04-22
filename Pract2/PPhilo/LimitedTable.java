// CSD Mar 2013 Juansa Sendra

public class LimitedTable extends RegularTable { //max 4 in dinning-room
    public LimitedTable(StateManager state) {super(state);}
    
    protected void illegal(String s) {System.out.println("ILLEGAL: "+s+" invoked"); System.exit(1);}

    public synchronized void begin(int id)  {state.begin(id);}
    public synchronized void ponder(int id) {state.ponder(id);}
    public synchronized void takeR(int id) throws InterruptedException {
        while (!state.rightFree(id)) {state.wtakeR(id); wait();}
        state.takeR(id);
    }
    public synchronized void takeL(int id) throws InterruptedException {
        while (!state.leftFree(id)) {state.wtakeL(id); wait();}
        state.takeL(id);
    }
    public synchronized void eat(int id)   {state.eat(id);}
    public synchronized void dropR(int id) {state.dropR(id); notifyAll();}
    public synchronized void dropL(int id) {state.dropL(id); notifyAll();}
    public synchronized void end(int id)   {state.end(id);}
    public synchronized void takeLR(int id) throws InterruptedException{illegal("RegularTable.takeLR");}
    private int n = 0;
    public synchronized void enter(int id) throws InterruptedException {
        while (n>=4){
            state.wenter(id);
            wait();
        }
        state.enter(id);
        n++;
        notifyAll();
    }
    public synchronized void exit(int id)  {
        state.exit(id);
        n--;
        notifyAll();
    }
}
