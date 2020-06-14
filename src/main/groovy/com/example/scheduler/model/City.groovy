package com.example.scheduler.model

enum City {
    ARCHANGELSK         ('Архангельск',         [Cinema.EUROPARK, Cinema.MAXI_ARCHANGELSK, Cinema.TITAN_ARENA]),
    VELIKIY_NOVGOROD    ('Великий Новгород',    [Cinema.MARMELAD]),
    MOSCOW              ('Москва',              [Cinema.MARI]),
    MURMANSK            ('Мурманск',            [Cinema.SEVERNOE_NAGORNOE]),
    PETROZAVODSK        ('Петрозаводск',        [Cinema.MAXI_PETROZAVODSK, Cinema.TETRIS, Cinema.LOTOS_PLAZA]),
    PSKOV               ('Псков',               [Cinema.AQUAPOLIS]),
    SAINT_PETERSBURG    ('Санкт-Петербург',     [Cinema.NA_BOLSHOM, Cinema.GULLIVER, Cinema.ULIANKA, Cinema.OZERKI,
                                                        Cinema.ATLANTIC_CITY, Cinema.MEZHDUNARODNYI, Cinema.BALKANIA_NOVA_2,
                                                        Cinema.OKA, Cinema.MONPASIE, Cinema.EUROPOLIS]),
    SMOLENSK            ('Смоленск',            [Cinema.MAXI_SMOLENSK]),
    STERLITAMAK         ('Стерлитамак',         [Cinema.ARBAT])

    String name
    List<Cinema> cinemas

    City(String name, List<Cinema> cinemas) {
        this.name = name
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