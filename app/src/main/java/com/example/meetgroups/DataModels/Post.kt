package com.example.meetgroups.DataModels

class Post {
    var owner : String? = ""
    var descr : String? = ""
    var date : String? = ""

    constructor()

    constructor(name: String, descr: String, date: String){
        this.owner = name
        this.date = date
        this.descr = descr
    }

}
