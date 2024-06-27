package lt.viko.eif.pvaiciulis.StoreSystemSoap.db;

import lt.viko.eif.pvaiciulis.StoreSystemSoap.db.repositories.PersonRepository;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.db.repositories.ReceiptRepository;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.db.repositories.discount.DiscountCardRepository;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.db.repositories.discount.DiscountRepository;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.db.repositories.product.QuantifiableProductRepository;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.Person;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.Receipt;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.discount.Discount;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.discount.DiscountCard;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.discount.DiscountCardCategory;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.product.AmountType;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.product.QuantifiableProduct;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.service.DiscountCardService;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class DBLoader {
    private ReceiptRepository receiptRepository;
    private QuantifiableProductRepository quantifiableProductRepository;
	private PersonRepository personRepository;
	private DiscountCardRepository discountCardRepository;
	private DiscountRepository discountRepository;

	private DiscountCardService discountCardService;
	private ReceiptService receiptService;

    @Autowired
    public void DBLoader(ReceiptRepository receiptRepository, QuantifiableProductRepository quantifiableProductRepository, PersonRepository personRepository, DiscountCardRepository discountCardRepository, DiscountRepository discountRepository, @Lazy DiscountCardService discountCardService, @Lazy ReceiptService receiptService) {
        this.receiptRepository = receiptRepository;
        this.quantifiableProductRepository = quantifiableProductRepository;
		this.personRepository = personRepository;
		this.discountCardRepository = discountCardRepository;
		this.discountRepository = discountRepository;
		this.discountCardService = discountCardService;
		this.receiptService = receiptService;
        //loadData();
    }
    public void loadData() {
		QuantifiableProduct milk = new QuantifiableProduct("Lactose free milk",
				2098765112, 1.99, AmountType.amount, 1);

		QuantifiableProduct steak = new QuantifiableProduct("Meat",
				2098765115, 10.99, AmountType.amount, 1);

		QuantifiableProduct rubiksCube = new QuantifiableProduct("Rubiks cube",
				2098744513, 4.99, AmountType.amount, 1);

		QuantifiableProduct chips = new QuantifiableProduct("Chips",
				2098741285, 4.5, AmountType.amount, 2);

		QuantifiableProduct banana = new QuantifiableProduct("Banana",
				2098744413, 1.2, AmountType.kg, 1.2);

		Person person1 = new Person("Paulius", "Vaiciulis", "+37069744323", Date.valueOf("2002-05-31"));
		DiscountCard discountCard = new DiscountCard(DiscountCardCategory.Member, person1, discountCardService.generateUniqueBarcode());

		Discount steakDiscount = new Discount(7.99, DiscountCardCategory.Member, steak);
		Discount rubiksCubeDiscount = new Discount(DiscountCardCategory.Member, 50, rubiksCube);

		//jdbc:h2:tcp://10.0.30.42:9092/mem:test;DB_CLOSE_DELAY=-1;MODE=MySQL

		quantifiableProductRepository.save(milk);
		quantifiableProductRepository.save(steak);
		quantifiableProductRepository.save(rubiksCube);
		quantifiableProductRepository.save(chips);
		quantifiableProductRepository.save(banana);

		personRepository.save(person1);

		discountCardRepository.save(discountCard);

		discountRepository.save(steakDiscount);
		discountRepository.save(rubiksCubeDiscount);

		Receipt receipt = new Receipt();
		receiptService.scanProduct(receipt, milk.getBarCode());
		receiptService.scanProduct(receipt, steak.getBarCode());
		receiptService.scanProduct(receipt, rubiksCube.getBarCode());
		receiptService.scanProduct(receipt, discountCard.getBarCode());

		receiptService.scanProduct(receipt,2098765112);
		receiptService.scanProduct(receipt, 2098765115);
		receiptService.scanProduct(receipt, 2098744513);
		receipt.finishTransaction();

		receiptRepository.save(receipt);
    }
}
