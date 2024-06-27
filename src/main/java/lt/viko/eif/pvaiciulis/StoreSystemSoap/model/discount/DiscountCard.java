package lt.viko.eif.pvaiciulis.StoreSystemSoap.model.discount;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.Person;
import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.product.EntityProduct;

/**
 * Class for representing discount card entities.
 */
@Entity
public class DiscountCard extends EntityProduct {
    private DiscountCardCategory category;

    public static final int DISCOUNT_CARD_MIN_BARCODE = 1000000;
    public static final int DISCOUNT_CARD_MAX_BARCODE = 9999999;

    @OneToOne(targetEntity = Person.class, fetch = FetchType.EAGER)
    private Person person;

    /**
     *
     * @param category  the discount card category (member, worker, etc)
     * @param person    the person the discount card is associated with
     */
    public DiscountCard(DiscountCardCategory category, Person person, int barcode) {
        super("Discount card", barcode, 2);
        this.category = category;
        this.person = person;
    }

    /** converts discount card object into a string representing all of its states.
     *
     * @return a string representation of discount card object.
     */
    @Override
    public String toString() {
        return String.format("\t\tcategory: %s\n" + "\t\tperson: %s\n",
                category, person);
    }

    /**
     * Default DiscountCard constructor
     */
    public DiscountCard() {
    }

    /** Gets the current discount card category
     *
     * @return DiscountCardCategory enum
     */
    public DiscountCardCategory getCategory() {
        return category;
    }

    /** Sets the discount card's category
     *
     * @param category
     */
    public void setCategory(DiscountCardCategory category) {
        this.category = category;
    }

    /** Gets the person that's associated with this discount card.
     *
     * @return returns the person which this discount card is associated with.
     */
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
