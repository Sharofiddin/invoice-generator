package com.invoicegen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.xhtmlrenderer.pdf.ITextRenderer;

import io.quarkus.logging.Log;
import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/invoice")
@Consumes(MediaType.APPLICATION_JSON)
@Produces("application/pdf")
public class InvoiceResource {
    
	@Inject
	Template invoice;
	
	
    @POST
    public Response generate(InvoiceRequest invoiceRequest) throws IOException {
        String html = invoice.data("invoice", invoiceRequest).render();
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
          
          ITextRenderer renderer = new ITextRenderer();
          System.out.println(html);
          renderer.setDocumentFromString(html);
          renderer.layout();
          renderer.createPDF(baos);
          return Response.ok(baos.toByteArray()).header("Content-Disposition", "inline; filename=" + invoiceRequest.invoiceNumber() + ".pdf").build();
        }
        catch (Exception e) {
        	Log.error(e);
        	e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), 500);
		}
        
    }
}
