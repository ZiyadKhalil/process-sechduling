import java.util.ArrayList;
import java.util.Comparator;
import scheduling.Process;
import scheduling.Scheduler;
import scheduling.SchedulingType;
import static scheduling.SchedulingType.*;

public class Main {
    public static void main(String[] args){
        ArrayList<Process> processes = new ArrayList<>();
        //CREATING PROCESSES
        for(int i=0;i<50;i++)
            processes.add(new Process());

        //SORTING BASED ON ARRIVAL TIME
        processes.sort(Comparator.comparingInt(process -> process.getArrivalTime()));


        //FIRST REQUIREMENT IN OUTPUT: Processes' details
        for(int i=0;i<50;i++)
            System.out.println(processes.get(i));

        Scheduler scheduler = new Scheduler(processes);
        SchedulingType schedulingType = ROUND_ROBIN;
        scheduler.run(schedulingType);


        //PRINTING TIME CHART
        for(int i=0;i<scheduler.getTimeChart().size();i++)
            System.out.println(String.format("%d : %s",i,scheduler.getTimeChart().get(i)));


        int totalWaitTime=0;
        int totalTurnaroundTime=0;
        int totalResponseTime=0;
        for(int i=0;i<50;i++){
            totalWaitTime+=processes.get(i).getWaitTime();
            totalTurnaroundTime+=processes.get(i).getTurnAroundTime();
            totalResponseTime+=processes.get(i).getResponseTime();
        }


        //PRINTING REQUIRED OUTPUT FOR ONE RUN
        System.out.println("Avg Turnaround Time:"+(totalTurnaroundTime/50.0));
        System.out.println("Avg Wait Time:"+(totalWaitTime/50.0));
        System.out.println("Avg Response Time:"+(totalResponseTime/50.0));
    }
}
