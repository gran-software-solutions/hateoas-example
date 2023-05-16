package de.gransoftware.example.hateoas

import org.springframework.hateoas.RepresentationModel

class CustomCollectionModel<T>(val data: List<T>) : RepresentationModel<CustomCollectionModel<T>?>()
