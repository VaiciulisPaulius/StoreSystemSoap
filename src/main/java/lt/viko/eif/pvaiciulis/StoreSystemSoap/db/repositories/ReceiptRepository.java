package lt.viko.eif.pvaiciulis.StoreSystemSoap.db.repositories;

import lt.viko.eif.pvaiciulis.StoreSystemSoap.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Receipt repository that performs DB operations with Receipt table
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
    List<Receipt> findAll();
}
