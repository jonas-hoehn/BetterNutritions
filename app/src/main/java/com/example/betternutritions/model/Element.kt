package com.example.betternutritions.model

data class Element(
    val action_element: String,
    val image_element: ImageElement,
    val panel_element: PanelElement,
    val panel_group_element: PanelGroupElement,
    val table_element: TableElement,
    val text_element: TextElement,
    val type: String
)