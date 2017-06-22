package me.ialistannen.minigameframeworky.storage

/**
 * An object that stores data
 */
interface StorageObject {

    var isDirty: Boolean
    val storageKey: StorageKey

    /**
     * Retrieves stored data.
     *
     * @param key the key the data is stored under
     * @param T the type of the value. Will be safely cast to it (`value as? T`)
     * @return the stored value, if any
     */
    fun <T> getCasted(key: String): T? {
        @Suppress("UNCHECKED_CAST")
        val cast = get(key) as? T
        return cast
    }

    /**
     * Retrieves stored data.
     *
     * @param key the key the data is stored under
     * @return the stored value, if any
     */
    operator fun get(key: String): Any?

    /**
     * Stores data in the object.
     *
     * @param key the key to store the data under
     * @param value the value to store
     */
    operator fun set(key: String, value: Any?)
}

/**
 * A Key to identify a [StorageObject] in the [DataContainer]
 */
interface StorageKey