package ru.nsu.fit;

import ru.nsu.fit.transformers.TimeTracingTransformer;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String argument, Instrumentation inst) {
        inst.addTransformer(new TimeTracingTransformer());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println(inst.getAllLoadedClasses().length)));
    }
}
