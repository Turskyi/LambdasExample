typealias DoubleConversion = (Double) -> Double

fun convert(
    x: Double,
    converter: DoubleConversion
): Double {
    val result = converter(x)
    println("$x is converted to $result")
    return result
}

fun getConversionLambda(str: String): DoubleConversion {
    return when (str) {
        "CentigradeToFahrenheit" -> {
            { it * 1.8 + 32 }
        }
        "KgsToPounds" -> {
            { it * 2.204623 }
        }
        "PoundsToUSTons" -> {
            { it / 2000.0 }
        }
        else -> {
            { it }
        }
    }
}

fun combine(
    lambda1: DoubleConversion,
    lambda2: DoubleConversion
) = { x: Double -> lambda2(lambda1(x)) }

data class Grocery(
    val name: String,
    val category: String,
    val unit: String,
    val unitPrice: Double
)

fun search(list: List<Grocery>, criteria: (g: Grocery) -> Boolean) {
    for (grocery in list) {
        if (criteria(grocery)) {
            println(grocery.name)
        }
    }
}

/**
When we run the code, the following text gets printed in the IDE’s output window:
Convert 2.5kg to Pounds: 5.5115575
17.4 is converted to 0.0191802201
17.4 kgs is 0.0191802201 US tons
------Next Example ---------
Expensive ingredients:
Olive oil
All vegetables:
Tomatoes
Mushrooms
All packs:
Bagels
Ice cream
 */

fun main() {
    /* Convert 2.5kg to Pounds */
    println("Convert 2.5kg to Pounds: ${getConversionLambda("KgsToPounds")(2.5)}")

    /* Define two conversion lambdas */
    val kgsToPoundsLambda = getConversionLambda("KgsToPounds")
    val poundsToUSTonsLambda = getConversionLambda("PoundsToUSTons")

    /* Combine the two lambdas to create a new one */
    val kgsToUSTonsLambda = combine(kgsToPoundsLambda, poundsToUSTonsLambda)

    /* Use the new lambda to convert 17.4 to US tons */
    val value = 17.4
    println("$value kgs is ${convert(value, kgsToUSTonsLambda)} US tons")

    println("------Next Example ---------")

    val groceries = listOf(
        Grocery("Tomatoes", "Vegetable", "lb", 3.0),

        Grocery("Mushrooms", "Vegetable", "lb", 4.0),

        Grocery("Bagels", "Bakery", "Pack", 1.5),
        Grocery("Olive oil", "Pantry", "Bottle", 6.0),
        Grocery("Ice cream", "Frozen", "Pack", 3.0)
    )

    println("Expensive ingredients:")

    search(groceries) { grocery: Grocery -> grocery.unitPrice > 5.0 }

    println("All vegetables:")

    search(groceries) { grocery: Grocery -> grocery.category == "Vegetable" }

    println("All packs:")

    search(groceries) { grocery: Grocery -> grocery.unit == "Pack" }
}