package org.eclipse.tracecompass.incubator.internal.jpftrace.core.analysis;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.tmf.core.util.Pair;

@SuppressWarnings({ "nls", "javadoc" })
public interface Attributes {

    /* First-level attributes */
    String CPUS = "CPUs";
    String THREADS = "Threads";

    /* Sub-attributes of the CPU nodes */
    String CURRENT_THREAD = "Current_thread";
    String SOFT_IRQS = "Soft_IRQs";
    String IRQS = "IRQs";
    String CURRENT_FREQUENCY = "Frequency";
    String MIN_FREQUENCY = "Min frequency";
    String MAX_FREQUENCY = "Max frequency";

    /* Sub-attributes of the Thread nodes */
    String CURRENT_CPU_RQ = "Current_cpu_rq";
    String PPID = "PPID";
    String PID = "PID";
    String EXEC_NAME = "Exec_name";

    String PRIO = "Prio";
    String SYSTEM_CALL = "System_call";

    /* Misc stuff */
    String UNKNOWN = "Unknown";
    String THREAD_0_PREFIX = "0_";
    String THREAD_0_SEPARATOR = "_";

    /* Defined Jpf Thread States */
    String EXPOSE = "Expose";
    String LOCK = "Lock";

    public static @Nullable String buildThreadAttributeName(int threadId, @Nullable Integer cpuId) {
        if (threadId == 0) {
            if (cpuId == null) {
                return null;
            }
            return Attributes.THREAD_0_PREFIX + String.valueOf(cpuId);
        }

        return String.valueOf(threadId);
    }

    public static Pair<Integer, Integer> parseThreadAttributeName(String threadAttributeName) {
        Integer threadId = -1;
        Integer cpuId = -1;

        try {
            if (threadAttributeName.startsWith(Attributes.THREAD_0_PREFIX)) {
                threadId = 0;
                String[] tokens = threadAttributeName.split(Attributes.THREAD_0_SEPARATOR);
                cpuId = Integer.parseInt(tokens[1]);
            } else {
                threadId = Integer.parseInt(threadAttributeName);
            }
        } catch (NumberFormatException e1) {
            // pass
        }

        return new Pair<>(threadId, cpuId);
    }
}
