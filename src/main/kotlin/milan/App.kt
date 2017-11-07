package milan

fun main(args: Array<String>) {
    val app = App()

    app.run()
}

class App {

    private val container = Container()

    fun run() {

        println("Hello, my name is Milan, and I will tell you what's in today menu. Just give me a sec…")

        val menuRepo = container.resolveMenuProvider()
        val menu = menuRepo.getMenu()
        val menuRenderer = container.resolveMenuRenderer()

        menuRenderer.renderMenu(menu)
    }

}

class Container {

    fun resolveMenuProvider(): MenuProvider {
        return KolegiumSmeProvider()
    }

    fun resolveMenuRenderer(): MenuRenderer {
        return ConsoleMenuRenderer()
    }

}
