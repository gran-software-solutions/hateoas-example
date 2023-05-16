package de.gransoftware.example.hateoas

import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.ceil
import kotlin.math.min

@RestController
@CrossOrigin(origins =  ["http://localhost:3000"] )
class ItemController {

    @GetMapping("/items")
    fun getItems(
        @RequestParam(value = "page", defaultValue = "0") page: Int = 0,
        @RequestParam(value = "size", defaultValue = "2") size: Int = 2,
    ): ResponseEntity<CustomCollectionModel<Item>> {
        val items = listOf(
            Item("Blue Pen", "John").apply {
                add(linkTo(methodOn(ItemController::class.java).getItem(this.name)).withSelfRel())
                add(linkTo(methodOn(BorroweeController::class.java).getBorrowee(this.borrowee)).withRel("borrowee"))
            },
            Item("Notebook", "John").apply {
                add(linkTo(methodOn(ItemController::class.java).getItem(this.name)).withSelfRel())
                add(linkTo(methodOn(BorroweeController::class.java).getBorrowee(this.borrowee)).withRel("borrowee"))
            },
            Item("Headphones", "John").apply {
                add(linkTo(methodOn(ItemController::class.java).getItem(this.name)).withSelfRel())
                add(linkTo(methodOn(BorroweeController::class.java).getBorrowee(this.borrowee)).withRel("borrowee"))
            }
        )

        val pageable = PageRequest.of(page, size)
        val startIndex = pageable.offset.toInt()
        val endIndex = min(startIndex + pageable.pageSize, items.size)
        val selected = items.subList(startIndex, endIndex)

        val totalPages = ceil(items.size.toDouble() / size).toInt()

        return ResponseEntity.ok(
            CustomCollectionModel(selected).apply {
                add(linkTo(methodOn(ItemController::class.java).getItems(page, size)).withSelfRel())

                if (page > 0) {
                    add(linkTo(methodOn(ItemController::class.java).getItems(0, size)).withRel("first"))
                    add(linkTo(methodOn(ItemController::class.java).getItems(page - 1, size)).withRel("prev"))
                }

                if (page < totalPages - 1) {
                    add(linkTo(methodOn(ItemController::class.java).getItems(page + 1, size)).withRel("next"))
                    add(linkTo(methodOn(ItemController::class.java).getItems(totalPages - 1, size)).withRel("last"))
                }
            }
        )
    }

    @GetMapping("/items/{name}")
    fun getItem(@PathVariable name: String): ResponseEntity<EntityModel<Item>> {
        val item = Item(name, "John").apply {
            add(linkTo(methodOn(ItemController::class.java).getItem(name)).withSelfRel())
            add(linkTo(methodOn(BorroweeController::class.java).getBorrowee(this.borrowee)).withRel("borrowee"))
        }

        return ResponseEntity.ok(EntityModel.of(item))
    }

    @GetMapping("/items/by/{name}")
    fun getItemByBorrowee(@PathVariable name: String): ResponseEntity<CustomCollectionModel<Item>> {
        val items = listOf(
            Item("Blue Pen", name).apply {
                add(linkTo(methodOn(ItemController::class.java).getItem(this.name)).withSelfRel())
                add(linkTo(methodOn(BorroweeController::class.java).getBorrowee(name)).withRel("borrowee"))
            },
            Item("Notebook", name).apply {
                add(linkTo(methodOn(ItemController::class.java).getItem(this.name)).withSelfRel())
                add(linkTo(methodOn(BorroweeController::class.java).getBorrowee(name)).withRel("borrowee"))
            }
        )

        return ResponseEntity.ok(CustomCollectionModel(items))
    }
}