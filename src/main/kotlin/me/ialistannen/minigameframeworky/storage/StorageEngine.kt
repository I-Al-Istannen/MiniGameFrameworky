package me.ialistannen.minigameframeworky.storage

/**
 * An object to store data
 */
interface StorageEngine {

    fun save(storageObject: StorageObject)

    /**
     * Loads a [StorageObject] from a [StorageKey].
     *
     * @param storageKey the [StorageKey] to denote
     * @return the loaded [StorageObject], if any.
     */
    fun load(storageKey: StorageKey): StorageObject?

    /**
     * Returns all keys in this [StorageEngine].
     *
     * **Might be *really* slow and inefficient.**
     *
     * @return all keys in this [StorageEngine].
     */
    fun getAll(): Iterable<StorageKey>
}