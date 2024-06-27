package lt.viko.eif.pvaiciulis.StoreSystemSoap.endpoint;

import lt.viko.eif.pvaiciulis.StoreSystemSoap.service.ReceiptService;
import lt.viko.eif.pvaiciulis.springsoap.gen.GetReceiptRequest;
import lt.viko.eif.pvaiciulis.springsoap.gen.GetReceiptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ReceiptEndpoint {
    private static final String NAMESPACE_URI = "http://eif.viko.lt/pvaiciulis/springsoap/gen";

    private ReceiptService receiptService;

    @Autowired
    public ReceiptEndpoint(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getReceiptRequest")
    @ResponsePayload
    public GetReceiptResponse getReceipt(@RequestPayload GetReceiptRequest request) {
        GetReceiptResponse response = new GetReceiptResponse();
        response.setReceipt(receiptService.findReceipt(request.getId()));

        return response;
    }
}
