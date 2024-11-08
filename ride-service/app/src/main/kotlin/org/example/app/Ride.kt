package org.example.app

class Ride : Entity<Int> {
    override fun getId(): Int {
        return Int.MAX_VALUE;
    }

}
