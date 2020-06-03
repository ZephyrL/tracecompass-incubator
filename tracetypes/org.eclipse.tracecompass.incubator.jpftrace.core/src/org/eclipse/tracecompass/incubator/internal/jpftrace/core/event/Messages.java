package org.eclipse.tracecompass.incubator.internal.jpftrace.core.event;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.incubator.internal.jpftrace.core.event.messages"; 

    /**
     * Name
     */
    public static @Nullable String JpfTraceAspects_Name;
    /**
     * Name Description
     */
    public static @Nullable String JpfTraceAspects_NameD;
    
    public static @Nullable String JpfTraceAspects_ThreadState;

    public static @Nullable String JpfTraceAspects_ThreadStateD;

    public static @Nullable String JpfTraceAspects_ThreadName;

    public static @Nullable String JpfTraceAspects_ThreadNameD;

    public static @Nullable String JpfTraceAspects_Choice;

    public static @Nullable String JpfTraceAspects_ChoiceD;

    public static @Nullable String JpfTraceAspects_Source;

    public static @Nullable String JpfTraceAspects_SourceD;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
