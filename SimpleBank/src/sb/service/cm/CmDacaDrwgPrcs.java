package sb.service.cm;

import java.util.Map;

import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.util.StringUtils;

import org.apache.log4j.Logger;

import sb.common.DaoHandler;

public class CmDacaDrwgPrcs
{

	static Logger logger = Logger.getLogger(CmAcnoGen.class);
	private final String rpb1000Dft = "sb.repository.mapper.RPB1000_DefaultMapper";
	public IDataSet cmDacaDrwgPrcs(IDataSet requestData) throws Exception{
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
		DaoHandler dh = new DaoHandler();  /*DAO Handler*/
		Map s001MapOut = null;
		
		try
		{
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			initCheck(requestData);
			
			/********************************************************************
			 *  계좌잔고정보조회
			 ********************************************************************/
			s001MapOut = dh.selectOneSql(requestData, rpb1000Dft+"."+"S001");
			
			if( s001MapOut == null)
			{
				logger.error("계좌잔고 정보가 존재하지 않습니다.");
				throw new Exception("계좌잔고 정보가 존재하지 않습니다.");
			}
			/********************************************************************
			 *  예수금 출금반영 호출
			 ********************************************************************/
			CmDacaDrwgRfct ddr = new CmDacaDrwgRfct();  /*예수금 출금반영*/
			IDataSet ddrDsIn = new DataSet();
			IDataSet ddrDsOut = null; 
			
			ddrDsIn.putField("TR_DT", requestData.getField("TR_DT"));
			ddrDsIn.putField("ACNO", requestData.getField("ACNO"));
			ddrDsIn.putField("DRWG_AMT", requestData.getField("DRWG_AMT"));
			
			ddrDsOut = ddr.cmDacaDrwgRfct(ddrDsIn);
			
			/********************************************************************
			 *  거래내역반영 호출
			 ********************************************************************/
			CmTrDetlRfct tdr = new CmTrDetlRfct();  /*거래내역반영*/
			IDataSet tdrDsIn = new DataSet();
			
			tdrDsIn.putField("TR_DT", requestData.getField("TR_DT"));
			tdrDsIn.putField("ACNO", requestData.getField("ACNO"));
			tdrDsIn.putField("TR_NO", requestData.getLongField("TR_SN"));
			tdrDsIn.putField("SYNS_CD", requestData.getField("SYNS_CD"));
			tdrDsIn.putField("TR_AMT", requestData.getLongField("TR_AMT"));
			tdrDsIn.putField("BF_DACA", ddrDsOut.getLongField("BF_DACA"));
			tdrDsIn.putField("AF_DACA", ddrDsOut.getLongField("AF_DACA"));
			tdrDsIn.putField("STRT_TR_NO", requestData.getLongField("STRT_TR_NO"));
			tdrDsIn.putField("ORGN_TR_NO", 0);
			tdrDsIn.putField("CLNT_NM", requestData.getField("CLNT_NM"));
			
			tdr.cmTrDetlRfct(tdrDsIn);
			
			/*결과값 생성*/
			responseData.putField("BF_DACA", ddrDsOut.getLongField("BF_DACA"));
			responseData.putField("AF_DACA", ddrDsOut.getLongField("AF_DACA"));
			
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
	
		/*일자체크*/
		if( StringUtils.isEmpty(requestData.getField("TR_DT"))) {
			logger.error("거래일자를 확인하세요.");
			throw new Exception("거래일자를 확인하세요.");
		}
		
		/*계좌번호체크*/
		if( StringUtils.isEmpty(requestData.getField("ACNO"))
				|| StringUtils.length(requestData.getField("ACNO")) != 11) {
			logger.error("계좌번호를 확인하세요.");
			throw new Exception("계좌번호를 확인하세요.");
		}
		
		/*거래번호*/
		if( requestData.getLongField("TR_SN") <= 0) {
			logger.error("거래번호를 확인하세요.");
			throw new Exception("거래번호를 확인하세요.");
		}
		
		/*시작거래번호*/
		if( requestData.getLongField("STRT_TR_SN") <= 0) {
			logger.error("시작거래번호 확인하세요.");
			throw new Exception("시작거래번호 확인하세요.");
		}
		
		/*총거래금액체크*/
		if( requestData.getLongField("TOT_TR_AMT") <= 0) {
			logger.error("총거래금액 확인하세요.");
			throw new Exception("총거래금액 확인하세요.");
		}
		
		/*적요코드체크*/
		if( StringUtils.isEmpty(requestData.getField("SYNS_CD"))
				||StringUtils.length(requestData.getField("SYNS_CD")) != 3) {
			logger.error("적요코드를 확인하세요.");
			throw new Exception("적요코드를 확인하세요.");
		}
		
	}
}
