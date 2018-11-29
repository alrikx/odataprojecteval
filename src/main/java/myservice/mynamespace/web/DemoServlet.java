package myservice.mynamespace.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.edmx.EdmxReference;

import myservice.mynamespace.data.Storage;
import myservice.mynamespace.service.DemoEdmProvider;
import myservice.mynamespace.service.DemoEntityCollectionProcessor;
import myservice.mynamespace.service.DemoEntityProcessor;
import myservice.mynamespace.service.DemoPrimitiveProcessor;

public class DemoServlet extends HttpServlet {

	  private static final long serialVersionUID = 1L;


	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 try {
		        HttpSession session = req.getSession(true);
		        Storage storage = (Storage) session.getAttribute(Storage.class.getName());
		        if (storage == null) {
		           storage = new Storage();
		           session.setAttribute(Storage.class.getName(), storage);
		        }

		        // create odata handler and configure it with EdmProvider and Processor
		        OData odata = OData.newInstance();
		        ServiceMetadata edm = odata.createServiceMetadata(new DemoEdmProvider(), new ArrayList<EdmxReference>());
		        ODataHttpHandler handler = odata.createHandler(edm);
		        handler.register(new DemoEntityCollectionProcessor(storage));
		        handler.register(new DemoEntityProcessor(storage));
		        handler.register(new DemoPrimitiveProcessor(storage));

		      // let the handler do the work
		      handler.process(req, resp);
		    } catch (RuntimeException e) {
		      throw new ServletException(e);
		    }
		  
	}

}
