package org.eclipse.tracecompass.incubator.internal.jpftrace.core.event;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.incubator.internal.jpftrace.core.event.messages"; 
    /**
     * Tags
     */
    public static @Nullable String JpfTraceAspects_Tags;
    /**
     * Tags Description
     */
    public static @Nullable String JpfTraceAspects_TagsD;
    /**
     * ID
     */
    public static @Nullable String JpfTraceAspects_SpanId;
    /**
     * ID Description
     */
    public static @Nullable String JpfTraceAspects_SpanIdD;
    /**
     * Name
     */
    public static @Nullable String JpfTraceAspects_Name;
    /**
     * Name Description
     */
    public static @Nullable String JpfTraceAspects_NameD;
    /**
     * Duration
     */
    public static @Nullable String JpfTraceAspects_Duration;
    /**
     * Duration Description
     */
    public static @Nullable String JpfTraceAspects_DurationD;
    /**
     * Process Id
     */
    public static @Nullable String JpfTraceAspects_Process;
    /**
     * Process Id Description
     */
    public static @Nullable String JpfTraceAspects_ProcessD;
    /**
     * Process Tags
     */
    public static @Nullable String JpfTraceAspects_ProcessTags;
    /**
     * Process Tags Description
     */
    public static @Nullable String JpfTraceAspects_ProcessTagsD;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
