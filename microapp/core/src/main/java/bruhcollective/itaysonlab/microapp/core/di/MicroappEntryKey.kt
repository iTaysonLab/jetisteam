package bruhcollective.itaysonlab.microapp.core.di

import bruhcollective.itaysonlab.microapp.core.MicroappEntry
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
annotation class MicroappEntryKey(
    val value: KClass<out MicroappEntry>
)
