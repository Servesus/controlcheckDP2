
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.XXXXX;

@Repository
public interface XXXXXRepository extends JpaRepository<XXXXX, Integer> {

	@Query("select x from XXXXX x where x.application.id=?1")
	Collection<XXXXX> getXXXXXs(int applicationId);

	@Query("select x from XXXXX x where x.application.id=?1 and x.isFinal = true")
	Collection<XXXXX> getXXXXXsC(int applicationId);
}
