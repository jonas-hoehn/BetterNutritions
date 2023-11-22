package com.example.betternutritions.model

data class Adjustments(
    val origins_of_ingredients: OriginsOfIngredients,
    val packaging: Packaging,
    val production_system: ProductionSystem,
    val threatened_species: ThreatenedSpecies
)