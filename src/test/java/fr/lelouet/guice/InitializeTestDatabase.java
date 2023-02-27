package fr.lelouet.guice;

import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InitializeTestDatabase {

	@Inject
	public InitializeTestDatabase(TransactionManagerQuerydsl mainTransactionManager) {
//		Flyway
//			.configure()
//			.dataSource(mainTransactionManager.dataSource())
//			.locations("/db/migration/release**")
//			.outOfOrder(true)
//			.load()
//			.migrate();
	}

}
