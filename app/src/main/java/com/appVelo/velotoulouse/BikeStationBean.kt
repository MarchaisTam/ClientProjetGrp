package com.appVelo.velotoulouse

class BikeStationBean (
    var name : String,
    var address : String,
    var position : CoordBean,
    var banking : Boolean,
    var stands : Int,
    var availableStands : Int,
    var availableBikes : Int,
    var status : String,
    var lastUpdate : Long,
    var id : Long
        )