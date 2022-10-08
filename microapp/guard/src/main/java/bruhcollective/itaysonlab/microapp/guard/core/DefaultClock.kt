package bruhcollective.itaysonlab.microapp.guard.core

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

class DefaultClock @Inject constructor(): Clock() {
    private val sysClock = systemDefaultZone()

    override fun getZone(): ZoneId = sysClock.zone

    override fun withZone(zone: ZoneId?): Clock = sysClock.withZone(zone)

    override fun instant(): Instant = sysClock.instant()
}