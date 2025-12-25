package cloud.matheusdcunha.fintech.repository;

import cloud.matheusdcunha.fintech.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
