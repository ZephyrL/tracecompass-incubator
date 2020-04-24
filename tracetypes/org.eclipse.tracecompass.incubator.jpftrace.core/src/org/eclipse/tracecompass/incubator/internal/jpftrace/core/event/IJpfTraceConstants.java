package org.eclipse.tracecompass.incubator.internal.jpftrace.core.event;

public interface IJpfTraceConstants {

    String TRACE_ID = "traceID"; 

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
    /**
     * spanID field name
     */
    String SPAN_ID = "spanID"; 
    /**
     * flags field name (specific to jaeger tracer)
     */
    String FLAGS = "flags"; 
    /**
     * operationName field name
     */
    String OPERATION_NAME = "operationName"; 
    /**
     * references field name
     */
    String REFERENCES = "references"; 
    /**
     * reference type field name
     */
    String REFERENCE_TYPE = "refType"; 
    /**
     * startTime field name
     */
    String START_TIME = "startTime"; 
    /**
     * duration field name
     */
    String DURATION = "duration"; 
    /**
     * tags field name
     */
    String TAGS = "tags"; 
    /**
     * logs field name
     */
    String LOGS = "logs"; 
    /**
     * processID field name
     */
    String PROCESS_ID = "processID"; 
    /**
     * processID field name
     */
    String PROCESS_NAME = "processName"; 
    /**
     * service field name
     */
    String SERVICE_NAME = "serviceName"; 
    /**
     * process tags field name
     */
    String PROCESS_TAGS = "processTags"; 
    /**
     * key field name
     */
    String KEY = "key"; 
    /**
     * value field name
     */
    String VALUE = "value"; 
    /**
     * timestamp field name
     */
    String TIMESTAMP = "timestamp"; 
    /**
     * fields field name
     */
    String FIELDS = "fields"; 

}
