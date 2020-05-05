package org.eclipse.tracecompass.incubator.internal.jpftrace.core.layout;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.analysis.os.linux.core.trace.DefaultEventLayout;
// import org.eclipse.tracecompass.incubator.internal.jpt.core.event.IJpfTraceConstants;

public class JpfTraceEventLayout extends DefaultEventLayout {
    private static final @NonNull String NEXT_PID = "next_pid"; //$NON-NLS-1$
    private static final @NonNull String PREV_PID = "prev_pid"; //$NON-NLS-1$
    // private static final @NonNull String CHILD_TID = "child_pid"; //$NON-NLS-1$
    private static final @NonNull String TID = "pid"; //$NON-NLS-1$
    private static final @NonNull String CPU_FREQUENCY = "cpu_frequency"; //$NON-NLS-1$
    private static @Nullable JpfTraceEventLayout INSTANCE;

    public static synchronized @NonNull JpfTraceEventLayout getInstance() {
        JpfTraceEventLayout inst = INSTANCE;
        if (inst == null) {
            inst = new JpfTraceEventLayout();
            INSTANCE = inst;
        }
        return inst;
    }

    @Override
    public String fieldNextTid() {
        return NEXT_PID;
    }

    @Override
    public String fieldPrevTid() {
        return PREV_PID;
    }

    @Override
    public String fieldTid() {
        return TID;
    }

    @Override
    public String fieldParentTid() {
        return TID;
    }

    // @Override
    // public String fieldChildTid() {
    //     return CHILD_TID;
    // }

    @Override
    public @NonNull String eventCpuFrequency() {
        return CPU_FREQUENCY;
    }
}