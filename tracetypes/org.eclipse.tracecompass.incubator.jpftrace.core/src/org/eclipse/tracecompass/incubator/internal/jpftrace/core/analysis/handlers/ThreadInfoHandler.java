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

public class ThreadInfoHandler extends KernelEventHandler {
    
    public ThreadInfoHandler(IKernelAnalysisEventLayout layout) {
        super(layout);
    }

    @Override
    public void handleEvent(ITmfStateSystemBuilder ss, ITmfEvent event) throws AttributeNotFoundException {
        Integer cpu = KernelEventHandlerUtils.getCpu(event);
        ITmfEventField content = event.getContent();

        Integer tid = content.getFieldValue(Integer.class, IJpfTraceConstants.THREAD_ID);
        if (tid == null) {
            System.out.println("ThreadInfoHandler::handleEvent: event TID not found");
            return;
        }

        String threadEntryMethod = content.getFieldValue(String.class, IJpfTraceConstants.THREAD_ENTRY_METHOD);
        if (threadEntryMethod == null) {
            System.out.println("ThreadInfoHandler::handleEvent: thread entry method not found");
            return;
        }
        
        long timestamp = KernelEventHandlerUtils.getTimestamp(event);

        String threadAttributeName = Attributes.buildThreadAttributeName(tid, cpu);
        final int threadNode = ss.getQuarkRelativeAndAdd(KernelEventHandlerUtils.getNodeThreads(ss), threadAttributeName);
        final int threadEntryMethodNode = ss.getQuarkRelativeAndAdd(threadNode, Attributes.ENTRY_METHOD);

        ss.modifyAttribute(timestamp, threadEntryMethod, threadEntryMethodNode);

        ITmfStateValue value = ss.queryOngoingState(threadEntryMethodNode);
        System.out.println("ThreadInfoHandler::value " + String.valueOf(value.unboxValue()));
    }
}