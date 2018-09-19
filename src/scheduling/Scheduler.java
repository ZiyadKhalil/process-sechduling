package scheduling;

import scheduling.Process;
import scheduling.SchedulingType;

import java.util.ArrayList;

public class Scheduler {
    private ArrayList<Process> processes;
    private ArrayList<String> timeChart;
    private ArrayList<Process> queue;
    private int quantaCounter;
    public Scheduler(ArrayList processes) {
        this.processes = processes;
        timeChart = new ArrayList();
        queue = new ArrayList();
    }

    public void run(SchedulingType schedulingType) {
        switch (schedulingType){
            case FIRST_COME_FIRST_SERVE:
                firstComeFirstServe();
                break;
            case SHORTEST_JOB_FIRST:
                shortestJobFirst();
                break;
            case SHORTEST_REMAINING_TIME_FIRST:
                shortestRemainingJobFirst();
            case ROUND_ROBIN:
                roundRobin();
                break;
            case HIGHEST_PRIORITY_FIRST_NON_PREEMPTIVE:
                highestPriorityFirstNonPreemptive();
                break;
            case HIGHEST_PRIORITY_FIRST_PREEMPTIVE:
                highestPriorityFirstPreemptive();
        }
    }

    private void firstComeFirstServe() {

    }
    private void shortestJobFirst() {

    }
    private void shortestRemainingJobFirst() {

    }
    private void roundRobin() {
        quantaCounter = 0;
        int roundSlice = 1;
        int roundIndex = 0;
        Process currentProcess;
        int i=0;
        while (true){
            for(;i<processes.size()&&processes.get(i).getArrivalTime()==quantaCounter && quantaCounter < 100;i++)
                queue.add(processes.get(i));
            if(!queue.isEmpty()){
                currentProcess=queue.get(roundIndex);
                currentProcess.run(quantaCounter);
                timeChart.add(currentProcess.getName());
                if(currentProcess.getRemainingTime()==0) {
                    queue.remove(currentProcess);
                    currentProcess.calculateTurnAroundTime(quantaCounter);
                    roundIndex--;
                }
                for (Process process:
                     queue) {
                    if(process==currentProcess)
                        continue;
                    process.incrementWaitTime();
                }
                if(queue.size()-1==roundIndex)
                    roundIndex=0;
                else
                    roundIndex++;
            }
            else
                timeChart.add("idle");
            if(queue.isEmpty()&&quantaCounter>99)
                break;
            quantaCounter++;
        }
    }
    private void highestPriorityFirstNonPreemptive() {
    }
    private void highestPriorityFirstPreemptive() {

    }

    public ArrayList<String> getTimeChart() {
        return timeChart;
    }
}
