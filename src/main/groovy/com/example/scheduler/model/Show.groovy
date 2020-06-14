package com.example.scheduler.model

/**
 * Class Show is used to fill the Cinema's schedule. It's just a "container" to store all the info about the show
 * in the single object.
 */
class Show {
    private static final def SEPARATOR = System.getProperty('line.separator')

    String time
    String name
    String format
    String ageLimit
    String auditorium
    List<String> price = []

    @Override
    String toString() {
        return time + SEPARATOR +
                name + SEPARATOR +
                format + SEPARATOR +
                ageLimit + SEPARATOR +
                auditorium + SEPARATOR +
                price
    }
}
