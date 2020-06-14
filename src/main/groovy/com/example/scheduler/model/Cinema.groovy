package com.example.scheduler.model

import groovy.transform.ToString

@ToString(includePackage = false, includes = ['simpleName'])
enum Cinema {
    EUROPARK            (City.ARCHANGELSK, 'Европарк', 'ТРК "ЕвроПарк"', 'http://arhangelsk.mirage.ru/schedule/---DATEINFO---/0/15/0/0/0/schedule.htm'),
    TITAN_ARENA         (City.ARCHANGELSK, 'Титан Арена', 'ТРК "Титан Арена"', 'http://arhangelsk.mirage.ru/schedule/---DATEINFO---/0/19/0/0/0/schedule.htm'),
    MAXI_ARCHANGELSK    (City.ARCHANGELSK, 'Макси', 'ТРЦ "Макси"', 'http://arhangelsk.mirage.ru/schedule/---DATEINFO---/0/22/0/0/0/schedule.htm'),

    MARI                (City.MOSCOW, 'Мари', 'ТРК "MARi"', 'http://moscow.mirage.ru/schedule/---DATEINFO---/0/18/0/0/0/schedule.htm'),

    SEVERNOE_NAGORNOE   (City.MURMANSK, 'Северное Нагорное', 'МФК "Северное Нагорное"', 'http://murmansk.mirage.ru/schedule/---DATEINFO---/0/12/0/0/0/schedule.htm'),

    MARMELAD            (City.VELIKIY_NOVGOROD, 'Мармелад', 'ТРЦ "Мармелад"', 'http://novgorod.mirage.ru/schedule/---DATEINFO---/0/20/0/0/0/schedule.htm'),

    MAXI_PETROZAVODSK   (City.PETROZAVODSK, 'Макси', 'ТРЦ "Макси"', 'http://petrozavodsk.mirage.ru/schedule/---DATEINFO---/0/7/0/0/0/schedule.htm'),
    TETRIS              (City.PETROZAVODSK, 'Тетрис', 'ТРЦ "Тетрис"', 'http://petrozavodsk.mirage.ru/schedule/---DATEINFO---/0/6/0/0/0/schedule.htm'),
    LOTOS_PLAZA         (City.PETROZAVODSK, 'Лотос Плаза', 'ТРК "Лотос Plaza"', 'http://petrozavodsk.mirage.ru/schedule/---DATEINFO---/0/16/0/0/0/schedule.htm'),

    AQUAPOLIS           (City.PSKOV, 'Акваполис', 'ТРК "Акваполис"', 'http://pskov.mirage.ru/schedule/---DATEINFO---/0/21/0/0/0/schedule.htm'),

    NA_BOLSHOM          (City.SAINT_PETERSBURG, 'на Большом', 'на Большом', 'http://www.mirage.ru/schedule/---DATEINFO---/0/1/0/0/0/schedule.htm'),
    GULLIVER            (City.SAINT_PETERSBURG, 'Гулливер', 'ТРК "Гулливер"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/2/0/0/0/schedule.htm'),
    ULIANKA             (City.SAINT_PETERSBURG, 'Ульянка', 'ТРК "Ульянка"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/3/0/0/0/schedule.htm'),
    OZERKI              (City.SAINT_PETERSBURG, 'Озерки', 'в Озерках', 'http://www.mirage.ru/schedule/---DATEINFO---/0/4/0/0/0/schedule.htm'),
    ATLANTIC_CITY       (City.SAINT_PETERSBURG, 'Атлантик-Сити', 'ТРК "Атлантик-Сити"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/5/0/0/0/schedule.htm'),
    MEZHDUNARODNYI      (City.SAINT_PETERSBURG, 'Международный', 'ТРК "Международный"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/8/0/0/0/schedule.htm'),
    BALKANIA_NOVA_2     (City.SAINT_PETERSBURG, 'Балкания НОВА-2', 'ТРК "Балкания NOVA-2"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/10/0/0/0/schedule.htm'),
    OKA                 (City.SAINT_PETERSBURG, 'Ока', 'ТК "Ока", г. Колпино', 'http://www.mirage.ru/schedule/---DATEINFO---/0/11/0/0/0/schedule.htm'),
    MONPASIE            (City.SAINT_PETERSBURG, 'Монпасье', 'ТРЦ "Монпасье"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/13/0/0/0/schedule.htm'),
    EUROPOLIS           (City.SAINT_PETERSBURG, 'Европолис', 'ТРК "Европолис"', 'http://www.mirage.ru/schedule/---DATEINFO---/0/14/0/0/0/schedule.htm'),

    MAXI_SMOLENSK       (City.SMOLENSK, 'Макси', 'ТРЦ "Макси"', 'http://smolensk.mirage.ru/schedule/---DATEINFO---/0/17/0/0/0/schedule.htm'),

    ARBAT               (City.STERLITAMAK, 'Арбат', 'ТЦ "Арбат"', 'http://sterlitamak.mirage.ru/schedule/---DATEINFO---/0/9/0/0/0/schedule.htm')

    City city
    String simpleName
    String formalName
    String link

    Cinema(City city, String simpleName, String formalName, String link) {
        this.city = city
        this.simpleName = simpleName
        this.formalName = formalName
        this.link = link
    }
}