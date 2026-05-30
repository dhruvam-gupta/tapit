package com.app.tapit.data

data class Category(
    val name: String,
    val key: String,
    val emoji: String,
    val words: List<String>
) {
    fun imagePathForWord(word: String): String {
        val fileName = word.lowercase().replace(" ", "_")
        return "file:///android_asset/images/${key}/${fileName}.webp"
    }
}

object CategoryData {

    // Categories in this set will be shown sequentially (1, 2, 3...) instead of randomly.
    val sequentialCategories = setOf("numbers", "colors")

    val categories = listOf(
        Category( // done
            name = "Fruits",
            key = "fruits",
            emoji = "🍎",
            words = listOf(
                "Apple", "Banana", "Cherry", "Dragonfruit", "Elderberry",
                "Fig", "Grape", "Honeydew", "Indian Gooseberry", "Jackfruit",
                "Kiwi", "Lemon", "Mango", "Nectarine", "Orange",
                "Papaya", "Pomegranate", "Quince", "Raspberry", "Strawberry", "Tangerine",
                "Ugli Fruit", "Vanilla Bean", "Watermelon", "Ximenia or Wild Plum",
                "Yellow Passion Fruit", "Zucchini"
            )
        ),
        Category(
            name = "Vegetables",
            key = "vegetables",
            emoji = "🥦",
            words = listOf(
                "Artichoke", "Asparagus", "Beetroot", "Bell Pepper", "Broccoli",
                "Cabbage", "Carrot", "Cauliflower", "Celery", "Corn",
                "Cucumber", "Eggplant", "Garlic", "Ginger", "Green Bean",
                "Kale", "Lettuce", "Mushroom", "Onion", "Pea",
                "Potato", "Pumpkin", "Radish", "Spinach", "Sweet Potato",
                "Tomato"
            )
        ),
        Category( //done
            name = "Animals",
            key = "animals",
            emoji = "🐾",
            words = listOf(
                "Alligator", "Bear", "Cat", "Dog", "Elephant",
                "Fox", "Frog", "Giraffe", "Horse", "Iguana", "Jaguar",
                "Kangaroo", "Lion", "Monkey", "Newt", "Octopus",
                "Panda", "Quoll", "Rabbit", "Snake", "Tiger",
                "Unicorn", "Vampire Bat", "Wolf", "X-Ray Fish",
                "Yak", "Zebra"
            )
        ),
        Category(
            name = "Birds",
            key = "birds",
            emoji = "🐦",
            words = listOf(
                "Albatross", "Bluebird", "Crow", "Dove", "Eagle",
                "Flamingo", "Goldfinch", "Hummingbird", "Ibis", "Jay",
                "Kingfisher", "Lark", "Magpie", "Nightingale", "Owl",
                "Parrot", "Quail", "Quetzal", "Robin", "Sparrow", "Toucan",
                "Umbrellabird", "Vulture", "Woodpecker", "Xenops",
                "Yellow Warbler", "Zebra Finch"
            )
        ),
        Category( // done
            name = "Colors",
            key = "colors",
            emoji = "🎨",
            words = listOf(
                "Blue", "Green", "Pink",
                "Khaki", "Orange", "Purple",
                "Red", "Gray", "White",
                "Yellow", "Black", "Brown"
            )
        ),
        Category(
            name = "Shapes",
            key = "shapes",
            emoji = "🔷",
            words = listOf(
                "Arrow", "Bowtie", "Circle", "Diamond", "Ellipse",
                "Fan", "Grid", "Heart", "Infinity", "Jigsaw",
                "Kite", "Lightning", "Moon", "Nonagon", "Oval",
                "Pentagon", "Quadrilateral", "Rectangle", "Star", "Triangle",
                "U Shape", "V Shape", "Wave", "X Shape",
                "Y Shape", "Zigzag"
            )
        ),
        Category(
            name = "Vehicles",
            key = "vehicles",
            emoji = "🚗",
            words = listOf(
                "Ambulance", "Bus", "Car", "Dump Truck", "Engine",
                "Fire Truck", "Golf Cart", "Helicopter", "Ice Cream Truck", "Jeep",
                "Kayak", "Limousine", "Motorcycle", "Narrowboat", "Off-Roader",
                "Plane", "Quad Bike", "Rickshaw", "Scooter", "Train",
                "Unicycle", "Van", "Wagon", "X-Wing",
                "Yacht", "Zeppelin"
            )
        ),
        Category(
            name = "Body Parts",
            key = "body_parts",
            emoji = "🦵",
            words = listOf(
                "Head", "Hair", "Face", "Eye", "Ear",
                "Nose", "Mouth", "Teeth", "Tongue", "Neck",
                "Shoulder", "Arm", "Elbow", "Hand", "Palm",
                "Fingers", "Nails", "Chest", "Stomach", "Back",
                "Leg", "Thigh", "Knee", "Foot", "Heel",
                "Toes"
            )
        ),
        Category(
            name = "Clothing",
            key = "clothing",
            emoji = "👕",
            words = listOf(
                "Apron", "Belt", "Cap", "Dress", "Earring",
                "Flannel", "Gloves", "Hat", "Inners", "Jacket",
                "Kimono", "Legging", "Mittens", "Necklace", "Overalls",
                "Pants", "Quilt Jacket", "Raincoat", "Scarf", "T-Shirt",
                "Uniform", "Vest", "Windbreaker", "X-Strap Sandal",
                "Yarn Sweater", "Zip Hoodie"
            )
        ),
        Category(
            name = "Everyday Objects",
            key = "everyday_objects",
            emoji = "🏠",
            words = listOf(
                "Alarm Clock", "Book", "Chair", "Door", "Envelope",
                "Fork", "Globe", "Hammer", "Iron", "Ice Cream", "Jar", "Juice",
                "Key", "Lamp", "Mirror", "Notebook", "Oven",
                "Pillow", "Quill", "Ruler", "Scissors", "Table",
                "Umbrella", "Vase", "Watch", "Xylophone",
                "Yarn", "Zipper"
            )
        ),
        Category( // done
            name = "Numbers",
            key = "numbers",
            emoji = "❶❷\n❸❹",
            words = listOf(
                "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty",
                "Twenty-one", "Twenty-two", "Twenty-three", "Twenty-four", "Twenty-five", "Twenty-six", "Twenty-seven", "Twenty-eight", 
                "Twenty-nine", "Thirty"
            )
        )
    )

    fun getCategoryByKey(key: String): Category? {
        return categories.find { it.key == key }
    }
}
