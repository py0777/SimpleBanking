package sb.service.cm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;

import org.apache.log4j.Logger;

import sb.common.DaoHandler;


public class CmAcnoGen {
	static Logger logger = Logger.getLogger(CmAcnoGen.class);
	private final String namespace = "sb.repository.mapper.AcnoGenMapper";
	
	public IDataSet cmAcnoGen(IDataSet requestData)throws Exception{
		
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		
		String today = "";
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss초 E(a)");
		String rtnMsg = "";
		String sAcno = "";
		
		int    rsCnt = 0;  /*결과 건수*/
		
		try
		{
			Date d = gc.getTime();
			today = sdformat.format(d);
			if(		!StringUtils.isEmpty(requestData.getField("ACNO"))
				&& StringUtils.length(StringUtils.trim(requestData.getField("ACNO"))) == 11)
			{
				sAcno = requestData.getField("ACNO");
			}
			else
			{
				/********************************************************************
				 *  예수금잔고 존재여부 체크
				 ********************************************************************/
				IDataSet dsS002Out = null;
				
				dsS002Out = dh.selectSql(requestData,namespace+"."+"S002");
				
				logger.error("dsS002Out" + dsS002Out);
				if( dsS002Out.getField("ACNO") != null)
				{
					logger.error("계좌잔고 정보가 이미 존재합니다.");
					throw new Exception("계좌잔고 정보가 이미 존재합니다.");
				}
				
				/********************************************************************
				 *  계좌번호 채번
				 ********************************************************************/
				
				Map s001Mapout = null;
				s001Mapout = dh.selectOneSql(requestData, namespace+"."+"S001");
				
				logger.debug("s001Mapout" + s001Mapout);
				
				sAcno = String.valueOf(s001Mapout.get("ACNO"));
				
			}
			
			/********************************************************************
			 *  채번 및 잔고테이블 생성
			 ********************************************************************/
			IDataSet dsI000In = new DataSet();
			
			/*채번테이블  생성*/
			dsI000In.putField("ACNO", sAcno);
			dsI000In.putField("LAST_TR_NO", 0);
			dsI000In.putField("LAST_TR_DT", "00000000");
			
			
			rsCnt =  dh.insertSql(dsI000In, namespace+"."+"I001");
			
			if(rsCnt <=  0) {
				throw new Exception( namespace+"."+"I001"+" 처리 건수 없음.");
			}
			
			/*예수금잔고  생성*/
			
			dsI000In.putField("CUST_NM", requestData.getField("CUST_NM"));
			
			rsCnt =  dh.insertSql(dsI000In,namespace+"."+"I002");
			
			if(rsCnt <=  0) {
				throw new Exception( namespace+"."+"I002"+" 처리 건수 없음.");
			}
			/********************************************************************
			 *  예수금잔고조회
			 ********************************************************************/
			
			responseData = dh.selectSql(dsI000In,namespace+"."+"S002");
			
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
