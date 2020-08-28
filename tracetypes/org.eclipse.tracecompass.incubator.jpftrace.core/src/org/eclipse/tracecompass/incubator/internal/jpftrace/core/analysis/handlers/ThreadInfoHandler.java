package org.eclipse.tracecompass.incubator.internal.jpftrace.core.analysis.handlers;

import org.eclipse.tracecompass.analysis.os.linux.core.trace.IKernelAnalysisEventLayout;
import org.eclipse.tracecompass.internal.analysis.os.linux.core.kernel.handlers.KernelEventHandler;
import org.eclipse.tracecompass.internal.analysis.os.linux.core.kernel.handlers.KernelEventHandlerUtils;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
// import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
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
        // System.out.println("Threadinfo Handler: tid: " + String.valueOf(tid));
        final int threadEntryMethodNode = ss.getQuarkRelativeAndAdd(threadNode, Attributes.ENTRY_METHOD);
        final int specNode = ss.getQuarkRelativeAndAdd(threadNode, Attributes.SPEC);
        final int detailNode = ss.getQuarkRelativeAndAdd(threadNode, Attributes.DETAIL);

        ss.modifyAttribute(timestamp, threadEntryMethod, threadEntryMethodNode);

        // set the state of special instructions to null
        ss.modifyAttribute(timestamp, "", specNode);
        ss.modifyAttribute(timestamp, "", detailNode);

        // ITmfStateValue value = ss.queryOngoingState(choideMadeNode);
        // System.out.println("ChoiceInfoHandler::value " + String.valueOf(value.unboxValue()));
    }
}