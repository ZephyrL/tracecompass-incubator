package org.eclipse.tracecompass.incubator.internal.jpftrace.core.event;

import java.util.HashMap;
import java.util.Map;
// import java.util.Map.Entry;
// import java.util.Objects;
// import java.util.stream.Collectors;
import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.tmf.core.event.ITmfEventField;
import org.eclipse.tracecompass.tmf.core.event.TmfEventField;
// import org.eclipse.tracecompass.tmf.core.timestamp.TmfTimestamp;

import org.eclipse.tracecompass.analysis.os.linux.core.kernel.LinuxValues;
import com.google.common.collect.ImmutableMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * JPF Trace fields.
 */
public class JpfTraceField {

    private static final Map<@NonNull String, @NonNull Long> PREV_STATE_LUT;

    static {
        ImmutableMap.Builder<@NonNull String, @NonNull Long> builder = new ImmutableMap.Builder<>();

        builder.put("START", (long) LinuxValues.TASK_STATE_RUNNING);
        builder.put("LOCK", (long) LinuxValues.TASK_INTERRUPTIBLE);
        PREV_STATE_LUT = builder.build();
    }

    private final String fType;
    private final Integer fTransitionId;
    private final Integer fThreadId;
    private final String fThreadName;
    private final long fTimestamp;
    private final String fThreadState;

    private final String fChoiceId;
    private final ArrayList<String> fChoices;

    private final ArrayList<String> fSteps;

    private final ArrayList< Map<String, Object> > fInstructions;

    private ITmfEventField fContent;

    private static final Gson G_SON = new Gson();

    private static long pseudoTime = 0L;

    private JpfTraceField(ArrayList<String> choices,
    ArrayList<String> steps,
    ArrayList<Map<String, Object> > insns,
    Map<String, Object> fields) {
        fType = (String) fields.get(IJpfTraceConstants.TYPE);
        fTransitionId = (Integer) fields.get(IJpfTraceConstants.TRANSITION_ID);
        fThreadId = (Integer) fields.get(IJpfTraceConstants.THREAD_ID);
        fThreadName = (String) fields.get(IJpfTraceConstants.THREAD_NAME);
        fThreadState = (String) fields.get(IJpfTraceConstants.THREAD_STATE);

        fChoiceId = (String) fields.get(IJpfTraceConstants.CHOICE_ID);
        fChoices = choices;

        fSteps = steps;
        fInstructions = insns;

        fTimestamp = JpfTraceField.getPseudoTime() + 100 * fTransitionId;

        ITmfEventField[] array = fields.entrySet().stream()
                .map(entry -> new TmfEventField(entry.getKey(), entry.getValue(), null))
                .toArray(ITmfEventField[]::new);
        fContent = new TmfEventField(ITmfEventField.ROOT_FIELD_ID, fields, array); 

    }

    private static final void Log(String s){
        System.out.println(s);
    }

    public static void setPseudoTime(long value) {
        pseudoTime = value;
    }

    public static long getPseudoTime() {
        return pseudoTime;
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
    public static @Nullable JpfTraceField parseJson(String fieldsString) {
        Log("JpfTraceField::parseJson");

        @Nullable
        JsonObject root = G_SON.fromJson(fieldsString, JsonObject.class);

        Integer transitionId = optInt(root, IJpfTraceConstants.TRANSITION_ID);

        // get thread info component
        JsonObject threadInfo = root.get(IJpfTraceConstants.THREAD_INFO).getAsJsonObject();

        // get thread name from threadInfo
        String threadName = optString(threadInfo, IJpfTraceConstants.THREAD_NAME);

        // get thread ID from threadInfo
        Integer threadId = optInt(threadInfo, IJpfTraceConstants.THREAD_ID);

        String choiceId = optString(root, IJpfTraceConstants.CHOICE_ID);

        ArrayList<String> choices = new ArrayList<>();
        JsonArray choiceArray = optJSONArray(root, IJpfTraceConstants.CHOICES);
        if (choiceArray != null) {
            for (int i = 0; i < choiceArray.size(); i++ ) {
                String choiceName = choiceArray.get(i).getAsString();
                choices.add(choiceName);
            }
        }

        String threadState = optString(root, IJpfTraceConstants.THREAD_STATE);

        ArrayList<String> steps = new ArrayList<>();
        JsonArray stepArray = optJSONArray(root, IJpfTraceConstants.STEPS);
        if(stepArray != null) {
            for (int i = 0; i < stepArray.size(); i++) {
                String step = stepArray.get(i).getAsString();
                steps.add(step);
            }
        }
        Log("Number of Steps: " + String.valueOf(steps.size()));

        Integer numSrc = optInt(root, IJpfTraceConstants.SOURCES);
        Log("Number of Sources: " + String.valueOf(numSrc));

        ArrayList<Map<@NonNull String, @NonNull Object>> insns = new ArrayList<>();
        JsonArray insnArray = optJSONArray(root, IJpfTraceConstants.INSTRUCTIONS);
        if (insnArray != null) {
            for (int i = 0; i < insnArray.size(); i++) {
                Map<@NonNull String, @NonNull Object> insn = new HashMap<>();
                
                JsonObject insnObj = insnArray.get(i).getAsJsonObject();
                Integer stepLocation = optInt(insnObj, IJpfTraceConstants.STEP_LOCATION);
                insn.put(IJpfTraceConstants.STEP_LOCATION, stepLocation);

                String fileLocation = optString(insnObj, IJpfTraceConstants.FILE_LOCATION);
                insn.put(IJpfTraceConstants.FILE_LOCATION, fileLocation);

                Boolean isVirtualInv = optBoolean(insnObj, IJpfTraceConstants.IS_VIRTUAL_INV);
                if (isVirtualInv != null){
                    insn.put(IJpfTraceConstants.IS_VIRTUAL_INV, isVirtualInv);
                }

                Boolean isFieldInst = optBoolean(insnObj, IJpfTraceConstants.IS_FIELD_INST);
                if (isFieldInst != null) {
                    insn.put(IJpfTraceConstants.IS_FIELD_INST, isFieldInst);
                }

                Boolean isLockInst = optBoolean(insnObj, IJpfTraceConstants.IS_LOCK_INST);
                if (isFieldInst != null) {
                    insn.put(IJpfTraceConstants.IS_LOCK_INST, isLockInst);
                    if (isFieldInst) {
                        String lockFieldname = optString(insnObj, IJpfTraceConstants.LOCK_FIELD_NAME);
                        if (lockFieldname != null) {
                            insn.put(IJpfTraceConstants.LOCK_FIELD_NAME, lockFieldname);
                        }
                    }
                }

                Boolean isJVMInvok = optBoolean(insnObj, IJpfTraceConstants.IS_JVM_INVOK);
                if (isJVMInvok != null) {
                    insn.put(IJpfTraceConstants.IS_JVM_INVOK, isJVMInvok);
                }

                Boolean isJVMReturn = optBoolean(insnObj, IJpfTraceConstants.IS_JVM_RETURN);
                if (isJVMReturn != null) {
                    insn.put(IJpfTraceConstants.IS_JVM_RETURN, isJVMReturn);
                }

                insns.add(insn);
            }
        }

        Map<@NonNull String, @NonNull Object> fieldsMap = new HashMap<>();

        fieldsMap.put(IJpfTraceConstants.TRANSITION_ID, transitionId);
        fieldsMap.put(IJpfTraceConstants.THREAD_ID, threadId);
        fieldsMap.put(IJpfTraceConstants.THREAD_NAME, threadName);
        fieldsMap.put(IJpfTraceConstants.THREAD_STATE, threadState);

        fieldsMap.put(IJpfTraceConstants.CHOICE_ID, choiceId);
        
        if (choiceId != null && choiceId.equals("START")) {
            fieldsMap.put(IJpfTraceConstants.TYPE, "sched_wakeup");
            String comm = optString(root, IJpfTraceConstants.COMM);
            fieldsMap.put(IJpfTraceConstants.COMM, comm);
            Integer pid = optInt(root, IJpfTraceConstants.PID);
            fieldsMap.put(IJpfTraceConstants.PID, pid);
            fieldsMap.put(IJpfTraceConstants.PRIO, 100);
            fieldsMap.put(IJpfTraceConstants.SUCCESS, 1);
            fieldsMap.put(IJpfTraceConstants.TARGET_CPU, 0);
        } else {
            fieldsMap.put(IJpfTraceConstants.TYPE, "sched_switch");
            String prevComm = optString(root, IJpfTraceConstants.PREV_COMM);
            fieldsMap.put(IJpfTraceConstants.PREV_COMM, prevComm);
            Integer prevPid = optInt(root, IJpfTraceConstants.PREV_PID);
            fieldsMap.put(IJpfTraceConstants.PREV_PID, prevPid);
            String nextComm = optString(root, IJpfTraceConstants.NEXT_COMM);
            fieldsMap.put(IJpfTraceConstants.NEXT_COMM, nextComm);
            Integer nextPid = optInt(root, IJpfTraceConstants.NEXT_PID);
            fieldsMap.put(IJpfTraceConstants.NEXT_PID, nextPid);

            fieldsMap.put(IJpfTraceConstants.PREV_PRIO, 100);
            fieldsMap.put(IJpfTraceConstants.NEXT_PRIO, 100);
            fieldsMap.put(IJpfTraceConstants.PREV_STATE, PREV_STATE_LUT.getOrDefault(choiceId, 0L));
        }
        return new JpfTraceField(choices, steps, insns, fieldsMap);
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

    private static @Nullable Boolean optBoolean(JsonObject root, String key) {
        JsonElement jsonElement = root.get(key);
        if (jsonElement != null) {
            return jsonElement.getAsBoolean();
        } 
        
        Log("WARNING: JpfTraceField::optBoolean: non-exist boolean requested");
        return null;
    }

    public String getThreadName(){
        return fThreadName;
    }
    /**
     * Get the event content
     *
     * @return the event content
     */
    public ITmfEventField getContent() {
        return fContent;
    }

    public long getTimestamp() {
        return fTimestamp;
    }

    public String getName() {
        return fType;
    }

    public int getThreadId() {
        return fThreadId;
    }

    public String getThreadState() {
        return fThreadState;
    }

    public String getChoiceId() {
        return fChoiceId;
    }

    public int getNumSteps() {
        return fSteps.size();
    } 

    public int getNumInstructions() {
        return fInstructions.size();
    }

    public int getNumChoices() {
        return fChoices.size();
    }
}
