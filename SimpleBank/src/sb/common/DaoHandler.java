package sb.common;

import java.util.HashMap;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import sb.repository.AbstractRepository;

public class DaoHandler extends AbstractRepository{
	
	static Logger logger = Logger.getLogger(DaoHandler.class);
	public IDataSet selectSql(IDataSet requestData, String SqlID) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		try {
			String statement = SqlID;
			
			RecordSetResultHandler resultHandler = new RecordSetResultHandler();
			resultHandler.setRecordSetId("ResultSet");
			sqlSession.select(statement, requestData.getFieldMap(), resultHandler);

			IDataSet ds = new DataSet();
			ds.putRecordSet(resultHandler.getRecordSet());
			logger.debug(ds);
			return ds;
		} finally {
			sqlSession.close();
			/* test */
		}
	}
		
	
	public HashMap selectOneSql(IDataSet requestData, String SqlID) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		try {
			String statement = SqlID;
			
			HashMap hm = sqlSession.selectOne(statement, requestData);
			
			logger.debug(hm);
			return hm;
		} finally {
			sqlSession.close();
			/* test */
			
		}
	}
	
	
	public int insertSql(IDataSet requestData, String SqlID) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		int cnt = 0;
		
		try {
			String statement = SqlID;
			cnt = sqlSession.insert(statement, requestData.getFieldMap());
			
			return cnt;
		} finally {
			sqlSession.close();
			/* test */
		}
	}	
	
	public int updateSql(IDataSet requestData, String SqlID) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		int cnt = 0;
		try {
			String statement = SqlID;
			cnt = sqlSession.update(statement, requestData.getFieldMap());
			
			return cnt;
			
		} finally {
			sqlSession.close();
			/* test */
		}
	}	
}
