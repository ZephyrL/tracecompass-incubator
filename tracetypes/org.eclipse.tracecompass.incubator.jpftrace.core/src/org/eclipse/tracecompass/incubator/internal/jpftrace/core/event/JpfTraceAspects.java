package org.eclipse.tracecompass.incubator.internal.jpftrace.core.event;

import java.text.Format;
import java.util.Collections;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.common.core.format.SubSecondTimeWithUnitFormat;
import org.eclipse.tracecompass.tmf.core.event.aspect.ITmfEventAspect;
import org.eclipse.tracecompass.tmf.core.event.aspect.TmfBaseAspects;

import com.google.common.collect.ImmutableList;

/**
 * Aspects for Trace Compass Logs
 *
 * @author Katherine Nadeau
 */
public class JpfTraceAspects {

    /**
     * Apects of a trace
     */
    private static @Nullable Iterable<@NonNull ITmfEventAspect<?>> aspects;

    /**
     * Get the event aspects
     *
     * @return get the event aspects
     */
    public static Iterable<@NonNull ITmfEventAspect<?>> getAspects() {
        Iterable<@NonNull ITmfEventAspect<?>> aspectSet = aspects;
        if (aspectSet == null) {
            aspectSet = ImmutableList.of(
                    new JpfTraceLabelAspect(),
                    TmfBaseAspects.getTimestampAspect(),
                    new JpfTraceDurationAspect(),
                    new JpfTraceSpanIdAspect(),
                    new JpfTraceProcessAspect(),
                    new JpfTraceProcessTagsAspect(),
                    new JpfTraceTagsAspect());
            aspects = aspectSet;
        }
        return aspectSet;
    }

    private static class JpfTraceLabelAspect implements IJpfTraceAspect<String> {

        @Override
        public @NonNull String getName() {
            return String.valueOf(Messages.JpfTraceAspects_Name);
        }

        @Override
        public @NonNull String getHelpText() {
            return String.valueOf(Messages.JpfTraceAspects_NameD);
        }

        @Override
        public String resolveJpfTraceLogs(@NonNull JpfTraceEvent event) {
            return event.getName();
        }
    }

    private static class JpfTraceProcessAspect implements IJpfTraceAspect<String> {

        @Override
        public @NonNull String getName() {
            return String.valueOf(Messages.JpfTraceAspects_Process);
        }

        @Override
        public @NonNull String getHelpText() {
            return String.valueOf(Messages.JpfTraceAspects_ProcessD);
        }

        @Override
        public String resolveJpfTraceLogs(@NonNull JpfTraceEvent event) {
            Object field = event.getField().getProcessName();
            return String.valueOf(field);
        }
    }

    private static class JpfTraceDurationAspect implements IJpfTraceAspect<@Nullable String> {
        private static final Format FORMATTER = SubSecondTimeWithUnitFormat.getInstance();

        @Override
        public @NonNull String getName() {
            return String.valueOf(Messages.JpfTraceAspects_Duration);
        }

        @Override
        public @NonNull String getHelpText() {
            return String.valueOf(Messages.JpfTraceAspects_DurationD);
        }

        @Override
        public @Nullable String resolveJpfTraceLogs(@NonNull JpfTraceEvent event) {
            return FORMATTER.format(event.getField().getDuration());
        }
    }

    private static class JpfTraceSpanIdAspect implements IJpfTraceAspect<String> {

        @Override
        public @NonNull String getName() {
            return String.valueOf(Messages.JpfTraceAspects_SpanId);
        }

        @Override
        public @NonNull String getHelpText() {
            return String.valueOf(Messages.JpfTraceAspects_SpanIdD);
        }

        @Override
        public String resolveJpfTraceLogs(@NonNull JpfTraceEvent event) {
            return event.getField().getSpanId();
        }
    }

    private static class JpfTraceProcessTagsAspect implements IJpfTraceAspect<Map<String, Object>> {

        @Override
        public String getName() {
            return String.valueOf(Messages.JpfTraceAspects_ProcessTags);
        }

        @Override
        public String getHelpText() {
            return String.valueOf(Messages.JpfTraceAspects_ProcessTagsD);
        }

        @Override
        public Map<String, Object> resolveJpfTraceLogs(@NonNull JpfTraceEvent event) {
            Map<String, Object> processTags = event.getField().getProcessTags();
            return processTags == null ? Collections.emptyMap() : processTags;
        }
    }

    private static class JpfTraceTagsAspect implements IJpfTraceAspect<Map<String, Object>> {

        @Override
        public String getName() {
            return String.valueOf(Messages.JpfTraceAspects_Tags);
        }

        @Override
        public String getHelpText() {
            return String.valueOf(Messages.JpfTraceAspects_TagsD);
        }

        @Override
        public Map<String, Object> resolveJpfTraceLogs(@NonNull JpfTraceEvent event) {
            Map<String, Object> tags = event.getField().getTags();
            return tags == null ? Collections.emptyMap() : tags;
        }
    }
}
