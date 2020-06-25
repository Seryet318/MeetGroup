package com.example.meetgroups.DataModels

import android.location.Location
import com.google.firebase.ktx.Firebase
import java.util.*

class UserLocation {

    var position: Location? = null
    var date: Date? = null
    var uid: String? = ""

    constructor(){

    }

    constructor(pos: Location, date: Date, uid: String){
        position = pos
        this.date = date
        this.uid = uid
    }
}