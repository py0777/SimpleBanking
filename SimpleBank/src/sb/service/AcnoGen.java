package sb.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import org.apache.log4j.Logger;
import sb.common.DaoHandler;


public class AcnoGen {
	static Logger logger = Logger.getLogger(AcnoGen.class);
	private final String namespace = "sb.repository.mapper.AcnoGenMapper";
	
	public IDataSet asGenAcno(IDataSet requestData){
		
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
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
			
			dsTbl = dh.selectOneSql(requestData, namespace+"."+"S001");
			
			sAcno = dsTbl.getField("ACNO");
			
			/*채번 생성*/
			
			requestData.putField("IN_ACNO", sAcno);
			dh.insertSql(requestData,"I001");
			
			/*예수금잔고  생성*/
			dh.insertSql(requestData,"I002");
			
			/*예수금잔고  조회*/
			
			responseData = dh.selectSql(requestData,"S002");
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
