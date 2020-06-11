package org.eclipse.tracecompass.incubator.internal.jpftrace.core.event;

public interface IJpfTraceConstants {

    String TRANSITION_ID = "transitionId"; 

    String BASE_TIME = "time";

    String THREAD_INFO = "threadInfo";

    String THREAD_NAME = "threadName";

    String THREAD_ID = "threadId";

    String THREAD_ENTRY_METHOD = "threadEntryMethod";

    String THREAD_STATE = "threadState";

    String TRANSITIONS = "transitions";

    String CHOICE_ID = "choiceId"; 

    String NUM_CHOICES = "numChoices";

    String CURRENT_CHOICE = "currentChoice";
    
    String CHOICES = "choices";
    
    String STEPS = "steps";

    String NUM_STEPS = "numSteps";

    String INSTRUCTIONS = "instructions";

    String STEP_LOCATION = "stepLocation";

    String FILE_LOCATION = "fileLocation";

    String IS_VIRTUAL_INV = "isVirtualInv";

    String IS_FIELD_INST = "isFieldInst";

    String IS_LOCK_INST = "isLockInst";

    String LOCK_FIELD_NAME = "lockFieldName";

    String IS_JVM_INVOK = "isJVMInvok";

    String IS_JVM_RETURN = "isJVMReturn";

    String IS_SYNCHRONIZED = "isSynchronized";

    String SYNC_METHOD_NAME = "syncMethodName";

    String IS_METHOD_RETURN = "isMethodReturn";

    String RETURNED_METHOD_NAME = "returndMethodName";

    String IS_METHOD_CALL = "isMethodCall";

    String CALLED_METHOD_NAME = "calledMethodName";

    String IS_THREAD_RELATED_METHOD = "isThreadRelatedMethod";

    String THREAD_RELATED_METHOD = "threadRelatedMethod";

    String IS_FIELD_ACCESS = "isFieldAccess";

    String ACCESSED_FIELD = "accessedField"; 

    String TYPE = "type";

    String PREV_PID = "prevTid";

    String PREV_COMM = "prevThreadName";

    String PREV_PRIO = "prev_prio";

    String PREV_STATE = "prevState";

    String NEXT_PID = "nextTid";

    String NEXT_COMM = "nextThreadName";

    String NEXT_PRIO = "next_prio";

    String COMM = "currentThreadName";

    String TID = "tid";

    String PRIO = "prio";

    String SUCCESS = "success";

    String TARGET_CPU = "target_cpu";

    String DURATION = "duration";

    String THREAD_SWITCH = "switchThread";
}
