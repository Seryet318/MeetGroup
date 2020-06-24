package com.example.meetgroups.DataModels

class Account {
    var email: String? = ""
    var username: String? = ""
    var firstName: String? = ""
    var lastName: String? = ""

    constructor()

    constructor(fName: String, lName: String, email: String) {
        this.email = email
        firstName = fName
        lastName = lName
    }

    constructor(fName: String, lName: String, username: String, email: String) {
        this.email = email
        this.username = username
        firstName = fName
        lastName = lName
    }

}
