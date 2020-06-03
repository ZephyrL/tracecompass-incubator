package org.eclipse.tracecompass.incubator.internal.jpftrace.core.analysis.handlers;

import org.eclipse.tracecompass.analysis.os.linux.core.trace.IKernelAnalysisEventLayout;
import org.eclipse.tracecompass.internal.analysis.os.linux.core.kernel.handlers.KernelEventHandler;
import org.eclipse.tracecompass.internal.analysis.os.linux.core.kernel.handlers.KernelEventHandlerUtils;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.event.ITmfEventField;

import org.eclipse.tracecompass.incubator.internal.jpftrace.core.analysis.Attributes;
import org.eclipse.tracecompass.incubator.internal.jpftrace.core.event.IJpfTraceConstants;

public class ThreadExposeHandler extends KernelEventHandler {
    
    public ThreadExposeHandler(IKernelAnalysisEventLayout layout) {
        super(layout);
    }

    @Override
    public void handleEvent(ITmfStateSystemBuilder ss, ITmfEvent event) throws AttributeNotFoundException {
        Integer cpu = KernelEventHandlerUtils.getCpu(event);
        ITmfEventField content = event.getContent();
        Integer tid = content.getFieldValue(Integer.class, IJpfTraceConstants.THREAD_ID);
        long timestamp = KernelEventHandlerUtils.getTimestamp(event);

        if (tid == null) {
            return;
        }

        String threadAttributeName = Attributes.buildThreadAttributeName(tid, cpu);
        final int threadNode = ss.getQuarkRelativeAndAdd(KernelEventHandlerUtils.getNodeThreads(ss), threadAttributeName);
        final int exposeEvent = ss.getQuarkRelativeAndAdd(threadNode, Attributes.EXPOSE);

        ss.modifyAttribute(timestamp, timestamp, exposeEvent);

        ITmfStateValue value = ss.queryOngoingState(exposeEvent);
        System.out.println("ThreadExposeHandler::value " + String.valueOf(value.unboxValue()));

        System.out.println("ThreadExposeHandler::handleEvent " + String.valueOf(tid) + " " + event.getContent().getName());
    }
}