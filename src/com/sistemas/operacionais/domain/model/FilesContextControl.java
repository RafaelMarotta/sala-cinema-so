package com.sistemas.operacionais.domain.model;

import java.util.concurrent.atomic.AtomicInteger;

public class FilesContextControl {

    private static Integer totalLines = 0;
    private static AtomicInteger idCliente = new AtomicInteger(1);
    private static AtomicInteger numberOfLinesReaded = new AtomicInteger(0);

    public static int incrementAndGetReadedLine() {
        return numberOfLinesReaded.incrementAndGet();
    }

    public static int incrementAndGetIdCliente() {
        return idCliente.incrementAndGet();
    }

    public static int getIdCliente() {
        return idCliente.get();
    }

    public static void addLinesToTotalLines(int lines) {
        totalLines += lines;
    }

    public static boolean isReadComplete() {
        return (totalLines) == numberOfLinesReaded.get();
    }
}
