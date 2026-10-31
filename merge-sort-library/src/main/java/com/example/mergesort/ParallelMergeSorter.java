package com.example.mergesort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelMergeSorter<T> implements ParallelSorter<T> {

    @Override
    public List<T> sort(List<T> data, Comparator<T> comparator, int threadCount) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }

        if (threadCount < 1) {
            throw new IllegalArgumentException(
                    "Thread count must be greater than zero"
            );
        }

        if (data.size() <= 1) {
            return new ArrayList<>(data);
        }

        int actualThreadCount = Math.min(threadCount, data.size());
        ExecutorService executor = Executors.newFixedThreadPool(actualThreadCount);
        try {
            List<List<T>> partitions = split(data, actualThreadCount);
            List<Future<List<T>>> futures = new ArrayList<>();
            for (List<T> partition : partitions) {
                Callable<List<T>> task = () -> sequentialMergeSort(partition, comparator);
                futures.add(executor.submit(task));
            }

            List<List<T>> sortedPartitions = new ArrayList<>();

            for (Future<List<T>> future : futures) {
                sortedPartitions.add(future.get());
            }

            while (sortedPartitions.size() > 1) {
                List<List<T>> mergedPartitions = new ArrayList<>();
                for (int i = 0; i < sortedPartitions.size(); i += 2) {
                    if (i + 1 < sortedPartitions.size()) {
                        List<T> merged = merge(
                                sortedPartitions.get(i),
                                sortedPartitions.get(i + 1),
                                comparator
                        );
                        mergedPartitions.add(merged);
                    } else {
                        mergedPartitions.add(sortedPartitions.get(i));
                    }
                }
                sortedPartitions = mergedPartitions;
            }

            return sortedPartitions.get(0);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Sorting was interrupted", e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Error during parallel sorting", e.getCause());
        } finally {
            executor.shutdownNow();
        }
    }

    private List<List<T>> split(List<T> data, int partsCount) {
        List<List<T>> partitions = new ArrayList<>();
        int size = data.size();
        int baseSize = size / partsCount;
        int remainder = size % partsCount;

        int start = 0;
        for (int i = 0; i < partsCount; i++) {
            int partitionSize = baseSize;

            if (i < remainder) {
                partitionSize++;
            }

            int end = start + partitionSize;
            partitions.add(new ArrayList<>(data.subList(start, end)));
            start = end;
        }

        return partitions;
    }

    private List<T> sequentialMergeSort(List<T> data, Comparator<T> comparator) {
        if (data.size() <= 1) {
            return new ArrayList<>(data);
        }

        int middle = data.size() / 2;
        List<T> left = sequentialMergeSort(new ArrayList<>(data.subList(0, middle)), comparator);
        List<T> right = sequentialMergeSort(new ArrayList<>(data.subList(middle, data.size())), comparator);
        return merge(left, right, comparator);
    }

    private List<T> merge(List<T> left, List<T> right, Comparator<T> comparator) {
        List<T> result = new ArrayList<>(left.size() + right.size());

        int leftIndex = 0;
        int rightIndex = 0;
        while (leftIndex < left.size() && rightIndex < right.size()) {

            if (comparator.compare(left.get(leftIndex), right.get(rightIndex)) <= 0) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        while (leftIndex < left.size()) {
            result.add(left.get(leftIndex));
            leftIndex++;
        }

        while (rightIndex < right.size()) {
            result.add(right.get(rightIndex));
            rightIndex++;
        }

        return result;
    }
}
