package me.ialistannen.minigameframeworky.storage

import me.ialistannen.minigameframeworky.storage.DataContainer.DataType

/**
 * A base class for [DataContainer]s.
 */
open class AbstractDataContainer(override val type: DataType) : DataContainer {

    override fun save(storageObject: StorageObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun load(storageKey: StorageKey, forceReload: Boolean): StorageObject? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reload(storageKey: StorageKey) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reloadAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(storageKey: StorageKey): StorageObject? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}