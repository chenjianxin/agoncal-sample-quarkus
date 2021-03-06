package org.agoncal.sample.quarkus;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// tag::adocSnippet[]
@Path("/books")
@Produces(MediaType.TEXT_PLAIN)
public class BookResource {

  @Inject
  @ThirteenDigits
  NumberGenerator numberGenerator;

  @GET
  @Path("/isbn")
  @Produces(MediaType.TEXT_PLAIN)
  public String getISBN() {
    return numberGenerator.generateNumber();
  }
}
// end::adocSnippet[]
