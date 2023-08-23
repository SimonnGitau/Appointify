package com.devsimone.appointify.Models

data class User(var userId: String, var userDp: String, var userName: String, var userMail: String, var userPhone: String, var userPass: String, var userAdmn: String, var admnCheck: Boolean) {
    constructor() : this("", "", "", "", "", "", "", false)
}
