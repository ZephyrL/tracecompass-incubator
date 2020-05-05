package org.eclipse.tracecompass.incubator.internal.jpftrace.core.event;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.tmf.core.event.ITmfEventField;
import org.eclipse.tracecompass.tmf.core.event.TmfEvent;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

/**
 * JPF trace span (event)
 *
 * @author Katherine Nadeau
 */
public class JpfTraceEvent extends TmfEvent {

    private final String fName;
    private final JpfTraceField fField;

    /**
     * Constructor for simple traceEventEvent
     *
     * @param trace
     *            the trace
     * @param rank
     *            the rank
     * @param field
     *            the event field, contains all the needed data
     */
    public JpfTraceEvent(ITmfTrace trace, long rank, JpfTraceField field) {
        super(trace, rank, trace.createTimestamp(field.getTimestamp()), JpfTraceEventTypeFactory.get(field.getName()), field.getContent()); //$NON-NLS-1$
        // super(trace, rank, trace.createTimestamp(field.getTimestamp()), JpfTraceEventTypeFactory.get("sched_switch"), field.getContent()); //$NON-NLS-1$
        fField = field;
        fName = field.getName();
    }

    @Override
    public ITmfEventField getContent() {
        return fField.getContent();
    }

    @Override
    public @NonNull String getName() {
        return fName;
    }

    /**
     * Get the fields of the event
     *
     * @return the fields of the event
     */
    public JpfTraceField getField() {
        return fField;
    }

    public String getThreadState() {
        return fField.getThreadState();
    }

    public String getThreadName() {
        return fField.getThreadName();
    }

}