package com.flowline.workflow_engine.steps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReadFileStep implements StepDefinition{
    private String name;
    private String path;

    public String getName() {
        return name;
    }

    public Runnable toRun() {
        return () -> {
            try {
                List<String> lines = Files.readAllLines(Path.of(path));
                System.out.println("File has " + lines.size() + " lines");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
