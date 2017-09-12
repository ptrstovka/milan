package milan

fun dd(action: () -> Unit) {
    action.invoke()
    System.exit(0)
}

fun dd(variable: Any?) {
    dd {
        println(variable)
    }
}
