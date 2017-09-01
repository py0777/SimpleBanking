package sb.repository;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public abstract class AbstractRepository {
	private static SqlSessionFactory sqlSessionFactory;
	/*20170902세션관리로 추가*/
	private static final ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();

	static {
		setSqlSessionFactory();
	}

	private static void setSqlSessionFactory() {
		String resource = "mybatis-config.xml";
		InputStream inputStream;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

	protected SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
	/*20170902세션관리로 추가*/
	public static SqlSession getSession() {
		// To obtain a connection from the ThreadLocal. 
		SqlSession session = (SqlSession) threadLocal.get();

		// If you don't have connection, has a new connection
		if (session == null) {
			session = sqlSessionFactory.openSession();
			// Connect to record ThreadLocal get out, so that next time you use. 
			threadLocal.set(session);
		}
		return session;
	}
	
	/*20170902세션관리로 추가*/
	public static void closeSession() {
		SqlSession session = (SqlSession) threadLocal.get();
		// The ThreadLocal emptied, said the current thread has no connection. 
		threadLocal.set(null);
		// Connection to the connection pool
		if (session != null) {
			session.close();
		}
	}

}
