package sb.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import sb.common.RecordSetResultHandler;
import sb.repository.AbstractRepository;

public class GenAcno extends AbstractRepository{
	static Logger logger = Logger.getLogger(GenAcno.class);
	private final String namespace = "sb.repository.mapper.GenAcnoMapper";
	
	public IDataSet selectSql(IDataSet requestData, String SqlID) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		try {
			String statement = namespace + "."+SqlID;
			

			RecordSetResultHandler resultHandler = new RecordSetResultHandler();
			resultHandler.setRecordSetId("ResultSet");

			logger.debug("strat!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			sqlSession.select(statement, requestData.getFieldMap(), resultHandler);

			IDataSet ds = new DataSet();
			ds.putRecordSet(resultHandler.getRecordSet());

			return ds;
		} finally {
			sqlSession.close();
			/* test */
		}
	}
	
	public void insertSql(IDataSet requestData, String SqlID) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		try {
			String statement = namespace + "."+SqlID;

			logger.debug("strat!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			sqlSession.insert(statement, requestData.getFieldMap());

		} finally {
			sqlSession.close();
			/* test */
		}
	}	
	public IDataSet asGenAcno(IDataSet requestData){
		
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		IDataSet dsTbl = null;
		String today = "";
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss초 E(a)");
		String rtnMsg = "";
		String sAcno = "";
		
		try
		{
			Date d = gc.getTime();
			today = sdformat.format(d);
			/*계좌번호 채번*/
			dsTbl = selectSql(requestData,"S001");
			
			sAcno = dsTbl.getField("ACNO");
			
			/*채번 생성*/
			
			requestData.putField("IN_ACNO", sAcno);
			insertSql(requestData,"I001");
			
			/*예수금잔고  생성*/
			insertSql(requestData,"I002");
			
			/*예수금잔고  조회*/
			
			responseData = selectSql(requestData,"S002");
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
		
		/*************************************************************
		 * Retrun Result Data
		 *************************************************************/
		rtnMsg = "처리완료되었습니다.";
		responseData.putField("rtnMsg", today + " " + rtnMsg);
		responseData.setOkResultMessage("OK", new String[]{"처리완료되었습니다."});
		
		return responseData;
		
		
	}

}
