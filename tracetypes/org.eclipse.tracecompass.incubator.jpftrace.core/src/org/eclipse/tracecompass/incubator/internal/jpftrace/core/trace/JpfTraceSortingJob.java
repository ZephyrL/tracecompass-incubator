package org.eclipse.tracecompass.incubator.internal.jpftrace.core.trace;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.tracecompass.internal.jsontrace.core.job.SortingJob;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

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
        super(trace, path, "\"startTime\":", 2); 
    }

    @Override
    protected void processMetadata(ITmfTrace trace, String dir) throws IOException {
        try (FileReader fileReader = new FileReader(getPath())) {
            try (JsonReader reader = new JsonReader(fileReader);) {
                Gson gson = new Gson();
                JsonObject object = gson.fromJson(reader, JsonObject.class);

                // division of meta json file                 
                JsonElement jsonTrace = object.get("data").getAsJsonArray().get(0); 
                JsonObject jsonProcesses = jsonTrace.getAsJsonObject().get("processes").getAsJsonObject();
                JsonArray processes = new JsonArray();
                processes.add(jsonProcesses);

                String filePath = trace.getPath().replaceAll(".json", "Processes.json");
                File processFile = new File(dir + File.separator + new File(filePath).getName());
                processFile.createNewFile();
                try (PrintWriter tempWriter = new PrintWriter(processFile)) {
                    tempWriter.println(gson.toJson(processes));
                }
            }
        } catch (IOException e) {
            // file io exception to handle
        }
    }

}
