package org.eclipse.tracecompass.incubator.internal.jpftrace.ui.view;

import org.eclipse.tracecompass.internal.analysis.os.linux.ui.views.controlflow.ControlFlowView;
import org.eclipse.jdt.annotation.NonNull;

public class JpfTraceControlFlowView extends ControlFlowView {
    
    public static @NonNull String id = "org.eclipse.tracecompass.incubator.jpftrace.ui.view";

    public JpfTraceControlFlowView() {
        super();
    }

    @Override
    protected String getProviderId() {
        // System.out.println("JpfTraceControlFlowView::getProviderId: called");
        return JpfThreadStatusDataProvider.ID;
    }
}