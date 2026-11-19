package be.vives.jarne.assignment_8.models

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    var id: Int,
    var userName: String,
    var firstName: String,
    var lastName: String,
    var password: String,
    var isActive: Boolean
) {
    // Empty constructor
    constructor() : this(0, "", "", "", "", false)
}
