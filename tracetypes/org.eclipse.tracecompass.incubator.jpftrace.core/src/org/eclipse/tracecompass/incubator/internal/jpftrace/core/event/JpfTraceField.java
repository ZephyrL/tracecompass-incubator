package org.eclipse.tracecompass.incubator.internal.jpftrace.core.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.sql.Timestamp;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.tmf.core.event.ITmfEventField;
import org.eclipse.tracecompass.tmf.core.event.TmfEventField;
import org.eclipse.tracecompass.tmf.core.timestamp.TmfTimestamp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * JPF Trace fields.
 */
public class JpfTraceField {

    private final String fOperationName;
    private final String fThreadName;
    private final ITmfEventField fContent;
    private final String fSpanId;
    private final Long fStartTime;
    private final Long fDuration;
    private final @Nullable Map<String, Object> fTags;
    private final @Nullable Map<String, Object> fProcessTags;
    private String fProcessName;

    private static final Gson G_SON = new Gson();

    private static @Nullable Timestamp pseudoTime = null;

    private static final void Log(String s){
        System.out.println(s);
    }

    /** 
     * pretend to move time to 100 nanoseconds later
     */
    private static final void TimeForward(){
        if(pseudoTime != null){
            pseudoTime.setNanos(pseudoTime.getNanos() + 100);
            Log(String.valueOf(pseudoTime));
        }
    }

    /**
     * Get the process id
     *
     * @param eventString
     *            the event string
     * @return the process id
     */
    public static @Nullable String getProcess(String eventString) {
        @Nullable
        JsonObject root = G_SON.fromJson(eventString, JsonObject.class);
        String process = optString(root, IJpfTraceConstants.PROCESS_ID);
        return process == null ? "" : process; 
    }

    /**
     * Parse a JSON string
     *
     * @param fieldsString
     *            the string
     * @param processField
     *            the process name and tags
     * @return an event field
     */
    public static @Nullable JpfTraceField parseJson(String fieldsString, @Nullable String processField) {
        Log("JpfTraceField::parseJson");

        @Nullable
        JsonObject root = G_SON.fromJson(fieldsString, JsonObject.class);

        // get base time stamp
        // pass basic time from path
        String time = optString(root, IJpfTraceConstants.BASE_TIME);
        if(time != null){
            Log("JpfTraceField::parseJson, timeString: " + time);
            pseudoTime = Timestamp.valueOf(time);
            Log("JpfTraceField::parseJson, pseudotime: " + String.valueOf(pseudoTime));
            TimeForward();
            Log("JpfTraceField::parseJson, pseudotime: " + String.valueOf(pseudoTime));
        }

        // get thread info component
        JsonObject threadInfo = root.get(IJpfTraceConstants.THREAD_INFO).getAsJsonObject();

        // get thread name from threadInfo
        String threadName = optString(threadInfo, IJpfTraceConstants.THREAD_NAME);
        String name = String.valueOf(threadName);
        if(threadName != null){
            Log("JpfTraceField::parseJson, name: " + threadName);
        }

        // get thread ID from threadInfo
        Integer threadId = optInt(threadInfo, IJpfTraceConstants.THREAD_ID);
        String traceId = String.valueOf(threadId);
        if(threadId != Integer.MIN_VALUE){
            Log("JpfTraceField::parseJson, id: " + String.valueOf(threadId));
        }

        // get transitions array
        JsonArray transitions = optJSONArray(root, IJpfTraceConstants.TRANSITIONS);
        if(transitions != null){
            Log("JpfTraceField::parseJson, numTran: " + String.valueOf(transitions.size()));
        }

        


        // Sample parsing 
        // String name = String.valueOf(optString(root, IJpfTraceConstants.OPERATION_NAME));
        if (name == "null") { 
            return null;
        }
        // String traceId = optString(root, IJpfTraceConstants.TRACE_ID);
        String spanId = optString(root, IJpfTraceConstants.SPAN_ID);
        Integer flags = optInt(root, IJpfTraceConstants.FLAGS);

        // parsing of basic components

        Long startTime = optLong(root, IJpfTraceConstants.START_TIME);
        if (Double.isFinite(startTime)) {
            startTime = TmfTimestamp.fromMicros(startTime).toNanos();
        }
        Long duration = optLong(root, IJpfTraceConstants.DURATION);
        if (Double.isFinite(duration)) {
            duration = TmfTimestamp.fromMicros(duration).toNanos();
        }

        Map<@NonNull String, @NonNull Object> fieldsMap = new HashMap<>();

        JsonArray refs = optJSONArray(root, IJpfTraceConstants.REFERENCES);
        if (refs != null) {
            for (int i = 0; i < refs.size(); i++) {
                String key = Objects.requireNonNull(refs.get(i).getAsJsonObject().get(IJpfTraceConstants.REFERENCE_TYPE).getAsString());
                JsonElement element = Objects.requireNonNull(refs.get(i).getAsJsonObject().get(IJpfTraceConstants.SPAN_ID));
                String value = String.valueOf(element.isJsonPrimitive() ? element.getAsJsonPrimitive().getAsString() : element.toString());
                fieldsMap.put(IJpfTraceConstants.REFERENCES + '/' + key, value);
            }
        }

        JsonArray tags = optJSONArray(root, IJpfTraceConstants.TAGS);
        if (tags != null) {
            for (int i = 0; i < tags.size(); i++) {
                String key = Objects.requireNonNull(tags.get(i).getAsJsonObject().get(IJpfTraceConstants.KEY).getAsString());
                JsonElement element = Objects.requireNonNull(tags.get(i).getAsJsonObject().get(IJpfTraceConstants.VALUE));
                String value = String.valueOf(element.isJsonPrimitive() ? element.getAsJsonPrimitive().getAsString() : element.toString());
                fieldsMap.put(IJpfTraceConstants.TAGS + '/' + key, value);
            }
        }

        JsonArray logs = optJSONArray(root, IJpfTraceConstants.LOGS);
        if (logs != null) {
            Map<Long, Map<String, String>> timestampList = new HashMap();
            for (int i = 0; i < logs.size(); i++) {
                Long timestamp = optLong(logs.get(i).getAsJsonObject(), IJpfTraceConstants.TIMESTAMP);
                if (Double.isFinite(timestamp)) {
                    timestamp = TmfTimestamp.fromMicros(timestamp).toNanos();
                }
                JsonArray fields = Objects.requireNonNull(logs.get(i).getAsJsonObject().get(IJpfTraceConstants.FIELDS).getAsJsonArray());
                Map<String, String> fieldsList = new HashMap();
                for (int j = 0; j < fields.size(); j++) {
                    String key = Objects.requireNonNull(fields.get(j).getAsJsonObject().get(IJpfTraceConstants.KEY).getAsString());
                    JsonElement element = Objects.requireNonNull(fields.get(j).getAsJsonObject().get(IJpfTraceConstants.VALUE));
                    String value = String.valueOf(element.isJsonPrimitive() ? element.getAsJsonPrimitive().getAsString() : element.toString());
                    fieldsList.put(key, value);
                }
                timestampList.put(timestamp.longValue(), fieldsList);
            }
            fieldsMap.put(IJpfTraceConstants.LOGS, timestampList);
        }

        // if (traceId == null || spanId == null) {
        //     return null;
        // }

        fieldsMap.put(IJpfTraceConstants.OPERATION_NAME, name);
        fieldsMap.put(IJpfTraceConstants.TRACE_ID, traceId);
        fieldsMap.put(IJpfTraceConstants.SPAN_ID, spanId);
        if (flags != Integer.MIN_VALUE) {
            fieldsMap.put(IJpfTraceConstants.FLAGS, flags);
        }
        fieldsMap.put(IJpfTraceConstants.START_TIME, startTime);
        fieldsMap.put(IJpfTraceConstants.DURATION, duration);

        String processName = processField == null ? "" : parseProcess(processField, fieldsMap); 
        fieldsMap.put(IJpfTraceConstants.PROCESS_NAME, processName);

        return new JpfTraceField(name, threadName, fieldsMap, spanId, startTime, duration, processName);
    }

    /**
     * Parse a JSON string of the process and add it in fieldsMap
     *
     * @param processField
     *            the string
     * @param fieldsMap
     *            processes list
     * @return the process name
     */
    public static String parseProcess(String processField, Map<String, Object> fieldsMap) {
        @Nullable
        JsonObject root = G_SON.fromJson(processField, JsonObject.class);

        String name = String.valueOf(optString(root, IJpfTraceConstants.SERVICE_NAME));
        if (name == "null") { 
            return ""; 
        }

        JsonArray tags = optJSONArray(root, IJpfTraceConstants.TAGS);
        if (tags != null) {
            for (int i = 0; i < tags.size(); i++) {
                String key = Objects.requireNonNull(tags.get(i).getAsJsonObject().get("key").getAsString()); 
                JsonElement element = Objects.requireNonNull(tags.get(i).getAsJsonObject().get("value")); 
                String value = String.valueOf(element.isJsonPrimitive() ? element.getAsJsonPrimitive().getAsString() : element.toString());
                fieldsMap.put(IJpfTraceConstants.PROCESS_TAGS + '/' + key, value);
            }
        }

        return name;

    }

    private static long optLong(JsonObject root, String key) {
        JsonElement jsonElement = root.get(key);
        return jsonElement != null ? jsonElement.getAsLong() : Long.MIN_VALUE;
    }

    private static int optInt(JsonObject root, String key) {
        JsonElement jsonElement = root.get(key);
        return jsonElement != null ? jsonElement.getAsInt() : Integer.MIN_VALUE;
    }

    private static @Nullable JsonArray optJSONArray(JsonObject root, String key) {
        JsonElement jsonElement = root.get(key);
        return jsonElement != null ? jsonElement.getAsJsonArray() : null;
    }

    private static @Nullable String optString(JsonObject root, String key) {
        JsonElement jsonElement = root.get(key);
        return jsonElement != null ? jsonElement.getAsString() : null;
    }

    /**
     * Constructor
     *
     * @param name
     *            operation name
     * @param fields
     *            span fields (arguments)
     * @param spanId
     *            the span id
     * @param startTime
     *            the span start time
     * @param duration
     *            the span duration
     * @param processName
     *            the span process name
     */
    private JpfTraceField(String name, String threadName, Map<String, Object> fields, String spanId, Long startTime, Long duration, String processName) {
        fOperationName = name;
        fThreadName = threadName;
        ITmfEventField[] array = fields.entrySet().stream()
                .map(entry -> new TmfEventField(entry.getKey(), entry.getValue(), null))
                .toArray(ITmfEventField[]::new);
        fContent = new TmfEventField(ITmfEventField.ROOT_FIELD_ID, fields, array);
        fSpanId = spanId;
        fStartTime = startTime;
        fDuration = duration;
        @SuppressWarnings("null")
        Map<@NonNull String, @NonNull Object> tags = fields.entrySet().stream()
                .filter(entry -> {
                    return entry.getKey().startsWith(IJpfTraceConstants.TAGS + '/');
                })
                .collect(Collectors.toMap(entry -> entry.getKey().substring(5), Entry::getValue));
        fTags = tags.isEmpty() ? null : tags;
        @SuppressWarnings("null")
        Map<@NonNull String, @NonNull Object> processTags = fields.entrySet().stream()
                .filter(entry -> {
                    return entry.getKey().startsWith(IJpfTraceConstants.PROCESS_TAGS + '/');
                })
                .collect(Collectors.toMap(entry -> entry.getKey().substring(12), Entry::getValue));
        fProcessTags = processTags.isEmpty() ? null : processTags;
        fProcessName = processName;
    }

    public String getThreadName(){
        return fThreadName;
    }

    /**
     * Get the operation name
     *
     * @return the operation name
     */
    public String getName() {
        return fOperationName;
    }

    /**
     * Get the event content
     *
     * @return the event content
     */
    public ITmfEventField getContent() {
        return fContent;
    }

    /**
     * Get the span id
     *
     * @return the span id
     */
    public String getSpanId() {
        return fSpanId;
    }

    /**
     * Get the span start time
     *
     * @return the start time
     */
    public Long getStartTime() {
        return fStartTime;
    }

    /**
     * Get the event duration
     *
     * @return the duration
     */
    public Long getDuration() {
        return fDuration;
    }

    /**
     * Get the span tags
     *
     * @return a map of the tags and their field names
     */
    public @Nullable Map<String, Object> getTags() {
        return fTags;
    }

    /**
     * Get the span process tags
     *
     * @return a map of the process tags and their field names
     */
    @Nullable
    public Map<String, Object> getProcessTags() {
        return fProcessTags;
    }

    /**
     * Get the span processName
     *
     * @return the process name
     */
    public String getProcessName() {
        return fProcessName;
    }
}
