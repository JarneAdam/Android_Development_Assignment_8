package be.vives.jarne.assignment_8.models

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

enum class Status {
    NEW, ASSIGNED, FINISHED
}

@Entity(
    tableName = "todos",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["createdByUserId"],
            onDelete = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["assignedToUserId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index("createdByUserId"),
        Index("assignedToUserId"),
    ],
)
data class ToDo(
    @PrimaryKey
    var number: Int,
    var title: String,
    var description: String,
    var createdByUserId: Int,
    var createOnDate: Long,
    var assignedToUserId: Int?,
    var finishedOnDate: Long?,
    var timeEstimated: Int,
    var analysisDone: Boolean = false,
    var developmentDone: Boolean = false,
    var reviewAndTestingDone: Boolean = false,
    var acceptanceDone: Boolean = false,
) {
    val status: Status
        get() {
            return if ((assignedToUserId != null) && (finishedOnDate != null)) {
                Status.FINISHED
            } else if (assignedToUserId != null) {
                Status.ASSIGNED
            } else {
                Status.NEW
            }
        }

    val statusDescription: String
        get() = when (status) {
            Status.NEW -> "New"
            Status.ASSIGNED -> "Assigned"
            Status.FINISHED -> "Finished"
        }

    val timeRemaining: Double
        get() {
            return when {
                acceptanceDone -> 0.0
                reviewAndTestingDone -> timeEstimated * 0.10
                developmentDone -> timeEstimated * 0.30
                analysisDone -> timeEstimated * 0.85
                else -> timeEstimated.toDouble()
            }
        }
}
