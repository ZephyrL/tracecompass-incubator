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
// import org.eclipse.tracecompass.incubator.internal.jpftrace.core.event.JpfTraceEvent;

// import java.util.ArrayList;

public class InstructionHandler extends KernelEventHandler {
    
    public InstructionHandler(IKernelAnalysisEventLayout layout) {
        super(layout);
    }

    @Override
    public void handleEvent(ITmfStateSystemBuilder ss, ITmfEvent event) throws AttributeNotFoundException {
        Integer cpu = KernelEventHandlerUtils.getCpu(event);
        ITmfEventField content = event.getContent();
        Integer tid = content.getFieldValue(Integer.class, IJpfTraceConstants.THREAD_ID);

        if (tid == null) {
            System.out.println("InstructionHandler::handleEvent: event TID not found");
            return;
        }

        String src = content.getFieldValue(String.class, IJpfTraceConstants.SRC);
        if (src == null) {
            System.out.println("InstructionHandler::handleEvent: source code not found");
            return;
        }

        StringBuilder specBuilder = new StringBuilder();
        StringBuilder detailBuilder = new StringBuilder();
        boolean isSpec = false;

        Boolean isSync = content.getFieldValue(Boolean.class, IJpfTraceConstants.IS_SYNCHRONIZED);
        if (isSync != null) {
            specBuilder.append("isSync");
            String detail = content.getFieldValue(String.class, IJpfTraceConstants.SYNC_METHOD_NAME);
            if (detail != null) {
                detailBuilder.append(detail);
            } else {
                System.out.println("InstructionHandler::handleEvent: Warning, the insn is SYNC but has no detail");
            }
            isSpec = true;
        }

        Boolean isMethodCall = content.getFieldValue(Boolean.class, IJpfTraceConstants.IS_METHOD_CALL);
        if (isMethodCall != null) {
            if (isSpec) {
                specBuilder.append(" | ");
                detailBuilder.append(" | ");
            }

            specBuilder.append("isMethodCall");
            String detail = content.getFieldValue(String.class, IJpfTraceConstants.CALLED_METHOD_NAME);
            if (detail != null) {
                detailBuilder.append(detail);
            } else {
                System.out.println("InstructionHandler::handleEvent: Warning, the insn is METHOD CALL but has no detail");
            }
            isSpec = true;
        }

        Boolean isThreadRelatedMethod = content.getFieldValue(Boolean.class, IJpfTraceConstants.IS_THREAD_RELATED_METHOD);
        if (isThreadRelatedMethod != null) {
            if (isSpec) {
                specBuilder.append(" | ");
                detailBuilder.append(" | ");
            }

            specBuilder.append("isThreadRelatedMethod");
            String detail = content.getFieldValue(String.class, IJpfTraceConstants.THREAD_RELATED_METHOD);
            if (detail != null) {
                detailBuilder.append(detail);
            } else {
                System.out.println("InstructionHandler::handleEvent: Warning, the insn is a LOCK/UNLOCK but has no detail");
            }
            isSpec = true;
        }

        Boolean isFieldAccess = content.getFieldValue(Boolean.class, IJpfTraceConstants.IS_FIELD_ACCESS);
        if (isFieldAccess != null) {
            if (isSpec) {
                specBuilder.append(" | ");
                detailBuilder.append(" | ");
            }

            specBuilder.append("isFieldAccess");
            String detail = content.getFieldValue(String.class, IJpfTraceConstants.ACCESSED_FIELD);
            if (detail != null) {
                detailBuilder.append(detail);
            } else {
                System.out.println("InstructionHandler::handleEvent: Warning, the insn is a FIELD ACCESS but has no detail");
            }
            isSpec = true;
        }

        String threadAttributeName = Attributes.buildThreadAttributeName(tid, cpu);
        final int threadNode = ss.getQuarkRelativeAndAdd(KernelEventHandlerUtils.getNodeThreads(ss), threadAttributeName);
        final int specNode = ss.getQuarkRelativeAndAdd(threadNode, Attributes.SPEC);
        final int detailNode = ss.getQuarkRelativeAndAdd(threadNode, Attributes.DETAIL);

        long timestamp = KernelEventHandlerUtils.getTimestamp(event);

        ss.modifyAttribute(timestamp, specBuilder.toString(), specNode);
        ss.modifyAttribute(timestamp, detailBuilder.toString(), detailNode);

        // ITmfStateValue value = ss.queryOngoingState(specNode);
        // System.out.println("InstructionHandler::value " + String.valueOf(value.unboxValue()));
    }
}