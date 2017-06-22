package me.ialistannen.minigameframeworky.storage

/**
 * A container for [StorageObject]s that are logically grouped.
 */
interface DataContainer {

    val type: DataType

    /**
     * Saves a [StorageObject].
     *
     * @param storageObject the [StorageObject] to save
     */
    fun save(storageObject: StorageObject)

    /**
     * Forces to save all unsaved data
     *
     * @see save
     */
    fun saveAll()

    /**
     * Loads a [StorageObject] by its [StorageKey]
     *
     * @param forceReload whether to force reloading it from disk. If false, a cached value may be returned
     * @param storageKey the [StorageKey] to load it from
     * @return the [StorageObject], if any
     */
    fun load(storageKey: StorageKey, forceReload: Boolean = false): StorageObject?

    /**
     * Reloads the data from disk, discarding any loaded one and unsaved changes.
     *
     * @param storageKey the [StorageKey] to reload
     */
    fun reload(storageKey: StorageKey)

    /**
     * Reloads all data from disk, discarding any loaded one and unsaved changes.
     *
     * @see reload
     */
    fun reloadAll()

    /**
     * Returns the [StorageObject] for a given [StorageKey], if any.
     *
     * @param storageKey the [StorageKey] to use
     * @return the found [StorageObject], if any
     */
    operator fun get(storageKey: StorageKey): StorageObject?

    /**
     * The type of the data to store.
     */
    enum class DataType {
        PLAYER, ARENA
    }
}