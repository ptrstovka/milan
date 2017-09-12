package milan

class ConsoleMenuRenderer: MenuRenderer {

    override fun renderMenu(menu: Menu) {
        if (!menu.isFound || menu.items.isEmpty()) {
            println("Hmm, looks like there is no menu for today.")
            return
        }

        println("Here is what I've found:")
        println()
        menu.items.forEach {
            println(it)
        }
        println()
        println("Yours Milan.")
    }
}