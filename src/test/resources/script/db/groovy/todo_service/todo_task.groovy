package script.db.groovy.todo_service

databaseChangeLog(logicalFilePath: 'todo_task.groovy') {
    changeSet(id: '2019-05-30-todo_task', author: 'your.email@email.com') {
        createTable(tableName: "TODO_TASK") {
            column(name: 'ID', type: 'BIGINT UNSIGNED', remarks: 'ID', autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: 'EMPLOYEE_ID', type: 'BIGINT', remarks: '员工ID')
            column(name: 'STATE', type: 'VARCHAR(36)', remarks: '状态')
            column(name: 'TASK_NUMBER', type: 'VARCHAR(64)', remarks: '任务编号') {
                constraints(unique: true)
            }
            column(name: 'TASK_DESCRIPTION', type: 'VARCHAR(256)', remarks: '任务描述')

            column(name: "OBJECT_VERSION_NUMBER", type: "BIGINT", defaultValue: "1")
            column(name: "CREATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATION_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "LAST_UPDATE_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
        }
    }
}
