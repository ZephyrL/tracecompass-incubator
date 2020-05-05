package org.eclipse.tracecompass.incubator.internal.jpftrace.core.event;

public interface IJpfTraceConstants {

    String TRANSITION_ID = "transitionId"; 

    String BASE_TIME = "time";

    String THREAD_INFO = "threadInfo";

    String THREAD_NAME = "threadName";

    String THREAD_ID = "threadId";

    String TRANSITIONS = "transitions";

    String CHOICE_ID = "choiceId"; 

    String NUM_CHOICES = "numChoices";
    
    String CHOICES = "choices";

    String THREAD_STATE = "threadState";
    
    String STEP_LOCATIONS = "stepLocation";
     
    String STEPS = "steps";

    String SOURCES = "sources";

    String INSTRUCTIONS = "instructions";

    String STEP_LOCATION = "stepLocation";

    String FILE_LOCATION = "fileLocation";

    String IS_VIRTUAL_INV = "isVirtualInv";

    String IS_FIELD_INST = "isFieldInst";

    String IS_LOCK_INST = "isLockInst";

    String LOCK_FIELD_NAME = "lockFieldName";

    String IS_JVM_INVOK = "isJVMInvok";

    String IS_JVM_RETURN = "isJVMReturn";

    String TYPE = "type";

    String PREV_PID = "prev_pid";

    String PREV_COMM = "prev_comm";

    String PREV_PRIO = "prev_prio";

    String PREV_STATE = "prev_state";

    String NEXT_PID = "next_pid";

    String NEXT_COMM = "next_comm";

    String NEXT_PRIO = "next_prio";

    String COMM = "comm";

    String PID = "pid";

    String PRIO = "prio";

    String SUCCESS = "success";

    String TARGET_CPU = "target_cpu";

    String DURATION = "duration";
}
