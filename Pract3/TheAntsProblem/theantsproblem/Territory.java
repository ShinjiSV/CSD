package theantsproblem;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.*;
import java.util.*;
public class Territory {
    private int tam; // Matrix size
    private boolean occupied[][];
    String description = "Basic Java Synchronization (Using Barriers)";
    private Log log;
    ReentrantLock lock = new ReentrantLock();
    Condition [][]ocupado;
    
    

    public String getDesc() {
        return description;
    }

    public Territory(int tamT, Log l) {
        tam = tamT;
        occupied = new boolean[tam][tam];
        ocupado = new Condition[tam][tam];
        log = l;
        // Initializing the matrix
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                occupied[i][j] = false;
                ocupado[i][j] = lock.newCondition();
            }
        }
    }

    public int getTam() {
        return tam;
    }

    public void putAnt(Ant h, int x, int y) {
        lock.lock();
        while (occupied[x][y]) {
            try {
                // Write in the log: ant waiting
                
                log.writeLog(LogItem.PUT, h.getid(), x, y, LogItem.WAITINS,
                        "Ant " + h.getid() + " waiting for [" + x + "," + y + "]");
                if(ocupado[x][y].await(10L,TimeUnit.MILLISECONDS)==false){
                    Random rnd = new Random();
                    x = (int) (rnd.nextDouble() * getTam());
                    y  = (int) (rnd.nextDouble() * getTam());
                }
            } catch (InterruptedException e) {
            } 
        }
        occupied[x][y] = true;
        h.setPosition(x, y);
        // Write in the log: ant inside territory
        log.writeLog(LogItem.PUT, h.getid(), x, y, LogItem.OK, "Ant " + h.getid() + " : [" + x + "," + y + "]  inside");
        ocupado[x][y].signalAll();
        lock.unlock();
    }

    public void takeAnt(Ant h) {
        lock.lock();
        int x = h.getX();
        int y = h.getY();
        occupied[x][y] = false;
        // Write in the log: ant outside territory
        log.writeLog(LogItem.TAKE, h.getid(), x, y, LogItem.OUT, "Ant " + h.getid() + " : [" + x + "," + y + "] out");
        ocupado[x][y].signalAll();
        lock.unlock();
    }

    public void moves(Ant h, int x1, int y1, int step) {
        int x = h.getX();
        int y = h.getY();
        lock.lock();
        while (occupied[x1][y1]) {
            try {
                // Write in the log: ant waiting
                log.writeLog(LogItem.MOVE, h.getid(), x1, y1, LogItem.WAIT,
                        "Ant " + h.getid() + " waiting for [" + x1 + "," + y1 + "]");
                if(ocupado[x1][y1].await(10L,TimeUnit.MILLISECONDS)==false){
                    h.nextMovement();
                    x1 = h.getNextPosX();
                    y1 = h.getNextPosY();
                }
            } catch (InterruptedException e) {
            }
        }
        occupied[x][y] = false;
        occupied[x1][y1] = true;
        h.setX(x1);
        h.setY(y1);
        // Write in the log: ant moving
        log.writeLog(LogItem.MOVE, h.getid(), x1, y1, LogItem.OK,
                "Ant " + h.getid() + " : [" + x + "," + y + "] -> [" + x1 + "," + y1 + "] step:" + step);
        
        ocupado[x][y].signalAll();
        lock.unlock();
    }
}
