package com.example.scheduler.model

import java.time.ZoneId

enum City {
    ARCHANGELSK         ('Архангельск',         ZoneId.of('UTC+3'), [Cinema.EUROPARK, Cinema.MAXI_ARCHANGELSK, Cinema.TITAN_ARENA]),
    VELIKIY_NOVGOROD    ('Великий Новгород',    ZoneId.of('UTC+3'), [Cinema.MARMELAD]),
    MOSCOW              ('Москва',              ZoneId.of('UTC+3'), [Cinema.MARI]),
    MURMANSK            ('Мурманск',            ZoneId.of('UTC+3'), [Cinema.SEVERNOE_NAGORNOE]),
    PETROZAVODSK        ('Петрозаводск',        ZoneId.of('UTC+3'), [Cinema.MAXI_PETROZAVODSK, Cinema.TETRIS, Cinema.LOTOS_PLAZA]),
    PSKOV               ('Псков',               ZoneId.of('UTC+3'), [Cinema.AQUAPOLIS]),
    SAINT_PETERSBURG    ('Санкт-Петербург',     ZoneId.of('UTC+3'), [Cinema.NA_BOLSHOM, Cinema.GULLIVER, Cinema.ULIANKA, Cinema.OZERKI,
                                                                                    Cinema.ATLANTIC_CITY, Cinema.MEZHDUNARODNYI, Cinema.BALKANIA_NOVA_2,
                                                                                    Cinema.OKA, Cinema.MONPASIE, Cinema.EUROPOLIS]),
    SMOLENSK            ('Смоленск',            ZoneId.of('UTC+3'), [Cinema.MAXI_SMOLENSK]),
    STERLITAMAK         ('Стерлитамак',         ZoneId.of('UTC+5'), [Cinema.ARBAT])

    String name
    ZoneId timeZone
    List<Cinema> cinemas

    City(String name, ZoneId timeZone, List<Cinema> cinemas) {
        this.name = name
        this.timeZone = timeZone
        this.cinemas = cinemas
    }

    static City byName(String name) {
        Arrays.stream(values()).find {
            it.name.equalsIgnoreCase(name)
        } as City
    }

    @Override
    String toString() {
        name
    }
}