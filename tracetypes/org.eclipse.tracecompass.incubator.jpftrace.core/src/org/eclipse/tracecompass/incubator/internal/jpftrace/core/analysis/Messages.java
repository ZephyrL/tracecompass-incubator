package org.eclipse.tracecompass.incubator.internal.jpftrace.core.analysis;

import org.eclipse.osgi.util.NLS;

class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.incubator.internal.jpftrace.core.analysis"; //$NON-NLS-1$

    public static String LttngKernelAnalysisModule_Help;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}