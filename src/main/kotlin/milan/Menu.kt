package milan

data class Menu(
        public val isFound: Boolean,
        public val items: List<String> = listOf()
)