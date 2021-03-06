package scheduling;

public enum SchedulingType {
    FIRST_COME_FIRST_SERVE,
    SHORTEST_JOB_FIRST,
    SHORTEST_REMAINING_TIME_FIRST,
    ROUND_ROBIN,
    HIGHEST_PRIORITY_FIRST_NON_PREEMPTIVE,
    HIGHEST_PRIORITY_FIRST_PREEMPTIVE,
    HIGHEST_PRIORITY_FIRST_NON_PREEMPTIVE_WITH_AGING,
    HIGHEST_PRIORITY_FIRST_PREEMPTIVE_WITH_AGING
}
