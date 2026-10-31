package com.example.mergesort;

import java.util.Comparator;
import java.util.List;

public interface ParallelSorter<T> {

    List<T> sort(
            List<T> data,
            Comparator<T> comparator,
            int threadCount
    );
}
