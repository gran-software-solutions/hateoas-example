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
class BorroweeController {

    @GetMapping("/borrowee")
    fun getBorrowees(
        @RequestParam(value = "page", defaultValue = "0") page: Int = 0,
        @RequestParam(value = "size", defaultValue = "2") size: Int = 2,
    ): ResponseEntity<CustomCollectionModel<Borrowee>> {
        val borrowees = listOf(
            Borrowee("John").apply {
                add(linkTo(methodOn(BorroweeController::class.java).getBorrowee(this.name)).withSelfRel())
                add(linkTo(methodOn(ItemController::class.java).getItemByBorrowee(this.name)).withRel("items"))
            },
            Borrowee("Tom").apply {
                add(linkTo(methodOn(BorroweeController::class.java).getBorrowee(this.name)).withSelfRel())
                add(linkTo(methodOn(ItemController::class.java).getItemByBorrowee(this.name)).withRel("items"))
            }
        )

        val pageable = PageRequest.of(page, size)
        val startIndex = pageable.offset.toInt()
        val endIndex = min(startIndex + pageable.pageSize, borrowees.size)
        val selected = borrowees.subList(startIndex, endIndex)

        val totalPages = ceil(borrowees.size.toDouble() / size).toInt()

        return ResponseEntity.ok(
            CustomCollectionModel(selected).apply {
                add(linkTo(methodOn(BorroweeController::class.java).getBorrowees(page, size)).withSelfRel())

                if (page > 0) {
                    add(linkTo(methodOn(BorroweeController::class.java).getBorrowees(0, size)).withRel("first"))
                    add(linkTo(methodOn(BorroweeController::class.java).getBorrowees(page - 1, size)).withRel("prev"))
                }

                if (page < totalPages - 1) {
                    add(linkTo(methodOn(BorroweeController::class.java).getBorrowees(page + 1, size)).withRel("next"))
                    add(linkTo(methodOn(BorroweeController::class.java).getBorrowees(totalPages - 1, size)).withRel("last"))
                }
            }
        )
    }

    @GetMapping("/borrowee/{name}")
    fun getBorrowee(@PathVariable name: String): ResponseEntity<EntityModel<Borrowee>> {
        val item = Borrowee(name).apply {
            add(linkTo(methodOn(BorroweeController::class.java).getBorrowee(name)).withSelfRel())
            add(linkTo(methodOn(ItemController::class.java).getItemByBorrowee(name)).withRel("items"))
        }

        return ResponseEntity.ok(EntityModel.of(item))
    }
}
