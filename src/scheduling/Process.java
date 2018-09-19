package scheduling;

import java.util.Random;

public class Process {
    private static int idCounter=0;
    private static Random random = new Random();
    private int id;
    private int arrivalTime;
    private int runTime;
    private int priority;
    private int remainingTime;
    private int waitTime;
    private String name;
    private int turnAroundTime;
    private boolean didResponse; //flag to set the response time for the first time the process is run
    private int responseTime;

    public Process() {
        didResponse=false;
        this.id = idCounter;
        this.name = "P"+id;
        idCounter++;
        this.arrivalTime = random.nextInt(100);
        this.runTime = random.nextInt(10)+1;
        this.priority = random.nextInt(4)+1;
        this.remainingTime=runTime;
        this.waitTime=0;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", arrivalTime=" + arrivalTime +
                ", runTime=" + runTime +
                ", priority=" + priority +
                '}';
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void run(int quantaCounter) {
        if(!didResponse){
            calculateResponseTime(quantaCounter);
            didResponse=true;
        }
        this.remainingTime--;
    }

    private void calculateResponseTime(int quantaCounter) {
        this.responseTime=quantaCounter-arrivalTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void incrementWaitTime() {
        waitTime++;
    }

    public void calculateTurnAroundTime(int quantaCounter) {
        this.turnAroundTime =quantaCounter-arrivalTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public int getResponseTime() {
        return responseTime;
    }
}
