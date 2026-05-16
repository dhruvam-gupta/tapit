package com.app.tapit.data

data class Category(
    val name: String,
    val key: String,
    val emoji: String,
    val words: List<String>
) {
    val thumbnailPath: String
        get() = "file:///android_asset/images/categories/${key}.webp"

    fun imagePathForWord(word: String): String {
        val fileName = word.lowercase().replace(" ", "_")
        return "file:///android_asset/images/${key}/${fileName}.webp"
    }
}

object CategoryData {

    val categories = listOf(
        Category(
            name = "Fruits",
            key = "fruits",
            emoji = "🍎",
            words = listOf(
                "Apple", "Banana", "Cherry", "Dragonfruit", "Elderberry",
                "Fig", "Grape", "Honeydew", "Indian Gooseberry", "Jackfruit",
                "Kiwi", "Lemon", "Mango", "Nectarine", "Orange",
                "Papaya", "Quince", "Raspberry", "Strawberry", "Tangerine",
                "Ugli Fruit", "Vanilla Bean", "Watermelon", "Ximenia",
                "Yellow Passion Fruit", "Zucchini"
            )
        ),
        Category(
            name = "Vegetables",
            key = "vegetables",
            emoji = "🥦",
            words = listOf(
                "Asparagus", "Broccoli", "Carrot", "Daikon", "Eggplant",
                "Fennel", "Garlic", "Horseradish", "Iceberg Lettuce", "Jalapeno",
                "Kale", "Leek", "Mushroom", "Napa Cabbage", "Onion",
                "Potato", "Quinoa", "Radish", "Spinach", "Tomato",
                "Ube", "Vidalia Onion", "Watercress", "Xigua",
                "Yam", "Zinger"
            )
        ),
        Category(
            name = "Animals",
            key = "animals",
            emoji = "🐾",
            words = listOf(
                "Alligator", "Bear", "Cat", "Dog", "Elephant",
                "Fox", "Giraffe", "Horse", "Iguana", "Jaguar",
                "Kangaroo", "Lion", "Monkey", "Newt", "Octopus",
                "Panda", "Quail", "Rabbit", "Snake", "Tiger",
                "Unicorn", "Vulture", "Wolf", "X-Ray Fish",
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
                "Parrot", "Quetzal", "Robin", "Sparrow", "Toucan",
                "Umbrellabird", "Vulture", "Woodpecker", "Xenops",
                "Yellow Warbler", "Zebra Finch"
            )
        ),
        Category(
            name = "Colors",
            key = "colors",
            emoji = "🎨",
            words = listOf(
                "Amber", "Blue", "Cyan", "Dark Green", "Emerald",
                "Fuchsia", "Gold", "Hot Pink", "Indigo", "Jade",
                "Khaki", "Lavender", "Magenta", "Navy", "Orange",
                "Pink", "Quartz", "Red", "Silver", "Teal",
                "Umber", "Violet", "White", "Xanadu",
                "Yellow", "Zinc White"
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
                "Ankle", "Belly", "Cheek", "Dimple", "Ear",
                "Finger", "Gum", "Hair", "Eye", "Jaw",
                "Knee", "Leg", "Mouth", "Nose", "Oval Face",
                "Palm", "Chin", "Rib", "Shoulder", "Thumb",
                "Upper Arm", "Vein", "Wrist", "Elbow",
                "Yawn", "Zygomatic"
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
                "Fork", "Globe", "Hammer", "Iron", "Jar",
                "Key", "Lamp", "Mirror", "Notebook", "Oven",
                "Pillow", "Quill", "Ruler", "Scissors", "Table",
                "Umbrella", "Vase", "Watch", "Xylophone",
                "Yarn", "Zipper"
            )
        ),
        Category(
            name = "Numbers",
            key = "numbers",
            emoji = "🔢",
            words = listOf(
                "One", "Two", "Three", "Four", "Five",
                "Six", "Seven", "Eight", "Nine", "Ten",
                "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
                "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty",
                "Twenty One", "Twenty Two", "Twenty Three", "Twenty Four",
                "Twenty Five", "Twenty Six"
            )
        )
    )

    fun getCategoryByKey(key: String): Category? {
        return categories.find { it.key == key }
    }
}
