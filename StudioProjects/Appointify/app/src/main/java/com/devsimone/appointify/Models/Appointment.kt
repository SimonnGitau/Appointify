package com.devsimone.appointify.Models

data class Appointment(

    var appId: String,
    var appOwnId: String,
    var appLecId: String,
    var appTopic: String,
    var appDate: String,
    var appTime: String,
    var appTimeEnd: String,
    var appDes: String,
    var appLink: String,
    var appStatus: Boolean,
    var appDecline: Boolean

) {
    constructor() : this("", "", "", "", "", "", "", "", "", false, false)
}
