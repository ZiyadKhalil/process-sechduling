package scheduling;

import java.util.Random;

public class Process {
    private static int idCounter=0;
    private static Random random = new Random(0);
    private int id;
    private int arrivalTime;
    private int runTime;
    private int priority;
    private int originalPriority;
    private int remainingTime;
    private int waitTime=0;
    private String name;
    private int turnAroundTime=0;
    private boolean didResponse; //flag to set the response time for the first time the process is run
    private int responseTime = 0;
    private static int firstPriorityCounter=0;
    private static int secondPriorityCounter=0;
    private static int thirdPriorityCounter=0;
    private static int fourthPriorityCounter=0;
    private static int processesRunCounter=0;
    private boolean aging = false;

    public boolean isAging() {
        return aging;
    }

    private int agingCounter=0;
    private Scheduler scheduler=null;
    private static int randomSeed;

    public Process() {
        didResponse=false;
        this.id = idCounter;
        this.name = "P"+id;
        idCounter++;
        this.arrivalTime = random.nextInt(100);
        this.runTime = random.nextInt(10)+1;
        this.priority = random.nextInt(4)+1;
        this.originalPriority=priority;
        this.remainingTime=runTime;
        this.waitTime=0;
    }

    public static void setRandomSeed(int randomSeed) {
       random.setSeed(randomSeed);
    }

    public static int getProcessesRunCounter() {
        return processesRunCounter;
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


    public void run(int quantaCounter) {
        if(!didResponse){
            calculateResponseTime(quantaCounter);
            processesRunCounter++;
            switch(priority){
                case 1:
                    firstPriorityCounter++;
                    break;
                case 2:
                    secondPriorityCounter++;
                    break;
                case 3:
                    thirdPriorityCounter++;
                    break;
                case 4:
                    fourthPriorityCounter++;
                    break;

            }
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
        if(aging&&priority!=1){
            agingCounter++;
            if(agingCounter==5){
                priority--;
                agingCounter=0;
                if(scheduler!=null){
                    scheduler.changeProcessPriority(this);
                }
            }
        }
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

    public int getRunTime() {
        return runTime;
    }

    public int getPriority() {
        return priority;
    }

//    public static void incrementPriorityProcessesCounter(int priority){
//        switch (priority){
//            case 1:
//                firstPriorityCounter++;
//                break;
//            case 2:
//                secondPriorityCounter++;
//                break;
//            case 3:
//                thirdPriorityCounter++;
//                break;
//            case 4:
//                fourthPriorityCounter++;
//                break;
//        }
//    }


    public void setAging(boolean aging) {
        this.aging = aging;
    }

    public void injectScheduler(Scheduler scheduler) {
        this.scheduler=scheduler;
    }

    public void clear() {
        this.aging=false;
        this.agingCounter=0;
        this.waitTime=0;
        this.remainingTime=runTime;
        this.scheduler=null;
        this.priority=originalPriority;
    }

    public boolean didResponse() {
        return didResponse;
    }

    public static int getFirstPriorityCounter() {
        return firstPriorityCounter;
    }

    public static int getSecondPriorityCounter() {
        return secondPriorityCounter;
    }

    public static int getThirdPriorityCounter() {
        return thirdPriorityCounter;
    }

    public static int getFourthPriorityCounter() {
        return fourthPriorityCounter;
    }

    public static void setID() {
        Process.idCounter=0;
    }

    public static void reset() {
        Process.firstPriorityCounter=0;
        Process.secondPriorityCounter=0;
        Process.thirdPriorityCounter=0;
        Process.fourthPriorityCounter=0;
        Process.processesRunCounter=0;
    }
}
