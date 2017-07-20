package sb.service.cm;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;

import org.apache.log4j.Logger;

import sb.common.DaoHandler;

public class CmAcnoDacaInqr
{
	static Logger logger = Logger.getLogger(CmAcnoGen.class);
	private final String rpb1000Dft = "sb.repository.mapper.RPB1000_DefaultMapper";
	
	public IDataSet cmAcnoDacaInqr(IDataSet requestData)throws Exception{
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		IDataSet dSS001Out = null;
		
		try
		{
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			initCheck(requestData);
			
			/********************************************************************
			 *  계좌잔고정보조회
			 ********************************************************************/
			dSS001Out = dh.selectOneSql(requestData, rpb1000Dft+"."+"S001");
			
			if( dSS001Out == null)
			{
				logger.error("계좌잔고 정보가 존재하지 않습니다.");
				throw new Exception("계좌잔고 정보가 존재하지 않습니다.");
			}
			
			/********************************************************************
			 *  결과값 설정
			 ********************************************************************/
			responseData = dSS001Out;
			
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
		/*************************************************************
		 * Retrun Result Data
		 *************************************************************/
		
		responseData.setOkResultMessage("OK", new String[]{"조회완료되었습니다."});
		
		return responseData;
	}
	
	/*initCheck*/
	private void initCheck(IDataSet requestData) throws Exception {
	
		
		/*계좌번호체크*/
		if( StringUtils.isEmpty(requestData.getField("ACNO"))
				|| StringUtils.length(requestData.getField("ACNO")) != 11) {
			logger.error("계좌번호를 확인하세요.");
			throw new Exception("계좌번호를 확인하세요.");
		}
		
	}
}
