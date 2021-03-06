/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agoncal.sample.quarkus.book.rest;

import org.agoncal.sample.quarkus.book.domain.Book;
import org.agoncal.sample.quarkus.book.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static java.util.Optional.ofNullable;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@ApplicationScoped
@Path("books")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    @Inject
    private BookRepository bookRepository;

    @GET
    @Path("/{id : \\d+}")
    @Produces(APPLICATION_JSON)
    public Response findById(@PathParam("id") final Long id) {
        log.info("Getting the book " + id);
        return ofNullable(bookRepository.findById(id))
            .map(Response::ok)
            .orElse(status(NOT_FOUND))
            .build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response findAll() {
        log.info("Getting all the books");
        return ok(bookRepository.findAll()).build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response create(Book book, @Context UriInfo uriInfo) {
        log.info("Creating the book " + book);

        log.info("Invoking the number-api");

//   TODO     NumbersApi numberApi = new ApiClient().buildNumberApiClient();
//        String isbn = numberApi.generateBookNumber();
        String isbn = "test";
        book.setIsbn(isbn);

        final Book created = bookRepository.create(book);
        URI createdURI = uriInfo.getBaseUriBuilder().path(String.valueOf(created.getId())).build();
        return Response.created(createdURI).build();
    }

    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response update(Book book) {
        log.info("Updating the book " + book);
        return ok(bookRepository.update(book)).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    public Response delete(@PathParam("id") final Long id) {
        log.info("Deleting the book " + id);
        bookRepository.deleteById(id);
        return noContent().build();
    }
}
