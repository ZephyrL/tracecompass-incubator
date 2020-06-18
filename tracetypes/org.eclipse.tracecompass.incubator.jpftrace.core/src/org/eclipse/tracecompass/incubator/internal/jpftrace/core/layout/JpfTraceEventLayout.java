package org.eclipse.tracecompass.incubator.internal.jpftrace.core.layout;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.analysis.os.linux.core.trace.DefaultEventLayout;
// import org.eclipse.tracecompass.incubator.internal.jpt.core.event.IJpfTraceConstants;

public class JpfTraceEventLayout extends DefaultEventLayout {
    private static final @NonNull String NEXT_TID = "nextTid"; //$NON-NLS-1$
    private static final @NonNull String PREV_TID = "prevTid"; //$NON-NLS-1$
    private static final @NonNull String PREV_COMM = "prevThreadName";
    private static final @NonNull String NEXT_COMM = "nextThreadName";
    private static final @NonNull String COMM = "currentThreadName";
    private static final @NonNull String TID = "tid"; //$NON-NLS-1$
    private static final @NonNull String PREV_STATE = "prevState";
    // private static final @NonNull String CHILD_TID = "child_pid"; //$NON-NLS-1$
    private static final @NonNull String CPU_FREQUENCY = "cpu_frequency"; //$NON-NLS-1$

    // event types 
    private static final @NonNull String THREAD_SWITCH = "ThreadSwitch";
    private static final @NonNull String THREAD_START = "ThreadAwake";
    private static final @NonNull String THREAD_INFO = "ThreadInfo";
    private static final @NonNull String CHOICE_INFO = "ChoiceInfo";
    private static final @NonNull String INSTRUCTION = "Instruction";

    private static final @NonNull String LOCK = "LOCK";
    private static final @NonNull String EXPOSE = "EXPOSE";
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
        return NEXT_TID;
    }

    @Override
    public String fieldNextComm() {
        return NEXT_COMM;
    }

    @Override
    public String fieldPrevTid() {
        return PREV_TID;
    }    
    
    @Override
    public String fieldPrevComm() {
        return PREV_COMM;
    }

    @Override
    public String fieldComm() {
        return COMM;
    }

    @Override
    public String fieldTid() {
        return TID;
    }

    @Override
    public String fieldParentTid() {
        return TID;
    }

    @Override
    public String fieldPrevState() {
        return PREV_STATE;
    }

    @Override
    public @NonNull String eventCpuFrequency() {
        return CPU_FREQUENCY;
    }

    @Override 
    public @NonNull String eventSchedSwitch() {
        return THREAD_SWITCH;
    }

    @Override
    public @NonNull String eventSchedProcessWakeup() {
        return THREAD_START;
    }

    @Override
    public @NonNull String eventSchedProcessWaking() {
        return THREAD_START;
    }
    
    public @NonNull String eventThreadInfo() {
        return THREAD_INFO;
    }

    public @NonNull String eventChoiceInfo() {
        return CHOICE_INFO;
    }

    public @NonNull String eventInstruction() {
        return INSTRUCTION;
    }

    // TODO: remove following
    public @NonNull String eventThreadLock() {
        return LOCK;
    }

    public @NonNull String eventThreadExpose() {
        return EXPOSE;
    }
}