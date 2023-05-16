package de.gransoftware.example.hateoas

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@CrossOrigin(origins =  ["http://localhost:3000"] )
class HATEOASController {
    @GetMapping("/")
    fun getItem(): RepresentationModel<*> {
        return RepresentationModel.of(String).apply {
            add(linkTo(methodOn(ItemController::class.java).getItems()).withRel("items"))
            add(linkTo(methodOn(BorroweeController::class.java).getBorrowees()).withRel("borrowees"))
        }
    }
}
