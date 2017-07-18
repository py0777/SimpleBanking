package sb.common;

import org.apache.ibatis.session.SqlSession;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import sb.repository.AbstractRepository;

public class DaoHandler extends AbstractRepository{
	public IDataSet selectSql(IDataSet requestData, String SqlID) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		try {
			String statement = SqlID;
			
			RecordSetResultHandler resultHandler = new RecordSetResultHandler();
			resultHandler.setRecordSetId("ResultSet");
			sqlSession.select(statement, requestData.getFieldMap(), resultHandler);

			IDataSet ds = new DataSet();
			ds.putRecordSet(resultHandler.getRecordSet());

			return ds;
		} finally {
			sqlSession.close();
			/* test */
		}
	}
		
	
	public IDataSet selectOneSql(IDataSet requestData, String SqlID) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		try {
			String statement = SqlID;
			
			RecordSetResultHandler resultHandler = new RecordSetResultHandler();
			resultHandler.setRecordSetId("ResultSet");
			sqlSession.selectOne(statement, requestData.getFieldMap());

			IDataSet ds = new DataSet();
			ds.putRecordSet(resultHandler.getRecordSet());

			return ds;
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
