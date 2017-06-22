package me.ialistannen.minigameframeworky.storage

/**
 * A [StorageObject] that delegates saving and reloading to a [StorageEngine].
 *
 * Keeps all data in memory.
 */
open class DelegatingMemoryStorageObject(override var isDirty: Boolean = false,
                                         protected val dataContainer: DataContainer,
                                         override val storageKey: StorageKey) : StorageObject {

    protected val map: MutableMap<String, Any?> = HashMap()

    override fun get(key: String): Any? = map[key]

    override fun set(key: String, value: Any?) {
        map[key] = value
    }
}