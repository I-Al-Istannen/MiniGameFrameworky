package me.ialistannen.minigameframeworky.storage

import org.bukkit.entity.Player
import java.util.*

/**
 * A [StorageObject] for a [Player].
 */
class PlayerStorageObject(isDirty: Boolean,
                          dataContainer: DataContainer,
                          storageKey: PlayerStorageKey
) : DelegatingMemoryStorageObject(
        isDirty,
        dataContainer,
        storageKey) {

    data class PlayerStorageKey(val uuid: UUID) : StorageKey
}