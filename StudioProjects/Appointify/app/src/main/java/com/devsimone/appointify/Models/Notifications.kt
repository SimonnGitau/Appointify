package com.devsimone.appointify.Models

data class Notifications(

    var notID: String,
    var notText: String

) {
    constructor() : this("", "")
}
