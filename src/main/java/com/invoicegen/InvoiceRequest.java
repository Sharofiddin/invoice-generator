package com.invoicegen;

import java.util.List;

public record InvoiceRequest(
		 Party sender,
		 Party recipient,
		 List<Item> items,
		 String invoiceNumber,
		 String dueDate,
		 String currency,
		 String logoBase64
		) {}
