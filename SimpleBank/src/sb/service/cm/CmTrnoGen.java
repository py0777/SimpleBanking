package sb.service.cm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;



import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;
import sb.common.DaoHandler;

public class CmTrnoGen {
	static Logger logger = Logger.getLogger(CmAcnoGen.class);
	private final String namespace = "sb.repository.mapper.TrnoGenMapper";
	
	public IDataSet cmTrnoGen(IDataSet requestData) throws Exception{
		
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		Map s001MapOut = null;
		
		String today = "";
		long l_trno = 0;

		int    rsCnt = 0;  /*결과 건수*/
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		
		try
		{
			Date d = gc.getTime();
			today = sdformat.format(d);
			
			/*입력값 체크*/
			initCheck(requestData);
			
			/*일자체크*/
			if( !today.equals(requestData.getField("TR_DT"))) {
				throw new Exception("입력일자가 오늘이 아닙니다.");
			}
			
			/* 채번테이블 조회*/
			
			s001MapOut = dh.selectOneSql(requestData, namespace+"."+"S001");
			
			/*채번 값 설정*/
			if(today.compareTo(String.valueOf(s001MapOut.get("LAST_TR_DT"))) < 0) {
				throw new Exception("최종거래일자가 당일보다 큽니다.");
			}
			
			if(today.compareTo(String.valueOf(s001MapOut.get("LAST_TRDT"))) > 0) {
				l_trno = 1;
			}
			else{
				
				l_trno = (Long)s001MapOut.get("LAST_TRNO") + 1;
			}
			/*채번테이블 갱신*/
			rsCnt = dh.updateSql(requestData, namespace+"."+"U001");
	
			if(rsCnt <=  0) {
				throw new Exception( namespace+"."+"U001"+" 처리 건수 없음.");
			}
			responseData.putField("TRNO", l_trno);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
		
		/*************************************************************
		 * Retrun Result Data
		 *************************************************************/
		
		responseData.setOkResultMessage("OK", new String[]{"처리완료되었습니다."});
		
		return responseData;
		
		
	}
	
	/*initCheck*/
	private void initCheck(IDataSet requestData) throws Exception {
		
		try {
			/*일자체크*/
			if( StringUtils.isEmpty(requestData.getField("TR_DT"))) {
				throw new Exception("입력일자를 확인하세요.");
			}
			
			/*계좌번호체크*/
			if( StringUtils.isEmpty(requestData.getField("ACNO"))
					|| StringUtils.length(requestData.getField("ACNO")) != 11) {
				throw new Exception("계좌번호를 확인하세요.");
			}
		}catch(Exception e){
			throw e;
		}
		
	}
	
}
