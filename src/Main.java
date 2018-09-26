import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import scheduling.Process;
import scheduling.Scheduler;
import scheduling.SchedulingType;
import static scheduling.SchedulingType.*;

public class Main {
    private static int NUMBER_OF_PROCESSES=30;
    private static int firstWaitTime=0;
    private static int firstTurnaroundTime=0;
    private static int firstResponseTime =0;
    private static int secondWaitTime=0;
    private static int secondTurnaroundTime=0;
    private static int secondResponseTime =0;
    private static int thirdWaitTime=0;
    private static int thirdTurnaroundTime=0;
    private static int thirdResponseTime =0;
    private static int fourthWaitTime=0;
    private static int fourthTurnaroundTime=0;
    private static int fourthResponseTime =0;


    private static boolean first=true;

    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter("output.txt");
        ArrayList<Process> processes = new ArrayList<>();
        //CREATING PROCESSES
        //RUN1

        for (int i = 1; i <=5 ; i++) {
            Process.setRandomSeed(i);
            printOutput(i,processes,fw,FIRST_COME_FIRST_SERVE);
            Process.setID();
            Process.reset();
            Process.setRandomSeed(i);
            printOutput(i,processes,fw,SHORTEST_JOB_FIRST);
            Process.setID();
            Process.reset();
            Process.setRandomSeed(i);
            printOutput(i,processes,fw,SHORTEST_REMAINING_TIME_FIRST);
            Process.setID();
            Process.reset();
            Process.setRandomSeed(i);
            printOutput(i,processes,fw,ROUND_ROBIN);
            Process.setID();
            Process.reset();
            Process.setRandomSeed(i);
            printOutput(i,processes,fw,HIGHEST_PRIORITY_FIRST_NON_PREEMPTIVE);
            Process.setID();
            Process.reset();
            Process.setRandomSeed(i);
            printOutput(i,processes,fw,HIGHEST_PRIORITY_FIRST_PREEMPTIVE);
            Process.setID();
            Process.reset();
            Process.setRandomSeed(i);
            printOutput(i,processes,fw,HIGHEST_PRIORITY_FIRST_NON_PREEMPTIVE_WITH_AGING);
            Process.setID();
            Process.reset();
            Process.setRandomSeed(i);
            printOutput(i,processes,fw,HIGHEST_PRIORITY_FIRST_PREEMPTIVE_WITH_AGING);
            Process.setID();
            Process.reset();
            first=true;
            fw.write("\n\n\n\n");
        }
//        Process.setRandomSeed(0);
//        printOutput(1,processes,fw,HIGHEST_PRIORITY_FIRST_PREEMPTIVE);
        fw.close();
        //SORTING BASED ON ARRIVAL TIME


    }
    private static void clearProcesses(ArrayList<Process> processes){
        for (Process process:
                processes) {
            process.clear();
        }
    }
    private static void printOutput(int runNumber,ArrayList<Process> processes,FileWriter fw, SchedulingType schedulingType) throws IOException {
        processes.clear();
        for(int i=0;i<NUMBER_OF_PROCESSES;i++)
            processes.add(new Process());
       String schedulingAlgorithm;
       boolean hpf=false;
       switch(schedulingType){
           case FIRST_COME_FIRST_SERVE:
               schedulingAlgorithm="First Come First Serve:\n";
               break;
           case ROUND_ROBIN:
               schedulingAlgorithm="Round Robin:\n";
               break;
           case SHORTEST_JOB_FIRST:
               schedulingAlgorithm= "Shortest Job First:\n";
               break;
           case SHORTEST_REMAINING_TIME_FIRST:
               schedulingAlgorithm="Shortest Remaining Time First:\n";
               break;
           case HIGHEST_PRIORITY_FIRST_NON_PREEMPTIVE:
               schedulingAlgorithm="Highest Priority First (Non Preemptive):\n";
               hpf=true;
               break;
           case HIGHEST_PRIORITY_FIRST_PREEMPTIVE:
               schedulingAlgorithm="Highest Priority First (Preemptive):\n";
               hpf=true;
               break;
           case HIGHEST_PRIORITY_FIRST_NON_PREEMPTIVE_WITH_AGING:
               schedulingAlgorithm="Highest Priority First (Non Preemptive) WITH AGING:\n";
               break;
           case HIGHEST_PRIORITY_FIRST_PREEMPTIVE_WITH_AGING:
               schedulingAlgorithm="Highest Priority First (Preemptive) WITH AGING:\n";
               break;
               default:
                   schedulingAlgorithm="";
       }

        if (first) {
            fw.write("RUN#"+runNumber+"\n");
            fw.write("Processes: \n");
            //FIRST REQUIREMENT IN OUTPUT: Processes' details
            for(int i=0;i<NUMBER_OF_PROCESSES;i++)
                fw.write(processes.get(i).toString()+"\n");
            fw.write("\n");
            fw.flush();
            first=false;
        }


        processes.sort(Comparator.comparingInt(process -> process.getArrivalTime()));
        Scheduler scheduler = new Scheduler(processes);
        fw.write(schedulingAlgorithm);
        scheduler.run(schedulingType);

        fw.write("Time Chart:\n");
        //PRINTING TIME CHART
        String x="";
        for(int i=0;i<scheduler.getTimeChart().size();i++)
            x+=scheduler.getTimeChart().get(i)+"-";
        x=x.substring(0,x.length()-1);
        fw.write(x+"\n");
        int totalWaitTime=0;
        int totalTurnaroundTime=0;
        int totalResponseTime=0;
        firstWaitTime=0;
        firstTurnaroundTime=0;
        firstResponseTime=0;
        secondResponseTime=0;
        secondTurnaroundTime=0;
        secondWaitTime=0;
        thirdResponseTime=0;
        thirdTurnaroundTime=0;
        thirdWaitTime=0;
        fourthResponseTime=0;
        fourthTurnaroundTime=0;
        fourthWaitTime=0;
        for(int i=0;i<NUMBER_OF_PROCESSES;i++){
            if (processes.get(i).didResponse()) {
                totalWaitTime += processes.get(i).getWaitTime();
                totalTurnaroundTime += processes.get(i).getTurnAroundTime();
                totalResponseTime += processes.get(i).getResponseTime();

                if (processes.get(i).getPriority() == 1) {
                    firstWaitTime += processes.get(i).getWaitTime();
                    firstTurnaroundTime += processes.get(i).getTurnAroundTime();
                    firstResponseTime += processes.get(i).getResponseTime();
                } else if (processes.get(i).getPriority() == 2) {
                    secondWaitTime += processes.get(i).getWaitTime();
                    secondTurnaroundTime += processes.get(i).getTurnAroundTime();
                    secondResponseTime += processes.get(i).getResponseTime();
                } else if (processes.get(i).getPriority() == 3) {
                    thirdWaitTime += processes.get(i).getWaitTime();
                    thirdTurnaroundTime += processes.get(i).getTurnAroundTime();
                    thirdResponseTime += processes.get(i).getResponseTime();
                } else if (processes.get(i).getPriority() == 4) {
                    fourthWaitTime += processes.get(i).getWaitTime();
                    fourthTurnaroundTime += processes.get(i).getTurnAroundTime();
                    fourthResponseTime += processes.get(i).getResponseTime();
                }
            }
        }

        fw.write("Statistics:\n");
        //PRINTING REQUIRED OUTPUT FOR ONE RUN
        fw.write("Time Taken: "+scheduler.getTimeChart().size()+" quanta"+"\n");
        fw.write("Number of All Processes: " + NUMBER_OF_PROCESSES +"\n");
        fw.write("Number of Processes ran: " + Process.getProcessesRunCounter()+"\n");
        if(hpf){
            fw.write("First Queue:"+"\n");
            fw.write("      Avg Turnaround Time: "+firstTurnaroundTime/(float) Process.getFirstPriorityCounter()+"\n");
            fw.write("      Avg Wait Time: "+firstWaitTime/(float) Process.getFirstPriorityCounter()+"\n");
            fw.write("      Avg Response Time: "+firstResponseTime/(float) Process.getFirstPriorityCounter()+"\n");
            fw.write("      Throughput: "+Process.getFirstPriorityCounter()/(float)scheduler.getTimeChart().size()+" process per quantum"+"\n");


            fw.write("Second Queue:\n");
            fw.write("      Avg Turnaround Time: "+secondTurnaroundTime/(float) Process.getSecondPriorityCounter()+"\n");
            fw.write("      Avg Wait Time: "+secondWaitTime/(float) Process.getSecondPriorityCounter()+"\n");
            fw.write("      Avg Response Time: "+secondResponseTime/(float) Process.getSecondPriorityCounter()+"\n");
            fw.write("      Throughput: "+Process.getSecondPriorityCounter()/(float)scheduler.getTimeChart().size()+" process per quantum"+"\n");


            fw.write("Third Queue:\n");
            fw.write("      Avg Turnaround Time: "+thirdTurnaroundTime/(float) Process.getThirdPriorityCounter()+"\n");
            fw.write("      Avg Wait Time: "+thirdWaitTime/(float) Process.getThirdPriorityCounter()+"\n");
            fw.write("      Avg Response Time: "+thirdResponseTime/(float) Process.getThirdPriorityCounter()+"\n");
            fw.write("      Throughput: "+Process.getThirdPriorityCounter()/(float)scheduler.getTimeChart().size()+" process per quantum"+"\n");


            fw.write("Fourth Queue:\n");
            fw.write("      Avg Turnaround Time: "+fourthTurnaroundTime/(float) Process.getFourthPriorityCounter()+"\n");
            fw.write("      Avg Wait Time: "+fourthWaitTime/(float) Process.getFourthPriorityCounter()+"\n");
            fw.write("      Avg Response Time: "+fourthResponseTime/(float) Process.getFourthPriorityCounter()+"\n");
            fw.write("      Throughput: "+Process.getFourthPriorityCounter()  /(float)scheduler.getTimeChart().size()+" process per quantum"+"\n");

            fw.write("Overall: \n");
        }
        fw.write("Avg Turnaround Time: "+(totalTurnaroundTime/ (float) Process.getProcessesRunCounter())+"\n");
        fw.write("Avg Wait Time: "+(totalWaitTime/(float)Process.getProcessesRunCounter())+"\n");
        fw.write("Avg Response Time: "+(totalResponseTime/(float)Process.getProcessesRunCounter())+"\n");
        fw.write("Throughput: "+Process.getProcessesRunCounter()/(float)scheduler.getTimeChart().size()+" process per quantum"+"\n");
        fw.write("\n\n");
        fw.flush();

    }
}
