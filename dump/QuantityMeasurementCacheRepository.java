package com.app.quantitymeasurement.repository.impl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;

/**
 * Singleton repository implementation that keeps measurements in an in-memory cache
 * and persists them to disk using Java serialization.
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final String DEFAULT_STORE_FILE = "quantity_measurements.ser";
    private static QuantityMeasurementCacheRepository instance;
    private final List<QuantityMeasurementEntity> cache = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Path storeFilePath;

    private QuantityMeasurementCacheRepository() {
        // store file placed in user's working directory
        this.storeFilePath = determineStorePath();
        loadFromDisk();
    }

    private Path determineStorePath() {
        try {
            String userDir = System.getProperty("user.dir");
            Path p = Paths.get(userDir).resolve(DEFAULT_STORE_FILE);
            // ensure parent exists (it will for user.dir)
            return p;
        } catch (Exception e) {
            // fallback to temp dir
            return Paths.get(System.getProperty("java.io.tmpdir")).resolve(DEFAULT_STORE_FILE);
        }
    }

    /**
     * Get the singleton instance.
     * @return instance
     */
    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    @Override
    public QuantityMeasurementEntity saveMeasurement(QuantityMeasurementEntity measurement) {
        if (measurement == null) {
            throw new IllegalArgumentException("measurement must not be null");
        }
        lock.lock();
        try {
            // add to cache and persist
            cache.add(measurement);
            saveToDisk();
            return measurement;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        lock.lock();
        try {
            return Collections.unmodifiableList(new ArrayList<>(cache));
        } finally {
            lock.unlock();
        }
    }

    /**
     * Load persisted measurements from disk into the in-memory cache.
     * If the file does not exist or is unreadable, the cache will remain empty.
     */
    @SuppressWarnings("unchecked")
    public void loadFromDisk() {
        lock.lock();
        try {
            if (!Files.exists(storeFilePath)) {
                return;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(storeFilePath)))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    cache.clear();
                    cache.addAll((List<QuantityMeasurementEntity>) obj);
                }
            } catch (IOException | ClassNotFoundException e) {
                // Could log this in a real app; keep cache empty
                // For now, wrap as runtime to make failures visible to callers
                throw new RuntimeException("Failed to load measurements from disk: " + storeFilePath, e);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Persist the in-memory cache to disk.
     */
    private void saveToDisk() {
        lock.lock();
        try {
            // ensure parent directory exists
            Path parent = storeFilePath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(storeFilePath)))) {
                oos.writeObject(new ArrayList<>(cache));
                oos.flush();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save measurements to disk: " + storeFilePath, e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to ensure storage directory for: " + storeFilePath, e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Get history (alias for getAllMeasurements) to match responsibility name.
     * @return list of historical measurements (immutable copy)
     */
    public List<QuantityMeasurementEntity> getHistory() {
        return getAllMeasurements();
    }

    /**
     * Clear in-memory cache and delete persisted file. Useful for tests.
     */
    public void clearAndDeleteStore() {
        lock.lock();
        try {
            cache.clear();
            try {
                Files.deleteIfExists(storeFilePath);
            } catch (IOException ignored) {
            }
        } finally {
            lock.unlock();
        }
    }
}
