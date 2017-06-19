package me.ialistannen.minigameframeworky.language

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import me.ialistannen.minigameframeworky.config.Config
import me.ialistannen.minigameframeworky.language.Resolve.ALL
import me.ialistannen.minigameframeworky.language.Resolve.PlaceholderResolver
import me.ialistannen.minigameframeworky.util.color
import java.text.MessageFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A Language.
 */
class Language(@Suppress("CanBeParameter") val locale: Locale, private val config: Config) {
    internal val placeholderResolver: PlaceholderResolver = PlaceholderResolver(locale, config)

    /**
     * Translates a string.
     *
     * @param key the key in the config
     * @param resolve the [Resolve] to apply
     * @param params any parameters to replace
     * @return the translated string
     * @see [translateIfPresent]
     */
    fun translate(key: String, resolve: Resolve = ALL, vararg params: Any): String {
        return translateIfPresent(key, resolve, *params) ?: "Key '$key' not found!"
    }

    /**
     * Translates a string or returns null if not found.
     *
     * @param key the key in the config
     * @param resolve the [Resolve] to apply
     * @param params any parameters to replace
     * @return the replaced string or null if not found
     */
    fun translateIfPresent(key: String, resolve: Resolve = ALL, vararg params: Any): String? {
        val value = config.get<String>(key) ?: return null

        return resolve.resolve(key, this, value, params)
    }
}

enum class Resolve(
        private val resolver:
        (key: String, language: Language, value: String, parameters: Array<out Any>) -> String,
        private val inside: Set<Resolve>
) {
    COLOR({ _, _, value, _ -> value.color() }, COLOR),
    PLACEHOLDER({ key, language, _, parameters ->
        language.placeholderResolver.resolvePlaceholders(key, parameters)
    }, PLACEHOLDER),
    NESTED({ _, language, value, _ -> keyResolver.resolve(language, value) }, NESTED),


    ALL({ key, language, value, parameters ->
        COLOR.resolve(key,
                language,
                NESTED_PLACEHOLDER.resolve(key, language, value, parameters),
                emptyArray())
    }, COLOR, PLACEHOLDER, NESTED),
    NONE({ _, _, value, _ -> value }),


    COLOR_PLACEHOLDER({ key, language, value, parameters ->
        COLOR.resolve(key,
                language,
                PLACEHOLDER.resolve(key, language, value, parameters),
                emptyArray())
    }, COLOR, PLACEHOLDER),
    COLOR_NESTED({ key, language, value, parameters ->
        COLOR.resolve(key,
                language,
                // not possible to have parameters
                // TODO: Make it possible to have parameters
                NESTED.resolve(key, language, value, emptyArray()),
                parameters)
    }, COLOR, NESTED),
    NESTED_PLACEHOLDER({ key, language, value, parameters ->
        NESTED.resolve(key,
                language,
                PLACEHOLDER.resolve(key, language, value, parameters),
                emptyArray())
    }, PLACEHOLDER, NESTED);

    @Suppress("unused") // is used. Thanks IDEA!
    constructor(
            resolver:
            (key: String, language: Language, value: String, parameters: Array<out Any>) -> String,
            vararg inside: Resolve
    ) : this(resolver, setOf(*inside))

    /**
     * Resolves stuff in a config value.
     *
     * @param key the key to the message
     * @param language the [Language] to use
     * @param parameters the parameters to translate
     * @param value the value in the config
     */
    fun resolve(key: String,
                language: Language,
                value: String,
                parameters: Array<out Any>): String {
        return resolver.invoke(key, language, value, parameters)
    }

    operator fun contains(resolve: Resolve) = resolve in inside

    internal companion object {
        val keyResolver: KeyResolver = KeyResolver()
    }

    internal class PlaceholderResolver(val locale: Locale, val config: Config) {
        private val cache: LoadingCache<String, MessageFormat> = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build<String, MessageFormat>(
                        CacheLoader.from({
                            input ->
                            MessageFormat(config.get<String>(input!!), locale)
                        })
                )

        /**
         * Replaces placeholders in the message.
         *
         * @param key the key to the message
         * @param parameters the parameters
         * @return the replaced string or a default if none found
         */
        fun resolvePlaceholders(key: String, parameters: Array<out Any>): String {
            val messageFormat = cache.get(key) ?: return "Key '$key' not found!"

            return messageFormat.format(parameters)
        }
    }

    internal class KeyResolver {

        private var resolvePattern = Regex("\\[\\[(.+?)]]")

        /**
         * Resolves all nested references `[[name]]` for the input string.
         *
         * @param language the [Language] to use
         * @param string the string to resolve nested keys for
         */
        fun resolve(language: Language, string: String): String {
            var resultString = string

            resolvePattern.findAll(string).forEach {
                val key = it.groupValues[1]
                val resolved: String = language.translate(key, NESTED)
                resultString = resultString.replace("[[$key]]", resolved)
            }
            return resultString
        }
    }
}