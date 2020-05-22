package org.eclipse.tracecompass.incubator.internal.jpftrace.ui.style;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.incubator.internal.jpftrace.ui.style.messages"; //$NON-NLS-1$
    
    public static @Nullable String JpfThreadStyle_ProcessGroup = null;
    
    public static @Nullable String JpfThreadStyle_lock = null;

    public static @Nullable String JpfThreadStyle_expose = null;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
