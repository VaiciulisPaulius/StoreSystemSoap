package lt.viko.eif.pvaiciulis.StoreSystemSoap.service;

import lt.viko.eif.pvaiciulis.StoreSystemSoap.db.repositories.ReceiptRepository;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.Receipt;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.discount.Discount;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.discount.DiscountCard;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.product.EntityProduct;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.product.QuantifiableProduct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private EntityProductService entityProductService;

    @Autowired
    private ModelMapper modelMapper;

    public List<Receipt> list() {
        return receiptRepository.findAll();
    }

    private lt.viko.eif.pvaiciulis.springsoap.gen.Receipt convertToDto(Receipt receipt) {
        lt.viko.eif.pvaiciulis.springsoap.gen.Receipt receiptDto = modelMapper.map(receipt, lt.viko.eif.pvaiciulis.springsoap.gen.Receipt.class);
        return receiptDto;
    }

    public lt.viko.eif.pvaiciulis.springsoap.gen.Receipt findReceipt(int id) {
        Receipt receipt = receiptRepository.findById(id).orElse(null);
        System.out.println(receipt);

        if(receipt == null) return null;
        return convertToDto(receipt);
    }

    public void scanProduct(Receipt receipt, int barCode) {
        EntityProduct product = entityProductService.getProduct(barCode);
        if (product instanceof QuantifiableProduct) {
            receipt.getProducts().add((QuantifiableProduct) product);
            QuantifiableProduct quantProduct = (QuantifiableProduct) product;
            receipt.setSubtotal(receipt.getSubtotal() + quantProduct.calculatePrice());
            applyDiscount(receipt, quantProduct);
        } else if (product instanceof DiscountCard) {
            receipt.setDiscountCard((DiscountCard) product);
            receipt.setTotal(0);
            for (QuantifiableProduct quantifiableProduct : receipt.getProducts()) {
                applyDiscount(receipt, quantifiableProduct);
            }
        }
    }
    public void applyDiscount(Receipt receipt, EntityProduct product) {
        Discount discount = entityProductService.checkDiscount(product);
        if (discount == null) {
            if (product instanceof QuantifiableProduct) {
                receipt.setTotal(receipt.getTotal() + ((QuantifiableProduct) product).calculatePrice());
            } else {
                receipt.setTotal(receipt.getTotal() + product.getPrice());
            }
            return;
        }

        if (discount.getCategory() != null) {
            if (receipt.getDiscountCard() == null) {
                if (product instanceof QuantifiableProduct) {
                    receipt.setTotal(receipt.getTotal() + ((QuantifiableProduct) product).calculatePrice());
                } else {
                    receipt.setTotal(receipt.getTotal() + product.getPrice());
                }
                return;
            }
            if (receipt.getDiscountCard().getCategory() == discount.getCategory()) {
                double discountedPrice = discount.getDiscountPrice();
                if (product instanceof QuantifiableProduct) {
                    discountedPrice *= ((QuantifiableProduct) product).calculatePrice();
                }
                receipt.setTotal(receipt.getTotal() + discountedPrice);
            }
        }
    }
}
