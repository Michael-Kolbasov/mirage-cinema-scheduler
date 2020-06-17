package com.example.scheduler.model

import groovy.transform.ToString

@ToString(includePackage = false, includes = ['simpleName'])
enum Cinema {
    EUROPARK            ('Архангельск', 'Европарк', 'ТРК "ЕвроПарк"', 'http://arhangelsk.mirage.ru/schedule/---DATEINFO---/0/15/0/0/0/schedule.htm'),
    TITAN_ARENA         ('Архангельск', 'Титан Арена', 'ТРК "Титан Арена"', 'http://arhangelsk.mirage.ru/schedule/---DATEINFO---/0/19/0/0/0/schedule.htm'),
    MAXI_ARCHANGELSK    ('Архангельск', 'Макси', 'ТРЦ "Макси"', 'http://arhangelsk.mirage.ru/schedule/---DATEINFO---/0/22/0/0/0/schedule.htm'),

    MARI                ('Москва', 'Мари', 'ТРК "MARi"', 'http://moscow.mirage.ru/schedule/---DATEINFO---/0/18/0/0/0/schedule.htm'),

    SEVERNOE_NAGORNOE   ('Мурманск', 'Северное Нагорное', 'МФК "Северное Нагорное"', 'http://murmansk.mirage.ru/schedule/---DATEINFO---/0/12/0/0/0/schedule.htm'),

    MARMELAD            ('Великий Новгород', 'Мармелад', 'ТРЦ "Мармелад"', 'http://novgorod.mirage.ru/schedule/---DATEINFO---/0/20/0/0/0/schedule.htm'),

    MAXI_PETROZAVODSK   ('Петрозаводск', 'Макси', 'ТРЦ "Макси"', 'http://petrozavodsk.mirage.ru/schedule/---DATEINFO---/0/7/0/0/0/schedule.htm'),
    TETRIS              ('Петрозаводск', 'Тетрис', 'ТРЦ "Тетрис"', 'http://petrozavodsk.mirage.ru/schedule/---DATEINFO---/0/6/0/0/0/schedule.htm'),
    LOTOS_PLAZA         ('Петрозаводск', 'Лотос Плаза', 'ТРК "Лотос Plaza"', 'http://petrozavodsk.mirage.ru/schedule/---DATEINFO---/0/16/0/0/0/schedule.htm'),

    AQUAPOLIS           ('Псков', 'Акваполис', 'ТРК "Акваполис"', 'http://pskov.mirage.ru/schedule/---DATEINFO---/0/21/0/0/0/schedule.htm'),

    NA_BOLSHOM          ('Санкт-Петербург', 'на Большом', 'на Большом', 'http://www.mirage.ru/schedule/---DATEINFO---/0/1/0/0/0/schedule.htm'),
    GULLIVER            ('Санкт-Петербург', 'Гулливер', 'ТРК "Гулливер"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/2/0/0/0/schedule.htm'),
    ULIANKA             ('Санкт-Петербург', 'Ульянка', 'ТРК "Ульянка"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/3/0/0/0/schedule.htm'),
    OZERKI              ('Санкт-Петербург', 'Озерки', 'в Озерках', 'http://www.mirage.ru/schedule/---DATEINFO---/0/4/0/0/0/schedule.htm'),
    ATLANTIC_CITY       ('Санкт-Петербург', 'Атлантик-Сити', 'ТРК "Атлантик-Сити"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/5/0/0/0/schedule.htm'),
    MEZHDUNARODNYI      ('Санкт-Петербург', 'Международный', 'ТРК "Международный"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/8/0/0/0/schedule.htm'),
    BALKANIA_NOVA_2     ('Санкт-Петербург', 'Балкания НОВА-2', 'ТРК "Балкания NOVA-2"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/10/0/0/0/schedule.htm'),
    OKA                 ('Санкт-Петербург', 'Ока', 'ТК "Ока", г. Колпино', 'http://www.mirage.ru/schedule/---DATEINFO---/0/11/0/0/0/schedule.htm'),
    MONPASIE            ('Санкт-Петербург', 'Монпасье', 'ТРЦ "Монпасье"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/13/0/0/0/schedule.htm'),
    EUROPOLIS           ('Санкт-Петербург', 'Европолис', 'ТРК "Европолис"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/14/0/0/0/schedule.htm'),

    MAXI_SMOLENSK       ('Смоленск', 'Макси', 'ТРЦ "Макси"', 'http://smolensk.mirage.ru/schedule/---DATEINFO---/0/17/0/0/0/schedule.htm'),

    ARBAT               ('Стерлитамак', 'Арбат', 'ТЦ "Арбат"', 'http://sterlitamak.mirage.ru/schedule/---DATEINFO---/0/9/0/0/0/schedule.htm')

    String city
    String simpleName
    String formalName
    String link

    Cinema(String city, String simpleName, String formalName, String link) {
        this.city = city
        this.simpleName = simpleName
        this.formalName = formalName
        this.link = link
    }

    City getCity() {
        City.valueOf(city)
    }

    static Cinema byName(String name) {
        Arrays.stream(values()).find {
            it.formalName.equalsIgnoreCase(name)
        } as Cinema
    }
}