package org.eclipse.tracecompass.incubator.internal.jpftrace.core.trace;

import java.io.IOException;

import org.eclipse.tracecompass.internal.jsontrace.core.job.SortingJob;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

public class JpfTraceSortingJob extends SortingJob {

    /**
     * Constructor
     *
     * @param trace
     *            the trace to be sort
     * @param path
     *            the path to the trace file
     */
    public JpfTraceSortingJob(ITmfTrace trace, String path) {
        super(trace, path, "\"groupId\":", 1); 
    }

    @Override
    protected void processMetadata(ITmfTrace trace, String dir) throws IOException {
        // try (FileReader fileReader = new FileReader(getPath())) {
        //     try (JsonReader reader = new JsonReader(fileReader);) {
        //         Gson gson = new Gson();
        //         JsonObject object = gson.fromJson(reader, JsonObject.class);

        //         // division of meta json file                 
        //         JsonElement jsonTrace = object.get("transitionGroups").getAsJsonArray().get(0);
        //         JsonObject jsonProcesses = jsonTrace.getAsJsonObject().get("transitions").getAsJsonObject();
        //         JsonArray transitions = new JsonArray();
        //         transitions.add(jsonProcesses);

        //         String filePath = trace.getPath().replaceAll(".json", "Transitions.json");
        //         System.out.println("JpfTraceSortingJob::processMetadata: filePath " + filePath);
        //         File transitionFile = new File(dir + File.separator + new File(filePath).getName());
        //         transitionFile.createNewFile();
        //         try (PrintWriter tempWriter = new PrintWriter(transitionFile)) {
        //             tempWriter.println(gson.toJson(transitions));
        //         }
        //     }
        // } catch (IOException e) {
        //     // file io exception to handle
        // }
    }

}
