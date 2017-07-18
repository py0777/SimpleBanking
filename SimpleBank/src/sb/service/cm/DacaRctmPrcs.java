package sb.service.cm;

import org.apache.log4j.Logger;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;
import sb.common.DaoHandler;

public class DacaRctmPrcs {

	static Logger logger = Logger.getLogger(AcnoGen.class);
	private final String namespace = "sb.repository.mapper.DacaRctmPrcsMapper";
	private final String rpb1000Dft = "sb.repository.mapper.RPB1000_DefaultMapper";
	public IDataSet asDacaRctmPrcs(IDataSet requestData) throws Exception{
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		IDataSet dsTbl = null;
		

		try
		{
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			initCheck(requestData);
			
			/*계좌잔고정보조회*/
			
			dsTbl = dh.selectOneSql(requestData, rpb1000Dft+"."+"S001");
			
			
			/*예수금 입금반영 호출*/
			DacaRctmRfct drr = new DacaRctmRfct();  /*DacaRctmRfct*/
			IDataSet drrDs = new DataSet();
			
			drrDs.putField("TR_DT", requestData.getField("TR_DT"));
			drrDs.putField("ACNO", requestData.getField("ACNO"));
			drrDs.putField("RCTM_AMT", requestData.getField("RCTM_AMT"));
			
			drr.asDacaRctmRfct(drrDs);
			
			/*거래내역반영 호출*/
			
			/*결과값 생성*/
			
			
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
			
			/*거래번호*/
			if( requestData.getLongField("TR_SN") <= 0) {
				throw new Exception("거래번호 확인하세요.");
			}
			
			/*시작거래번호*/
			if( requestData.getLongField("STRT_TR_SN") <= 0) {
				throw new Exception("시작거래번호 확인하세요.");
			}
			
			/*총거래금액체크*/
			if( requestData.getLongField("TOT_TR_AMT") <= 0) {
				throw new Exception("총거래금액 확인하세요.");
			}
			
			/*적요코드체크*/
			if( StringUtils.isEmpty(requestData.getField("SYNS_CD"))
					||StringUtils.length(requestData.getField("SYNS_CD")) != 3) {
				throw new Exception("적요코드를 확인하세요.");
			}
		}catch(Exception e){
			throw e;
		}
		
	}
}
