package de.gransoftware.example.hateoas

import org.springframework.hateoas.RepresentationModel

data class Item(val name: String, val borrowee: String) : RepresentationModel<Item>()
data class Borrowee(val name: String) : RepresentationModel<Borrowee>()
