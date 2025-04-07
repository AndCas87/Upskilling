package customer.upskilling.handlers;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sap.cds.ql.Select;
import com.sap.cds.ql.cqn.CqnInsert;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import com.sap.cds.services.request.ParameterInfo;

import cds.gen.catalogservice.CatalogService_;
import cds.gen.my.bookshop.Books_;
import cds.gen.catalogservice.DestExternal_;

import com.vdm.services.DefaultMedestinationService;
import customer.upskilling.helper.ConnectivityHelper;
import customer.upskilling.utilities.Utilities;

import com.vdm.namespaces.medestination.Books;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {

	@Autowired
	PersistenceService db;
	DefaultMedestinationService service = new DefaultMedestinationService();

	@On(event = CqnService.EVENT_CREATE, entity = Books_.CDS_NAME)
	public void books(CdsCreateEventContext context) {

		CqnInsert q = context.getCqn();
		context.setResult(db.run(q));
	}

	// Esempio lettura vdm
	@On(event = CqnService.EVENT_READ, entity = DestExternal_.CDS_NAME)
	public void destExt(CdsReadEventContext context) {

		context.setResult(Utilities.convertListToMap(
				service.getAllBooks().execute(ConnectivityHelper.getDestination("DESTINATION_TEST",context.getUserInfo()))));
	}

	@On(event = CqnService.EVENT_READ, entity = Books_.CDS_NAME)
	public void books(CdsReadEventContext context) {
		ParameterInfo paramInfo = context.getParameterInfo();
		Long skip = Long.valueOf(paramInfo.getQueryParameter("$skip").toString());
		Long top = Long.valueOf(paramInfo.getQueryParameter("$top").toString());

		CqnSelect q = Select.from(Books_.CDS_NAME).limit(top, skip);
		context.setResult(db.run(q));
	}

	// @On(event = CqnService.EVENT_READ, entity = Books_.CDS_NAME)
	// public void books(CdsReadEventContext context) {

	// CqnSelect q = context.getCqn();
	// context.setResult(db.run(q));
	// }

	// @On(event = CqnService.EVENT_READ, entity = Books_.CDS_NAME)
	// public void books(CdsReadEventContext context) {
	// CqnSelect q = Select.from(Books_.CDS_NAME).where("ID eq 1");
	// context.setResult(db.run(q));
	// }

	// @After(event = CqnService.EVENT_READ)
	// public void discountBooks(Stream<Books> books) {
	// books.filter(b -> b.getTitle() != null && b.getStock() != null)
	// .filter(b -> b.getStock() > 200)
	// .forEach(b -> b.setTitle(b.getTitle() + " (discounted)"));
	// }

}