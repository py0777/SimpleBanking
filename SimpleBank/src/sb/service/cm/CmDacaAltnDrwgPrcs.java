package sb.service.cm;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;

import org.apache.log4j.Logger;

import sb.common.DaoHandler;

public class CmDacaAltnDrwgPrcs
{
	static Logger logger = Logger.getLogger(CmAcnoGen.class);
	private final String rpb1000Dft = "sb.repository.mapper.RPB1000_DefaultMapper";
	public IDataSet cmDacaAltnDrwgPrcs(IDataSet requestData) throws Exception{
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		try
		{
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			initCheck(requestData);
			
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
		logger.debug("[Start] initCheck");
		
		/*일자체크*/
		if( StringUtils.isEmpty(requestData.getField("TR_DT"))) {
			logger.error("거래일자를 확인하세요.");
			throw new Exception("거래일자를 확인하세요.");
		}
		
		/*출금계좌번호체크*/
		if( StringUtils.isEmpty(requestData.getField("DRWG_ACNO"))
				|| StringUtils.length(requestData.getField("DRWG_ACNO")) != 11) {
			logger.error("출금계좌번호를 확인하세요.");
			throw new Exception("출금계좌번호를 확인하세요.");
		}
		
		/*출금거래번호*/
		if( requestData.getLongField("DRWG_TR_SN") <= 0) {
			logger.error("거래번호를 확인하세요.");
			throw new Exception("거래번호를 확인하세요.");
		}
	
		/*입금계좌번호체크*/
		if( StringUtils.isEmpty(requestData.getField("RCTM_ACNO"))
				|| StringUtils.length(requestData.getField("RCTM_ACNO")) != 11) {
			logger.error("입금계좌번호를 확인하세요.");
			throw new Exception("입금계좌번호를 확인하세요.");
		}
		
		/*입금거래번호*/
		if( requestData.getLongField("RCTM_TR_SN") <= 0) {
			logger.error("입금거래번호를 확인하세요.");
			throw new Exception("입금거래번호를 확인하세요.");
		}

		/*대체금액*/
		if( requestData.getLongField("ATLN_AMT") <= 0) {
			logger.error("대체금액을 확인하세요.");
			throw new Exception("대체금액을 확인하세요.");
		}
		
		/*적요코드체크*/
		if( StringUtils.isEmpty(requestData.getField("DRWG_SYNS_CD"))
				||StringUtils.length(requestData.getField("DRWG_SYNS_CD")) != 3) {
			logger.error("출금적요코드를 확인하세요.");
			throw new Exception("출금적요코드를 확인하세요.");
		}
		
		/*적요코드체크*/
		if( StringUtils.isEmpty(requestData.getField("RCTM_SYNS_CD"))
				||StringUtils.length(requestData.getField("RCTM_SYNS_CD")) != 3) {
			logger.error("입금적요코드를 확인하세요.");
			throw new Exception("입금적요코드를 확인하세요.");
		}
	}
}
