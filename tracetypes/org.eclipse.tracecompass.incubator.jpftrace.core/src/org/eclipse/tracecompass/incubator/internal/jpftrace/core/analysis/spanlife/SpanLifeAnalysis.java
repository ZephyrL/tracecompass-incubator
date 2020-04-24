package org.eclipse.tracecompass.incubator.internal.jpftrace.core.analysis.spanlife;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

public class SpanLifeAnalysis extends TmfStateSystemAnalysisModule {

    // class ID
    public static final String ID = "org.eclipse.tracecompass.incubator.jpftrace.analysis.spanlife"; //$NON-NLS-1$

    public SpanLifeAnalysis() {
        setId(ID);
    }

    @Override
    protected @NonNull ITmfStateProvider createStateProvider() {
        ITmfTrace trace = getTrace();
        return new SpanLifeStateProvider(Objects.requireNonNull(trace));
    }

}
