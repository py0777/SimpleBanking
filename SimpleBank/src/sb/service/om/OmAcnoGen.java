package sb.service.om;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;

import org.apache.log4j.Logger;

import sb.service.cm.CmAcnoGen;

public class OmAcnoGen
{
static Logger logger = Logger.getLogger(CmAcnoGen.class);
	
	public IDataSet cmGenAcno(IDataSet requestData)throws Exception{
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		
		CmAcnoGen cmAcnoGen = new CmAcnoGen();
		
		try
		{
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			initCheck(requestData);
			
			/********************************************************************
			 *  계좌잔고정보조회
			 ********************************************************************/
			responseData = cmAcnoGen.cmAcnoGen(requestData);
			
			
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
		logger.debug("[Start] initCheck");
		
		/*계좌번호체크*/
		if( !StringUtils.isEmpty(requestData.getField("ACNO"))
				&& StringUtils.length(requestData.getField("ACNO")) != 11) {
			logger.error("계좌번호를 확인하세요.");
			throw new Exception("계좌번호를 확인하세요.");
		}
		
	}
}