package sb.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;
import sb.common.DaoHandler;

public class DacaRctmRfct {

	static Logger logger = Logger.getLogger(AcnoGen.class);
	private final String namespace = "sb.repository.mapper.DacaRctmRfctMapper";
	
	public IDataSet asDacaRctmRfct(IDataSet requestData) throws Exception{
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		IDataSet dsTbl = null;
		
		IDataSet dsU001 = new DataSet();
		long l_bfDaca = 0;  /*이전예수금*/
		long l_afDaca = 0;  /*이전예수금*/
		
		int    rsCnt = 0;  /*결과 건수*/

		try
		{
			/*입력값 체크*/
			initCheck(requestData);
			
			/*1. 거래전 계좌잔고 조회 */
			dsTbl = dh.selectOneSql(requestData, namespace+"."+"S001");
			
			logger.debug("거래전 계좌잔고 조회 : " + dsTbl);
			
			l_bfDaca = dsTbl.getLongField("DACA");
			/*2. 계좌잔고 갱신 */
			if(l_bfDaca + requestData.getLongField("RCTM_AMT") < 0) {
				logger.error("예수금이 음수입니다.");
				throw new Exception( "예수금이 음수입니다.");
			}
			
			dsU001.putField("DACA", l_bfDaca + requestData.getLongField("RCTM_AMT"));
			
			rsCnt = dh.updateSql(dsU001, namespace+"."+"U001");
			
			if(rsCnt <=  0) {
				throw new Exception( namespace+"."+"U001"+" 처리 건수 없음.");
			}
			/*3. 거래후 계좌잔고 조회 */
			dsTbl = dh.selectOneSql(requestData, namespace+"."+"S001");
			
			logger.debug("거래후 계좌잔고 조회 : " + dsTbl);
			l_afDaca = dsTbl.getLongField("DACA");
			
			/*4. 결과값 생성*/
			responseData.putField("BF_DACA", l_bfDaca);
			responseData.putField("AF_DACA", l_afDaca);
			
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
				throw new Exception("거래일자를 확인하세요.");
			}
			
			/*계좌번호체크*/
			if( StringUtils.isEmpty(requestData.getField("ACNO"))
					|| StringUtils.length(requestData.getField("ACNO")) != 10) {
				throw new Exception("계좌번호를 확인하세요.");
			}
			
			/*입금금액체크*/
			if( requestData.getLongField("RCTM_AMT") <= 0) {
				throw new Exception("입금금액 확인하세요.");
			}
		}catch(Exception e){
			throw e;
		}
		
	}
}
