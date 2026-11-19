package be.vives.jarne.assignment_8.models

object MockupToDo {
    fun getUsers(): List<User> {
        return listOf(
            User(1, "dhostens", "Dirk", "Hostens", "pass123", true),
            User(2, "jdoe", "John", "Doe", "pass123", true),
            User(3, "asmith", "Alice", "Smith", "pass123", true),
            User(4, "bwilliams", "Bob", "Williams", "pass123", false)
        )
    }

    fun getToDos(): List<ToDo> {
        return listOf(
            ToDo(
                number = 1,
                title = "Finish detail ToDo",
                description = "Add extra fields like assigned user, time estimated, ...to the ToDo detail screen",
                createdByUserId = 2,
                createOnDate = System.currentTimeMillis(),
                assignedToUserId = 1,
                finishedOnDate = System.currentTimeMillis(),
                timeEstimated = 20,
            ),
            ToDo(
                number = 2,
                title = "Implement Models",
                description = "Create User and ToDo data classes",
                createdByUserId = 1,
                createOnDate = System.currentTimeMillis(),
                assignedToUserId = 2,
                finishedOnDate = null,
                timeEstimated = 2,
                analysisDone = true,
            ),
            ToDo(
                number = 3,
                title = "Create Mock Data",
                description = "Generate a list of users and todos for testing",
                createdByUserId = 2,
                createOnDate = System.currentTimeMillis(),
                assignedToUserId = 2,
                finishedOnDate = null,
                timeEstimated = 1,
                analysisDone = true,
                developmentDone = true,
            ),
            ToDo(
                number = 4,
                title = "Design UI",
                description = "Create Jetpack Compose layout for ToDo details",
                createdByUserId = 3,
                createOnDate = System.currentTimeMillis(),
                assignedToUserId = 1,
                finishedOnDate = null,
                timeEstimated = 8,
                analysisDone = true,
                developmentDone = true,
                reviewAndTestingDone = true,
            ),
            ToDo(
                number = 5,
                title = "Testing and QA",
                description = "Ensure everything works as expected",
                createdByUserId = 1,
                createOnDate = System.currentTimeMillis(),
                assignedToUserId = 3,
                finishedOnDate = System.currentTimeMillis(),
                timeEstimated = 5,
                analysisDone = true,
                developmentDone = true,
                reviewAndTestingDone = true,
                acceptanceDone = true,
            )
        )
    }
}
