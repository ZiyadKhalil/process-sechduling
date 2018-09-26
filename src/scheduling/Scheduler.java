package scheduling;

import scheduling.Process;
import scheduling.SchedulingType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class Scheduler {
    private ArrayList<Process> processes;
    private ArrayList<String> timeChart;
    private ArrayList<Process> queue;
    private ArrayList<Process> p1queue;
    private ArrayList<Process> p2queue;
    private ArrayList<Process> p3queue;
    private ArrayList<Process> p4queue;
    private int p1RoundIndex = 0;
    private int p2RoundIndex = 0;
    private int p3RoundIndex = 0;
    private int p4RoundIndex = 0;
    private int numberOfProcessesRun;

    private int quantaCounter;
    private int numberOfFirstProcessesRun;
    private int numberOfSecondProcessesRun;
    private int numberOfFourthProcessesRun;
    private int numberOfThirdProcessesRun;
    private int roundIndex;

    public Scheduler(ArrayList processes) {
        this.processes = processes;
        timeChart = new ArrayList();
        queue = new ArrayList();
        p1queue = new ArrayList();
        p2queue = new ArrayList();
        p3queue = new ArrayList();
        p4queue = new ArrayList();
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
                break;
            case ROUND_ROBIN:
                roundRobin();
                break;
            case HIGHEST_PRIORITY_FIRST_NON_PREEMPTIVE:
                highestPriorityFirstNonPreemptive(false);
                break;
            case HIGHEST_PRIORITY_FIRST_PREEMPTIVE:
                highestPriorityFirstPreemptive(false);
                break;
            case HIGHEST_PRIORITY_FIRST_NON_PREEMPTIVE_WITH_AGING:
                highestPriorityFirstNonPreemptive(true);
                break;
            case HIGHEST_PRIORITY_FIRST_PREEMPTIVE_WITH_AGING:
                highestPriorityFirstPreemptive(true);
                break;
        }
    }

    private void firstComeFirstServe() {
        quantaCounter = 0;
        Process currentProcess=null;
        timeChart.clear();
        int i =0;
        while (true){
            for (; i < processes.size() && processes.get(i).getArrivalTime() == quantaCounter && quantaCounter < 100; i++)
                queue.add(processes.get(i));
            if (quantaCounter==100) {
                ArrayList<Process> deletedProcesses = new ArrayList<>();
                for (Process process :
                        queue) {
                    if (!process.didResponse()){
                        deletedProcesses.add(process);
                    }
                }
                queue.removeAll(deletedProcesses);
                numberOfProcessesRun=processes.size()-deletedProcesses.size();
            }
            if(queue.isEmpty()&&quantaCounter>99) {
                break;
            }

            if(!queue.isEmpty()){
                if(currentProcess==null)
                    currentProcess=queue.get(0);
                currentProcess.run(quantaCounter);
                timeChart.add(currentProcess.getName());
                for (Process process:
                        queue) {
                    if(process==currentProcess)
                        continue;
                    process.incrementWaitTime();
                }
                if(currentProcess.getRemainingTime()==0){
                    currentProcess.calculateTurnAroundTime(quantaCounter);
                    queue.remove(currentProcess);
                    currentProcess=null;
                }
            }
            else
                timeChart.add("idle");
            quantaCounter++;
        }
    }
    private void shortestJobFirst() {
        quantaCounter = 0;
        Process currentProcess=null;
        ArrayList<Process> deletedProcesses = new ArrayList<>();
        timeChart.clear();
        int i =0;
        while (true){
            for (; i < processes.size() && processes.get(i).getArrivalTime() == quantaCounter && quantaCounter < 100; i++)
                queue.add(processes.get(i));
            if(quantaCounter==100){
                for (Process process  :
                        queue) {
                    if(!process.didResponse()){
                        deletedProcesses.add(process);
                    }
                }
                queue.removeAll(deletedProcesses);
                numberOfProcessesRun=processes.size()-deletedProcesses.size();
            }
            if(queue.isEmpty()&&quantaCounter>99) {
                break;
            }
            if(!queue.isEmpty()){
                if(currentProcess==null) {
                    queue.sort(Comparator.comparingInt(process -> process.getRunTime()));
                    currentProcess=queue.get(0);
                }
                currentProcess.run(quantaCounter);
                timeChart.add(currentProcess.getName());
                for (Process process:
                        queue) {
                    if(process==currentProcess)
                        continue;
                    process.incrementWaitTime();
                }
                if(currentProcess.getRemainingTime()==0){
                    currentProcess.calculateTurnAroundTime(quantaCounter);
                    queue.remove(currentProcess);
                    currentProcess=null;
                }
            }
            else
                timeChart.add("idle");

            quantaCounter++;
        }

    }
    private void shortestRemainingJobFirst() {
        quantaCounter = 0;
        Process currentProcess;
        timeChart.clear();
        int i=0;
        while (true) {
            for (; i < processes.size() && processes.get(i).getArrivalTime() == quantaCounter && quantaCounter < 100; i++)
                queue.add(processes.get(i));
            if(quantaCounter==100){
                ArrayList<Process> deletedProcesses = new ArrayList<>();
                for (Process process  :
                        queue) {
                    if(!process.didResponse()){
                        deletedProcesses.add(process);
                    }
                }
                queue.removeAll(deletedProcesses);
                numberOfProcessesRun=processes.size()-deletedProcesses.size();
            }
            queue.sort(Comparator.comparingInt(process -> process.getRemainingTime()));
            if(queue.isEmpty()&&quantaCounter>99) {
                break;
            }
            if(!queue.isEmpty()) {
                currentProcess=queue.get(0);
                timeChart.add(currentProcess.getName());
                currentProcess.run(quantaCounter);
                for (Process process:
                        queue) {
                    if(process==currentProcess)
                        continue;
                    process.incrementWaitTime();
                }
                if(currentProcess.getRemainingTime()==0){
                    currentProcess.calculateTurnAroundTime(quantaCounter);
                    queue.remove(currentProcess);
                }
            } else{
                timeChart.add("idle");
            }
            if(queue.isEmpty()&&quantaCounter>99) {
                break;
            }
            quantaCounter++;
        }

    }
    private void roundRobin() {
        quantaCounter = 0;
        int roundSlice = 1;
        int roundIndex = 0;
        timeChart.clear();
        Process currentProcess;
        int i=0;
        while (true){
            for(;i<processes.size()&&processes.get(i).getArrivalTime()==quantaCounter && quantaCounter < 100;i++)
                queue.add(processes.get(i));
            if(quantaCounter==100){
                ArrayList<Process> deletedProcesses=new ArrayList<>();
                for (Process process  :
                        queue) {
                    if(!process.didResponse()){
//                        if(queue.indexOf(process)<=roundIndex)
//                            roundIndex--;
                        deletedProcesses.add(process);
                    }
                }
                queue.removeAll(deletedProcesses);
                roundIndex=0;
                numberOfProcessesRun=0;
            }
            if(queue.isEmpty()&&quantaCounter>99) {
                break;
            }
            if(!queue.isEmpty()){

//                System.out.println("size: " +queue.size());
//                System.out.println(roundIndex);
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
                roundIndex++;
                if(queue.size()==roundIndex)
                    roundIndex=0;

            }
            else
                timeChart.add("idle");

            quantaCounter++;
        }
    }
    private void highestPriorityFirstNonPreemptive(boolean aging) {
        quantaCounter = 0;
        Process currentProcess=null;
        timeChart.clear();
        int i =0;
        while (true){
            for (; i < processes.size() && processes.get(i).getArrivalTime() == quantaCounter && quantaCounter < 100; i++) {
                if(aging)
                    processes.get(i).setAging(true);
                queue.add(processes.get(i));
            } if(quantaCounter==100){
                ArrayList<Process> deletedProcesses=new ArrayList<>();
                for (Process process  :
                        queue) {
                    if(!process.didResponse()){
                        switch (process.getPriority()){
                            case 1:
                                deletedProcesses.add(process);
                                break;
                            case 2:
                                deletedProcesses.add(process);


                                break;
                            case 3:
                                deletedProcesses.add(process);
                                break;
                            case 4:
                                deletedProcesses.add(process);
                                break;
                        }
                    }
                }
                queue.removeAll(deletedProcesses);
            }
            if(queue.isEmpty()&&quantaCounter>99) {
                break;
            }
            if(!queue.isEmpty()) {
                    if (currentProcess == null) {
                        queue.sort((o1, o2) -> {
                            if(o1.getPriority()<o2.getPriority())
                                return -1;
                            else if(o1.getPriority()>o2.getPriority())
                                return 1;
                            else
                                return Integer.compare(o1.getArrivalTime(), o2.getArrivalTime());
                        });
                        currentProcess = queue.get(0);
                    }
                    currentProcess.run(quantaCounter);
                    timeChart.add(currentProcess.getName());
                    for (Process process:
                            queue) {
                        if(process==currentProcess)
                            continue;
                        process.incrementWaitTime();
                    }
                    if(currentProcess.getRemainingTime()==0){
                        currentProcess.calculateTurnAroundTime(quantaCounter);
                        queue.remove(currentProcess);
                        currentProcess=null;
                    }
                }
                else
                    timeChart.add("idle");

            quantaCounter++;
            }
    }
    private void highestPriorityFirstPreemptive(boolean aging) {
        ArrayList<Process> allQueues = new ArrayList<>();
        quantaCounter = 0;
        int roundSlice = 1;
         roundIndex = 0;
        p1RoundIndex=0;
        p2RoundIndex=0;
        p3RoundIndex=0;
        p4RoundIndex=0;
        p1queue.clear();
        p2queue.clear();
        p3queue.clear();
        p4queue.clear();
        timeChart.clear();
        Process currentProcess;

        int i=0;
        while (true){
            for (; i < processes.size() && processes.get(i).getArrivalTime() == quantaCounter && quantaCounter < 100; i++) {
                processes.get(i).setAging(aging);
                if(aging) {
                    processes.get(i).injectScheduler(this);
                }
                switch (processes.get(i).getPriority()){

                    case 1:
                        p1queue.add(processes.get(i));
                        break;
                    case 2:
                        p2queue.add(processes.get(i));
                        break;
                    case 3:
                        p3queue.add(processes.get(i));
                        break;
                    case 4:
                        p4queue.add(processes.get(i));
                        break;

                }
                allQueues.add(processes.get(i));
            }
            int p=0;

            if(quantaCounter==100){
                ArrayList<Process> deletedProcesses=new ArrayList<>();
                ArrayList<Process> firstDeletedProcesses = new ArrayList();
                ArrayList<Process> secondDeletedProcesses = new ArrayList();
                ArrayList<Process> thirdDeletedProcesses = new ArrayList();
                ArrayList<Process> fourthDeletedProcesses = new ArrayList();
                for (Process process  :
                        allQueues) {
                    if(!process.didResponse()){
                        switch (process.getPriority()){
                            case 1:
                                firstDeletedProcesses.add(process);
                                deletedProcesses.add(process);
                                break;
                            case 2:
                                secondDeletedProcesses.add(process);
                                deletedProcesses.add(process);


                                break;
                            case 3:
                                thirdDeletedProcesses.add(process);
                                deletedProcesses.add(process);
                                break;
                            case 4:
                                fourthDeletedProcesses.add(process);
                                deletedProcesses.add(process);
                                break;
                        }
                    }
                }
                allQueues.removeAll(deletedProcesses);
                p1queue.removeAll(firstDeletedProcesses);
                p2queue.removeAll(secondDeletedProcesses);
                p3queue.removeAll(thirdDeletedProcesses);
                p4queue.removeAll(fourthDeletedProcesses);
                p1RoundIndex=0;
                p2RoundIndex=0;
                p3RoundIndex=0;
                p4RoundIndex=0;
            }
            if(!p1queue.isEmpty()) {
                queue=p1queue;
                roundIndex=p1RoundIndex;
                p=1;
            } else if (!p2queue.isEmpty()) {
                queue=p2queue;
                roundIndex=p2RoundIndex;
                p=2;
            } else if (!p3queue.isEmpty()) {
                queue=p3queue;
                roundIndex=p3RoundIndex;
                p=3;
            } else if (!p4queue.isEmpty()) {
                queue=p4queue;
                roundIndex=p4RoundIndex;
                p=4;
            }
            if(allQueues.isEmpty()&&quantaCounter>99) {
                break;
            }
            if(!queue.isEmpty()){
                System.out.println(queue);
                System.out.println(roundIndex);
                System.out.println(p1RoundIndex);
                System.out.println(p2RoundIndex);
                System.out.println(p3RoundIndex);
                System.out.println(p4RoundIndex);
                System.out.println(quantaCounter);
                if(roundIndex==queue.size()) {

                    setRoundIndex(p,0);
                    roundIndex=0;
                }
                currentProcess=queue.get(roundIndex);
                System.out.println(currentProcess.isAging());
                currentProcess.run(quantaCounter);
                timeChart.add(currentProcess.getName());
                if(currentProcess.getRemainingTime()==0) {
                    queue.remove(currentProcess);
                    allQueues.remove(currentProcess);
                    currentProcess.calculateTurnAroundTime(quantaCounter);
                    roundIndex--;
                    setRoundIndex(currentProcess.getPriority(),roundIndex);
                }
                for (Process process:
                        allQueues) {
                    if(process==currentProcess) {
                        continue;
                    }
                    process.incrementWaitTime();
                }
                roundIndex++;
                setRoundIndex(currentProcess.getPriority(),roundIndex);
                if(queue.size()==roundIndex) {
                    setRoundIndex(currentProcess.getPriority(),0);
                    roundIndex=0;
                }

            }
            else {
                timeChart.add("idle");
            }
            quantaCounter++;
        }
    }


    private void setRoundIndex(int priority, int i) {

        switch (priority){
            case 1:
                p1RoundIndex=i;
                break;
            case 2:
                p2RoundIndex=i;
                break;
            case 3:
                p3RoundIndex=i;
                break;
            case 4:
                p4RoundIndex=i;
                break;
        }

    }
    public ArrayList<String> getTimeChart() {
        return timeChart;
    }
    public void changeProcessPriority(Process process) {
        switch (process.getPriority()){
            case 1:
                p2queue.remove(process);
                if(p2queue.size()==p2RoundIndex) {
                    p2RoundIndex=0;
                    roundIndex=0;
                }
                p1queue.add(process);
                break;
            case 2:
                p3queue.remove(process);
                if(p3queue.size()==p3RoundIndex) {
                    p3RoundIndex=0;
                    roundIndex=0;

                }
                p2queue.add(process);
                break;
            case 3:
                p4queue.remove(process);
                if(p4queue.size()==p4RoundIndex) {
                    p4RoundIndex=0;
                    roundIndex=0;
                }
                p3queue.add(process);
        }
    }

    public int getNumberOfProcessesRun() {
        return numberOfProcessesRun;
    }

    public int getNumberOfSecondProcessesRun() {
        return numberOfSecondProcessesRun;
    }

    public int getNumberOfFirstProcessesRun() {
        return numberOfFirstProcessesRun;
    }

    public int getNumberOfThirdProcessesRun() {
        return numberOfThirdProcessesRun;
    }

    public int getNumberOfFourthProcessesRun() {
        return numberOfFourthProcessesRun;
    }
}
